package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.PaymentProof;
import com.lighthouse.aditum.repository.PaymentProofRepository;
import com.lighthouse.aditum.service.PaymentProofService;
import com.lighthouse.aditum.service.dto.PaymentProofDTO;
import com.lighthouse.aditum.service.mapper.PaymentProofMapper;
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
 * Test class for the PaymentProofResource REST controller.
 *
 * @see PaymentProofResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class PaymentProofResourceIntTest {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REGISTER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REGISTER_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_BANK = "AAAAAAAAAA";
    private static final String UPDATED_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    @Autowired
    private PaymentProofRepository paymentProofRepository;

    @Autowired
    private PaymentProofMapper paymentProofMapper;

    @Autowired
    private PaymentProofService paymentProofService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentProofMockMvc;

    private PaymentProof paymentProof;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentProofResource paymentProofResource = new PaymentProofResource(paymentProofService);
        this.restPaymentProofMockMvc = MockMvcBuilders.standaloneSetup(paymentProofResource)
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
    public static PaymentProof createEntity(EntityManager em) {
        PaymentProof paymentProof = new PaymentProof()
            .imageUrl(DEFAULT_IMAGE_URL)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .subject(DEFAULT_SUBJECT)
            .registerDate(DEFAULT_REGISTER_DATE)
            .bank(DEFAULT_BANK)
            .reference(DEFAULT_REFERENCE);
        return paymentProof;
    }

    @Before
    public void initTest() {
        paymentProof = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentProof() throws Exception {
        int databaseSizeBeforeCreate = paymentProofRepository.findAll().size();

        // Create the PaymentProof
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(paymentProof);
        restPaymentProofMockMvc.perform(post("/api/payment-proofs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentProofDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentProof in the database
        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentProof testPaymentProof = paymentProofList.get(paymentProofList.size() - 1);
        assertThat(testPaymentProof.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testPaymentProof.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPaymentProof.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentProof.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testPaymentProof.getRegisterDate()).isEqualTo(DEFAULT_REGISTER_DATE);
        assertThat(testPaymentProof.getBank()).isEqualTo(DEFAULT_BANK);
        assertThat(testPaymentProof.getReference()).isEqualTo(DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    public void createPaymentProofWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentProofRepository.findAll().size();

        // Create the PaymentProof with an existing ID
        paymentProof.setId(1L);
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(paymentProof);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentProofMockMvc.perform(post("/api/payment-proofs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentProofDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentProof in the database
        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProofRepository.findAll().size();
        // set the field null
        paymentProof.setSubject(null);

        // Create the PaymentProof, which fails.
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(paymentProof);

        restPaymentProofMockMvc.perform(post("/api/payment-proofs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentProofDTO)))
            .andExpect(status().isBadRequest());

        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegisterDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProofRepository.findAll().size();
        // set the field null
        paymentProof.setRegisterDate(null);

        // Create the PaymentProof, which fails.
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(paymentProof);

        restPaymentProofMockMvc.perform(post("/api/payment-proofs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentProofDTO)))
            .andExpect(status().isBadRequest());

        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentProofs() throws Exception {
        // Initialize the database
        paymentProofRepository.saveAndFlush(paymentProof);

        // Get all the paymentProofList
        restPaymentProofMockMvc.perform(get("/api/payment-proofs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentProof.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].registerDate").value(hasItem(sameInstant(DEFAULT_REGISTER_DATE))))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())));
    }

    @Test
    @Transactional
    public void getPaymentProof() throws Exception {
        // Initialize the database
        paymentProofRepository.saveAndFlush(paymentProof);

        // Get the paymentProof
        restPaymentProofMockMvc.perform(get("/api/payment-proofs/{id}", paymentProof.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentProof.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.registerDate").value(sameInstant(DEFAULT_REGISTER_DATE)))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentProof() throws Exception {
        // Get the paymentProof
        restPaymentProofMockMvc.perform(get("/api/payment-proofs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentProof() throws Exception {
        // Initialize the database
        paymentProofRepository.saveAndFlush(paymentProof);
        int databaseSizeBeforeUpdate = paymentProofRepository.findAll().size();

        // Update the paymentProof
        PaymentProof updatedPaymentProof = paymentProofRepository.findOne(paymentProof.getId());
        // Disconnect from session so that the updates on updatedPaymentProof are not directly saved in db
        em.detach(updatedPaymentProof);
        updatedPaymentProof
            .imageUrl(UPDATED_IMAGE_URL)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .subject(UPDATED_SUBJECT)
            .registerDate(UPDATED_REGISTER_DATE)
            .bank(UPDATED_BANK)
            .reference(UPDATED_REFERENCE);
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(updatedPaymentProof);

        restPaymentProofMockMvc.perform(put("/api/payment-proofs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentProofDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentProof in the database
        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeUpdate);
        PaymentProof testPaymentProof = paymentProofList.get(paymentProofList.size() - 1);
        assertThat(testPaymentProof.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testPaymentProof.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPaymentProof.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentProof.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testPaymentProof.getRegisterDate()).isEqualTo(UPDATED_REGISTER_DATE);
        assertThat(testPaymentProof.getBank()).isEqualTo(UPDATED_BANK);
        assertThat(testPaymentProof.getReference()).isEqualTo(UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentProof() throws Exception {
        int databaseSizeBeforeUpdate = paymentProofRepository.findAll().size();

        // Create the PaymentProof
        PaymentProofDTO paymentProofDTO = paymentProofMapper.toDto(paymentProof);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentProofMockMvc.perform(put("/api/payment-proofs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentProofDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentProof in the database
        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentProof() throws Exception {
        // Initialize the database
        paymentProofRepository.saveAndFlush(paymentProof);
        int databaseSizeBeforeDelete = paymentProofRepository.findAll().size();

        // Get the paymentProof
        restPaymentProofMockMvc.perform(delete("/api/payment-proofs/{id}", paymentProof.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentProof> paymentProofList = paymentProofRepository.findAll();
        assertThat(paymentProofList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentProof.class);
        PaymentProof paymentProof1 = new PaymentProof();
        paymentProof1.setId(1L);
        PaymentProof paymentProof2 = new PaymentProof();
        paymentProof2.setId(paymentProof1.getId());
        assertThat(paymentProof1).isEqualTo(paymentProof2);
        paymentProof2.setId(2L);
        assertThat(paymentProof1).isNotEqualTo(paymentProof2);
        paymentProof1.setId(null);
        assertThat(paymentProof1).isNotEqualTo(paymentProof2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentProofDTO.class);
        PaymentProofDTO paymentProofDTO1 = new PaymentProofDTO();
        paymentProofDTO1.setId(1L);
        PaymentProofDTO paymentProofDTO2 = new PaymentProofDTO();
        assertThat(paymentProofDTO1).isNotEqualTo(paymentProofDTO2);
        paymentProofDTO2.setId(paymentProofDTO1.getId());
        assertThat(paymentProofDTO1).isEqualTo(paymentProofDTO2);
        paymentProofDTO2.setId(2L);
        assertThat(paymentProofDTO1).isNotEqualTo(paymentProofDTO2);
        paymentProofDTO1.setId(null);
        assertThat(paymentProofDTO1).isNotEqualTo(paymentProofDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentProofMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentProofMapper.fromId(null)).isNull();
    }
}
