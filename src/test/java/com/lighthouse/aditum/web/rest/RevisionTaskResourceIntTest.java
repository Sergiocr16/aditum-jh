package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.RevisionTask;
import com.lighthouse.aditum.repository.RevisionTaskRepository;
import com.lighthouse.aditum.service.RevisionTaskService;
import com.lighthouse.aditum.service.dto.RevisionTaskDTO;
import com.lighthouse.aditum.service.mapper.RevisionTaskMapper;
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

//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RevisionTaskResource REST controller.
 *
 * @see RevisionTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RevisionTaskResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION_FILE = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION_FILE = "BBBBBBBBBB";

    @Autowired
    private RevisionTaskRepository revisionTaskRepository;

    @Autowired
    private RevisionTaskMapper revisionTaskMapper;

    @Autowired
    private RevisionTaskService revisionTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRevisionTaskMockMvc;

    private RevisionTask revisionTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevisionTaskResource revisionTaskResource = new RevisionTaskResource(revisionTaskService);
        this.restRevisionTaskMockMvc = MockMvcBuilders.standaloneSetup(revisionTaskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevisionTask createEntity(EntityManager em) {
        RevisionTask revisionTask = new RevisionTask()
            .description(DEFAULT_DESCRIPTION)
            .done(DEFAULT_DONE)
            .observations(DEFAULT_OBSERVATIONS)
            .observationFile(DEFAULT_OBSERVATION_FILE);
        return revisionTask;
    }

    @Before
    public void initTest() {
        revisionTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevisionTask() throws Exception {
        int databaseSizeBeforeCreate = revisionTaskRepository.findAll().size();

        // Create the RevisionTask
        RevisionTaskDTO revisionTaskDTO = revisionTaskMapper.toDto(revisionTask);
        restRevisionTaskMockMvc.perform(post("/api/revision-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionTask in the database
        List<RevisionTask> revisionTaskList = revisionTaskRepository.findAll();
        assertThat(revisionTaskList).hasSize(databaseSizeBeforeCreate + 1);
        RevisionTask testRevisionTask = revisionTaskList.get(revisionTaskList.size() - 1);
        assertThat(testRevisionTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRevisionTask.isDone()).isEqualTo(DEFAULT_DONE);
        assertThat(testRevisionTask.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testRevisionTask.getObservationFile()).isEqualTo(DEFAULT_OBSERVATION_FILE);
    }

    @Test
    @Transactional
    public void createRevisionTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revisionTaskRepository.findAll().size();

        // Create the RevisionTask with an existing ID
        revisionTask.setId(1L);
        RevisionTaskDTO revisionTaskDTO = revisionTaskMapper.toDto(revisionTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionTaskMockMvc.perform(post("/api/revision-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RevisionTask in the database
        List<RevisionTask> revisionTaskList = revisionTaskRepository.findAll();
        assertThat(revisionTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRevisionTasks() throws Exception {
        // Initialize the database
        revisionTaskRepository.saveAndFlush(revisionTask);

        // Get all the revisionTaskList
        restRevisionTaskMockMvc.perform(get("/api/revision-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revisionTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].observationFile").value(hasItem(DEFAULT_OBSERVATION_FILE.toString())));
    }

    @Test
    @Transactional
    public void getRevisionTask() throws Exception {
        // Initialize the database
        revisionTaskRepository.saveAndFlush(revisionTask);

        // Get the revisionTask
        restRevisionTaskMockMvc.perform(get("/api/revision-tasks/{id}", revisionTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revisionTask.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.booleanValue()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.observationFile").value(DEFAULT_OBSERVATION_FILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRevisionTask() throws Exception {
        // Get the revisionTask
        restRevisionTaskMockMvc.perform(get("/api/revision-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevisionTask() throws Exception {
        // Initialize the database
        revisionTaskRepository.saveAndFlush(revisionTask);
        int databaseSizeBeforeUpdate = revisionTaskRepository.findAll().size();

        // Update the revisionTask
        RevisionTask updatedRevisionTask = revisionTaskRepository.findOne(revisionTask.getId());
        // Disconnect from session so that the updates on updatedRevisionTask are not directly saved in db
        em.detach(updatedRevisionTask);
        updatedRevisionTask
            .description(UPDATED_DESCRIPTION)
            .done(UPDATED_DONE)
            .observations(UPDATED_OBSERVATIONS)
            .observationFile(UPDATED_OBSERVATION_FILE);
        RevisionTaskDTO revisionTaskDTO = revisionTaskMapper.toDto(updatedRevisionTask);

        restRevisionTaskMockMvc.perform(put("/api/revision-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskDTO)))
            .andExpect(status().isOk());

        // Validate the RevisionTask in the database
        List<RevisionTask> revisionTaskList = revisionTaskRepository.findAll();
        assertThat(revisionTaskList).hasSize(databaseSizeBeforeUpdate);
        RevisionTask testRevisionTask = revisionTaskList.get(revisionTaskList.size() - 1);
        assertThat(testRevisionTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRevisionTask.isDone()).isEqualTo(UPDATED_DONE);
        assertThat(testRevisionTask.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testRevisionTask.getObservationFile()).isEqualTo(UPDATED_OBSERVATION_FILE);
    }

    @Test
    @Transactional
    public void updateNonExistingRevisionTask() throws Exception {
        int databaseSizeBeforeUpdate = revisionTaskRepository.findAll().size();

        // Create the RevisionTask
        RevisionTaskDTO revisionTaskDTO = revisionTaskMapper.toDto(revisionTask);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRevisionTaskMockMvc.perform(put("/api/revision-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionTask in the database
        List<RevisionTask> revisionTaskList = revisionTaskRepository.findAll();
        assertThat(revisionTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRevisionTask() throws Exception {
        // Initialize the database
        revisionTaskRepository.saveAndFlush(revisionTask);
        int databaseSizeBeforeDelete = revisionTaskRepository.findAll().size();

        // Get the revisionTask
        restRevisionTaskMockMvc.perform(delete("/api/revision-tasks/{id}", revisionTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RevisionTask> revisionTaskList = revisionTaskRepository.findAll();
        assertThat(revisionTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionTask.class);
        RevisionTask revisionTask1 = new RevisionTask();
        revisionTask1.setId(1L);
        RevisionTask revisionTask2 = new RevisionTask();
        revisionTask2.setId(revisionTask1.getId());
        assertThat(revisionTask1).isEqualTo(revisionTask2);
        revisionTask2.setId(2L);
        assertThat(revisionTask1).isNotEqualTo(revisionTask2);
        revisionTask1.setId(null);
        assertThat(revisionTask1).isNotEqualTo(revisionTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionTaskDTO.class);
        RevisionTaskDTO revisionTaskDTO1 = new RevisionTaskDTO();
        revisionTaskDTO1.setId(1L);
        RevisionTaskDTO revisionTaskDTO2 = new RevisionTaskDTO();
        assertThat(revisionTaskDTO1).isNotEqualTo(revisionTaskDTO2);
        revisionTaskDTO2.setId(revisionTaskDTO1.getId());
        assertThat(revisionTaskDTO1).isEqualTo(revisionTaskDTO2);
        revisionTaskDTO2.setId(2L);
        assertThat(revisionTaskDTO1).isNotEqualTo(revisionTaskDTO2);
        revisionTaskDTO1.setId(null);
        assertThat(revisionTaskDTO1).isNotEqualTo(revisionTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(revisionTaskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(revisionTaskMapper.fromId(null)).isNull();
    }
}
