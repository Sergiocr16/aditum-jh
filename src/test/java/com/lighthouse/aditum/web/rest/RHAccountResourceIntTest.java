package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.RHAccount;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.RHAccountRepository;
import com.lighthouse.aditum.service.RHAccountService;
import com.lighthouse.aditum.service.dto.RHAccountDTO;
import com.lighthouse.aditum.service.mapper.RHAccountMapper;
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
 * Test class for the RHAccountResource REST controller.
 *
 * @see RHAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RHAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENTERPRISENAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTERPRISENAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private RHAccountRepository rHAccountRepository;

    @Autowired
    private RHAccountMapper rHAccountMapper;

    @Autowired
    private RHAccountService rHAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRHAccountMockMvc;

    private RHAccount rHAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RHAccountResource rHAccountResource = new RHAccountResource(rHAccountService);
        this.restRHAccountMockMvc = MockMvcBuilders.standaloneSetup(rHAccountResource)
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
    public static RHAccount createEntity(EntityManager em) {
        RHAccount rHAccount = new RHAccount()
                .name(DEFAULT_NAME)
                .lastname(DEFAULT_LASTNAME)
                .secondlastname(DEFAULT_SECONDLASTNAME)
                .enterprisename(DEFAULT_ENTERPRISENAME)
                .email(DEFAULT_EMAIL);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        rHAccount.getCompanies().add(company);
        return rHAccount;
    }

    @Before
    public void initTest() {
        rHAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createRHAccount() throws Exception {
        int databaseSizeBeforeCreate = rHAccountRepository.findAll().size();

        // Create the RHAccount
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);

        restRHAccountMockMvc.perform(post("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the RHAccount in the database
        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeCreate + 1);
        RHAccount testRHAccount = rHAccountList.get(rHAccountList.size() - 1);
        assertThat(testRHAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRHAccount.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testRHAccount.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testRHAccount.getEnterprisename()).isEqualTo(DEFAULT_ENTERPRISENAME);
        assertThat(testRHAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createRHAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rHAccountRepository.findAll().size();

        // Create the RHAccount with an existing ID
        RHAccount existingRHAccount = new RHAccount();
        existingRHAccount.setId(1L);
        RHAccountDTO existingRHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(existingRHAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRHAccountMockMvc.perform(post("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRHAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rHAccountRepository.findAll().size();
        // set the field null
        rHAccount.setName(null);

        // Create the RHAccount, which fails.
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);

        restRHAccountMockMvc.perform(post("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isBadRequest());

        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rHAccountRepository.findAll().size();
        // set the field null
        rHAccount.setLastname(null);

        // Create the RHAccount, which fails.
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);

        restRHAccountMockMvc.perform(post("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isBadRequest());

        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rHAccountRepository.findAll().size();
        // set the field null
        rHAccount.setSecondlastname(null);

        // Create the RHAccount, which fails.
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);

        restRHAccountMockMvc.perform(post("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isBadRequest());

        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnterprisenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rHAccountRepository.findAll().size();
        // set the field null
        rHAccount.setEnterprisename(null);

        // Create the RHAccount, which fails.
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);

        restRHAccountMockMvc.perform(post("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isBadRequest());

        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRHAccounts() throws Exception {
        // Initialize the database
        rHAccountRepository.saveAndFlush(rHAccount);

        // Get all the rHAccountList
        restRHAccountMockMvc.perform(get("/api/r-h-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rHAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].enterprisename").value(hasItem(DEFAULT_ENTERPRISENAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getRHAccount() throws Exception {
        // Initialize the database
        rHAccountRepository.saveAndFlush(rHAccount);

        // Get the rHAccount
        restRHAccountMockMvc.perform(get("/api/r-h-accounts/{id}", rHAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rHAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.enterprisename").value(DEFAULT_ENTERPRISENAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRHAccount() throws Exception {
        // Get the rHAccount
        restRHAccountMockMvc.perform(get("/api/r-h-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRHAccount() throws Exception {
        // Initialize the database
        rHAccountRepository.saveAndFlush(rHAccount);
        int databaseSizeBeforeUpdate = rHAccountRepository.findAll().size();

        // Update the rHAccount
        RHAccount updatedRHAccount = rHAccountRepository.findOne(rHAccount.getId());
        updatedRHAccount
                .name(UPDATED_NAME)
                .lastname(UPDATED_LASTNAME)
                .secondlastname(UPDATED_SECONDLASTNAME)
                .enterprisename(UPDATED_ENTERPRISENAME)
                .email(UPDATED_EMAIL);
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(updatedRHAccount);

        restRHAccountMockMvc.perform(put("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isOk());

        // Validate the RHAccount in the database
        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeUpdate);
        RHAccount testRHAccount = rHAccountList.get(rHAccountList.size() - 1);
        assertThat(testRHAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRHAccount.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testRHAccount.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testRHAccount.getEnterprisename()).isEqualTo(UPDATED_ENTERPRISENAME);
        assertThat(testRHAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingRHAccount() throws Exception {
        int databaseSizeBeforeUpdate = rHAccountRepository.findAll().size();

        // Create the RHAccount
        RHAccountDTO rHAccountDTO = rHAccountMapper.rHAccountToRHAccountDTO(rHAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRHAccountMockMvc.perform(put("/api/r-h-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rHAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the RHAccount in the database
        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRHAccount() throws Exception {
        // Initialize the database
        rHAccountRepository.saveAndFlush(rHAccount);
        int databaseSizeBeforeDelete = rHAccountRepository.findAll().size();

        // Get the rHAccount
        restRHAccountMockMvc.perform(delete("/api/r-h-accounts/{id}", rHAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RHAccount> rHAccountList = rHAccountRepository.findAll();
        assertThat(rHAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RHAccount.class);
    }
}
