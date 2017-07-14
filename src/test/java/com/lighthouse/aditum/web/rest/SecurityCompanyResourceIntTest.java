package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.SecurityCompany;
import com.lighthouse.aditum.repository.SecurityCompanyRepository;
import com.lighthouse.aditum.service.dto.SecurityCompanyDTO;
import com.lighthouse.aditum.service.mapper.SecurityCompanyMapper;
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
 * Test class for the SecurityCompanyResource REST controller.
 *
 * @see SecurityCompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class SecurityCompanyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private SecurityCompanyRepository securityCompanyRepository;

    @Autowired
    private SecurityCompanyMapper securityCompanyMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSecurityCompanyMockMvc;

    private SecurityCompany securityCompany;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityCompanyResource securityCompanyResource = new SecurityCompanyResource(securityCompanyRepository, securityCompanyMapper);
        this.restSecurityCompanyMockMvc = MockMvcBuilders.standaloneSetup(securityCompanyResource)
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
    public static SecurityCompany createEntity(EntityManager em) {
        SecurityCompany securityCompany = new SecurityCompany()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE);
        return securityCompany;
    }

    @Before
    public void initTest() {
        securityCompany = createEntity(em);
    }

    @Test
    @Transactional
    public void createSecurityCompany() throws Exception {
        int databaseSizeBeforeCreate = securityCompanyRepository.findAll().size();

        // Create the SecurityCompany
        SecurityCompanyDTO securityCompanyDTO = securityCompanyMapper.toDto(securityCompany);
        restSecurityCompanyMockMvc.perform(post("/api/security-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(securityCompanyDTO)))
            .andExpect(status().isCreated());

        // Validate the SecurityCompany in the database
        List<SecurityCompany> securityCompanyList = securityCompanyRepository.findAll();
        assertThat(securityCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityCompany testSecurityCompany = securityCompanyList.get(securityCompanyList.size() - 1);
        assertThat(testSecurityCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecurityCompany.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createSecurityCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = securityCompanyRepository.findAll().size();

        // Create the SecurityCompany with an existing ID
        securityCompany.setId(1L);
        SecurityCompanyDTO securityCompanyDTO = securityCompanyMapper.toDto(securityCompany);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityCompanyMockMvc.perform(post("/api/security-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(securityCompanyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SecurityCompany> securityCompanyList = securityCompanyRepository.findAll();
        assertThat(securityCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSecurityCompanies() throws Exception {
        // Initialize the database
        securityCompanyRepository.saveAndFlush(securityCompany);

        // Get all the securityCompanyList
        restSecurityCompanyMockMvc.perform(get("/api/security-companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getSecurityCompany() throws Exception {
        // Initialize the database
        securityCompanyRepository.saveAndFlush(securityCompany);

        // Get the securityCompany
        restSecurityCompanyMockMvc.perform(get("/api/security-companies/{id}", securityCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(securityCompany.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityCompany() throws Exception {
        // Get the securityCompany
        restSecurityCompanyMockMvc.perform(get("/api/security-companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityCompany() throws Exception {
        // Initialize the database
        securityCompanyRepository.saveAndFlush(securityCompany);
        int databaseSizeBeforeUpdate = securityCompanyRepository.findAll().size();

        // Update the securityCompany
        SecurityCompany updatedSecurityCompany = securityCompanyRepository.findOne(securityCompany.getId());
        updatedSecurityCompany
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);
        SecurityCompanyDTO securityCompanyDTO = securityCompanyMapper.toDto(updatedSecurityCompany);

        restSecurityCompanyMockMvc.perform(put("/api/security-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(securityCompanyDTO)))
            .andExpect(status().isOk());

        // Validate the SecurityCompany in the database
        List<SecurityCompany> securityCompanyList = securityCompanyRepository.findAll();
        assertThat(securityCompanyList).hasSize(databaseSizeBeforeUpdate);
        SecurityCompany testSecurityCompany = securityCompanyList.get(securityCompanyList.size() - 1);
        assertThat(testSecurityCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecurityCompany.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingSecurityCompany() throws Exception {
        int databaseSizeBeforeUpdate = securityCompanyRepository.findAll().size();

        // Create the SecurityCompany
        SecurityCompanyDTO securityCompanyDTO = securityCompanyMapper.toDto(securityCompany);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSecurityCompanyMockMvc.perform(put("/api/security-companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(securityCompanyDTO)))
            .andExpect(status().isCreated());

        // Validate the SecurityCompany in the database
        List<SecurityCompany> securityCompanyList = securityCompanyRepository.findAll();
        assertThat(securityCompanyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSecurityCompany() throws Exception {
        // Initialize the database
        securityCompanyRepository.saveAndFlush(securityCompany);
        int databaseSizeBeforeDelete = securityCompanyRepository.findAll().size();

        // Get the securityCompany
        restSecurityCompanyMockMvc.perform(delete("/api/security-companies/{id}", securityCompany.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SecurityCompany> securityCompanyList = securityCompanyRepository.findAll();
        assertThat(securityCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityCompany.class);
        SecurityCompany securityCompany1 = new SecurityCompany();
        securityCompany1.setId(1L);
        SecurityCompany securityCompany2 = new SecurityCompany();
        securityCompany2.setId(securityCompany1.getId());
        assertThat(securityCompany1).isEqualTo(securityCompany2);
        securityCompany2.setId(2L);
        assertThat(securityCompany1).isNotEqualTo(securityCompany2);
        securityCompany1.setId(null);
        assertThat(securityCompany1).isNotEqualTo(securityCompany2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityCompanyDTO.class);
        SecurityCompanyDTO securityCompanyDTO1 = new SecurityCompanyDTO();
        securityCompanyDTO1.setId(1L);
        SecurityCompanyDTO securityCompanyDTO2 = new SecurityCompanyDTO();
        assertThat(securityCompanyDTO1).isNotEqualTo(securityCompanyDTO2);
        securityCompanyDTO2.setId(securityCompanyDTO1.getId());
        assertThat(securityCompanyDTO1).isEqualTo(securityCompanyDTO2);
        securityCompanyDTO2.setId(2L);
        assertThat(securityCompanyDTO1).isNotEqualTo(securityCompanyDTO2);
        securityCompanyDTO1.setId(null);
        assertThat(securityCompanyDTO1).isNotEqualTo(securityCompanyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(securityCompanyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(securityCompanyMapper.fromId(null)).isNull();
    }
}
