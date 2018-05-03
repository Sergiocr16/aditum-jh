package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.BalanceByAccount;
import com.lighthouse.aditum.repository.BalanceByAccountRepository;
import com.lighthouse.aditum.service.BalanceByAccountService;
import com.lighthouse.aditum.service.dto.BalanceByAccountDTO;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BalanceByAccountResource REST controller.
 *
 * @see BalanceByAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class BalanceByAccountResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_ACCOUNT_ID = 1L;
    private static final Long UPDATED_ACCOUNT_ID = 2L;

    private static final Integer DEFAULT_BALANCE = 1;
    private static final Integer UPDATED_BALANCE = 2;

    @Autowired
    private BalanceByAccountRepository balanceByAccountRepository;

    @Autowired
    private BalanceByAccountMapper balanceByAccountMapper;

    @Autowired
    private BalanceByAccountService balanceByAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBalanceByAccountMockMvc;

    private BalanceByAccount balanceByAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BalanceByAccountResource balanceByAccountResource = new BalanceByAccountResource(balanceByAccountService);
        this.restBalanceByAccountMockMvc = MockMvcBuilders.standaloneSetup(balanceByAccountResource)
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
    public static BalanceByAccount createEntity(EntityManager em) {
        BalanceByAccount balanceByAccount = new BalanceByAccount()
            .date(DEFAULT_DATE)
            .accountId(DEFAULT_ACCOUNT_ID)
            .balance(DEFAULT_BALANCE);
        return balanceByAccount;
    }

    @Before
    public void initTest() {
        balanceByAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createBalanceByAccount() throws Exception {
        int databaseSizeBeforeCreate = balanceByAccountRepository.findAll().size();

        // Create the BalanceByAccount
        BalanceByAccountDTO balanceByAccountDTO = balanceByAccountMapper.toDto(balanceByAccount);
        restBalanceByAccountMockMvc.perform(post("/api/balance-by-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceByAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the BalanceByAccount in the database
        List<BalanceByAccount> balanceByAccountList = balanceByAccountRepository.findAll();
        assertThat(balanceByAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BalanceByAccount testBalanceByAccount = balanceByAccountList.get(balanceByAccountList.size() - 1);
        assertThat(testBalanceByAccount.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBalanceByAccount.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testBalanceByAccount.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createBalanceByAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = balanceByAccountRepository.findAll().size();

        // Create the BalanceByAccount with an existing ID
        balanceByAccount.setId(1L);
        BalanceByAccountDTO balanceByAccountDTO = balanceByAccountMapper.toDto(balanceByAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBalanceByAccountMockMvc.perform(post("/api/balance-by-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceByAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BalanceByAccount> balanceByAccountList = balanceByAccountRepository.findAll();
        assertThat(balanceByAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBalanceByAccounts() throws Exception {
        // Initialize the database
        balanceByAccountRepository.saveAndFlush(balanceByAccount);

        // Get all the balanceByAccountList
        restBalanceByAccountMockMvc.perform(get("/api/balance-by-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balanceByAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE)));
    }

    @Test
    @Transactional
    public void getBalanceByAccount() throws Exception {
        // Initialize the database
        balanceByAccountRepository.saveAndFlush(balanceByAccount);

        // Get the balanceByAccount
        restBalanceByAccountMockMvc.perform(get("/api/balance-by-accounts/{id}", balanceByAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(balanceByAccount.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE));
    }

    @Test
    @Transactional
    public void getNonExistingBalanceByAccount() throws Exception {
        // Get the balanceByAccount
        restBalanceByAccountMockMvc.perform(get("/api/balance-by-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBalanceByAccount() throws Exception {
        // Initialize the database
        balanceByAccountRepository.saveAndFlush(balanceByAccount);
        int databaseSizeBeforeUpdate = balanceByAccountRepository.findAll().size();

        // Update the balanceByAccount
        BalanceByAccount updatedBalanceByAccount = balanceByAccountRepository.findOne(balanceByAccount.getId());
        updatedBalanceByAccount
            .date(UPDATED_DATE)
            .accountId(UPDATED_ACCOUNT_ID)
            .balance(UPDATED_BALANCE);
        BalanceByAccountDTO balanceByAccountDTO = balanceByAccountMapper.toDto(updatedBalanceByAccount);

        restBalanceByAccountMockMvc.perform(put("/api/balance-by-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceByAccountDTO)))
            .andExpect(status().isOk());

        // Validate the BalanceByAccount in the database
        List<BalanceByAccount> balanceByAccountList = balanceByAccountRepository.findAll();
        assertThat(balanceByAccountList).hasSize(databaseSizeBeforeUpdate);
        BalanceByAccount testBalanceByAccount = balanceByAccountList.get(balanceByAccountList.size() - 1);
        assertThat(testBalanceByAccount.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBalanceByAccount.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testBalanceByAccount.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingBalanceByAccount() throws Exception {
        int databaseSizeBeforeUpdate = balanceByAccountRepository.findAll().size();

        // Create the BalanceByAccount
        BalanceByAccountDTO balanceByAccountDTO = balanceByAccountMapper.toDto(balanceByAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBalanceByAccountMockMvc.perform(put("/api/balance-by-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceByAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the BalanceByAccount in the database
        List<BalanceByAccount> balanceByAccountList = balanceByAccountRepository.findAll();
        assertThat(balanceByAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBalanceByAccount() throws Exception {
        // Initialize the database
        balanceByAccountRepository.saveAndFlush(balanceByAccount);
        int databaseSizeBeforeDelete = balanceByAccountRepository.findAll().size();

        // Get the balanceByAccount
        restBalanceByAccountMockMvc.perform(delete("/api/balance-by-accounts/{id}", balanceByAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BalanceByAccount> balanceByAccountList = balanceByAccountRepository.findAll();
        assertThat(balanceByAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalanceByAccount.class);
        BalanceByAccount balanceByAccount1 = new BalanceByAccount();
        balanceByAccount1.setId(1L);
        BalanceByAccount balanceByAccount2 = new BalanceByAccount();
        balanceByAccount2.setId(balanceByAccount1.getId());
        assertThat(balanceByAccount1).isEqualTo(balanceByAccount2);
        balanceByAccount2.setId(2L);
        assertThat(balanceByAccount1).isNotEqualTo(balanceByAccount2);
        balanceByAccount1.setId(null);
        assertThat(balanceByAccount1).isNotEqualTo(balanceByAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalanceByAccountDTO.class);
        BalanceByAccountDTO balanceByAccountDTO1 = new BalanceByAccountDTO();
        balanceByAccountDTO1.setId(1L);
        BalanceByAccountDTO balanceByAccountDTO2 = new BalanceByAccountDTO();
        assertThat(balanceByAccountDTO1).isNotEqualTo(balanceByAccountDTO2);
        balanceByAccountDTO2.setId(balanceByAccountDTO1.getId());
        assertThat(balanceByAccountDTO1).isEqualTo(balanceByAccountDTO2);
        balanceByAccountDTO2.setId(2L);
        assertThat(balanceByAccountDTO1).isNotEqualTo(balanceByAccountDTO2);
        balanceByAccountDTO1.setId(null);
        assertThat(balanceByAccountDTO1).isNotEqualTo(balanceByAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(balanceByAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(balanceByAccountMapper.fromId(null)).isNull();
    }
}
