package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.MacroVisit;
import com.lighthouse.aditum.domain.MacroCondominium;
import com.lighthouse.aditum.repository.MacroVisitRepository;
import com.lighthouse.aditum.service.MacroVisitService;
import com.lighthouse.aditum.service.dto.MacroVisitDTO;
import com.lighthouse.aditum.service.mapper.MacroVisitMapper;
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
 * Test class for the MacroVisitResource REST controller.
 *
 * @see MacroVisitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class MacroVisitResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSEPLATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSEPLATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final ZonedDateTime DEFAULT_ARRIVALTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ARRIVALTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DEPARTURETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESTINY = "AAAAAAAAAA";
    private static final String UPDATED_DESTINY = "BBBBBBBBBB";

    @Autowired
    private MacroVisitRepository macroVisitRepository;

    @Autowired
    private MacroVisitMapper macroVisitMapper;

    @Autowired
    private MacroVisitService macroVisitService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMacroVisitMockMvc;

    private MacroVisit macroVisit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MacroVisitResource macroVisitResource = new MacroVisitResource(macroVisitService);
        this.restMacroVisitMockMvc = MockMvcBuilders.standaloneSetup(macroVisitResource)
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
    public static MacroVisit createEntity(EntityManager em) {
        MacroVisit macroVisit = new MacroVisit()
            .name(DEFAULT_NAME)
            .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
            .lastname(DEFAULT_LASTNAME)
            .secondlastname(DEFAULT_SECONDLASTNAME)
            .licenseplate(DEFAULT_LICENSEPLATE)
            .status(DEFAULT_STATUS)
            .arrivaltime(DEFAULT_ARRIVALTIME)
            .departuretime(DEFAULT_DEPARTURETIME)
            .destiny(DEFAULT_DESTINY);
        // Add required entity
        MacroCondominium macroCondominium = MacroCondominiumResourceIntTest.createEntity(em);
        em.persist(macroCondominium);
        em.flush();
        macroVisit.setMacroCondominium(macroCondominium);
        return macroVisit;
    }

    @Before
    public void initTest() {
        macroVisit = createEntity(em);
    }

    @Test
    @Transactional
    public void createMacroVisit() throws Exception {
        int databaseSizeBeforeCreate = macroVisitRepository.findAll().size();

        // Create the MacroVisit
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);
        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroVisit in the database
        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeCreate + 1);
        MacroVisit testMacroVisit = macroVisitList.get(macroVisitList.size() - 1);
        assertThat(testMacroVisit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMacroVisit.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testMacroVisit.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testMacroVisit.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testMacroVisit.getLicenseplate()).isEqualTo(DEFAULT_LICENSEPLATE);
        assertThat(testMacroVisit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMacroVisit.getArrivaltime()).isEqualTo(DEFAULT_ARRIVALTIME);
        assertThat(testMacroVisit.getDeparturetime()).isEqualTo(DEFAULT_DEPARTURETIME);
        assertThat(testMacroVisit.getDestiny()).isEqualTo(DEFAULT_DESTINY);
    }

    @Test
    @Transactional
    public void createMacroVisitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = macroVisitRepository.findAll().size();

        // Create the MacroVisit with an existing ID
        macroVisit.setId(1L);
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MacroVisit in the database
        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroVisitRepository.findAll().size();
        // set the field null
        macroVisit.setName(null);

        // Create the MacroVisit, which fails.
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentificationnumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroVisitRepository.findAll().size();
        // set the field null
        macroVisit.setIdentificationnumber(null);

        // Create the MacroVisit, which fails.
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroVisitRepository.findAll().size();
        // set the field null
        macroVisit.setLastname(null);

        // Create the MacroVisit, which fails.
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroVisitRepository.findAll().size();
        // set the field null
        macroVisit.setSecondlastname(null);

        // Create the MacroVisit, which fails.
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroVisitRepository.findAll().size();
        // set the field null
        macroVisit.setStatus(null);

        // Create the MacroVisit, which fails.
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkArrivaltimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroVisitRepository.findAll().size();
        // set the field null
        macroVisit.setArrivaltime(null);

        // Create the MacroVisit, which fails.
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        restMacroVisitMockMvc.perform(post("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isBadRequest());

        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMacroVisits() throws Exception {
        // Initialize the database
        macroVisitRepository.saveAndFlush(macroVisit);

        // Get all the macroVisitList
        restMacroVisitMockMvc.perform(get("/api/macro-visits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macroVisit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].licenseplate").value(hasItem(DEFAULT_LICENSEPLATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].arrivaltime").value(hasItem(sameInstant(DEFAULT_ARRIVALTIME))))
            .andExpect(jsonPath("$.[*].departuretime").value(hasItem(sameInstant(DEFAULT_DEPARTURETIME))))
            .andExpect(jsonPath("$.[*].destiny").value(hasItem(DEFAULT_DESTINY.toString())));
    }

    @Test
    @Transactional
    public void getMacroVisit() throws Exception {
        // Initialize the database
        macroVisitRepository.saveAndFlush(macroVisit);

        // Get the macroVisit
        restMacroVisitMockMvc.perform(get("/api/macro-visits/{id}", macroVisit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(macroVisit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.licenseplate").value(DEFAULT_LICENSEPLATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.arrivaltime").value(sameInstant(DEFAULT_ARRIVALTIME)))
            .andExpect(jsonPath("$.departuretime").value(sameInstant(DEFAULT_DEPARTURETIME)))
            .andExpect(jsonPath("$.destiny").value(DEFAULT_DESTINY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMacroVisit() throws Exception {
        // Get the macroVisit
        restMacroVisitMockMvc.perform(get("/api/macro-visits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacroVisit() throws Exception {
        // Initialize the database
        macroVisitRepository.saveAndFlush(macroVisit);
        int databaseSizeBeforeUpdate = macroVisitRepository.findAll().size();

        // Update the macroVisit
        MacroVisit updatedMacroVisit = macroVisitRepository.findOne(macroVisit.getId());
        // Disconnect from session so that the updates on updatedMacroVisit are not directly saved in db
        em.detach(updatedMacroVisit);
        updatedMacroVisit
            .name(UPDATED_NAME)
            .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
            .lastname(UPDATED_LASTNAME)
            .secondlastname(UPDATED_SECONDLASTNAME)
            .licenseplate(UPDATED_LICENSEPLATE)
            .status(UPDATED_STATUS)
            .arrivaltime(UPDATED_ARRIVALTIME)
            .departuretime(UPDATED_DEPARTURETIME)
            .destiny(UPDATED_DESTINY);
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(updatedMacroVisit);

        restMacroVisitMockMvc.perform(put("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isOk());

        // Validate the MacroVisit in the database
        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeUpdate);
        MacroVisit testMacroVisit = macroVisitList.get(macroVisitList.size() - 1);
        assertThat(testMacroVisit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMacroVisit.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testMacroVisit.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testMacroVisit.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testMacroVisit.getLicenseplate()).isEqualTo(UPDATED_LICENSEPLATE);
        assertThat(testMacroVisit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMacroVisit.getArrivaltime()).isEqualTo(UPDATED_ARRIVALTIME);
        assertThat(testMacroVisit.getDeparturetime()).isEqualTo(UPDATED_DEPARTURETIME);
        assertThat(testMacroVisit.getDestiny()).isEqualTo(UPDATED_DESTINY);
    }

    @Test
    @Transactional
    public void updateNonExistingMacroVisit() throws Exception {
        int databaseSizeBeforeUpdate = macroVisitRepository.findAll().size();

        // Create the MacroVisit
        MacroVisitDTO macroVisitDTO = macroVisitMapper.toDto(macroVisit);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMacroVisitMockMvc.perform(put("/api/macro-visits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroVisitDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroVisit in the database
        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMacroVisit() throws Exception {
        // Initialize the database
        macroVisitRepository.saveAndFlush(macroVisit);
        int databaseSizeBeforeDelete = macroVisitRepository.findAll().size();

        // Get the macroVisit
        restMacroVisitMockMvc.perform(delete("/api/macro-visits/{id}", macroVisit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MacroVisit> macroVisitList = macroVisitRepository.findAll();
        assertThat(macroVisitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroVisit.class);
        MacroVisit macroVisit1 = new MacroVisit();
        macroVisit1.setId(1L);
        MacroVisit macroVisit2 = new MacroVisit();
        macroVisit2.setId(macroVisit1.getId());
        assertThat(macroVisit1).isEqualTo(macroVisit2);
        macroVisit2.setId(2L);
        assertThat(macroVisit1).isNotEqualTo(macroVisit2);
        macroVisit1.setId(null);
        assertThat(macroVisit1).isNotEqualTo(macroVisit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroVisitDTO.class);
        MacroVisitDTO macroVisitDTO1 = new MacroVisitDTO();
        macroVisitDTO1.setId(1L);
        MacroVisitDTO macroVisitDTO2 = new MacroVisitDTO();
        assertThat(macroVisitDTO1).isNotEqualTo(macroVisitDTO2);
        macroVisitDTO2.setId(macroVisitDTO1.getId());
        assertThat(macroVisitDTO1).isEqualTo(macroVisitDTO2);
        macroVisitDTO2.setId(2L);
        assertThat(macroVisitDTO1).isNotEqualTo(macroVisitDTO2);
        macroVisitDTO1.setId(null);
        assertThat(macroVisitDTO1).isNotEqualTo(macroVisitDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(macroVisitMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(macroVisitMapper.fromId(null)).isNull();
    }
}
