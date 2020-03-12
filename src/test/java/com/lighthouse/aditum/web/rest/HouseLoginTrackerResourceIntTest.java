package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.HouseLoginTracker;
import com.lighthouse.aditum.repository.HouseLoginTrackerRepository;
import com.lighthouse.aditum.service.HouseLoginTrackerService;
import com.lighthouse.aditum.service.dto.HouseLoginTrackerDTO;
import com.lighthouse.aditum.service.mapper.HouseLoginTrackerMapper;
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
 * Test class for the HouseLoginTrackerResource REST controller.
 *
 * @see HouseLoginTrackerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HouseLoginTrackerResourceIntTest {

    private static final ZonedDateTime DEFAULT_LAST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    @Autowired
    private HouseLoginTrackerRepository houseLoginTrackerRepository;

    @Autowired
    private HouseLoginTrackerMapper houseLoginTrackerMapper;

    @Autowired
    private HouseLoginTrackerService houseLoginTrackerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHouseLoginTrackerMockMvc;

    private HouseLoginTracker houseLoginTracker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HouseLoginTrackerResource houseLoginTrackerResource = new HouseLoginTrackerResource(houseLoginTrackerService);
        this.restHouseLoginTrackerMockMvc = MockMvcBuilders.standaloneSetup(houseLoginTrackerResource)
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
    public static HouseLoginTracker createEntity(EntityManager em) {
        HouseLoginTracker houseLoginTracker = new HouseLoginTracker()
            .lastTime(DEFAULT_LAST_TIME)
            .user(DEFAULT_USER);
        return houseLoginTracker;
    }

    @Before
    public void initTest() {
        houseLoginTracker = createEntity(em);
    }

    @Test
    @Transactional
    public void createHouseLoginTracker() throws Exception {
        int databaseSizeBeforeCreate = houseLoginTrackerRepository.findAll().size();

        // Create the HouseLoginTracker
        HouseLoginTrackerDTO houseLoginTrackerDTO = houseLoginTrackerMapper.toDto(houseLoginTracker);
        restHouseLoginTrackerMockMvc.perform(post("/api/house-login-trackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseLoginTrackerDTO)))
            .andExpect(status().isCreated());

        // Validate the HouseLoginTracker in the database
        List<HouseLoginTracker> houseLoginTrackerList = houseLoginTrackerRepository.findAll();
        assertThat(houseLoginTrackerList).hasSize(databaseSizeBeforeCreate + 1);
        HouseLoginTracker testHouseLoginTracker = houseLoginTrackerList.get(houseLoginTrackerList.size() - 1);
        assertThat(testHouseLoginTracker.getLastTime()).isEqualTo(DEFAULT_LAST_TIME);
        assertThat(testHouseLoginTracker.getUser()).isEqualTo(DEFAULT_USER);
    }

    @Test
    @Transactional
    public void createHouseLoginTrackerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = houseLoginTrackerRepository.findAll().size();

        // Create the HouseLoginTracker with an existing ID
        houseLoginTracker.setId(1L);
        HouseLoginTrackerDTO houseLoginTrackerDTO = houseLoginTrackerMapper.toDto(houseLoginTracker);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseLoginTrackerMockMvc.perform(post("/api/house-login-trackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseLoginTrackerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HouseLoginTracker in the database
        List<HouseLoginTracker> houseLoginTrackerList = houseLoginTrackerRepository.findAll();
        assertThat(houseLoginTrackerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHouseLoginTrackers() throws Exception {
        // Initialize the database
        houseLoginTrackerRepository.saveAndFlush(houseLoginTracker);

        // Get all the houseLoginTrackerList
        restHouseLoginTrackerMockMvc.perform(get("/api/house-login-trackers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseLoginTracker.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastTime").value(hasItem(sameInstant(DEFAULT_LAST_TIME))))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())));
    }

    @Test
    @Transactional
    public void getHouseLoginTracker() throws Exception {
        // Initialize the database
        houseLoginTrackerRepository.saveAndFlush(houseLoginTracker);

        // Get the houseLoginTracker
        restHouseLoginTrackerMockMvc.perform(get("/api/house-login-trackers/{id}", houseLoginTracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(houseLoginTracker.getId().intValue()))
            .andExpect(jsonPath("$.lastTime").value(sameInstant(DEFAULT_LAST_TIME)))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHouseLoginTracker() throws Exception {
        // Get the houseLoginTracker
        restHouseLoginTrackerMockMvc.perform(get("/api/house-login-trackers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouseLoginTracker() throws Exception {
        // Initialize the database
        houseLoginTrackerRepository.saveAndFlush(houseLoginTracker);
        int databaseSizeBeforeUpdate = houseLoginTrackerRepository.findAll().size();

        // Update the houseLoginTracker
        HouseLoginTracker updatedHouseLoginTracker = houseLoginTrackerRepository.findOne(houseLoginTracker.getId());
        // Disconnect from session so that the updates on updatedHouseLoginTracker are not directly saved in db
        em.detach(updatedHouseLoginTracker);
        updatedHouseLoginTracker
            .lastTime(UPDATED_LAST_TIME)
            .user(UPDATED_USER);
        HouseLoginTrackerDTO houseLoginTrackerDTO = houseLoginTrackerMapper.toDto(updatedHouseLoginTracker);

        restHouseLoginTrackerMockMvc.perform(put("/api/house-login-trackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseLoginTrackerDTO)))
            .andExpect(status().isOk());

        // Validate the HouseLoginTracker in the database
        List<HouseLoginTracker> houseLoginTrackerList = houseLoginTrackerRepository.findAll();
        assertThat(houseLoginTrackerList).hasSize(databaseSizeBeforeUpdate);
        HouseLoginTracker testHouseLoginTracker = houseLoginTrackerList.get(houseLoginTrackerList.size() - 1);
        assertThat(testHouseLoginTracker.getLastTime()).isEqualTo(UPDATED_LAST_TIME);
        assertThat(testHouseLoginTracker.getUser()).isEqualTo(UPDATED_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingHouseLoginTracker() throws Exception {
        int databaseSizeBeforeUpdate = houseLoginTrackerRepository.findAll().size();

        // Create the HouseLoginTracker
        HouseLoginTrackerDTO houseLoginTrackerDTO = houseLoginTrackerMapper.toDto(houseLoginTracker);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHouseLoginTrackerMockMvc.perform(put("/api/house-login-trackers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseLoginTrackerDTO)))
            .andExpect(status().isCreated());

        // Validate the HouseLoginTracker in the database
        List<HouseLoginTracker> houseLoginTrackerList = houseLoginTrackerRepository.findAll();
        assertThat(houseLoginTrackerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHouseLoginTracker() throws Exception {
        // Initialize the database
        houseLoginTrackerRepository.saveAndFlush(houseLoginTracker);
        int databaseSizeBeforeDelete = houseLoginTrackerRepository.findAll().size();

        // Get the houseLoginTracker
        restHouseLoginTrackerMockMvc.perform(delete("/api/house-login-trackers/{id}", houseLoginTracker.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HouseLoginTracker> houseLoginTrackerList = houseLoginTrackerRepository.findAll();
        assertThat(houseLoginTrackerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseLoginTracker.class);
        HouseLoginTracker houseLoginTracker1 = new HouseLoginTracker();
        houseLoginTracker1.setId(1L);
        HouseLoginTracker houseLoginTracker2 = new HouseLoginTracker();
        houseLoginTracker2.setId(houseLoginTracker1.getId());
        assertThat(houseLoginTracker1).isEqualTo(houseLoginTracker2);
        houseLoginTracker2.setId(2L);
        assertThat(houseLoginTracker1).isNotEqualTo(houseLoginTracker2);
        houseLoginTracker1.setId(null);
        assertThat(houseLoginTracker1).isNotEqualTo(houseLoginTracker2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseLoginTrackerDTO.class);
        HouseLoginTrackerDTO houseLoginTrackerDTO1 = new HouseLoginTrackerDTO();
        houseLoginTrackerDTO1.setId(1L);
        HouseLoginTrackerDTO houseLoginTrackerDTO2 = new HouseLoginTrackerDTO();
        assertThat(houseLoginTrackerDTO1).isNotEqualTo(houseLoginTrackerDTO2);
        houseLoginTrackerDTO2.setId(houseLoginTrackerDTO1.getId());
        assertThat(houseLoginTrackerDTO1).isEqualTo(houseLoginTrackerDTO2);
        houseLoginTrackerDTO2.setId(2L);
        assertThat(houseLoginTrackerDTO1).isNotEqualTo(houseLoginTrackerDTO2);
        houseLoginTrackerDTO1.setId(null);
        assertThat(houseLoginTrackerDTO1).isNotEqualTo(houseLoginTrackerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(houseLoginTrackerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(houseLoginTrackerMapper.fromId(null)).isNull();
    }
}
