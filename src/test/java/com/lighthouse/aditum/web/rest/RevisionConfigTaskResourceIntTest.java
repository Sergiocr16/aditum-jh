package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.RevisionConfigTask;
import com.lighthouse.aditum.repository.RevisionConfigTaskRepository;
import com.lighthouse.aditum.service.RevisionConfigTaskService;
import com.lighthouse.aditum.service.dto.RevisionConfigTaskDTO;
import com.lighthouse.aditum.service.mapper.RevisionConfigTaskMapper;
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
 * Test class for the RevisionConfigTaskResource REST controller.
 *
 * @see RevisionConfigTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RevisionConfigTaskResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    @Autowired
    private RevisionConfigTaskRepository revisionConfigTaskRepository;

    @Autowired
    private RevisionConfigTaskMapper revisionConfigTaskMapper;

    @Autowired
    private RevisionConfigTaskService revisionConfigTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRevisionConfigTaskMockMvc;

    private RevisionConfigTask revisionConfigTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevisionConfigTaskResource revisionConfigTaskResource = new RevisionConfigTaskResource(revisionConfigTaskService);
        this.restRevisionConfigTaskMockMvc = MockMvcBuilders.standaloneSetup(revisionConfigTaskResource)
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
    public static RevisionConfigTask createEntity(EntityManager em) {
        RevisionConfigTask revisionConfigTask = new RevisionConfigTask()
            .description(DEFAULT_DESCRIPTION)
            .observations(DEFAULT_OBSERVATIONS);
        return revisionConfigTask;
    }

    @Before
    public void initTest() {
        revisionConfigTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevisionConfigTask() throws Exception {
        int databaseSizeBeforeCreate = revisionConfigTaskRepository.findAll().size();

        // Create the RevisionConfigTask
        RevisionConfigTaskDTO revisionConfigTaskDTO = revisionConfigTaskMapper.toDto(revisionConfigTask);
        restRevisionConfigTaskMockMvc.perform(post("/api/revision-config-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionConfigTask in the database
        List<RevisionConfigTask> revisionConfigTaskList = revisionConfigTaskRepository.findAll();
        assertThat(revisionConfigTaskList).hasSize(databaseSizeBeforeCreate + 1);
        RevisionConfigTask testRevisionConfigTask = revisionConfigTaskList.get(revisionConfigTaskList.size() - 1);
        assertThat(testRevisionConfigTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRevisionConfigTask.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
    }

    @Test
    @Transactional
    public void createRevisionConfigTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revisionConfigTaskRepository.findAll().size();

        // Create the RevisionConfigTask with an existing ID
        revisionConfigTask.setId(1L);
        RevisionConfigTaskDTO revisionConfigTaskDTO = revisionConfigTaskMapper.toDto(revisionConfigTask);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionConfigTaskMockMvc.perform(post("/api/revision-config-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RevisionConfigTask in the database
        List<RevisionConfigTask> revisionConfigTaskList = revisionConfigTaskRepository.findAll();
        assertThat(revisionConfigTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRevisionConfigTasks() throws Exception {
        // Initialize the database
        revisionConfigTaskRepository.saveAndFlush(revisionConfigTask);

        // Get all the revisionConfigTaskList
        restRevisionConfigTaskMockMvc.perform(get("/api/revision-config-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revisionConfigTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())));
    }

    @Test
    @Transactional
    public void getRevisionConfigTask() throws Exception {
        // Initialize the database
        revisionConfigTaskRepository.saveAndFlush(revisionConfigTask);

        // Get the revisionConfigTask
        restRevisionConfigTaskMockMvc.perform(get("/api/revision-config-tasks/{id}", revisionConfigTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revisionConfigTask.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRevisionConfigTask() throws Exception {
        // Get the revisionConfigTask
        restRevisionConfigTaskMockMvc.perform(get("/api/revision-config-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevisionConfigTask() throws Exception {
        // Initialize the database
        revisionConfigTaskRepository.saveAndFlush(revisionConfigTask);
        int databaseSizeBeforeUpdate = revisionConfigTaskRepository.findAll().size();

        // Update the revisionConfigTask
        RevisionConfigTask updatedRevisionConfigTask = revisionConfigTaskRepository.findOne(revisionConfigTask.getId());
        // Disconnect from session so that the updates on updatedRevisionConfigTask are not directly saved in db
        em.detach(updatedRevisionConfigTask);
        updatedRevisionConfigTask
            .description(UPDATED_DESCRIPTION)
            .observations(UPDATED_OBSERVATIONS);
        RevisionConfigTaskDTO revisionConfigTaskDTO = revisionConfigTaskMapper.toDto(updatedRevisionConfigTask);

        restRevisionConfigTaskMockMvc.perform(put("/api/revision-config-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigTaskDTO)))
            .andExpect(status().isOk());

        // Validate the RevisionConfigTask in the database
        List<RevisionConfigTask> revisionConfigTaskList = revisionConfigTaskRepository.findAll();
        assertThat(revisionConfigTaskList).hasSize(databaseSizeBeforeUpdate);
        RevisionConfigTask testRevisionConfigTask = revisionConfigTaskList.get(revisionConfigTaskList.size() - 1);
        assertThat(testRevisionConfigTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRevisionConfigTask.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingRevisionConfigTask() throws Exception {
        int databaseSizeBeforeUpdate = revisionConfigTaskRepository.findAll().size();

        // Create the RevisionConfigTask
        RevisionConfigTaskDTO revisionConfigTaskDTO = revisionConfigTaskMapper.toDto(revisionConfigTask);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRevisionConfigTaskMockMvc.perform(put("/api/revision-config-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigTaskDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionConfigTask in the database
        List<RevisionConfigTask> revisionConfigTaskList = revisionConfigTaskRepository.findAll();
        assertThat(revisionConfigTaskList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRevisionConfigTask() throws Exception {
        // Initialize the database
        revisionConfigTaskRepository.saveAndFlush(revisionConfigTask);
        int databaseSizeBeforeDelete = revisionConfigTaskRepository.findAll().size();

        // Get the revisionConfigTask
        restRevisionConfigTaskMockMvc.perform(delete("/api/revision-config-tasks/{id}", revisionConfigTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RevisionConfigTask> revisionConfigTaskList = revisionConfigTaskRepository.findAll();
        assertThat(revisionConfigTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionConfigTask.class);
        RevisionConfigTask revisionConfigTask1 = new RevisionConfigTask();
        revisionConfigTask1.setId(1L);
        RevisionConfigTask revisionConfigTask2 = new RevisionConfigTask();
        revisionConfigTask2.setId(revisionConfigTask1.getId());
        assertThat(revisionConfigTask1).isEqualTo(revisionConfigTask2);
        revisionConfigTask2.setId(2L);
        assertThat(revisionConfigTask1).isNotEqualTo(revisionConfigTask2);
        revisionConfigTask1.setId(null);
        assertThat(revisionConfigTask1).isNotEqualTo(revisionConfigTask2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionConfigTaskDTO.class);
        RevisionConfigTaskDTO revisionConfigTaskDTO1 = new RevisionConfigTaskDTO();
        revisionConfigTaskDTO1.setId(1L);
        RevisionConfigTaskDTO revisionConfigTaskDTO2 = new RevisionConfigTaskDTO();
        assertThat(revisionConfigTaskDTO1).isNotEqualTo(revisionConfigTaskDTO2);
        revisionConfigTaskDTO2.setId(revisionConfigTaskDTO1.getId());
        assertThat(revisionConfigTaskDTO1).isEqualTo(revisionConfigTaskDTO2);
        revisionConfigTaskDTO2.setId(2L);
        assertThat(revisionConfigTaskDTO1).isNotEqualTo(revisionConfigTaskDTO2);
        revisionConfigTaskDTO1.setId(null);
        assertThat(revisionConfigTaskDTO1).isNotEqualTo(revisionConfigTaskDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(revisionConfigTaskMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(revisionConfigTaskMapper.fromId(null)).isNull();
    }
}
