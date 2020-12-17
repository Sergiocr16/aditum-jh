package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.HistoricalPositive;
import com.lighthouse.aditum.repository.HistoricalPositiveRepository;
import com.lighthouse.aditum.service.HistoricalPositiveService;
import com.lighthouse.aditum.service.dto.HistoricalPositiveDTO;
import com.lighthouse.aditum.service.mapper.HistoricalPositiveMapper;
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
 * Test class for the HistoricalPositiveResource REST controller.
 *
 * @see HistoricalPositiveResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HistoricalPositiveResourceIntTest {

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSENUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private HistoricalPositiveRepository historicalPositiveRepository;

    @Autowired
    private HistoricalPositiveMapper historicalPositiveMapper;

    @Autowired
    private HistoricalPositiveService historicalPositiveService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoricalPositiveMockMvc;

    private HistoricalPositive historicalPositive;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoricalPositiveResource historicalPositiveResource = new HistoricalPositiveResource(historicalPositiveService);
        this.restHistoricalPositiveMockMvc = MockMvcBuilders.standaloneSetup(historicalPositiveResource)
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
    public static HistoricalPositive createEntity(EntityManager em) {
        HistoricalPositive historicalPositive = new HistoricalPositive()
            .total(DEFAULT_TOTAL)
            .housenumber(DEFAULT_HOUSENUMBER)
            .date(DEFAULT_DATE);
        return historicalPositive;
    }

    @Before
    public void initTest() {
        historicalPositive = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoricalPositive() throws Exception {
        int databaseSizeBeforeCreate = historicalPositiveRepository.findAll().size();

        // Create the HistoricalPositive
        HistoricalPositiveDTO historicalPositiveDTO = historicalPositiveMapper.toDto(historicalPositive);
        restHistoricalPositiveMockMvc.perform(post("/api/historical-positives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalPositiveDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalPositive in the database
        List<HistoricalPositive> historicalPositiveList = historicalPositiveRepository.findAll();
        assertThat(historicalPositiveList).hasSize(databaseSizeBeforeCreate + 1);
        HistoricalPositive testHistoricalPositive = historicalPositiveList.get(historicalPositiveList.size() - 1);
        assertThat(testHistoricalPositive.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testHistoricalPositive.getHousenumber()).isEqualTo(DEFAULT_HOUSENUMBER);
        assertThat(testHistoricalPositive.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createHistoricalPositiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historicalPositiveRepository.findAll().size();

        // Create the HistoricalPositive with an existing ID
        historicalPositive.setId(1L);
        HistoricalPositiveDTO historicalPositiveDTO = historicalPositiveMapper.toDto(historicalPositive);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricalPositiveMockMvc.perform(post("/api/historical-positives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalPositiveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoricalPositive in the database
        List<HistoricalPositive> historicalPositiveList = historicalPositiveRepository.findAll();
        assertThat(historicalPositiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHistoricalPositives() throws Exception {
        // Initialize the database
        historicalPositiveRepository.saveAndFlush(historicalPositive);

        // Get all the historicalPositiveList
        restHistoricalPositiveMockMvc.perform(get("/api/historical-positives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicalPositive.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.toString())))
            .andExpect(jsonPath("$.[*].housenumber").value(hasItem(DEFAULT_HOUSENUMBER.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    @Test
    @Transactional
    public void getHistoricalPositive() throws Exception {
        // Initialize the database
        historicalPositiveRepository.saveAndFlush(historicalPositive);

        // Get the historicalPositive
        restHistoricalPositiveMockMvc.perform(get("/api/historical-positives/{id}", historicalPositive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historicalPositive.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.toString()))
            .andExpect(jsonPath("$.housenumber").value(DEFAULT_HOUSENUMBER.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingHistoricalPositive() throws Exception {
        // Get the historicalPositive
        restHistoricalPositiveMockMvc.perform(get("/api/historical-positives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoricalPositive() throws Exception {
        // Initialize the database
        historicalPositiveRepository.saveAndFlush(historicalPositive);
        int databaseSizeBeforeUpdate = historicalPositiveRepository.findAll().size();

        // Update the historicalPositive
        HistoricalPositive updatedHistoricalPositive = historicalPositiveRepository.findOne(historicalPositive.getId());
        // Disconnect from session so that the updates on updatedHistoricalPositive are not directly saved in db
        em.detach(updatedHistoricalPositive);
        updatedHistoricalPositive
            .total(UPDATED_TOTAL)
            .housenumber(UPDATED_HOUSENUMBER)
            .date(UPDATED_DATE);
        HistoricalPositiveDTO historicalPositiveDTO = historicalPositiveMapper.toDto(updatedHistoricalPositive);

        restHistoricalPositiveMockMvc.perform(put("/api/historical-positives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalPositiveDTO)))
            .andExpect(status().isOk());

        // Validate the HistoricalPositive in the database
        List<HistoricalPositive> historicalPositiveList = historicalPositiveRepository.findAll();
        assertThat(historicalPositiveList).hasSize(databaseSizeBeforeUpdate);
        HistoricalPositive testHistoricalPositive = historicalPositiveList.get(historicalPositiveList.size() - 1);
        assertThat(testHistoricalPositive.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testHistoricalPositive.getHousenumber()).isEqualTo(UPDATED_HOUSENUMBER);
        assertThat(testHistoricalPositive.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoricalPositive() throws Exception {
        int databaseSizeBeforeUpdate = historicalPositiveRepository.findAll().size();

        // Create the HistoricalPositive
        HistoricalPositiveDTO historicalPositiveDTO = historicalPositiveMapper.toDto(historicalPositive);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoricalPositiveMockMvc.perform(put("/api/historical-positives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalPositiveDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalPositive in the database
        List<HistoricalPositive> historicalPositiveList = historicalPositiveRepository.findAll();
        assertThat(historicalPositiveList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHistoricalPositive() throws Exception {
        // Initialize the database
        historicalPositiveRepository.saveAndFlush(historicalPositive);
        int databaseSizeBeforeDelete = historicalPositiveRepository.findAll().size();

        // Get the historicalPositive
        restHistoricalPositiveMockMvc.perform(delete("/api/historical-positives/{id}", historicalPositive.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HistoricalPositive> historicalPositiveList = historicalPositiveRepository.findAll();
        assertThat(historicalPositiveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalPositive.class);
        HistoricalPositive historicalPositive1 = new HistoricalPositive();
        historicalPositive1.setId(1L);
        HistoricalPositive historicalPositive2 = new HistoricalPositive();
        historicalPositive2.setId(historicalPositive1.getId());
        assertThat(historicalPositive1).isEqualTo(historicalPositive2);
        historicalPositive2.setId(2L);
        assertThat(historicalPositive1).isNotEqualTo(historicalPositive2);
        historicalPositive1.setId(null);
        assertThat(historicalPositive1).isNotEqualTo(historicalPositive2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalPositiveDTO.class);
        HistoricalPositiveDTO historicalPositiveDTO1 = new HistoricalPositiveDTO();
        historicalPositiveDTO1.setId(1L);
        HistoricalPositiveDTO historicalPositiveDTO2 = new HistoricalPositiveDTO();
        assertThat(historicalPositiveDTO1).isNotEqualTo(historicalPositiveDTO2);
        historicalPositiveDTO2.setId(historicalPositiveDTO1.getId());
        assertThat(historicalPositiveDTO1).isEqualTo(historicalPositiveDTO2);
        historicalPositiveDTO2.setId(2L);
        assertThat(historicalPositiveDTO1).isNotEqualTo(historicalPositiveDTO2);
        historicalPositiveDTO1.setId(null);
        assertThat(historicalPositiveDTO1).isNotEqualTo(historicalPositiveDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historicalPositiveMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historicalPositiveMapper.fromId(null)).isNull();
    }
}
