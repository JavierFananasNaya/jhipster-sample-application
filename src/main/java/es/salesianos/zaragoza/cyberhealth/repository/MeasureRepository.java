package es.salesianos.zaragoza.cyberhealth.repository;

import es.salesianos.zaragoza.cyberhealth.domain.Measure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Measure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasureRepository extends JpaRepository<Measure, Long> {}
