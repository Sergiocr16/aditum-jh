//package com.lighthouse.aditum.web.rest;
//
//import com.lighthouse.aditum.AditumApp;
//
//import com.lighthouse.aditum.domain.AnnouncementComment;
//import com.lighthouse.aditum.domain.Resident;
//import com.lighthouse.aditum.domain.Announcement;
//import com.lighthouse.aditum.repository.AnnouncementCommentRepository;
//import com.lighthouse.aditum.service.AnnouncementCommentService;
//import com.lighthouse.aditum.service.dto.AnnouncementCommentDTO;
//import com.lighthouse.aditum.service.mapper.AnnouncementCommentMapper;
//import com.lighthouse.aditum.web.rest.errors.ExceptionTranslator;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.time.Instant;
//import java.time.ZonedDateTime;
//import java.time.ZoneOffset;
//import java.time.ZoneId;
//import java.util.List;
//
//
//import static com.lighthouse.aditum.web.rest.TestUtil.sameInstant;
//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Test class for the AnnouncementCommentResource REST controller.
// *
// * @see AnnouncementCommentResource
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AditumApp.class)
//public class AnnouncementCommentResourceIntTest {
//
//    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
//    private static final String UPDATED_COMMENT = "BBBBBBBBBB";
//
//    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//
//    @Autowired
//    private AnnouncementCommentRepository announcementCommentRepository;
//
//
//    @Autowired
//    private AnnouncementCommentMapper announcementCommentMapper;
//
//
//    @Autowired
//    private AnnouncementCommentService announcementCommentService;
//
//    @Autowired
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Autowired
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    @Autowired
//    private ExceptionTranslator exceptionTranslator;
//
//    @Autowired
//    private EntityManager em;
//
//    private MockMvc restAnnouncementCommentMockMvc;
//
//    private AnnouncementComment announcementComment;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final AnnouncementCommentResource announcementCommentResource = new AnnouncementCommentResource(announcementCommentService);
//        this.restAnnouncementCommentMockMvc = MockMvcBuilders.standaloneSetup(announcementCommentResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setConversionService(createFormattingConversionService())
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static AnnouncementComment createEntity(EntityManager em) {
//        AnnouncementComment announcementComment = new AnnouncementComment()
//            .comment(DEFAULT_COMMENT)
//            .creationDate(DEFAULT_CREATION_DATE);
//        // Add required entity
//        Resident resident = ResidentResourceIntTest.createEntity(em);
//        em.persist(resident);
//        em.flush();
//        announcementComment.setResident(resident);
//        // Add required entity
//        Announcement announcement = AnnouncementResourceIntTest.createEntity(em);
//        em.persist(announcement);
//        em.flush();
//        announcementComment.setAnnouncement(announcement);
//        return announcementComment;
//    }
//
//    @Before
//    public void initTest() {
//        announcementComment = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    public void createAnnouncementComment() throws Exception {
//        int databaseSizeBeforeCreate = announcementCommentRepository.findAll().size();
//
//        // Create the AnnouncementComment
//        AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(announcementComment);
//        restAnnouncementCommentMockMvc.perform(post("/api/announcement-comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(announcementCommentDTO)))
//            .andExpect(status().isCreated());
//
//        // Validate the AnnouncementComment in the database
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeCreate + 1);
//        AnnouncementComment testAnnouncementComment = announcementCommentList.get(announcementCommentList.size() - 1);
//        assertThat(testAnnouncementComment.getComment()).isEqualTo(DEFAULT_COMMENT);
//        assertThat(testAnnouncementComment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void createAnnouncementCommentWithExistingId() throws Exception {
//        int databaseSizeBeforeCreate = announcementCommentRepository.findAll().size();
//
//        // Create the AnnouncementComment with an existing ID
//        announcementComment.setId(1L);
//        AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(announcementComment);
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restAnnouncementCommentMockMvc.perform(post("/api/announcement-comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(announcementCommentDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the AnnouncementComment in the database
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    public void checkCommentIsRequired() throws Exception {
//        int databaseSizeBeforeTest = announcementCommentRepository.findAll().size();
//        // set the field null
//        announcementComment.setComment(null);
//
//        // Create the AnnouncementComment, which fails.
//        AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(announcementComment);
//
//        restAnnouncementCommentMockMvc.perform(post("/api/announcement-comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(announcementCommentDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void checkCreationDateIsRequired() throws Exception {
//        int databaseSizeBeforeTest = announcementCommentRepository.findAll().size();
//        // set the field null
//        announcementComment.setCreationDate(null);
//
//        // Create the AnnouncementComment, which fails.
//        AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(announcementComment);
//
//        restAnnouncementCommentMockMvc.perform(post("/api/announcement-comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(announcementCommentDTO)))
//            .andExpect(status().isBadRequest());
//
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAnnouncementComments() throws Exception {
//        // Initialize the database
//        announcementCommentRepository.saveAndFlush(announcementComment);
//
//        // Get all the announcementCommentList
//        restAnnouncementCommentMockMvc.perform(get("/api/announcement-comments?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(announcementComment.getId().intValue())))
//            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
//            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
//    }
//
//
//    @Test
//    @Transactional
//    public void getAnnouncementComment() throws Exception {
//        // Initialize the database
//        announcementCommentRepository.saveAndFlush(announcementComment);
//
//        // Get the announcementComment
//        restAnnouncementCommentMockMvc.perform(get("/api/announcement-comments/{id}", announcementComment.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.id").value(announcementComment.getId().intValue()))
//            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
//            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
//    }
//    @Test
//    @Transactional
//    public void getNonExistingAnnouncementComment() throws Exception {
//        // Get the announcementComment
//        restAnnouncementCommentMockMvc.perform(get("/api/announcement-comments/{id}", Long.MAX_VALUE))
//            .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void updateAnnouncementComment() throws Exception {
//        // Initialize the database
//        announcementCommentRepository.saveAndFlush(announcementComment);
//
//        int databaseSizeBeforeUpdate = announcementCommentRepository.findAll().size();
//
//        // Update the announcementComment
//        AnnouncementComment updatedAnnouncementComment = announcementCommentRepository.findById(announcementComment.getId()).get();
//        // Disconnect from session so that the updates on updatedAnnouncementComment are not directly saved in db
//        em.detach(updatedAnnouncementComment);
//        updatedAnnouncementComment
//            .comment(UPDATED_COMMENT)
//            .creationDate(UPDATED_CREATION_DATE);
//        AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(updatedAnnouncementComment);
//
//        restAnnouncementCommentMockMvc.perform(put("/api/announcement-comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(announcementCommentDTO)))
//            .andExpect(status().isOk());
//
//        // Validate the AnnouncementComment in the database
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeUpdate);
//        AnnouncementComment testAnnouncementComment = announcementCommentList.get(announcementCommentList.size() - 1);
//        assertThat(testAnnouncementComment.getComment()).isEqualTo(UPDATED_COMMENT);
//        assertThat(testAnnouncementComment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
//    }
//
//    @Test
//    @Transactional
//    public void updateNonExistingAnnouncementComment() throws Exception {
//        int databaseSizeBeforeUpdate = announcementCommentRepository.findAll().size();
//
//        // Create the AnnouncementComment
//        AnnouncementCommentDTO announcementCommentDTO = announcementCommentMapper.toDto(announcementComment);
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restAnnouncementCommentMockMvc.perform(put("/api/announcement-comments")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(announcementCommentDTO)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the AnnouncementComment in the database
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    public void deleteAnnouncementComment() throws Exception {
//        // Initialize the database
//        announcementCommentRepository.saveAndFlush(announcementComment);
//
//        int databaseSizeBeforeDelete = announcementCommentRepository.findAll().size();
//
//        // Get the announcementComment
//        restAnnouncementCommentMockMvc.perform(delete("/api/announcement-comments/{id}", announcementComment.getId())
//            .accept(TestUtil.APPLICATION_JSON_UTF8))
//            .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<AnnouncementComment> announcementCommentList = announcementCommentRepository.findAll();
//        assertThat(announcementCommentList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//
//    @Test
//    @Transactional
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(AnnouncementComment.class);
//        AnnouncementComment announcementComment1 = new AnnouncementComment();
//        announcementComment1.setId(1L);
//        AnnouncementComment announcementComment2 = new AnnouncementComment();
//        announcementComment2.setId(announcementComment1.getId());
//        assertThat(announcementComment1).isEqualTo(announcementComment2);
//        announcementComment2.setId(2L);
//        assertThat(announcementComment1).isNotEqualTo(announcementComment2);
//        announcementComment1.setId(null);
//        assertThat(announcementComment1).isNotEqualTo(announcementComment2);
//    }
//
//    @Test
//    @Transactional
//    public void dtoEqualsVerifier() throws Exception {
//        TestUtil.equalsVerifier(AnnouncementCommentDTO.class);
//        AnnouncementCommentDTO announcementCommentDTO1 = new AnnouncementCommentDTO();
//        announcementCommentDTO1.setId(1L);
//        AnnouncementCommentDTO announcementCommentDTO2 = new AnnouncementCommentDTO();
//        assertThat(announcementCommentDTO1).isNotEqualTo(announcementCommentDTO2);
//        announcementCommentDTO2.setId(announcementCommentDTO1.getId());
//        assertThat(announcementCommentDTO1).isEqualTo(announcementCommentDTO2);
//        announcementCommentDTO2.setId(2L);
//        assertThat(announcementCommentDTO1).isNotEqualTo(announcementCommentDTO2);
//        announcementCommentDTO1.setId(null);
//        assertThat(announcementCommentDTO1).isNotEqualTo(announcementCommentDTO2);
//    }
//
//    @Test
//    @Transactional
//    public void testEntityFromId() {
//        assertThat(announcementCommentMapper.fromId(42L).getId()).isEqualTo(42);
//        assertThat(announcementCommentMapper.fromId(null)).isNull();
//    }
//}
