package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.KeyWords;
import com.lighthouse.aditum.repository.KeyWordsRepository;
import com.lighthouse.aditum.service.KeyWordsService;
import com.lighthouse.aditum.service.dto.KeyWordsDTO;
import com.lighthouse.aditum.service.mapper.KeyWordsMapper;
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
 * Test class for the KeyWordsResource REST controller.
 *
 * @see KeyWordsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class KeyWordsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private KeyWordsRepository keyWordsRepository;

    @Autowired
    private KeyWordsMapper keyWordsMapper;

    @Autowired
    private KeyWordsService keyWordsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKeyWordsMockMvc;

    private KeyWords keyWords;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KeyWordsResource keyWordsResource = new KeyWordsResource(keyWordsService);
        this.restKeyWordsMockMvc = MockMvcBuilders.standaloneSetup(keyWordsResource)
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
    public static KeyWords createEntity(EntityManager em) {
        KeyWords keyWords = new KeyWords()
            .name(DEFAULT_NAME)
            .deleted(DEFAULT_DELETED);
        return keyWords;
    }

    @Before
    public void initTest() {
        keyWords = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeyWords() throws Exception {
        int databaseSizeBeforeCreate = keyWordsRepository.findAll().size();

        // Create the KeyWords
        KeyWordsDTO keyWordsDTO = keyWordsMapper.toDto(keyWords);
        restKeyWordsMockMvc.perform(post("/api/key-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyWordsDTO)))
            .andExpect(status().isCreated());

        // Validate the KeyWords in the database
        List<KeyWords> keyWordsList = keyWordsRepository.findAll();
        assertThat(keyWordsList).hasSize(databaseSizeBeforeCreate + 1);
        KeyWords testKeyWords = keyWordsList.get(keyWordsList.size() - 1);
        assertThat(testKeyWords.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKeyWords.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createKeyWordsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keyWordsRepository.findAll().size();

        // Create the KeyWords with an existing ID
        keyWords.setId(1L);
        KeyWordsDTO keyWordsDTO = keyWordsMapper.toDto(keyWords);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeyWordsMockMvc.perform(post("/api/key-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyWordsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KeyWords in the database
        List<KeyWords> keyWordsList = keyWordsRepository.findAll();
        assertThat(keyWordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = keyWordsRepository.findAll().size();
        // set the field null
        keyWords.setName(null);

        // Create the KeyWords, which fails.
        KeyWordsDTO keyWordsDTO = keyWordsMapper.toDto(keyWords);

        restKeyWordsMockMvc.perform(post("/api/key-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyWordsDTO)))
            .andExpect(status().isBadRequest());

        List<KeyWords> keyWordsList = keyWordsRepository.findAll();
        assertThat(keyWordsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKeyWords() throws Exception {
        // Initialize the database
        keyWordsRepository.saveAndFlush(keyWords);

        // Get all the keyWordsList
        restKeyWordsMockMvc.perform(get("/api/key-words?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keyWords.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getKeyWords() throws Exception {
        // Initialize the database
        keyWordsRepository.saveAndFlush(keyWords);

        // Get the keyWords
        restKeyWordsMockMvc.perform(get("/api/key-words/{id}", keyWords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(keyWords.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingKeyWords() throws Exception {
        // Get the keyWords
        restKeyWordsMockMvc.perform(get("/api/key-words/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeyWords() throws Exception {
        // Initialize the database
        keyWordsRepository.saveAndFlush(keyWords);
        int databaseSizeBeforeUpdate = keyWordsRepository.findAll().size();

        // Update the keyWords
        KeyWords updatedKeyWords = keyWordsRepository.findOne(keyWords.getId());
        // Disconnect from session so that the updates on updatedKeyWords are not directly saved in db
        em.detach(updatedKeyWords);
        updatedKeyWords
            .name(UPDATED_NAME)
            .deleted(UPDATED_DELETED);
        KeyWordsDTO keyWordsDTO = keyWordsMapper.toDto(updatedKeyWords);

        restKeyWordsMockMvc.perform(put("/api/key-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyWordsDTO)))
            .andExpect(status().isOk());

        // Validate the KeyWords in the database
        List<KeyWords> keyWordsList = keyWordsRepository.findAll();
        assertThat(keyWordsList).hasSize(databaseSizeBeforeUpdate);
        KeyWords testKeyWords = keyWordsList.get(keyWordsList.size() - 1);
        assertThat(testKeyWords.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKeyWords.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingKeyWords() throws Exception {
        int databaseSizeBeforeUpdate = keyWordsRepository.findAll().size();

        // Create the KeyWords
        KeyWordsDTO keyWordsDTO = keyWordsMapper.toDto(keyWords);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKeyWordsMockMvc.perform(put("/api/key-words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keyWordsDTO)))
            .andExpect(status().isCreated());

        // Validate the KeyWords in the database
        List<KeyWords> keyWordsList = keyWordsRepository.findAll();
        assertThat(keyWordsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteKeyWords() throws Exception {
        // Initialize the database
        keyWordsRepository.saveAndFlush(keyWords);
        int databaseSizeBeforeDelete = keyWordsRepository.findAll().size();

        // Get the keyWords
        restKeyWordsMockMvc.perform(delete("/api/key-words/{id}", keyWords.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<KeyWords> keyWordsList = keyWordsRepository.findAll();
        assertThat(keyWordsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyWords.class);
        KeyWords keyWords1 = new KeyWords();
        keyWords1.setId(1L);
        KeyWords keyWords2 = new KeyWords();
        keyWords2.setId(keyWords1.getId());
        assertThat(keyWords1).isEqualTo(keyWords2);
        keyWords2.setId(2L);
        assertThat(keyWords1).isNotEqualTo(keyWords2);
        keyWords1.setId(null);
        assertThat(keyWords1).isNotEqualTo(keyWords2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyWordsDTO.class);
        KeyWordsDTO keyWordsDTO1 = new KeyWordsDTO();
        keyWordsDTO1.setId(1L);
        KeyWordsDTO keyWordsDTO2 = new KeyWordsDTO();
        assertThat(keyWordsDTO1).isNotEqualTo(keyWordsDTO2);
        keyWordsDTO2.setId(keyWordsDTO1.getId());
        assertThat(keyWordsDTO1).isEqualTo(keyWordsDTO2);
        keyWordsDTO2.setId(2L);
        assertThat(keyWordsDTO1).isNotEqualTo(keyWordsDTO2);
        keyWordsDTO1.setId(null);
        assertThat(keyWordsDTO1).isNotEqualTo(keyWordsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(keyWordsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(keyWordsMapper.fromId(null)).isNull();
    }
}
