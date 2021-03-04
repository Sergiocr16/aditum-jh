package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Complaint;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.repository.ComplaintRepository;
import com.lighthouse.aditum.service.ComplaintService;
import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.service.dto.ComplaintDTO;
import com.lighthouse.aditum.service.mapper.ComplaintMapper;
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
 * Test class for the ComplaintResource REST controller.
 *
 * @see ComplaintResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ComplaintResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLAINT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_RESOLUTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESOLUTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMPLAINT_CATEGORY = 1;
    private static final Integer UPDATED_COMPLAINT_CATEGORY = 2;

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_READED_ADMIN = "AAAAAAAAAA";
    private static final String UPDATED_READED_ADMIN = "BBBBBBBBBB";

    private static final String DEFAULT_READED_RESIDENT = "AAAAAAAAAA";
    private static final String UPDATED_READED_RESIDENT = "BBBBBBBBBB";

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private ComplaintService complaintService;

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

    private MockMvc restComplaintMockMvc;

    private Complaint complaint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ComplaintResource complaintResource = new ComplaintResource(complaintService,residentService);
        this.restComplaintMockMvc = MockMvcBuilders.standaloneSetup(complaintResource)
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
    public static Complaint createEntity(EntityManager em) {
        Complaint complaint = new Complaint()
            .description(DEFAULT_DESCRIPTION)
            .complaintType(DEFAULT_COMPLAINT_TYPE)
            .status(DEFAULT_STATUS)
            .deleted(DEFAULT_DELETED)
            .creationDate(DEFAULT_CREATION_DATE)
            .resolutionDate(DEFAULT_RESOLUTION_DATE)
            .fileUrl(DEFAULT_FILE_URL)
            .complaintCategory(DEFAULT_COMPLAINT_CATEGORY)
            .subject(DEFAULT_SUBJECT)
            .readedAdmin(DEFAULT_READED_ADMIN)
            .readedResident(DEFAULT_READED_RESIDENT);
        // Add required entity
        House house = HouseResourceIntTest.createEntity(em);
        em.persist(house);
        em.flush();
        complaint.setHouse(house);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        complaint.setCompany(company);
        // Add required entity
        Resident resident = ResidentResourceIntTest.createEntity(em);
        em.persist(resident);
        em.flush();
        complaint.setResident(resident);
        return complaint;
    }

    @Before
    public void initTest() {
        complaint = createEntity(em);
    }

    @Test
    @Transactional
    public void createComplaint() throws Exception {
        int databaseSizeBeforeCreate = complaintRepository.findAll().size();

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);
        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isCreated());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeCreate + 1);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testComplaint.getComplaintType()).isEqualTo(DEFAULT_COMPLAINT_TYPE);
        assertThat(testComplaint.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testComplaint.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testComplaint.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testComplaint.getResolutionDate()).isEqualTo(DEFAULT_RESOLUTION_DATE);
        assertThat(testComplaint.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testComplaint.getComplaintCategory()).isEqualTo(DEFAULT_COMPLAINT_CATEGORY);
        assertThat(testComplaint.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testComplaint.getReadedAdmin()).isEqualTo(DEFAULT_READED_ADMIN);
        assertThat(testComplaint.getReadedResident()).isEqualTo(DEFAULT_READED_RESIDENT);
    }

    @Test
    @Transactional
    public void createComplaintWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = complaintRepository.findAll().size();

        // Create the Complaint with an existing ID
        complaint.setId(1L);
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setDescription(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComplaintTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setComplaintType(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setStatus(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setDeleted(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setCreationDate(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkComplaintCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = complaintRepository.findAll().size();
        // set the field null
        complaint.setComplaintCategory(null);

        // Create the Complaint, which fails.
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        restComplaintMockMvc.perform(post("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isBadRequest());

        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComplaints() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get all the complaintList
        restComplaintMockMvc.perform(get("/api/complaints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(complaint.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].complaintType").value(hasItem(DEFAULT_COMPLAINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].resolutionDate").value(hasItem(sameInstant(DEFAULT_RESOLUTION_DATE))))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL.toString())))
            .andExpect(jsonPath("$.[*].complaintCategory").value(hasItem(DEFAULT_COMPLAINT_CATEGORY)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].readedAdmin").value(hasItem(DEFAULT_READED_ADMIN.toString())))
            .andExpect(jsonPath("$.[*].readedResident").value(hasItem(DEFAULT_READED_RESIDENT.toString())));
    }

    @Test
    @Transactional
    public void getComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);

        // Get the complaint
        restComplaintMockMvc.perform(get("/api/complaints/{id}", complaint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(complaint.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.complaintType").value(DEFAULT_COMPLAINT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.resolutionDate").value(sameInstant(DEFAULT_RESOLUTION_DATE)))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL.toString()))
            .andExpect(jsonPath("$.complaintCategory").value(DEFAULT_COMPLAINT_CATEGORY))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.readedAdmin").value(DEFAULT_READED_ADMIN.toString()))
            .andExpect(jsonPath("$.readedResident").value(DEFAULT_READED_RESIDENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComplaint() throws Exception {
        // Get the complaint
        restComplaintMockMvc.perform(get("/api/complaints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Update the complaint
        Complaint updatedComplaint = complaintRepository.findOne(complaint.getId());
        // Disconnect from session so that the updates on updatedComplaint are not directly saved in db
        em.detach(updatedComplaint);
        updatedComplaint
            .description(UPDATED_DESCRIPTION)
            .complaintType(UPDATED_COMPLAINT_TYPE)
            .status(UPDATED_STATUS)
            .deleted(UPDATED_DELETED)
            .creationDate(UPDATED_CREATION_DATE)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .fileUrl(UPDATED_FILE_URL)
            .complaintCategory(UPDATED_COMPLAINT_CATEGORY)
            .subject(UPDATED_SUBJECT)
            .readedAdmin(UPDATED_READED_ADMIN)
            .readedResident(UPDATED_READED_RESIDENT);
        ComplaintDTO complaintDTO = complaintMapper.toDto(updatedComplaint);

        restComplaintMockMvc.perform(put("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isOk());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate);
        Complaint testComplaint = complaintList.get(complaintList.size() - 1);
        assertThat(testComplaint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComplaint.getComplaintType()).isEqualTo(UPDATED_COMPLAINT_TYPE);
        assertThat(testComplaint.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testComplaint.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testComplaint.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testComplaint.getResolutionDate()).isEqualTo(UPDATED_RESOLUTION_DATE);
        assertThat(testComplaint.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testComplaint.getComplaintCategory()).isEqualTo(UPDATED_COMPLAINT_CATEGORY);
        assertThat(testComplaint.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testComplaint.getReadedAdmin()).isEqualTo(UPDATED_READED_ADMIN);
        assertThat(testComplaint.getReadedResident()).isEqualTo(UPDATED_READED_RESIDENT);
    }

    @Test
    @Transactional
    public void updateNonExistingComplaint() throws Exception {
        int databaseSizeBeforeUpdate = complaintRepository.findAll().size();

        // Create the Complaint
        ComplaintDTO complaintDTO = complaintMapper.toDto(complaint);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restComplaintMockMvc.perform(put("/api/complaints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(complaintDTO)))
            .andExpect(status().isCreated());

        // Validate the Complaint in the database
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteComplaint() throws Exception {
        // Initialize the database
        complaintRepository.saveAndFlush(complaint);
        int databaseSizeBeforeDelete = complaintRepository.findAll().size();

        // Get the complaint
        restComplaintMockMvc.perform(delete("/api/complaints/{id}", complaint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Complaint> complaintList = complaintRepository.findAll();
        assertThat(complaintList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Complaint.class);
        Complaint complaint1 = new Complaint();
        complaint1.setId(1L);
        Complaint complaint2 = new Complaint();
        complaint2.setId(complaint1.getId());
        assertThat(complaint1).isEqualTo(complaint2);
        complaint2.setId(2L);
        assertThat(complaint1).isNotEqualTo(complaint2);
        complaint1.setId(null);
        assertThat(complaint1).isNotEqualTo(complaint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComplaintDTO.class);
        ComplaintDTO complaintDTO1 = new ComplaintDTO();
        complaintDTO1.setId(1L);
        ComplaintDTO complaintDTO2 = new ComplaintDTO();
        assertThat(complaintDTO1).isNotEqualTo(complaintDTO2);
        complaintDTO2.setId(complaintDTO1.getId());
        assertThat(complaintDTO1).isEqualTo(complaintDTO2);
        complaintDTO2.setId(2L);
        assertThat(complaintDTO1).isNotEqualTo(complaintDTO2);
        complaintDTO1.setId(null);
        assertThat(complaintDTO1).isNotEqualTo(complaintDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(complaintMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(complaintMapper.fromId(null)).isNull();
    }
}
