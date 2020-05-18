package es.salesianos.zaragoza.cyberhealth.repository;

import es.salesianos.zaragoza.cyberhealth.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
