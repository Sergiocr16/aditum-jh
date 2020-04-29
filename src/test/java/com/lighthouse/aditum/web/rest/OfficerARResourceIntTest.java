package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.OfficerAR;
import com.lighthouse.aditum.repository.OfficerARRepository;
import com.lighthouse.aditum.service.OfficerARService;
import com.lighthouse.aditum.service.dto.OfficerARDTO;
import com.lighthouse.aditum.service.mapper.OfficerARMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OfficerARResource REST controller.
 *
 * @see OfficerARResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class OfficerARResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IN_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_IN_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ANNOS_EXPERIENCIA = "AAAAAAAAAA";
    private static final String UPDATED_ANNOS_EXPERIENCIA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTION = "BBBBBBBBBB";

    private static final String DEFAULT_PLATE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PLATE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADITIONAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_ADITIONAL_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private OfficerARRepository officerARRepository;

    @Autowired
    private OfficerARMapper officerARMapper;

    @Autowired
    private OfficerARService officerARService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfficerARMockMvc;

    private OfficerAR officerAR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OfficerARResource officerARResource = new OfficerARResource(officerARService);
        this.restOfficerARMockMvc = MockMvcBuilders.standaloneSetup(officerARResource)
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
    public static OfficerAR createEntity(EntityManager em) {
        OfficerAR officerAR = new OfficerAR()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .secondLastName(DEFAULT_SECOND_LAST_NAME)
            .identificationNumber(DEFAULT_IDENTIFICATION_NUMBER)
            .inService(DEFAULT_IN_SERVICE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .annosExperiencia(DEFAULT_ANNOS_EXPERIENCIA)
            .birthDate(DEFAULT_BIRTH_DATE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .direction(DEFAULT_DIRECTION)
            .plateNumber(DEFAULT_PLATE_NUMBER)
            .aditionalNotes(DEFAULT_ADITIONAL_NOTES)
            .enabled(DEFAULT_ENABLED)
            .deleted(DEFAULT_DELETED)
            .email(DEFAULT_EMAIL);
        return officerAR;
    }

    @Before
    public void initTest() {
        officerAR = createEntity(em);
    }

    @Test
    @Transactional
    public void createOfficerAR() throws Exception {
        int databaseSizeBeforeCreate = officerARRepository.findAll().size();

        // Create the OfficerAR
        OfficerARDTO officerARDTO = officerARMapper.toDto(officerAR);
        restOfficerARMockMvc.perform(post("/api/officer-ars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerARDTO)))
            .andExpect(status().isCreated());

        // Validate the OfficerAR in the database
        List<OfficerAR> officerARList = officerARRepository.findAll();
        assertThat(officerARList).hasSize(databaseSizeBeforeCreate + 1);
        OfficerAR testOfficerAR = officerARList.get(officerARList.size() - 1);
        assertThat(testOfficerAR.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOfficerAR.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testOfficerAR.getSecondLastName()).isEqualTo(DEFAULT_SECOND_LAST_NAME);
        assertThat(testOfficerAR.getIdentificationNumber()).isEqualTo(DEFAULT_IDENTIFICATION_NUMBER);
        assertThat(testOfficerAR.getInService()).isEqualTo(DEFAULT_IN_SERVICE);
        assertThat(testOfficerAR.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testOfficerAR.getAnnosExperiencia()).isEqualTo(DEFAULT_ANNOS_EXPERIENCIA);
        assertThat(testOfficerAR.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testOfficerAR.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOfficerAR.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testOfficerAR.getPlateNumber()).isEqualTo(DEFAULT_PLATE_NUMBER);
        assertThat(testOfficerAR.getAditionalNotes()).isEqualTo(DEFAULT_ADITIONAL_NOTES);
        assertThat(testOfficerAR.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testOfficerAR.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testOfficerAR.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createOfficerARWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = officerARRepository.findAll().size();

        // Create the OfficerAR with an existing ID
        officerAR.setId(1L);
        OfficerARDTO officerARDTO = officerARMapper.toDto(officerAR);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficerARMockMvc.perform(post("/api/officer-ars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerARDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OfficerAR in the database
        List<OfficerAR> officerARList = officerARRepository.findAll();
        assertThat(officerARList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOfficerARS() throws Exception {
        // Initialize the database
        officerARRepository.saveAndFlush(officerAR);

        // Get all the officerARList
        restOfficerARMockMvc.perform(get("/api/officer-ars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(officerAR.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].secondLastName").value(hasItem(DEFAULT_SECOND_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].identificationNumber").value(hasItem(DEFAULT_IDENTIFICATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].inService").value(hasItem(DEFAULT_IN_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].annosExperiencia").value(hasItem(DEFAULT_ANNOS_EXPERIENCIA.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].plateNumber").value(hasItem(DEFAULT_PLATE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].aditionalNotes").value(hasItem(DEFAULT_ADITIONAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getOfficerAR() throws Exception {
        // Initialize the database
        officerARRepository.saveAndFlush(officerAR);

        // Get the officerAR
        restOfficerARMockMvc.perform(get("/api/officer-ars/{id}", officerAR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(officerAR.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.secondLastName").value(DEFAULT_SECOND_LAST_NAME.toString()))
            .andExpect(jsonPath("$.identificationNumber").value(DEFAULT_IDENTIFICATION_NUMBER.toString()))
            .andExpect(jsonPath("$.inService").value(DEFAULT_IN_SERVICE.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.annosExperiencia").value(DEFAULT_ANNOS_EXPERIENCIA.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.plateNumber").value(DEFAULT_PLATE_NUMBER.toString()))
            .andExpect(jsonPath("$.aditionalNotes").value(DEFAULT_ADITIONAL_NOTES.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOfficerAR() throws Exception {
        // Get the officerAR
        restOfficerARMockMvc.perform(get("/api/officer-ars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOfficerAR() throws Exception {
        // Initialize the database
        officerARRepository.saveAndFlush(officerAR);
        int databaseSizeBeforeUpdate = officerARRepository.findAll().size();

        // Update the officerAR
        OfficerAR updatedOfficerAR = officerARRepository.findOne(officerAR.getId());
        // Disconnect from session so that the updates on updatedOfficerAR are not directly saved in db
        em.detach(updatedOfficerAR);
        updatedOfficerAR
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .identificationNumber(UPDATED_IDENTIFICATION_NUMBER)
            .inService(UPDATED_IN_SERVICE)
            .imageUrl(UPDATED_IMAGE_URL)
            .annosExperiencia(UPDATED_ANNOS_EXPERIENCIA)
            .birthDate(UPDATED_BIRTH_DATE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .direction(UPDATED_DIRECTION)
            .plateNumber(UPDATED_PLATE_NUMBER)
            .aditionalNotes(UPDATED_ADITIONAL_NOTES)
            .enabled(UPDATED_ENABLED)
            .deleted(UPDATED_DELETED)
            .email(UPDATED_EMAIL);
        OfficerARDTO officerARDTO = officerARMapper.toDto(updatedOfficerAR);

        restOfficerARMockMvc.perform(put("/api/officer-ars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerARDTO)))
            .andExpect(status().isOk());

        // Validate the OfficerAR in the database
        List<OfficerAR> officerARList = officerARRepository.findAll();
        assertThat(officerARList).hasSize(databaseSizeBeforeUpdate);
        OfficerAR testOfficerAR = officerARList.get(officerARList.size() - 1);
        assertThat(testOfficerAR.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOfficerAR.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testOfficerAR.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testOfficerAR.getIdentificationNumber()).isEqualTo(UPDATED_IDENTIFICATION_NUMBER);
        assertThat(testOfficerAR.getInService()).isEqualTo(UPDATED_IN_SERVICE);
        assertThat(testOfficerAR.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testOfficerAR.getAnnosExperiencia()).isEqualTo(UPDATED_ANNOS_EXPERIENCIA);
        assertThat(testOfficerAR.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testOfficerAR.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOfficerAR.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testOfficerAR.getPlateNumber()).isEqualTo(UPDATED_PLATE_NUMBER);
        assertThat(testOfficerAR.getAditionalNotes()).isEqualTo(UPDATED_ADITIONAL_NOTES);
        assertThat(testOfficerAR.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testOfficerAR.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testOfficerAR.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingOfficerAR() throws Exception {
        int databaseSizeBeforeUpdate = officerARRepository.findAll().size();

        // Create the OfficerAR
        OfficerARDTO officerARDTO = officerARMapper.toDto(officerAR);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfficerARMockMvc.perform(put("/api/officer-ars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerARDTO)))
            .andExpect(status().isCreated());

        // Validate the OfficerAR in the database
        List<OfficerAR> officerARList = officerARRepository.findAll();
        assertThat(officerARList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOfficerAR() throws Exception {
        // Initialize the database
        officerARRepository.saveAndFlush(officerAR);
        int databaseSizeBeforeDelete = officerARRepository.findAll().size();

        // Get the officerAR
        restOfficerARMockMvc.perform(delete("/api/officer-ars/{id}", officerAR.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OfficerAR> officerARList = officerARRepository.findAll();
        assertThat(officerARList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfficerAR.class);
        OfficerAR officerAR1 = new OfficerAR();
        officerAR1.setId(1L);
        OfficerAR officerAR2 = new OfficerAR();
        officerAR2.setId(officerAR1.getId());
        assertThat(officerAR1).isEqualTo(officerAR2);
        officerAR2.setId(2L);
        assertThat(officerAR1).isNotEqualTo(officerAR2);
        officerAR1.setId(null);
        assertThat(officerAR1).isNotEqualTo(officerAR2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfficerARDTO.class);
        OfficerARDTO officerARDTO1 = new OfficerARDTO();
        officerARDTO1.setId(1L);
        OfficerARDTO officerARDTO2 = new OfficerARDTO();
        assertThat(officerARDTO1).isNotEqualTo(officerARDTO2);
        officerARDTO2.setId(officerARDTO1.getId());
        assertThat(officerARDTO1).isEqualTo(officerARDTO2);
        officerARDTO2.setId(2L);
        assertThat(officerARDTO1).isNotEqualTo(officerARDTO2);
        officerARDTO1.setId(null);
        assertThat(officerARDTO1).isNotEqualTo(officerARDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(officerARMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(officerARMapper.fromId(null)).isNull();
    }
}
