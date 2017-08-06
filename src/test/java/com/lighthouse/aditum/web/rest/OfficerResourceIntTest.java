package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Officer;
import com.lighthouse.aditum.repository.OfficerRepository;
import com.lighthouse.aditum.service.OfficerService;
import com.lighthouse.aditum.service.dto.OfficerDTO;
import com.lighthouse.aditum.service.mapper.OfficerMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OfficerResource REST controller.
 *
 * @see OfficerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class OfficerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_INSERVICE = 0;
    private static final Integer UPDATED_INSERVICE = 1;

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    @Autowired
    private OfficerRepository officerRepository;

    @Autowired
    private OfficerMapper officerMapper;

    @Autowired
    private OfficerService officerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfficerMockMvc;

    private Officer officer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfficerResource officerResource = new OfficerResource(officerService);
        this.restOfficerMockMvc = MockMvcBuilders.standaloneSetup(officerResource)
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
    public static Officer createEntity(EntityManager em) {
        Officer officer = new Officer()
                .name(DEFAULT_NAME)
                .lastname(DEFAULT_LASTNAME)
                .secondlastname(DEFAULT_SECONDLASTNAME)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .email(DEFAULT_EMAIL)
                .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
                .inservice(DEFAULT_INSERVICE)
                .enable(DEFAULT_ENABLE)
                .image_url(DEFAULT_IMAGE_URL);
        return officer;
    }

    @Before
    public void initTest() {
        officer = createEntity(em);
    }

    @Test
    @Transactional
    public void createOfficer() throws Exception {
        int databaseSizeBeforeCreate = officerRepository.findAll().size();

        // Create the Officer
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isCreated());

        // Validate the Officer in the database
        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeCreate + 1);
        Officer testOfficer = officerList.get(officerList.size() - 1);
        assertThat(testOfficer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOfficer.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testOfficer.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testOfficer.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testOfficer.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testOfficer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOfficer.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testOfficer.getInservice()).isEqualTo(DEFAULT_INSERVICE);
        assertThat(testOfficer.isEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testOfficer.getImage_url()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    public void createOfficerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = officerRepository.findAll().size();

        // Create the Officer with an existing ID
        Officer existingOfficer = new Officer();
        existingOfficer.setId(1L);
        OfficerDTO existingOfficerDTO = officerMapper.officerToOfficerDTO(existingOfficer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingOfficerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = officerRepository.findAll().size();
        // set the field null
        officer.setName(null);

        // Create the Officer, which fails.
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isBadRequest());

        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = officerRepository.findAll().size();
        // set the field null
        officer.setLastname(null);

        // Create the Officer, which fails.
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isBadRequest());

        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = officerRepository.findAll().size();
        // set the field null
        officer.setSecondlastname(null);

        // Create the Officer, which fails.
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isBadRequest());

        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = officerRepository.findAll().size();
        // set the field null
        officer.setEmail(null);

        // Create the Officer, which fails.
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isBadRequest());

        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentificationnumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = officerRepository.findAll().size();
        // set the field null
        officer.setIdentificationnumber(null);

        // Create the Officer, which fails.
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isBadRequest());

        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInserviceIsRequired() throws Exception {
        int databaseSizeBeforeTest = officerRepository.findAll().size();
        // set the field null
        officer.setInservice(null);

        // Create the Officer, which fails.
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        restOfficerMockMvc.perform(post("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isBadRequest());

        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOfficers() throws Exception {
        // Initialize the database
        officerRepository.saveAndFlush(officer);

        // Get all the officerList
        restOfficerMockMvc.perform(get("/api/officers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(officer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].inservice").value(hasItem(DEFAULT_INSERVICE)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())));
    }

    @Test
    @Transactional
    public void getOfficer() throws Exception {
        // Initialize the database
        officerRepository.saveAndFlush(officer);

        // Get the officer
        restOfficerMockMvc.perform(get("/api/officers/{id}", officer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(officer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.inservice").value(DEFAULT_INSERVICE))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.image_url").value(DEFAULT_IMAGE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOfficer() throws Exception {
        // Get the officer
        restOfficerMockMvc.perform(get("/api/officers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOfficer() throws Exception {
        // Initialize the database
        officerRepository.saveAndFlush(officer);
        int databaseSizeBeforeUpdate = officerRepository.findAll().size();

        // Update the officer
        Officer updatedOfficer = officerRepository.findOne(officer.getId());
        updatedOfficer
                .name(UPDATED_NAME)
                .lastname(UPDATED_LASTNAME)
                .secondlastname(UPDATED_SECONDLASTNAME)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .email(UPDATED_EMAIL)
                .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
                .inservice(UPDATED_INSERVICE)
                .enable(UPDATED_ENABLE)
                .image_url(UPDATED_IMAGE_URL);
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(updatedOfficer);

        restOfficerMockMvc.perform(put("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isOk());

        // Validate the Officer in the database
        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeUpdate);
        Officer testOfficer = officerList.get(officerList.size() - 1);
        assertThat(testOfficer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOfficer.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testOfficer.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testOfficer.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOfficer.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testOfficer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOfficer.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testOfficer.getInservice()).isEqualTo(UPDATED_INSERVICE);
        assertThat(testOfficer.isEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testOfficer.getImage_url()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingOfficer() throws Exception {
        int databaseSizeBeforeUpdate = officerRepository.findAll().size();

        // Create the Officer
        OfficerDTO officerDTO = officerMapper.officerToOfficerDTO(officer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfficerMockMvc.perform(put("/api/officers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(officerDTO)))
            .andExpect(status().isCreated());

        // Validate the Officer in the database
        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOfficer() throws Exception {
        // Initialize the database
        officerRepository.saveAndFlush(officer);
        int databaseSizeBeforeDelete = officerRepository.findAll().size();

        // Get the officer
        restOfficerMockMvc.perform(delete("/api/officers/{id}", officer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Officer> officerList = officerRepository.findAll();
        assertThat(officerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Officer.class);
    }
}
