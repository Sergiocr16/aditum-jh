package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.SubsidiaryType;
import com.lighthouse.aditum.repository.SubsidiaryTypeRepository;
import com.lighthouse.aditum.service.SubsidiaryTypeService;
import com.lighthouse.aditum.service.dto.SubsidiaryTypeDTO;
import com.lighthouse.aditum.service.mapper.SubsidiaryTypeMapper;
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
 * Test class for the SubsidiaryTypeResource REST controller.
 *
 * @see SubsidiaryTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class SubsidiaryTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SIZE = "BBBBBBBBBB";

    private static final Double DEFAULT_JOINT_OWNERSHIP_PERCENTAGE = 1D;
    private static final Double UPDATED_JOINT_OWNERSHIP_PERCENTAGE = 2D;

    private static final String DEFAULT_AMMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIMIT = 1;
    private static final Integer UPDATED_LIMIT = 2;

    private static final Integer DEFAULT_SUBSIDIARY_TYPE = 1;
    private static final Integer UPDATED_SUBSIDIARY_TYPE = 2;

    @Autowired
    private SubsidiaryTypeRepository subsidiaryTypeRepository;

    @Autowired
    private SubsidiaryTypeMapper subsidiaryTypeMapper;

    @Autowired
    private SubsidiaryTypeService subsidiaryTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubsidiaryTypeMockMvc;

    private SubsidiaryType subsidiaryType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubsidiaryTypeResource subsidiaryTypeResource = new SubsidiaryTypeResource(subsidiaryTypeService);
        this.restSubsidiaryTypeMockMvc = MockMvcBuilders.standaloneSetup(subsidiaryTypeResource)
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
    public static SubsidiaryType createEntity(EntityManager em) {
        SubsidiaryType subsidiaryType = new SubsidiaryType()
            .description(DEFAULT_DESCRIPTION)
            .size(DEFAULT_SIZE)
            .jointOwnershipPercentage(DEFAULT_JOINT_OWNERSHIP_PERCENTAGE)
            .ammount(DEFAULT_AMMOUNT)
            .limit(DEFAULT_LIMIT)
            .subsidiaryType(DEFAULT_SUBSIDIARY_TYPE);
        return subsidiaryType;
    }

    @Before
    public void initTest() {
        subsidiaryType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubsidiaryType() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryTypeRepository.findAll().size();

        // Create the SubsidiaryType
        SubsidiaryTypeDTO subsidiaryTypeDTO = subsidiaryTypeMapper.toDto(subsidiaryType);
        restSubsidiaryTypeMockMvc.perform(post("/api/subsidiary-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SubsidiaryType in the database
        List<SubsidiaryType> subsidiaryTypeList = subsidiaryTypeRepository.findAll();
        assertThat(subsidiaryTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SubsidiaryType testSubsidiaryType = subsidiaryTypeList.get(subsidiaryTypeList.size() - 1);
        assertThat(testSubsidiaryType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubsidiaryType.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testSubsidiaryType.getJointOwnershipPercentage()).isEqualTo(DEFAULT_JOINT_OWNERSHIP_PERCENTAGE);
        assertThat(testSubsidiaryType.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testSubsidiaryType.getLimit()).isEqualTo(DEFAULT_LIMIT);
        assertThat(testSubsidiaryType.getSubsidiaryType()).isEqualTo(DEFAULT_SUBSIDIARY_TYPE);
    }

    @Test
    @Transactional
    public void createSubsidiaryTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryTypeRepository.findAll().size();

        // Create the SubsidiaryType with an existing ID
        subsidiaryType.setId(1L);
        SubsidiaryTypeDTO subsidiaryTypeDTO = subsidiaryTypeMapper.toDto(subsidiaryType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubsidiaryTypeMockMvc.perform(post("/api/subsidiary-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubsidiaryType in the database
        List<SubsidiaryType> subsidiaryTypeList = subsidiaryTypeRepository.findAll();
        assertThat(subsidiaryTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = subsidiaryTypeRepository.findAll().size();
        // set the field null
        subsidiaryType.setDescription(null);

        // Create the SubsidiaryType, which fails.
        SubsidiaryTypeDTO subsidiaryTypeDTO = subsidiaryTypeMapper.toDto(subsidiaryType);

        restSubsidiaryTypeMockMvc.perform(post("/api/subsidiary-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SubsidiaryType> subsidiaryTypeList = subsidiaryTypeRepository.findAll();
        assertThat(subsidiaryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubsidiaryTypes() throws Exception {
        // Initialize the database
        subsidiaryTypeRepository.saveAndFlush(subsidiaryType);

        // Get all the subsidiaryTypeList
        restSubsidiaryTypeMockMvc.perform(get("/api/subsidiary-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subsidiaryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].jointOwnershipPercentage").value(hasItem(DEFAULT_JOINT_OWNERSHIP_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.toString())))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT)))
            .andExpect(jsonPath("$.[*].subsidiaryType").value(hasItem(DEFAULT_SUBSIDIARY_TYPE)));
    }

    @Test
    @Transactional
    public void getSubsidiaryType() throws Exception {
        // Initialize the database
        subsidiaryTypeRepository.saveAndFlush(subsidiaryType);

        // Get the subsidiaryType
        restSubsidiaryTypeMockMvc.perform(get("/api/subsidiary-types/{id}", subsidiaryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subsidiaryType.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()))
            .andExpect(jsonPath("$.jointOwnershipPercentage").value(DEFAULT_JOINT_OWNERSHIP_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.toString()))
            .andExpect(jsonPath("$.limit").value(DEFAULT_LIMIT))
            .andExpect(jsonPath("$.subsidiaryType").value(DEFAULT_SUBSIDIARY_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingSubsidiaryType() throws Exception {
        // Get the subsidiaryType
        restSubsidiaryTypeMockMvc.perform(get("/api/subsidiary-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubsidiaryType() throws Exception {
        // Initialize the database
        subsidiaryTypeRepository.saveAndFlush(subsidiaryType);
        int databaseSizeBeforeUpdate = subsidiaryTypeRepository.findAll().size();

        // Update the subsidiaryType
        SubsidiaryType updatedSubsidiaryType = subsidiaryTypeRepository.findOne(subsidiaryType.getId());
        // Disconnect from session so that the updates on updatedSubsidiaryType are not directly saved in db
        em.detach(updatedSubsidiaryType);
        updatedSubsidiaryType
            .description(UPDATED_DESCRIPTION)
            .size(UPDATED_SIZE)
            .jointOwnershipPercentage(UPDATED_JOINT_OWNERSHIP_PERCENTAGE)
            .ammount(UPDATED_AMMOUNT)
            .limit(UPDATED_LIMIT)
            .subsidiaryType(UPDATED_SUBSIDIARY_TYPE);
        SubsidiaryTypeDTO subsidiaryTypeDTO = subsidiaryTypeMapper.toDto(updatedSubsidiaryType);

        restSubsidiaryTypeMockMvc.perform(put("/api/subsidiary-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryTypeDTO)))
            .andExpect(status().isOk());

        // Validate the SubsidiaryType in the database
        List<SubsidiaryType> subsidiaryTypeList = subsidiaryTypeRepository.findAll();
        assertThat(subsidiaryTypeList).hasSize(databaseSizeBeforeUpdate);
        SubsidiaryType testSubsidiaryType = subsidiaryTypeList.get(subsidiaryTypeList.size() - 1);
        assertThat(testSubsidiaryType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubsidiaryType.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testSubsidiaryType.getJointOwnershipPercentage()).isEqualTo(UPDATED_JOINT_OWNERSHIP_PERCENTAGE);
        assertThat(testSubsidiaryType.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testSubsidiaryType.getLimit()).isEqualTo(UPDATED_LIMIT);
        assertThat(testSubsidiaryType.getSubsidiaryType()).isEqualTo(UPDATED_SUBSIDIARY_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubsidiaryType() throws Exception {
        int databaseSizeBeforeUpdate = subsidiaryTypeRepository.findAll().size();

        // Create the SubsidiaryType
        SubsidiaryTypeDTO subsidiaryTypeDTO = subsidiaryTypeMapper.toDto(subsidiaryType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubsidiaryTypeMockMvc.perform(put("/api/subsidiary-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsidiaryTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SubsidiaryType in the database
        List<SubsidiaryType> subsidiaryTypeList = subsidiaryTypeRepository.findAll();
        assertThat(subsidiaryTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubsidiaryType() throws Exception {
        // Initialize the database
        subsidiaryTypeRepository.saveAndFlush(subsidiaryType);
        int databaseSizeBeforeDelete = subsidiaryTypeRepository.findAll().size();

        // Get the subsidiaryType
        restSubsidiaryTypeMockMvc.perform(delete("/api/subsidiary-types/{id}", subsidiaryType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubsidiaryType> subsidiaryTypeList = subsidiaryTypeRepository.findAll();
        assertThat(subsidiaryTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubsidiaryType.class);
        SubsidiaryType subsidiaryType1 = new SubsidiaryType();
        subsidiaryType1.setId(1L);
        SubsidiaryType subsidiaryType2 = new SubsidiaryType();
        subsidiaryType2.setId(subsidiaryType1.getId());
        assertThat(subsidiaryType1).isEqualTo(subsidiaryType2);
        subsidiaryType2.setId(2L);
        assertThat(subsidiaryType1).isNotEqualTo(subsidiaryType2);
        subsidiaryType1.setId(null);
        assertThat(subsidiaryType1).isNotEqualTo(subsidiaryType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubsidiaryTypeDTO.class);
        SubsidiaryTypeDTO subsidiaryTypeDTO1 = new SubsidiaryTypeDTO();
        subsidiaryTypeDTO1.setId(1L);
        SubsidiaryTypeDTO subsidiaryTypeDTO2 = new SubsidiaryTypeDTO();
        assertThat(subsidiaryTypeDTO1).isNotEqualTo(subsidiaryTypeDTO2);
        subsidiaryTypeDTO2.setId(subsidiaryTypeDTO1.getId());
        assertThat(subsidiaryTypeDTO1).isEqualTo(subsidiaryTypeDTO2);
        subsidiaryTypeDTO2.setId(2L);
        assertThat(subsidiaryTypeDTO1).isNotEqualTo(subsidiaryTypeDTO2);
        subsidiaryTypeDTO1.setId(null);
        assertThat(subsidiaryTypeDTO1).isNotEqualTo(subsidiaryTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subsidiaryTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subsidiaryTypeMapper.fromId(null)).isNull();
    }
}
