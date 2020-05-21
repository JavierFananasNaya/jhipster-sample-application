package es.salesianos.zaragoza.cyberhealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.salesianos.zaragoza.cyberhealth.domain.enumeration.SYMPTOMS;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Measure.
 */
@Entity
@Table(name = "measure")
public class Measure implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temperature")
    private Float temperature;

    @Enumerated(EnumType.STRING)
    @Column(name = "symptom")
    private SYMPTOMS symptom;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "contact_with_infected")
    private Boolean contactWithInfected;

    @ManyToOne
    @JsonIgnoreProperties(value = "measures", allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Measure temperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public SYMPTOMS getSymptom() {
        return symptom;
    }

    public Measure symptom(SYMPTOMS symptom) {
        this.symptom = symptom;
        return this;
    }

    public void setSymptom(SYMPTOMS symptom) {
        this.symptom = symptom;
    }

    public Integer getDuration() {
        return duration;
    }

    public Measure duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean isContactWithInfected() {
        return contactWithInfected;
    }

    public Measure contactWithInfected(Boolean contactWithInfected) {
        this.contactWithInfected = contactWithInfected;
        return this;
    }

    public void setContactWithInfected(Boolean contactWithInfected) {
        this.contactWithInfected = contactWithInfected;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Measure employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Measure)) {
            return false;
        }
        return id != null && id.equals(((Measure) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Measure{" +
            "id=" + getId() +
            ", temperature=" + getTemperature() +
            ", symptom='" + getSymptom() + "'" +
            ", duration=" + getDuration() +
            ", contactWithInfected='" + isContactWithInfected() + "'" +
            "}";
    }
}
