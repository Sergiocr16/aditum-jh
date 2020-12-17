package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.HistoricalDefaulter;
import com.lighthouse.aditum.repository.HistoricalDefaulterRepository;
import com.lighthouse.aditum.service.HistoricalDefaulterService;
import com.lighthouse.aditum.service.ScheduledTasks;
import com.lighthouse.aditum.service.dto.HistoricalDefaulterDTO;
import com.lighthouse.aditum.service.mapper.HistoricalDefaulterMapper;
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
 * Test class for the HistoricalDefaulterResource REST controller.
 *
 * @see HistoricalDefaulterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HistoricalDefaulterResourceIntTest {

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CATEGORIES = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIES = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSENUMBER = "BBBBBBBBBB";

    @Autowired
    private HistoricalDefaulterRepository historicalDefaulterRepository;

    @Autowired
    private HistoricalDefaulterMapper historicalDefaulterMapper;

    @Autowired
    private HistoricalDefaulterService historicalDefaulterService;

    @Autowired
    private ScheduledTasks scheduledTasks;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHistoricalDefaulterMockMvc;

    private HistoricalDefaulter historicalDefaulter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HistoricalDefaulterResource historicalDefaulterResource = new HistoricalDefaulterResource(scheduledTasks,historicalDefaulterService);
        this.restHistoricalDefaulterMockMvc = MockMvcBuilders.standaloneSetup(historicalDefaulterResource)
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
    public static HistoricalDefaulter createEntity(EntityManager em) {
        HistoricalDefaulter historicalDefaulter = new HistoricalDefaulter()
            .total(DEFAULT_TOTAL)
            .date(DEFAULT_DATE)
            .categories(DEFAULT_CATEGORIES)
            .housenumber(DEFAULT_HOUSENUMBER);
        return historicalDefaulter;
    }

    @Before
    public void initTest() {
        historicalDefaulter = createEntity(em);
    }

    @Test
    @Transactional
    public void createHistoricalDefaulter() throws Exception {
        int databaseSizeBeforeCreate = historicalDefaulterRepository.findAll().size();

        // Create the HistoricalDefaulter
        HistoricalDefaulterDTO historicalDefaulterDTO = historicalDefaulterMapper.toDto(historicalDefaulter);
        restHistoricalDefaulterMockMvc.perform(post("/api/historical-defaulters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalDefaulter in the database
        List<HistoricalDefaulter> historicalDefaulterList = historicalDefaulterRepository.findAll();
        assertThat(historicalDefaulterList).hasSize(databaseSizeBeforeCreate + 1);
        HistoricalDefaulter testHistoricalDefaulter = historicalDefaulterList.get(historicalDefaulterList.size() - 1);
        assertThat(testHistoricalDefaulter.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testHistoricalDefaulter.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testHistoricalDefaulter.getCategories()).isEqualTo(DEFAULT_CATEGORIES);
        assertThat(testHistoricalDefaulter.getHousenumber()).isEqualTo(DEFAULT_HOUSENUMBER);
    }

    @Test
    @Transactional
    public void createHistoricalDefaulterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = historicalDefaulterRepository.findAll().size();

        // Create the HistoricalDefaulter with an existing ID
        historicalDefaulter.setId(1L);
        HistoricalDefaulterDTO historicalDefaulterDTO = historicalDefaulterMapper.toDto(historicalDefaulter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricalDefaulterMockMvc.perform(post("/api/historical-defaulters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoricalDefaulter in the database
        List<HistoricalDefaulter> historicalDefaulterList = historicalDefaulterRepository.findAll();
        assertThat(historicalDefaulterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHistoricalDefaulters() throws Exception {
        // Initialize the database
        historicalDefaulterRepository.saveAndFlush(historicalDefaulter);

        // Get all the historicalDefaulterList
        restHistoricalDefaulterMockMvc.perform(get("/api/historical-defaulters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicalDefaulter.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].categories").value(hasItem(DEFAULT_CATEGORIES.toString())))
            .andExpect(jsonPath("$.[*].housenumber").value(hasItem(DEFAULT_HOUSENUMBER.toString())));
    }

    @Test
    @Transactional
    public void getHistoricalDefaulter() throws Exception {
        // Initialize the database
        historicalDefaulterRepository.saveAndFlush(historicalDefaulter);

        // Get the historicalDefaulter
        restHistoricalDefaulterMockMvc.perform(get("/api/historical-defaulters/{id}", historicalDefaulter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(historicalDefaulter.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.categories").value(DEFAULT_CATEGORIES.toString()))
            .andExpect(jsonPath("$.housenumber").value(DEFAULT_HOUSENUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHistoricalDefaulter() throws Exception {
        // Get the historicalDefaulter
        restHistoricalDefaulterMockMvc.perform(get("/api/historical-defaulters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistoricalDefaulter() throws Exception {
        // Initialize the database
        historicalDefaulterRepository.saveAndFlush(historicalDefaulter);
        int databaseSizeBeforeUpdate = historicalDefaulterRepository.findAll().size();

        // Update the historicalDefaulter
        HistoricalDefaulter updatedHistoricalDefaulter = historicalDefaulterRepository.findOne(historicalDefaulter.getId());
        // Disconnect from session so that the updates on updatedHistoricalDefaulter are not directly saved in db
        em.detach(updatedHistoricalDefaulter);
        updatedHistoricalDefaulter
            .total(UPDATED_TOTAL)
            .date(UPDATED_DATE)
            .categories(UPDATED_CATEGORIES)
            .housenumber(UPDATED_HOUSENUMBER);
        HistoricalDefaulterDTO historicalDefaulterDTO = historicalDefaulterMapper.toDto(updatedHistoricalDefaulter);

        restHistoricalDefaulterMockMvc.perform(put("/api/historical-defaulters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterDTO)))
            .andExpect(status().isOk());

        // Validate the HistoricalDefaulter in the database
        List<HistoricalDefaulter> historicalDefaulterList = historicalDefaulterRepository.findAll();
        assertThat(historicalDefaulterList).hasSize(databaseSizeBeforeUpdate);
        HistoricalDefaulter testHistoricalDefaulter = historicalDefaulterList.get(historicalDefaulterList.size() - 1);
        assertThat(testHistoricalDefaulter.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testHistoricalDefaulter.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testHistoricalDefaulter.getCategories()).isEqualTo(UPDATED_CATEGORIES);
        assertThat(testHistoricalDefaulter.getHousenumber()).isEqualTo(UPDATED_HOUSENUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingHistoricalDefaulter() throws Exception {
        int databaseSizeBeforeUpdate = historicalDefaulterRepository.findAll().size();

        // Create the HistoricalDefaulter
        HistoricalDefaulterDTO historicalDefaulterDTO = historicalDefaulterMapper.toDto(historicalDefaulter);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHistoricalDefaulterMockMvc.perform(put("/api/historical-defaulters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(historicalDefaulterDTO)))
            .andExpect(status().isCreated());

        // Validate the HistoricalDefaulter in the database
        List<HistoricalDefaulter> historicalDefaulterList = historicalDefaulterRepository.findAll();
        assertThat(historicalDefaulterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHistoricalDefaulter() throws Exception {
        // Initialize the database
        historicalDefaulterRepository.saveAndFlush(historicalDefaulter);
        int databaseSizeBeforeDelete = historicalDefaulterRepository.findAll().size();

        // Get the historicalDefaulter
        restHistoricalDefaulterMockMvc.perform(delete("/api/historical-defaulters/{id}", historicalDefaulter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HistoricalDefaulter> historicalDefaulterList = historicalDefaulterRepository.findAll();
        assertThat(historicalDefaulterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalDefaulter.class);
        HistoricalDefaulter historicalDefaulter1 = new HistoricalDefaulter();
        historicalDefaulter1.setId(1L);
        HistoricalDefaulter historicalDefaulter2 = new HistoricalDefaulter();
        historicalDefaulter2.setId(historicalDefaulter1.getId());
        assertThat(historicalDefaulter1).isEqualTo(historicalDefaulter2);
        historicalDefaulter2.setId(2L);
        assertThat(historicalDefaulter1).isNotEqualTo(historicalDefaulter2);
        historicalDefaulter1.setId(null);
        assertThat(historicalDefaulter1).isNotEqualTo(historicalDefaulter2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricalDefaulterDTO.class);
        HistoricalDefaulterDTO historicalDefaulterDTO1 = new HistoricalDefaulterDTO();
        historicalDefaulterDTO1.setId(1L);
        HistoricalDefaulterDTO historicalDefaulterDTO2 = new HistoricalDefaulterDTO();
        assertThat(historicalDefaulterDTO1).isNotEqualTo(historicalDefaulterDTO2);
        historicalDefaulterDTO2.setId(historicalDefaulterDTO1.getId());
        assertThat(historicalDefaulterDTO1).isEqualTo(historicalDefaulterDTO2);
        historicalDefaulterDTO2.setId(2L);
        assertThat(historicalDefaulterDTO1).isNotEqualTo(historicalDefaulterDTO2);
        historicalDefaulterDTO1.setId(null);
        assertThat(historicalDefaulterDTO1).isNotEqualTo(historicalDefaulterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(historicalDefaulterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(historicalDefaulterMapper.fromId(null)).isNull();
    }
}
