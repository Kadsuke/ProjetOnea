package com.sidot.gestioneau.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sidot.gestioneau.IntegrationTest;
import com.sidot.gestioneau.domain.Site;
import com.sidot.gestioneau.repository.SiteRepository;
import com.sidot.gestioneau.service.dto.SiteDTO;
import com.sidot.gestioneau.service.mapper.SiteMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restSiteMockMvc;

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

    @BeforeEach
    public void initTest() {
        site = createEntity(em);
    }

    @Test
    @Transactional
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();
        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isCreated());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);
        SiteDTO siteDTO = siteMapper.toDto(site);

        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setLibelle(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setResponsable(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setContact(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSites() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).get();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);
        SiteDTO siteDTO = siteMapper.toDto(updatedSite);

        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.libelle(UPDATED_LIBELLE).responsable(UPDATED_RESPONSABLE).contact(UPDATED_CONTACT);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
        assertThat(testSite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSite.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testSite.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();
        site.setId(count.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(siteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        int databaseSizeBeforeDelete = siteRepository.findAll().size();

        // Delete the site
        restSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, site.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
