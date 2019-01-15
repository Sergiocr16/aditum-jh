package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.JuntaDirectivaAccount;
import com.lighthouse.aditum.repository.JuntaDirectivaAccountRepository;
import com.lighthouse.aditum.service.JuntaDirectivaAccountService;
import com.lighthouse.aditum.service.dto.JuntaDirectivaAccountDTO;
import com.lighthouse.aditum.service.mapper.JuntaDirectivaAccountMapper;
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
 * Test class for the JuntaDirectivaAccountResource REST controller.
 *
 * @see JuntaDirectivaAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class JuntaDirectivaAccountResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private JuntaDirectivaAccountRepository juntaDirectivaAccountRepository;

    @Autowired
    private JuntaDirectivaAccountMapper juntaDirectivaAccountMapper;

    @Autowired
    private JuntaDirectivaAccountService juntaDirectivaAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJuntaDirectivaAccountMockMvc;

    private JuntaDirectivaAccount juntaDirectivaAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JuntaDirectivaAccountResource juntaDirectivaAccountResource = new JuntaDirectivaAccountResource(juntaDirectivaAccountService);
        this.restJuntaDirectivaAccountMockMvc = MockMvcBuilders.standaloneSetup(juntaDirectivaAccountResource)
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
    public static JuntaDirectivaAccount createEntity(EntityManager em) {
        JuntaDirectivaAccount juntaDirectivaAccount = new JuntaDirectivaAccount()
            .email(DEFAULT_EMAIL);
        return juntaDirectivaAccount;
    }

    @Before
    public void initTest() {
        juntaDirectivaAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createJuntaDirectivaAccount() throws Exception {
        int databaseSizeBeforeCreate = juntaDirectivaAccountRepository.findAll().size();

        // Create the JuntaDirectivaAccount
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(juntaDirectivaAccount);
        restJuntaDirectivaAccountMockMvc.perform(post("/api/junta-directiva-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(juntaDirectivaAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the JuntaDirectivaAccount in the database
        List<JuntaDirectivaAccount> juntaDirectivaAccountList = juntaDirectivaAccountRepository.findAll();
        assertThat(juntaDirectivaAccountList).hasSize(databaseSizeBeforeCreate + 1);
        JuntaDirectivaAccount testJuntaDirectivaAccount = juntaDirectivaAccountList.get(juntaDirectivaAccountList.size() - 1);
        assertThat(testJuntaDirectivaAccount.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createJuntaDirectivaAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = juntaDirectivaAccountRepository.findAll().size();

        // Create the JuntaDirectivaAccount with an existing ID
        juntaDirectivaAccount.setId(1L);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(juntaDirectivaAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJuntaDirectivaAccountMockMvc.perform(post("/api/junta-directiva-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(juntaDirectivaAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JuntaDirectivaAccount in the database
        List<JuntaDirectivaAccount> juntaDirectivaAccountList = juntaDirectivaAccountRepository.findAll();
        assertThat(juntaDirectivaAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = juntaDirectivaAccountRepository.findAll().size();
        // set the field null
        juntaDirectivaAccount.setEmail(null);

        // Create the JuntaDirectivaAccount, which fails.
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(juntaDirectivaAccount);

        restJuntaDirectivaAccountMockMvc.perform(post("/api/junta-directiva-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(juntaDirectivaAccountDTO)))
            .andExpect(status().isBadRequest());

        List<JuntaDirectivaAccount> juntaDirectivaAccountList = juntaDirectivaAccountRepository.findAll();
        assertThat(juntaDirectivaAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJuntaDirectivaAccounts() throws Exception {
        // Initialize the database
        juntaDirectivaAccountRepository.saveAndFlush(juntaDirectivaAccount);

        // Get all the juntaDirectivaAccountList
        restJuntaDirectivaAccountMockMvc.perform(get("/api/junta-directiva-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(juntaDirectivaAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getJuntaDirectivaAccount() throws Exception {
        // Initialize the database
        juntaDirectivaAccountRepository.saveAndFlush(juntaDirectivaAccount);

        // Get the juntaDirectivaAccount
        restJuntaDirectivaAccountMockMvc.perform(get("/api/junta-directiva-accounts/{id}", juntaDirectivaAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(juntaDirectivaAccount.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJuntaDirectivaAccount() throws Exception {
        // Get the juntaDirectivaAccount
        restJuntaDirectivaAccountMockMvc.perform(get("/api/junta-directiva-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJuntaDirectivaAccount() throws Exception {
        // Initialize the database
        juntaDirectivaAccountRepository.saveAndFlush(juntaDirectivaAccount);
        int databaseSizeBeforeUpdate = juntaDirectivaAccountRepository.findAll().size();

        // Update the juntaDirectivaAccount
        JuntaDirectivaAccount updatedJuntaDirectivaAccount = juntaDirectivaAccountRepository.findOne(juntaDirectivaAccount.getId());
        // Disconnect from session so that the updates on updatedJuntaDirectivaAccount are not directly saved in db
        em.detach(updatedJuntaDirectivaAccount);
        updatedJuntaDirectivaAccount
            .email(UPDATED_EMAIL);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(updatedJuntaDirectivaAccount);

        restJuntaDirectivaAccountMockMvc.perform(put("/api/junta-directiva-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(juntaDirectivaAccountDTO)))
            .andExpect(status().isOk());

        // Validate the JuntaDirectivaAccount in the database
        List<JuntaDirectivaAccount> juntaDirectivaAccountList = juntaDirectivaAccountRepository.findAll();
        assertThat(juntaDirectivaAccountList).hasSize(databaseSizeBeforeUpdate);
        JuntaDirectivaAccount testJuntaDirectivaAccount = juntaDirectivaAccountList.get(juntaDirectivaAccountList.size() - 1);
        assertThat(testJuntaDirectivaAccount.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingJuntaDirectivaAccount() throws Exception {
        int databaseSizeBeforeUpdate = juntaDirectivaAccountRepository.findAll().size();

        // Create the JuntaDirectivaAccount
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountMapper.toDto(juntaDirectivaAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJuntaDirectivaAccountMockMvc.perform(put("/api/junta-directiva-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(juntaDirectivaAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the JuntaDirectivaAccount in the database
        List<JuntaDirectivaAccount> juntaDirectivaAccountList = juntaDirectivaAccountRepository.findAll();
        assertThat(juntaDirectivaAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJuntaDirectivaAccount() throws Exception {
        // Initialize the database
        juntaDirectivaAccountRepository.saveAndFlush(juntaDirectivaAccount);
        int databaseSizeBeforeDelete = juntaDirectivaAccountRepository.findAll().size();

        // Get the juntaDirectivaAccount
        restJuntaDirectivaAccountMockMvc.perform(delete("/api/junta-directiva-accounts/{id}", juntaDirectivaAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JuntaDirectivaAccount> juntaDirectivaAccountList = juntaDirectivaAccountRepository.findAll();
        assertThat(juntaDirectivaAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JuntaDirectivaAccount.class);
        JuntaDirectivaAccount juntaDirectivaAccount1 = new JuntaDirectivaAccount();
        juntaDirectivaAccount1.setId(1L);
        JuntaDirectivaAccount juntaDirectivaAccount2 = new JuntaDirectivaAccount();
        juntaDirectivaAccount2.setId(juntaDirectivaAccount1.getId());
        assertThat(juntaDirectivaAccount1).isEqualTo(juntaDirectivaAccount2);
        juntaDirectivaAccount2.setId(2L);
        assertThat(juntaDirectivaAccount1).isNotEqualTo(juntaDirectivaAccount2);
        juntaDirectivaAccount1.setId(null);
        assertThat(juntaDirectivaAccount1).isNotEqualTo(juntaDirectivaAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JuntaDirectivaAccountDTO.class);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO1 = new JuntaDirectivaAccountDTO();
        juntaDirectivaAccountDTO1.setId(1L);
        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO2 = new JuntaDirectivaAccountDTO();
        assertThat(juntaDirectivaAccountDTO1).isNotEqualTo(juntaDirectivaAccountDTO2);
        juntaDirectivaAccountDTO2.setId(juntaDirectivaAccountDTO1.getId());
        assertThat(juntaDirectivaAccountDTO1).isEqualTo(juntaDirectivaAccountDTO2);
        juntaDirectivaAccountDTO2.setId(2L);
        assertThat(juntaDirectivaAccountDTO1).isNotEqualTo(juntaDirectivaAccountDTO2);
        juntaDirectivaAccountDTO1.setId(null);
        assertThat(juntaDirectivaAccountDTO1).isNotEqualTo(juntaDirectivaAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(juntaDirectivaAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(juntaDirectivaAccountMapper.fromId(null)).isNull();
    }
}
