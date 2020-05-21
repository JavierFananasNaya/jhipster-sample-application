package es.salesianos.zaragoza.cyberhealth.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.salesianos.zaragoza.cyberhealth.Covid19TesterApp;
import es.salesianos.zaragoza.cyberhealth.domain.Measure;
import es.salesianos.zaragoza.cyberhealth.domain.enumeration.SYMPTOMS;
import es.salesianos.zaragoza.cyberhealth.repository.MeasureRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MeasureResource} REST controller.
 */
@SpringBootTest(classes = Covid19TesterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MeasureResourceIT {
    private static final Float DEFAULT_TEMPERATURE = 1F;
    private static final Float UPDATED_TEMPERATURE = 2F;

    private static final SYMPTOMS DEFAULT_SYMPTOM = SYMPTOMS.NONE;
    private static final SYMPTOMS UPDATED_SYMPTOM = SYMPTOMS.DISNEA;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Boolean DEFAULT_CONTACT_WITH_INFECTED = false;
    private static final Boolean UPDATED_CONTACT_WITH_INFECTED = true;

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeasureMockMvc;

    private Measure measure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measure createEntity(EntityManager em) {
        Measure measure = new Measure()
            .temperature(DEFAULT_TEMPERATURE)
            .symptom(DEFAULT_SYMPTOM)
            .duration(DEFAULT_DURATION)
            .contactWithInfected(DEFAULT_CONTACT_WITH_INFECTED);
        return measure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measure createUpdatedEntity(EntityManager em) {
        Measure measure = new Measure()
            .temperature(UPDATED_TEMPERATURE)
            .symptom(UPDATED_SYMPTOM)
            .duration(UPDATED_DURATION)
            .contactWithInfected(UPDATED_CONTACT_WITH_INFECTED);
        return measure;
    }

    @BeforeEach
    public void initTest() {
        measure = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasure() throws Exception {
        int databaseSizeBeforeCreate = measureRepository.findAll().size();
        // Create the Measure
        restMeasureMockMvc
            .perform(post("/api/measures").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isCreated());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeCreate + 1);
        Measure testMeasure = measureList.get(measureList.size() - 1);
        assertThat(testMeasure.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testMeasure.getSymptom()).isEqualTo(DEFAULT_SYMPTOM);
        assertThat(testMeasure.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testMeasure.isContactWithInfected()).isEqualTo(DEFAULT_CONTACT_WITH_INFECTED);
    }

    @Test
    @Transactional
    public void createMeasureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measureRepository.findAll().size();

        // Create the Measure with an existing ID
        measure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasureMockMvc
            .perform(post("/api/measures").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isBadRequest());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeasures() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList
        restMeasureMockMvc
            .perform(get("/api/measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measure.getId().intValue())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].symptom").value(hasItem(DEFAULT_SYMPTOM.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].contactWithInfected").value(hasItem(DEFAULT_CONTACT_WITH_INFECTED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get the measure
        restMeasureMockMvc
            .perform(get("/api/measures/{id}", measure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(measure.getId().intValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.symptom").value(DEFAULT_SYMPTOM.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.contactWithInfected").value(DEFAULT_CONTACT_WITH_INFECTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMeasure() throws Exception {
        // Get the measure
        restMeasureMockMvc.perform(get("/api/measures/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        int databaseSizeBeforeUpdate = measureRepository.findAll().size();

        // Update the measure
        Measure updatedMeasure = measureRepository.findById(measure.getId()).get();
        // Disconnect from session so that the updates on updatedMeasure are not directly saved in db
        em.detach(updatedMeasure);
        updatedMeasure
            .temperature(UPDATED_TEMPERATURE)
            .symptom(UPDATED_SYMPTOM)
            .duration(UPDATED_DURATION)
            .contactWithInfected(UPDATED_CONTACT_WITH_INFECTED);

        restMeasureMockMvc
            .perform(
                put("/api/measures").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedMeasure))
            )
            .andExpect(status().isOk());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeUpdate);
        Measure testMeasure = measureList.get(measureList.size() - 1);
        assertThat(testMeasure.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testMeasure.getSymptom()).isEqualTo(UPDATED_SYMPTOM);
        assertThat(testMeasure.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMeasure.isContactWithInfected()).isEqualTo(UPDATED_CONTACT_WITH_INFECTED);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasure() throws Exception {
        int databaseSizeBeforeUpdate = measureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureMockMvc
            .perform(put("/api/measures").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isBadRequest());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        int databaseSizeBeforeDelete = measureRepository.findAll().size();

        // Delete the measure
        restMeasureMockMvc
            .perform(delete("/api/measures/{id}", measure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
