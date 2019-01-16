package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.DetallePresupuesto;
import com.lighthouse.aditum.repository.DetallePresupuestoRepository;
import com.lighthouse.aditum.service.DetallePresupuestoService;
import com.lighthouse.aditum.service.dto.DetallePresupuestoDTO;
import com.lighthouse.aditum.service.mapper.DetallePresupuestoMapper;
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
 * Test class for the DetallePresupuestoResource REST controller.
 *
 * @see DetallePresupuestoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class DetallePresupuestoResourceIntTest {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE_PER_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_VALUE_PER_MONTH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PRESUPUESTO_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRESUPUESTO_ID = "BBBBBBBBBB";

    @Autowired
    private DetallePresupuestoRepository detallePresupuestoRepository;

    @Autowired
    private DetallePresupuestoMapper detallePresupuestoMapper;

    @Autowired
    private DetallePresupuestoService detallePresupuestoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDetallePresupuestoMockMvc;

    private DetallePresupuesto detallePresupuesto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DetallePresupuestoResource detallePresupuestoResource = new DetallePresupuestoResource(detallePresupuestoService);
        this.restDetallePresupuestoMockMvc = MockMvcBuilders.standaloneSetup(detallePresupuestoResource)
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
    public static DetallePresupuesto createEntity(EntityManager em) {
        DetallePresupuesto detallePresupuesto = new DetallePresupuesto()
            .category(DEFAULT_CATEGORY)
            .valuePerMonth(DEFAULT_VALUE_PER_MONTH)
            .type(DEFAULT_TYPE)
            .presupuestoId(DEFAULT_PRESUPUESTO_ID);
        return detallePresupuesto;
    }

    @Before
    public void initTest() {
        detallePresupuesto = createEntity(em);
    }

    @Test
    @Transactional
    public void createDetallePresupuesto() throws Exception {
        int databaseSizeBeforeCreate = detallePresupuestoRepository.findAll().size();

        // Create the DetallePresupuesto
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(detallePresupuesto);
        restDetallePresupuestoMockMvc.perform(post("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isCreated());

        // Validate the DetallePresupuesto in the database
        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeCreate + 1);
        DetallePresupuesto testDetallePresupuesto = detallePresupuestoList.get(detallePresupuestoList.size() - 1);
        assertThat(testDetallePresupuesto.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testDetallePresupuesto.getValuePerMonth()).isEqualTo(DEFAULT_VALUE_PER_MONTH);
        assertThat(testDetallePresupuesto.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDetallePresupuesto.getPresupuestoId()).isEqualTo(DEFAULT_PRESUPUESTO_ID);
    }

    @Test
    @Transactional
    public void createDetallePresupuestoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = detallePresupuestoRepository.findAll().size();

        // Create the DetallePresupuesto with an existing ID
        detallePresupuesto.setId(1L);
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(detallePresupuesto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetallePresupuestoMockMvc.perform(post("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = detallePresupuestoRepository.findAll().size();
        // set the field null
        detallePresupuesto.setCategory(null);

        // Create the DetallePresupuesto, which fails.
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(detallePresupuesto);

        restDetallePresupuestoMockMvc.perform(post("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isBadRequest());

        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = detallePresupuestoRepository.findAll().size();
        // set the field null
        detallePresupuesto.setType(null);

        // Create the DetallePresupuesto, which fails.
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(detallePresupuesto);

        restDetallePresupuestoMockMvc.perform(post("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isBadRequest());

        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresupuestoIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = detallePresupuestoRepository.findAll().size();
        // set the field null
        detallePresupuesto.setPresupuestoId(null);

        // Create the DetallePresupuesto, which fails.
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(detallePresupuesto);

        restDetallePresupuestoMockMvc.perform(post("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isBadRequest());

        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDetallePresupuestos() throws Exception {
        // Initialize the database
        detallePresupuestoRepository.saveAndFlush(detallePresupuesto);

        // Get all the detallePresupuestoList
        restDetallePresupuestoMockMvc.perform(get("/api/detalle-presupuestos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detallePresupuesto.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].valuePerMonth").value(hasItem(DEFAULT_VALUE_PER_MONTH.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].presupuestoId").value(hasItem(DEFAULT_PRESUPUESTO_ID.toString())));
    }

    @Test
    @Transactional
    public void getDetallePresupuesto() throws Exception {
        // Initialize the database
        detallePresupuestoRepository.saveAndFlush(detallePresupuesto);

        // Get the detallePresupuesto
        restDetallePresupuestoMockMvc.perform(get("/api/detalle-presupuestos/{id}", detallePresupuesto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(detallePresupuesto.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.valuePerMonth").value(DEFAULT_VALUE_PER_MONTH.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.presupuestoId").value(DEFAULT_PRESUPUESTO_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDetallePresupuesto() throws Exception {
        // Get the detallePresupuesto
        restDetallePresupuestoMockMvc.perform(get("/api/detalle-presupuestos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDetallePresupuesto() throws Exception {
        // Initialize the database
        detallePresupuestoRepository.saveAndFlush(detallePresupuesto);
        int databaseSizeBeforeUpdate = detallePresupuestoRepository.findAll().size();

        // Update the detallePresupuesto
        DetallePresupuesto updatedDetallePresupuesto = detallePresupuestoRepository.findOne(detallePresupuesto.getId());
        updatedDetallePresupuesto
            .category(UPDATED_CATEGORY)
            .valuePerMonth(UPDATED_VALUE_PER_MONTH)
            .type(UPDATED_TYPE)
            .presupuestoId(UPDATED_PRESUPUESTO_ID);
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(updatedDetallePresupuesto);

        restDetallePresupuestoMockMvc.perform(put("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isOk());

        // Validate the DetallePresupuesto in the database
        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeUpdate);
        DetallePresupuesto testDetallePresupuesto = detallePresupuestoList.get(detallePresupuestoList.size() - 1);
        assertThat(testDetallePresupuesto.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testDetallePresupuesto.getValuePerMonth()).isEqualTo(UPDATED_VALUE_PER_MONTH);
        assertThat(testDetallePresupuesto.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDetallePresupuesto.getPresupuestoId()).isEqualTo(UPDATED_PRESUPUESTO_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingDetallePresupuesto() throws Exception {
        int databaseSizeBeforeUpdate = detallePresupuestoRepository.findAll().size();

        // Create the DetallePresupuesto
        DetallePresupuestoDTO detallePresupuestoDTO = detallePresupuestoMapper.toDto(detallePresupuesto);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDetallePresupuestoMockMvc.perform(put("/api/detalle-presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(detallePresupuestoDTO)))
            .andExpect(status().isCreated());

        // Validate the DetallePresupuesto in the database
        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDetallePresupuesto() throws Exception {
        // Initialize the database
        detallePresupuestoRepository.saveAndFlush(detallePresupuesto);
        int databaseSizeBeforeDelete = detallePresupuestoRepository.findAll().size();

        // Get the detallePresupuesto
        restDetallePresupuestoMockMvc.perform(delete("/api/detalle-presupuestos/{id}", detallePresupuesto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DetallePresupuesto> detallePresupuestoList = detallePresupuestoRepository.findAll();
        assertThat(detallePresupuestoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetallePresupuesto.class);
        DetallePresupuesto detallePresupuesto1 = new DetallePresupuesto();
        detallePresupuesto1.setId(1L);
        DetallePresupuesto detallePresupuesto2 = new DetallePresupuesto();
        detallePresupuesto2.setId(detallePresupuesto1.getId());
        assertThat(detallePresupuesto1).isEqualTo(detallePresupuesto2);
        detallePresupuesto2.setId(2L);
        assertThat(detallePresupuesto1).isNotEqualTo(detallePresupuesto2);
        detallePresupuesto1.setId(null);
        assertThat(detallePresupuesto1).isNotEqualTo(detallePresupuesto2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetallePresupuestoDTO.class);
        DetallePresupuestoDTO detallePresupuestoDTO1 = new DetallePresupuestoDTO();
        detallePresupuestoDTO1.setId(1L);
        DetallePresupuestoDTO detallePresupuestoDTO2 = new DetallePresupuestoDTO();
        assertThat(detallePresupuestoDTO1).isNotEqualTo(detallePresupuestoDTO2);
        detallePresupuestoDTO2.setId(detallePresupuestoDTO1.getId());
        assertThat(detallePresupuestoDTO1).isEqualTo(detallePresupuestoDTO2);
        detallePresupuestoDTO2.setId(2L);
        assertThat(detallePresupuestoDTO1).isNotEqualTo(detallePresupuestoDTO2);
        detallePresupuestoDTO1.setId(null);
        assertThat(detallePresupuestoDTO1).isNotEqualTo(detallePresupuestoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(detallePresupuestoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(detallePresupuestoMapper.fromId(null)).isNull();
    }
}
