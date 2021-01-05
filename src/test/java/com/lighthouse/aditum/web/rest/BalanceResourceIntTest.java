package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Balance;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.BalanceRepository;
import com.lighthouse.aditum.service.BalanceService;
import com.lighthouse.aditum.service.dto.BalanceDTO;
import com.lighthouse.aditum.service.mapper.BalanceMapper;
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
 * Test class for the BalanceResource REST controller.
 *
 * @see BalanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class BalanceResourceIntTest {

    private static final String DEFAULT_EXTRAORDINARY = "AAAAAAAAAA";
    private static final String UPDATED_EXTRAORDINARY = "BBBBBBBBBB";

    private static final String DEFAULT_COMMON_AREAS = "AAAAAAAAAA";
    private static final String UPDATED_COMMON_AREAS = "BBBBBBBBBB";

    private static final String DEFAULT_MAINTENANCE = "AAAAAAAAAA";
    private static final String UPDATED_MAINTENANCE = "BBBBBBBBBB";

    private static final String DEFAULT_WATER_CHARGE = "AAAAAAAAAA";
    private static final String UPDATED_WATER_CHARGE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHERS = "AAAAAAAAAA";
    private static final String UPDATED_OTHERS = "BBBBBBBBBB";

    private static final String DEFAULT_MULTA = "AAAAAAAAAA";
    private static final String UPDATED_MULTA = "BBBBBBBBBB";

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBalanceMockMvc;

    private Balance balance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        final BalanceResource balanceResource = new BalanceResource(balanceService);
//        this.restBalanceMockMvc = MockMvcBuilders.standaloneSetup(balanceResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
//            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balance createEntity(EntityManager em) {
        Balance balance = new Balance()
            .extraordinary(DEFAULT_EXTRAORDINARY)
            .commonAreas(DEFAULT_COMMON_AREAS)
            .maintenance(DEFAULT_MAINTENANCE)
            .waterCharge(DEFAULT_WATER_CHARGE)
            .others(DEFAULT_OTHERS)
            .multa(DEFAULT_MULTA);
        // Add required entity
        House house = HouseResourceIntTest.createEntity(em);
        em.persist(house);
        em.flush();
        balance.setHouse(house);
        return balance;
    }

    @Before
    public void initTest() {
        balance = createEntity(em);
    }

    @Test
    @Transactional
    public void createBalance() throws Exception {
        int databaseSizeBeforeCreate = balanceRepository.findAll().size();

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);
        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate + 1);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getExtraordinary()).isEqualTo(DEFAULT_EXTRAORDINARY);
        assertThat(testBalance.getCommonAreas()).isEqualTo(DEFAULT_COMMON_AREAS);
        assertThat(testBalance.getMaintenance()).isEqualTo(DEFAULT_MAINTENANCE);
        assertThat(testBalance.getWaterCharge()).isEqualTo(DEFAULT_WATER_CHARGE);
        assertThat(testBalance.getOthers()).isEqualTo(DEFAULT_OTHERS);
        assertThat(testBalance.getMulta()).isEqualTo(DEFAULT_MULTA);
    }

    @Test
    @Transactional
    public void createBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = balanceRepository.findAll().size();

        // Create the Balance with an existing ID
        balance.setId(1L);
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkExtraordinaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setExtraordinary(null);

        // Create the Balance, which fails.
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommonAreasIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setCommonAreas(null);

        // Create the Balance, which fails.
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaintenanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = balanceRepository.findAll().size();
        // set the field null
        balance.setMaintenance(null);

        // Create the Balance, which fails.
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        restBalanceMockMvc.perform(post("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isBadRequest());

        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBalances() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get all the balanceList
        restBalanceMockMvc.perform(get("/api/balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(balance.getId().intValue())))
            .andExpect(jsonPath("$.[*].extraordinary").value(hasItem(DEFAULT_EXTRAORDINARY.toString())))
            .andExpect(jsonPath("$.[*].commonAreas").value(hasItem(DEFAULT_COMMON_AREAS.toString())))
            .andExpect(jsonPath("$.[*].maintenance").value(hasItem(DEFAULT_MAINTENANCE.toString())))
            .andExpect(jsonPath("$.[*].waterCharge").value(hasItem(DEFAULT_WATER_CHARGE.toString())))
            .andExpect(jsonPath("$.[*].others").value(hasItem(DEFAULT_OTHERS.toString())))
            .andExpect(jsonPath("$.[*].multa").value(hasItem(DEFAULT_MULTA.toString())));
    }

    @Test
    @Transactional
    public void getBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);

        // Get the balance
        restBalanceMockMvc.perform(get("/api/balances/{id}", balance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(balance.getId().intValue()))
            .andExpect(jsonPath("$.extraordinary").value(DEFAULT_EXTRAORDINARY.toString()))
            .andExpect(jsonPath("$.commonAreas").value(DEFAULT_COMMON_AREAS.toString()))
            .andExpect(jsonPath("$.maintenance").value(DEFAULT_MAINTENANCE.toString()))
            .andExpect(jsonPath("$.waterCharge").value(DEFAULT_WATER_CHARGE.toString()))
            .andExpect(jsonPath("$.others").value(DEFAULT_OTHERS.toString()))
            .andExpect(jsonPath("$.multa").value(DEFAULT_MULTA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBalance() throws Exception {
        // Get the balance
        restBalanceMockMvc.perform(get("/api/balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Update the balance
        Balance updatedBalance = balanceRepository.findOne(balance.getId());
        // Disconnect from session so that the updates on updatedBalance are not directly saved in db
        em.detach(updatedBalance);
        updatedBalance
            .extraordinary(UPDATED_EXTRAORDINARY)
            .commonAreas(UPDATED_COMMON_AREAS)
            .maintenance(UPDATED_MAINTENANCE)
            .waterCharge(UPDATED_WATER_CHARGE)
            .others(UPDATED_OTHERS)
            .multa(UPDATED_MULTA);
        BalanceDTO balanceDTO = balanceMapper.toDto(updatedBalance);

        restBalanceMockMvc.perform(put("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isOk());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate);
        Balance testBalance = balanceList.get(balanceList.size() - 1);
        assertThat(testBalance.getExtraordinary()).isEqualTo(UPDATED_EXTRAORDINARY);
        assertThat(testBalance.getCommonAreas()).isEqualTo(UPDATED_COMMON_AREAS);
        assertThat(testBalance.getMaintenance()).isEqualTo(UPDATED_MAINTENANCE);
        assertThat(testBalance.getWaterCharge()).isEqualTo(UPDATED_WATER_CHARGE);
        assertThat(testBalance.getOthers()).isEqualTo(UPDATED_OTHERS);
        assertThat(testBalance.getMulta()).isEqualTo(UPDATED_MULTA);
    }

    @Test
    @Transactional
    public void updateNonExistingBalance() throws Exception {
        int databaseSizeBeforeUpdate = balanceRepository.findAll().size();

        // Create the Balance
        BalanceDTO balanceDTO = balanceMapper.toDto(balance);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBalanceMockMvc.perform(put("/api/balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(balanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Balance in the database
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBalance() throws Exception {
        // Initialize the database
        balanceRepository.saveAndFlush(balance);
        int databaseSizeBeforeDelete = balanceRepository.findAll().size();

        // Get the balance
        restBalanceMockMvc.perform(delete("/api/balances/{id}", balance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Balance> balanceList = balanceRepository.findAll();
        assertThat(balanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Balance.class);
        Balance balance1 = new Balance();
        balance1.setId(1L);
        Balance balance2 = new Balance();
        balance2.setId(balance1.getId());
        assertThat(balance1).isEqualTo(balance2);
        balance2.setId(2L);
        assertThat(balance1).isNotEqualTo(balance2);
        balance1.setId(null);
        assertThat(balance1).isNotEqualTo(balance2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BalanceDTO.class);
        BalanceDTO balanceDTO1 = new BalanceDTO();
        balanceDTO1.setId(1L);
        BalanceDTO balanceDTO2 = new BalanceDTO();
        assertThat(balanceDTO1).isNotEqualTo(balanceDTO2);
        balanceDTO2.setId(balanceDTO1.getId());
        assertThat(balanceDTO1).isEqualTo(balanceDTO2);
        balanceDTO2.setId(2L);
        assertThat(balanceDTO1).isNotEqualTo(balanceDTO2);
        balanceDTO1.setId(null);
        assertThat(balanceDTO1).isNotEqualTo(balanceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(balanceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(balanceMapper.fromId(null)).isNull();
    }
}
