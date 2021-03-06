package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.CompanyConfiguration;
import com.lighthouse.aditum.repository.CompanyConfigurationRepository;
import com.lighthouse.aditum.service.CompanyConfigurationService;
import com.lighthouse.aditum.service.dto.CompanyConfigurationDTO;
import com.lighthouse.aditum.service.mapper.CompanyConfigurationMapper;
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
 * Test class for the CompanyConfigurationResource REST controller.
 *
 * @see CompanyConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CompanyConfigurationResourceIntTest {

    private static final Integer DEFAULT_QUANTITYHOUSES = 1;
    private static final Integer UPDATED_QUANTITYHOUSES = 2;

    private static final Integer DEFAULT_QUANTITYADMINS = 1;
    private static final Integer UPDATED_QUANTITYADMINS = 2;

    private static final Integer DEFAULT_QUANTITYACCESSDOOR = 1;
    private static final Integer UPDATED_QUANTITYACCESSDOOR = 2;

    @Autowired
    private CompanyConfigurationRepository companyConfigurationRepository;

    @Autowired
    private CompanyConfigurationMapper companyConfigurationMapper;

    @Autowired
    private CompanyConfigurationService companyConfigurationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyConfigurationMockMvc;

    private CompanyConfiguration companyConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyConfigurationResource companyConfigurationResource = new CompanyConfigurationResource(companyConfigurationService);
        this.restCompanyConfigurationMockMvc = MockMvcBuilders.standaloneSetup(companyConfigurationResource)
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
    public static CompanyConfiguration createEntity(EntityManager em) {
        CompanyConfiguration companyConfiguration = new CompanyConfiguration()
                .quantityhouses(DEFAULT_QUANTITYHOUSES)
                .quantityadmins(DEFAULT_QUANTITYADMINS)
                .quantityaccessdoor(DEFAULT_QUANTITYACCESSDOOR);
        return companyConfiguration;
    }

    @Before
    public void initTest() {
        companyConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyConfiguration() throws Exception {
        int databaseSizeBeforeCreate = companyConfigurationRepository.findAll().size();

        // Create the CompanyConfiguration
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(companyConfiguration);

        restCompanyConfigurationMockMvc.perform(post("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the CompanyConfiguration in the database
        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyConfiguration testCompanyConfiguration = companyConfigurationList.get(companyConfigurationList.size() - 1);
        assertThat(testCompanyConfiguration.getQuantityhouses()).isEqualTo(DEFAULT_QUANTITYHOUSES);
        assertThat(testCompanyConfiguration.getQuantityadmins()).isEqualTo(DEFAULT_QUANTITYADMINS);
        assertThat(testCompanyConfiguration.getQuantityaccessdoor()).isEqualTo(DEFAULT_QUANTITYACCESSDOOR);
    }

    @Test
    @Transactional
    public void createCompanyConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyConfigurationRepository.findAll().size();

        // Create the CompanyConfiguration with an existing ID
        CompanyConfiguration existingCompanyConfiguration = new CompanyConfiguration();
        existingCompanyConfiguration.setId(1L);
        CompanyConfigurationDTO existingCompanyConfigurationDTO = companyConfigurationMapper.toDto(existingCompanyConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyConfigurationMockMvc.perform(post("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCompanyConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantityhousesIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyConfigurationRepository.findAll().size();
        // set the field null
        companyConfiguration.setQuantityhouses(null);

        // Create the CompanyConfiguration, which fails.
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(companyConfiguration);

        restCompanyConfigurationMockMvc.perform(post("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isBadRequest());

        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityadminsIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyConfigurationRepository.findAll().size();
        // set the field null
        companyConfiguration.setQuantityadmins(null);

        // Create the CompanyConfiguration, which fails.
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(companyConfiguration);

        restCompanyConfigurationMockMvc.perform(post("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isBadRequest());

        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityaccessdoorIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyConfigurationRepository.findAll().size();
        // set the field null
        companyConfiguration.setQuantityaccessdoor(null);

        // Create the CompanyConfiguration, which fails.
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(companyConfiguration);

        restCompanyConfigurationMockMvc.perform(post("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isBadRequest());

        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyConfigurations() throws Exception {
        // Initialize the database
        companyConfigurationRepository.saveAndFlush(companyConfiguration);

        // Get all the companyConfigurationList
        restCompanyConfigurationMockMvc.perform(get("/api/company-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantityhouses").value(hasItem(DEFAULT_QUANTITYHOUSES)))
            .andExpect(jsonPath("$.[*].quantityadmins").value(hasItem(DEFAULT_QUANTITYADMINS)))
            .andExpect(jsonPath("$.[*].quantityaccessdoor").value(hasItem(DEFAULT_QUANTITYACCESSDOOR)));
    }

    @Test
    @Transactional
    public void getCompanyConfiguration() throws Exception {
        // Initialize the database
        companyConfigurationRepository.saveAndFlush(companyConfiguration);

        // Get the companyConfiguration
        restCompanyConfigurationMockMvc.perform(get("/api/company-configurations/{id}", companyConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.quantityhouses").value(DEFAULT_QUANTITYHOUSES))
            .andExpect(jsonPath("$.quantityadmins").value(DEFAULT_QUANTITYADMINS))
            .andExpect(jsonPath("$.quantityaccessdoor").value(DEFAULT_QUANTITYACCESSDOOR));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyConfiguration() throws Exception {
        // Get the companyConfiguration
        restCompanyConfigurationMockMvc.perform(get("/api/company-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyConfiguration() throws Exception {
        // Initialize the database
        companyConfigurationRepository.saveAndFlush(companyConfiguration);
        int databaseSizeBeforeUpdate = companyConfigurationRepository.findAll().size();

        // Update the companyConfiguration
        CompanyConfiguration updatedCompanyConfiguration = companyConfigurationRepository.findOne(companyConfiguration.getId());
        updatedCompanyConfiguration
                .quantityhouses(UPDATED_QUANTITYHOUSES)
                .quantityadmins(UPDATED_QUANTITYADMINS)
                .quantityaccessdoor(UPDATED_QUANTITYACCESSDOOR);
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(updatedCompanyConfiguration);

        restCompanyConfigurationMockMvc.perform(put("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isOk());

        // Validate the CompanyConfiguration in the database
        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeUpdate);
        CompanyConfiguration testCompanyConfiguration = companyConfigurationList.get(companyConfigurationList.size() - 1);
        assertThat(testCompanyConfiguration.getQuantityhouses()).isEqualTo(UPDATED_QUANTITYHOUSES);
        assertThat(testCompanyConfiguration.getQuantityadmins()).isEqualTo(UPDATED_QUANTITYADMINS);
        assertThat(testCompanyConfiguration.getQuantityaccessdoor()).isEqualTo(UPDATED_QUANTITYACCESSDOOR);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = companyConfigurationRepository.findAll().size();

        // Create the CompanyConfiguration
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(companyConfiguration);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyConfigurationMockMvc.perform(put("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the CompanyConfiguration in the database
        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompanyConfiguration() throws Exception {
        // Initialize the database
        companyConfigurationRepository.saveAndFlush(companyConfiguration);
        int databaseSizeBeforeDelete = companyConfigurationRepository.findAll().size();

        // Get the companyConfiguration
        restCompanyConfigurationMockMvc.perform(delete("/api/company-configurations/{id}", companyConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyConfiguration> companyConfigurationList = companyConfigurationRepository.findAll();
        assertThat(companyConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyConfiguration.class);
    }
}
