package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.repository.CommonAreaReservationsRepository;
import com.lighthouse.aditum.service.CommonAreaReservationsService;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaReservationsMapper;
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
 * Test class for the CommonAreaReservationsResource REST controller.
 *
 * @see CommonAreaReservationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CommonAreaReservationsResourceIntTest {

    private static final Long DEFAULT_HOUSE_ID = 1L;
    private static final Long UPDATED_HOUSE_ID = 2L;

    private static final Long DEFAULT_RESIDENT_ID = 1L;
    private static final Long UPDATED_RESIDENT_ID = 2L;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_INITIAL_TIME = "AAAAAAAAAA";
    private static final String UPDATED_INITIAL_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_FINAL_TIME = "AAAAAAAAAA";
    private static final String UPDATED_FINAL_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private CommonAreaReservationsRepository commonAreaReservationsRepository;

    @Autowired
    private CommonAreaReservationsMapper commonAreaReservationsMapper;

    @Autowired
    private CommonAreaReservationsService commonAreaReservationsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommonAreaReservationsMockMvc;

    private CommonAreaReservations commonAreaReservations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommonAreaReservationsResource commonAreaReservationsResource = new CommonAreaReservationsResource(commonAreaReservationsService);
        this.restCommonAreaReservationsMockMvc = MockMvcBuilders.standaloneSetup(commonAreaReservationsResource)
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
    public static CommonAreaReservations createEntity(EntityManager em) {
        CommonAreaReservations commonAreaReservations = new CommonAreaReservations()
            .houseId(DEFAULT_HOUSE_ID)
            .residentId(DEFAULT_RESIDENT_ID)
            .date(DEFAULT_DATE)
            .initialTime(DEFAULT_INITIAL_TIME)
            .finalTime(DEFAULT_FINAL_TIME)
            .comments(DEFAULT_COMMENTS);
        return commonAreaReservations;
    }

    @Before
    public void initTest() {
        commonAreaReservations = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommonAreaReservations() throws Exception {
        int databaseSizeBeforeCreate = commonAreaReservationsRepository.findAll().size();

        // Create the CommonAreaReservations
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isCreated());

        // Validate the CommonAreaReservations in the database
        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeCreate + 1);
        CommonAreaReservations testCommonAreaReservations = commonAreaReservationsList.get(commonAreaReservationsList.size() - 1);
        assertThat(testCommonAreaReservations.getHouseId()).isEqualTo(DEFAULT_HOUSE_ID);
        assertThat(testCommonAreaReservations.getResidentId()).isEqualTo(DEFAULT_RESIDENT_ID);
        assertThat(testCommonAreaReservations.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommonAreaReservations.getInitialTime()).isEqualTo(DEFAULT_INITIAL_TIME);
        assertThat(testCommonAreaReservations.getFinalTime()).isEqualTo(DEFAULT_FINAL_TIME);
        assertThat(testCommonAreaReservations.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createCommonAreaReservationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commonAreaReservationsRepository.findAll().size();

        // Create the CommonAreaReservations with an existing ID
        commonAreaReservations.setId(1L);
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHouseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonAreaReservationsRepository.findAll().size();
        // set the field null
        commonAreaReservations.setHouseId(null);

        // Create the CommonAreaReservations, which fails.
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isBadRequest());

        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResidentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonAreaReservationsRepository.findAll().size();
        // set the field null
        commonAreaReservations.setResidentId(null);

        // Create the CommonAreaReservations, which fails.
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isBadRequest());

        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonAreaReservationsRepository.findAll().size();
        // set the field null
        commonAreaReservations.setDate(null);

        // Create the CommonAreaReservations, which fails.
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isBadRequest());

        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInitialTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonAreaReservationsRepository.findAll().size();
        // set the field null
        commonAreaReservations.setInitialTime(null);

        // Create the CommonAreaReservations, which fails.
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isBadRequest());

        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinalTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonAreaReservationsRepository.findAll().size();
        // set the field null
        commonAreaReservations.setFinalTime(null);

        // Create the CommonAreaReservations, which fails.
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        restCommonAreaReservationsMockMvc.perform(post("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isBadRequest());

        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommonAreaReservations() throws Exception {
        // Initialize the database
        commonAreaReservationsRepository.saveAndFlush(commonAreaReservations);

        // Get all the commonAreaReservationsList
        restCommonAreaReservationsMockMvc.perform(get("/api/common-area-reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonAreaReservations.getId().intValue())))
            .andExpect(jsonPath("$.[*].houseId").value(hasItem(DEFAULT_HOUSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].residentId").value(hasItem(DEFAULT_RESIDENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].initialTime").value(hasItem(DEFAULT_INITIAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].finalTime").value(hasItem(DEFAULT_FINAL_TIME.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getCommonAreaReservations() throws Exception {
        // Initialize the database
        commonAreaReservationsRepository.saveAndFlush(commonAreaReservations);

        // Get the commonAreaReservations
        restCommonAreaReservationsMockMvc.perform(get("/api/common-area-reservations/{id}", commonAreaReservations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commonAreaReservations.getId().intValue()))
            .andExpect(jsonPath("$.houseId").value(DEFAULT_HOUSE_ID.intValue()))
            .andExpect(jsonPath("$.residentId").value(DEFAULT_RESIDENT_ID.intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.initialTime").value(DEFAULT_INITIAL_TIME.toString()))
            .andExpect(jsonPath("$.finalTime").value(DEFAULT_FINAL_TIME.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommonAreaReservations() throws Exception {
        // Get the commonAreaReservations
        restCommonAreaReservationsMockMvc.perform(get("/api/common-area-reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommonAreaReservations() throws Exception {
        // Initialize the database
        commonAreaReservationsRepository.saveAndFlush(commonAreaReservations);
        int databaseSizeBeforeUpdate = commonAreaReservationsRepository.findAll().size();

        // Update the commonAreaReservations
        CommonAreaReservations updatedCommonAreaReservations = commonAreaReservationsRepository.findOne(commonAreaReservations.getId());
        updatedCommonAreaReservations
            .houseId(UPDATED_HOUSE_ID)
            .residentId(UPDATED_RESIDENT_ID)
            .date(UPDATED_DATE)
            .initialTime(UPDATED_INITIAL_TIME)
            .finalTime(UPDATED_FINAL_TIME)
            .comments(UPDATED_COMMENTS);
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(updatedCommonAreaReservations);

        restCommonAreaReservationsMockMvc.perform(put("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isOk());

        // Validate the CommonAreaReservations in the database
        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeUpdate);
        CommonAreaReservations testCommonAreaReservations = commonAreaReservationsList.get(commonAreaReservationsList.size() - 1);
        assertThat(testCommonAreaReservations.getHouseId()).isEqualTo(UPDATED_HOUSE_ID);
        assertThat(testCommonAreaReservations.getResidentId()).isEqualTo(UPDATED_RESIDENT_ID);
        assertThat(testCommonAreaReservations.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommonAreaReservations.getInitialTime()).isEqualTo(UPDATED_INITIAL_TIME);
        assertThat(testCommonAreaReservations.getFinalTime()).isEqualTo(UPDATED_FINAL_TIME);
        assertThat(testCommonAreaReservations.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingCommonAreaReservations() throws Exception {
        int databaseSizeBeforeUpdate = commonAreaReservationsRepository.findAll().size();

        // Create the CommonAreaReservations
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommonAreaReservationsMockMvc.perform(put("/api/common-area-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaReservationsDTO)))
            .andExpect(status().isCreated());

        // Validate the CommonAreaReservations in the database
        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommonAreaReservations() throws Exception {
        // Initialize the database
        commonAreaReservationsRepository.saveAndFlush(commonAreaReservations);
        int databaseSizeBeforeDelete = commonAreaReservationsRepository.findAll().size();

        // Get the commonAreaReservations
        restCommonAreaReservationsMockMvc.perform(delete("/api/common-area-reservations/{id}", commonAreaReservations.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CommonAreaReservations> commonAreaReservationsList = commonAreaReservationsRepository.findAll();
        assertThat(commonAreaReservationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonAreaReservations.class);
        CommonAreaReservations commonAreaReservations1 = new CommonAreaReservations();
        commonAreaReservations1.setId(1L);
        CommonAreaReservations commonAreaReservations2 = new CommonAreaReservations();
        commonAreaReservations2.setId(commonAreaReservations1.getId());
        assertThat(commonAreaReservations1).isEqualTo(commonAreaReservations2);
        commonAreaReservations2.setId(2L);
        assertThat(commonAreaReservations1).isNotEqualTo(commonAreaReservations2);
        commonAreaReservations1.setId(null);
        assertThat(commonAreaReservations1).isNotEqualTo(commonAreaReservations2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonAreaReservationsDTO.class);
        CommonAreaReservationsDTO commonAreaReservationsDTO1 = new CommonAreaReservationsDTO();
        commonAreaReservationsDTO1.setId(1L);
        CommonAreaReservationsDTO commonAreaReservationsDTO2 = new CommonAreaReservationsDTO();
        assertThat(commonAreaReservationsDTO1).isNotEqualTo(commonAreaReservationsDTO2);
        commonAreaReservationsDTO2.setId(commonAreaReservationsDTO1.getId());
        assertThat(commonAreaReservationsDTO1).isEqualTo(commonAreaReservationsDTO2);
        commonAreaReservationsDTO2.setId(2L);
        assertThat(commonAreaReservationsDTO1).isNotEqualTo(commonAreaReservationsDTO2);
        commonAreaReservationsDTO1.setId(null);
        assertThat(commonAreaReservationsDTO1).isNotEqualTo(commonAreaReservationsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commonAreaReservationsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commonAreaReservationsMapper.fromId(null)).isNull();
    }
}
