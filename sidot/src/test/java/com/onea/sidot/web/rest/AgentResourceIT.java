package com.onea.sidot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.onea.sidot.IntegrationTest;
import com.onea.sidot.domain.Agent;
import com.onea.sidot.repository.AgentRepository;
import com.onea.sidot.service.EntityManager;
import com.onea.sidot.service.dto.AgentDTO;
import com.onea.sidot.service.mapper.AgentMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AgentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AgentResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Agent agent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agent createEntity(EntityManager em) {
        Agent agent = new Agent().nom(DEFAULT_NOM).numero(DEFAULT_NUMERO).role(DEFAULT_ROLE);
        return agent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agent createUpdatedEntity(EntityManager em) {
        Agent agent = new Agent().nom(UPDATED_NOM).numero(UPDATED_NUMERO).role(UPDATED_ROLE);
        return agent;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Agent.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        agent = createEntity(em);
    }

    @Test
    void createAgent() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().collectList().block().size();
        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate + 1);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAgent.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testAgent.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    void createAgentWithExistingId() throws Exception {
        // Create the Agent with an existing ID
        agent.setId(1L);
        AgentDTO agentDTO = agentMapper.toDto(agent);

        int databaseSizeBeforeCreate = agentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().collectList().block().size();
        // set the field null
        agent.setNom(null);

        // Create the Agent, which fails.
        AgentDTO agentDTO = agentMapper.toDto(agent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().collectList().block().size();
        // set the field null
        agent.setNumero(null);

        // Create the Agent, which fails.
        AgentDTO agentDTO = agentMapper.toDto(agent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = agentRepository.findAll().collectList().block().size();
        // set the field null
        agent.setRole(null);

        // Create the Agent, which fails.
        AgentDTO agentDTO = agentMapper.toDto(agent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAgents() {
        // Initialize the database
        agentRepository.save(agent).block();

        // Get all the agentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(agent.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].numero")
            .value(hasItem(DEFAULT_NUMERO))
            .jsonPath("$.[*].role")
            .value(hasItem(DEFAULT_ROLE));
    }

    @Test
    void getAgent() {
        // Initialize the database
        agentRepository.save(agent).block();

        // Get the agent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, agent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(agent.getId().intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.numero")
            .value(is(DEFAULT_NUMERO))
            .jsonPath("$.role")
            .value(is(DEFAULT_ROLE));
    }

    @Test
    void getNonExistingAgent() {
        // Get the agent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAgent() throws Exception {
        // Initialize the database
        agentRepository.save(agent).block();

        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();

        // Update the agent
        Agent updatedAgent = agentRepository.findById(agent.getId()).block();
        updatedAgent.nom(UPDATED_NOM).numero(UPDATED_NUMERO).role(UPDATED_ROLE);
        AgentDTO agentDTO = agentMapper.toDto(updatedAgent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, agentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgent.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testAgent.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void putNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, agentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAgentWithPatch() throws Exception {
        // Initialize the database
        agentRepository.save(agent).block();

        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();

        // Update the agent using partial update
        Agent partialUpdatedAgent = new Agent();
        partialUpdatedAgent.setId(agent.getId());

        partialUpdatedAgent.nom(UPDATED_NOM).role(UPDATED_ROLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAgent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAgent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgent.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testAgent.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void fullUpdateAgentWithPatch() throws Exception {
        // Initialize the database
        agentRepository.save(agent).block();

        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();

        // Update the agent using partial update
        Agent partialUpdatedAgent = new Agent();
        partialUpdatedAgent.setId(agent.getId());

        partialUpdatedAgent.nom(UPDATED_NOM).numero(UPDATED_NUMERO).role(UPDATED_ROLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAgent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAgent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAgent.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testAgent.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    void patchNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, agentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().collectList().block().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(agentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAgent() {
        // Initialize the database
        agentRepository.save(agent).block();

        int databaseSizeBeforeDelete = agentRepository.findAll().collectList().block().size();

        // Delete the agent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, agent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Agent> agentList = agentRepository.findAll().collectList().block();
        assertThat(agentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
