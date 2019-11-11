package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ProcedureVisits;
import com.lighthouse.aditum.repository.ProcedureVisitsRepository;
import com.lighthouse.aditum.service.ProcedureVisitsService;
import com.lighthouse.aditum.service.dto.ProcedureVisitsDTO;
import com.lighthouse.aditum.service.mapper.ProcedureVisitsMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.lighthouse.aditum.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProcedureVisitsResource REST controller.
 *
 * @see ProcedureVisitsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ProcedureVisitsResourceIntTest {

    private static final ZonedDateTime DEFAULT_VISIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VISIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final Double DEFAULT_PUNTUATION = 1D;
    private static final Double UPDATED_PUNTUATION = 2D;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final Integer DEFAULT_IS_DONE = 1;
    private static final Integer UPDATED_IS_DONE = 2;

    @Autowired
    private ProcedureVisitsRepository procedureVisitsRepository;

    @Autowired
    private ProcedureVisitsMapper procedureVisitsMapper;

    @Autowired
    private ProcedureVisitsService procedureVisitsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureVisitsMockMvc;

    private ProcedureVisits procedureVisits;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureVisitsResource procedureVisitsResource = new ProcedureVisitsResource(procedureVisitsService);
        this.restProcedureVisitsMockMvc = MockMvcBuilders.standaloneSetup(procedureVisitsResource)
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
    public static ProcedureVisits createEntity(EntityManager em) {
        ProcedureVisits procedureVisits = new ProcedureVisits()
            .visitDate(DEFAULT_VISIT_DATE)
            .duration(DEFAULT_DURATION)
            .puntuation(DEFAULT_PUNTUATION)
            .deleted(DEFAULT_DELETED)
            .isDone(DEFAULT_IS_DONE);
        return procedureVisits;
    }

    @Before
    public void initTest() {
        procedureVisits = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedureVisits() throws Exception {
        int databaseSizeBeforeCreate = procedureVisitsRepository.findAll().size();

        // Create the ProcedureVisits
        ProcedureVisitsDTO procedureVisitsDTO = procedureVisitsMapper.toDto(procedureVisits);
        restProcedureVisitsMockMvc.perform(post("/api/procedure-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureVisits in the database
        List<ProcedureVisits> procedureVisitsList = procedureVisitsRepository.findAll();
        assertThat(procedureVisitsList).hasSize(databaseSizeBeforeCreate + 1);
        ProcedureVisits testProcedureVisits = procedureVisitsList.get(procedureVisitsList.size() - 1);
        assertThat(testProcedureVisits.getVisitDate()).isEqualTo(DEFAULT_VISIT_DATE);
        assertThat(testProcedureVisits.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testProcedureVisits.getPuntuation()).isEqualTo(DEFAULT_PUNTUATION);
        assertThat(testProcedureVisits.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testProcedureVisits.getIsDone()).isEqualTo(DEFAULT_IS_DONE);
    }

    @Test
    @Transactional
    public void createProcedureVisitsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureVisitsRepository.findAll().size();

        // Create the ProcedureVisits with an existing ID
        procedureVisits.setId(1L);
        ProcedureVisitsDTO procedureVisitsDTO = procedureVisitsMapper.toDto(procedureVisits);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureVisitsMockMvc.perform(post("/api/procedure-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcedureVisits in the database
        List<ProcedureVisits> procedureVisitsList = procedureVisitsRepository.findAll();
        assertThat(procedureVisitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcedureVisits() throws Exception {
        // Initialize the database
        procedureVisitsRepository.saveAndFlush(procedureVisits);

        // Get all the procedureVisitsList
        restProcedureVisitsMockMvc.perform(get("/api/procedure-visits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureVisits.getId().intValue())))
            .andExpect(jsonPath("$.[*].visitDate").value(hasItem(sameInstant(DEFAULT_VISIT_DATE))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].puntuation").value(hasItem(DEFAULT_PUNTUATION.doubleValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].isDone").value(hasItem(DEFAULT_IS_DONE)));
    }

    @Test
    @Transactional
    public void getProcedureVisits() throws Exception {
        // Initialize the database
        procedureVisitsRepository.saveAndFlush(procedureVisits);

        // Get the procedureVisits
        restProcedureVisitsMockMvc.perform(get("/api/procedure-visits/{id}", procedureVisits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedureVisits.getId().intValue()))
            .andExpect(jsonPath("$.visitDate").value(sameInstant(DEFAULT_VISIT_DATE)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.puntuation").value(DEFAULT_PUNTUATION.doubleValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.isDone").value(DEFAULT_IS_DONE));
    }

    @Test
    @Transactional
    public void getNonExistingProcedureVisits() throws Exception {
        // Get the procedureVisits
        restProcedureVisitsMockMvc.perform(get("/api/procedure-visits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedureVisits() throws Exception {
        // Initialize the database
        procedureVisitsRepository.saveAndFlush(procedureVisits);
        int databaseSizeBeforeUpdate = procedureVisitsRepository.findAll().size();

        // Update the procedureVisits
        ProcedureVisits updatedProcedureVisits = procedureVisitsRepository.findOne(procedureVisits.getId());
        // Disconnect from session so that the updates on updatedProcedureVisits are not directly saved in db
        em.detach(updatedProcedureVisits);
        updatedProcedureVisits
            .visitDate(UPDATED_VISIT_DATE)
            .duration(UPDATED_DURATION)
            .puntuation(UPDATED_PUNTUATION)
            .deleted(UPDATED_DELETED)
            .isDone(UPDATED_IS_DONE);
        ProcedureVisitsDTO procedureVisitsDTO = procedureVisitsMapper.toDto(updatedProcedureVisits);

        restProcedureVisitsMockMvc.perform(put("/api/procedure-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitsDTO)))
            .andExpect(status().isOk());

        // Validate the ProcedureVisits in the database
        List<ProcedureVisits> procedureVisitsList = procedureVisitsRepository.findAll();
        assertThat(procedureVisitsList).hasSize(databaseSizeBeforeUpdate);
        ProcedureVisits testProcedureVisits = procedureVisitsList.get(procedureVisitsList.size() - 1);
        assertThat(testProcedureVisits.getVisitDate()).isEqualTo(UPDATED_VISIT_DATE);
        assertThat(testProcedureVisits.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testProcedureVisits.getPuntuation()).isEqualTo(UPDATED_PUNTUATION);
        assertThat(testProcedureVisits.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testProcedureVisits.getIsDone()).isEqualTo(UPDATED_IS_DONE);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedureVisits() throws Exception {
        int databaseSizeBeforeUpdate = procedureVisitsRepository.findAll().size();

        // Create the ProcedureVisits
        ProcedureVisitsDTO procedureVisitsDTO = procedureVisitsMapper.toDto(procedureVisits);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureVisitsMockMvc.perform(put("/api/procedure-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureVisitsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureVisits in the database
        List<ProcedureVisits> procedureVisitsList = procedureVisitsRepository.findAll();
        assertThat(procedureVisitsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedureVisits() throws Exception {
        // Initialize the database
        procedureVisitsRepository.saveAndFlush(procedureVisits);
        int databaseSizeBeforeDelete = procedureVisitsRepository.findAll().size();

        // Get the procedureVisits
        restProcedureVisitsMockMvc.perform(delete("/api/procedure-visits/{id}", procedureVisits.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProcedureVisits> procedureVisitsList = procedureVisitsRepository.findAll();
        assertThat(procedureVisitsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureVisits.class);
        ProcedureVisits procedureVisits1 = new ProcedureVisits();
        procedureVisits1.setId(1L);
        ProcedureVisits procedureVisits2 = new ProcedureVisits();
        procedureVisits2.setId(procedureVisits1.getId());
        assertThat(procedureVisits1).isEqualTo(procedureVisits2);
        procedureVisits2.setId(2L);
        assertThat(procedureVisits1).isNotEqualTo(procedureVisits2);
        procedureVisits1.setId(null);
        assertThat(procedureVisits1).isNotEqualTo(procedureVisits2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureVisitsDTO.class);
        ProcedureVisitsDTO procedureVisitsDTO1 = new ProcedureVisitsDTO();
        procedureVisitsDTO1.setId(1L);
        ProcedureVisitsDTO procedureVisitsDTO2 = new ProcedureVisitsDTO();
        assertThat(procedureVisitsDTO1).isNotEqualTo(procedureVisitsDTO2);
        procedureVisitsDTO2.setId(procedureVisitsDTO1.getId());
        assertThat(procedureVisitsDTO1).isEqualTo(procedureVisitsDTO2);
        procedureVisitsDTO2.setId(2L);
        assertThat(procedureVisitsDTO1).isNotEqualTo(procedureVisitsDTO2);
        procedureVisitsDTO1.setId(null);
        assertThat(procedureVisitsDTO1).isNotEqualTo(procedureVisitsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(procedureVisitsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(procedureVisitsMapper.fromId(null)).isNull();
    }
}
