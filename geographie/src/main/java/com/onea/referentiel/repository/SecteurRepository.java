package com.onea.referentiel.repository;

import com.onea.referentiel.domain.Secteur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Secteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecteurRepository extends JpaRepository<Secteur, Long> {}
