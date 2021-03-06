package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.AdministrationConfiguration;
import com.lighthouse.aditum.repository.AdministrationConfigurationRepository;
import com.lighthouse.aditum.service.AdministrationConfigurationService;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
import com.lighthouse.aditum.service.mapper.AdministrationConfigurationMapper;
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
 * Test class for the AdministrationConfigurationResource REST controller.
 *
 * @see AdministrationConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class AdministrationConfigurationResourceIntTest {

    private static final String DEFAULT_SQUARE_METERS_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_SQUARE_METERS_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_FOLIO_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_FOLIO_SERIE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FOLIO_NUMBER = 1;
    private static final Integer UPDATED_FOLIO_NUMBER = 2;

    private static final Integer DEFAULT_DAYS_TOBE_DEFAULTER = 1;
    private static final Integer UPDATED_DAYS_TOBE_DEFAULTER = 2;

    private static final Boolean DEFAULT_HAS_SUBCHARGES = false;
    private static final Boolean UPDATED_HAS_SUBCHARGES = true;

    private static final Double DEFAULT_SUBCHARGE_PERCENTAGE = 1D;
    private static final Double UPDATED_SUBCHARGE_PERCENTAGE = 2D;

    private static final Double DEFAULT_SUBCHARGE_AMMOUNT = 1D;
    private static final Double UPDATED_SUBCHARGE_AMMOUNT = 2D;

    private static final Integer DEFAULT_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER = 1;
    private static final Integer UPDATED_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER = 2;

    @Autowired
    private AdministrationConfigurationRepository administrationConfigurationRepository;

    @Autowired
    private AdministrationConfigurationMapper administrationConfigurationMapper;

    @Autowired
    private AdministrationConfigurationService administrationConfigurationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdministrationConfigurationMockMvc;

    private AdministrationConfiguration administrationConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdministrationConfigurationResource administrationConfigurationResource = new AdministrationConfigurationResource(administrationConfigurationService);
        this.restAdministrationConfigurationMockMvc = MockMvcBuilders.standaloneSetup(administrationConfigurationResource)
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
    public static AdministrationConfiguration createEntity(EntityManager em) {
        AdministrationConfiguration administrationConfiguration = new AdministrationConfiguration()
            .squareMetersPrice(DEFAULT_SQUARE_METERS_PRICE)
            .folioSerie(DEFAULT_FOLIO_SERIE)
            .folioNumber(DEFAULT_FOLIO_NUMBER)
            .daysTobeDefaulter(DEFAULT_DAYS_TOBE_DEFAULTER)
            .hasSubcharges(DEFAULT_HAS_SUBCHARGES)
            .subchargePercentage(DEFAULT_SUBCHARGE_PERCENTAGE)
            .subchargeAmmount(DEFAULT_SUBCHARGE_AMMOUNT)
            .daysToSendEmailBeforeBeDefaulter(DEFAULT_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER);
        return administrationConfiguration;
    }

    @Before
    public void initTest() {
        administrationConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdministrationConfiguration() throws Exception {
        int databaseSizeBeforeCreate = administrationConfigurationRepository.findAll().size();

        // Create the AdministrationConfiguration
        AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationMapper.toDto(administrationConfiguration);
        restAdministrationConfigurationMockMvc.perform(post("/api/administration-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(administrationConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the AdministrationConfiguration in the database
        List<AdministrationConfiguration> administrationConfigurationList = administrationConfigurationRepository.findAll();
        assertThat(administrationConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        AdministrationConfiguration testAdministrationConfiguration = administrationConfigurationList.get(administrationConfigurationList.size() - 1);
        assertThat(testAdministrationConfiguration.getSquareMetersPrice()).isEqualTo(DEFAULT_SQUARE_METERS_PRICE);
        assertThat(testAdministrationConfiguration.getFolioSerie()).isEqualTo(DEFAULT_FOLIO_SERIE);
        assertThat(testAdministrationConfiguration.getFolioNumber()).isEqualTo(DEFAULT_FOLIO_NUMBER);
        assertThat(testAdministrationConfiguration.getDaysTobeDefaulter()).isEqualTo(DEFAULT_DAYS_TOBE_DEFAULTER);
        assertThat(testAdministrationConfiguration.isHasSubcharges()).isEqualTo(DEFAULT_HAS_SUBCHARGES);
        assertThat(testAdministrationConfiguration.getSubchargePercentage()).isEqualTo(DEFAULT_SUBCHARGE_PERCENTAGE);
        assertThat(testAdministrationConfiguration.getSubchargeAmmount()).isEqualTo(DEFAULT_SUBCHARGE_AMMOUNT);
        assertThat(testAdministrationConfiguration.getDaysToSendEmailBeforeBeDefaulter()).isEqualTo(DEFAULT_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER);
    }

    @Test
    @Transactional
    public void createAdministrationConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = administrationConfigurationRepository.findAll().size();

        // Create the AdministrationConfiguration with an existing ID
        administrationConfiguration.setId(1L);
        AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationMapper.toDto(administrationConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrationConfigurationMockMvc.perform(post("/api/administration-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(administrationConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdministrationConfiguration in the database
        List<AdministrationConfiguration> administrationConfigurationList = administrationConfigurationRepository.findAll();
        assertThat(administrationConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHasSubchargesIsRequired() throws Exception {
        int databaseSizeBeforeTest = administrationConfigurationRepository.findAll().size();
        // set the field null
        administrationConfiguration.setHasSubcharges(null);

        // Create the AdministrationConfiguration, which fails.
        AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationMapper.toDto(administrationConfiguration);

        restAdministrationConfigurationMockMvc.perform(post("/api/administration-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(administrationConfigurationDTO)))
            .andExpect(status().isBadRequest());

        List<AdministrationConfiguration> administrationConfigurationList = administrationConfigurationRepository.findAll();
        assertThat(administrationConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdministrationConfigurations() throws Exception {
        // Initialize the database
        administrationConfigurationRepository.saveAndFlush(administrationConfiguration);

        // Get all the administrationConfigurationList
        restAdministrationConfigurationMockMvc.perform(get("/api/administration-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrationConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].squareMetersPrice").value(hasItem(DEFAULT_SQUARE_METERS_PRICE.toString())))
            .andExpect(jsonPath("$.[*].folioSerie").value(hasItem(DEFAULT_FOLIO_SERIE.toString())))
            .andExpect(jsonPath("$.[*].folioNumber").value(hasItem(DEFAULT_FOLIO_NUMBER)))
            .andExpect(jsonPath("$.[*].daysTobeDefaulter").value(hasItem(DEFAULT_DAYS_TOBE_DEFAULTER)))
            .andExpect(jsonPath("$.[*].hasSubcharges").value(hasItem(DEFAULT_HAS_SUBCHARGES.booleanValue())))
            .andExpect(jsonPath("$.[*].subchargePercentage").value(hasItem(DEFAULT_SUBCHARGE_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].subchargeAmmount").value(hasItem(DEFAULT_SUBCHARGE_AMMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].daysToSendEmailBeforeBeDefaulter").value(hasItem(DEFAULT_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER)));
    }

    @Test
    @Transactional
    public void getAdministrationConfiguration() throws Exception {
        // Initialize the database
        administrationConfigurationRepository.saveAndFlush(administrationConfiguration);

        // Get the administrationConfiguration
        restAdministrationConfigurationMockMvc.perform(get("/api/administration-configurations/{id}", administrationConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(administrationConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.squareMetersPrice").value(DEFAULT_SQUARE_METERS_PRICE.toString()))
            .andExpect(jsonPath("$.folioSerie").value(DEFAULT_FOLIO_SERIE.toString()))
            .andExpect(jsonPath("$.folioNumber").value(DEFAULT_FOLIO_NUMBER))
            .andExpect(jsonPath("$.daysTobeDefaulter").value(DEFAULT_DAYS_TOBE_DEFAULTER))
            .andExpect(jsonPath("$.hasSubcharges").value(DEFAULT_HAS_SUBCHARGES.booleanValue()))
            .andExpect(jsonPath("$.subchargePercentage").value(DEFAULT_SUBCHARGE_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.subchargeAmmount").value(DEFAULT_SUBCHARGE_AMMOUNT.doubleValue()))
            .andExpect(jsonPath("$.daysToSendEmailBeforeBeDefaulter").value(DEFAULT_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER));
    }

    @Test
    @Transactional
    public void getNonExistingAdministrationConfiguration() throws Exception {
        // Get the administrationConfiguration
        restAdministrationConfigurationMockMvc.perform(get("/api/administration-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdministrationConfiguration() throws Exception {
        // Initialize the database
        administrationConfigurationRepository.saveAndFlush(administrationConfiguration);
        int databaseSizeBeforeUpdate = administrationConfigurationRepository.findAll().size();

        // Update the administrationConfiguration
        AdministrationConfiguration updatedAdministrationConfiguration = administrationConfigurationRepository.findOne(administrationConfiguration.getId());
        // Disconnect from session so that the updates on updatedAdministrationConfiguration are not directly saved in db
        em.detach(updatedAdministrationConfiguration);
        updatedAdministrationConfiguration
            .squareMetersPrice(UPDATED_SQUARE_METERS_PRICE)
            .folioSerie(UPDATED_FOLIO_SERIE)
            .folioNumber(UPDATED_FOLIO_NUMBER)
            .daysTobeDefaulter(UPDATED_DAYS_TOBE_DEFAULTER)
            .hasSubcharges(UPDATED_HAS_SUBCHARGES)
            .subchargePercentage(UPDATED_SUBCHARGE_PERCENTAGE)
            .subchargeAmmount(UPDATED_SUBCHARGE_AMMOUNT)
            .daysToSendEmailBeforeBeDefaulter(UPDATED_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER);
        AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationMapper.toDto(updatedAdministrationConfiguration);

        restAdministrationConfigurationMockMvc.perform(put("/api/administration-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(administrationConfigurationDTO)))
            .andExpect(status().isOk());

        // Validate the AdministrationConfiguration in the database
        List<AdministrationConfiguration> administrationConfigurationList = administrationConfigurationRepository.findAll();
        assertThat(administrationConfigurationList).hasSize(databaseSizeBeforeUpdate);
        AdministrationConfiguration testAdministrationConfiguration = administrationConfigurationList.get(administrationConfigurationList.size() - 1);
        assertThat(testAdministrationConfiguration.getSquareMetersPrice()).isEqualTo(UPDATED_SQUARE_METERS_PRICE);
        assertThat(testAdministrationConfiguration.getFolioSerie()).isEqualTo(UPDATED_FOLIO_SERIE);
        assertThat(testAdministrationConfiguration.getFolioNumber()).isEqualTo(UPDATED_FOLIO_NUMBER);
        assertThat(testAdministrationConfiguration.getDaysTobeDefaulter()).isEqualTo(UPDATED_DAYS_TOBE_DEFAULTER);
        assertThat(testAdministrationConfiguration.isHasSubcharges()).isEqualTo(UPDATED_HAS_SUBCHARGES);
        assertThat(testAdministrationConfiguration.getSubchargePercentage()).isEqualTo(UPDATED_SUBCHARGE_PERCENTAGE);
        assertThat(testAdministrationConfiguration.getSubchargeAmmount()).isEqualTo(UPDATED_SUBCHARGE_AMMOUNT);
        assertThat(testAdministrationConfiguration.getDaysToSendEmailBeforeBeDefaulter()).isEqualTo(UPDATED_DAYS_TO_SEND_EMAIL_BEFORE_BE_DEFAULTER);
    }

    @Test
    @Transactional
    public void updateNonExistingAdministrationConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = administrationConfigurationRepository.findAll().size();

        // Create the AdministrationConfiguration
        AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationMapper.toDto(administrationConfiguration);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdministrationConfigurationMockMvc.perform(put("/api/administration-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(administrationConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the AdministrationConfiguration in the database
        List<AdministrationConfiguration> administrationConfigurationList = administrationConfigurationRepository.findAll();
        assertThat(administrationConfigurationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdministrationConfiguration() throws Exception {
        // Initialize the database
        administrationConfigurationRepository.saveAndFlush(administrationConfiguration);
        int databaseSizeBeforeDelete = administrationConfigurationRepository.findAll().size();

        // Get the administrationConfiguration
        restAdministrationConfigurationMockMvc.perform(delete("/api/administration-configurations/{id}", administrationConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AdministrationConfiguration> administrationConfigurationList = administrationConfigurationRepository.findAll();
        assertThat(administrationConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdministrationConfiguration.class);
        AdministrationConfiguration administrationConfiguration1 = new AdministrationConfiguration();
        administrationConfiguration1.setId(1L);
        AdministrationConfiguration administrationConfiguration2 = new AdministrationConfiguration();
        administrationConfiguration2.setId(administrationConfiguration1.getId());
        assertThat(administrationConfiguration1).isEqualTo(administrationConfiguration2);
        administrationConfiguration2.setId(2L);
        assertThat(administrationConfiguration1).isNotEqualTo(administrationConfiguration2);
        administrationConfiguration1.setId(null);
        assertThat(administrationConfiguration1).isNotEqualTo(administrationConfiguration2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdministrationConfigurationDTO.class);
        AdministrationConfigurationDTO administrationConfigurationDTO1 = new AdministrationConfigurationDTO();
        administrationConfigurationDTO1.setId(1L);
        AdministrationConfigurationDTO administrationConfigurationDTO2 = new AdministrationConfigurationDTO();
        assertThat(administrationConfigurationDTO1).isNotEqualTo(administrationConfigurationDTO2);
        administrationConfigurationDTO2.setId(administrationConfigurationDTO1.getId());
        assertThat(administrationConfigurationDTO1).isEqualTo(administrationConfigurationDTO2);
        administrationConfigurationDTO2.setId(2L);
        assertThat(administrationConfigurationDTO1).isNotEqualTo(administrationConfigurationDTO2);
        administrationConfigurationDTO1.setId(null);
        assertThat(administrationConfigurationDTO1).isNotEqualTo(administrationConfigurationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(administrationConfigurationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(administrationConfigurationMapper.fromId(null)).isNull();
    }
}
