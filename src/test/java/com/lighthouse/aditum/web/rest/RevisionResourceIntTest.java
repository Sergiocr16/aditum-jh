package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Revision;
import com.lighthouse.aditum.repository.RevisionRepository;
import com.lighthouse.aditum.service.RevisionService;
import com.lighthouse.aditum.service.RevisionTaskService;
import com.lighthouse.aditum.service.dto.RevisionDTO;
import com.lighthouse.aditum.service.mapper.RevisionMapper;
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
//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RevisionResource REST controller.
 *
 * @see RevisionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RevisionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXECUTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXECUTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private RevisionRepository revisionRepository;

    @Autowired
    private RevisionMapper revisionMapper;

    @Autowired
    private RevisionService revisionService;

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

    private MockMvc restRevisionMockMvc;

    private Revision revision;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevisionResource revisionResource = new RevisionResource(revisionTaskService,revisionService);
        this.restRevisionMockMvc = MockMvcBuilders.standaloneSetup(revisionResource)
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
    public static Revision createEntity(EntityManager em) {
        Revision revision = new Revision()
            .name(DEFAULT_NAME)
            .executionDate(DEFAULT_EXECUTION_DATE)
            .observations(DEFAULT_OBSERVATIONS)
            .status(DEFAULT_STATUS);
        return revision;
    }

    @Before
    public void initTest() {
        revision = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevision() throws Exception {
        int databaseSizeBeforeCreate = revisionRepository.findAll().size();

        // Create the Revision
        RevisionDTO revisionDTO = revisionMapper.toDto(revision);
        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionDTO)))
            .andExpect(status().isCreated());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate + 1);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRevision.getExecutionDate()).isEqualTo(DEFAULT_EXECUTION_DATE);
        assertThat(testRevision.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
        assertThat(testRevision.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRevisionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revisionRepository.findAll().size();

        // Create the Revision with an existing ID
        revision.setId(1L);
        RevisionDTO revisionDTO = revisionMapper.toDto(revision);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRevisions() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList
        restRevisionMockMvc.perform(get("/api/revisions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revision.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].executionDate").value(hasItem(sameInstant(DEFAULT_EXECUTION_DATE))))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get the revision
        restRevisionMockMvc.perform(get("/api/revisions/{id}", revision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revision.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.executionDate").value(sameInstant(DEFAULT_EXECUTION_DATE)))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingRevision() throws Exception {
        // Get the revision
        restRevisionMockMvc.perform(get("/api/revisions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Update the revision
        Revision updatedRevision = revisionRepository.findOne(revision.getId());
        // Disconnect from session so that the updates on updatedRevision are not directly saved in db
        em.detach(updatedRevision);
        updatedRevision
            .name(UPDATED_NAME)
            .executionDate(UPDATED_EXECUTION_DATE)
            .observations(UPDATED_OBSERVATIONS)
            .status(UPDATED_STATUS);
        RevisionDTO revisionDTO = revisionMapper.toDto(updatedRevision);

        restRevisionMockMvc.perform(put("/api/revisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionDTO)))
            .andExpect(status().isOk());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRevision.getExecutionDate()).isEqualTo(UPDATED_EXECUTION_DATE);
        assertThat(testRevision.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
        assertThat(testRevision.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Create the Revision
        RevisionDTO revisionDTO = revisionMapper.toDto(revision);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRevisionMockMvc.perform(put("/api/revisions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionDTO)))
            .andExpect(status().isCreated());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);
        int databaseSizeBeforeDelete = revisionRepository.findAll().size();

        // Get the revision
        restRevisionMockMvc.perform(delete("/api/revisions/{id}", revision.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Revision.class);
        Revision revision1 = new Revision();
        revision1.setId(1L);
        Revision revision2 = new Revision();
        revision2.setId(revision1.getId());
        assertThat(revision1).isEqualTo(revision2);
        revision2.setId(2L);
        assertThat(revision1).isNotEqualTo(revision2);
        revision1.setId(null);
        assertThat(revision1).isNotEqualTo(revision2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionDTO.class);
        RevisionDTO revisionDTO1 = new RevisionDTO();
        revisionDTO1.setId(1L);
        RevisionDTO revisionDTO2 = new RevisionDTO();
        assertThat(revisionDTO1).isNotEqualTo(revisionDTO2);
        revisionDTO2.setId(revisionDTO1.getId());
        assertThat(revisionDTO1).isEqualTo(revisionDTO2);
        revisionDTO2.setId(2L);
        assertThat(revisionDTO1).isNotEqualTo(revisionDTO2);
        revisionDTO1.setId(null);
        assertThat(revisionDTO1).isNotEqualTo(revisionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(revisionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(revisionMapper.fromId(null)).isNull();
    }
}
