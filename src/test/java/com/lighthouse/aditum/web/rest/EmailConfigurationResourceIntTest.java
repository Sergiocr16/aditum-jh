package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.EmailConfiguration;
import com.lighthouse.aditum.repository.EmailConfigurationRepository;
import com.lighthouse.aditum.service.EmailConfigurationService;
import com.lighthouse.aditum.service.dto.EmailConfigurationDTO;
import com.lighthouse.aditum.service.mapper.EmailConfigurationMapper;
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
 * Test class for the EmailConfigurationResource REST controller.
 *
 * @see EmailConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class EmailConfigurationResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CUSTOM_EMAIL = false;
    private static final Boolean UPDATED_CUSTOM_EMAIL = true;

    private static final String DEFAULT_EMAIL_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_ADMIN_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ADMIN_COMPANY_NAME = "BBBBBBBBBB";

    @Autowired
    private EmailConfigurationRepository emailConfigurationRepository;

    @Autowired
    private EmailConfigurationMapper emailConfigurationMapper;

    @Autowired
    private EmailConfigurationService emailConfigurationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmailConfigurationMockMvc;

    private EmailConfiguration emailConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailConfigurationResource emailConfigurationResource = new EmailConfigurationResource(emailConfigurationService);
        this.restEmailConfigurationMockMvc = MockMvcBuilders.standaloneSetup(emailConfigurationResource)
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
    public static EmailConfiguration createEntity(EntityManager em) {
        EmailConfiguration emailConfiguration = new EmailConfiguration()
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .customEmail(DEFAULT_CUSTOM_EMAIL)
            .emailCompany(DEFAULT_EMAIL_COMPANY)
            .adminCompanyName(DEFAULT_ADMIN_COMPANY_NAME);
        return emailConfiguration;
    }

    @Before
    public void initTest() {
        emailConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailConfiguration() throws Exception {
        int databaseSizeBeforeCreate = emailConfigurationRepository.findAll().size();

        // Create the EmailConfiguration
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(emailConfiguration);
        restEmailConfigurationMockMvc.perform(post("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        EmailConfiguration testEmailConfiguration = emailConfigurationList.get(emailConfigurationList.size() - 1);
        assertThat(testEmailConfiguration.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmailConfiguration.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testEmailConfiguration.isCustomEmail()).isEqualTo(DEFAULT_CUSTOM_EMAIL);
        assertThat(testEmailConfiguration.getEmailCompany()).isEqualTo(DEFAULT_EMAIL_COMPANY);
        assertThat(testEmailConfiguration.getAdminCompanyName()).isEqualTo(DEFAULT_ADMIN_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void createEmailConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailConfigurationRepository.findAll().size();

        // Create the EmailConfiguration with an existing ID
        emailConfiguration.setId(1L);
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(emailConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailConfigurationMockMvc.perform(post("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmailConfigurations() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);

        // Get all the emailConfigurationList
        restEmailConfigurationMockMvc.perform(get("/api/email-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].customEmail").value(hasItem(DEFAULT_CUSTOM_EMAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].emailCompany").value(hasItem(DEFAULT_EMAIL_COMPANY.toString())))
            .andExpect(jsonPath("$.[*].adminCompanyName").value(hasItem(DEFAULT_ADMIN_COMPANY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getEmailConfiguration() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);

        // Get the emailConfiguration
        restEmailConfigurationMockMvc.perform(get("/api/email-configurations/{id}", emailConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emailConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.customEmail").value(DEFAULT_CUSTOM_EMAIL.booleanValue()))
            .andExpect(jsonPath("$.emailCompany").value(DEFAULT_EMAIL_COMPANY.toString()))
            .andExpect(jsonPath("$.adminCompanyName").value(DEFAULT_ADMIN_COMPANY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmailConfiguration() throws Exception {
        // Get the emailConfiguration
        restEmailConfigurationMockMvc.perform(get("/api/email-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailConfiguration() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);
        int databaseSizeBeforeUpdate = emailConfigurationRepository.findAll().size();

        // Update the emailConfiguration
        EmailConfiguration updatedEmailConfiguration = emailConfigurationRepository.findOne(emailConfiguration.getId());
        // Disconnect from session so that the updates on updatedEmailConfiguration are not directly saved in db
        em.detach(updatedEmailConfiguration);
        updatedEmailConfiguration
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .customEmail(UPDATED_CUSTOM_EMAIL)
            .emailCompany(UPDATED_EMAIL_COMPANY)
            .adminCompanyName(UPDATED_ADMIN_COMPANY_NAME);
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(updatedEmailConfiguration);

        restEmailConfigurationMockMvc.perform(put("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isOk());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeUpdate);
        EmailConfiguration testEmailConfiguration = emailConfigurationList.get(emailConfigurationList.size() - 1);
        assertThat(testEmailConfiguration.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmailConfiguration.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testEmailConfiguration.isCustomEmail()).isEqualTo(UPDATED_CUSTOM_EMAIL);
        assertThat(testEmailConfiguration.getEmailCompany()).isEqualTo(UPDATED_EMAIL_COMPANY);
        assertThat(testEmailConfiguration.getAdminCompanyName()).isEqualTo(UPDATED_ADMIN_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = emailConfigurationRepository.findAll().size();

        // Create the EmailConfiguration
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(emailConfiguration);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmailConfigurationMockMvc.perform(put("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmailConfiguration() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);
        int databaseSizeBeforeDelete = emailConfigurationRepository.findAll().size();

        // Get the emailConfiguration
        restEmailConfigurationMockMvc.perform(delete("/api/email-configurations/{id}", emailConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailConfiguration.class);
        EmailConfiguration emailConfiguration1 = new EmailConfiguration();
        emailConfiguration1.setId(1L);
        EmailConfiguration emailConfiguration2 = new EmailConfiguration();
        emailConfiguration2.setId(emailConfiguration1.getId());
        assertThat(emailConfiguration1).isEqualTo(emailConfiguration2);
        emailConfiguration2.setId(2L);
        assertThat(emailConfiguration1).isNotEqualTo(emailConfiguration2);
        emailConfiguration1.setId(null);
        assertThat(emailConfiguration1).isNotEqualTo(emailConfiguration2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailConfigurationDTO.class);
        EmailConfigurationDTO emailConfigurationDTO1 = new EmailConfigurationDTO();
        emailConfigurationDTO1.setId(1L);
        EmailConfigurationDTO emailConfigurationDTO2 = new EmailConfigurationDTO();
        assertThat(emailConfigurationDTO1).isNotEqualTo(emailConfigurationDTO2);
        emailConfigurationDTO2.setId(emailConfigurationDTO1.getId());
        assertThat(emailConfigurationDTO1).isEqualTo(emailConfigurationDTO2);
        emailConfigurationDTO2.setId(2L);
        assertThat(emailConfigurationDTO1).isNotEqualTo(emailConfigurationDTO2);
        emailConfigurationDTO1.setId(null);
        assertThat(emailConfigurationDTO1).isNotEqualTo(emailConfigurationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(emailConfigurationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(emailConfigurationMapper.fromId(null)).isNull();
    }
}
