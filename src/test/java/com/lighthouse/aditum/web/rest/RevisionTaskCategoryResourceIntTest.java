package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.RevisionTaskCategory;
import com.lighthouse.aditum.repository.RevisionTaskCategoryRepository;
import com.lighthouse.aditum.service.RevisionTaskCategoryService;
import com.lighthouse.aditum.service.dto.RevisionTaskCategoryDTO;
import com.lighthouse.aditum.service.mapper.RevisionTaskCategoryMapper;
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
 * Test class for the RevisionTaskCategoryResource REST controller.
 *
 * @see RevisionTaskCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RevisionTaskCategoryResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    @Autowired
    private RevisionTaskCategoryRepository revisionTaskCategoryRepository;

    @Autowired
    private RevisionTaskCategoryMapper revisionTaskCategoryMapper;

    @Autowired
    private RevisionTaskCategoryService revisionTaskCategoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRevisionTaskCategoryMockMvc;

    private RevisionTaskCategory revisionTaskCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevisionTaskCategoryResource revisionTaskCategoryResource = new RevisionTaskCategoryResource(revisionTaskCategoryService);
        this.restRevisionTaskCategoryMockMvc = MockMvcBuilders.standaloneSetup(revisionTaskCategoryResource)
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
    public static RevisionTaskCategory createEntity(EntityManager em) {
        RevisionTaskCategory revisionTaskCategory = new RevisionTaskCategory()
            .description(DEFAULT_DESCRIPTION)
            .deleted(DEFAULT_DELETED)
            .order(DEFAULT_ORDER);
        return revisionTaskCategory;
    }

    @Before
    public void initTest() {
        revisionTaskCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevisionTaskCategory() throws Exception {
        int databaseSizeBeforeCreate = revisionTaskCategoryRepository.findAll().size();

        // Create the RevisionTaskCategory
        RevisionTaskCategoryDTO revisionTaskCategoryDTO = revisionTaskCategoryMapper.toDto(revisionTaskCategory);
        restRevisionTaskCategoryMockMvc.perform(post("/api/revision-task-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionTaskCategory in the database
        List<RevisionTaskCategory> revisionTaskCategoryList = revisionTaskCategoryRepository.findAll();
        assertThat(revisionTaskCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        RevisionTaskCategory testRevisionTaskCategory = revisionTaskCategoryList.get(revisionTaskCategoryList.size() - 1);
        assertThat(testRevisionTaskCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRevisionTaskCategory.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testRevisionTaskCategory.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    public void createRevisionTaskCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revisionTaskCategoryRepository.findAll().size();

        // Create the RevisionTaskCategory with an existing ID
        revisionTaskCategory.setId(1L);
        RevisionTaskCategoryDTO revisionTaskCategoryDTO = revisionTaskCategoryMapper.toDto(revisionTaskCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionTaskCategoryMockMvc.perform(post("/api/revision-task-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RevisionTaskCategory in the database
        List<RevisionTaskCategory> revisionTaskCategoryList = revisionTaskCategoryRepository.findAll();
        assertThat(revisionTaskCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRevisionTaskCategories() throws Exception {
        // Initialize the database
        revisionTaskCategoryRepository.saveAndFlush(revisionTaskCategory);

        // Get all the revisionTaskCategoryList
        restRevisionTaskCategoryMockMvc.perform(get("/api/revision-task-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revisionTaskCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    public void getRevisionTaskCategory() throws Exception {
        // Initialize the database
        revisionTaskCategoryRepository.saveAndFlush(revisionTaskCategory);

        // Get the revisionTaskCategory
        restRevisionTaskCategoryMockMvc.perform(get("/api/revision-task-categories/{id}", revisionTaskCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revisionTaskCategory.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingRevisionTaskCategory() throws Exception {
        // Get the revisionTaskCategory
        restRevisionTaskCategoryMockMvc.perform(get("/api/revision-task-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevisionTaskCategory() throws Exception {
        // Initialize the database
        revisionTaskCategoryRepository.saveAndFlush(revisionTaskCategory);
        int databaseSizeBeforeUpdate = revisionTaskCategoryRepository.findAll().size();

        // Update the revisionTaskCategory
        RevisionTaskCategory updatedRevisionTaskCategory = revisionTaskCategoryRepository.findOne(revisionTaskCategory.getId());
        // Disconnect from session so that the updates on updatedRevisionTaskCategory are not directly saved in db
        em.detach(updatedRevisionTaskCategory);
        updatedRevisionTaskCategory
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED)
            .order(UPDATED_ORDER);
        RevisionTaskCategoryDTO revisionTaskCategoryDTO = revisionTaskCategoryMapper.toDto(updatedRevisionTaskCategory);

        restRevisionTaskCategoryMockMvc.perform(put("/api/revision-task-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the RevisionTaskCategory in the database
        List<RevisionTaskCategory> revisionTaskCategoryList = revisionTaskCategoryRepository.findAll();
        assertThat(revisionTaskCategoryList).hasSize(databaseSizeBeforeUpdate);
        RevisionTaskCategory testRevisionTaskCategory = revisionTaskCategoryList.get(revisionTaskCategoryList.size() - 1);
        assertThat(testRevisionTaskCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRevisionTaskCategory.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testRevisionTaskCategory.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingRevisionTaskCategory() throws Exception {
        int databaseSizeBeforeUpdate = revisionTaskCategoryRepository.findAll().size();

        // Create the RevisionTaskCategory
        RevisionTaskCategoryDTO revisionTaskCategoryDTO = revisionTaskCategoryMapper.toDto(revisionTaskCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRevisionTaskCategoryMockMvc.perform(put("/api/revision-task-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionTaskCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionTaskCategory in the database
        List<RevisionTaskCategory> revisionTaskCategoryList = revisionTaskCategoryRepository.findAll();
        assertThat(revisionTaskCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRevisionTaskCategory() throws Exception {
        // Initialize the database
        revisionTaskCategoryRepository.saveAndFlush(revisionTaskCategory);
        int databaseSizeBeforeDelete = revisionTaskCategoryRepository.findAll().size();

        // Get the revisionTaskCategory
        restRevisionTaskCategoryMockMvc.perform(delete("/api/revision-task-categories/{id}", revisionTaskCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RevisionTaskCategory> revisionTaskCategoryList = revisionTaskCategoryRepository.findAll();
        assertThat(revisionTaskCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionTaskCategory.class);
        RevisionTaskCategory revisionTaskCategory1 = new RevisionTaskCategory();
        revisionTaskCategory1.setId(1L);
        RevisionTaskCategory revisionTaskCategory2 = new RevisionTaskCategory();
        revisionTaskCategory2.setId(revisionTaskCategory1.getId());
        assertThat(revisionTaskCategory1).isEqualTo(revisionTaskCategory2);
        revisionTaskCategory2.setId(2L);
        assertThat(revisionTaskCategory1).isNotEqualTo(revisionTaskCategory2);
        revisionTaskCategory1.setId(null);
        assertThat(revisionTaskCategory1).isNotEqualTo(revisionTaskCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionTaskCategoryDTO.class);
        RevisionTaskCategoryDTO revisionTaskCategoryDTO1 = new RevisionTaskCategoryDTO();
        revisionTaskCategoryDTO1.setId(1L);
        RevisionTaskCategoryDTO revisionTaskCategoryDTO2 = new RevisionTaskCategoryDTO();
        assertThat(revisionTaskCategoryDTO1).isNotEqualTo(revisionTaskCategoryDTO2);
        revisionTaskCategoryDTO2.setId(revisionTaskCategoryDTO1.getId());
        assertThat(revisionTaskCategoryDTO1).isEqualTo(revisionTaskCategoryDTO2);
        revisionTaskCategoryDTO2.setId(2L);
        assertThat(revisionTaskCategoryDTO1).isNotEqualTo(revisionTaskCategoryDTO2);
        revisionTaskCategoryDTO1.setId(null);
        assertThat(revisionTaskCategoryDTO1).isNotEqualTo(revisionTaskCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(revisionTaskCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(revisionTaskCategoryMapper.fromId(null)).isNull();
    }
}
