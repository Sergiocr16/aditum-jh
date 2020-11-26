package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.NotificationSended;
import com.lighthouse.aditum.repository.NotificationSendedRepository;
import com.lighthouse.aditum.service.NotificationSendedService;
import com.lighthouse.aditum.service.dto.NotificationSendedDTO;
import com.lighthouse.aditum.service.mapper.NotificationSendedMapper;
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
 * Test class for the NotificationSendedResource REST controller.
 *
 * @see NotificationSendedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class NotificationSendedResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SENDED_TO = "AAAAAAAAAA";
    private static final String UPDATED_SENDED_TO = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private NotificationSendedRepository notificationSendedRepository;

    @Autowired
    private NotificationSendedMapper notificationSendedMapper;

    @Autowired
    private NotificationSendedService notificationSendedService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNotificationSendedMockMvc;

    private NotificationSended notificationSended;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationSendedResource notificationSendedResource = new NotificationSendedResource(notificationSendedService);
        this.restNotificationSendedMockMvc = MockMvcBuilders.standaloneSetup(notificationSendedResource)
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
    public static NotificationSended createEntity(EntityManager em) {
        NotificationSended notificationSended = new NotificationSended()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .sendedTo(DEFAULT_SENDED_TO)
            .url(DEFAULT_URL)
            .state(DEFAULT_STATE)
            .date(DEFAULT_DATE);
        return notificationSended;
    }

    @Before
    public void initTest() {
        notificationSended = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationSended() throws Exception {
        int databaseSizeBeforeCreate = notificationSendedRepository.findAll().size();

        // Create the NotificationSended
        NotificationSendedDTO notificationSendedDTO = notificationSendedMapper.toDto(notificationSended);
        restNotificationSendedMockMvc.perform(post("/api/notification-sendeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationSendedDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationSended in the database
        List<NotificationSended> notificationSendedList = notificationSendedRepository.findAll();
        assertThat(notificationSendedList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationSended testNotificationSended = notificationSendedList.get(notificationSendedList.size() - 1);
        assertThat(testNotificationSended.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotificationSended.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNotificationSended.getSendedTo()).isEqualTo(DEFAULT_SENDED_TO);
        assertThat(testNotificationSended.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testNotificationSended.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testNotificationSended.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createNotificationSendedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationSendedRepository.findAll().size();

        // Create the NotificationSended with an existing ID
        notificationSended.setId(1L);
        NotificationSendedDTO notificationSendedDTO = notificationSendedMapper.toDto(notificationSended);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationSendedMockMvc.perform(post("/api/notification-sendeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationSendedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationSended in the database
        List<NotificationSended> notificationSendedList = notificationSendedRepository.findAll();
        assertThat(notificationSendedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNotificationSendeds() throws Exception {
        // Initialize the database
        notificationSendedRepository.saveAndFlush(notificationSended);

        // Get all the notificationSendedList
        restNotificationSendedMockMvc.perform(get("/api/notification-sendeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationSended.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].sendedTo").value(hasItem(DEFAULT_SENDED_TO.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getNotificationSended() throws Exception {
        // Initialize the database
        notificationSendedRepository.saveAndFlush(notificationSended);

        // Get the notificationSended
        restNotificationSendedMockMvc.perform(get("/api/notification-sendeds/{id}", notificationSended.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notificationSended.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.sendedTo").value(DEFAULT_SENDED_TO.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationSended() throws Exception {
        // Get the notificationSended
        restNotificationSendedMockMvc.perform(get("/api/notification-sendeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationSended() throws Exception {
        // Initialize the database
        notificationSendedRepository.saveAndFlush(notificationSended);
        int databaseSizeBeforeUpdate = notificationSendedRepository.findAll().size();

        // Update the notificationSended
        NotificationSended updatedNotificationSended = notificationSendedRepository.findOne(notificationSended.getId());
        // Disconnect from session so that the updates on updatedNotificationSended are not directly saved in db
        em.detach(updatedNotificationSended);
        updatedNotificationSended
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .sendedTo(UPDATED_SENDED_TO)
            .url(UPDATED_URL)
            .state(UPDATED_STATE)
            .date(UPDATED_DATE);
        NotificationSendedDTO notificationSendedDTO = notificationSendedMapper.toDto(updatedNotificationSended);

        restNotificationSendedMockMvc.perform(put("/api/notification-sendeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationSendedDTO)))
            .andExpect(status().isOk());

        // Validate the NotificationSended in the database
        List<NotificationSended> notificationSendedList = notificationSendedRepository.findAll();
        assertThat(notificationSendedList).hasSize(databaseSizeBeforeUpdate);
        NotificationSended testNotificationSended = notificationSendedList.get(notificationSendedList.size() - 1);
        assertThat(testNotificationSended.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotificationSended.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNotificationSended.getSendedTo()).isEqualTo(UPDATED_SENDED_TO);
        assertThat(testNotificationSended.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testNotificationSended.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testNotificationSended.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationSended() throws Exception {
        int databaseSizeBeforeUpdate = notificationSendedRepository.findAll().size();

        // Create the NotificationSended
        NotificationSendedDTO notificationSendedDTO = notificationSendedMapper.toDto(notificationSended);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNotificationSendedMockMvc.perform(put("/api/notification-sendeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notificationSendedDTO)))
            .andExpect(status().isCreated());

        // Validate the NotificationSended in the database
        List<NotificationSended> notificationSendedList = notificationSendedRepository.findAll();
        assertThat(notificationSendedList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNotificationSended() throws Exception {
        // Initialize the database
        notificationSendedRepository.saveAndFlush(notificationSended);
        int databaseSizeBeforeDelete = notificationSendedRepository.findAll().size();

        // Get the notificationSended
        restNotificationSendedMockMvc.perform(delete("/api/notification-sendeds/{id}", notificationSended.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NotificationSended> notificationSendedList = notificationSendedRepository.findAll();
        assertThat(notificationSendedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationSended.class);
        NotificationSended notificationSended1 = new NotificationSended();
        notificationSended1.setId(1L);
        NotificationSended notificationSended2 = new NotificationSended();
        notificationSended2.setId(notificationSended1.getId());
        assertThat(notificationSended1).isEqualTo(notificationSended2);
        notificationSended2.setId(2L);
        assertThat(notificationSended1).isNotEqualTo(notificationSended2);
        notificationSended1.setId(null);
        assertThat(notificationSended1).isNotEqualTo(notificationSended2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationSendedDTO.class);
        NotificationSendedDTO notificationSendedDTO1 = new NotificationSendedDTO();
        notificationSendedDTO1.setId(1L);
        NotificationSendedDTO notificationSendedDTO2 = new NotificationSendedDTO();
        assertThat(notificationSendedDTO1).isNotEqualTo(notificationSendedDTO2);
        notificationSendedDTO2.setId(notificationSendedDTO1.getId());
        assertThat(notificationSendedDTO1).isEqualTo(notificationSendedDTO2);
        notificationSendedDTO2.setId(2L);
        assertThat(notificationSendedDTO1).isNotEqualTo(notificationSendedDTO2);
        notificationSendedDTO1.setId(null);
        assertThat(notificationSendedDTO1).isNotEqualTo(notificationSendedDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(notificationSendedMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(notificationSendedMapper.fromId(null)).isNull();
    }
}
