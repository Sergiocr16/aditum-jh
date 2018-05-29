package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Presupuesto;
import com.lighthouse.aditum.repository.PresupuestoRepository;
import com.lighthouse.aditum.service.PresupuestoService;
import com.lighthouse.aditum.service.dto.PresupuestoDTO;
import com.lighthouse.aditum.service.mapper.PresupuestoMapper;
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
 * Test class for the PresupuestoResource REST controller.
 *
 * @see PresupuestoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class PresupuestoResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_ANNO = 1;
    private static final Integer UPDATED_ANNO = 2;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private PresupuestoMapper presupuestoMapper;

    @Autowired
    private PresupuestoService presupuestoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPresupuestoMockMvc;

    private Presupuesto presupuesto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PresupuestoResource presupuestoResource = new PresupuestoResource(presupuestoService);
        this.restPresupuestoMockMvc = MockMvcBuilders.standaloneSetup(presupuestoResource)
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
    public static Presupuesto createEntity(EntityManager em) {
        Presupuesto presupuesto = new Presupuesto()
            .date(DEFAULT_DATE)
            .modificationDate(DEFAULT_MODIFICATION_DATE)
            .anno(DEFAULT_ANNO);
        return presupuesto;
    }

    @Before
    public void initTest() {
        presupuesto = createEntity(em);
    }

    @Test
    @Transactional
    public void createPresupuesto() throws Exception {
        int databaseSizeBeforeCreate = presupuestoRepository.findAll().size();

        // Create the Presupuesto
        PresupuestoDTO presupuestoDTO = presupuestoMapper.toDto(presupuesto);
        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuestoDTO)))
            .andExpect(status().isCreated());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeCreate + 1);
        Presupuesto testPresupuesto = presupuestoList.get(presupuestoList.size() - 1);
        assertThat(testPresupuesto.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPresupuesto.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testPresupuesto.getAnno()).isEqualTo(DEFAULT_ANNO);
    }

    @Test
    @Transactional
    public void createPresupuestoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = presupuestoRepository.findAll().size();

        // Create the Presupuesto with an existing ID
        presupuesto.setId(1L);
        PresupuestoDTO presupuestoDTO = presupuestoMapper.toDto(presupuesto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuestoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = presupuestoRepository.findAll().size();
        // set the field null
        presupuesto.setDate(null);

        // Create the Presupuesto, which fails.
        PresupuestoDTO presupuestoDTO = presupuestoMapper.toDto(presupuesto);

        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuestoDTO)))
            .andExpect(status().isBadRequest());

        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = presupuestoRepository.findAll().size();
        // set the field null
        presupuesto.setAnno(null);

        // Create the Presupuesto, which fails.
        PresupuestoDTO presupuestoDTO = presupuestoMapper.toDto(presupuesto);

        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuestoDTO)))
            .andExpect(status().isBadRequest());

        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPresupuestos() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);

        // Get all the presupuestoList
        restPresupuestoMockMvc.perform(get("/api/presupuestos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presupuesto.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(sameInstant(DEFAULT_MODIFICATION_DATE))))
            .andExpect(jsonPath("$.[*].anno").value(hasItem(DEFAULT_ANNO)));
    }

    @Test
    @Transactional
    public void getPresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);

        // Get the presupuesto
        restPresupuestoMockMvc.perform(get("/api/presupuestos/{id}", presupuesto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(presupuesto.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.modificationDate").value(sameInstant(DEFAULT_MODIFICATION_DATE)))
            .andExpect(jsonPath("$.anno").value(DEFAULT_ANNO));
    }

    @Test
    @Transactional
    public void getNonExistingPresupuesto() throws Exception {
        // Get the presupuesto
        restPresupuestoMockMvc.perform(get("/api/presupuestos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);
        int databaseSizeBeforeUpdate = presupuestoRepository.findAll().size();

        // Update the presupuesto
        Presupuesto updatedPresupuesto = presupuestoRepository.findOne(presupuesto.getId());
        updatedPresupuesto
            .date(UPDATED_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .anno(UPDATED_ANNO);
        PresupuestoDTO presupuestoDTO = presupuestoMapper.toDto(updatedPresupuesto);

        restPresupuestoMockMvc.perform(put("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuestoDTO)))
            .andExpect(status().isOk());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeUpdate);
        Presupuesto testPresupuesto = presupuestoList.get(presupuestoList.size() - 1);
        assertThat(testPresupuesto.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPresupuesto.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testPresupuesto.getAnno()).isEqualTo(UPDATED_ANNO);
    }

    @Test
    @Transactional
    public void updateNonExistingPresupuesto() throws Exception {
        int databaseSizeBeforeUpdate = presupuestoRepository.findAll().size();

        // Create the Presupuesto
        PresupuestoDTO presupuestoDTO = presupuestoMapper.toDto(presupuesto);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPresupuestoMockMvc.perform(put("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuestoDTO)))
            .andExpect(status().isCreated());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);
        int databaseSizeBeforeDelete = presupuestoRepository.findAll().size();

        // Get the presupuesto
        restPresupuestoMockMvc.perform(delete("/api/presupuestos/{id}", presupuesto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presupuesto.class);
        Presupuesto presupuesto1 = new Presupuesto();
        presupuesto1.setId(1L);
        Presupuesto presupuesto2 = new Presupuesto();
        presupuesto2.setId(presupuesto1.getId());
        assertThat(presupuesto1).isEqualTo(presupuesto2);
        presupuesto2.setId(2L);
        assertThat(presupuesto1).isNotEqualTo(presupuesto2);
        presupuesto1.setId(null);
        assertThat(presupuesto1).isNotEqualTo(presupuesto2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresupuestoDTO.class);
        PresupuestoDTO presupuestoDTO1 = new PresupuestoDTO();
        presupuestoDTO1.setId(1L);
        PresupuestoDTO presupuestoDTO2 = new PresupuestoDTO();
        assertThat(presupuestoDTO1).isNotEqualTo(presupuestoDTO2);
        presupuestoDTO2.setId(presupuestoDTO1.getId());
        assertThat(presupuestoDTO1).isEqualTo(presupuestoDTO2);
        presupuestoDTO2.setId(2L);
        assertThat(presupuestoDTO1).isNotEqualTo(presupuestoDTO2);
        presupuestoDTO1.setId(null);
        assertThat(presupuestoDTO1).isNotEqualTo(presupuestoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(presupuestoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(presupuestoMapper.fromId(null)).isNull();
    }
}
