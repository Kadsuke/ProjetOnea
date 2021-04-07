package com.onea.referentiel.repository;

import com.onea.referentiel.domain.Province;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Province entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {}
