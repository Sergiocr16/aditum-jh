package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.OfficerAccount;
import com.lighthouse.aditum.repository.OfficerAccountRepository;
import com.lighthouse.aditum.service.OfficerAccountService;
import com.lighthouse.aditum.service.dto.OfficerAccountDTO;
import com.lighthouse.aditum.service.mapper.OfficerAccountMapper;
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
 * Test class for the OfficerAccountResource REST controller.
 *
 * @see OfficerAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class OfficerAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENABLE = 1;
    private static final Integer UPDATED_ENABLE = 2;

    @Autowired
    private OfficerAccountRepository officerAccountRepository;

    @Autowired
    private OfficerAccountMapper officerAccountMapper;

    @Autowired
    private OfficerAccountService officerAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfficerAccountMockMvc;

    private OfficerAccount officerAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfficerAccountResource officerAccountResource = new OfficerAccountResource(officerAccountService);
        this.restOfficerAccountMockMvc = MockMvcBuilders.standaloneSetup(officerAccountResource)
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
    public static OfficerAccount createEntity(EntityManager em) {
        OfficerAccount officerAccount = new OfficerAccount()
                .name(DEFAULT_NAME)
                .enable(DEFAULT_ENABLE);
        return officerAccount;
    }

    @Before
    public void initTest() {
        officerAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createOfficerAccount() throws Exception {
        int databaseSizeBeforeCreate = officerAccountRepository.findAll().size();

        // Create the OfficerAccount
        OfficerAccountDTO officerAccountDTO = officerAccountMapper.officerAccountToOfficerAccountDTO(officerAccount);

        restOfficerAccountMockMvc.perform(post("/api/officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the OfficerAccount in the database
        List<OfficerAccount> officerAccountList = officerAccountRepository.findAll();
        assertThat(officerAccountList).hasSize(databaseSizeBeforeCreate + 1);
        OfficerAccount testOfficerAccount = officerAccountList.get(officerAccountList.size() - 1);
        assertThat(testOfficerAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOfficerAccount.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    public void createOfficerAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = officerAccountRepository.findAll().size();

        // Create the OfficerAccount with an existing ID
        OfficerAccount existingOfficerAccount = new OfficerAccount();
        existingOfficerAccount.setId(1L);
        OfficerAccountDTO existingOfficerAccountDTO = officerAccountMapper.officerAccountToOfficerAccountDTO(existingOfficerAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficerAccountMockMvc.perform(post("/api/officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingOfficerAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OfficerAccount> officerAccountList = officerAccountRepository.findAll();
        assertThat(officerAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOfficerAccounts() throws Exception {
        // Initialize the database
        officerAccountRepository.saveAndFlush(officerAccount);

        // Get all the officerAccountList
        restOfficerAccountMockMvc.perform(get("/api/officer-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(officerAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE)));
    }

    @Test
    @Transactional
    public void getOfficerAccount() throws Exception {
        // Initialize the database
        officerAccountRepository.saveAndFlush(officerAccount);

        // Get the officerAccount
        restOfficerAccountMockMvc.perform(get("/api/officer-accounts/{id}", officerAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(officerAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE));
    }

    @Test
    @Transactional
    public void getNonExistingOfficerAccount() throws Exception {
        // Get the officerAccount
        restOfficerAccountMockMvc.perform(get("/api/officer-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOfficerAccount() throws Exception {
        // Initialize the database
        officerAccountRepository.saveAndFlush(officerAccount);
        int databaseSizeBeforeUpdate = officerAccountRepository.findAll().size();

        // Update the officerAccount
        OfficerAccount updatedOfficerAccount = officerAccountRepository.findOne(officerAccount.getId());
        updatedOfficerAccount
                .name(UPDATED_NAME)
                .enable(UPDATED_ENABLE);
        OfficerAccountDTO officerAccountDTO = officerAccountMapper.officerAccountToOfficerAccountDTO(updatedOfficerAccount);

        restOfficerAccountMockMvc.perform(put("/api/officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerAccountDTO)))
            .andExpect(status().isOk());

        // Validate the OfficerAccount in the database
        List<OfficerAccount> officerAccountList = officerAccountRepository.findAll();
        assertThat(officerAccountList).hasSize(databaseSizeBeforeUpdate);
        OfficerAccount testOfficerAccount = officerAccountList.get(officerAccountList.size() - 1);
        assertThat(testOfficerAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOfficerAccount.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    public void updateNonExistingOfficerAccount() throws Exception {
        int databaseSizeBeforeUpdate = officerAccountRepository.findAll().size();

        // Create the OfficerAccount
        OfficerAccountDTO officerAccountDTO = officerAccountMapper.officerAccountToOfficerAccountDTO(officerAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfficerAccountMockMvc.perform(put("/api/officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the OfficerAccount in the database
        List<OfficerAccount> officerAccountList = officerAccountRepository.findAll();
        assertThat(officerAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOfficerAccount() throws Exception {
        // Initialize the database
        officerAccountRepository.saveAndFlush(officerAccount);
        int databaseSizeBeforeDelete = officerAccountRepository.findAll().size();

        // Get the officerAccount
        restOfficerAccountMockMvc.perform(delete("/api/officer-accounts/{id}", officerAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OfficerAccount> officerAccountList = officerAccountRepository.findAll();
        assertThat(officerAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfficerAccount.class);
    }
}
