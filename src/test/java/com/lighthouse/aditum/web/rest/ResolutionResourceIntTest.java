package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Resolution;
import com.lighthouse.aditum.repository.ResolutionRepository;
import com.lighthouse.aditum.service.ResolutionService;
import com.lighthouse.aditum.service.dto.ResolutionDTO;
import com.lighthouse.aditum.service.mapper.ResolutionMapper;
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
 * Test class for the ResolutionResource REST controller.
 *
 * @see ResolutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ResolutionResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PROBLEM = "AAAAAAAAAA";
    private static final String UPDATED_PROBLEM = "BBBBBBBBBB";

    private static final String DEFAULT_SOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_SOLUTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATED = 1;
    private static final Integer UPDATED_STATED = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final Integer DEFAULT_SOLVED_TIMES = 1;
    private static final Integer UPDATED_SOLVED_TIMES = 2;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private ResolutionMapper resolutionMapper;

    @Autowired
    private ResolutionService resolutionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResolutionMockMvc;

    private Resolution resolution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResolutionResource resolutionResource = new ResolutionResource(resolutionService);
        this.restResolutionMockMvc = MockMvcBuilders.standaloneSetup(resolutionResource)
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
    public static Resolution createEntity(EntityManager em) {
        Resolution resolution = new Resolution()
            .title(DEFAULT_TITLE)
            .problem(DEFAULT_PROBLEM)
            .solution(DEFAULT_SOLUTION)
            .stated(DEFAULT_STATED)
            .deleted(DEFAULT_DELETED)
            .solvedTimes(DEFAULT_SOLVED_TIMES)
            .creationDate(DEFAULT_CREATION_DATE);
        return resolution;
    }

    @Before
    public void initTest() {
        resolution = createEntity(em);
    }

    @Test
    @Transactional
    public void createResolution() throws Exception {
        int databaseSizeBeforeCreate = resolutionRepository.findAll().size();

        // Create the Resolution
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(resolution);
        restResolutionMockMvc.perform(post("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isCreated());

        // Validate the Resolution in the database
        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeCreate + 1);
        Resolution testResolution = resolutionList.get(resolutionList.size() - 1);
        assertThat(testResolution.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResolution.getProblem()).isEqualTo(DEFAULT_PROBLEM);
        assertThat(testResolution.getSolution()).isEqualTo(DEFAULT_SOLUTION);
        assertThat(testResolution.getStated()).isEqualTo(DEFAULT_STATED);
        assertThat(testResolution.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testResolution.getSolvedTimes()).isEqualTo(DEFAULT_SOLVED_TIMES);
        assertThat(testResolution.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createResolutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resolutionRepository.findAll().size();

        // Create the Resolution with an existing ID
        resolution.setId(1L);
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(resolution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResolutionMockMvc.perform(post("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resolution in the database
        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = resolutionRepository.findAll().size();
        // set the field null
        resolution.setTitle(null);

        // Create the Resolution, which fails.
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(resolution);

        restResolutionMockMvc.perform(post("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isBadRequest());

        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProblemIsRequired() throws Exception {
        int databaseSizeBeforeTest = resolutionRepository.findAll().size();
        // set the field null
        resolution.setProblem(null);

        // Create the Resolution, which fails.
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(resolution);

        restResolutionMockMvc.perform(post("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isBadRequest());

        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSolutionIsRequired() throws Exception {
        int databaseSizeBeforeTest = resolutionRepository.findAll().size();
        // set the field null
        resolution.setSolution(null);

        // Create the Resolution, which fails.
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(resolution);

        restResolutionMockMvc.perform(post("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isBadRequest());

        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResolutions() throws Exception {
        // Initialize the database
        resolutionRepository.saveAndFlush(resolution);

        // Get all the resolutionList
        restResolutionMockMvc.perform(get("/api/resolutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resolution.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].problem").value(hasItem(DEFAULT_PROBLEM.toString())))
            .andExpect(jsonPath("$.[*].solution").value(hasItem(DEFAULT_SOLUTION.toString())))
            .andExpect(jsonPath("$.[*].stated").value(hasItem(DEFAULT_STATED)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].solvedTimes").value(hasItem(DEFAULT_SOLVED_TIMES)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
    }

    @Test
    @Transactional
    public void getResolution() throws Exception {
        // Initialize the database
        resolutionRepository.saveAndFlush(resolution);

        // Get the resolution
        restResolutionMockMvc.perform(get("/api/resolutions/{id}", resolution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resolution.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.problem").value(DEFAULT_PROBLEM.toString()))
            .andExpect(jsonPath("$.solution").value(DEFAULT_SOLUTION.toString()))
            .andExpect(jsonPath("$.stated").value(DEFAULT_STATED))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.solvedTimes").value(DEFAULT_SOLVED_TIMES))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingResolution() throws Exception {
        // Get the resolution
        restResolutionMockMvc.perform(get("/api/resolutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResolution() throws Exception {
        // Initialize the database
        resolutionRepository.saveAndFlush(resolution);
        int databaseSizeBeforeUpdate = resolutionRepository.findAll().size();

        // Update the resolution
        Resolution updatedResolution = resolutionRepository.findOne(resolution.getId());
        // Disconnect from session so that the updates on updatedResolution are not directly saved in db
        em.detach(updatedResolution);
        updatedResolution
            .title(UPDATED_TITLE)
            .problem(UPDATED_PROBLEM)
            .solution(UPDATED_SOLUTION)
            .stated(UPDATED_STATED)
            .deleted(UPDATED_DELETED)
            .solvedTimes(UPDATED_SOLVED_TIMES)
            .creationDate(UPDATED_CREATION_DATE);
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(updatedResolution);

        restResolutionMockMvc.perform(put("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isOk());

        // Validate the Resolution in the database
        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeUpdate);
        Resolution testResolution = resolutionList.get(resolutionList.size() - 1);
        assertThat(testResolution.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResolution.getProblem()).isEqualTo(UPDATED_PROBLEM);
        assertThat(testResolution.getSolution()).isEqualTo(UPDATED_SOLUTION);
        assertThat(testResolution.getStated()).isEqualTo(UPDATED_STATED);
        assertThat(testResolution.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testResolution.getSolvedTimes()).isEqualTo(UPDATED_SOLVED_TIMES);
        assertThat(testResolution.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingResolution() throws Exception {
        int databaseSizeBeforeUpdate = resolutionRepository.findAll().size();

        // Create the Resolution
        ResolutionDTO resolutionDTO = resolutionMapper.toDto(resolution);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResolutionMockMvc.perform(put("/api/resolutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionDTO)))
            .andExpect(status().isCreated());

        // Validate the Resolution in the database
        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResolution() throws Exception {
        // Initialize the database
        resolutionRepository.saveAndFlush(resolution);
        int databaseSizeBeforeDelete = resolutionRepository.findAll().size();

        // Get the resolution
        restResolutionMockMvc.perform(delete("/api/resolutions/{id}", resolution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Resolution> resolutionList = resolutionRepository.findAll();
        assertThat(resolutionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resolution.class);
        Resolution resolution1 = new Resolution();
        resolution1.setId(1L);
        Resolution resolution2 = new Resolution();
        resolution2.setId(resolution1.getId());
        assertThat(resolution1).isEqualTo(resolution2);
        resolution2.setId(2L);
        assertThat(resolution1).isNotEqualTo(resolution2);
        resolution1.setId(null);
        assertThat(resolution1).isNotEqualTo(resolution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResolutionDTO.class);
        ResolutionDTO resolutionDTO1 = new ResolutionDTO();
        resolutionDTO1.setId(1L);
        ResolutionDTO resolutionDTO2 = new ResolutionDTO();
        assertThat(resolutionDTO1).isNotEqualTo(resolutionDTO2);
        resolutionDTO2.setId(resolutionDTO1.getId());
        assertThat(resolutionDTO1).isEqualTo(resolutionDTO2);
        resolutionDTO2.setId(2L);
        assertThat(resolutionDTO1).isNotEqualTo(resolutionDTO2);
        resolutionDTO1.setId(null);
        assertThat(resolutionDTO1).isNotEqualTo(resolutionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resolutionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resolutionMapper.fromId(null)).isNull();
    }
}
