package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.PaymentRepository;
import com.lighthouse.aditum.service.PaymentService;
import com.lighthouse.aditum.service.dto.PaymentDTO;
import com.lighthouse.aditum.service.mapper.PaymentMapper;
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
 * Test class for the PaymentResource REST controller.
 *
 * @see PaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class PaymentResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_RECEIPT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_AMMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_CONCEPT = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPT = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMPANY_ID = 1;
    private static final Integer UPDATED_COMPANY_ID = 2;

    private static final String DEFAULT_AMMOUNT_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT_LEFT = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_EXCHANGE_RATE = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_AMMOUNT_DOLLAR = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT_DOLLAR = "BBBBBBBBBB";

    private static final String DEFAULT_AMMOUNT_LEFT_DOLLAR = "AAAAAAAAAA";
    private static final String UPDATED_AMMOUNT_LEFT_DOLLAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOUBLE_MONEY = 1;
    private static final Integer UPDATED_DOUBLE_MONEY = 2;

    private static final Integer DEFAULT_FAVOR_TYPE_BALANCE = 1;
    private static final Integer UPDATED_FAVOR_TYPE_BALANCE = 2;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentMockMvc;

    private Payment payment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        final PaymentResource paymentResource = new PaymentResource(paymentService);
//        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource)
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
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .date(DEFAULT_DATE)
            .receiptNumber(DEFAULT_RECEIPT_NUMBER)
            .transaction(DEFAULT_TRANSACTION)
            .account(DEFAULT_ACCOUNT)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .comments(DEFAULT_COMMENTS)
            .ammount(DEFAULT_AMMOUNT)
            .concept(DEFAULT_CONCEPT)
            .companyId(DEFAULT_COMPANY_ID);
