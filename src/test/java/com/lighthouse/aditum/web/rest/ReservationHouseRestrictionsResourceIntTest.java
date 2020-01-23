package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ReservationHouseRestrictions;
import com.lighthouse.aditum.repository.ReservationHouseRestrictionsRepository;
import com.lighthouse.aditum.service.ReservationHouseRestrictionsService;
import com.lighthouse.aditum.service.dto.ReservationHouseRestrictionsDTO;
import com.lighthouse.aditum.service.mapper.ReservationHouseRestrictionsMapper;
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
 * Test class for the ReservationHouseRestrictionsResource REST controller.
 *
 * @see ReservationHouseRestrictionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ReservationHouseRestrictionsResourceIntTest {

    private static final Integer DEFAULT_RESERVATION_QUANTITY = 1;
    private static final Integer UPDATED_RESERVATION_QUANTITY = 2;

    private static final ZonedDateTime DEFAULT_LAST_TIME_RESERVATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_TIME_RESERVATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ReservationHouseRestrictionsRepository reservationHouseRestrictionsRepository;

    @Autowired
    private ReservationHouseRestrictionsMapper reservationHouseRestrictionsMapper;

    @Autowired
    private ReservationHouseRestrictionsService reservationHouseRestrictionsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservationHouseRestrictionsMockMvc;

    private ReservationHouseRestrictions reservationHouseRestrictions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservationHouseRestrictionsResource reservationHouseRestrictionsResource = new ReservationHouseRestrictionsResource(reservationHouseRestrictionsService);
        this.restReservationHouseRestrictionsMockMvc = MockMvcBuilders.standaloneSetup(reservationHouseRestrictionsResource)
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
    public static ReservationHouseRestrictions createEntity(EntityManager em) {
        ReservationHouseRestrictions reservationHouseRestrictions = new ReservationHouseRestrictions()
            .reservationQuantity(DEFAULT_RESERVATION_QUANTITY)
            .lastTimeReservation(DEFAULT_LAST_TIME_RESERVATION);
        return reservationHouseRestrictions;
    }

    @Before
    public void initTest() {
        reservationHouseRestrictions = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservationHouseRestrictions() throws Exception {
        int databaseSizeBeforeCreate = reservationHouseRestrictionsRepository.findAll().size();

        // Create the ReservationHouseRestrictions
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO = reservationHouseRestrictionsMapper.toDto(reservationHouseRestrictions);
        restReservationHouseRestrictionsMockMvc.perform(post("/api/reservation-house-restrictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationHouseRestrictionsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReservationHouseRestrictions in the database
        List<ReservationHouseRestrictions> reservationHouseRestrictionsList = reservationHouseRestrictionsRepository.findAll();
        assertThat(reservationHouseRestrictionsList).hasSize(databaseSizeBeforeCreate + 1);
        ReservationHouseRestrictions testReservationHouseRestrictions = reservationHouseRestrictionsList.get(reservationHouseRestrictionsList.size() - 1);
        assertThat(testReservationHouseRestrictions.getReservationQuantity()).isEqualTo(DEFAULT_RESERVATION_QUANTITY);
        assertThat(testReservationHouseRestrictions.getLastTimeReservation()).isEqualTo(DEFAULT_LAST_TIME_RESERVATION);
    }

    @Test
    @Transactional
    public void createReservationHouseRestrictionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservationHouseRestrictionsRepository.findAll().size();

        // Create the ReservationHouseRestrictions with an existing ID
        reservationHouseRestrictions.setId(1L);
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO = reservationHouseRestrictionsMapper.toDto(reservationHouseRestrictions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationHouseRestrictionsMockMvc.perform(post("/api/reservation-house-restrictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationHouseRestrictionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReservationHouseRestrictions in the database
        List<ReservationHouseRestrictions> reservationHouseRestrictionsList = reservationHouseRestrictionsRepository.findAll();
        assertThat(reservationHouseRestrictionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReservationHouseRestrictions() throws Exception {
        // Initialize the database
        reservationHouseRestrictionsRepository.saveAndFlush(reservationHouseRestrictions);

        // Get all the reservationHouseRestrictionsList
        restReservationHouseRestrictionsMockMvc.perform(get("/api/reservation-house-restrictions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservationHouseRestrictions.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationQuantity").value(hasItem(DEFAULT_RESERVATION_QUANTITY)))
            .andExpect(jsonPath("$.[*].lastTimeReservation").value(hasItem(sameInstant(DEFAULT_LAST_TIME_RESERVATION))));
    }

    @Test
    @Transactional
    public void getReservationHouseRestrictions() throws Exception {
        // Initialize the database
        reservationHouseRestrictionsRepository.saveAndFlush(reservationHouseRestrictions);

        // Get the reservationHouseRestrictions
        restReservationHouseRestrictionsMockMvc.perform(get("/api/reservation-house-restrictions/{id}", reservationHouseRestrictions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservationHouseRestrictions.getId().intValue()))
            .andExpect(jsonPath("$.reservationQuantity").value(DEFAULT_RESERVATION_QUANTITY))
            .andExpect(jsonPath("$.lastTimeReservation").value(sameInstant(DEFAULT_LAST_TIME_RESERVATION)));
    }

    @Test
    @Transactional
    public void getNonExistingReservationHouseRestrictions() throws Exception {
        // Get the reservationHouseRestrictions
        restReservationHouseRestrictionsMockMvc.perform(get("/api/reservation-house-restrictions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservationHouseRestrictions() throws Exception {
        // Initialize the database
        reservationHouseRestrictionsRepository.saveAndFlush(reservationHouseRestrictions);
        int databaseSizeBeforeUpdate = reservationHouseRestrictionsRepository.findAll().size();

        // Update the reservationHouseRestrictions
        ReservationHouseRestrictions updatedReservationHouseRestrictions = reservationHouseRestrictionsRepository.findOne(reservationHouseRestrictions.getId());
        // Disconnect from session so that the updates on updatedReservationHouseRestrictions are not directly saved in db
        em.detach(updatedReservationHouseRestrictions);
        updatedReservationHouseRestrictions
            .reservationQuantity(UPDATED_RESERVATION_QUANTITY)
            .lastTimeReservation(UPDATED_LAST_TIME_RESERVATION);
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO = reservationHouseRestrictionsMapper.toDto(updatedReservationHouseRestrictions);

        restReservationHouseRestrictionsMockMvc.perform(put("/api/reservation-house-restrictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationHouseRestrictionsDTO)))
            .andExpect(status().isOk());

        // Validate the ReservationHouseRestrictions in the database
        List<ReservationHouseRestrictions> reservationHouseRestrictionsList = reservationHouseRestrictionsRepository.findAll();
        assertThat(reservationHouseRestrictionsList).hasSize(databaseSizeBeforeUpdate);
        ReservationHouseRestrictions testReservationHouseRestrictions = reservationHouseRestrictionsList.get(reservationHouseRestrictionsList.size() - 1);
        assertThat(testReservationHouseRestrictions.getReservationQuantity()).isEqualTo(UPDATED_RESERVATION_QUANTITY);
        assertThat(testReservationHouseRestrictions.getLastTimeReservation()).isEqualTo(UPDATED_LAST_TIME_RESERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingReservationHouseRestrictions() throws Exception {
        int databaseSizeBeforeUpdate = reservationHouseRestrictionsRepository.findAll().size();

        // Create the ReservationHouseRestrictions
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO = reservationHouseRestrictionsMapper.toDto(reservationHouseRestrictions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReservationHouseRestrictionsMockMvc.perform(put("/api/reservation-house-restrictions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationHouseRestrictionsDTO)))
            .andExpect(status().isCreated());

        // Validate the ReservationHouseRestrictions in the database
        List<ReservationHouseRestrictions> reservationHouseRestrictionsList = reservationHouseRestrictionsRepository.findAll();
        assertThat(reservationHouseRestrictionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReservationHouseRestrictions() throws Exception {
        // Initialize the database
        reservationHouseRestrictionsRepository.saveAndFlush(reservationHouseRestrictions);
        int databaseSizeBeforeDelete = reservationHouseRestrictionsRepository.findAll().size();

        // Get the reservationHouseRestrictions
        restReservationHouseRestrictionsMockMvc.perform(delete("/api/reservation-house-restrictions/{id}", reservationHouseRestrictions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReservationHouseRestrictions> reservationHouseRestrictionsList = reservationHouseRestrictionsRepository.findAll();
        assertThat(reservationHouseRestrictionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationHouseRestrictions.class);
        ReservationHouseRestrictions reservationHouseRestrictions1 = new ReservationHouseRestrictions();
        reservationHouseRestrictions1.setId(1L);
        ReservationHouseRestrictions reservationHouseRestrictions2 = new ReservationHouseRestrictions();
        reservationHouseRestrictions2.setId(reservationHouseRestrictions1.getId());
        assertThat(reservationHouseRestrictions1).isEqualTo(reservationHouseRestrictions2);
        reservationHouseRestrictions2.setId(2L);
        assertThat(reservationHouseRestrictions1).isNotEqualTo(reservationHouseRestrictions2);
        reservationHouseRestrictions1.setId(null);
        assertThat(reservationHouseRestrictions1).isNotEqualTo(reservationHouseRestrictions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationHouseRestrictionsDTO.class);
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO1 = new ReservationHouseRestrictionsDTO();
        reservationHouseRestrictionsDTO1.setId(1L);
        ReservationHouseRestrictionsDTO reservationHouseRestrictionsDTO2 = new ReservationHouseRestrictionsDTO();
        assertThat(reservationHouseRestrictionsDTO1).isNotEqualTo(reservationHouseRestrictionsDTO2);
        reservationHouseRestrictionsDTO2.setId(reservationHouseRestrictionsDTO1.getId());
        assertThat(reservationHouseRestrictionsDTO1).isEqualTo(reservationHouseRestrictionsDTO2);
        reservationHouseRestrictionsDTO2.setId(2L);
        assertThat(reservationHouseRestrictionsDTO1).isNotEqualTo(reservationHouseRestrictionsDTO2);
        reservationHouseRestrictionsDTO1.setId(null);
        assertThat(reservationHouseRestrictionsDTO1).isNotEqualTo(reservationHouseRestrictionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(reservationHouseRestrictionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(reservationHouseRestrictionsMapper.fromId(null)).isNull();
    }
}
