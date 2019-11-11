package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ProcedureComments;
import com.lighthouse.aditum.repository.ProcedureCommentsRepository;
import com.lighthouse.aditum.service.ProcedureCommentsService;
import com.lighthouse.aditum.service.dto.ProcedureCommentsDTO;
import com.lighthouse.aditum.service.mapper.ProcedureCommentsMapper;
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
 * Test class for the ProcedureCommentsResource REST controller.
 *
 * @see ProcedureCommentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ProcedureCommentsResourceIntTest {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_CREATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CREATION_DATE = "BBBBBBBBBB";

    @Autowired
    private ProcedureCommentsRepository procedureCommentsRepository;

    @Autowired
    private ProcedureCommentsMapper procedureCommentsMapper;

    @Autowired
    private ProcedureCommentsService procedureCommentsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureCommentsMockMvc;

    private ProcedureComments procedureComments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureCommentsResource procedureCommentsResource = new ProcedureCommentsResource(procedureCommentsService);
        this.restProcedureCommentsMockMvc = MockMvcBuilders.standaloneSetup(procedureCommentsResource)
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
    public static ProcedureComments createEntity(EntityManager em) {
        ProcedureComments procedureComments = new ProcedureComments()
            .comment(DEFAULT_COMMENT)
            .deleted(DEFAULT_DELETED)
            .creationDate(DEFAULT_CREATION_DATE);
        return procedureComments;
    }

    @Before
    public void initTest() {
        procedureComments = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedureComments() throws Exception {
        int databaseSizeBeforeCreate = procedureCommentsRepository.findAll().size();

        // Create the ProcedureComments
        ProcedureCommentsDTO procedureCommentsDTO = procedureCommentsMapper.toDto(procedureComments);
        restProcedureCommentsMockMvc.perform(post("/api/procedure-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureCommentsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureComments in the database
        List<ProcedureComments> procedureCommentsList = procedureCommentsRepository.findAll();
        assertThat(procedureCommentsList).hasSize(databaseSizeBeforeCreate + 1);
        ProcedureComments testProcedureComments = procedureCommentsList.get(procedureCommentsList.size() - 1);
        assertThat(testProcedureComments.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testProcedureComments.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testProcedureComments.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createProcedureCommentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureCommentsRepository.findAll().size();

        // Create the ProcedureComments with an existing ID
        procedureComments.setId(1L);
        ProcedureCommentsDTO procedureCommentsDTO = procedureCommentsMapper.toDto(procedureComments);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureCommentsMockMvc.perform(post("/api/procedure-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureCommentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcedureComments in the database
        List<ProcedureComments> procedureCommentsList = procedureCommentsRepository.findAll();
        assertThat(procedureCommentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureCommentsRepository.findAll().size();
        // set the field null
        procedureComments.setComment(null);

        // Create the ProcedureComments, which fails.
        ProcedureCommentsDTO procedureCommentsDTO = procedureCommentsMapper.toDto(procedureComments);

        restProcedureCommentsMockMvc.perform(post("/api/procedure-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureCommentsDTO)))
            .andExpect(status().isBadRequest());

        List<ProcedureComments> procedureCommentsList = procedureCommentsRepository.findAll();
        assertThat(procedureCommentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedureComments() throws Exception {
        // Initialize the database
        procedureCommentsRepository.saveAndFlush(procedureComments);

        // Get all the procedureCommentsList
        restProcedureCommentsMockMvc.perform(get("/api/procedure-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureComments.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getProcedureComments() throws Exception {
        // Initialize the database
        procedureCommentsRepository.saveAndFlush(procedureComments);

        // Get the procedureComments
        restProcedureCommentsMockMvc.perform(get("/api/procedure-comments/{id}", procedureComments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedureComments.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProcedureComments() throws Exception {
        // Get the procedureComments
        restProcedureCommentsMockMvc.perform(get("/api/procedure-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedureComments() throws Exception {
        // Initialize the database
        procedureCommentsRepository.saveAndFlush(procedureComments);
        int databaseSizeBeforeUpdate = procedureCommentsRepository.findAll().size();

        // Update the procedureComments
        ProcedureComments updatedProcedureComments = procedureCommentsRepository.findOne(procedureComments.getId());
        // Disconnect from session so that the updates on updatedProcedureComments are not directly saved in db
        em.detach(updatedProcedureComments);
        updatedProcedureComments
            .comment(UPDATED_COMMENT)
            .deleted(UPDATED_DELETED)
            .creationDate(UPDATED_CREATION_DATE);
        ProcedureCommentsDTO procedureCommentsDTO = procedureCommentsMapper.toDto(updatedProcedureComments);

        restProcedureCommentsMockMvc.perform(put("/api/procedure-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureCommentsDTO)))
            .andExpect(status().isOk());

        // Validate the ProcedureComments in the database
        List<ProcedureComments> procedureCommentsList = procedureCommentsRepository.findAll();
        assertThat(procedureCommentsList).hasSize(databaseSizeBeforeUpdate);
        ProcedureComments testProcedureComments = procedureCommentsList.get(procedureCommentsList.size() - 1);
        assertThat(testProcedureComments.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testProcedureComments.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testProcedureComments.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedureComments() throws Exception {
        int databaseSizeBeforeUpdate = procedureCommentsRepository.findAll().size();

        // Create the ProcedureComments
        ProcedureCommentsDTO procedureCommentsDTO = procedureCommentsMapper.toDto(procedureComments);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureCommentsMockMvc.perform(put("/api/procedure-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureCommentsDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcedureComments in the database
        List<ProcedureComments> procedureCommentsList = procedureCommentsRepository.findAll();
        assertThat(procedureCommentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedureComments() throws Exception {
        // Initialize the database
        procedureCommentsRepository.saveAndFlush(procedureComments);
        int databaseSizeBeforeDelete = procedureCommentsRepository.findAll().size();

        // Get the procedureComments
        restProcedureCommentsMockMvc.perform(delete("/api/procedure-comments/{id}", procedureComments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProcedureComments> procedureCommentsList = procedureCommentsRepository.findAll();
        assertThat(procedureCommentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureComments.class);
        ProcedureComments procedureComments1 = new ProcedureComments();
        procedureComments1.setId(1L);
        ProcedureComments procedureComments2 = new ProcedureComments();
        procedureComments2.setId(procedureComments1.getId());
        assertThat(procedureComments1).isEqualTo(procedureComments2);
        procedureComments2.setId(2L);
        assertThat(procedureComments1).isNotEqualTo(procedureComments2);
        procedureComments1.setId(null);
        assertThat(procedureComments1).isNotEqualTo(procedureComments2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureCommentsDTO.class);
        ProcedureCommentsDTO procedureCommentsDTO1 = new ProcedureCommentsDTO();
        procedureCommentsDTO1.setId(1L);
        ProcedureCommentsDTO procedureCommentsDTO2 = new ProcedureCommentsDTO();
        assertThat(procedureCommentsDTO1).isNotEqualTo(procedureCommentsDTO2);
        procedureCommentsDTO2.setId(procedureCommentsDTO1.getId());
        assertThat(procedureCommentsDTO1).isEqualTo(procedureCommentsDTO2);
        procedureCommentsDTO2.setId(2L);
        assertThat(procedureCommentsDTO1).isNotEqualTo(procedureCommentsDTO2);
        procedureCommentsDTO1.setId(null);
        assertThat(procedureCommentsDTO1).isNotEqualTo(procedureCommentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(procedureCommentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(procedureCommentsMapper.fromId(null)).isNull();
    }
}
