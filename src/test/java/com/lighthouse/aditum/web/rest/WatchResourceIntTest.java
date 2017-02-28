package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Watch;
import com.lighthouse.aditum.repository.WatchRepository;
import com.lighthouse.aditum.service.WatchService;
import com.lighthouse.aditum.service.dto.WatchDTO;
import com.lighthouse.aditum.service.mapper.WatchMapper;
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
 * Test class for the WatchResource REST controller.
 *
 * @see WatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class WatchResourceIntTest {

    private static final ZonedDateTime DEFAULT_INITIALTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INITIALTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FINALTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FINALTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_RESPONSABLEOFFICER = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLEOFFICER = "BBBBBBBBBB";

    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private WatchMapper watchMapper;

    @Autowired
    private WatchService watchService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWatchMockMvc;

    private Watch watch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WatchResource watchResource = new WatchResource(watchService);
        this.restWatchMockMvc = MockMvcBuilders.standaloneSetup(watchResource)
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
    public static Watch createEntity(EntityManager em) {
        Watch watch = new Watch()
                .initialtime(DEFAULT_INITIALTIME)
                .finaltime(DEFAULT_FINALTIME)
                .responsableofficer(DEFAULT_RESPONSABLEOFFICER);
        return watch;
    }

    @Before
    public void initTest() {
        watch = createEntity(em);
    }

    @Test
    @Transactional
    public void createWatch() throws Exception {
        int databaseSizeBeforeCreate = watchRepository.findAll().size();

        // Create the Watch
        WatchDTO watchDTO = watchMapper.watchToWatchDTO(watch);

        restWatchMockMvc.perform(post("/api/watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchDTO)))
            .andExpect(status().isCreated());

        // Validate the Watch in the database
        List<Watch> watchList = watchRepository.findAll();
        assertThat(watchList).hasSize(databaseSizeBeforeCreate + 1);
        Watch testWatch = watchList.get(watchList.size() - 1);
        assertThat(testWatch.getInitialtime()).isEqualTo(DEFAULT_INITIALTIME);
        assertThat(testWatch.getFinaltime()).isEqualTo(DEFAULT_FINALTIME);
        assertThat(testWatch.getResponsableofficer()).isEqualTo(DEFAULT_RESPONSABLEOFFICER);
    }

    @Test
    @Transactional
    public void createWatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = watchRepository.findAll().size();

        // Create the Watch with an existing ID
        Watch existingWatch = new Watch();
        existingWatch.setId(1L);
        WatchDTO existingWatchDTO = watchMapper.watchToWatchDTO(existingWatch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchMockMvc.perform(post("/api/watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingWatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Watch> watchList = watchRepository.findAll();
        assertThat(watchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkResponsableofficerIsRequired() throws Exception {
        int databaseSizeBeforeTest = watchRepository.findAll().size();
        // set the field null
        watch.setResponsableofficer(null);

        // Create the Watch, which fails.
        WatchDTO watchDTO = watchMapper.watchToWatchDTO(watch);

        restWatchMockMvc.perform(post("/api/watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchDTO)))
            .andExpect(status().isBadRequest());

        List<Watch> watchList = watchRepository.findAll();
        assertThat(watchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWatches() throws Exception {
        // Initialize the database
        watchRepository.saveAndFlush(watch);

        // Get all the watchList
        restWatchMockMvc.perform(get("/api/watches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watch.getId().intValue())))
            .andExpect(jsonPath("$.[*].initialtime").value(hasItem(sameInstant(DEFAULT_INITIALTIME))))
            .andExpect(jsonPath("$.[*].finaltime").value(hasItem(sameInstant(DEFAULT_FINALTIME))))
            .andExpect(jsonPath("$.[*].responsableofficer").value(hasItem(DEFAULT_RESPONSABLEOFFICER.toString())));
    }

    @Test
    @Transactional
    public void getWatch() throws Exception {
        // Initialize the database
        watchRepository.saveAndFlush(watch);

        // Get the watch
        restWatchMockMvc.perform(get("/api/watches/{id}", watch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(watch.getId().intValue()))
            .andExpect(jsonPath("$.initialtime").value(sameInstant(DEFAULT_INITIALTIME)))
            .andExpect(jsonPath("$.finaltime").value(sameInstant(DEFAULT_FINALTIME)))
            .andExpect(jsonPath("$.responsableofficer").value(DEFAULT_RESPONSABLEOFFICER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWatch() throws Exception {
        // Get the watch
        restWatchMockMvc.perform(get("/api/watches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWatch() throws Exception {
        // Initialize the database
        watchRepository.saveAndFlush(watch);
        int databaseSizeBeforeUpdate = watchRepository.findAll().size();

        // Update the watch
        Watch updatedWatch = watchRepository.findOne(watch.getId());
        updatedWatch
                .initialtime(UPDATED_INITIALTIME)
                .finaltime(UPDATED_FINALTIME)
                .responsableofficer(UPDATED_RESPONSABLEOFFICER);
        WatchDTO watchDTO = watchMapper.watchToWatchDTO(updatedWatch);

        restWatchMockMvc.perform(put("/api/watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchDTO)))
            .andExpect(status().isOk());

        // Validate the Watch in the database
        List<Watch> watchList = watchRepository.findAll();
        assertThat(watchList).hasSize(databaseSizeBeforeUpdate);
        Watch testWatch = watchList.get(watchList.size() - 1);
        assertThat(testWatch.getInitialtime()).isEqualTo(UPDATED_INITIALTIME);
        assertThat(testWatch.getFinaltime()).isEqualTo(UPDATED_FINALTIME);
        assertThat(testWatch.getResponsableofficer()).isEqualTo(UPDATED_RESPONSABLEOFFICER);
    }

    @Test
    @Transactional
    public void updateNonExistingWatch() throws Exception {
        int databaseSizeBeforeUpdate = watchRepository.findAll().size();

        // Create the Watch
        WatchDTO watchDTO = watchMapper.watchToWatchDTO(watch);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWatchMockMvc.perform(put("/api/watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(watchDTO)))
            .andExpect(status().isCreated());

        // Validate the Watch in the database
        List<Watch> watchList = watchRepository.findAll();
        assertThat(watchList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWatch() throws Exception {
        // Initialize the database
        watchRepository.saveAndFlush(watch);
        int databaseSizeBeforeDelete = watchRepository.findAll().size();

        // Get the watch
        restWatchMockMvc.perform(delete("/api/watches/{id}", watch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Watch> watchList = watchRepository.findAll();
        assertThat(watchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Watch.class);
    }
}
