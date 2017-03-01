package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.HouseRepository;
import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
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
 * Test class for the HouseResource REST controller.
 *
 * @see HouseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HouseResourceIntTest {

    private static final String DEFAULT_HOUSENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSENUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ISDESOCUPATED = 0;
    private static final Integer UPDATED_ISDESOCUPATED = 1;

    private static final ZonedDateTime DEFAULT_DESOCUPATIONINITIALTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DESOCUPATIONINITIALTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DESOCUPATIONFINALTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DESOCUPATIONFINALTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SECURITY_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_KEY = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_KEY = "BBBBBBBBBB";

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseService houseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHouseMockMvc;

    private House house;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HouseResource houseResource = new HouseResource(houseService);
        this.restHouseMockMvc = MockMvcBuilders.standaloneSetup(houseResource)
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
    public static House createEntity(EntityManager em) {
        House house = new House()
                .housenumber(DEFAULT_HOUSENUMBER)
                .extension(DEFAULT_EXTENSION)
                .isdesocupated(DEFAULT_ISDESOCUPATED)
                .desocupationinitialtime(DEFAULT_DESOCUPATIONINITIALTIME)
                .desocupationfinaltime(DEFAULT_DESOCUPATIONFINALTIME)
                .securityKey(DEFAULT_SECURITY_KEY)
                .emergencyKey(DEFAULT_EMERGENCY_KEY);
        return house;
    }

    @Before
    public void initTest() {
        house = createEntity(em);
    }

    @Test
    @Transactional
    public void createHouse() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // Create the House
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);

        restHouseMockMvc.perform(post("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isCreated());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeCreate + 1);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getHousenumber()).isEqualTo(DEFAULT_HOUSENUMBER);
        assertThat(testHouse.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testHouse.getIsdesocupated()).isEqualTo(DEFAULT_ISDESOCUPATED);
        assertThat(testHouse.getDesocupationinitialtime()).isEqualTo(DEFAULT_DESOCUPATIONINITIALTIME);
        assertThat(testHouse.getDesocupationfinaltime()).isEqualTo(DEFAULT_DESOCUPATIONFINALTIME);
        assertThat(testHouse.getSecurityKey()).isEqualTo(DEFAULT_SECURITY_KEY);
        assertThat(testHouse.getEmergencyKey()).isEqualTo(DEFAULT_EMERGENCY_KEY);
    }

    @Test
    @Transactional
    public void createHouseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // Create the House with an existing ID
        House existingHouse = new House();
        existingHouse.setId(1L);
        HouseDTO existingHouseDTO = houseMapper.houseToHouseDTO(existingHouse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseMockMvc.perform(post("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHouseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHousenumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setHousenumber(null);

        // Create the House, which fails.
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);

        restHouseMockMvc.perform(post("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isBadRequest());

        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsdesocupatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setIsdesocupated(null);

        // Create the House, which fails.
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);

        restHouseMockMvc.perform(post("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isBadRequest());

        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHouses() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houseList
        restHouseMockMvc.perform(get("/api/houses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
            .andExpect(jsonPath("$.[*].housenumber").value(hasItem(DEFAULT_HOUSENUMBER.toString())))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].isdesocupated").value(hasItem(DEFAULT_ISDESOCUPATED)))
            .andExpect(jsonPath("$.[*].desocupationinitialtime").value(hasItem(sameInstant(DEFAULT_DESOCUPATIONINITIALTIME))))
            .andExpect(jsonPath("$.[*].desocupationfinaltime").value(hasItem(sameInstant(DEFAULT_DESOCUPATIONFINALTIME))))
            .andExpect(jsonPath("$.[*].securityKey").value(hasItem(DEFAULT_SECURITY_KEY.toString())))
            .andExpect(jsonPath("$.[*].emergencyKey").value(hasItem(DEFAULT_EMERGENCY_KEY.toString())));
    }

    @Test
    @Transactional
    public void getHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(house.getId().intValue()))
            .andExpect(jsonPath("$.housenumber").value(DEFAULT_HOUSENUMBER.toString()))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION.toString()))
            .andExpect(jsonPath("$.isdesocupated").value(DEFAULT_ISDESOCUPATED))
            .andExpect(jsonPath("$.desocupationinitialtime").value(sameInstant(DEFAULT_DESOCUPATIONINITIALTIME)))
            .andExpect(jsonPath("$.desocupationfinaltime").value(sameInstant(DEFAULT_DESOCUPATIONFINALTIME)))
            .andExpect(jsonPath("$.securityKey").value(DEFAULT_SECURITY_KEY.toString()))
            .andExpect(jsonPath("$.emergencyKey").value(DEFAULT_EMERGENCY_KEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHouse() throws Exception {
        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house
        House updatedHouse = houseRepository.findOne(house.getId());
        updatedHouse
                .housenumber(UPDATED_HOUSENUMBER)
                .extension(UPDATED_EXTENSION)
                .isdesocupated(UPDATED_ISDESOCUPATED)
                .desocupationinitialtime(UPDATED_DESOCUPATIONINITIALTIME)
                .desocupationfinaltime(UPDATED_DESOCUPATIONFINALTIME)
                .securityKey(UPDATED_SECURITY_KEY)
                .emergencyKey(UPDATED_EMERGENCY_KEY);
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(updatedHouse);

        restHouseMockMvc.perform(put("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houseList.get(houseList.size() - 1);
        assertThat(testHouse.getHousenumber()).isEqualTo(UPDATED_HOUSENUMBER);
        assertThat(testHouse.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testHouse.getIsdesocupated()).isEqualTo(UPDATED_ISDESOCUPATED);
        assertThat(testHouse.getDesocupationinitialtime()).isEqualTo(UPDATED_DESOCUPATIONINITIALTIME);
        assertThat(testHouse.getDesocupationfinaltime()).isEqualTo(UPDATED_DESOCUPATIONFINALTIME);
        assertThat(testHouse.getSecurityKey()).isEqualTo(UPDATED_SECURITY_KEY);
        assertThat(testHouse.getEmergencyKey()).isEqualTo(UPDATED_EMERGENCY_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingHouse() throws Exception {
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Create the House
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHouseMockMvc.perform(put("/api/houses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseDTO)))
            .andExpect(status().isCreated());

        // Validate the House in the database
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);
        int databaseSizeBeforeDelete = houseRepository.findAll().size();

        // Get the house
        restHouseMockMvc.perform(delete("/api/houses/{id}", house.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<House> houseList = houseRepository.findAll();
        assertThat(houseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(House.class);
    }
}
