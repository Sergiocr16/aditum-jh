package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.WaterConsumption;
import com.lighthouse.aditum.repository.WaterConsumptionRepository;
import com.lighthouse.aditum.service.AdministrationConfigurationService;
import com.lighthouse.aditum.service.ChargeService;
import com.lighthouse.aditum.service.WaterConsumptionService;
import com.lighthouse.aditum.service.dto.WaterConsumptionDTO;
import com.lighthouse.aditum.service.mapper.WaterConsumptionMapper;
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
 * Test class for the WaterConsumptionResource REST controller.
 *
 * @see WaterConsumptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class WaterConsumptionResourceIntTest {

    private static final String DEFAULT_CONSUMPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONSUMPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RECORD_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECORD_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private WaterConsumptionRepository waterConsumptionRepository;

    @Autowired
    private WaterConsumptionMapper waterConsumptionMapper;

    @Autowired
    private WaterConsumptionService waterConsumptionService;

    @Autowired
    private AdministrationConfigurationService administrationConfigurationService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWaterConsumptionMockMvc;

    private WaterConsumption waterConsumption;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WaterConsumptionResource waterConsumptionResource = new WaterConsumptionResource(administrationConfigurationService, waterConsumptionService, chargeService);
        this.restWaterConsumptionMockMvc = MockMvcBuilders.standaloneSetup(waterConsumptionResource)
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
    public static WaterConsumption createEntity(EntityManager em) {
        WaterConsumption waterConsumption = new WaterConsumption()
            .consumption(DEFAULT_CONSUMPTION)
            .month(DEFAULT_MONTH)
            .recordDate(DEFAULT_RECORD_DATE)
            .status(DEFAULT_STATUS);
        return waterConsumption;
    }

    @Before
    public void initTest() {
        waterConsumption = createEntity(em);
    }

    @Test
    @Transactional
    public void createWaterConsumption() throws Exception {
        int databaseSizeBeforeCreate = waterConsumptionRepository.findAll().size();

        // Create the WaterConsumption
        WaterConsumptionDTO waterConsumptionDTO = waterConsumptionMapper.toDto(waterConsumption);
        restWaterConsumptionMockMvc.perform(post("/api/water-consumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waterConsumptionDTO)))
            .andExpect(status().isCreated());

        // Validate the WaterConsumption in the database
        List<WaterConsumption> waterConsumptionList = waterConsumptionRepository.findAll();
        assertThat(waterConsumptionList).hasSize(databaseSizeBeforeCreate + 1);
        WaterConsumption testWaterConsumption = waterConsumptionList.get(waterConsumptionList.size() - 1);
        assertThat(testWaterConsumption.getConsumption()).isEqualTo(DEFAULT_CONSUMPTION);
        assertThat(testWaterConsumption.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testWaterConsumption.getRecordDate()).isEqualTo(DEFAULT_RECORD_DATE);
        assertThat(testWaterConsumption.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createWaterConsumptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = waterConsumptionRepository.findAll().size();

        // Create the WaterConsumption with an existing ID
        waterConsumption.setId(1L);
        WaterConsumptionDTO waterConsumptionDTO = waterConsumptionMapper.toDto(waterConsumption);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWaterConsumptionMockMvc.perform(post("/api/water-consumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waterConsumptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WaterConsumption in the database
        List<WaterConsumption> waterConsumptionList = waterConsumptionRepository.findAll();
        assertThat(waterConsumptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWaterConsumptions() throws Exception {
        // Initialize the database
        waterConsumptionRepository.saveAndFlush(waterConsumption);

        // Get all the waterConsumptionList
        restWaterConsumptionMockMvc.perform(get("/api/water-consumptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waterConsumption.getId().intValue())))
            .andExpect(jsonPath("$.[*].consumption").value(hasItem(DEFAULT_CONSUMPTION.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].recordDate").value(hasItem(sameInstant(DEFAULT_RECORD_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getWaterConsumption() throws Exception {
        // Initialize the database
        waterConsumptionRepository.saveAndFlush(waterConsumption);

        // Get the waterConsumption
        restWaterConsumptionMockMvc.perform(get("/api/water-consumptions/{id}", waterConsumption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(waterConsumption.getId().intValue()))
            .andExpect(jsonPath("$.consumption").value(DEFAULT_CONSUMPTION.toString()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.recordDate").value(sameInstant(DEFAULT_RECORD_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingWaterConsumption() throws Exception {
        // Get the waterConsumption
        restWaterConsumptionMockMvc.perform(get("/api/water-consumptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWaterConsumption() throws Exception {
        // Initialize the database
        waterConsumptionRepository.saveAndFlush(waterConsumption);
        int databaseSizeBeforeUpdate = waterConsumptionRepository.findAll().size();

        // Update the waterConsumption
        WaterConsumption updatedWaterConsumption = waterConsumptionRepository.findOne(waterConsumption.getId());
        // Disconnect from session so that the updates on updatedWaterConsumption are not directly saved in db
        em.detach(updatedWaterConsumption);
        updatedWaterConsumption
            .consumption(UPDATED_CONSUMPTION)
            .month(UPDATED_MONTH)
            .recordDate(UPDATED_RECORD_DATE)
            .status(UPDATED_STATUS);
        WaterConsumptionDTO waterConsumptionDTO = waterConsumptionMapper.toDto(updatedWaterConsumption);

        restWaterConsumptionMockMvc.perform(put("/api/water-consumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waterConsumptionDTO)))
            .andExpect(status().isOk());

        // Validate the WaterConsumption in the database
        List<WaterConsumption> waterConsumptionList = waterConsumptionRepository.findAll();
        assertThat(waterConsumptionList).hasSize(databaseSizeBeforeUpdate);
        WaterConsumption testWaterConsumption = waterConsumptionList.get(waterConsumptionList.size() - 1);
        assertThat(testWaterConsumption.getConsumption()).isEqualTo(UPDATED_CONSUMPTION);
        assertThat(testWaterConsumption.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testWaterConsumption.getRecordDate()).isEqualTo(UPDATED_RECORD_DATE);
        assertThat(testWaterConsumption.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingWaterConsumption() throws Exception {
        int databaseSizeBeforeUpdate = waterConsumptionRepository.findAll().size();

        // Create the WaterConsumption
        WaterConsumptionDTO waterConsumptionDTO = waterConsumptionMapper.toDto(waterConsumption);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWaterConsumptionMockMvc.perform(put("/api/water-consumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(waterConsumptionDTO)))
            .andExpect(status().isCreated());

        // Validate the WaterConsumption in the database
        List<WaterConsumption> waterConsumptionList = waterConsumptionRepository.findAll();
        assertThat(waterConsumptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWaterConsumption() throws Exception {
        // Initialize the database
        waterConsumptionRepository.saveAndFlush(waterConsumption);
        int databaseSizeBeforeDelete = waterConsumptionRepository.findAll().size();

        // Get the waterConsumption
        restWaterConsumptionMockMvc.perform(delete("/api/water-consumptions/{id}", waterConsumption.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WaterConsumption> waterConsumptionList = waterConsumptionRepository.findAll();
        assertThat(waterConsumptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterConsumption.class);
        WaterConsumption waterConsumption1 = new WaterConsumption();
        waterConsumption1.setId(1L);
        WaterConsumption waterConsumption2 = new WaterConsumption();
        waterConsumption2.setId(waterConsumption1.getId());
        assertThat(waterConsumption1).isEqualTo(waterConsumption2);
        waterConsumption2.setId(2L);
        assertThat(waterConsumption1).isNotEqualTo(waterConsumption2);
        waterConsumption1.setId(null);
        assertThat(waterConsumption1).isNotEqualTo(waterConsumption2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WaterConsumptionDTO.class);
        WaterConsumptionDTO waterConsumptionDTO1 = new WaterConsumptionDTO();
        waterConsumptionDTO1.setId(1L);
        WaterConsumptionDTO waterConsumptionDTO2 = new WaterConsumptionDTO();
        assertThat(waterConsumptionDTO1).isNotEqualTo(waterConsumptionDTO2);
        waterConsumptionDTO2.setId(waterConsumptionDTO1.getId());
        assertThat(waterConsumptionDTO1).isEqualTo(waterConsumptionDTO2);
        waterConsumptionDTO2.setId(2L);
        assertThat(waterConsumptionDTO1).isNotEqualTo(waterConsumptionDTO2);
        waterConsumptionDTO1.setId(null);
        assertThat(waterConsumptionDTO1).isNotEqualTo(waterConsumptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(waterConsumptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(waterConsumptionMapper.fromId(null)).isNull();
    }
}
