package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Emergency;
import com.lighthouse.aditum.repository.EmergencyRepository;
import com.lighthouse.aditum.service.EmergencyService;
import com.lighthouse.aditum.service.dto.EmergencyDTO;
import com.lighthouse.aditum.service.mapper.EmergencyMapper;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EmergencyResource REST controller.
 *
 * @see EmergencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class EmergencyResourceIntTest {

    private static final Integer DEFAULT_IS_ATTENDED = 0;
    private static final Integer UPDATED_IS_ATTENDED = 1;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private EmergencyRepository emergencyRepository;

    @Autowired
    private EmergencyMapper emergencyMapper;

    @Autowired
    private EmergencyService emergencyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmergencyMockMvc;

    private Emergency emergency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmergencyResource emergencyResource = new EmergencyResource(emergencyService);
        this.restEmergencyMockMvc = MockMvcBuilders.standaloneSetup(emergencyResource)
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
    public static Emergency createEntity(EntityManager em) {
        Emergency emergency = new Emergency()
                .isAttended(DEFAULT_IS_ATTENDED)
                .observation(DEFAULT_OBSERVATION);
        return emergency;
    }

    @Before
    public void initTest() {
        emergency = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmergency() throws Exception {
        int databaseSizeBeforeCreate = emergencyRepository.findAll().size();

        // Create the Emergency
        EmergencyDTO emergencyDTO = emergencyMapper.emergencyToEmergencyDTO(emergency);

        restEmergencyMockMvc.perform(post("/api/emergencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emergencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Emergency in the database
        List<Emergency> emergencyList = emergencyRepository.findAll();
        assertThat(emergencyList).hasSize(databaseSizeBeforeCreate + 1);
        Emergency testEmergency = emergencyList.get(emergencyList.size() - 1);
        assertThat(testEmergency.getIsAttended()).isEqualTo(DEFAULT_IS_ATTENDED);
        assertThat(testEmergency.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createEmergencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emergencyRepository.findAll().size();

        // Create the Emergency with an existing ID
        Emergency existingEmergency = new Emergency();
        existingEmergency.setId(1L);
        EmergencyDTO existingEmergencyDTO = emergencyMapper.emergencyToEmergencyDTO(existingEmergency);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmergencyMockMvc.perform(post("/api/emergencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEmergencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Emergency> emergencyList = emergencyRepository.findAll();
        assertThat(emergencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsAttendedIsRequired() throws Exception {
        int databaseSizeBeforeTest = emergencyRepository.findAll().size();
        // set the field null
        emergency.setIsAttended(null);

        // Create the Emergency, which fails.
        EmergencyDTO emergencyDTO = emergencyMapper.emergencyToEmergencyDTO(emergency);

        restEmergencyMockMvc.perform(post("/api/emergencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emergencyDTO)))
            .andExpect(status().isBadRequest());

        List<Emergency> emergencyList = emergencyRepository.findAll();
        assertThat(emergencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmergencies() throws Exception {
        // Initialize the database
        emergencyRepository.saveAndFlush(emergency);

        // Get all the emergencyList
        restEmergencyMockMvc.perform(get("/api/emergencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emergency.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAttended").value(hasItem(DEFAULT_IS_ATTENDED)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }

    @Test
    @Transactional
    public void getEmergency() throws Exception {
        // Initialize the database
        emergencyRepository.saveAndFlush(emergency);

        // Get the emergency
        restEmergencyMockMvc.perform(get("/api/emergencies/{id}", emergency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emergency.getId().intValue()))
            .andExpect(jsonPath("$.isAttended").value(DEFAULT_IS_ATTENDED))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmergency() throws Exception {
        // Get the emergency
        restEmergencyMockMvc.perform(get("/api/emergencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmergency() throws Exception {
        // Initialize the database
        emergencyRepository.saveAndFlush(emergency);
        int databaseSizeBeforeUpdate = emergencyRepository.findAll().size();

        // Update the emergency
        Emergency updatedEmergency = emergencyRepository.findOne(emergency.getId());
        updatedEmergency
                .isAttended(UPDATED_IS_ATTENDED)
                .observation(UPDATED_OBSERVATION);
        EmergencyDTO emergencyDTO = emergencyMapper.emergencyToEmergencyDTO(updatedEmergency);

        restEmergencyMockMvc.perform(put("/api/emergencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emergencyDTO)))
            .andExpect(status().isOk());

        // Validate the Emergency in the database
        List<Emergency> emergencyList = emergencyRepository.findAll();
        assertThat(emergencyList).hasSize(databaseSizeBeforeUpdate);
        Emergency testEmergency = emergencyList.get(emergencyList.size() - 1);
        assertThat(testEmergency.getIsAttended()).isEqualTo(UPDATED_IS_ATTENDED);
        assertThat(testEmergency.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingEmergency() throws Exception {
        int databaseSizeBeforeUpdate = emergencyRepository.findAll().size();

        // Create the Emergency
        EmergencyDTO emergencyDTO = emergencyMapper.emergencyToEmergencyDTO(emergency);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmergencyMockMvc.perform(put("/api/emergencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emergencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Emergency in the database
        List<Emergency> emergencyList = emergencyRepository.findAll();
        assertThat(emergencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmergency() throws Exception {
        // Initialize the database
        emergencyRepository.saveAndFlush(emergency);
        int databaseSizeBeforeDelete = emergencyRepository.findAll().size();

        // Get the emergency
        restEmergencyMockMvc.perform(delete("/api/emergencies/{id}", emergency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Emergency> emergencyList = emergencyRepository.findAll();
        assertThat(emergencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emergency.class);
    }
}
