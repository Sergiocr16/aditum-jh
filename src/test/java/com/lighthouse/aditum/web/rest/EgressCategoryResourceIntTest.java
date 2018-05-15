package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.EgressCategory;
import com.lighthouse.aditum.repository.EgressCategoryRepository;
import com.lighthouse.aditum.service.EgressCategoryService;
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
 * Test class for the EgressCategoryResource REST controller.
 *
 * @see EgressCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class EgressCategoryResourceIntTest {

    private static final String DEFAULT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private EgressCategoryRepository egressCategoryRepository;

    @Autowired
    private EgressCategoryService egressCategoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEgressCategoryMockMvc;

    private EgressCategory egressCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EgressCategoryResource egressCategoryResource = new EgressCategoryResource(egressCategoryService);
        this.restEgressCategoryMockMvc = MockMvcBuilders.standaloneSetup(egressCategoryResource)
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
    public static EgressCategory createEntity(EntityManager em) {
        EgressCategory egressCategory = new EgressCategory()
            .group(DEFAULT_GROUP)
            .category(DEFAULT_CATEGORY);
        return egressCategory;
    }

    @Before
    public void initTest() {
        egressCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createEgressCategory() throws Exception {
        int databaseSizeBeforeCreate = egressCategoryRepository.findAll().size();

        // Create the EgressCategory
        restEgressCategoryMockMvc.perform(post("/api/egress-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressCategory)))
            .andExpect(status().isCreated());

        // Validate the EgressCategory in the database
        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        EgressCategory testEgressCategory = egressCategoryList.get(egressCategoryList.size() - 1);
        assertThat(testEgressCategory.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testEgressCategory.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    public void createEgressCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = egressCategoryRepository.findAll().size();

        // Create the EgressCategory with an existing ID
        egressCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgressCategoryMockMvc.perform(post("/api/egress-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressCategory)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressCategoryRepository.findAll().size();
        // set the field null
        egressCategory.setGroup(null);

        // Create the EgressCategory, which fails.

        restEgressCategoryMockMvc.perform(post("/api/egress-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressCategory)))
            .andExpect(status().isBadRequest());

        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressCategoryRepository.findAll().size();
        // set the field null
        egressCategory.setCategory(null);

        // Create the EgressCategory, which fails.

        restEgressCategoryMockMvc.perform(post("/api/egress-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressCategory)))
            .andExpect(status().isBadRequest());

        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEgressCategories() throws Exception {
        // Initialize the database
        egressCategoryRepository.saveAndFlush(egressCategory);

        // Get all the egressCategoryList
        restEgressCategoryMockMvc.perform(get("/api/egress-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egressCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getEgressCategory() throws Exception {
        // Initialize the database
        egressCategoryRepository.saveAndFlush(egressCategory);

        // Get the egressCategory
        restEgressCategoryMockMvc.perform(get("/api/egress-categories/{id}", egressCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(egressCategory.getId().intValue()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEgressCategory() throws Exception {
        // Get the egressCategory
        restEgressCategoryMockMvc.perform(get("/api/egress-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEgressCategory() throws Exception {
        // Initialize the database
        egressCategoryService.save(egressCategory);

        int databaseSizeBeforeUpdate = egressCategoryRepository.findAll().size();

        // Update the egressCategory
        EgressCategory updatedEgressCategory = egressCategoryRepository.findOne(egressCategory.getId());
        updatedEgressCategory
            .group(UPDATED_GROUP)
            .category(UPDATED_CATEGORY);

        restEgressCategoryMockMvc.perform(put("/api/egress-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEgressCategory)))
            .andExpect(status().isOk());

        // Validate the EgressCategory in the database
        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeUpdate);
        EgressCategory testEgressCategory = egressCategoryList.get(egressCategoryList.size() - 1);
        assertThat(testEgressCategory.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testEgressCategory.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void updateNonExistingEgressCategory() throws Exception {
        int databaseSizeBeforeUpdate = egressCategoryRepository.findAll().size();

        // Create the EgressCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEgressCategoryMockMvc.perform(put("/api/egress-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressCategory)))
            .andExpect(status().isCreated());

        // Validate the EgressCategory in the database
        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEgressCategory() throws Exception {
        // Initialize the database
        egressCategoryService.save(egressCategory);

        int databaseSizeBeforeDelete = egressCategoryRepository.findAll().size();

        // Get the egressCategory
        restEgressCategoryMockMvc.perform(delete("/api/egress-categories/{id}", egressCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EgressCategory> egressCategoryList = egressCategoryRepository.findAll();
        assertThat(egressCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EgressCategory.class);
        EgressCategory egressCategory1 = new EgressCategory();
        egressCategory1.setId(1L);
        EgressCategory egressCategory2 = new EgressCategory();
        egressCategory2.setId(egressCategory1.getId());
        assertThat(egressCategory1).isEqualTo(egressCategory2);
        egressCategory2.setId(2L);
        assertThat(egressCategory1).isNotEqualTo(egressCategory2);
        egressCategory1.setId(null);
        assertThat(egressCategory1).isNotEqualTo(egressCategory2);
    }
}
