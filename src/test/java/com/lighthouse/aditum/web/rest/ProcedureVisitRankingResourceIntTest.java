package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ProcedureVisitRanking;
import com.lighthouse.aditum.repository.ProcedureVisitRankingRepository;
import com.lighthouse.aditum.service.ProcedureVisitRankingService;
import com.lighthouse.aditum.service.dto.ProcedureVisitRankingDTO;
import com.lighthouse.aditum.service.mapper.ProcedureVisitRankingMapper;
import com.lighthouse.aditum.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProcedureVisitRankingResource REST controller.
 *
 * @see ProcedureVisitRankingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ProcedureVisitRankingResourceIntTest {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PUNTUATION = 1D;
    private static final Double UPDATED_PUNTUATION = 2D;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private ProcedureVisitRankingRepository procedureVisitRankingRepository;

    @Autowired
    private ProcedureVisitRankingMapper procedureVisitRankingMapper;

    @Autowired
    private ProcedureVisitRankingService procedureVisitRankingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureVisitRankingMockMvc;

    private ProcedureVisitRanking procedureVisitRanking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureVisitRankingResource procedureVisitRankingResource = new ProcedureVisitRankingResource(procedureVisitRankingService);
        this.restProcedureVisitRankingMockMvc = MockMvcBuilders.standaloneSetup(procedureVisitRankingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcedureVisitRanking createEntity(EntityManager em) {
        ProcedureVisitRanking procedureVisitRanking = new ProcedureVisitRanking()
            .question(DEFAULT_QUESTION)
            .puntuation(DEFAULT_PUNTUATION)
            .deleted(DEFAULT_DELETED);
        return procedureVisitRanking;
    }

    @Before
    public void initTest() {
        procedureVisitRanking = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedureVisitRanking() throws Exception {
        int databaseSizeBeforeCreate = procedureVisitRankingRepository.findAll().size();

        // Create the ProcedureVisitRanking
        ProcedureVisitRankingDTO procedureVisitRankingDTO = procedureVisitRankingMapper.toDto(procedureVisitRanking);
        restProcedureVisitRankingMockMvc.perform(post("/api/procedure-visit-rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitRankingDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureVisitRanking in the database
        List<ProcedureVisitRanking> procedureVisitRankingList = procedureVisitRankingRepository.findAll();
        assertThat(procedureVisitRankingList).hasSize(databaseSizeBeforeCreate + 1);
        ProcedureVisitRanking testProcedureVisitRanking = procedureVisitRankingList.get(procedureVisitRankingList.size() - 1);
        assertThat(testProcedureVisitRanking.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testProcedureVisitRanking.getPuntuation()).isEqualTo(DEFAULT_PUNTUATION);
        assertThat(testProcedureVisitRanking.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createProcedureVisitRankingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureVisitRankingRepository.findAll().size();

        // Create the ProcedureVisitRanking with an existing ID
        procedureVisitRanking.setId(1L);
        ProcedureVisitRankingDTO procedureVisitRankingDTO = procedureVisitRankingMapper.toDto(procedureVisitRanking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureVisitRankingMockMvc.perform(post("/api/procedure-visit-rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitRankingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcedureVisitRanking in the database
        List<ProcedureVisitRanking> procedureVisitRankingList = procedureVisitRankingRepository.findAll();
        assertThat(procedureVisitRankingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcedureVisitRankings() throws Exception {
        // Initialize the database
        procedureVisitRankingRepository.saveAndFlush(procedureVisitRanking);

        // Get all the procedureVisitRankingList
        restProcedureVisitRankingMockMvc.perform(get("/api/procedure-visit-rankings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureVisitRanking.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].puntuation").value(hasItem(DEFAULT_PUNTUATION.doubleValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getProcedureVisitRanking() throws Exception {
        // Initialize the database
        procedureVisitRankingRepository.saveAndFlush(procedureVisitRanking);

        // Get the procedureVisitRanking
        restProcedureVisitRankingMockMvc.perform(get("/api/procedure-visit-rankings/{id}", procedureVisitRanking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedureVisitRanking.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.puntuation").value(DEFAULT_PUNTUATION.doubleValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingProcedureVisitRanking() throws Exception {
        // Get the procedureVisitRanking
        restProcedureVisitRankingMockMvc.perform(get("/api/procedure-visit-rankings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedureVisitRanking() throws Exception {
        // Initialize the database
        procedureVisitRankingRepository.saveAndFlush(procedureVisitRanking);
        int databaseSizeBeforeUpdate = procedureVisitRankingRepository.findAll().size();

        // Update the procedureVisitRanking
        ProcedureVisitRanking updatedProcedureVisitRanking = procedureVisitRankingRepository.findOne(procedureVisitRanking.getId());
        // Disconnect from session so that the updates on updatedProcedureVisitRanking are not directly saved in db
        em.detach(updatedProcedureVisitRanking);
        updatedProcedureVisitRanking
            .question(UPDATED_QUESTION)
            .puntuation(UPDATED_PUNTUATION)
            .deleted(UPDATED_DELETED);
        ProcedureVisitRankingDTO procedureVisitRankingDTO = procedureVisitRankingMapper.toDto(updatedProcedureVisitRanking);

        restProcedureVisitRankingMockMvc.perform(put("/api/procedure-visit-rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitRankingDTO)))
            .andExpect(status().isOk());

        // Validate the ProcedureVisitRanking in the database
        List<ProcedureVisitRanking> procedureVisitRankingList = procedureVisitRankingRepository.findAll();
        assertThat(procedureVisitRankingList).hasSize(databaseSizeBeforeUpdate);
        ProcedureVisitRanking testProcedureVisitRanking = procedureVisitRankingList.get(procedureVisitRankingList.size() - 1);
        assertThat(testProcedureVisitRanking.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testProcedureVisitRanking.getPuntuation()).isEqualTo(UPDATED_PUNTUATION);
        assertThat(testProcedureVisitRanking.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedureVisitRanking() throws Exception {
        int databaseSizeBeforeUpdate = procedureVisitRankingRepository.findAll().size();

        // Create the ProcedureVisitRanking
        ProcedureVisitRankingDTO procedureVisitRankingDTO = procedureVisitRankingMapper.toDto(procedureVisitRanking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureVisitRankingMockMvc.perform(put("/api/procedure-visit-rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitRankingDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureVisitRanking in the database
        List<ProcedureVisitRanking> procedureVisitRankingList = procedureVisitRankingRepository.findAll();
        assertThat(procedureVisitRankingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedureVisitRanking() throws Exception {
        // Initialize the database
        procedureVisitRankingRepository.saveAndFlush(procedureVisitRanking);
        int databaseSizeBeforeDelete = procedureVisitRankingRepository.findAll().size();

        // Get the procedureVisitRanking
        restProcedureVisitRankingMockMvc.perform(delete("/api/procedure-visit-rankings/{id}", procedureVisitRanking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProcedureVisitRanking> procedureVisitRankingList = procedureVisitRankingRepository.findAll();
        assertThat(procedureVisitRankingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureVisitRanking.class);
        ProcedureVisitRanking procedureVisitRanking1 = new ProcedureVisitRanking();
        procedureVisitRanking1.setId(1L);
        ProcedureVisitRanking procedureVisitRanking2 = new ProcedureVisitRanking();
        procedureVisitRanking2.setId(procedureVisitRanking1.getId());
        assertThat(procedureVisitRanking1).isEqualTo(procedureVisitRanking2);
        procedureVisitRanking2.setId(2L);
        assertThat(procedureVisitRanking1).isNotEqualTo(procedureVisitRanking2);
        procedureVisitRanking1.setId(null);
        assertThat(procedureVisitRanking1).isNotEqualTo(procedureVisitRanking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureVisitRankingDTO.class);
        ProcedureVisitRankingDTO procedureVisitRankingDTO1 = new ProcedureVisitRankingDTO();
        procedureVisitRankingDTO1.setId(1L);
        ProcedureVisitRankingDTO procedureVisitRankingDTO2 = new ProcedureVisitRankingDTO();
        assertThat(procedureVisitRankingDTO1).isNotEqualTo(procedureVisitRankingDTO2);
        procedureVisitRankingDTO2.setId(procedureVisitRankingDTO1.getId());
        assertThat(procedureVisitRankingDTO1).isEqualTo(procedureVisitRankingDTO2);
        procedureVisitRankingDTO2.setId(2L);
        assertThat(procedureVisitRankingDTO1).isNotEqualTo(procedureVisitRankingDTO2);
        procedureVisitRankingDTO1.setId(null);
        assertThat(procedureVisitRankingDTO1).isNotEqualTo(procedureVisitRankingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(procedureVisitRankingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(procedureVisitRankingMapper.fromId(null)).isNull();
    }
}
