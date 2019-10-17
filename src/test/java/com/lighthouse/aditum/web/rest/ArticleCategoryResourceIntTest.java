package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ArticleCategory;
import com.lighthouse.aditum.repository.ArticleCategoryRepository;
import com.lighthouse.aditum.service.ArticleCategoryService;
import com.lighthouse.aditum.service.dto.ArticleCategoryDTO;
import com.lighthouse.aditum.service.mapper.ArticleCategoryMapper;
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
 * Test class for the ArticleCategoryResource REST controller.
 *
 * @see ArticleCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ArticleCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArticleCategoryMockMvc;

    private ArticleCategory articleCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArticleCategoryResource articleCategoryResource = new ArticleCategoryResource(articleCategoryService);
        this.restArticleCategoryMockMvc = MockMvcBuilders.standaloneSetup(articleCategoryResource)
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
    public static ArticleCategory createEntity(EntityManager em) {
        ArticleCategory articleCategory = new ArticleCategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .deleted(DEFAULT_DELETED);
        return articleCategory;
    }

    @Before
    public void initTest() {
        articleCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticleCategory() throws Exception {
        int databaseSizeBeforeCreate = articleCategoryRepository.findAll().size();

        // Create the ArticleCategory
        ArticleCategoryDTO articleCategoryDTO = articleCategoryMapper.toDto(articleCategory);
        restArticleCategoryMockMvc.perform(post("/api/article-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(articleCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ArticleCategory testArticleCategory = articleCategoryList.get(articleCategoryList.size() - 1);
        assertThat(testArticleCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArticleCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArticleCategory.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createArticleCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleCategoryRepository.findAll().size();

        // Create the ArticleCategory with an existing ID
        articleCategory.setId(1L);
        ArticleCategoryDTO articleCategoryDTO = articleCategoryMapper.toDto(articleCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleCategoryMockMvc.perform(post("/api/article-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(articleCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArticleCategories() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        // Get all the articleCategoryList
        restArticleCategoryMockMvc.perform(get("/api/article-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        // Get the articleCategory
        restArticleCategoryMockMvc.perform(get("/api/article-categories/{id}", articleCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(articleCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingArticleCategory() throws Exception {
        // Get the articleCategory
        restArticleCategoryMockMvc.perform(get("/api/article-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();

        // Update the articleCategory
        ArticleCategory updatedArticleCategory = articleCategoryRepository.findOne(articleCategory.getId());
        // Disconnect from session so that the updates on updatedArticleCategory are not directly saved in db
        em.detach(updatedArticleCategory);
        updatedArticleCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED);
        ArticleCategoryDTO articleCategoryDTO = articleCategoryMapper.toDto(updatedArticleCategory);

        restArticleCategoryMockMvc.perform(put("/api/article-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(articleCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate);
        ArticleCategory testArticleCategory = articleCategoryList.get(articleCategoryList.size() - 1);
        assertThat(testArticleCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArticleCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticleCategory.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingArticleCategory() throws Exception {
        int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();

        // Create the ArticleCategory
        ArticleCategoryDTO articleCategoryDTO = articleCategoryMapper.toDto(articleCategory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArticleCategoryMockMvc.perform(put("/api/article-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(articleCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);
        int databaseSizeBeforeDelete = articleCategoryRepository.findAll().size();

        // Get the articleCategory
        restArticleCategoryMockMvc.perform(delete("/api/article-categories/{id}", articleCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ArticleCategory> articleCategoryList = articleCategoryRepository.findAll();
        assertThat(articleCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleCategory.class);
        ArticleCategory articleCategory1 = new ArticleCategory();
        articleCategory1.setId(1L);
        ArticleCategory articleCategory2 = new ArticleCategory();
        articleCategory2.setId(articleCategory1.getId());
        assertThat(articleCategory1).isEqualTo(articleCategory2);
        articleCategory2.setId(2L);
        assertThat(articleCategory1).isNotEqualTo(articleCategory2);
        articleCategory1.setId(null);
        assertThat(articleCategory1).isNotEqualTo(articleCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArticleCategoryDTO.class);
        ArticleCategoryDTO articleCategoryDTO1 = new ArticleCategoryDTO();
        articleCategoryDTO1.setId(1L);
        ArticleCategoryDTO articleCategoryDTO2 = new ArticleCategoryDTO();
        assertThat(articleCategoryDTO1).isNotEqualTo(articleCategoryDTO2);
        articleCategoryDTO2.setId(articleCategoryDTO1.getId());
        assertThat(articleCategoryDTO1).isEqualTo(articleCategoryDTO2);
        articleCategoryDTO2.setId(2L);
        assertThat(articleCategoryDTO1).isNotEqualTo(articleCategoryDTO2);
        articleCategoryDTO1.setId(null);
        assertThat(articleCategoryDTO1).isNotEqualTo(articleCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(articleCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(articleCategoryMapper.fromId(null)).isNull();
    }
}
