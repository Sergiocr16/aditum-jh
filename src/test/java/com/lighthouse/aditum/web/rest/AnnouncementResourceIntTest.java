package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Announcement;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.AnnouncementRepository;
import com.lighthouse.aditum.service.AnnouncementMailService;
import com.lighthouse.aditum.service.AnnouncementService;
import com.lighthouse.aditum.service.CompanyService;
import com.lighthouse.aditum.service.PushNotificationService;
import com.lighthouse.aditum.service.dto.AnnouncementDTO;
import com.lighthouse.aditum.service.mapper.AnnouncementMapper;
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
 * Test class for the AnnouncementResource REST controller.
 *
 * @see AnnouncementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class AnnouncementResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PUBLISHING_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PUBLISHING_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AnnouncementMailService announcementMailService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnnouncementMockMvc;

    private Announcement announcement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnnouncementResource announcementResource = new AnnouncementResource(companyService,pushNotificationService,announcementService,announcementMailService);
        this.restAnnouncementMockMvc = MockMvcBuilders.standaloneSetup(announcementResource)
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
    public static Announcement createEntity(EntityManager em) {
        Announcement announcement = new Announcement()
            .title(DEFAULT_TITLE)
            .publishingDate(DEFAULT_PUBLISHING_DATE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .deleted(DEFAULT_DELETED);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        announcement.setCompany(company);
        return announcement;
    }

    @Before
    public void initTest() {
        announcement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnouncement() throws Exception {
        int databaseSizeBeforeCreate = announcementRepository.findAll().size();

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);
        restAnnouncementMockMvc.perform(post("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isCreated());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeCreate + 1);
        Announcement testAnnouncement = announcementList.get(announcementList.size() - 1);
        assertThat(testAnnouncement.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAnnouncement.getPublishingDate()).isEqualTo(DEFAULT_PUBLISHING_DATE);
        assertThat(testAnnouncement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnnouncement.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAnnouncement.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createAnnouncementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = announcementRepository.findAll().size();

        // Create the Announcement with an existing ID
        announcement.setId(1L);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnouncementMockMvc.perform(post("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = announcementRepository.findAll().size();
        // set the field null
        announcement.setTitle(null);

        // Create the Announcement, which fails.
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        restAnnouncementMockMvc.perform(post("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublishingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = announcementRepository.findAll().size();
        // set the field null
        announcement.setPublishingDate(null);

        // Create the Announcement, which fails.
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        restAnnouncementMockMvc.perform(post("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = announcementRepository.findAll().size();
        // set the field null
        announcement.setDescription(null);

        // Create the Announcement, which fails.
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        restAnnouncementMockMvc.perform(post("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = announcementRepository.findAll().size();
        // set the field null
        announcement.setStatus(null);

        // Create the Announcement, which fails.
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        restAnnouncementMockMvc.perform(post("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isBadRequest());

        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnnouncements() throws Exception {
        // Initialize the database
        announcementRepository.saveAndFlush(announcement);

        // Get all the announcementList
        restAnnouncementMockMvc.perform(get("/api/announcements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].publishingDate").value(hasItem(sameInstant(DEFAULT_PUBLISHING_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getAnnouncement() throws Exception {
        // Initialize the database
        announcementRepository.saveAndFlush(announcement);

        // Get the announcement
        restAnnouncementMockMvc.perform(get("/api/announcements/{id}", announcement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(announcement.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.publishingDate").value(sameInstant(DEFAULT_PUBLISHING_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingAnnouncement() throws Exception {
        // Get the announcement
        restAnnouncementMockMvc.perform(get("/api/announcements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnouncement() throws Exception {
        // Initialize the database
        announcementRepository.saveAndFlush(announcement);
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();

        // Update the announcement
        Announcement updatedAnnouncement = announcementRepository.findOne(announcement.getId());
        updatedAnnouncement
            .title(UPDATED_TITLE)
            .publishingDate(UPDATED_PUBLISHING_DATE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .deleted(UPDATED_DELETED);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(updatedAnnouncement);

        restAnnouncementMockMvc.perform(put("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isOk());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
        Announcement testAnnouncement = announcementList.get(announcementList.size() - 1);
        assertThat(testAnnouncement.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAnnouncement.getPublishingDate()).isEqualTo(UPDATED_PUBLISHING_DATE);
        assertThat(testAnnouncement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnnouncement.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAnnouncement.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnnouncementMockMvc.perform(put("/api/announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(announcementDTO)))
            .andExpect(status().isCreated());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnnouncement() throws Exception {
        // Initialize the database
        announcementRepository.saveAndFlush(announcement);
        int databaseSizeBeforeDelete = announcementRepository.findAll().size();

        // Get the announcement
        restAnnouncementMockMvc.perform(delete("/api/announcements/{id}", announcement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Announcement.class);
        Announcement announcement1 = new Announcement();
        announcement1.setId(1L);
        Announcement announcement2 = new Announcement();
        announcement2.setId(announcement1.getId());
        assertThat(announcement1).isEqualTo(announcement2);
        announcement2.setId(2L);
        assertThat(announcement1).isNotEqualTo(announcement2);
        announcement1.setId(null);
        assertThat(announcement1).isNotEqualTo(announcement2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnouncementDTO.class);
        AnnouncementDTO announcementDTO1 = new AnnouncementDTO();
        announcementDTO1.setId(1L);
        AnnouncementDTO announcementDTO2 = new AnnouncementDTO();
        assertThat(announcementDTO1).isNotEqualTo(announcementDTO2);
        announcementDTO2.setId(announcementDTO1.getId());
        assertThat(announcementDTO1).isEqualTo(announcementDTO2);
        announcementDTO2.setId(2L);
        assertThat(announcementDTO1).isNotEqualTo(announcementDTO2);
        announcementDTO1.setId(null);
        assertThat(announcementDTO1).isNotEqualTo(announcementDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(announcementMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(announcementMapper.fromId(null)).isNull();
    }
}
