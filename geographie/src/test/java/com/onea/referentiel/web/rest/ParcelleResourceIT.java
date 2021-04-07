package com.onea.referentiel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onea.referentiel.IntegrationTest;
import com.onea.referentiel.domain.Parcelle;
import com.onea.referentiel.repository.ParcelleRepository;
import com.onea.referentiel.service.dto.ParcelleDTO;
import com.onea.referentiel.service.mapper.ParcelleMapper;
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
 * Integration tests for the {@link ParcelleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParcelleResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parcelles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParcelleRepository parcelleRepository;

    @Autowired
    private ParcelleMapper parcelleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParcelleMockMvc;

    private Parcelle parcelle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parcelle createEntity(EntityManager em) {
        Parcelle parcelle = new Parcelle().libelle(DEFAULT_LIBELLE);
        return parcelle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parcelle createUpdatedEntity(EntityManager em) {
        Parcelle parcelle = new Parcelle().libelle(UPDATED_LIBELLE);
        return parcelle;
    }

    @BeforeEach
    public void initTest() {
        parcelle = createEntity(em);
    }

    @Test
    @Transactional
    void createParcelle() throws Exception {
        int databaseSizeBeforeCreate = parcelleRepository.findAll().size();
        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);
        restParcelleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcelleDTO)))
            .andExpect(status().isCreated());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeCreate + 1);
        Parcelle testParcelle = parcelleList.get(parcelleList.size() - 1);
        assertThat(testParcelle.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createParcelleWithExistingId() throws Exception {
        // Create the Parcelle with an existing ID
        parcelle.setId(1L);
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        int databaseSizeBeforeCreate = parcelleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParcelleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcelleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = parcelleRepository.findAll().size();
        // set the field null
        parcelle.setLibelle(null);

        // Create the Parcelle, which fails.
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        restParcelleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcelleDTO)))
            .andExpect(status().isBadRequest());

        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParcelles() throws Exception {
        // Initialize the database
        parcelleRepository.saveAndFlush(parcelle);

        // Get all the parcelleList
        restParcelleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parcelle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getParcelle() throws Exception {
        // Initialize the database
        parcelleRepository.saveAndFlush(parcelle);

        // Get the parcelle
        restParcelleMockMvc
            .perform(get(ENTITY_API_URL_ID, parcelle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parcelle.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingParcelle() throws Exception {
        // Get the parcelle
        restParcelleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParcelle() throws Exception {
        // Initialize the database
        parcelleRepository.saveAndFlush(parcelle);

        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();

        // Update the parcelle
        Parcelle updatedParcelle = parcelleRepository.findById(parcelle.getId()).get();
        // Disconnect from session so that the updates on updatedParcelle are not directly saved in db
        em.detach(updatedParcelle);
        updatedParcelle.libelle(UPDATED_LIBELLE);
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(updatedParcelle);

        restParcelleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parcelleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcelleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
        Parcelle testParcelle = parcelleList.get(parcelleList.size() - 1);
        assertThat(testParcelle.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingParcelle() throws Exception {
        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();
        parcelle.setId(count.incrementAndGet());

        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcelleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parcelleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParcelle() throws Exception {
        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();
        parcelle.setId(count.incrementAndGet());

        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parcelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParcelle() throws Exception {
        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();
        parcelle.setId(count.incrementAndGet());

        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parcelleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParcelleWithPatch() throws Exception {
        // Initialize the database
        parcelleRepository.saveAndFlush(parcelle);

        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();

        // Update the parcelle using partial update
        Parcelle partialUpdatedParcelle = new Parcelle();
        partialUpdatedParcelle.setId(parcelle.getId());

        partialUpdatedParcelle.libelle(UPDATED_LIBELLE);

        restParcelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParcelle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParcelle))
            )
            .andExpect(status().isOk());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
        Parcelle testParcelle = parcelleList.get(parcelleList.size() - 1);
        assertThat(testParcelle.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateParcelleWithPatch() throws Exception {
        // Initialize the database
        parcelleRepository.saveAndFlush(parcelle);

        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();

        // Update the parcelle using partial update
        Parcelle partialUpdatedParcelle = new Parcelle();
        partialUpdatedParcelle.setId(parcelle.getId());

        partialUpdatedParcelle.libelle(UPDATED_LIBELLE);

        restParcelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParcelle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParcelle))
            )
            .andExpect(status().isOk());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
        Parcelle testParcelle = parcelleList.get(parcelleList.size() - 1);
        assertThat(testParcelle.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingParcelle() throws Exception {
        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();
        parcelle.setId(count.incrementAndGet());

        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParcelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parcelleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parcelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParcelle() throws Exception {
        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();
        parcelle.setId(count.incrementAndGet());

        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parcelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParcelle() throws Exception {
        int databaseSizeBeforeUpdate = parcelleRepository.findAll().size();
        parcelle.setId(count.incrementAndGet());

        // Create the Parcelle
        ParcelleDTO parcelleDTO = parcelleMapper.toDto(parcelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParcelleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parcelleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parcelle in the database
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParcelle() throws Exception {
        // Initialize the database
        parcelleRepository.saveAndFlush(parcelle);

        int databaseSizeBeforeDelete = parcelleRepository.findAll().size();

        // Delete the parcelle
        restParcelleMockMvc
            .perform(delete(ENTITY_API_URL_ID, parcelle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parcelle> parcelleList = parcelleRepository.findAll();
        assertThat(parcelleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
