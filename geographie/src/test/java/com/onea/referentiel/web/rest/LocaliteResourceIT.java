package com.onea.referentiel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.referentiel.IntegrationTest;
import com.onea.referentiel.domain.Localite;
import com.onea.referentiel.repository.LocaliteRepository;
import com.onea.referentiel.service.dto.LocaliteDTO;
import com.onea.referentiel.service.mapper.LocaliteMapper;
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
 * Integration tests for the {@link LocaliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocaliteResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/localites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocaliteRepository localiteRepository;

    @Autowired
    private LocaliteMapper localiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocaliteMockMvc;

    private Localite localite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localite createEntity(EntityManager em) {
        Localite localite = new Localite().libelle(DEFAULT_LIBELLE);
        return localite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localite createUpdatedEntity(EntityManager em) {
        Localite localite = new Localite().libelle(UPDATED_LIBELLE);
        return localite;
    }

    @BeforeEach
    public void initTest() {
        localite = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalite() throws Exception {
        int databaseSizeBeforeCreate = localiteRepository.findAll().size();
        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);
        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isCreated());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeCreate + 1);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createLocaliteWithExistingId() throws Exception {
        // Create the Localite with an existing ID
        localite.setId(1L);
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        int databaseSizeBeforeCreate = localiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = localiteRepository.findAll().size();
        // set the field null
        localite.setLibelle(null);

        // Create the Localite, which fails.
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        restLocaliteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isBadRequest());

        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalites() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get all the localiteList
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localite.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        // Get the localite
        restLocaliteMockMvc
            .perform(get(ENTITY_API_URL_ID, localite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localite.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingLocalite() throws Exception {
        // Get the localite
        restLocaliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite
        Localite updatedLocalite = localiteRepository.findById(localite.getId()).get();
        // Disconnect from session so that the updates on updatedLocalite are not directly saved in db
        em.detach(updatedLocalite);
        updatedLocalite.libelle(UPDATED_LIBELLE);
        LocaliteDTO localiteDTO = localiteMapper.toDto(updatedLocalite);

        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(localiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocaliteWithPatch() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite using partial update
        Localite partialUpdatedLocalite = new Localite();
        partialUpdatedLocalite.setId(localite.getId());

        partialUpdatedLocalite.libelle(UPDATED_LIBELLE);

        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateLocaliteWithPatch() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();

        // Update the localite using partial update
        Localite partialUpdatedLocalite = new Localite();
        partialUpdatedLocalite.setId(localite.getId());

        partialUpdatedLocalite.libelle(UPDATED_LIBELLE);

        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalite))
            )
            .andExpect(status().isOk());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
        Localite testLocalite = localiteList.get(localiteList.size() - 1);
        assertThat(testLocalite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localiteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalite() throws Exception {
        int databaseSizeBeforeUpdate = localiteRepository.findAll().size();
        localite.setId(count.incrementAndGet());

        // Create the Localite
        LocaliteDTO localiteDTO = localiteMapper.toDto(localite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocaliteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(localiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localite in the database
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalite() throws Exception {
        // Initialize the database
        localiteRepository.saveAndFlush(localite);

        int databaseSizeBeforeDelete = localiteRepository.findAll().size();

        // Delete the localite
        restLocaliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, localite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Localite> localiteList = localiteRepository.findAll();
        assertThat(localiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
