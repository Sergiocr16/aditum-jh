package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.BitacoraAcciones;
import com.lighthouse.aditum.repository.BitacoraAccionesRepository;
import com.lighthouse.aditum.service.BitacoraAccionesService;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import com.lighthouse.aditum.service.mapper.BitacoraAccionesMapper;
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
 * Test class for the BitacoraAccionesResource REST controller.
 *
 * @see BitacoraAccionesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class BitacoraAccionesResourceIntTest {

    private static final String DEFAULT_CONCEPT = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Long DEFAULT_ID_REFERENCE = 1L;
    private static final Long UPDATED_ID_REFERENCE = 2L;

    private static final Long DEFAULT_ID_RESPONSABLE = 1L;
    private static final Long UPDATED_ID_RESPONSABLE = 2L;

    private static final ZonedDateTime DEFAULT_EJECUTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EJECUTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private BitacoraAccionesRepository bitacoraAccionesRepository;

    @Autowired
    private BitacoraAccionesMapper bitacoraAccionesMapper;

    @Autowired
    private BitacoraAccionesService bitacoraAccionesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBitacoraAccionesMockMvc;

    private BitacoraAcciones bitacoraAcciones;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BitacoraAccionesResource bitacoraAccionesResource = new BitacoraAccionesResource(bitacoraAccionesService);
        this.restBitacoraAccionesMockMvc = MockMvcBuilders.standaloneSetup(bitacoraAccionesResource)
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
    public static BitacoraAcciones createEntity(EntityManager em) {
        BitacoraAcciones bitacoraAcciones = new BitacoraAcciones()
            .concept(DEFAULT_CONCEPT)
            .type(DEFAULT_TYPE)
            .idReference(DEFAULT_ID_REFERENCE)
            .idResponsable(DEFAULT_ID_RESPONSABLE)
            .ejecutionDate(DEFAULT_EJECUTION_DATE)
            .category(DEFAULT_CATEGORY);
        return bitacoraAcciones;
    }

    @Before
    public void initTest() {
        bitacoraAcciones = createEntity(em);
    }

    @Test
    @Transactional
    public void createBitacoraAcciones() throws Exception {
        int databaseSizeBeforeCreate = bitacoraAccionesRepository.findAll().size();

        // Create the BitacoraAcciones
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesMapper.toDto(bitacoraAcciones);
        restBitacoraAccionesMockMvc.perform(post("/api/bitacora-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bitacoraAccionesDTO)))
            .andExpect(status().isCreated());

        // Validate the BitacoraAcciones in the database
        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeCreate + 1);
        BitacoraAcciones testBitacoraAcciones = bitacoraAccionesList.get(bitacoraAccionesList.size() - 1);
        assertThat(testBitacoraAcciones.getConcept()).isEqualTo(DEFAULT_CONCEPT);
        assertThat(testBitacoraAcciones.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBitacoraAcciones.getIdReference()).isEqualTo(DEFAULT_ID_REFERENCE);
        assertThat(testBitacoraAcciones.getIdResponsable()).isEqualTo(DEFAULT_ID_RESPONSABLE);
        assertThat(testBitacoraAcciones.getEjecutionDate()).isEqualTo(DEFAULT_EJECUTION_DATE);
        assertThat(testBitacoraAcciones.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    public void createBitacoraAccionesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bitacoraAccionesRepository.findAll().size();

        // Create the BitacoraAcciones with an existing ID
        bitacoraAcciones.setId(1L);
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesMapper.toDto(bitacoraAcciones);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBitacoraAccionesMockMvc.perform(post("/api/bitacora-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bitacoraAccionesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BitacoraAcciones in the database
        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkConceptIsRequired() throws Exception {
        int databaseSizeBeforeTest = bitacoraAccionesRepository.findAll().size();
        // set the field null
        bitacoraAcciones.setConcept(null);

        // Create the BitacoraAcciones, which fails.
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesMapper.toDto(bitacoraAcciones);

        restBitacoraAccionesMockMvc.perform(post("/api/bitacora-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bitacoraAccionesDTO)))
            .andExpect(status().isBadRequest());

        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEjecutionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bitacoraAccionesRepository.findAll().size();
        // set the field null
        bitacoraAcciones.setEjecutionDate(null);

        // Create the BitacoraAcciones, which fails.
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesMapper.toDto(bitacoraAcciones);

        restBitacoraAccionesMockMvc.perform(post("/api/bitacora-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bitacoraAccionesDTO)))
            .andExpect(status().isBadRequest());

        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBitacoraAcciones() throws Exception {
        // Initialize the database
        bitacoraAccionesRepository.saveAndFlush(bitacoraAcciones);

        // Get all the bitacoraAccionesList
        restBitacoraAccionesMockMvc.perform(get("/api/bitacora-acciones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bitacoraAcciones.getId().intValue())))
            .andExpect(jsonPath("$.[*].concept").value(hasItem(DEFAULT_CONCEPT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].idReference").value(hasItem(DEFAULT_ID_REFERENCE.intValue())))
            .andExpect(jsonPath("$.[*].idResponsable").value(hasItem(DEFAULT_ID_RESPONSABLE.intValue())))
            .andExpect(jsonPath("$.[*].ejecutionDate").value(hasItem(sameInstant(DEFAULT_EJECUTION_DATE))))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getBitacoraAcciones() throws Exception {
        // Initialize the database
        bitacoraAccionesRepository.saveAndFlush(bitacoraAcciones);

        // Get the bitacoraAcciones
        restBitacoraAccionesMockMvc.perform(get("/api/bitacora-acciones/{id}", bitacoraAcciones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bitacoraAcciones.getId().intValue()))
            .andExpect(jsonPath("$.concept").value(DEFAULT_CONCEPT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.idReference").value(DEFAULT_ID_REFERENCE.intValue()))
            .andExpect(jsonPath("$.idResponsable").value(DEFAULT_ID_RESPONSABLE.intValue()))
            .andExpect(jsonPath("$.ejecutionDate").value(sameInstant(DEFAULT_EJECUTION_DATE)))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBitacoraAcciones() throws Exception {
        // Get the bitacoraAcciones
        restBitacoraAccionesMockMvc.perform(get("/api/bitacora-acciones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBitacoraAcciones() throws Exception {
        // Initialize the database
        bitacoraAccionesRepository.saveAndFlush(bitacoraAcciones);
        int databaseSizeBeforeUpdate = bitacoraAccionesRepository.findAll().size();

        // Update the bitacoraAcciones
        BitacoraAcciones updatedBitacoraAcciones = bitacoraAccionesRepository.findOne(bitacoraAcciones.getId());
        // Disconnect from session so that the updates on updatedBitacoraAcciones are not directly saved in db
        em.detach(updatedBitacoraAcciones);
        updatedBitacoraAcciones
            .concept(UPDATED_CONCEPT)
            .type(UPDATED_TYPE)
            .idReference(UPDATED_ID_REFERENCE)
            .idResponsable(UPDATED_ID_RESPONSABLE)
            .ejecutionDate(UPDATED_EJECUTION_DATE)
            .category(UPDATED_CATEGORY);
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesMapper.toDto(updatedBitacoraAcciones);

        restBitacoraAccionesMockMvc.perform(put("/api/bitacora-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bitacoraAccionesDTO)))
            .andExpect(status().isOk());

        // Validate the BitacoraAcciones in the database
        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeUpdate);
        BitacoraAcciones testBitacoraAcciones = bitacoraAccionesList.get(bitacoraAccionesList.size() - 1);
        assertThat(testBitacoraAcciones.getConcept()).isEqualTo(UPDATED_CONCEPT);
        assertThat(testBitacoraAcciones.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBitacoraAcciones.getIdReference()).isEqualTo(UPDATED_ID_REFERENCE);
        assertThat(testBitacoraAcciones.getIdResponsable()).isEqualTo(UPDATED_ID_RESPONSABLE);
        assertThat(testBitacoraAcciones.getEjecutionDate()).isEqualTo(UPDATED_EJECUTION_DATE);
        assertThat(testBitacoraAcciones.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void updateNonExistingBitacoraAcciones() throws Exception {
        int databaseSizeBeforeUpdate = bitacoraAccionesRepository.findAll().size();

        // Create the BitacoraAcciones
        BitacoraAccionesDTO bitacoraAccionesDTO = bitacoraAccionesMapper.toDto(bitacoraAcciones);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBitacoraAccionesMockMvc.perform(put("/api/bitacora-acciones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bitacoraAccionesDTO)))
            .andExpect(status().isCreated());

        // Validate the BitacoraAcciones in the database
        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBitacoraAcciones() throws Exception {
        // Initialize the database
        bitacoraAccionesRepository.saveAndFlush(bitacoraAcciones);
        int databaseSizeBeforeDelete = bitacoraAccionesRepository.findAll().size();

        // Get the bitacoraAcciones
        restBitacoraAccionesMockMvc.perform(delete("/api/bitacora-acciones/{id}", bitacoraAcciones.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BitacoraAcciones> bitacoraAccionesList = bitacoraAccionesRepository.findAll();
        assertThat(bitacoraAccionesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BitacoraAcciones.class);
        BitacoraAcciones bitacoraAcciones1 = new BitacoraAcciones();
        bitacoraAcciones1.setId(1L);
        BitacoraAcciones bitacoraAcciones2 = new BitacoraAcciones();
        bitacoraAcciones2.setId(bitacoraAcciones1.getId());
        assertThat(bitacoraAcciones1).isEqualTo(bitacoraAcciones2);
        bitacoraAcciones2.setId(2L);
        assertThat(bitacoraAcciones1).isNotEqualTo(bitacoraAcciones2);
        bitacoraAcciones1.setId(null);
        assertThat(bitacoraAcciones1).isNotEqualTo(bitacoraAcciones2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BitacoraAccionesDTO.class);
        BitacoraAccionesDTO bitacoraAccionesDTO1 = new BitacoraAccionesDTO();
        bitacoraAccionesDTO1.setId(1L);
        BitacoraAccionesDTO bitacoraAccionesDTO2 = new BitacoraAccionesDTO();
        assertThat(bitacoraAccionesDTO1).isNotEqualTo(bitacoraAccionesDTO2);
        bitacoraAccionesDTO2.setId(bitacoraAccionesDTO1.getId());
        assertThat(bitacoraAccionesDTO1).isEqualTo(bitacoraAccionesDTO2);
        bitacoraAccionesDTO2.setId(2L);
        assertThat(bitacoraAccionesDTO1).isNotEqualTo(bitacoraAccionesDTO2);
        bitacoraAccionesDTO1.setId(null);
        assertThat(bitacoraAccionesDTO1).isNotEqualTo(bitacoraAccionesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bitacoraAccionesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bitacoraAccionesMapper.fromId(null)).isNull();
    }
}
