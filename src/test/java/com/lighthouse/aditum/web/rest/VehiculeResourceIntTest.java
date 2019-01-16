package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Vehicule;
import com.lighthouse.aditum.repository.VehiculeRepository;
import com.lighthouse.aditum.service.VehiculeService;
import com.lighthouse.aditum.service.dto.VehiculeDTO;
import com.lighthouse.aditum.service.mapper.VehiculeMapper;
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
 * Test class for the VehiculeResource REST controller.
 *
 * @see VehiculeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class VehiculeResourceIntTest {

    private static final String DEFAULT_LICENSEPLATE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSEPLATE = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENABLED = 0;
    private static final Integer UPDATED_ENABLED = 1;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private VehiculeMapper vehiculeMapper;

    @Autowired
    private VehiculeService vehiculeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVehiculeMockMvc;

    private Vehicule vehicule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VehiculeResource vehiculeResource = new VehiculeResource(vehiculeService);
        this.restVehiculeMockMvc = MockMvcBuilders.standaloneSetup(vehiculeResource)
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
    public static Vehicule createEntity(EntityManager em) {
        Vehicule vehicule = new Vehicule()
            .licenseplate(DEFAULT_LICENSEPLATE)
            .brand(DEFAULT_BRAND)
            .color(DEFAULT_COLOR)
            .enabled(DEFAULT_ENABLED)
            .type(DEFAULT_TYPE)
            .deleted(DEFAULT_DELETED);
        return vehicule;
    }

    @Before
    public void initTest() {
        vehicule = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicule() throws Exception {
        int databaseSizeBeforeCreate = vehiculeRepository.findAll().size();

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);
        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getLicenseplate()).isEqualTo(DEFAULT_LICENSEPLATE);
        assertThat(testVehicule.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testVehicule.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testVehicule.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testVehicule.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVehicule.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createVehiculeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehiculeRepository.findAll().size();

        // Create the Vehicule with an existing ID
        vehicule.setId(1L);
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLicenseplateIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setLicenseplate(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setBrand(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setColor(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc.perform(post("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVehicules() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        // Get all the vehiculeList
        restVehiculeMockMvc.perform(get("/api/vehicules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenseplate").value(hasItem(DEFAULT_LICENSEPLATE.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        // Get the vehicule
        restVehiculeMockMvc.perform(get("/api/vehicules/{id}", vehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vehicule.getId().intValue()))
            .andExpect(jsonPath("$.licenseplate").value(DEFAULT_LICENSEPLATE.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingVehicule() throws Exception {
        // Get the vehicule
        restVehiculeMockMvc.perform(get("/api/vehicules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Update the vehicule
        Vehicule updatedVehicule = vehiculeRepository.findOne(vehicule.getId());
        updatedVehicule
            .licenseplate(UPDATED_LICENSEPLATE)
            .brand(UPDATED_BRAND)
            .color(UPDATED_COLOR)
            .enabled(UPDATED_ENABLED)
            .type(UPDATED_TYPE)
            .deleted(UPDATED_DELETED);
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(updatedVehicule);

        restVehiculeMockMvc.perform(put("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isOk());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getLicenseplate()).isEqualTo(UPDATED_LICENSEPLATE);
        assertThat(testVehicule.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testVehicule.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testVehicule.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testVehicule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVehicule.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVehiculeMockMvc.perform(put("/api/vehicules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);
        int databaseSizeBeforeDelete = vehiculeRepository.findAll().size();

        // Get the vehicule
        restVehiculeMockMvc.perform(delete("/api/vehicules/{id}", vehicule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicule.class);
        Vehicule vehicule1 = new Vehicule();
        vehicule1.setId(1L);
        Vehicule vehicule2 = new Vehicule();
        vehicule2.setId(vehicule1.getId());
        assertThat(vehicule1).isEqualTo(vehicule2);
        vehicule2.setId(2L);
        assertThat(vehicule1).isNotEqualTo(vehicule2);
        vehicule1.setId(null);
        assertThat(vehicule1).isNotEqualTo(vehicule2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehiculeDTO.class);
        VehiculeDTO vehiculeDTO1 = new VehiculeDTO();
        vehiculeDTO1.setId(1L);
        VehiculeDTO vehiculeDTO2 = new VehiculeDTO();
        assertThat(vehiculeDTO1).isNotEqualTo(vehiculeDTO2);
        vehiculeDTO2.setId(vehiculeDTO1.getId());
        assertThat(vehiculeDTO1).isEqualTo(vehiculeDTO2);
        vehiculeDTO2.setId(2L);
        assertThat(vehiculeDTO1).isNotEqualTo(vehiculeDTO2);
        vehiculeDTO1.setId(null);
        assertThat(vehiculeDTO1).isNotEqualTo(vehiculeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vehiculeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vehiculeMapper.fromId(null)).isNull();
    }
}
