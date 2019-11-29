package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.HistoricalCharge;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.HistoricalChargeRepository;
import com.lighthouse.aditum.service.HistoricalChargeService;
import com.lighthouse.aditum.service.dto.HistoricalChargeDTO;
import com.lighthouse.aditum.service.mapper.HistoricalChargeMapper;
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
 * Test class for the HistoricalChargeResource REST controller.
 *
 * @see HistoricalChargeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HistoricalChargeResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONCEPT = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPT = "BBBBBBBBBB";

    private static final String DEFAULT_AMMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_ABONO = "AAAAAAAAAA";
    private static final String UPDATED_ABONO = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    @Autowired
    private HistoricalChargeRepository historicalChargeRepository;

    @Autowired
    private HistoricalChargeMapper historicalChargeMapper;

    @Autowired
    private HistoricalChargeService historicalChargeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoricalChargeMockMvc;

    private HistoricalCharge historicalCharge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HistoricalChargeResource historicalChargeResource = new HistoricalChargeResource(historicalChargeService);
        this.restHistoricalChargeMockMvc = MockMvcBuilders.standaloneSetup(historicalChargeResource)
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
    public static HistoricalCharge createEntity(EntityManager em) {
        HistoricalCharge historicalCharge = new HistoricalCharge()
            .date(DEFAULT_DATE)
            .concept(DEFAULT_CONCEPT)
            .ammount(DEFAULT_AMMOUNT)
            .abono(DEFAULT_ABONO)
            .type(DEFAULT_TYPE)
            .state(DEFAULT_STATE)
            .deleted(DEFAULT_DELETED)
            .total(DEFAULT_TOTAL);
        // Add required entity
        House house = HouseResourceIntTest.createEntity(em);
        em.persist(house);
        em.flush();
        historicalCharge.setHouse(house);
        return historicalCharge;
    }

    @Before
    public void initTest() {
        historicalCharge = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoricalCharge() throws Exception {
        int databaseSizeBeforeCreate = historicalChargeRepository.findAll().size();

        // Create the HistoricalCharge
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);
        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalCharge in the database
        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeCreate + 1);
        HistoricalCharge testHistoricalCharge = historicalChargeList.get(historicalChargeList.size() - 1);
        assertThat(testHistoricalCharge.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testHistoricalCharge.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testHistoricalCharge.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testHistoricalCharge.getAbono()).isEqualTo(DEFAULT_ABONO);
        assertThat(testHistoricalCharge.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHistoricalCharge.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testHistoricalCharge.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testHistoricalCharge.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void createHistoricalChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historicalChargeRepository.findAll().size();

        // Create the HistoricalCharge with an existing ID
        historicalCharge.setId(1L);
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicalChargeRepository.findAll().size();
        // set the field null
        historicalCharge.setDate(null);

        // Create the HistoricalCharge, which fails.
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicalChargeRepository.findAll().size();
        // set the field null
        historicalCharge.setConcept(null);

        // Create the HistoricalCharge, which fails.
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicalChargeRepository.findAll().size();
        // set the field null
        historicalCharge.setAmmount(null);

        // Create the HistoricalCharge, which fails.
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicalChargeRepository.findAll().size();
        // set the field null
        historicalCharge.setType(null);

        // Create the HistoricalCharge, which fails.
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicalChargeRepository.findAll().size();
        // set the field null
        historicalCharge.setState(null);

        // Create the HistoricalCharge, which fails.
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = historicalChargeRepository.findAll().size();
        // set the field null
        historicalCharge.setDeleted(null);

        // Create the HistoricalCharge, which fails.
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        restHistoricalChargeMockMvc.perform(post("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isBadRequest());

        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHistoricalCharges() throws Exception {
        // Initialize the database
        historicalChargeRepository.saveAndFlush(historicalCharge);

        // Get all the historicalChargeList
        restHistoricalChargeMockMvc.perform(get("/api/historical-charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicalCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.toString())))
            .andExpect(jsonPath("$.[*].abono").value(hasItem(DEFAULT_ABONO.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.toString())));
    }

    @Test
    @Transactional
    public void getHistoricalCharge() throws Exception {
        // Initialize the database
        historicalChargeRepository.saveAndFlush(historicalCharge);

        // Get the historicalCharge
        restHistoricalChargeMockMvc.perform(get("/api/historical-charges/{id}", historicalCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historicalCharge.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.toString()))
            .andExpect(jsonPath("$.abono").value(DEFAULT_ABONO.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHistoricalCharge() throws Exception {
        // Get the historicalCharge
        restHistoricalChargeMockMvc.perform(get("/api/historical-charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoricalCharge() throws Exception {
        // Initialize the database
        historicalChargeRepository.saveAndFlush(historicalCharge);
        int databaseSizeBeforeUpdate = historicalChargeRepository.findAll().size();

        // Update the historicalCharge
        HistoricalCharge updatedHistoricalCharge = historicalChargeRepository.findOne(historicalCharge.getId());
        updatedHistoricalCharge
            .date(UPDATED_DATE)
            .concept(UPDATED_CONCEPT)
            .ammount(UPDATED_AMMOUNT)
            .abono(UPDATED_ABONO)
            .type(UPDATED_TYPE)
            .state(UPDATED_STATE)
            .deleted(UPDATED_DELETED)
            .total(UPDATED_TOTAL);
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(updatedHistoricalCharge);

        restHistoricalChargeMockMvc.perform(put("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isOk());

        // Validate the HistoricalCharge in the database
        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeUpdate);
        HistoricalCharge testHistoricalCharge = historicalChargeList.get(historicalChargeList.size() - 1);
        assertThat(testHistoricalCharge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHistoricalCharge.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testHistoricalCharge.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testHistoricalCharge.getAbono()).isEqualTo(UPDATED_ABONO);
        assertThat(testHistoricalCharge.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHistoricalCharge.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testHistoricalCharge.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testHistoricalCharge.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoricalCharge() throws Exception {
        int databaseSizeBeforeUpdate = historicalChargeRepository.findAll().size();

        // Create the HistoricalCharge
        HistoricalChargeDTO historicalChargeDTO = historicalChargeMapper.toDto(historicalCharge);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoricalChargeMockMvc.perform(put("/api/historical-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalCharge in the database
        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHistoricalCharge() throws Exception {
        // Initialize the database
        historicalChargeRepository.saveAndFlush(historicalCharge);
        int databaseSizeBeforeDelete = historicalChargeRepository.findAll().size();

        // Get the historicalCharge
        restHistoricalChargeMockMvc.perform(delete("/api/historical-charges/{id}", historicalCharge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HistoricalCharge> historicalChargeList = historicalChargeRepository.findAll();
        assertThat(historicalChargeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalCharge.class);
        HistoricalCharge historicalCharge1 = new HistoricalCharge();
        historicalCharge1.setId(1L);
        HistoricalCharge historicalCharge2 = new HistoricalCharge();
        historicalCharge2.setId(historicalCharge1.getId());
        assertThat(historicalCharge1).isEqualTo(historicalCharge2);
        historicalCharge2.setId(2L);
        assertThat(historicalCharge1).isNotEqualTo(historicalCharge2);
        historicalCharge1.setId(null);
        assertThat(historicalCharge1).isNotEqualTo(historicalCharge2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalChargeDTO.class);
        HistoricalChargeDTO historicalChargeDTO1 = new HistoricalChargeDTO();
        historicalChargeDTO1.setId(1L);
        HistoricalChargeDTO historicalChargeDTO2 = new HistoricalChargeDTO();
        assertThat(historicalChargeDTO1).isNotEqualTo(historicalChargeDTO2);
        historicalChargeDTO2.setId(historicalChargeDTO1.getId());
        assertThat(historicalChargeDTO1).isEqualTo(historicalChargeDTO2);
        historicalChargeDTO2.setId(2L);
        assertThat(historicalChargeDTO1).isNotEqualTo(historicalChargeDTO2);
        historicalChargeDTO1.setId(null);
        assertThat(historicalChargeDTO1).isNotEqualTo(historicalChargeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historicalChargeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historicalChargeMapper.fromId(null)).isNull();
    }
}
