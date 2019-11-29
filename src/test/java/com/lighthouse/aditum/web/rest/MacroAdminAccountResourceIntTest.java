package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.MacroAdminAccount;
import com.lighthouse.aditum.domain.MacroCondominium;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.repository.MacroAdminAccountRepository;
import com.lighthouse.aditum.service.MacroAdminAccountService;
import com.lighthouse.aditum.service.dto.MacroAdminAccountDTO;
import com.lighthouse.aditum.service.mapper.MacroAdminAccountMapper;
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
 * Test class for the MacroAdminAccountResource REST controller.
 *
 * @see MacroAdminAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class MacroAdminAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    @Autowired
    private MacroAdminAccountRepository macroAdminAccountRepository;

    @Autowired
    private MacroAdminAccountMapper macroAdminAccountMapper;

    @Autowired
    private MacroAdminAccountService macroAdminAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMacroAdminAccountMockMvc;

    private MacroAdminAccount macroAdminAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MacroAdminAccountResource macroAdminAccountResource = new MacroAdminAccountResource(macroAdminAccountService);
        this.restMacroAdminAccountMockMvc = MockMvcBuilders.standaloneSetup(macroAdminAccountResource)
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
    public static MacroAdminAccount createEntity(EntityManager em) {
        MacroAdminAccount macroAdminAccount = new MacroAdminAccount()
            .name(DEFAULT_NAME)
            .lastname(DEFAULT_LASTNAME)
            .secondlastname(DEFAULT_SECONDLASTNAME)
            .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
            .email(DEFAULT_EMAIL)
            .enabled(DEFAULT_ENABLED)
            .imageUrl(DEFAULT_IMAGE_URL);
        // Add required entity
        MacroCondominium macroCondominium = MacroCondominiumResourceIntTest.createEntity(em);
        em.persist(macroCondominium);
        em.flush();
        macroAdminAccount.setMacroCondominium(macroCondominium);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        macroAdminAccount.setUser(user);
        return macroAdminAccount;
    }

    @Before
    public void initTest() {
        macroAdminAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createMacroAdminAccount() throws Exception {
        int databaseSizeBeforeCreate = macroAdminAccountRepository.findAll().size();

        // Create the MacroAdminAccount
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);
        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroAdminAccount in the database
        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeCreate + 1);
        MacroAdminAccount testMacroAdminAccount = macroAdminAccountList.get(macroAdminAccountList.size() - 1);
        assertThat(testMacroAdminAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMacroAdminAccount.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testMacroAdminAccount.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testMacroAdminAccount.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testMacroAdminAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMacroAdminAccount.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testMacroAdminAccount.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    public void createMacroAdminAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = macroAdminAccountRepository.findAll().size();

        // Create the MacroAdminAccount with an existing ID
        macroAdminAccount.setId(1L);
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MacroAdminAccount in the database
        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroAdminAccountRepository.findAll().size();
        // set the field null
        macroAdminAccount.setName(null);

        // Create the MacroAdminAccount, which fails.
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroAdminAccountRepository.findAll().size();
        // set the field null
        macroAdminAccount.setLastname(null);

        // Create the MacroAdminAccount, which fails.
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroAdminAccountRepository.findAll().size();
        // set the field null
        macroAdminAccount.setSecondlastname(null);

        // Create the MacroAdminAccount, which fails.
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentificationnumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroAdminAccountRepository.findAll().size();
        // set the field null
        macroAdminAccount.setIdentificationnumber(null);

        // Create the MacroAdminAccount, which fails.
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroAdminAccountRepository.findAll().size();
        // set the field null
        macroAdminAccount.setEmail(null);

        // Create the MacroAdminAccount, which fails.
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroAdminAccountRepository.findAll().size();
        // set the field null
        macroAdminAccount.setEnabled(null);

        // Create the MacroAdminAccount, which fails.
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        restMacroAdminAccountMockMvc.perform(post("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMacroAdminAccounts() throws Exception {
        // Initialize the database
        macroAdminAccountRepository.saveAndFlush(macroAdminAccount);

        // Get all the macroAdminAccountList
        restMacroAdminAccountMockMvc.perform(get("/api/macro-admin-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macroAdminAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())));
    }

    @Test
    @Transactional
    public void getMacroAdminAccount() throws Exception {
        // Initialize the database
        macroAdminAccountRepository.saveAndFlush(macroAdminAccount);

        // Get the macroAdminAccount
        restMacroAdminAccountMockMvc.perform(get("/api/macro-admin-accounts/{id}", macroAdminAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(macroAdminAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMacroAdminAccount() throws Exception {
        // Get the macroAdminAccount
        restMacroAdminAccountMockMvc.perform(get("/api/macro-admin-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacroAdminAccount() throws Exception {
        // Initialize the database
        macroAdminAccountRepository.saveAndFlush(macroAdminAccount);
        int databaseSizeBeforeUpdate = macroAdminAccountRepository.findAll().size();

        // Update the macroAdminAccount
        MacroAdminAccount updatedMacroAdminAccount = macroAdminAccountRepository.findOne(macroAdminAccount.getId());
        // Disconnect from session so that the updates on updatedMacroAdminAccount are not directly saved in db
        em.detach(updatedMacroAdminAccount);
        updatedMacroAdminAccount
            .name(UPDATED_NAME)
            .lastname(UPDATED_LASTNAME)
            .secondlastname(UPDATED_SECONDLASTNAME)
            .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
            .email(UPDATED_EMAIL)
            .enabled(UPDATED_ENABLED)
            .imageUrl(UPDATED_IMAGE_URL);
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(updatedMacroAdminAccount);

        restMacroAdminAccountMockMvc.perform(put("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isOk());

        // Validate the MacroAdminAccount in the database
        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeUpdate);
        MacroAdminAccount testMacroAdminAccount = macroAdminAccountList.get(macroAdminAccountList.size() - 1);
        assertThat(testMacroAdminAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMacroAdminAccount.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testMacroAdminAccount.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testMacroAdminAccount.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testMacroAdminAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMacroAdminAccount.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testMacroAdminAccount.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingMacroAdminAccount() throws Exception {
        int databaseSizeBeforeUpdate = macroAdminAccountRepository.findAll().size();

        // Create the MacroAdminAccount
        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountMapper.toDto(macroAdminAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMacroAdminAccountMockMvc.perform(put("/api/macro-admin-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroAdminAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroAdminAccount in the database
        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMacroAdminAccount() throws Exception {
        // Initialize the database
        macroAdminAccountRepository.saveAndFlush(macroAdminAccount);
        int databaseSizeBeforeDelete = macroAdminAccountRepository.findAll().size();

        // Get the macroAdminAccount
        restMacroAdminAccountMockMvc.perform(delete("/api/macro-admin-accounts/{id}", macroAdminAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MacroAdminAccount> macroAdminAccountList = macroAdminAccountRepository.findAll();
        assertThat(macroAdminAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroAdminAccount.class);
        MacroAdminAccount macroAdminAccount1 = new MacroAdminAccount();
        macroAdminAccount1.setId(1L);
        MacroAdminAccount macroAdminAccount2 = new MacroAdminAccount();
        macroAdminAccount2.setId(macroAdminAccount1.getId());
        assertThat(macroAdminAccount1).isEqualTo(macroAdminAccount2);
        macroAdminAccount2.setId(2L);
        assertThat(macroAdminAccount1).isNotEqualTo(macroAdminAccount2);
        macroAdminAccount1.setId(null);
        assertThat(macroAdminAccount1).isNotEqualTo(macroAdminAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroAdminAccountDTO.class);
        MacroAdminAccountDTO macroAdminAccountDTO1 = new MacroAdminAccountDTO();
        macroAdminAccountDTO1.setId(1L);
        MacroAdminAccountDTO macroAdminAccountDTO2 = new MacroAdminAccountDTO();
        assertThat(macroAdminAccountDTO1).isNotEqualTo(macroAdminAccountDTO2);
        macroAdminAccountDTO2.setId(macroAdminAccountDTO1.getId());
        assertThat(macroAdminAccountDTO1).isEqualTo(macroAdminAccountDTO2);
        macroAdminAccountDTO2.setId(2L);
        assertThat(macroAdminAccountDTO1).isNotEqualTo(macroAdminAccountDTO2);
        macroAdminAccountDTO1.setId(null);
        assertThat(macroAdminAccountDTO1).isNotEqualTo(macroAdminAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(macroAdminAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(macroAdminAccountMapper.fromId(null)).isNull();
    }
}
