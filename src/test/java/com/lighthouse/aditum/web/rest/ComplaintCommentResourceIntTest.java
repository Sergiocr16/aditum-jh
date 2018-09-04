package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ComplaintComment;
import com.lighthouse.aditum.domain.Complaint;
import com.lighthouse.aditum.repository.ComplaintCommentRepository;
import com.lighthouse.aditum.service.ComplaintCommentService;
import com.lighthouse.aditum.service.dto.ComplaintCommentDTO;
import com.lighthouse.aditum.service.mapper.ComplaintCommentMapper;
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
 * Test class for the ComplaintCommentResource REST controller.
 *
 * @see ComplaintCommentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ComplaintCommentResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EDITED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EDITED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private ComplaintCommentRepository complaintCommentRepository;

    @Autowired
    private ComplaintCommentMapper complaintCommentMapper;

    @Autowired
    private ComplaintCommentService complaintCommentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restComplaintCommentMockMvc;

    private ComplaintComment complaintComment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComplaintCommentResource complaintCommentResource = new ComplaintCommentResource(complaintCommentService);
        this.restComplaintCommentMockMvc = MockMvcBuilders.standaloneSetup(complaintCommentResource)
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
    public static ComplaintComment createEntity(EntityManager em) {
        ComplaintComment complaintComment = new ComplaintComment()
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .editedDate(DEFAULT_EDITED_DATE)
            .deleted(DEFAULT_DELETED);
        // Add required entity
        Complaint complaint = ComplaintResourceIntTest.createEntity(em);
        em.persist(complaint);
        em.flush();
        complaintComment.setComplaint(complaint);
        return complaintComment;
    }

    @Before
    public void initTest() {
        complaintComment = createEntity(em);
    }

    @Test
    @Transactional
    public void createComplaintComment() throws Exception {
        int databaseSizeBeforeCreate = complaintCommentRepository.findAll().size();

        // Create the ComplaintComment
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);
        restComplaintCommentMockMvc.perform(post("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isCreated());

        // Validate the ComplaintComment in the database
        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeCreate + 1);
        ComplaintComment testComplaintComment = complaintCommentList.get(complaintCommentList.size() - 1);
        assertThat(testComplaintComment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testComplaintComment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testComplaintComment.getEditedDate()).isEqualTo(DEFAULT_EDITED_DATE);
        assertThat(testComplaintComment.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createComplaintCommentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = complaintCommentRepository.findAll().size();

        // Create the ComplaintComment with an existing ID
        complaintComment.setId(1L);
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplaintCommentMockMvc.perform(post("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ComplaintComment in the database
        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintCommentRepository.findAll().size();
        // set the field null
        complaintComment.setDescription(null);

        // Create the ComplaintComment, which fails.
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);

        restComplaintCommentMockMvc.perform(post("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isBadRequest());

        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintCommentRepository.findAll().size();
        // set the field null
        complaintComment.setCreationDate(null);

        // Create the ComplaintComment, which fails.
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);

        restComplaintCommentMockMvc.perform(post("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isBadRequest());

        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintCommentRepository.findAll().size();
        // set the field null
        complaintComment.setDeleted(null);

        // Create the ComplaintComment, which fails.
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);

        restComplaintCommentMockMvc.perform(post("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isBadRequest());

        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComplaintComments() throws Exception {
        // Initialize the database
        complaintCommentRepository.saveAndFlush(complaintComment);

        // Get all the complaintCommentList
        restComplaintCommentMockMvc.perform(get("/api/complaint-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaintComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].editedDate").value(hasItem(sameInstant(DEFAULT_EDITED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getComplaintComment() throws Exception {
        // Initialize the database
        complaintCommentRepository.saveAndFlush(complaintComment);

        // Get the complaintComment
        restComplaintCommentMockMvc.perform(get("/api/complaint-comments/{id}", complaintComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(complaintComment.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.editedDate").value(sameInstant(DEFAULT_EDITED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingComplaintComment() throws Exception {
        // Get the complaintComment
        restComplaintCommentMockMvc.perform(get("/api/complaint-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComplaintComment() throws Exception {
        // Initialize the database
        complaintCommentRepository.saveAndFlush(complaintComment);
        int databaseSizeBeforeUpdate = complaintCommentRepository.findAll().size();

        // Update the complaintComment
        ComplaintComment updatedComplaintComment = complaintCommentRepository.findOne(complaintComment.getId());
        // Disconnect from session so that the updates on updatedComplaintComment are not directly saved in db
        em.detach(updatedComplaintComment);
        updatedComplaintComment
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .editedDate(UPDATED_EDITED_DATE)
            .deleted(UPDATED_DELETED);
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(updatedComplaintComment);

        restComplaintCommentMockMvc.perform(put("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isOk());

        // Validate the ComplaintComment in the database
        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeUpdate);
        ComplaintComment testComplaintComment = complaintCommentList.get(complaintCommentList.size() - 1);
        assertThat(testComplaintComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComplaintComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testComplaintComment.getEditedDate()).isEqualTo(UPDATED_EDITED_DATE);
        assertThat(testComplaintComment.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingComplaintComment() throws Exception {
        int databaseSizeBeforeUpdate = complaintCommentRepository.findAll().size();

        // Create the ComplaintComment
        ComplaintCommentDTO complaintCommentDTO = complaintCommentMapper.toDto(complaintComment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restComplaintCommentMockMvc.perform(put("/api/complaint-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintCommentDTO)))
            .andExpect(status().isCreated());

        // Validate the ComplaintComment in the database
        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteComplaintComment() throws Exception {
        // Initialize the database
        complaintCommentRepository.saveAndFlush(complaintComment);
        int databaseSizeBeforeDelete = complaintCommentRepository.findAll().size();

        // Get the complaintComment
        restComplaintCommentMockMvc.perform(delete("/api/complaint-comments/{id}", complaintComment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ComplaintComment> complaintCommentList = complaintCommentRepository.findAll();
        assertThat(complaintCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComplaintComment.class);
        ComplaintComment complaintComment1 = new ComplaintComment();
        complaintComment1.setId(1L);
        ComplaintComment complaintComment2 = new ComplaintComment();
        complaintComment2.setId(complaintComment1.getId());
        assertThat(complaintComment1).isEqualTo(complaintComment2);
        complaintComment2.setId(2L);
        assertThat(complaintComment1).isNotEqualTo(complaintComment2);
        complaintComment1.setId(null);
        assertThat(complaintComment1).isNotEqualTo(complaintComment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComplaintCommentDTO.class);
        ComplaintCommentDTO complaintCommentDTO1 = new ComplaintCommentDTO();
        complaintCommentDTO1.setId(1L);
        ComplaintCommentDTO complaintCommentDTO2 = new ComplaintCommentDTO();
        assertThat(complaintCommentDTO1).isNotEqualTo(complaintCommentDTO2);
        complaintCommentDTO2.setId(complaintCommentDTO1.getId());
        assertThat(complaintCommentDTO1).isEqualTo(complaintCommentDTO2);
        complaintCommentDTO2.setId(2L);
        assertThat(complaintCommentDTO1).isNotEqualTo(complaintCommentDTO2);
        complaintCommentDTO1.setId(null);
        assertThat(complaintCommentDTO1).isNotEqualTo(complaintCommentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(complaintCommentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(complaintCommentMapper.fromId(null)).isNull();
    }
}
