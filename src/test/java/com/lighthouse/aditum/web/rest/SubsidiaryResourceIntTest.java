package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Subsidiary;
import com.lighthouse.aditum.repository.SubsidiaryRepository;
import com.lighthouse.aditum.service.SubsidiaryService;
import com.lighthouse.aditum.service.dto.SubsidiaryDTO;
import com.lighthouse.aditum.service.mapper.SubsidiaryMapper;
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
 * Test class for the SubsidiaryResource REST controller.
 *
 * @see SubsidiaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class SubsidiaryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SubsidiaryRepository subsidiaryRepository;

    @Autowired
    private SubsidiaryMapper subsidiaryMapper;

    @Autowired
    private SubsidiaryService subsidiaryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubsidiaryMockMvc;

    private Subsidiary subsidiary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubsidiaryResource subsidiaryResource = new SubsidiaryResource(subsidiaryService);
        this.restSubsidiaryMockMvc = MockMvcBuilders.standaloneSetup(subsidiaryResource)
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
    public static Subsidiary createEntity(EntityManager em) {
        Subsidiary subsidiary = new Subsidiary()
            .name(DEFAULT_NAME)
            .deleted(DEFAULT_DELETED)
            .description(DEFAULT_DESCRIPTION);
        return subsidiary;
    }

    @Before
    public void initTest() {
        subsidiary = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubsidiary() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryRepository.findAll().size();

        // Create the Subsidiary
        SubsidiaryDTO subsidiaryDTO = subsidiaryMapper.toDto(subsidiary);
        restSubsidiaryMockMvc.perform(post("/api/subsidiaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Subsidiary in the database
        List<Subsidiary> subsidiaryList = subsidiaryRepository.findAll();
        assertThat(subsidiaryList).hasSize(databaseSizeBeforeCreate + 1);
        Subsidiary testSubsidiary = subsidiaryList.get(subsidiaryList.size() - 1);
        assertThat(testSubsidiary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubsidiary.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testSubsidiary.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSubsidiaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryRepository.findAll().size();

        // Create the Subsidiary with an existing ID
        subsidiary.setId(1L);
        SubsidiaryDTO subsidiaryDTO = subsidiaryMapper.toDto(subsidiary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubsidiaryMockMvc.perform(post("/api/subsidiaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subsidiary in the database
        List<Subsidiary> subsidiaryList = subsidiaryRepository.findAll();
        assertThat(subsidiaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subsidiaryRepository.findAll().size();
        // set the field null
        subsidiary.setName(null);

        // Create the Subsidiary, which fails.
        SubsidiaryDTO subsidiaryDTO = subsidiaryMapper.toDto(subsidiary);

        restSubsidiaryMockMvc.perform(post("/api/subsidiaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryDTO)))
            .andExpect(status().isBadRequest());

        List<Subsidiary> subsidiaryList = subsidiaryRepository.findAll();
        assertThat(subsidiaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubsidiaries() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);

        // Get all the subsidiaryList
        restSubsidiaryMockMvc.perform(get("/api/subsidiaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subsidiary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSubsidiary() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);

        // Get the subsidiary
        restSubsidiaryMockMvc.perform(get("/api/subsidiaries/{id}", subsidiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subsidiary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubsidiary() throws Exception {
        // Get the subsidiary
        restSubsidiaryMockMvc.perform(get("/api/subsidiaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubsidiary() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);
        int databaseSizeBeforeUpdate = subsidiaryRepository.findAll().size();

        // Update the subsidiary
        Subsidiary updatedSubsidiary = subsidiaryRepository.findOne(subsidiary.getId());
        // Disconnect from session so that the updates on updatedSubsidiary are not directly saved in db
        em.detach(updatedSubsidiary);
        updatedSubsidiary
            .name(UPDATED_NAME)
            .deleted(UPDATED_DELETED)
            .description(UPDATED_DESCRIPTION);
        SubsidiaryDTO subsidiaryDTO = subsidiaryMapper.toDto(updatedSubsidiary);

        restSubsidiaryMockMvc.perform(put("/api/subsidiaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryDTO)))
            .andExpect(status().isOk());

        // Validate the Subsidiary in the database
        List<Subsidiary> subsidiaryList = subsidiaryRepository.findAll();
        assertThat(subsidiaryList).hasSize(databaseSizeBeforeUpdate);
        Subsidiary testSubsidiary = subsidiaryList.get(subsidiaryList.size() - 1);
        assertThat(testSubsidiary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubsidiary.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testSubsidiary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSubsidiary() throws Exception {
        int databaseSizeBeforeUpdate = subsidiaryRepository.findAll().size();

        // Create the Subsidiary
        SubsidiaryDTO subsidiaryDTO = subsidiaryMapper.toDto(subsidiary);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubsidiaryMockMvc.perform(put("/api/subsidiaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Subsidiary in the database
        List<Subsidiary> subsidiaryList = subsidiaryRepository.findAll();
        assertThat(subsidiaryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubsidiary() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);
        int databaseSizeBeforeDelete = subsidiaryRepository.findAll().size();

        // Get the subsidiary
        restSubsidiaryMockMvc.perform(delete("/api/subsidiaries/{id}", subsidiary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Subsidiary> subsidiaryList = subsidiaryRepository.findAll();
        assertThat(subsidiaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subsidiary.class);
        Subsidiary subsidiary1 = new Subsidiary();
        subsidiary1.setId(1L);
        Subsidiary subsidiary2 = new Subsidiary();
        subsidiary2.setId(subsidiary1.getId());
        assertThat(subsidiary1).isEqualTo(subsidiary2);
        subsidiary2.setId(2L);
        assertThat(subsidiary1).isNotEqualTo(subsidiary2);
        subsidiary1.setId(null);
        assertThat(subsidiary1).isNotEqualTo(subsidiary2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubsidiaryDTO.class);
        SubsidiaryDTO subsidiaryDTO1 = new SubsidiaryDTO();
        subsidiaryDTO1.setId(1L);
        SubsidiaryDTO subsidiaryDTO2 = new SubsidiaryDTO();
        assertThat(subsidiaryDTO1).isNotEqualTo(subsidiaryDTO2);
        subsidiaryDTO2.setId(subsidiaryDTO1.getId());
        assertThat(subsidiaryDTO1).isEqualTo(subsidiaryDTO2);
        subsidiaryDTO2.setId(2L);
        assertThat(subsidiaryDTO1).isNotEqualTo(subsidiaryDTO2);
        subsidiaryDTO1.setId(null);
        assertThat(subsidiaryDTO1).isNotEqualTo(subsidiaryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subsidiaryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subsidiaryMapper.fromId(null)).isNull();
    }
}
