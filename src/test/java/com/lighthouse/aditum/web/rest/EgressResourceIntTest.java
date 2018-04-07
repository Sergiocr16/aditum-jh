package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.EgressRepository;
import com.lighthouse.aditum.service.EgressService;
import com.lighthouse.aditum.service.dto.EgressDTO;
import com.lighthouse.aditum.service.mapper.EgressMapper;
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
 * Test class for the EgressResource REST controller.
 *
 * @see EgressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class EgressResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FOLIO = "AAAAAAAAAA";
    private static final String UPDATED_FOLIO = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_CONCEPT = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPT = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_PROVEEDOR = "AAAAAAAAAA";
    private static final String UPDATED_PROVEEDOR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PAYMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAYMENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_BILL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BILL_NUMBER = "BBBBBBBBBB";

    @Autowired
    private EgressRepository egressRepository;

    @Autowired
    private EgressMapper egressMapper;

    @Autowired
    private EgressService egressService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEgressMockMvc;

    private Egress egress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EgressResource egressResource = new EgressResource(egressService);
        this.restEgressMockMvc = MockMvcBuilders.standaloneSetup(egressResource)
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
    public static Egress createEntity(EntityManager em) {
        Egress egress = new Egress()
            .date(DEFAULT_DATE)
            .folio(DEFAULT_FOLIO)
            .account(DEFAULT_ACCOUNT)
            .category(DEFAULT_CATEGORY)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .concept(DEFAULT_CONCEPT)
            .total(DEFAULT_TOTAL)
            .reference(DEFAULT_REFERENCE)
            .comments(DEFAULT_COMMENTS)
            .proveedor(DEFAULT_PROVEEDOR)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .state(DEFAULT_STATE)
            .billNumber(DEFAULT_BILL_NUMBER);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        egress.setCompany(company);
        return egress;
    }

    @Before
    public void initTest() {
        egress = createEntity(em);
    }

    @Test
    @Transactional
    public void createEgress() throws Exception {
        int databaseSizeBeforeCreate = egressRepository.findAll().size();

        // Create the Egress
        EgressDTO egressDTO = egressMapper.toDto(egress);
        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isCreated());

        // Validate the Egress in the database
        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeCreate + 1);
        Egress testEgress = egressList.get(egressList.size() - 1);
        assertThat(testEgress.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEgress.getFolio()).isEqualTo(DEFAULT_FOLIO);
        assertThat(testEgress.getAccount()).isEqualTo(DEFAULT_ACCOUNT);
        assertThat(testEgress.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testEgress.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testEgress.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testEgress.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testEgress.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testEgress.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testEgress.getProveedor()).isEqualTo(DEFAULT_PROVEEDOR);
        assertThat(testEgress.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testEgress.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testEgress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testEgress.getBillNumber()).isEqualTo(DEFAULT_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void createEgressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = egressRepository.findAll().size();

        // Create the Egress with an existing ID
        egress.setId(1L);
        EgressDTO egressDTO = egressMapper.toDto(egress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressRepository.findAll().size();
        // set the field null
        egress.setDate(null);

        // Create the Egress, which fails.
        EgressDTO egressDTO = egressMapper.toDto(egress);

        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isBadRequest());

        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressRepository.findAll().size();
        // set the field null
        egress.setAccount(null);

        // Create the Egress, which fails.
        EgressDTO egressDTO = egressMapper.toDto(egress);

        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isBadRequest());

        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressRepository.findAll().size();
        // set the field null
        egress.setCategory(null);

        // Create the Egress, which fails.
        EgressDTO egressDTO = egressMapper.toDto(egress);

        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isBadRequest());

        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressRepository.findAll().size();
        // set the field null
        egress.setPaymentMethod(null);

        // Create the Egress, which fails.
        EgressDTO egressDTO = egressMapper.toDto(egress);

        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isBadRequest());

        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = egressRepository.findAll().size();
        // set the field null
        egress.setConcept(null);

        // Create the Egress, which fails.
        EgressDTO egressDTO = egressMapper.toDto(egress);

        restEgressMockMvc.perform(post("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isBadRequest());

        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEgresses() throws Exception {
        // Initialize the database
        egressRepository.saveAndFlush(egress);

        // Get all the egressList
        restEgressMockMvc.perform(get("/api/egresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egress.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].folio").value(hasItem(DEFAULT_FOLIO.toString())))
            .andExpect(jsonPath("$.[*].account").value(hasItem(DEFAULT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].proveedor").value(hasItem(DEFAULT_PROVEEDOR.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(sameInstant(DEFAULT_PAYMENT_DATE))))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].billNumber").value(hasItem(DEFAULT_BILL_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getEgress() throws Exception {
        // Initialize the database
        egressRepository.saveAndFlush(egress);

        // Get the egress
        restEgressMockMvc.perform(get("/api/egresses/{id}", egress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(egress.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.folio").value(DEFAULT_FOLIO.toString()))
            .andExpect(jsonPath("$.account").value(DEFAULT_ACCOUNT.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.proveedor").value(DEFAULT_PROVEEDOR.toString()))
            .andExpect(jsonPath("$.paymentDate").value(sameInstant(DEFAULT_PAYMENT_DATE)))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.billNumber").value(DEFAULT_BILL_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEgress() throws Exception {
        // Get the egress
        restEgressMockMvc.perform(get("/api/egresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEgress() throws Exception {
        // Initialize the database
        egressRepository.saveAndFlush(egress);
        int databaseSizeBeforeUpdate = egressRepository.findAll().size();

        // Update the egress
        Egress updatedEgress = egressRepository.findOne(egress.getId());
        updatedEgress
            .date(UPDATED_DATE)
            .folio(UPDATED_FOLIO)
            .account(UPDATED_ACCOUNT)
            .category(UPDATED_CATEGORY)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .concept(UPDATED_CONCEPT)
            .total(UPDATED_TOTAL)
            .reference(UPDATED_REFERENCE)
            .comments(UPDATED_COMMENTS)
            .proveedor(UPDATED_PROVEEDOR)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .state(UPDATED_STATE)
            .billNumber(UPDATED_BILL_NUMBER);
        EgressDTO egressDTO = egressMapper.toDto(updatedEgress);

        restEgressMockMvc.perform(put("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isOk());

        // Validate the Egress in the database
        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeUpdate);
        Egress testEgress = egressList.get(egressList.size() - 1);
        assertThat(testEgress.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEgress.getFolio()).isEqualTo(UPDATED_FOLIO);
        assertThat(testEgress.getAccount()).isEqualTo(UPDATED_ACCOUNT);
        assertThat(testEgress.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testEgress.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testEgress.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testEgress.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testEgress.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testEgress.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testEgress.getProveedor()).isEqualTo(UPDATED_PROVEEDOR);
        assertThat(testEgress.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testEgress.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testEgress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEgress.getBillNumber()).isEqualTo(UPDATED_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingEgress() throws Exception {
        int databaseSizeBeforeUpdate = egressRepository.findAll().size();

        // Create the Egress
        EgressDTO egressDTO = egressMapper.toDto(egress);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEgressMockMvc.perform(put("/api/egresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(egressDTO)))
            .andExpect(status().isCreated());

        // Validate the Egress in the database
        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEgress() throws Exception {
        // Initialize the database
        egressRepository.saveAndFlush(egress);
        int databaseSizeBeforeDelete = egressRepository.findAll().size();

        // Get the egress
        restEgressMockMvc.perform(delete("/api/egresses/{id}", egress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Egress> egressList = egressRepository.findAll();
        assertThat(egressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Egress.class);
        Egress egress1 = new Egress();
        egress1.setId(1L);
        Egress egress2 = new Egress();
        egress2.setId(egress1.getId());
        assertThat(egress1).isEqualTo(egress2);
        egress2.setId(2L);
        assertThat(egress1).isNotEqualTo(egress2);
        egress1.setId(null);
        assertThat(egress1).isNotEqualTo(egress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EgressDTO.class);
        EgressDTO egressDTO1 = new EgressDTO();
        egressDTO1.setId(1L);
        EgressDTO egressDTO2 = new EgressDTO();
        assertThat(egressDTO1).isNotEqualTo(egressDTO2);
        egressDTO2.setId(egressDTO1.getId());
        assertThat(egressDTO1).isEqualTo(egressDTO2);
        egressDTO2.setId(2L);
        assertThat(egressDTO1).isNotEqualTo(egressDTO2);
        egressDTO1.setId(null);
        assertThat(egressDTO1).isNotEqualTo(egressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(egressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(egressMapper.fromId(null)).isNull();
    }
}
