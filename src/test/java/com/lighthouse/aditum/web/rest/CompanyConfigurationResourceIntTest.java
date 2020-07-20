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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.lighthouse.aditum.web.rest.TestUtil.sameInstant;
//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
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

    private static final Integer DEFAULT_HAS_CONTABILITY = 1;
    private static final Integer UPDATED_HAS_CONTABILITY = 2;

    private static final ZonedDateTime DEFAULT_MIN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MIN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_HAS_ACCESS_DOOR = 1;
    private static final Integer UPDATED_HAS_ACCESS_DOOR = 2;

    private static final Boolean DEFAULT_HAS_TRANSIT = false;
    private static final Boolean UPDATED_HAS_TRANSIT = true;

    private static final Boolean DEFAULT_HAS_ROUNDS = false;
    private static final Boolean UPDATED_HAS_ROUNDS = true;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Integer DEFAULT_TENDERS_WATCH_WC = 1;
    private static final Integer UPDATED_TENDERS_WATCH_WC = 2;

    private static final String DEFAULT_BANK_ACCOUNTS = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNTS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_FROM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_FROM_NAME = "BBBBBBBBBB";

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
        final CompanyConfigurationResource companyConfigurationResource = new CompanyConfigurationResource(companyConfigurationService);
        this.restCompanyConfigurationMockMvc = MockMvcBuilders.standaloneSetup(companyConfigurationResource)
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
    public static CompanyConfiguration createEntity(EntityManager em) {
        CompanyConfiguration companyConfiguration = new CompanyConfiguration()
            .quantityhouses(DEFAULT_QUANTITYHOUSES)
            .quantityadmins(DEFAULT_QUANTITYADMINS)
            .quantityaccessdoor(DEFAULT_QUANTITYACCESSDOOR)
            .hasContability(DEFAULT_HAS_CONTABILITY)
            .minDate(DEFAULT_MIN_DATE)
            .hasAccessDoor(DEFAULT_HAS_ACCESS_DOOR)
            .hasTransit(DEFAULT_HAS_TRANSIT)
            .hasRounds(DEFAULT_HAS_ROUNDS)
            .currency(DEFAULT_CURRENCY)
            .tendersWatchWC(DEFAULT_TENDERS_WATCH_WC)
            .bankAccounts(DEFAULT_BANK_ACCOUNTS)
            .emailFromName(DEFAULT_EMAIL_FROM_NAME);
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
        assertThat(testCompanyConfiguration.getHasContability()).isEqualTo(DEFAULT_HAS_CONTABILITY);
        assertThat(testCompanyConfiguration.getMinDate()).isEqualTo(DEFAULT_MIN_DATE);
        assertThat(testCompanyConfiguration.getHasAccessDoor()).isEqualTo(DEFAULT_HAS_ACCESS_DOOR);
        assertThat(testCompanyConfiguration.isHasTransit()).isEqualTo(DEFAULT_HAS_TRANSIT);
        assertThat(testCompanyConfiguration.isHasRounds()).isEqualTo(DEFAULT_HAS_ROUNDS);
        assertThat(testCompanyConfiguration.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testCompanyConfiguration.getTendersWatchWC()).isEqualTo(DEFAULT_TENDERS_WATCH_WC);
        assertThat(testCompanyConfiguration.getBankAccounts()).isEqualTo(DEFAULT_BANK_ACCOUNTS);
        assertThat(testCompanyConfiguration.getEmailFromName()).isEqualTo(DEFAULT_EMAIL_FROM_NAME);
    }

    @Test
    @Transactional
    public void createCompanyConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyConfigurationRepository.findAll().size();

        // Create the CompanyConfiguration with an existing ID
        companyConfiguration.setId(1L);
        CompanyConfigurationDTO companyConfigurationDTO = companyConfigurationMapper.toDto(companyConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyConfigurationMockMvc.perform(post("/api/company-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyConfiguration in the database
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
    public void checkHasContabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyConfigurationRepository.findAll().size();
        // set the field null
        companyConfiguration.setHasContability(null);

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
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyConfigurationRepository.findAll().size();
        // set the field null
        companyConfiguration.setCurrency(null);

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
            .andExpect(jsonPath("$.[*].quantityaccessdoor").value(hasItem(DEFAULT_QUANTITYACCESSDOOR)))
            .andExpect(jsonPath("$.[*].hasContability").value(hasItem(DEFAULT_HAS_CONTABILITY)))
            .andExpect(jsonPath("$.[*].minDate").value(hasItem(sameInstant(DEFAULT_MIN_DATE))))
            .andExpect(jsonPath("$.[*].hasAccessDoor").value(hasItem(DEFAULT_HAS_ACCESS_DOOR)))
            .andExpect(jsonPath("$.[*].hasTransit").value(hasItem(DEFAULT_HAS_TRANSIT.booleanValue())))
            .andExpect(jsonPath("$.[*].hasRounds").value(hasItem(DEFAULT_HAS_ROUNDS.booleanValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].tendersWatchWC").value(hasItem(DEFAULT_TENDERS_WATCH_WC)))
            .andExpect(jsonPath("$.[*].bankAccounts").value(hasItem(DEFAULT_BANK_ACCOUNTS.toString())))
            .andExpect(jsonPath("$.[*].emailFromName").value(hasItem(DEFAULT_EMAIL_FROM_NAME.toString())));
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
            .andExpect(jsonPath("$.quantityaccessdoor").value(DEFAULT_QUANTITYACCESSDOOR))
            .andExpect(jsonPath("$.hasContability").value(DEFAULT_HAS_CONTABILITY))
            .andExpect(jsonPath("$.minDate").value(sameInstant(DEFAULT_MIN_DATE)))
            .andExpect(jsonPath("$.hasAccessDoor").value(DEFAULT_HAS_ACCESS_DOOR))
            .andExpect(jsonPath("$.hasTransit").value(DEFAULT_HAS_TRANSIT.booleanValue()))
            .andExpect(jsonPath("$.hasRounds").value(DEFAULT_HAS_ROUNDS.booleanValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.tendersWatchWC").value(DEFAULT_TENDERS_WATCH_WC))
            .andExpect(jsonPath("$.bankAccounts").value(DEFAULT_BANK_ACCOUNTS.toString()))
            .andExpect(jsonPath("$.emailFromName").value(DEFAULT_EMAIL_FROM_NAME.toString()));
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
        // Disconnect from session so that the updates on updatedCompanyConfiguration are not directly saved in db
        em.detach(updatedCompanyConfiguration);
        updatedCompanyConfiguration
            .quantityhouses(UPDATED_QUANTITYHOUSES)
            .quantityadmins(UPDATED_QUANTITYADMINS)
            .quantityaccessdoor(UPDATED_QUANTITYACCESSDOOR)
            .hasContability(UPDATED_HAS_CONTABILITY)
            .minDate(UPDATED_MIN_DATE)
            .hasAccessDoor(UPDATED_HAS_ACCESS_DOOR)
            .hasTransit(UPDATED_HAS_TRANSIT)
            .hasRounds(UPDATED_HAS_ROUNDS)
            .currency(UPDATED_CURRENCY)
            .tendersWatchWC(UPDATED_TENDERS_WATCH_WC)
            .bankAccounts(UPDATED_BANK_ACCOUNTS)
            .emailFromName(UPDATED_EMAIL_FROM_NAME);
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
        assertThat(testCompanyConfiguration.getHasContability()).isEqualTo(UPDATED_HAS_CONTABILITY);
        assertThat(testCompanyConfiguration.getMinDate()).isEqualTo(UPDATED_MIN_DATE);
        assertThat(testCompanyConfiguration.getHasAccessDoor()).isEqualTo(UPDATED_HAS_ACCESS_DOOR);
        assertThat(testCompanyConfiguration.isHasTransit()).isEqualTo(UPDATED_HAS_TRANSIT);
        assertThat(testCompanyConfiguration.isHasRounds()).isEqualTo(UPDATED_HAS_ROUNDS);
        assertThat(testCompanyConfiguration.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testCompanyConfiguration.getTendersWatchWC()).isEqualTo(UPDATED_TENDERS_WATCH_WC);
        assertThat(testCompanyConfiguration.getBankAccounts()).isEqualTo(UPDATED_BANK_ACCOUNTS);
        assertThat(testCompanyConfiguration.getEmailFromName()).isEqualTo(UPDATED_EMAIL_FROM_NAME);
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
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyConfiguration.class);
        CompanyConfiguration companyConfiguration1 = new CompanyConfiguration();
        companyConfiguration1.setId(1L);
        CompanyConfiguration companyConfiguration2 = new CompanyConfiguration();
        companyConfiguration2.setId(companyConfiguration1.getId());
        assertThat(companyConfiguration1).isEqualTo(companyConfiguration2);
        companyConfiguration2.setId(2L);
        assertThat(companyConfiguration1).isNotEqualTo(companyConfiguration2);
        companyConfiguration1.setId(null);
        assertThat(companyConfiguration1).isNotEqualTo(companyConfiguration2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyConfigurationDTO.class);
        CompanyConfigurationDTO companyConfigurationDTO1 = new CompanyConfigurationDTO();
        companyConfigurationDTO1.setId(1L);
        CompanyConfigurationDTO companyConfigurationDTO2 = new CompanyConfigurationDTO();
        assertThat(companyConfigurationDTO1).isNotEqualTo(companyConfigurationDTO2);
        companyConfigurationDTO2.setId(companyConfigurationDTO1.getId());
        assertThat(companyConfigurationDTO1).isEqualTo(companyConfigurationDTO2);
        companyConfigurationDTO2.setId(2L);
        assertThat(companyConfigurationDTO1).isNotEqualTo(companyConfigurationDTO2);
        companyConfigurationDTO1.setId(null);
        assertThat(companyConfigurationDTO1).isNotEqualTo(companyConfigurationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(companyConfigurationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(companyConfigurationMapper.fromId(null)).isNull();
    }
}
