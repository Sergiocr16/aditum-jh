package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.HistoricalDefaulterCharge;
import com.lighthouse.aditum.repository.HistoricalDefaulterChargeRepository;
import com.lighthouse.aditum.service.HistoricalDefaulterChargeService;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterChargeDTO;
import com.lighthouse.aditum.service.mapper.HistoricalDefaulterChargeMapper;
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
//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HistoricalDefaulterChargeResource REST controller.
 *
 * @see HistoricalDefaulterChargeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HistoricalDefaulterChargeResourceIntTest {

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONCEPT = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPT = "BBBBBBBBBB";

    private static final String DEFAULT_CONSECUTIVE = "AAAAAAAAAA";
    private static final String UPDATED_CONSECUTIVE = "BBBBBBBBBB";

    private static final String DEFAULT_AMMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_LEFT_TO_PAY = "AAAAAAAAAA";
    private static final String UPDATED_LEFT_TO_PAY = "BBBBBBBBBB";

    private static final String DEFAULT_ABONADO = "AAAAAAAAAA";
    private static final String UPDATED_ABONADO = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULTERS_DAY = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULTERS_DAY = "BBBBBBBBBB";

    @Autowired
    private HistoricalDefaulterChargeRepository historicalDefaulterChargeRepository;

    @Autowired
    private HistoricalDefaulterChargeMapper historicalDefaulterChargeMapper;

    @Autowired
    private HistoricalDefaulterChargeService historicalDefaulterChargeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoricalDefaulterChargeMockMvc;

    private HistoricalDefaulterCharge historicalDefaulterCharge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoricalDefaulterChargeResource historicalDefaulterChargeResource = new HistoricalDefaulterChargeResource(historicalDefaulterChargeService);
        this.restHistoricalDefaulterChargeMockMvc = MockMvcBuilders.standaloneSetup(historicalDefaulterChargeResource)
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
    public static HistoricalDefaulterCharge createEntity(EntityManager em) {
        HistoricalDefaulterCharge historicalDefaulterCharge = new HistoricalDefaulterCharge()
            .type(DEFAULT_TYPE)
            .date(DEFAULT_DATE)
            .concept(DEFAULT_CONCEPT)
            .consecutive(DEFAULT_CONSECUTIVE)
            .ammount(DEFAULT_AMMOUNT)
            .leftToPay(DEFAULT_LEFT_TO_PAY)
            .abonado(DEFAULT_ABONADO)
            .defaultersDay(DEFAULT_DEFAULTERS_DAY);
        return historicalDefaulterCharge;
    }

    @Before
    public void initTest() {
        historicalDefaulterCharge = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoricalDefaulterCharge() throws Exception {
        int databaseSizeBeforeCreate = historicalDefaulterChargeRepository.findAll().size();

        // Create the HistoricalDefaulterCharge
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO = historicalDefaulterChargeMapper.toDto(historicalDefaulterCharge);
        restHistoricalDefaulterChargeMockMvc.perform(post("/api/historical-defaulter-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalDefaulterCharge in the database
        List<HistoricalDefaulterCharge> historicalDefaulterChargeList = historicalDefaulterChargeRepository.findAll();
        assertThat(historicalDefaulterChargeList).hasSize(databaseSizeBeforeCreate + 1);
        HistoricalDefaulterCharge testHistoricalDefaulterCharge = historicalDefaulterChargeList.get(historicalDefaulterChargeList.size() - 1);
        assertThat(testHistoricalDefaulterCharge.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHistoricalDefaulterCharge.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testHistoricalDefaulterCharge.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testHistoricalDefaulterCharge.getConsecutive()).isEqualTo(DEFAULT_CONSECUTIVE);
        assertThat(testHistoricalDefaulterCharge.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testHistoricalDefaulterCharge.getLeftToPay()).isEqualTo(DEFAULT_LEFT_TO_PAY);
        assertThat(testHistoricalDefaulterCharge.getAbonado()).isEqualTo(DEFAULT_ABONADO);
        assertThat(testHistoricalDefaulterCharge.getDefaultersDay()).isEqualTo(DEFAULT_DEFAULTERS_DAY);
    }

    @Test
    @Transactional
    public void createHistoricalDefaulterChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historicalDefaulterChargeRepository.findAll().size();

        // Create the HistoricalDefaulterCharge with an existing ID
        historicalDefaulterCharge.setId(1L);
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO = historicalDefaulterChargeMapper.toDto(historicalDefaulterCharge);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricalDefaulterChargeMockMvc.perform(post("/api/historical-defaulter-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterChargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoricalDefaulterCharge in the database
        List<HistoricalDefaulterCharge> historicalDefaulterChargeList = historicalDefaulterChargeRepository.findAll();
        assertThat(historicalDefaulterChargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHistoricalDefaulterCharges() throws Exception {
        // Initialize the database
        historicalDefaulterChargeRepository.saveAndFlush(historicalDefaulterCharge);

        // Get all the historicalDefaulterChargeList
        restHistoricalDefaulterChargeMockMvc.perform(get("/api/historical-defaulter-charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicalDefaulterCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
            .andExpect(jsonPath("$.[*].consecutive").value(hasItem(DEFAULT_CONSECUTIVE.toString())))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.toString())))
            .andExpect(jsonPath("$.[*].leftToPay").value(hasItem(DEFAULT_LEFT_TO_PAY.toString())))
            .andExpect(jsonPath("$.[*].abonado").value(hasItem(DEFAULT_ABONADO.toString())))
            .andExpect(jsonPath("$.[*].defaultersDay").value(hasItem(DEFAULT_DEFAULTERS_DAY.toString())));
    }

    @Test
    @Transactional
    public void getHistoricalDefaulterCharge() throws Exception {
        // Initialize the database
        historicalDefaulterChargeRepository.saveAndFlush(historicalDefaulterCharge);

        // Get the historicalDefaulterCharge
        restHistoricalDefaulterChargeMockMvc.perform(get("/api/historical-defaulter-charges/{id}", historicalDefaulterCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historicalDefaulterCharge.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.consecutive").value(DEFAULT_CONSECUTIVE.toString()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.toString()))
            .andExpect(jsonPath("$.leftToPay").value(DEFAULT_LEFT_TO_PAY.toString()))
            .andExpect(jsonPath("$.abonado").value(DEFAULT_ABONADO.toString()))
            .andExpect(jsonPath("$.defaultersDay").value(DEFAULT_DEFAULTERS_DAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHistoricalDefaulterCharge() throws Exception {
        // Get the historicalDefaulterCharge
        restHistoricalDefaulterChargeMockMvc.perform(get("/api/historical-defaulter-charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoricalDefaulterCharge() throws Exception {
        // Initialize the database
        historicalDefaulterChargeRepository.saveAndFlush(historicalDefaulterCharge);
        int databaseSizeBeforeUpdate = historicalDefaulterChargeRepository.findAll().size();

        // Update the historicalDefaulterCharge
        HistoricalDefaulterCharge updatedHistoricalDefaulterCharge = historicalDefaulterChargeRepository.findOne(historicalDefaulterCharge.getId());
        // Disconnect from session so that the updates on updatedHistoricalDefaulterCharge are not directly saved in db
        em.detach(updatedHistoricalDefaulterCharge);
        updatedHistoricalDefaulterCharge
            .type(UPDATED_TYPE)
            .date(UPDATED_DATE)
            .concept(UPDATED_CONCEPT)
            .consecutive(UPDATED_CONSECUTIVE)
            .ammount(UPDATED_AMMOUNT)
            .leftToPay(UPDATED_LEFT_TO_PAY)
            .abonado(UPDATED_ABONADO)
            .defaultersDay(UPDATED_DEFAULTERS_DAY);
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO = historicalDefaulterChargeMapper.toDto(updatedHistoricalDefaulterCharge);

        restHistoricalDefaulterChargeMockMvc.perform(put("/api/historical-defaulter-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterChargeDTO)))
            .andExpect(status().isOk());

        // Validate the HistoricalDefaulterCharge in the database
        List<HistoricalDefaulterCharge> historicalDefaulterChargeList = historicalDefaulterChargeRepository.findAll();
        assertThat(historicalDefaulterChargeList).hasSize(databaseSizeBeforeUpdate);
        HistoricalDefaulterCharge testHistoricalDefaulterCharge = historicalDefaulterChargeList.get(historicalDefaulterChargeList.size() - 1);
        assertThat(testHistoricalDefaulterCharge.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHistoricalDefaulterCharge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHistoricalDefaulterCharge.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testHistoricalDefaulterCharge.getConsecutive()).isEqualTo(UPDATED_CONSECUTIVE);
        assertThat(testHistoricalDefaulterCharge.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testHistoricalDefaulterCharge.getLeftToPay()).isEqualTo(UPDATED_LEFT_TO_PAY);
        assertThat(testHistoricalDefaulterCharge.getAbonado()).isEqualTo(UPDATED_ABONADO);
        assertThat(testHistoricalDefaulterCharge.getDefaultersDay()).isEqualTo(UPDATED_DEFAULTERS_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoricalDefaulterCharge() throws Exception {
        int databaseSizeBeforeUpdate = historicalDefaulterChargeRepository.findAll().size();

        // Create the HistoricalDefaulterCharge
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO = historicalDefaulterChargeMapper.toDto(historicalDefaulterCharge);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoricalDefaulterChargeMockMvc.perform(put("/api/historical-defaulter-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalDefaulterCharge in the database
        List<HistoricalDefaulterCharge> historicalDefaulterChargeList = historicalDefaulterChargeRepository.findAll();
        assertThat(historicalDefaulterChargeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHistoricalDefaulterCharge() throws Exception {
        // Initialize the database
        historicalDefaulterChargeRepository.saveAndFlush(historicalDefaulterCharge);
        int databaseSizeBeforeDelete = historicalDefaulterChargeRepository.findAll().size();

        // Get the historicalDefaulterCharge
        restHistoricalDefaulterChargeMockMvc.perform(delete("/api/historical-defaulter-charges/{id}", historicalDefaulterCharge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HistoricalDefaulterCharge> historicalDefaulterChargeList = historicalDefaulterChargeRepository.findAll();
        assertThat(historicalDefaulterChargeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalDefaulterCharge.class);
        HistoricalDefaulterCharge historicalDefaulterCharge1 = new HistoricalDefaulterCharge();
        historicalDefaulterCharge1.setId(1L);
        HistoricalDefaulterCharge historicalDefaulterCharge2 = new HistoricalDefaulterCharge();
        historicalDefaulterCharge2.setId(historicalDefaulterCharge1.getId());
        assertThat(historicalDefaulterCharge1).isEqualTo(historicalDefaulterCharge2);
        historicalDefaulterCharge2.setId(2L);
        assertThat(historicalDefaulterCharge1).isNotEqualTo(historicalDefaulterCharge2);
        historicalDefaulterCharge1.setId(null);
        assertThat(historicalDefaulterCharge1).isNotEqualTo(historicalDefaulterCharge2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalDefaulterChargeDTO.class);
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO1 = new HistoricalDefaulterChargeDTO();
        historicalDefaulterChargeDTO1.setId(1L);
        HistoricalDefaulterChargeDTO historicalDefaulterChargeDTO2 = new HistoricalDefaulterChargeDTO();
        assertThat(historicalDefaulterChargeDTO1).isNotEqualTo(historicalDefaulterChargeDTO2);
        historicalDefaulterChargeDTO2.setId(historicalDefaulterChargeDTO1.getId());
        assertThat(historicalDefaulterChargeDTO1).isEqualTo(historicalDefaulterChargeDTO2);
        historicalDefaulterChargeDTO2.setId(2L);
        assertThat(historicalDefaulterChargeDTO1).isNotEqualTo(historicalDefaulterChargeDTO2);
        historicalDefaulterChargeDTO1.setId(null);
        assertThat(historicalDefaulterChargeDTO1).isNotEqualTo(historicalDefaulterChargeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historicalDefaulterChargeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historicalDefaulterChargeMapper.fromId(null)).isNull();
    }
}
