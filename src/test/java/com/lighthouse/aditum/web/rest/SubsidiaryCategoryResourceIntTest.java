package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.SubsidiaryCategory;
import com.lighthouse.aditum.repository.SubsidiaryCategoryRepository;
import com.lighthouse.aditum.service.SubsidiaryCategoryService;
import com.lighthouse.aditum.service.dto.SubsidiaryCategoryDTO;
import com.lighthouse.aditum.service.mapper.SubsidiaryCategoryMapper;
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
 * Test class for the SubsidiaryCategoryResource REST controller.
 *
 * @see SubsidiaryCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class SubsidiaryCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CATEGORY_TYPE = 1;
    private static final Integer UPDATED_CATEGORY_TYPE = 2;

    @Autowired
    private SubsidiaryCategoryRepository subsidiaryCategoryRepository;

    @Autowired
    private SubsidiaryCategoryMapper subsidiaryCategoryMapper;

    @Autowired
    private SubsidiaryCategoryService subsidiaryCategoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubsidiaryCategoryMockMvc;

    private SubsidiaryCategory subsidiaryCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubsidiaryCategoryResource subsidiaryCategoryResource = new SubsidiaryCategoryResource(subsidiaryCategoryService);
        this.restSubsidiaryCategoryMockMvc = MockMvcBuilders.standaloneSetup(subsidiaryCategoryResource)
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
    public static SubsidiaryCategory createEntity(EntityManager em) {
        SubsidiaryCategory subsidiaryCategory = new SubsidiaryCategory()
            .name(DEFAULT_NAME)
            .categoryType(DEFAULT_CATEGORY_TYPE);
        return subsidiaryCategory;
    }

    @Before
    public void initTest() {
        subsidiaryCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubsidiaryCategory() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryCategoryRepository.findAll().size();

        // Create the SubsidiaryCategory
        SubsidiaryCategoryDTO subsidiaryCategoryDTO = subsidiaryCategoryMapper.toDto(subsidiaryCategory);
        restSubsidiaryCategoryMockMvc.perform(post("/api/subsidiary-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SubsidiaryCategory in the database
        List<SubsidiaryCategory> subsidiaryCategoryList = subsidiaryCategoryRepository.findAll();
        assertThat(subsidiaryCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        SubsidiaryCategory testSubsidiaryCategory = subsidiaryCategoryList.get(subsidiaryCategoryList.size() - 1);
        assertThat(testSubsidiaryCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubsidiaryCategory.getCategoryType()).isEqualTo(DEFAULT_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    public void createSubsidiaryCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryCategoryRepository.findAll().size();

        // Create the SubsidiaryCategory with an existing ID
        subsidiaryCategory.setId(1L);
        SubsidiaryCategoryDTO subsidiaryCategoryDTO = subsidiaryCategoryMapper.toDto(subsidiaryCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubsidiaryCategoryMockMvc.perform(post("/api/subsidiary-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubsidiaryCategory in the database
        List<SubsidiaryCategory> subsidiaryCategoryList = subsidiaryCategoryRepository.findAll();
        assertThat(subsidiaryCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subsidiaryCategoryRepository.findAll().size();
        // set the field null
        subsidiaryCategory.setName(null);

        // Create the SubsidiaryCategory, which fails.
        SubsidiaryCategoryDTO subsidiaryCategoryDTO = subsidiaryCategoryMapper.toDto(subsidiaryCategory);

        restSubsidiaryCategoryMockMvc.perform(post("/api/subsidiary-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<SubsidiaryCategory> subsidiaryCategoryList = subsidiaryCategoryRepository.findAll();
        assertThat(subsidiaryCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubsidiaryCategories() throws Exception {
        // Initialize the database
        subsidiaryCategoryRepository.saveAndFlush(subsidiaryCategory);

        // Get all the subsidiaryCategoryList
        restSubsidiaryCategoryMockMvc.perform(get("/api/subsidiary-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subsidiaryCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].categoryType").value(hasItem(DEFAULT_CATEGORY_TYPE)));
    }

    @Test
    @Transactional
    public void getSubsidiaryCategory() throws Exception {
        // Initialize the database
        subsidiaryCategoryRepository.saveAndFlush(subsidiaryCategory);

        // Get the subsidiaryCategory
        restSubsidiaryCategoryMockMvc.perform(get("/api/subsidiary-categories/{id}", subsidiaryCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subsidiaryCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.categoryType").value(DEFAULT_CATEGORY_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingSubsidiaryCategory() throws Exception {
        // Get the subsidiaryCategory
        restSubsidiaryCategoryMockMvc.perform(get("/api/subsidiary-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubsidiaryCategory() throws Exception {
        // Initialize the database
        subsidiaryCategoryRepository.saveAndFlush(subsidiaryCategory);
        int databaseSizeBeforeUpdate = subsidiaryCategoryRepository.findAll().size();

        // Update the subsidiaryCategory
        SubsidiaryCategory updatedSubsidiaryCategory = subsidiaryCategoryRepository.findOne(subsidiaryCategory.getId());
        // Disconnect from session so that the updates on updatedSubsidiaryCategory are not directly saved in db
        em.detach(updatedSubsidiaryCategory);
        updatedSubsidiaryCategory
            .name(UPDATED_NAME)
            .categoryType(UPDATED_CATEGORY_TYPE);
        SubsidiaryCategoryDTO subsidiaryCategoryDTO = subsidiaryCategoryMapper.toDto(updatedSubsidiaryCategory);

        restSubsidiaryCategoryMockMvc.perform(put("/api/subsidiary-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the SubsidiaryCategory in the database
        List<SubsidiaryCategory> subsidiaryCategoryList = subsidiaryCategoryRepository.findAll();
        assertThat(subsidiaryCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubsidiaryCategory testSubsidiaryCategory = subsidiaryCategoryList.get(subsidiaryCategoryList.size() - 1);
        assertThat(testSubsidiaryCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubsidiaryCategory.getCategoryType()).isEqualTo(UPDATED_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubsidiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = subsidiaryCategoryRepository.findAll().size();

        // Create the SubsidiaryCategory
        SubsidiaryCategoryDTO subsidiaryCategoryDTO = subsidiaryCategoryMapper.toDto(subsidiaryCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubsidiaryCategoryMockMvc.perform(put("/api/subsidiary-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the SubsidiaryCategory in the database
        List<SubsidiaryCategory> subsidiaryCategoryList = subsidiaryCategoryRepository.findAll();
        assertThat(subsidiaryCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubsidiaryCategory() throws Exception {
        // Initialize the database
        subsidiaryCategoryRepository.saveAndFlush(subsidiaryCategory);
        int databaseSizeBeforeDelete = subsidiaryCategoryRepository.findAll().size();

        // Get the subsidiaryCategory
        restSubsidiaryCategoryMockMvc.perform(delete("/api/subsidiary-categories/{id}", subsidiaryCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubsidiaryCategory> subsidiaryCategoryList = subsidiaryCategoryRepository.findAll();
        assertThat(subsidiaryCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubsidiaryCategory.class);
        SubsidiaryCategory subsidiaryCategory1 = new SubsidiaryCategory();
        subsidiaryCategory1.setId(1L);
        SubsidiaryCategory subsidiaryCategory2 = new SubsidiaryCategory();
        subsidiaryCategory2.setId(subsidiaryCategory1.getId());
        assertThat(subsidiaryCategory1).isEqualTo(subsidiaryCategory2);
        subsidiaryCategory2.setId(2L);
        assertThat(subsidiaryCategory1).isNotEqualTo(subsidiaryCategory2);
        subsidiaryCategory1.setId(null);
        assertThat(subsidiaryCategory1).isNotEqualTo(subsidiaryCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubsidiaryCategoryDTO.class);
        SubsidiaryCategoryDTO subsidiaryCategoryDTO1 = new SubsidiaryCategoryDTO();
        subsidiaryCategoryDTO1.setId(1L);
        SubsidiaryCategoryDTO subsidiaryCategoryDTO2 = new SubsidiaryCategoryDTO();
        assertThat(subsidiaryCategoryDTO1).isNotEqualTo(subsidiaryCategoryDTO2);
        subsidiaryCategoryDTO2.setId(subsidiaryCategoryDTO1.getId());
        assertThat(subsidiaryCategoryDTO1).isEqualTo(subsidiaryCategoryDTO2);
        subsidiaryCategoryDTO2.setId(2L);
        assertThat(subsidiaryCategoryDTO1).isNotEqualTo(subsidiaryCategoryDTO2);
        subsidiaryCategoryDTO1.setId(null);
        assertThat(subsidiaryCategoryDTO1).isNotEqualTo(subsidiaryCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subsidiaryCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subsidiaryCategoryMapper.fromId(null)).isNull();
    }
}
