package com.onea.referentiel.repository;

import com.onea.referentiel.domain.Parcelle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Parcelle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcelleRepository extends JpaRepository<Parcelle, Long> {}
