package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.PaymentCharge;
import com.lighthouse.aditum.repository.PaymentChargeRepository;
import com.lighthouse.aditum.service.PaymentChargeService;
import com.lighthouse.aditum.service.dto.PaymentChargeDTO;
import com.lighthouse.aditum.service.mapper.PaymentChargeMapper;
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
 * Test class for the PaymentChargeResource REST controller.
 *
 * @see PaymentChargeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class PaymentChargeResourceIntTest {

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CONCEPT = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPT = "BBBBBBBBBB";

    private static final String DEFAULT_CONSECUTIVE = "AAAAAAAAAA";
    private static final String UPDATED_CONSECUTIVE = "BBBBBBBBBB";

    private static final Long DEFAULT_ORIGINAL_CHARGE = 1L;
    private static final Long UPDATED_ORIGINAL_CHARGE = 2L;

    private static final String DEFAULT_AMMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_LEFT_TO_PAY = "AAAAAAAAAA";
    private static final String UPDATED_LEFT_TO_PAY = "BBBBBBBBBB";

    private static final String DEFAULT_ABONADO = "AAAAAAAAAA";
    private static final String UPDATED_ABONADO = "BBBBBBBBBB";

    private static final Integer DEFAULT_OLD_STYLE = 1;
    private static final Integer UPDATED_OLD_STYLE = 2;

    @Autowired
    private PaymentChargeRepository paymentChargeRepository;

    @Autowired
    private PaymentChargeMapper paymentChargeMapper;

    @Autowired
    private PaymentChargeService paymentChargeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentChargeMockMvc;

    private PaymentCharge paymentCharge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentChargeResource paymentChargeResource = new PaymentChargeResource(paymentChargeService);
        this.restPaymentChargeMockMvc = MockMvcBuilders.standaloneSetup(paymentChargeResource)
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
    public static PaymentCharge createEntity(EntityManager em) {
        PaymentCharge paymentCharge = new PaymentCharge()
            .type(DEFAULT_TYPE)
            .date(DEFAULT_DATE)
            .concept(DEFAULT_CONCEPT)
            .consecutive(DEFAULT_CONSECUTIVE)
            .originalCharge(DEFAULT_ORIGINAL_CHARGE)
            .ammount(DEFAULT_AMMOUNT)
            .leftToPay(DEFAULT_LEFT_TO_PAY)
            .abonado(DEFAULT_ABONADO)
            .oldStyle(DEFAULT_OLD_STYLE);
        return paymentCharge;
    }

    @Before
    public void initTest() {
        paymentCharge = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentCharge() throws Exception {
        int databaseSizeBeforeCreate = paymentChargeRepository.findAll().size();

        // Create the PaymentCharge
        PaymentChargeDTO paymentChargeDTO = paymentChargeMapper.toDto(paymentCharge);
        restPaymentChargeMockMvc.perform(post("/api/payment-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentCharge in the database
        List<PaymentCharge> paymentChargeList = paymentChargeRepository.findAll();
        assertThat(paymentChargeList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentCharge testPaymentCharge = paymentChargeList.get(paymentChargeList.size() - 1);
        assertThat(testPaymentCharge.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPaymentCharge.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPaymentCharge.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testPaymentCharge.getConsecutive()).isEqualTo(DEFAULT_CONSECUTIVE);
        assertThat(testPaymentCharge.getOriginalCharge()).isEqualTo(DEFAULT_ORIGINAL_CHARGE);
        assertThat(testPaymentCharge.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testPaymentCharge.getLeftToPay()).isEqualTo(DEFAULT_LEFT_TO_PAY);
        assertThat(testPaymentCharge.getAbonado()).isEqualTo(DEFAULT_ABONADO);
        assertThat(testPaymentCharge.getOldStyle()).isEqualTo(DEFAULT_OLD_STYLE);
    }

    @Test
    @Transactional
    public void createPaymentChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentChargeRepository.findAll().size();

        // Create the PaymentCharge with an existing ID
        paymentCharge.setId(1L);
        PaymentChargeDTO paymentChargeDTO = paymentChargeMapper.toDto(paymentCharge);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentChargeMockMvc.perform(post("/api/payment-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentChargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCharge in the database
        List<PaymentCharge> paymentChargeList = paymentChargeRepository.findAll();
        assertThat(paymentChargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPaymentCharges() throws Exception {
        // Initialize the database
        paymentChargeRepository.saveAndFlush(paymentCharge);

        // Get all the paymentChargeList
        restPaymentChargeMockMvc.perform(get("/api/payment-charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
            .andExpect(jsonPath("$.[*].consecutive").value(hasItem(DEFAULT_CONSECUTIVE.toString())))
            .andExpect(jsonPath("$.[*].originalCharge").value(hasItem(DEFAULT_ORIGINAL_CHARGE.intValue())))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.toString())))
            .andExpect(jsonPath("$.[*].leftToPay").value(hasItem(DEFAULT_LEFT_TO_PAY.toString())))
            .andExpect(jsonPath("$.[*].abonado").value(hasItem(DEFAULT_ABONADO.toString())))
            .andExpect(jsonPath("$.[*].oldStyle").value(hasItem(DEFAULT_OLD_STYLE)));
    }

    @Test
    @Transactional
    public void getPaymentCharge() throws Exception {
        // Initialize the database
        paymentChargeRepository.saveAndFlush(paymentCharge);

        // Get the paymentCharge
        restPaymentChargeMockMvc.perform(get("/api/payment-charges/{id}", paymentCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentCharge.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.consecutive").value(DEFAULT_CONSECUTIVE.toString()))
            .andExpect(jsonPath("$.originalCharge").value(DEFAULT_ORIGINAL_CHARGE.intValue()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.toString()))
            .andExpect(jsonPath("$.leftToPay").value(DEFAULT_LEFT_TO_PAY.toString()))
            .andExpect(jsonPath("$.abonado").value(DEFAULT_ABONADO.toString()))
            .andExpect(jsonPath("$.oldStyle").value(DEFAULT_OLD_STYLE));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentCharge() throws Exception {
        // Get the paymentCharge
        restPaymentChargeMockMvc.perform(get("/api/payment-charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentCharge() throws Exception {
        // Initialize the database
        paymentChargeRepository.saveAndFlush(paymentCharge);
        int databaseSizeBeforeUpdate = paymentChargeRepository.findAll().size();

        // Update the paymentCharge
        PaymentCharge updatedPaymentCharge = paymentChargeRepository.findOne(paymentCharge.getId());
        // Disconnect from session so that the updates on updatedPaymentCharge are not directly saved in db
        em.detach(updatedPaymentCharge);
        updatedPaymentCharge
            .type(UPDATED_TYPE)
            .date(UPDATED_DATE)
            .concept(UPDATED_CONCEPT)
            .consecutive(UPDATED_CONSECUTIVE)
            .originalCharge(UPDATED_ORIGINAL_CHARGE)
            .ammount(UPDATED_AMMOUNT)
            .leftToPay(UPDATED_LEFT_TO_PAY)
            .abonado(UPDATED_ABONADO)
            .oldStyle(UPDATED_OLD_STYLE);
        PaymentChargeDTO paymentChargeDTO = paymentChargeMapper.toDto(updatedPaymentCharge);

        restPaymentChargeMockMvc.perform(put("/api/payment-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentChargeDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentCharge in the database
        List<PaymentCharge> paymentChargeList = paymentChargeRepository.findAll();
        assertThat(paymentChargeList).hasSize(databaseSizeBeforeUpdate);
        PaymentCharge testPaymentCharge = paymentChargeList.get(paymentChargeList.size() - 1);
        assertThat(testPaymentCharge.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPaymentCharge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPaymentCharge.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testPaymentCharge.getConsecutive()).isEqualTo(UPDATED_CONSECUTIVE);
        assertThat(testPaymentCharge.getOriginalCharge()).isEqualTo(UPDATED_ORIGINAL_CHARGE);
        assertThat(testPaymentCharge.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testPaymentCharge.getLeftToPay()).isEqualTo(UPDATED_LEFT_TO_PAY);
        assertThat(testPaymentCharge.getAbonado()).isEqualTo(UPDATED_ABONADO);
        assertThat(testPaymentCharge.getOldStyle()).isEqualTo(UPDATED_OLD_STYLE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentCharge() throws Exception {
        int databaseSizeBeforeUpdate = paymentChargeRepository.findAll().size();

        // Create the PaymentCharge
        PaymentChargeDTO paymentChargeDTO = paymentChargeMapper.toDto(paymentCharge);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentChargeMockMvc.perform(put("/api/payment-charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentChargeDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentCharge in the database
        List<PaymentCharge> paymentChargeList = paymentChargeRepository.findAll();
        assertThat(paymentChargeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentCharge() throws Exception {
        // Initialize the database
        paymentChargeRepository.saveAndFlush(paymentCharge);
        int databaseSizeBeforeDelete = paymentChargeRepository.findAll().size();

        // Get the paymentCharge
        restPaymentChargeMockMvc.perform(delete("/api/payment-charges/{id}", paymentCharge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentCharge> paymentChargeList = paymentChargeRepository.findAll();
        assertThat(paymentChargeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentCharge.class);
        PaymentCharge paymentCharge1 = new PaymentCharge();
        paymentCharge1.setId(1L);
        PaymentCharge paymentCharge2 = new PaymentCharge();
        paymentCharge2.setId(paymentCharge1.getId());
        assertThat(paymentCharge1).isEqualTo(paymentCharge2);
        paymentCharge2.setId(2L);
        assertThat(paymentCharge1).isNotEqualTo(paymentCharge2);
        paymentCharge1.setId(null);
        assertThat(paymentCharge1).isNotEqualTo(paymentCharge2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentChargeDTO.class);
//        PaymentChargeDTO paymentChargeDTO1 = new PaymentChargeDTO();
//        paymentChargeDTO1.setId(1L);
//        PaymentChargeDTO paymentChargeDTO2 = new PaymentChargeDTO();
//        assertThat(paymentChargeDTO1).isNotEqualTo(paymentChargeDTO2);
//        paymentChargeDTO2.setId(paymentChargeDTO1.getId());
//        assertThat(paymentChargeDTO1).isEqualTo(paymentChargeDTO2);
//        paymentChargeDTO2.setId(2L);
//        assertThat(paymentChargeDTO1).isNotEqualTo(paymentChargeDTO2);
//        paymentChargeDTO1.setId(null);
//        assertThat(paymentChargeDTO1).isNotEqualTo(paymentChargeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentChargeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentChargeMapper.fromId(null)).isNull();
    }
}
