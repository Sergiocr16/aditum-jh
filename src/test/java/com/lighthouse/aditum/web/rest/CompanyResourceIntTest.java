package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.CompanyRepository;
import com.lighthouse.aditum.service.CompanyService;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CompanyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACTIVE = 0;
    private static final Integer UPDATED_ACTIVE = 1;

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTION = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADMIN_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_ADMIN_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_TYPE = "BBBBBBBBBB";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyService);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .logoUrl(DEFAULT_LOGO_URL)
            .direction(DEFAULT_DIRECTION)
            .legalIdentification(DEFAULT_LEGAL_IDENTIFICATION)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .supportEmail(DEFAULT_SUPPORT_EMAIL)
            .supportNumber(DEFAULT_SUPPORT_NUMBER)
            .email(DEFAULT_EMAIL)
            .legalName(DEFAULT_LEGAL_NAME)
            .adminLogoUrl(DEFAULT_ADMIN_LOGO_URL)
            .productType(DEFAULT_PRODUCT_TYPE);
        return company;
    }

    @Before
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCompany.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testCompany.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testCompany.getLegalIdentification()).isEqualTo(DEFAULT_LEGAL_IDENTIFICATION);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCompany.getSupportEmail()).isEqualTo(DEFAULT_SUPPORT_EMAIL);
        assertThat(testCompany.getSupportNumber()).isEqualTo(DEFAULT_SUPPORT_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getLegalName()).isEqualTo(DEFAULT_LEGAL_NAME);
        assertThat(testCompany.getAdminLogoUrl()).isEqualTo(DEFAULT_ADMIN_LOGO_URL);
        assertThat(testCompany.getProductType()).isEqualTo(DEFAULT_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId(1L);
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setActive(null);

        // Create the Company, which fails.
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].legalIdentification").value(hasItem(DEFAULT_LEGAL_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].supportEmail").value(hasItem(DEFAULT_SUPPORT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].supportNumber").value(hasItem(DEFAULT_SUPPORT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME.toString())))
            .andExpect(jsonPath("$.[*].adminLogoUrl").value(hasItem(DEFAULT_ADMIN_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].productType").value(hasItem(DEFAULT_PRODUCT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.legalIdentification").value(DEFAULT_LEGAL_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.supportEmail").value(DEFAULT_SUPPORT_EMAIL.toString()))
            .andExpect(jsonPath("$.supportNumber").value(DEFAULT_SUPPORT_NUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.legalName").value(DEFAULT_LEGAL_NAME.toString()))
            .andExpect(jsonPath("$.adminLogoUrl").value(DEFAULT_ADMIN_LOGO_URL.toString()))
            .andExpect(jsonPath("$.productType").value(DEFAULT_PRODUCT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findOne(company.getId());
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .logoUrl(UPDATED_LOGO_URL)
            .direction(UPDATED_DIRECTION)
            .legalIdentification(UPDATED_LEGAL_IDENTIFICATION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .supportEmail(UPDATED_SUPPORT_EMAIL)
            .supportNumber(UPDATED_SUPPORT_NUMBER)
            .email(UPDATED_EMAIL)
            .legalName(UPDATED_LEGAL_NAME)
            .adminLogoUrl(UPDATED_ADMIN_LOGO_URL)
            .productType(UPDATED_PRODUCT_TYPE);
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(updatedCompany);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCompany.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testCompany.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testCompany.getLegalIdentification()).isEqualTo(UPDATED_LEGAL_IDENTIFICATION);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCompany.getSupportEmail()).isEqualTo(UPDATED_SUPPORT_EMAIL);
        assertThat(testCompany.getSupportNumber()).isEqualTo(UPDATED_SUPPORT_NUMBER);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getLegalName()).isEqualTo(UPDATED_LEGAL_NAME);
        assertThat(testCompany.getAdminLogoUrl()).isEqualTo(UPDATED_ADMIN_LOGO_URL);
        assertThat(testCompany.getProductType()).isEqualTo(UPDATED_PRODUCT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = new Company();
        company1.setId(1L);
        Company company2 = new Company();
        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);
        company2.setId(2L);
        assertThat(company1).isNotEqualTo(company2);
        company1.setId(null);
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyDTO.class);
        CompanyDTO companyDTO1 = new CompanyDTO();
        companyDTO1.setId(1L);
        CompanyDTO companyDTO2 = new CompanyDTO();
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO2.setId(companyDTO1.getId());
        assertThat(companyDTO1).isEqualTo(companyDTO2);
        companyDTO2.setId(2L);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
        companyDTO1.setId(null);
        assertThat(companyDTO1).isNotEqualTo(companyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(companyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(companyMapper.fromId(null)).isNull();
    }
}
