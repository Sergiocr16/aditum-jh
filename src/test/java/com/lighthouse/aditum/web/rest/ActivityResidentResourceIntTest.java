package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ActivityResident;
import com.lighthouse.aditum.repository.ActivityResidentRepository;
import com.lighthouse.aditum.service.ActivityResidentService;
import com.lighthouse.aditum.service.dto.ActivityResidentDTO;
import com.lighthouse.aditum.service.mapper.ActivityResidentMapper;
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
 * Test class for the ActivityResidentResource REST controller.
 *
 * @see ActivityResidentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ActivityResidentResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_SEEN = 1;
    private static final Integer UPDATED_SEEN = 2;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_USER = 1L;
    private static final Long UPDATED_USER = 2L;

    @Autowired
    private ActivityResidentRepository activityResidentRepository;

    @Autowired
    private ActivityResidentMapper activityResidentMapper;

    @Autowired
    private ActivityResidentService activityResidentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityResidentMockMvc;

    private ActivityResident activityResident;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityResidentResource activityResidentResource = new ActivityResidentResource(activityResidentService);
        this.restActivityResidentMockMvc = MockMvcBuilders.standaloneSetup(activityResidentResource)
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
    public static ActivityResident createEntity(EntityManager em) {
        ActivityResident activityResident = new ActivityResident()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE)
            .seen(DEFAULT_SEEN)
            .image(DEFAULT_IMAGE)
            .type(DEFAULT_TYPE)
            .user(DEFAULT_USER);
        return activityResident;
    }

    @Before
    public void initTest() {
        activityResident = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityResident() throws Exception {
        int databaseSizeBeforeCreate = activityResidentRepository.findAll().size();

        // Create the ActivityResident
        ActivityResidentDTO activityResidentDTO = activityResidentMapper.toDto(activityResident);
        restActivityResidentMockMvc.perform(post("/api/activity-residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityResidentDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityResident in the database
        List<ActivityResident> activityResidentList = activityResidentRepository.findAll();
        assertThat(activityResidentList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityResident testActivityResident = activityResidentList.get(activityResidentList.size() - 1);
        assertThat(testActivityResident.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testActivityResident.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testActivityResident.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testActivityResident.getSeen()).isEqualTo(DEFAULT_SEEN);
        assertThat(testActivityResident.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testActivityResident.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActivityResident.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    public void createActivityResidentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityResidentRepository.findAll().size();

        // Create the ActivityResident with an existing ID
        activityResident.setId(1L);
        ActivityResidentDTO activityResidentDTO = activityResidentMapper.toDto(activityResident);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityResidentMockMvc.perform(post("/api/activity-residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityResidentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityResident in the database
        List<ActivityResident> activityResidentList = activityResidentRepository.findAll();
        assertThat(activityResidentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllActivityResidents() throws Exception {
        // Initialize the database
        activityResidentRepository.saveAndFlush(activityResident);

        // Get all the activityResidentList
        restActivityResidentMockMvc.perform(get("/api/activity-residents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityResident.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].seen").value(hasItem(DEFAULT_SEEN)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.intValue())));
    }

    @Test
    @Transactional
    public void getActivityResident() throws Exception {
        // Initialize the database
        activityResidentRepository.saveAndFlush(activityResident);

        // Get the activityResident
        restActivityResidentMockMvc.perform(get("/api/activity-residents/{id}", activityResident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityResident.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.seen").value(DEFAULT_SEEN))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityResident() throws Exception {
        // Get the activityResident
        restActivityResidentMockMvc.perform(get("/api/activity-residents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityResident() throws Exception {
        // Initialize the database
        activityResidentRepository.saveAndFlush(activityResident);
        int databaseSizeBeforeUpdate = activityResidentRepository.findAll().size();

        // Update the activityResident
        ActivityResident updatedActivityResident = activityResidentRepository.findOne(activityResident.getId());
        // Disconnect from session so that the updates on updatedActivityResident are not directly saved in db
        em.detach(updatedActivityResident);
        updatedActivityResident
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .seen(UPDATED_SEEN)
            .image(UPDATED_IMAGE)
            .type(UPDATED_TYPE)
            .user(UPDATED_USER);
        ActivityResidentDTO activityResidentDTO = activityResidentMapper.toDto(updatedActivityResident);

        restActivityResidentMockMvc.perform(put("/api/activity-residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityResidentDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityResident in the database
        List<ActivityResident> activityResidentList = activityResidentRepository.findAll();
        assertThat(activityResidentList).hasSize(databaseSizeBeforeUpdate);
        ActivityResident testActivityResident = activityResidentList.get(activityResidentList.size() - 1);
        assertThat(testActivityResident.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testActivityResident.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testActivityResident.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActivityResident.getSeen()).isEqualTo(UPDATED_SEEN);
        assertThat(testActivityResident.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testActivityResident.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActivityResident.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityResident() throws Exception {
        int databaseSizeBeforeUpdate = activityResidentRepository.findAll().size();

        // Create the ActivityResident
        ActivityResidentDTO activityResidentDTO = activityResidentMapper.toDto(activityResident);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivityResidentMockMvc.perform(put("/api/activity-residents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityResidentDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityResident in the database
        List<ActivityResident> activityResidentList = activityResidentRepository.findAll();
        assertThat(activityResidentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActivityResident() throws Exception {
        // Initialize the database
        activityResidentRepository.saveAndFlush(activityResident);
        int databaseSizeBeforeDelete = activityResidentRepository.findAll().size();

        // Get the activityResident
        restActivityResidentMockMvc.perform(delete("/api/activity-residents/{id}", activityResident.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityResident> activityResidentList = activityResidentRepository.findAll();
        assertThat(activityResidentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityResident.class);
        ActivityResident activityResident1 = new ActivityResident();
        activityResident1.setId(1L);
        ActivityResident activityResident2 = new ActivityResident();
        activityResident2.setId(activityResident1.getId());
        assertThat(activityResident1).isEqualTo(activityResident2);
        activityResident2.setId(2L);
        assertThat(activityResident1).isNotEqualTo(activityResident2);
        activityResident1.setId(null);
        assertThat(activityResident1).isNotEqualTo(activityResident2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityResidentDTO.class);
        ActivityResidentDTO activityResidentDTO1 = new ActivityResidentDTO();
        activityResidentDTO1.setId(1L);
        ActivityResidentDTO activityResidentDTO2 = new ActivityResidentDTO();
        assertThat(activityResidentDTO1).isNotEqualTo(activityResidentDTO2);
        activityResidentDTO2.setId(activityResidentDTO1.getId());
        assertThat(activityResidentDTO1).isEqualTo(activityResidentDTO2);
        activityResidentDTO2.setId(2L);
        assertThat(activityResidentDTO1).isNotEqualTo(activityResidentDTO2);
        activityResidentDTO1.setId(null);
        assertThat(activityResidentDTO1).isNotEqualTo(activityResidentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityResidentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityResidentMapper.fromId(null)).isNull();
    }
}
