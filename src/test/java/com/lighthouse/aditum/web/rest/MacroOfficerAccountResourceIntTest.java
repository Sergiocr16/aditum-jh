package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.MacroOfficerAccount;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.domain.MacroCondominium;
import com.lighthouse.aditum.repository.MacroOfficerAccountRepository;
import com.lighthouse.aditum.service.MacroOfficerAccountService;
import com.lighthouse.aditum.service.dto.MacroOfficerAccountDTO;
import com.lighthouse.aditum.service.mapper.MacroOfficerAccountMapper;
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
 * Test class for the MacroOfficerAccountResource REST controller.
 *
 * @see MacroOfficerAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class MacroOfficerAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private MacroOfficerAccountRepository macroOfficerAccountRepository;

    @Autowired
    private MacroOfficerAccountMapper macroOfficerAccountMapper;

    @Autowired
    private MacroOfficerAccountService macroOfficerAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMacroOfficerAccountMockMvc;

    private MacroOfficerAccount macroOfficerAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MacroOfficerAccountResource macroOfficerAccountResource = new MacroOfficerAccountResource(macroOfficerAccountService);
        this.restMacroOfficerAccountMockMvc = MockMvcBuilders.standaloneSetup(macroOfficerAccountResource)
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
    public static MacroOfficerAccount createEntity(EntityManager em) {
        MacroOfficerAccount macroOfficerAccount = new MacroOfficerAccount()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        macroOfficerAccount.setUser(user);
        // Add required entity
        MacroCondominium macroCondominium = MacroCondominiumResourceIntTest.createEntity(em);
        em.persist(macroCondominium);
        em.flush();
        macroOfficerAccount.setMacroCondominium(macroCondominium);
        return macroOfficerAccount;
    }

    @Before
    public void initTest() {
        macroOfficerAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createMacroOfficerAccount() throws Exception {
        int databaseSizeBeforeCreate = macroOfficerAccountRepository.findAll().size();

        // Create the MacroOfficerAccount
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountMapper.toDto(macroOfficerAccount);
        restMacroOfficerAccountMockMvc.perform(post("/api/macro-officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroOfficerAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroOfficerAccount in the database
        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeCreate + 1);
        MacroOfficerAccount testMacroOfficerAccount = macroOfficerAccountList.get(macroOfficerAccountList.size() - 1);
        assertThat(testMacroOfficerAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMacroOfficerAccount.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createMacroOfficerAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = macroOfficerAccountRepository.findAll().size();

        // Create the MacroOfficerAccount with an existing ID
        macroOfficerAccount.setId(1L);
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountMapper.toDto(macroOfficerAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMacroOfficerAccountMockMvc.perform(post("/api/macro-officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroOfficerAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MacroOfficerAccount in the database
        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroOfficerAccountRepository.findAll().size();
        // set the field null
        macroOfficerAccount.setName(null);

        // Create the MacroOfficerAccount, which fails.
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountMapper.toDto(macroOfficerAccount);

        restMacroOfficerAccountMockMvc.perform(post("/api/macro-officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroOfficerAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroOfficerAccountRepository.findAll().size();
        // set the field null
        macroOfficerAccount.setEnabled(null);

        // Create the MacroOfficerAccount, which fails.
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountMapper.toDto(macroOfficerAccount);

        restMacroOfficerAccountMockMvc.perform(post("/api/macro-officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroOfficerAccountDTO)))
            .andExpect(status().isBadRequest());

        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMacroOfficerAccounts() throws Exception {
        // Initialize the database
        macroOfficerAccountRepository.saveAndFlush(macroOfficerAccount);

        // Get all the macroOfficerAccountList
        restMacroOfficerAccountMockMvc.perform(get("/api/macro-officer-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macroOfficerAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMacroOfficerAccount() throws Exception {
        // Initialize the database
        macroOfficerAccountRepository.saveAndFlush(macroOfficerAccount);

        // Get the macroOfficerAccount
        restMacroOfficerAccountMockMvc.perform(get("/api/macro-officer-accounts/{id}", macroOfficerAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(macroOfficerAccount.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMacroOfficerAccount() throws Exception {
        // Get the macroOfficerAccount
        restMacroOfficerAccountMockMvc.perform(get("/api/macro-officer-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacroOfficerAccount() throws Exception {
        // Initialize the database
        macroOfficerAccountRepository.saveAndFlush(macroOfficerAccount);
        int databaseSizeBeforeUpdate = macroOfficerAccountRepository.findAll().size();

        // Update the macroOfficerAccount
        MacroOfficerAccount updatedMacroOfficerAccount = macroOfficerAccountRepository.findOne(macroOfficerAccount.getId());
        // Disconnect from session so that the updates on updatedMacroOfficerAccount are not directly saved in db
        em.detach(updatedMacroOfficerAccount);
        updatedMacroOfficerAccount
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountMapper.toDto(updatedMacroOfficerAccount);

        restMacroOfficerAccountMockMvc.perform(put("/api/macro-officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroOfficerAccountDTO)))
            .andExpect(status().isOk());

        // Validate the MacroOfficerAccount in the database
        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeUpdate);
        MacroOfficerAccount testMacroOfficerAccount = macroOfficerAccountList.get(macroOfficerAccountList.size() - 1);
        assertThat(testMacroOfficerAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMacroOfficerAccount.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingMacroOfficerAccount() throws Exception {
        int databaseSizeBeforeUpdate = macroOfficerAccountRepository.findAll().size();

        // Create the MacroOfficerAccount
        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountMapper.toDto(macroOfficerAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMacroOfficerAccountMockMvc.perform(put("/api/macro-officer-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroOfficerAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroOfficerAccount in the database
        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMacroOfficerAccount() throws Exception {
        // Initialize the database
        macroOfficerAccountRepository.saveAndFlush(macroOfficerAccount);
        int databaseSizeBeforeDelete = macroOfficerAccountRepository.findAll().size();

        // Get the macroOfficerAccount
        restMacroOfficerAccountMockMvc.perform(delete("/api/macro-officer-accounts/{id}", macroOfficerAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MacroOfficerAccount> macroOfficerAccountList = macroOfficerAccountRepository.findAll();
        assertThat(macroOfficerAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroOfficerAccount.class);
        MacroOfficerAccount macroOfficerAccount1 = new MacroOfficerAccount();
        macroOfficerAccount1.setId(1L);
        MacroOfficerAccount macroOfficerAccount2 = new MacroOfficerAccount();
        macroOfficerAccount2.setId(macroOfficerAccount1.getId());
        assertThat(macroOfficerAccount1).isEqualTo(macroOfficerAccount2);
        macroOfficerAccount2.setId(2L);
        assertThat(macroOfficerAccount1).isNotEqualTo(macroOfficerAccount2);
        macroOfficerAccount1.setId(null);
        assertThat(macroOfficerAccount1).isNotEqualTo(macroOfficerAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroOfficerAccountDTO.class);
        MacroOfficerAccountDTO macroOfficerAccountDTO1 = new MacroOfficerAccountDTO();
        macroOfficerAccountDTO1.setId(1L);
        MacroOfficerAccountDTO macroOfficerAccountDTO2 = new MacroOfficerAccountDTO();
        assertThat(macroOfficerAccountDTO1).isNotEqualTo(macroOfficerAccountDTO2);
        macroOfficerAccountDTO2.setId(macroOfficerAccountDTO1.getId());
        assertThat(macroOfficerAccountDTO1).isEqualTo(macroOfficerAccountDTO2);
        macroOfficerAccountDTO2.setId(2L);
        assertThat(macroOfficerAccountDTO1).isNotEqualTo(macroOfficerAccountDTO2);
        macroOfficerAccountDTO1.setId(null);
        assertThat(macroOfficerAccountDTO1).isNotEqualTo(macroOfficerAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(macroOfficerAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(macroOfficerAccountMapper.fromId(null)).isNull();
    }
}
