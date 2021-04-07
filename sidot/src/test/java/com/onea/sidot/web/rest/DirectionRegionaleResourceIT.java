package com.onea.sidot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.onea.sidot.IntegrationTest;
import com.onea.sidot.domain.DirectionRegionale;
import com.onea.sidot.repository.DirectionRegionaleRepository;
import com.onea.sidot.service.EntityManager;
import com.onea.sidot.service.dto.DirectionRegionaleDTO;
import com.onea.sidot.service.mapper.DirectionRegionaleMapper;
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
 * Integration tests for the {@link DirectionRegionaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DirectionRegionaleResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/direction-regionales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirectionRegionaleRepository directionRegionaleRepository;

    @Autowired
    private DirectionRegionaleMapper directionRegionaleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DirectionRegionale directionRegionale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectionRegionale createEntity(EntityManager em) {
        DirectionRegionale directionRegionale = new DirectionRegionale()
            .libelle(DEFAULT_LIBELLE)
            .responsable(DEFAULT_RESPONSABLE)
            .contact(DEFAULT_CONTACT);
        return directionRegionale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectionRegionale createUpdatedEntity(EntityManager em) {
        DirectionRegionale directionRegionale = new DirectionRegionale()
            .libelle(UPDATED_LIBELLE)
            .responsable(UPDATED_RESPONSABLE)
            .contact(UPDATED_CONTACT);
        return directionRegionale;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DirectionRegionale.class).block();
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
        directionRegionale = createEntity(em);
    }

    @Test
    void createDirectionRegionale() throws Exception {
        int databaseSizeBeforeCreate = directionRegionaleRepository.findAll().collectList().block().size();
        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeCreate + 1);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    void createDirectionRegionaleWithExistingId() throws Exception {
        // Create the DirectionRegionale with an existing ID
        directionRegionale.setId(1L);
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        int databaseSizeBeforeCreate = directionRegionaleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRegionaleRepository.findAll().collectList().block().size();
        // set the field null
        directionRegionale.setLibelle(null);

        // Create the DirectionRegionale, which fails.
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRegionaleRepository.findAll().collectList().block().size();
        // set the field null
        directionRegionale.setResponsable(null);

        // Create the DirectionRegionale, which fails.
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRegionaleRepository.findAll().collectList().block().size();
        // set the field null
        directionRegionale.setContact(null);

        // Create the DirectionRegionale, which fails.
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDirectionRegionales() {
        // Initialize the database
        directionRegionaleRepository.save(directionRegionale).block();

        // Get all the directionRegionaleList
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
            .value(hasItem(directionRegionale.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].responsable")
            .value(hasItem(DEFAULT_RESPONSABLE))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT));
    }

    @Test
    void getDirectionRegionale() {
        // Initialize the database
        directionRegionaleRepository.save(directionRegionale).block();

        // Get the directionRegionale
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, directionRegionale.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(directionRegionale.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.responsable")
            .value(is(DEFAULT_RESPONSABLE))
            .jsonPath("$.contact")
            .value(is(DEFAULT_CONTACT));
    }

    @Test
    void getNonExistingDirectionRegionale() {
        // Get the directionRegionale
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDirectionRegionale() throws Exception {
        // Initialize the database
        directionRegionaleRepository.save(directionRegionale).block();

        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();

        // Update the directionRegionale
        DirectionRegionale updatedDirectionRegionale = directionRegionaleRepository.findById(directionRegionale.getId()).block();
        updatedDirectionRegionale.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(updatedDirectionRegionale);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, directionRegionaleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    void putNonExistingDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, directionRegionaleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDirectionRegionaleWithPatch() throws Exception {
        // Initialize the database
        directionRegionaleRepository.save(directionRegionale).block();

        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();

        // Update the directionRegionale using partial update
        DirectionRegionale partialUpdatedDirectionRegionale = new DirectionRegionale();
        partialUpdatedDirectionRegionale.setId(directionRegionale.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDirectionRegionale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectionRegionale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    void fullUpdateDirectionRegionaleWithPatch() throws Exception {
        // Initialize the database
        directionRegionaleRepository.save(directionRegionale).block();

        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();

        // Update the directionRegionale using partial update
        DirectionRegionale partialUpdatedDirectionRegionale = new DirectionRegionale();
        partialUpdatedDirectionRegionale.setId(directionRegionale.getId());

        partialUpdatedDirectionRegionale.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDirectionRegionale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectionRegionale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
        DirectionRegionale testDirectionRegionale = directionRegionaleList.get(directionRegionaleList.size() - 1);
        assertThat(testDirectionRegionale.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDirectionRegionale.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testDirectionRegionale.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    void patchNonExistingDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, directionRegionaleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDirectionRegionale() throws Exception {
        int databaseSizeBeforeUpdate = directionRegionaleRepository.findAll().collectList().block().size();
        directionRegionale.setId(count.incrementAndGet());

        // Create the DirectionRegionale
        DirectionRegionaleDTO directionRegionaleDTO = directionRegionaleMapper.toDto(directionRegionale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(directionRegionaleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DirectionRegionale in the database
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDirectionRegionale() {
        // Initialize the database
        directionRegionaleRepository.save(directionRegionale).block();

        int databaseSizeBeforeDelete = directionRegionaleRepository.findAll().collectList().block().size();

        // Delete the directionRegionale
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, directionRegionale.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DirectionRegionale> directionRegionaleList = directionRegionaleRepository.findAll().collectList().block();
        assertThat(directionRegionaleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
