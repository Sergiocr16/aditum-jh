package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.ResidentRepository;
import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.ResidentMapper;
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
 * Test class for the ResidentResource REST controller.
 *
 * @see ResidentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ResidentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONENUMBER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_IS_OWNER = 0;
    private static final Integer UPDATED_IS_OWNER = 1;

    private static final Integer DEFAULT_ENABLED = 0;
    private static final Integer UPDATED_ENABLED = 1;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private ResidentMapper residentMapper;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResidentMockMvc;

    private Resident resident;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResidentResource residentResource = new ResidentResource(residentService);
        this.restResidentMockMvc = MockMvcBuilders.standaloneSetup(residentResource)
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
    public static Resident createEntity(EntityManager em) {
        Resident resident = new Resident()
                .name(DEFAULT_NAME)
                .lastname(DEFAULT_LASTNAME)
                .secondlastname(DEFAULT_SECONDLASTNAME)
                .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
                .phonenumber(DEFAULT_PHONENUMBER)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .email(DEFAULT_EMAIL)
                .isOwner(DEFAULT_IS_OWNER)
                .enabled(DEFAULT_ENABLED)
                .image_url(DEFAULT_IMAGE_URL)
                .type(DEFAULT_TYPE);
        return resident;
    }

    @Before
    public void initTest() {
        resident = createEntity(em);
    }

    @Test
    @Transactional
    public void createResident() throws Exception {
        int databaseSizeBeforeCreate = residentRepository.findAll().size();

        // Create the Resident
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);

        restResidentMockMvc.perform(post("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isCreated());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeCreate + 1);
        Resident testResident = residentList.get(residentList.size() - 1);
        assertThat(testResident.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResident.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testResident.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testResident.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testResident.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testResident.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testResident.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testResident.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testResident.getIsOwner()).isEqualTo(DEFAULT_IS_OWNER);
        assertThat(testResident.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testResident.getImage_url()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testResident.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createResidentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = residentRepository.findAll().size();

        // Create the Resident with an existing ID
        Resident existingResident = new Resident();
        existingResident.setId(1L);
        ResidentDTO existingResidentDTO = residentMapper.residentToResidentDTO(existingResident);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResidentMockMvc.perform(post("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingResidentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = residentRepository.findAll().size();
        // set the field null
        resident.setName(null);

        // Create the Resident, which fails.
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);

        restResidentMockMvc.perform(post("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = residentRepository.findAll().size();
        // set the field null
        resident.setLastname(null);

        // Create the Resident, which fails.
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);

        restResidentMockMvc.perform(post("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = residentRepository.findAll().size();
        // set the field null
        resident.setSecondlastname(null);

        // Create the Resident, which fails.
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);

        restResidentMockMvc.perform(post("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentificationnumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = residentRepository.findAll().size();
        // set the field null
        resident.setIdentificationnumber(null);

        // Create the Resident, which fails.
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);

        restResidentMockMvc.perform(post("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isBadRequest());

        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResidents() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList
        restResidentMockMvc.perform(get("/api/residents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resident.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].isOwner").value(hasItem(DEFAULT_IS_OWNER)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    public void getResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get the resident
        restResidentMockMvc.perform(get("/api/residents/{id}", resident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resident.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.phonenumber").value(DEFAULT_PHONENUMBER.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.isOwner").value(DEFAULT_IS_OWNER))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED))
            .andExpect(jsonPath("$.image_url").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingResident() throws Exception {
        // Get the resident
        restResidentMockMvc.perform(get("/api/residents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);
        int databaseSizeBeforeUpdate = residentRepository.findAll().size();

        // Update the resident
        Resident updatedResident = residentRepository.findOne(resident.getId());
        updatedResident
                .name(UPDATED_NAME)
                .lastname(UPDATED_LASTNAME)
                .secondlastname(UPDATED_SECONDLASTNAME)
                .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
                .phonenumber(UPDATED_PHONENUMBER)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .email(UPDATED_EMAIL)
                .isOwner(UPDATED_IS_OWNER)
                .enabled(UPDATED_ENABLED)
                .image_url(UPDATED_IMAGE_URL)
                .type(UPDATED_TYPE);
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(updatedResident);

        restResidentMockMvc.perform(put("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isOk());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeUpdate);
        Resident testResident = residentList.get(residentList.size() - 1);
        assertThat(testResident.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResident.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testResident.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testResident.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testResident.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testResident.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testResident.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testResident.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testResident.getIsOwner()).isEqualTo(UPDATED_IS_OWNER);
        assertThat(testResident.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testResident.getImage_url()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testResident.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingResident() throws Exception {
        int databaseSizeBeforeUpdate = residentRepository.findAll().size();

        // Create the Resident
        ResidentDTO residentDTO = residentMapper.residentToResidentDTO(resident);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResidentMockMvc.perform(put("/api/residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(residentDTO)))
            .andExpect(status().isCreated());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);
        int databaseSizeBeforeDelete = residentRepository.findAll().size();

        // Get the resident
        restResidentMockMvc.perform(delete("/api/residents/{id}", resident.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resident.class);
    }
}
