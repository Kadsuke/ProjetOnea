package com.onea.sidot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.onea.sidot.IntegrationTest;
import com.onea.sidot.domain.Province;
import com.onea.sidot.repository.ProvinceRepository;
import com.onea.sidot.service.EntityManager;
import com.onea.sidot.service.dto.ProvinceDTO;
import com.onea.sidot.service.mapper.ProvinceMapper;
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
 * Integration tests for the {@link ProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ProvinceResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Province province;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createEntity(EntityManager em) {
        Province province = new Province().libelle(DEFAULT_LIBELLE);
        return province;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Province createUpdatedEntity(EntityManager em) {
        Province province = new Province().libelle(UPDATED_LIBELLE);
        return province;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Province.class).block();
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
        province = createEntity(em);
    }

    @Test
    void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().collectList().block().size();
        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    void createProvinceWithExistingId() throws Exception {
        // Create the Province with an existing ID
        province.setId(1L);
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        int databaseSizeBeforeCreate = provinceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = provinceRepository.findAll().collectList().block().size();
        // set the field null
        province.setLibelle(null);

        // Create the Province, which fails.
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProvinces() {
        // Initialize the database
        provinceRepository.save(province).block();

        // Get all the provinceList
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
            .value(hasItem(province.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE));
    }

    @Test
    void getProvince() {
        // Initialize the database
        provinceRepository.save(province).block();

        // Get the province
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, province.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(province.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE));
    }

    @Test
    void getNonExistingProvince() {
        // Get the province
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProvince() throws Exception {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();

        // Update the province
        Province updatedProvince = provinceRepository.findById(province.getId()).block();
        updatedProvince.libelle(UPDATED_LIBELLE);
        ProvinceDTO provinceDTO = provinceMapper.toDto(updatedProvince);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, provinceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    void putNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, provinceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.libelle(UPDATED_LIBELLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    void fullUpdateProvinceWithPatch() throws Exception {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();

        // Update the province using partial update
        Province partialUpdatedProvince = new Province();
        partialUpdatedProvince.setId(province.getId());

        partialUpdatedProvince.libelle(UPDATED_LIBELLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProvince.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProvince))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinceList.get(provinceList.size() - 1);
        assertThat(testProvince.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    void patchNonExistingProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, provinceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProvince() throws Exception {
        int databaseSizeBeforeUpdate = provinceRepository.findAll().collectList().block().size();
        province.setId(count.incrementAndGet());

        // Create the Province
        ProvinceDTO provinceDTO = provinceMapper.toDto(province);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(provinceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Province in the database
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProvince() {
        // Initialize the database
        provinceRepository.save(province).block();

        int databaseSizeBeforeDelete = provinceRepository.findAll().collectList().block().size();

        // Delete the province
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, province.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Province> provinceList = provinceRepository.findAll().collectList().block();
        assertThat(provinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
