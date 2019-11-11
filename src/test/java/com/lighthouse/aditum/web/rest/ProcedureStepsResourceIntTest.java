package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ProcedureSteps;
import com.lighthouse.aditum.repository.ProcedureStepsRepository;
import com.lighthouse.aditum.service.ProcedureStepsService;
import com.lighthouse.aditum.service.dto.ProcedureStepsDTO;
import com.lighthouse.aditum.service.mapper.ProcedureStepsMapper;
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
 * Test class for the ProcedureStepsResource REST controller.
 *
 * @see ProcedureStepsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ProcedureStepsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private ProcedureStepsRepository procedureStepsRepository;

    @Autowired
    private ProcedureStepsMapper procedureStepsMapper;

    @Autowired
    private ProcedureStepsService procedureStepsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureStepsMockMvc;

    private ProcedureSteps procedureSteps;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureStepsResource procedureStepsResource = new ProcedureStepsResource(procedureStepsService);
        this.restProcedureStepsMockMvc = MockMvcBuilders.standaloneSetup(procedureStepsResource)
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
    public static ProcedureSteps createEntity(EntityManager em) {
        ProcedureSteps procedureSteps = new ProcedureSteps()
            .description(DEFAULT_DESCRIPTION)
            .number(DEFAULT_NUMBER)
            .deleted(DEFAULT_DELETED);
        return procedureSteps;
    }

    @Before
    public void initTest() {
        procedureSteps = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedureSteps() throws Exception {
        int databaseSizeBeforeCreate = procedureStepsRepository.findAll().size();

        // Create the ProcedureSteps
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(procedureSteps);
        restProcedureStepsMockMvc.perform(post("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureSteps in the database
        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeCreate + 1);
        ProcedureSteps testProcedureSteps = procedureStepsList.get(procedureStepsList.size() - 1);
        assertThat(testProcedureSteps.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProcedureSteps.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testProcedureSteps.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createProcedureStepsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureStepsRepository.findAll().size();

        // Create the ProcedureSteps with an existing ID
        procedureSteps.setId(1L);
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(procedureSteps);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureStepsMockMvc.perform(post("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcedureSteps in the database
        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureStepsRepository.findAll().size();
        // set the field null
        procedureSteps.setDescription(null);

        // Create the ProcedureSteps, which fails.
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(procedureSteps);

        restProcedureStepsMockMvc.perform(post("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isBadRequest());

        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureStepsRepository.findAll().size();
        // set the field null
        procedureSteps.setNumber(null);

        // Create the ProcedureSteps, which fails.
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(procedureSteps);

        restProcedureStepsMockMvc.perform(post("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isBadRequest());

        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureStepsRepository.findAll().size();
        // set the field null
        procedureSteps.setDeleted(null);

        // Create the ProcedureSteps, which fails.
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(procedureSteps);

        restProcedureStepsMockMvc.perform(post("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isBadRequest());

        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedureSteps() throws Exception {
        // Initialize the database
        procedureStepsRepository.saveAndFlush(procedureSteps);

        // Get all the procedureStepsList
        restProcedureStepsMockMvc.perform(get("/api/procedure-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureSteps.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getProcedureSteps() throws Exception {
        // Initialize the database
        procedureStepsRepository.saveAndFlush(procedureSteps);

        // Get the procedureSteps
        restProcedureStepsMockMvc.perform(get("/api/procedure-steps/{id}", procedureSteps.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedureSteps.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingProcedureSteps() throws Exception {
        // Get the procedureSteps
        restProcedureStepsMockMvc.perform(get("/api/procedure-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedureSteps() throws Exception {
        // Initialize the database
        procedureStepsRepository.saveAndFlush(procedureSteps);
        int databaseSizeBeforeUpdate = procedureStepsRepository.findAll().size();

        // Update the procedureSteps
        ProcedureSteps updatedProcedureSteps = procedureStepsRepository.findOne(procedureSteps.getId());
        // Disconnect from session so that the updates on updatedProcedureSteps are not directly saved in db
        em.detach(updatedProcedureSteps);
        updatedProcedureSteps
            .description(UPDATED_DESCRIPTION)
            .number(UPDATED_NUMBER)
            .deleted(UPDATED_DELETED);
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(updatedProcedureSteps);

        restProcedureStepsMockMvc.perform(put("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isOk());

        // Validate the ProcedureSteps in the database
        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeUpdate);
        ProcedureSteps testProcedureSteps = procedureStepsList.get(procedureStepsList.size() - 1);
        assertThat(testProcedureSteps.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcedureSteps.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testProcedureSteps.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedureSteps() throws Exception {
        int databaseSizeBeforeUpdate = procedureStepsRepository.findAll().size();

        // Create the ProcedureSteps
        ProcedureStepsDTO procedureStepsDTO = procedureStepsMapper.toDto(procedureSteps);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureStepsMockMvc.perform(put("/api/procedure-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureStepsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureSteps in the database
        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedureSteps() throws Exception {
        // Initialize the database
        procedureStepsRepository.saveAndFlush(procedureSteps);
        int databaseSizeBeforeDelete = procedureStepsRepository.findAll().size();

        // Get the procedureSteps
        restProcedureStepsMockMvc.perform(delete("/api/procedure-steps/{id}", procedureSteps.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProcedureSteps> procedureStepsList = procedureStepsRepository.findAll();
        assertThat(procedureStepsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureSteps.class);
        ProcedureSteps procedureSteps1 = new ProcedureSteps();
        procedureSteps1.setId(1L);
        ProcedureSteps procedureSteps2 = new ProcedureSteps();
        procedureSteps2.setId(procedureSteps1.getId());
        assertThat(procedureSteps1).isEqualTo(procedureSteps2);
        procedureSteps2.setId(2L);
        assertThat(procedureSteps1).isNotEqualTo(procedureSteps2);
        procedureSteps1.setId(null);
        assertThat(procedureSteps1).isNotEqualTo(procedureSteps2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureStepsDTO.class);
        ProcedureStepsDTO procedureStepsDTO1 = new ProcedureStepsDTO();
        procedureStepsDTO1.setId(1L);
        ProcedureStepsDTO procedureStepsDTO2 = new ProcedureStepsDTO();
        assertThat(procedureStepsDTO1).isNotEqualTo(procedureStepsDTO2);
        procedureStepsDTO2.setId(procedureStepsDTO1.getId());
        assertThat(procedureStepsDTO1).isEqualTo(procedureStepsDTO2);
        procedureStepsDTO2.setId(2L);
        assertThat(procedureStepsDTO1).isNotEqualTo(procedureStepsDTO2);
        procedureStepsDTO1.setId(null);
        assertThat(procedureStepsDTO1).isNotEqualTo(procedureStepsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(procedureStepsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(procedureStepsMapper.fromId(null)).isNull();
    }
}
