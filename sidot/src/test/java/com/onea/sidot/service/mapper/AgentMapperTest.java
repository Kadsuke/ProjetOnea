package com.onea.sidot.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgentMapperTest {

    private AgentMapper agentMapper;

    @BeforeEach
    public void setUp() {
        agentMapper = new AgentMapperImpl();
    }
}
