package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Procedures;
import com.lighthouse.aditum.repository.ProceduresRepository;
import com.lighthouse.aditum.service.ProceduresService;
import com.lighthouse.aditum.service.dto.ProceduresDTO;
import com.lighthouse.aditum.service.mapper.ProceduresMapper;
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
 * Test class for the ProceduresResource REST controller.
 *
 * @see ProceduresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ProceduresResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private ProceduresRepository proceduresRepository;

    @Autowired
    private ProceduresMapper proceduresMapper;

    @Autowired
    private ProceduresService proceduresService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProceduresMockMvc;

    private Procedures procedures;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProceduresResource proceduresResource = new ProceduresResource(proceduresService);
        this.restProceduresMockMvc = MockMvcBuilders.standaloneSetup(proceduresResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Procedures createEntity(EntityManager em) {
        Procedures procedures = new Procedures()
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS);
        return procedures;
    }

    @Before
    public void initTest() {
        procedures = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedures() throws Exception {
        int databaseSizeBeforeCreate = proceduresRepository.findAll().size();

        // Create the Procedures
        ProceduresDTO proceduresDTO = proceduresMapper.toDto(procedures);
        restProceduresMockMvc.perform(post("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proceduresDTO)))
            .andExpect(status().isCreated());

        // Validate the Procedures in the database
        List<Procedures> proceduresList = proceduresRepository.findAll();
        assertThat(proceduresList).hasSize(databaseSizeBeforeCreate + 1);
        Procedures testProcedures = proceduresList.get(proceduresList.size() - 1);
        assertThat(testProcedures.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProcedures.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createProceduresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proceduresRepository.findAll().size();

        // Create the Procedures with an existing ID
        procedures.setId(1L);
        ProceduresDTO proceduresDTO = proceduresMapper.toDto(procedures);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProceduresMockMvc.perform(post("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proceduresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Procedures in the database
        List<Procedures> proceduresList = proceduresRepository.findAll();
        assertThat(proceduresList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = proceduresRepository.findAll().size();
        // set the field null
        procedures.setStatus(null);

        // Create the Procedures, which fails.
        ProceduresDTO proceduresDTO = proceduresMapper.toDto(procedures);

        restProceduresMockMvc.perform(post("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proceduresDTO)))
            .andExpect(status().isBadRequest());

        List<Procedures> proceduresList = proceduresRepository.findAll();
        assertThat(proceduresList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedures() throws Exception {
        // Initialize the database
        proceduresRepository.saveAndFlush(procedures);

        // Get all the proceduresList
        restProceduresMockMvc.perform(get("/api/procedures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedures.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getProcedures() throws Exception {
        // Initialize the database
        proceduresRepository.saveAndFlush(procedures);

        // Get the procedures
        restProceduresMockMvc.perform(get("/api/procedures/{id}", procedures.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedures.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingProcedures() throws Exception {
        // Get the procedures
        restProceduresMockMvc.perform(get("/api/procedures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedures() throws Exception {
        // Initialize the database
        proceduresRepository.saveAndFlush(procedures);
        int databaseSizeBeforeUpdate = proceduresRepository.findAll().size();

        // Update the procedures
        Procedures updatedProcedures = proceduresRepository.findOne(procedures.getId());
        // Disconnect from session so that the updates on updatedProcedures are not directly saved in db
        em.detach(updatedProcedures);
        updatedProcedures
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS);
        ProceduresDTO proceduresDTO = proceduresMapper.toDto(updatedProcedures);

        restProceduresMockMvc.perform(put("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proceduresDTO)))
            .andExpect(status().isOk());

        // Validate the Procedures in the database
        List<Procedures> proceduresList = proceduresRepository.findAll();
        assertThat(proceduresList).hasSize(databaseSizeBeforeUpdate);
        Procedures testProcedures = proceduresList.get(proceduresList.size() - 1);
        assertThat(testProcedures.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcedures.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedures() throws Exception {
        int databaseSizeBeforeUpdate = proceduresRepository.findAll().size();

        // Create the Procedures
        ProceduresDTO proceduresDTO = proceduresMapper.toDto(procedures);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProceduresMockMvc.perform(put("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proceduresDTO)))
            .andExpect(status().isCreated());

        // Validate the Procedures in the database
        List<Procedures> proceduresList = proceduresRepository.findAll();
        assertThat(proceduresList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedures() throws Exception {
        // Initialize the database
        proceduresRepository.saveAndFlush(procedures);
        int databaseSizeBeforeDelete = proceduresRepository.findAll().size();

        // Get the procedures
        restProceduresMockMvc.perform(delete("/api/procedures/{id}", procedures.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Procedures> proceduresList = proceduresRepository.findAll();
        assertThat(proceduresList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Procedures.class);
        Procedures procedures1 = new Procedures();
        procedures1.setId(1L);
        Procedures procedures2 = new Procedures();
        procedures2.setId(procedures1.getId());
        assertThat(procedures1).isEqualTo(procedures2);
        procedures2.setId(2L);
        assertThat(procedures1).isNotEqualTo(procedures2);
        procedures1.setId(null);
        assertThat(procedures1).isNotEqualTo(procedures2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProceduresDTO.class);
        ProceduresDTO proceduresDTO1 = new ProceduresDTO();
        proceduresDTO1.setId(1L);
        ProceduresDTO proceduresDTO2 = new ProceduresDTO();
        assertThat(proceduresDTO1).isNotEqualTo(proceduresDTO2);
        proceduresDTO2.setId(proceduresDTO1.getId());
        assertThat(proceduresDTO1).isEqualTo(proceduresDTO2);
        proceduresDTO2.setId(2L);
        assertThat(proceduresDTO1).isNotEqualTo(proceduresDTO2);
        proceduresDTO1.setId(null);
        assertThat(proceduresDTO1).isNotEqualTo(proceduresDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(proceduresMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(proceduresMapper.fromId(null)).isNull();
    }
}
