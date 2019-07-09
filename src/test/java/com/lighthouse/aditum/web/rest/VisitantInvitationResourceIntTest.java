package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.VisitantInvitation;
import com.lighthouse.aditum.repository.VisitantInvitationRepository;
import com.lighthouse.aditum.service.VisitantInvitationService;
import com.lighthouse.aditum.service.dto.VisitantInvitationDTO;
import com.lighthouse.aditum.service.mapper.VisitantInvitationMapper;
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
 * Test class for the VisitantInvitationResource REST controller.
 *
 * @see VisitantInvitationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class VisitantInvitationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_INVITATIONSTARTINGTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INVITATIONSTARTINGTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_INVITATIONLIMITTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INVITATIONLIMITTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LICENSEPLATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSEPLATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HASSCHEDULE = 1;
    private static final Integer UPDATED_HASSCHEDULE = 2;

    private static final String DEFAULT_DESTINY = "AAAAAAAAAA";
    private static final String UPDATED_DESTINY = "BBBBBBBBBB";

    private static final Long DEFAULT_HOUSE_ID = 1L;
    private static final Long UPDATED_HOUSE_ID = 2L;

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;

    private static final Long DEFAULT_ADMIN_ID = 1L;
    private static final Long UPDATED_ADMIN_ID = 2L;

    private static final Long DEFAULT_SCHEDULE_ID = 1L;
    private static final Long UPDATED_SCHEDULE_ID = 2L;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private VisitantInvitationRepository visitantInvitationRepository;

    @Autowired
    private VisitantInvitationMapper visitantInvitationMapper;

    @Autowired
    private VisitantInvitationService visitantInvitationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVisitantInvitationMockMvc;

    private VisitantInvitation visitantInvitation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VisitantInvitationResource visitantInvitationResource = new VisitantInvitationResource(visitantInvitationService);
        this.restVisitantInvitationMockMvc = MockMvcBuilders.standaloneSetup(visitantInvitationResource)
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
    public static VisitantInvitation createEntity(EntityManager em) {
        VisitantInvitation visitantInvitation = new VisitantInvitation()
            .name(DEFAULT_NAME)
            .lastname(DEFAULT_LASTNAME)
            .secondlastname(DEFAULT_SECONDLASTNAME)
            .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
            .invitationstartingtime(DEFAULT_INVITATIONSTARTINGTIME)
            .invitationlimittime(DEFAULT_INVITATIONLIMITTIME)
            .licenseplate(DEFAULT_LICENSEPLATE)
            .hasschedule(DEFAULT_HASSCHEDULE)
            .destiny(DEFAULT_DESTINY)
            .houseId(DEFAULT_HOUSE_ID)
            .companyId(DEFAULT_COMPANY_ID)
            .adminId(DEFAULT_ADMIN_ID)
            .scheduleId(DEFAULT_SCHEDULE_ID)
            .status(DEFAULT_STATUS);
        return visitantInvitation;
    }

    @Before
    public void initTest() {
        visitantInvitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisitantInvitation() throws Exception {
        int databaseSizeBeforeCreate = visitantInvitationRepository.findAll().size();

        // Create the VisitantInvitation
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantInvitation);
        restVisitantInvitationMockMvc.perform(post("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isCreated());

        // Validate the VisitantInvitation in the database
        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeCreate + 1);
        VisitantInvitation testVisitantInvitation = visitantInvitationList.get(visitantInvitationList.size() - 1);
        assertThat(testVisitantInvitation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVisitantInvitation.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testVisitantInvitation.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testVisitantInvitation.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testVisitantInvitation.getInvitationstartingtime()).isEqualTo(DEFAULT_INVITATIONSTARTINGTIME);
        assertThat(testVisitantInvitation.getInvitationlimittime()).isEqualTo(DEFAULT_INVITATIONLIMITTIME);
        assertThat(testVisitantInvitation.getLicenseplate()).isEqualTo(DEFAULT_LICENSEPLATE);
        assertThat(testVisitantInvitation.getHasschedule()).isEqualTo(DEFAULT_HASSCHEDULE);
        assertThat(testVisitantInvitation.getDestiny()).isEqualTo(DEFAULT_DESTINY);
        assertThat(testVisitantInvitation.getHouseId()).isEqualTo(DEFAULT_HOUSE_ID);
        assertThat(testVisitantInvitation.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testVisitantInvitation.getAdminId()).isEqualTo(DEFAULT_ADMIN_ID);
        assertThat(testVisitantInvitation.getScheduleId()).isEqualTo(DEFAULT_SCHEDULE_ID);
        assertThat(testVisitantInvitation.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createVisitantInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitantInvitationRepository.findAll().size();

        // Create the VisitantInvitation with an existing ID
        visitantInvitation.setId(1L);
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantInvitation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitantInvitationMockMvc.perform(post("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VisitantInvitation in the database
        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantInvitationRepository.findAll().size();
        // set the field null
        visitantInvitation.setName(null);

        // Create the VisitantInvitation, which fails.
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantInvitation);

        restVisitantInvitationMockMvc.perform(post("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isBadRequest());

        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantInvitationRepository.findAll().size();
        // set the field null
        visitantInvitation.setLastname(null);

        // Create the VisitantInvitation, which fails.
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantInvitation);

        restVisitantInvitationMockMvc.perform(post("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isBadRequest());

        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondlastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitantInvitationRepository.findAll().size();
        // set the field null
        visitantInvitation.setSecondlastname(null);

        // Create the VisitantInvitation, which fails.
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantInvitation);

        restVisitantInvitationMockMvc.perform(post("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isBadRequest());

        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVisitantInvitations() throws Exception {
        // Initialize the database
        visitantInvitationRepository.saveAndFlush(visitantInvitation);

        // Get all the visitantInvitationList
        restVisitantInvitationMockMvc.perform(get("/api/visitant-invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitantInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].invitationstartingtime").value(hasItem(sameInstant(DEFAULT_INVITATIONSTARTINGTIME))))
            .andExpect(jsonPath("$.[*].invitationlimittime").value(hasItem(sameInstant(DEFAULT_INVITATIONLIMITTIME))))
            .andExpect(jsonPath("$.[*].licenseplate").value(hasItem(DEFAULT_LICENSEPLATE.toString())))
            .andExpect(jsonPath("$.[*].hasschedule").value(hasItem(DEFAULT_HASSCHEDULE)))
            .andExpect(jsonPath("$.[*].destiny").value(hasItem(DEFAULT_DESTINY.toString())))
            .andExpect(jsonPath("$.[*].houseId").value(hasItem(DEFAULT_HOUSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].adminId").value(hasItem(DEFAULT_ADMIN_ID.intValue())))
            .andExpect(jsonPath("$.[*].scheduleId").value(hasItem(DEFAULT_SCHEDULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getVisitantInvitation() throws Exception {
        // Initialize the database
        visitantInvitationRepository.saveAndFlush(visitantInvitation);

        // Get the visitantInvitation
        restVisitantInvitationMockMvc.perform(get("/api/visitant-invitations/{id}", visitantInvitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(visitantInvitation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.invitationstartingtime").value(sameInstant(DEFAULT_INVITATIONSTARTINGTIME)))
            .andExpect(jsonPath("$.invitationlimittime").value(sameInstant(DEFAULT_INVITATIONLIMITTIME)))
            .andExpect(jsonPath("$.licenseplate").value(DEFAULT_LICENSEPLATE.toString()))
            .andExpect(jsonPath("$.hasschedule").value(DEFAULT_HASSCHEDULE))
            .andExpect(jsonPath("$.destiny").value(DEFAULT_DESTINY.toString()))
            .andExpect(jsonPath("$.houseId").value(DEFAULT_HOUSE_ID.intValue()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.adminId").value(DEFAULT_ADMIN_ID.intValue()))
            .andExpect(jsonPath("$.scheduleId").value(DEFAULT_SCHEDULE_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingVisitantInvitation() throws Exception {
        // Get the visitantInvitation
        restVisitantInvitationMockMvc.perform(get("/api/visitant-invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisitantInvitation() throws Exception {
        // Initialize the database
        visitantInvitationRepository.saveAndFlush(visitantInvitation);
        int databaseSizeBeforeUpdate = visitantInvitationRepository.findAll().size();

        // Update the visitantInvitation
        VisitantInvitation updatedVisitantInvitation = visitantInvitationRepository.findOne(visitantInvitation.getId());
        // Disconnect from session so that the updates on updatedVisitantInvitation are not directly saved in db
        em.detach(updatedVisitantInvitation);
        updatedVisitantInvitation
            .name(UPDATED_NAME)
            .lastname(UPDATED_LASTNAME)
            .secondlastname(UPDATED_SECONDLASTNAME)
            .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
            .invitationstartingtime(UPDATED_INVITATIONSTARTINGTIME)
            .invitationlimittime(UPDATED_INVITATIONLIMITTIME)
            .licenseplate(UPDATED_LICENSEPLATE)
            .hasschedule(UPDATED_HASSCHEDULE)
            .destiny(UPDATED_DESTINY)
            .houseId(UPDATED_HOUSE_ID)
            .companyId(UPDATED_COMPANY_ID)
            .adminId(UPDATED_ADMIN_ID)
            .scheduleId(UPDATED_SCHEDULE_ID)
            .status(UPDATED_STATUS);
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(updatedVisitantInvitation);

        restVisitantInvitationMockMvc.perform(put("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isOk());

        // Validate the VisitantInvitation in the database
        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeUpdate);
        VisitantInvitation testVisitantInvitation = visitantInvitationList.get(visitantInvitationList.size() - 1);
        assertThat(testVisitantInvitation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVisitantInvitation.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testVisitantInvitation.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testVisitantInvitation.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testVisitantInvitation.getInvitationstartingtime()).isEqualTo(UPDATED_INVITATIONSTARTINGTIME);
        assertThat(testVisitantInvitation.getInvitationlimittime()).isEqualTo(UPDATED_INVITATIONLIMITTIME);
        assertThat(testVisitantInvitation.getLicenseplate()).isEqualTo(UPDATED_LICENSEPLATE);
        assertThat(testVisitantInvitation.getHasschedule()).isEqualTo(UPDATED_HASSCHEDULE);
        assertThat(testVisitantInvitation.getDestiny()).isEqualTo(UPDATED_DESTINY);
        assertThat(testVisitantInvitation.getHouseId()).isEqualTo(UPDATED_HOUSE_ID);
        assertThat(testVisitantInvitation.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testVisitantInvitation.getAdminId()).isEqualTo(UPDATED_ADMIN_ID);
        assertThat(testVisitantInvitation.getScheduleId()).isEqualTo(UPDATED_SCHEDULE_ID);
        assertThat(testVisitantInvitation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingVisitantInvitation() throws Exception {
        int databaseSizeBeforeUpdate = visitantInvitationRepository.findAll().size();

        // Create the VisitantInvitation
        VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantInvitation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVisitantInvitationMockMvc.perform(put("/api/visitant-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visitantInvitationDTO)))
            .andExpect(status().isCreated());

        // Validate the VisitantInvitation in the database
        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVisitantInvitation() throws Exception {
        // Initialize the database
        visitantInvitationRepository.saveAndFlush(visitantInvitation);
        int databaseSizeBeforeDelete = visitantInvitationRepository.findAll().size();

        // Get the visitantInvitation
        restVisitantInvitationMockMvc.perform(delete("/api/visitant-invitations/{id}", visitantInvitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VisitantInvitation> visitantInvitationList = visitantInvitationRepository.findAll();
        assertThat(visitantInvitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitantInvitation.class);
        VisitantInvitation visitantInvitation1 = new VisitantInvitation();
        visitantInvitation1.setId(1L);
        VisitantInvitation visitantInvitation2 = new VisitantInvitation();
        visitantInvitation2.setId(visitantInvitation1.getId());
        assertThat(visitantInvitation1).isEqualTo(visitantInvitation2);
        visitantInvitation2.setId(2L);
        assertThat(visitantInvitation1).isNotEqualTo(visitantInvitation2);
        visitantInvitation1.setId(null);
        assertThat(visitantInvitation1).isNotEqualTo(visitantInvitation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitantInvitationDTO.class);
        VisitantInvitationDTO visitantInvitationDTO1 = new VisitantInvitationDTO();
        visitantInvitationDTO1.setId(1L);
        VisitantInvitationDTO visitantInvitationDTO2 = new VisitantInvitationDTO();
        assertThat(visitantInvitationDTO1).isNotEqualTo(visitantInvitationDTO2);
        visitantInvitationDTO2.setId(visitantInvitationDTO1.getId());
        assertThat(visitantInvitationDTO1).isEqualTo(visitantInvitationDTO2);
        visitantInvitationDTO2.setId(2L);
        assertThat(visitantInvitationDTO1).isNotEqualTo(visitantInvitationDTO2);
        visitantInvitationDTO1.setId(null);
        assertThat(visitantInvitationDTO1).isNotEqualTo(visitantInvitationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(visitantInvitationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(visitantInvitationMapper.fromId(null)).isNull();
    }
}
