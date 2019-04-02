package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.config.BugsnagConfig;
import com.lighthouse.aditum.domain.Visitant;
import com.lighthouse.aditum.repository.VisitantRepository;
import com.lighthouse.aditum.service.VisitantDocumentService;
import com.lighthouse.aditum.service.VisitantService;
import com.lighthouse.aditum.service.dto.VisitantDTO;
import com.lighthouse.aditum.service.mapper.VisitantMapper;
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
 * Test class for the VisitantResource REST controller.
 *
 * @see VisitantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class VisitantResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ARRIVALTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ARRIVALTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_INVITATIONSTARINGTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INVITATIONSTARINGTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_INVITATIONLIMITTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INVITATIONLIMITTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LICENSEPLATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSEPLATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ISINVITED = 0;
    private static final Integer UPDATED_ISINVITED = 1;

    private static final String DEFAULT_RESPONSABLEOFFICER = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLEOFFICER = "BBBBBBBBBB";

    @Autowired
    private VisitantRepository visitantRepository;

    @Autowired
    private VisitantMapper visitantMapper;

    @Autowired
    private VisitantService visitantService;

    @Autowired
    private BugsnagConfig bugsnagConfig;
    @Autowired
    private VisitantDocumentService visitantDocumentService;
    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVisitantMockMvc;

    private Visitant visitant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VisitantResource visitantResource = new VisitantResource(visitantService,visitantDocumentService,bugsnagConfig);
        this.restVisitantMockMvc = MockMvcBuilders.standaloneSetup(visitantResource)
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
    public static Visitant createEntity(EntityManager em) {
        Visitant visitant = new Visitant()
                .name(DEFAULT_NAME)
                .lastname(DEFAULT_LASTNAME)
                .secondlastname(DEFAULT_SECONDLASTNAME)
                .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
                .arrivaltime(DEFAULT_ARRIVALTIME)
                .invitationstaringtime(DEFAULT_INVITATIONSTARINGTIME)
                .invitationlimittime(DEFAULT_INVITATIONLIMITTIME)
                .licenseplate(DEFAULT_LICENSEPLATE)
                .isinvited(DEFAULT_ISINVITED)
                .responsableofficer(DEFAULT_RESPONSABLEOFFICER);
        return visitant;
    }

    @Before
    public void initTest() {
        visitant = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisitant() throws Exception {
        int databaseSizeBeforeCreate = visitantRepository.findAll().size();

        // Create the Visitant
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isCreated());

        // Validate the Visitant in the database
        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeCreate + 1);
        Visitant testVisitant = visitantList.get(visitantList.size() - 1);
        assertThat(testVisitant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVisitant.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testVisitant.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testVisitant.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testVisitant.getArrivaltime()).isEqualTo(DEFAULT_ARRIVALTIME);
        assertThat(testVisitant.getInvitationstaringtime()).isEqualTo(DEFAULT_INVITATIONSTARINGTIME);
        assertThat(testVisitant.getInvitationlimittime()).isEqualTo(DEFAULT_INVITATIONLIMITTIME);
        assertThat(testVisitant.getLicenseplate()).isEqualTo(DEFAULT_LICENSEPLATE);
        assertThat(testVisitant.getIsinvited()).isEqualTo(DEFAULT_ISINVITED);
        assertThat(testVisitant.getResponsableofficer()).isEqualTo(DEFAULT_RESPONSABLEOFFICER);
    }

    @Test
    @Transactional
    public void createVisitantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitantRepository.findAll().size();

        // Create the Visitant with an existing ID
        Visitant existingVisitant = new Visitant();
        existingVisitant.setId(1L);
        VisitantDTO existingVisitantDTO = visitantMapper.visitantToVisitantDTO(existingVisitant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingVisitantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantRepository.findAll().size();
        // set the field null
        visitant.setName(null);

        // Create the Visitant, which fails.
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isBadRequest());

        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantRepository.findAll().size();
        // set the field null
        visitant.setLastname(null);

        // Create the Visitant, which fails.
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isBadRequest());

        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantRepository.findAll().size();
        // set the field null
        visitant.setSecondlastname(null);

        // Create the Visitant, which fails.
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isBadRequest());

        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentificationnumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantRepository.findAll().size();
        // set the field null
        visitant.setIdentificationnumber(null);

        // Create the Visitant, which fails.
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isBadRequest());

        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsinvitedIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantRepository.findAll().size();
        // set the field null
        visitant.setIsinvited(null);

        // Create the Visitant, which fails.
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        restVisitantMockMvc.perform(post("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isBadRequest());

        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVisitants() throws Exception {
        // Initialize the database
        visitantRepository.saveAndFlush(visitant);

        // Get all the visitantList
        restVisitantMockMvc.perform(get("/api/visitants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].arrivaltime").value(hasItem(sameInstant(DEFAULT_ARRIVALTIME))))
            .andExpect(jsonPath("$.[*].invitationstaringtime").value(hasItem(sameInstant(DEFAULT_INVITATIONSTARINGTIME))))
            .andExpect(jsonPath("$.[*].invitationlimittime").value(hasItem(sameInstant(DEFAULT_INVITATIONLIMITTIME))))
            .andExpect(jsonPath("$.[*].licenseplate").value(hasItem(DEFAULT_LICENSEPLATE.toString())))
            .andExpect(jsonPath("$.[*].isinvited").value(hasItem(DEFAULT_ISINVITED)))
            .andExpect(jsonPath("$.[*].responsableofficer").value(hasItem(DEFAULT_RESPONSABLEOFFICER.toString())));
    }

    @Test
    @Transactional
    public void getVisitant() throws Exception {
        // Initialize the database
        visitantRepository.saveAndFlush(visitant);

        // Get the visitant
        restVisitantMockMvc.perform(get("/api/visitants/{id}", visitant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(visitant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.arrivaltime").value(sameInstant(DEFAULT_ARRIVALTIME)))
            .andExpect(jsonPath("$.invitationstaringtime").value(sameInstant(DEFAULT_INVITATIONSTARINGTIME)))
            .andExpect(jsonPath("$.invitationlimittime").value(sameInstant(DEFAULT_INVITATIONLIMITTIME)))
            .andExpect(jsonPath("$.licenseplate").value(DEFAULT_LICENSEPLATE.toString()))
            .andExpect(jsonPath("$.isinvited").value(DEFAULT_ISINVITED))
            .andExpect(jsonPath("$.responsableofficer").value(DEFAULT_RESPONSABLEOFFICER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVisitant() throws Exception {
        // Get the visitant
        restVisitantMockMvc.perform(get("/api/visitants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisitant() throws Exception {
        // Initialize the database
        visitantRepository.saveAndFlush(visitant);
        int databaseSizeBeforeUpdate = visitantRepository.findAll().size();

        // Update the visitant
        Visitant updatedVisitant = visitantRepository.findOne(visitant.getId());
        updatedVisitant
                .name(UPDATED_NAME)
                .lastname(UPDATED_LASTNAME)
                .secondlastname(UPDATED_SECONDLASTNAME)
                .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
                .arrivaltime(UPDATED_ARRIVALTIME)
                .invitationstaringtime(UPDATED_INVITATIONSTARINGTIME)
                .invitationlimittime(UPDATED_INVITATIONLIMITTIME)
                .licenseplate(UPDATED_LICENSEPLATE)
                .isinvited(UPDATED_ISINVITED)
                .responsableofficer(UPDATED_RESPONSABLEOFFICER);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(updatedVisitant);

        restVisitantMockMvc.perform(put("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isOk());

        // Validate the Visitant in the database
        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeUpdate);
        Visitant testVisitant = visitantList.get(visitantList.size() - 1);
        assertThat(testVisitant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVisitant.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testVisitant.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testVisitant.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testVisitant.getArrivaltime()).isEqualTo(UPDATED_ARRIVALTIME);
        assertThat(testVisitant.getInvitationstaringtime()).isEqualTo(UPDATED_INVITATIONSTARINGTIME);
        assertThat(testVisitant.getInvitationlimittime()).isEqualTo(UPDATED_INVITATIONLIMITTIME);
        assertThat(testVisitant.getLicenseplate()).isEqualTo(UPDATED_LICENSEPLATE);
        assertThat(testVisitant.getIsinvited()).isEqualTo(UPDATED_ISINVITED);
        assertThat(testVisitant.getResponsableofficer()).isEqualTo(UPDATED_RESPONSABLEOFFICER);
    }

    @Test
    @Transactional
    public void updateNonExistingVisitant() throws Exception {
        int databaseSizeBeforeUpdate = visitantRepository.findAll().size();

        // Create the Visitant
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVisitantMockMvc.perform(put("/api/visitants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantDTO)))
            .andExpect(status().isCreated());

        // Validate the Visitant in the database
        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVisitant() throws Exception {
        // Initialize the database
        visitantRepository.saveAndFlush(visitant);
        int databaseSizeBeforeDelete = visitantRepository.findAll().size();

        // Get the visitant
        restVisitantMockMvc.perform(delete("/api/visitants/{id}", visitant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Visitant> visitantList = visitantRepository.findAll();
        assertThat(visitantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visitant.class);
    }
}
