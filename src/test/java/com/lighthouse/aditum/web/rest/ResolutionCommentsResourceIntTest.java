package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ResolutionComments;
import com.lighthouse.aditum.repository.ResolutionCommentsRepository;
import com.lighthouse.aditum.service.ResolutionCommentsService;
import com.lighthouse.aditum.service.dto.ResolutionCommentsDTO;
import com.lighthouse.aditum.service.mapper.ResolutionCommentsMapper;
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
 * Test class for the ResolutionCommentsResource REST controller.
 *
 * @see ResolutionCommentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ResolutionCommentsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EDITED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EDITED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private ResolutionCommentsRepository resolutionCommentsRepository;

    @Autowired
    private ResolutionCommentsMapper resolutionCommentsMapper;

    @Autowired
    private ResolutionCommentsService resolutionCommentsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResolutionCommentsMockMvc;

    private ResolutionComments resolutionComments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResolutionCommentsResource resolutionCommentsResource = new ResolutionCommentsResource(resolutionCommentsService);
        this.restResolutionCommentsMockMvc = MockMvcBuilders.standaloneSetup(resolutionCommentsResource)
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
    public static ResolutionComments createEntity(EntityManager em) {
        ResolutionComments resolutionComments = new ResolutionComments()
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .editedDate(DEFAULT_EDITED_DATE)
            .deleted(DEFAULT_DELETED);
        return resolutionComments;
    }

    @Before
    public void initTest() {
        resolutionComments = createEntity(em);
    }

    @Test
    @Transactional
    public void createResolutionComments() throws Exception {
        int databaseSizeBeforeCreate = resolutionCommentsRepository.findAll().size();

        // Create the ResolutionComments
        ResolutionCommentsDTO resolutionCommentsDTO = resolutionCommentsMapper.toDto(resolutionComments);
        restResolutionCommentsMockMvc.perform(post("/api/resolution-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionCommentsDTO)))
            .andExpect(status().isCreated());

        // Validate the ResolutionComments in the database
        List<ResolutionComments> resolutionCommentsList = resolutionCommentsRepository.findAll();
        assertThat(resolutionCommentsList).hasSize(databaseSizeBeforeCreate + 1);
        ResolutionComments testResolutionComments = resolutionCommentsList.get(resolutionCommentsList.size() - 1);
        assertThat(testResolutionComments.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testResolutionComments.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testResolutionComments.getEditedDate()).isEqualTo(DEFAULT_EDITED_DATE);
        assertThat(testResolutionComments.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createResolutionCommentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resolutionCommentsRepository.findAll().size();

        // Create the ResolutionComments with an existing ID
        resolutionComments.setId(1L);
        ResolutionCommentsDTO resolutionCommentsDTO = resolutionCommentsMapper.toDto(resolutionComments);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResolutionCommentsMockMvc.perform(post("/api/resolution-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionCommentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResolutionComments in the database
        List<ResolutionComments> resolutionCommentsList = resolutionCommentsRepository.findAll();
        assertThat(resolutionCommentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllResolutionComments() throws Exception {
        // Initialize the database
        resolutionCommentsRepository.saveAndFlush(resolutionComments);

        // Get all the resolutionCommentsList
        restResolutionCommentsMockMvc.perform(get("/api/resolution-comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resolutionComments.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].editedDate").value(hasItem(sameInstant(DEFAULT_EDITED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getResolutionComments() throws Exception {
        // Initialize the database
        resolutionCommentsRepository.saveAndFlush(resolutionComments);

        // Get the resolutionComments
        restResolutionCommentsMockMvc.perform(get("/api/resolution-comments/{id}", resolutionComments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resolutionComments.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.editedDate").value(sameInstant(DEFAULT_EDITED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingResolutionComments() throws Exception {
        // Get the resolutionComments
        restResolutionCommentsMockMvc.perform(get("/api/resolution-comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResolutionComments() throws Exception {
        // Initialize the database
        resolutionCommentsRepository.saveAndFlush(resolutionComments);
        int databaseSizeBeforeUpdate = resolutionCommentsRepository.findAll().size();

        // Update the resolutionComments
        ResolutionComments updatedResolutionComments = resolutionCommentsRepository.findOne(resolutionComments.getId());
        // Disconnect from session so that the updates on updatedResolutionComments are not directly saved in db
        em.detach(updatedResolutionComments);
        updatedResolutionComments
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .editedDate(UPDATED_EDITED_DATE)
            .deleted(UPDATED_DELETED);
        ResolutionCommentsDTO resolutionCommentsDTO = resolutionCommentsMapper.toDto(updatedResolutionComments);

        restResolutionCommentsMockMvc.perform(put("/api/resolution-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionCommentsDTO)))
            .andExpect(status().isOk());

        // Validate the ResolutionComments in the database
        List<ResolutionComments> resolutionCommentsList = resolutionCommentsRepository.findAll();
        assertThat(resolutionCommentsList).hasSize(databaseSizeBeforeUpdate);
        ResolutionComments testResolutionComments = resolutionCommentsList.get(resolutionCommentsList.size() - 1);
        assertThat(testResolutionComments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testResolutionComments.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testResolutionComments.getEditedDate()).isEqualTo(UPDATED_EDITED_DATE);
        assertThat(testResolutionComments.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingResolutionComments() throws Exception {
        int databaseSizeBeforeUpdate = resolutionCommentsRepository.findAll().size();

        // Create the ResolutionComments
        ResolutionCommentsDTO resolutionCommentsDTO = resolutionCommentsMapper.toDto(resolutionComments);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResolutionCommentsMockMvc.perform(put("/api/resolution-comments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resolutionCommentsDTO)))
            .andExpect(status().isCreated());

        // Validate the ResolutionComments in the database
        List<ResolutionComments> resolutionCommentsList = resolutionCommentsRepository.findAll();
        assertThat(resolutionCommentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResolutionComments() throws Exception {
        // Initialize the database
        resolutionCommentsRepository.saveAndFlush(resolutionComments);
        int databaseSizeBeforeDelete = resolutionCommentsRepository.findAll().size();

        // Get the resolutionComments
        restResolutionCommentsMockMvc.perform(delete("/api/resolution-comments/{id}", resolutionComments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ResolutionComments> resolutionCommentsList = resolutionCommentsRepository.findAll();
        assertThat(resolutionCommentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResolutionComments.class);
        ResolutionComments resolutionComments1 = new ResolutionComments();
        resolutionComments1.setId(1L);
        ResolutionComments resolutionComments2 = new ResolutionComments();
        resolutionComments2.setId(resolutionComments1.getId());
        assertThat(resolutionComments1).isEqualTo(resolutionComments2);
        resolutionComments2.setId(2L);
        assertThat(resolutionComments1).isNotEqualTo(resolutionComments2);
        resolutionComments1.setId(null);
        assertThat(resolutionComments1).isNotEqualTo(resolutionComments2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResolutionCommentsDTO.class);
        ResolutionCommentsDTO resolutionCommentsDTO1 = new ResolutionCommentsDTO();
        resolutionCommentsDTO1.setId(1L);
        ResolutionCommentsDTO resolutionCommentsDTO2 = new ResolutionCommentsDTO();
        assertThat(resolutionCommentsDTO1).isNotEqualTo(resolutionCommentsDTO2);
        resolutionCommentsDTO2.setId(resolutionCommentsDTO1.getId());
        assertThat(resolutionCommentsDTO1).isEqualTo(resolutionCommentsDTO2);
        resolutionCommentsDTO2.setId(2L);
        assertThat(resolutionCommentsDTO1).isNotEqualTo(resolutionCommentsDTO2);
        resolutionCommentsDTO1.setId(null);
        assertThat(resolutionCommentsDTO1).isNotEqualTo(resolutionCommentsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resolutionCommentsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resolutionCommentsMapper.fromId(null)).isNull();
    }
}