//            .ammountLeft(DEFAULT_AMMOUNT_LEFT)
//            .documentReference(DEFAULT_DOCUMENT_REFERENCE)
//            .exchangeRate(DEFAULT_EXCHANGE_RATE)
//            .ammountDollar(DEFAULT_AMMOUNT_DOLLAR)
//            .ammountLeftDollar(DEFAULT_AMMOUNT_LEFT_DOLLAR)
//            .doubleMoney(DEFAULT_DOUBLE_MONEY)
//            .favorTypeBalance(DEFAULT_FAVOR_TYPE_BALANCE);
        return payment;
    }

    @Before
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPayment.getReceiptNumber()).isEqualTo(DEFAULT_RECEIPT_NUMBER);
        assertThat(testPayment.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testPayment.getAccount()).isEqualTo(DEFAULT_ACCOUNT);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPayment.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testPayment.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testPayment.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testPayment.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testPayment.getAmmountLeft()).isEqualTo(DEFAULT_AMMOUNT_LEFT);
        assertThat(testPayment.getDocumentReference()).isEqualTo(DEFAULT_DOCUMENT_REFERENCE);
        assertThat(testPayment.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
        assertThat(testPayment.getAmmountDollar()).isEqualTo(DEFAULT_AMMOUNT_DOLLAR);
        assertThat(testPayment.getAmmountLeftDollar()).isEqualTo(DEFAULT_AMMOUNT_LEFT_DOLLAR);
        assertThat(testPayment.getDoubleMoney()).isEqualTo(DEFAULT_DOUBLE_MONEY);
        assertThat(testPayment.getFavorTypeBalance()).isEqualTo(DEFAULT_FAVOR_TYPE_BALANCE);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setDate(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceiptNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setReceiptNumber(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setTransaction(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setAccount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setPaymentMethod(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].receiptNumber").value(hasItem(DEFAULT_RECEIPT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.toString())))
            .andExpect(jsonPath("$.[*].account").value(hasItem(DEFAULT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.toString())))
            .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].ammountLeft").value(hasItem(DEFAULT_AMMOUNT_LEFT.toString())))
            .andExpect(jsonPath("$.[*].documentReference").value(hasItem(DEFAULT_DOCUMENT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].exchangeRate").value(hasItem(DEFAULT_EXCHANGE_RATE.toString())))
            .andExpect(jsonPath("$.[*].ammountDollar").value(hasItem(DEFAULT_AMMOUNT_DOLLAR.toString())))
            .andExpect(jsonPath("$.[*].ammountLeftDollar").value(hasItem(DEFAULT_AMMOUNT_LEFT_DOLLAR.toString())))
            .andExpect(jsonPath("$.[*].doubleMoney").value(hasItem(DEFAULT_DOUBLE_MONEY)))
            .andExpect(jsonPath("$.[*].favorTypeBalance").value(hasItem(DEFAULT_FAVOR_TYPE_BALANCE)));
    }

    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.receiptNumber").value(DEFAULT_RECEIPT_NUMBER.toString()))
            .andExpect(jsonPath("$.transaction").value(DEFAULT_TRANSACTION.toString()))
            .andExpect(jsonPath("$.account").value(DEFAULT_ACCOUNT.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.toString()))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID))
            .andExpect(jsonPath("$.ammountLeft").value(DEFAULT_AMMOUNT_LEFT.toString()))
            .andExpect(jsonPath("$.documentReference").value(DEFAULT_DOCUMENT_REFERENCE.toString()))
            .andExpect(jsonPath("$.exchangeRate").value(DEFAULT_EXCHANGE_RATE.toString()))
            .andExpect(jsonPath("$.ammountDollar").value(DEFAULT_AMMOUNT_DOLLAR.toString()))
            .andExpect(jsonPath("$.ammountLeftDollar").value(DEFAULT_AMMOUNT_LEFT_DOLLAR.toString()))
            .andExpect(jsonPath("$.doubleMoney").value(DEFAULT_DOUBLE_MONEY))
            .andExpect(jsonPath("$.favorTypeBalance").value(DEFAULT_FAVOR_TYPE_BALANCE));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findOne(payment.getId());
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .date(UPDATED_DATE)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .transaction(UPDATED_TRANSACTION)
            .account(UPDATED_ACCOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .comments(UPDATED_COMMENTS)
            .ammount(UPDATED_AMMOUNT)
            .concept(UPDATED_CONCEPT)
            .companyId(UPDATED_COMPANY_ID)
            .ammountLeft(UPDATED_AMMOUNT_LEFT);
//            .documentReference(UPDATED_DOCUMENT_REFERENCE)
//            .exchangeRate(UPDATED_EXCHANGE_RATE)
//            .ammountDollar(UPDATED_AMMOUNT_DOLLAR)
//            .ammountLeftDollar(UPDATED_AMMOUNT_LEFT_DOLLAR)
//            .doubleMoney(UPDATED_DOUBLE_MONEY)
//            .favorTypeBalance(UPDATED_FAVOR_TYPE_BALANCE);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPayment.getReceiptNumber()).isEqualTo(UPDATED_RECEIPT_NUMBER);
        assertThat(testPayment.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testPayment.getAccount()).isEqualTo(UPDATED_ACCOUNT);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayment.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testPayment.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testPayment.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testPayment.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testPayment.getAmmountLeft()).isEqualTo(UPDATED_AMMOUNT_LEFT);
        assertThat(testPayment.getDocumentReference()).isEqualTo(UPDATED_DOCUMENT_REFERENCE);
        assertThat(testPayment.getExchangeRate()).isEqualTo(UPDATED_EXCHANGE_RATE);
        assertThat(testPayment.getAmmountDollar()).isEqualTo(UPDATED_AMMOUNT_DOLLAR);
        assertThat(testPayment.getAmmountLeftDollar()).isEqualTo(UPDATED_AMMOUNT_LEFT_DOLLAR);
        assertThat(testPayment.getDoubleMoney()).isEqualTo(UPDATED_DOUBLE_MONEY);
        assertThat(testPayment.getFavorTypeBalance()).isEqualTo(UPDATED_FAVOR_TYPE_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Get the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = new Payment();
        payment1.setId(1L);
        Payment payment2 = new Payment();
        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);
        payment2.setId(2L);
        assertThat(payment1).isNotEqualTo(payment2);
        payment1.setId(null);
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.setId(1L);
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.setId(paymentDTO1.getId());
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.setId(2L);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.setId(null);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentMapper.fromId(null)).isNull();
    }
}
