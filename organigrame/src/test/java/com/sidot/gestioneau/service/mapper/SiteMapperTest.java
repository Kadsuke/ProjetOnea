package com.sidot.gestioneau.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SiteMapperTest {

    private SiteMapper siteMapper;

    @BeforeEach
    public void setUp() {
        siteMapper = new SiteMapperImpl();
    }
}
