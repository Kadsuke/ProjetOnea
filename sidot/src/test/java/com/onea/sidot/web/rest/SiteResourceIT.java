package com.onea.sidot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.onea.sidot.IntegrationTest;
import com.onea.sidot.domain.Site;
import com.onea.sidot.repository.SiteRepository;
import com.onea.sidot.service.EntityManager;
import com.onea.sidot.service.dto.SiteDTO;
import com.onea.sidot.service.mapper.SiteMapper;
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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Site site;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity(EntityManager em) {
        Site site = new Site().libelle(DEFAULT_LIBELLE).responsable(DEFAULT_RESPONSABLE).contact(DEFAULT_CONTACT);
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity(EntityManager em) {
        Site site = new Site().libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        return site;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Site.class).block();
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
        site = createEntity(em);
    }

    @Test
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().collectList().block().size();
        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);
        SiteDTO siteDTO = siteMapper.toDto(site);

        int databaseSizeBeforeCreate = siteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().collectList().block().size();
        // set the field null
        site.setLibelle(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().collectList().block().size();
        // set the field null
        site.setResponsable(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().collectList().block().size();
        // set the field null
        site.setContact(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSites() {
        // Initialize the database
        siteRepository.save(site).block();

        // Get all the siteList
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
            .value(hasItem(site.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].responsable")
            .value(hasItem(DEFAULT_RESPONSABLE))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT));
    }

    @Test
    void getSite() {
        // Initialize the database
        siteRepository.save(site).block();

        // Get the site
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, site.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(site.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.responsable")
            .value(is(DEFAULT_RESPONSABLE))
            .jsonPath("$.contact")
            .value(is(DEFAULT_CONTACT));
    }

    @Test
    void getNonExistingSite() {
        // Get the site
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSite() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).block();
        updatedSite.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        SiteDTO siteDTO = siteMapper.toDto(updatedSite);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSite() {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeDelete = siteRepository.findAll().collectList().block().size();

        // Delete the site
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, site.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
