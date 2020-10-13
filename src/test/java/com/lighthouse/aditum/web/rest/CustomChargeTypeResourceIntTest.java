package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.CustomChargeType;
import com.lighthouse.aditum.repository.CustomChargeTypeRepository;
import com.lighthouse.aditum.service.CustomChargeTypeService;
import com.lighthouse.aditum.service.dto.CustomChargeTypeDTO;
import com.lighthouse.aditum.service.mapper.CustomChargeTypeMapper;
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
 * Test class for the CustomChargeTypeResource REST controller.
 *
 * @see CustomChargeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CustomChargeTypeResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private CustomChargeTypeRepository customChargeTypeRepository;

    @Autowired
    private CustomChargeTypeMapper customChargeTypeMapper;

    @Autowired
    private CustomChargeTypeService customChargeTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomChargeTypeMockMvc;

    private CustomChargeType customChargeType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomChargeTypeResource customChargeTypeResource = new CustomChargeTypeResource(customChargeTypeService);
        this.restCustomChargeTypeMockMvc = MockMvcBuilders.standaloneSetup(customChargeTypeResource)
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
    public static CustomChargeType createEntity(EntityManager em) {
        CustomChargeType customChargeType = new CustomChargeType()
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return customChargeType;
    }

    @Before
    public void initTest() {
        customChargeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomChargeType() throws Exception {
        int databaseSizeBeforeCreate = customChargeTypeRepository.findAll().size();

        // Create the CustomChargeType
        CustomChargeTypeDTO customChargeTypeDTO = customChargeTypeMapper.toDto(customChargeType);
        restCustomChargeTypeMockMvc.perform(post("/api/custom-charge-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customChargeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomChargeType in the database
        List<CustomChargeType> customChargeTypeList = customChargeTypeRepository.findAll();
        assertThat(customChargeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CustomChargeType testCustomChargeType = customChargeTypeList.get(customChargeTypeList.size() - 1);
        assertThat(testCustomChargeType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomChargeType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCustomChargeType.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCustomChargeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customChargeTypeRepository.findAll().size();

        // Create the CustomChargeType with an existing ID
        customChargeType.setId(1L);
        CustomChargeTypeDTO customChargeTypeDTO = customChargeTypeMapper.toDto(customChargeType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomChargeTypeMockMvc.perform(post("/api/custom-charge-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customChargeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomChargeType in the database
        List<CustomChargeType> customChargeTypeList = customChargeTypeRepository.findAll();
        assertThat(customChargeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomChargeTypes() throws Exception {
        // Initialize the database
        customChargeTypeRepository.saveAndFlush(customChargeType);

        // Get all the customChargeTypeList
        restCustomChargeTypeMockMvc.perform(get("/api/custom-charge-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customChargeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getCustomChargeType() throws Exception {
        // Initialize the database
        customChargeTypeRepository.saveAndFlush(customChargeType);

        // Get the customChargeType
        restCustomChargeTypeMockMvc.perform(get("/api/custom-charge-types/{id}", customChargeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customChargeType.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCustomChargeType() throws Exception {
        // Get the customChargeType
        restCustomChargeTypeMockMvc.perform(get("/api/custom-charge-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomChargeType() throws Exception {
        // Initialize the database
        customChargeTypeRepository.saveAndFlush(customChargeType);
        int databaseSizeBeforeUpdate = customChargeTypeRepository.findAll().size();

        // Update the customChargeType
        CustomChargeType updatedCustomChargeType = customChargeTypeRepository.findOne(customChargeType.getId());
        // Disconnect from session so that the updates on updatedCustomChargeType are not directly saved in db
        em.detach(updatedCustomChargeType);
        updatedCustomChargeType
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        CustomChargeTypeDTO customChargeTypeDTO = customChargeTypeMapper.toDto(updatedCustomChargeType);

        restCustomChargeTypeMockMvc.perform(put("/api/custom-charge-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customChargeTypeDTO)))
            .andExpect(status().isOk());

        // Validate the CustomChargeType in the database
        List<CustomChargeType> customChargeTypeList = customChargeTypeRepository.findAll();
        assertThat(customChargeTypeList).hasSize(databaseSizeBeforeUpdate);
        CustomChargeType testCustomChargeType = customChargeTypeList.get(customChargeTypeList.size() - 1);
        assertThat(testCustomChargeType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomChargeType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCustomChargeType.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomChargeType() throws Exception {
        int databaseSizeBeforeUpdate = customChargeTypeRepository.findAll().size();

        // Create the CustomChargeType
        CustomChargeTypeDTO customChargeTypeDTO = customChargeTypeMapper.toDto(customChargeType);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomChargeTypeMockMvc.perform(put("/api/custom-charge-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customChargeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomChargeType in the database
        List<CustomChargeType> customChargeTypeList = customChargeTypeRepository.findAll();
        assertThat(customChargeTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomChargeType() throws Exception {
        // Initialize the database
        customChargeTypeRepository.saveAndFlush(customChargeType);
        int databaseSizeBeforeDelete = customChargeTypeRepository.findAll().size();

        // Get the customChargeType
        restCustomChargeTypeMockMvc.perform(delete("/api/custom-charge-types/{id}", customChargeType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomChargeType> customChargeTypeList = customChargeTypeRepository.findAll();
        assertThat(customChargeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomChargeType.class);
        CustomChargeType customChargeType1 = new CustomChargeType();
        customChargeType1.setId(1L);
        CustomChargeType customChargeType2 = new CustomChargeType();
        customChargeType2.setId(customChargeType1.getId());
        assertThat(customChargeType1).isEqualTo(customChargeType2);
        customChargeType2.setId(2L);
        assertThat(customChargeType1).isNotEqualTo(customChargeType2);
        customChargeType1.setId(null);
        assertThat(customChargeType1).isNotEqualTo(customChargeType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomChargeTypeDTO.class);
        CustomChargeTypeDTO customChargeTypeDTO1 = new CustomChargeTypeDTO();
        customChargeTypeDTO1.setId(1L);
        CustomChargeTypeDTO customChargeTypeDTO2 = new CustomChargeTypeDTO();
        assertThat(customChargeTypeDTO1).isNotEqualTo(customChargeTypeDTO2);
        customChargeTypeDTO2.setId(customChargeTypeDTO1.getId());
        assertThat(customChargeTypeDTO1).isEqualTo(customChargeTypeDTO2);
        customChargeTypeDTO2.setId(2L);
        assertThat(customChargeTypeDTO1).isNotEqualTo(customChargeTypeDTO2);
        customChargeTypeDTO1.setId(null);
        assertThat(customChargeTypeDTO1).isNotEqualTo(customChargeTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customChargeTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customChargeTypeMapper.fromId(null)).isNull();
    }
}
