package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.ActivosFijos;
import com.lighthouse.aditum.repository.ActivosFijosRepository;
import com.lighthouse.aditum.service.ActivosFijosService;
import com.lighthouse.aditum.service.dto.ActivosFijosDTO;
import com.lighthouse.aditum.service.mapper.ActivosFijosMapper;
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
 * Test class for the ActivosFijosResource REST controller.
 *
 * @see ActivosFijosResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ActivosFijosResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final Double DEFAULT_DEPRECIACION = 1D;
    private static final Double UPDATED_DEPRECIACION = 2D;

    @Autowired
    private ActivosFijosRepository activosFijosRepository;

    @Autowired
    private ActivosFijosMapper activosFijosMapper;

    @Autowired
    private ActivosFijosService activosFijosService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivosFijosMockMvc;

    private ActivosFijos activosFijos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivosFijosResource activosFijosResource = new ActivosFijosResource(activosFijosService);
        this.restActivosFijosMockMvc = MockMvcBuilders.standaloneSetup(activosFijosResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivosFijos createEntity(EntityManager em) {
        ActivosFijos activosFijos = new ActivosFijos()
            .nombre(DEFAULT_NOMBRE)
            .valor(DEFAULT_VALOR)
            .depreciacion(DEFAULT_DEPRECIACION);
        return activosFijos;
    }

    @Before
    public void initTest() {
        activosFijos = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivosFijos() throws Exception {
        int databaseSizeBeforeCreate = activosFijosRepository.findAll().size();

        // Create the ActivosFijos
        ActivosFijosDTO activosFijosDTO = activosFijosMapper.toDto(activosFijos);
        restActivosFijosMockMvc.perform(post("/api/activos-fijos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activosFijosDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivosFijos in the database
        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeCreate + 1);
        ActivosFijos testActivosFijos = activosFijosList.get(activosFijosList.size() - 1);
        assertThat(testActivosFijos.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testActivosFijos.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testActivosFijos.getDepreciacion()).isEqualTo(DEFAULT_DEPRECIACION);
    }

    @Test
    @Transactional
    public void createActivosFijosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activosFijosRepository.findAll().size();

        // Create the ActivosFijos with an existing ID
        activosFijos.setId(1L);
        ActivosFijosDTO activosFijosDTO = activosFijosMapper.toDto(activosFijos);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivosFijosMockMvc.perform(post("/api/activos-fijos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activosFijosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivosFijos in the database
        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = activosFijosRepository.findAll().size();
        // set the field null
        activosFijos.setNombre(null);

        // Create the ActivosFijos, which fails.
        ActivosFijosDTO activosFijosDTO = activosFijosMapper.toDto(activosFijos);

        restActivosFijosMockMvc.perform(post("/api/activos-fijos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activosFijosDTO)))
            .andExpect(status().isBadRequest());

        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = activosFijosRepository.findAll().size();
        // set the field null
        activosFijos.setValor(null);

        // Create the ActivosFijos, which fails.
        ActivosFijosDTO activosFijosDTO = activosFijosMapper.toDto(activosFijos);

        restActivosFijosMockMvc.perform(post("/api/activos-fijos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activosFijosDTO)))
            .andExpect(status().isBadRequest());

        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivosFijos() throws Exception {
        // Initialize the database
        activosFijosRepository.saveAndFlush(activosFijos);

        // Get all the activosFijosList
        restActivosFijosMockMvc.perform(get("/api/activos-fijos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activosFijos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].depreciacion").value(hasItem(DEFAULT_DEPRECIACION.doubleValue())));
    }

    @Test
    @Transactional
    public void getActivosFijos() throws Exception {
        // Initialize the database
        activosFijosRepository.saveAndFlush(activosFijos);

        // Get the activosFijos
        restActivosFijosMockMvc.perform(get("/api/activos-fijos/{id}", activosFijos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activosFijos.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.depreciacion").value(DEFAULT_DEPRECIACION.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingActivosFijos() throws Exception {
        // Get the activosFijos
        restActivosFijosMockMvc.perform(get("/api/activos-fijos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivosFijos() throws Exception {
        // Initialize the database
        activosFijosRepository.saveAndFlush(activosFijos);
        int databaseSizeBeforeUpdate = activosFijosRepository.findAll().size();

        // Update the activosFijos
        ActivosFijos updatedActivosFijos = activosFijosRepository.findOne(activosFijos.getId());
        // Disconnect from session so that the updates on updatedActivosFijos are not directly saved in db
        em.detach(updatedActivosFijos);
        updatedActivosFijos
            .nombre(UPDATED_NOMBRE)
            .valor(UPDATED_VALOR)
            .depreciacion(UPDATED_DEPRECIACION);
        ActivosFijosDTO activosFijosDTO = activosFijosMapper.toDto(updatedActivosFijos);

        restActivosFijosMockMvc.perform(put("/api/activos-fijos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activosFijosDTO)))
            .andExpect(status().isOk());

        // Validate the ActivosFijos in the database
        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeUpdate);
        ActivosFijos testActivosFijos = activosFijosList.get(activosFijosList.size() - 1);
        assertThat(testActivosFijos.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testActivosFijos.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testActivosFijos.getDepreciacion()).isEqualTo(UPDATED_DEPRECIACION);
    }

    @Test
    @Transactional
    public void updateNonExistingActivosFijos() throws Exception {
        int databaseSizeBeforeUpdate = activosFijosRepository.findAll().size();

        // Create the ActivosFijos
        ActivosFijosDTO activosFijosDTO = activosFijosMapper.toDto(activosFijos);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restActivosFijosMockMvc.perform(put("/api/activos-fijos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activosFijosDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivosFijos in the database
        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteActivosFijos() throws Exception {
        // Initialize the database
        activosFijosRepository.saveAndFlush(activosFijos);
        int databaseSizeBeforeDelete = activosFijosRepository.findAll().size();

        // Get the activosFijos
        restActivosFijosMockMvc.perform(delete("/api/activos-fijos/{id}", activosFijos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivosFijos> activosFijosList = activosFijosRepository.findAll();
        assertThat(activosFijosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivosFijos.class);
        ActivosFijos activosFijos1 = new ActivosFijos();
        activosFijos1.setId(1L);
        ActivosFijos activosFijos2 = new ActivosFijos();
        activosFijos2.setId(activosFijos1.getId());
        assertThat(activosFijos1).isEqualTo(activosFijos2);
        activosFijos2.setId(2L);
        assertThat(activosFijos1).isNotEqualTo(activosFijos2);
        activosFijos1.setId(null);
        assertThat(activosFijos1).isNotEqualTo(activosFijos2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivosFijosDTO.class);
        ActivosFijosDTO activosFijosDTO1 = new ActivosFijosDTO();
        activosFijosDTO1.setId(1L);
        ActivosFijosDTO activosFijosDTO2 = new ActivosFijosDTO();
        assertThat(activosFijosDTO1).isNotEqualTo(activosFijosDTO2);
        activosFijosDTO2.setId(activosFijosDTO1.getId());
        assertThat(activosFijosDTO1).isEqualTo(activosFijosDTO2);
        activosFijosDTO2.setId(2L);
        assertThat(activosFijosDTO1).isNotEqualTo(activosFijosDTO2);
        activosFijosDTO1.setId(null);
        assertThat(activosFijosDTO1).isNotEqualTo(activosFijosDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activosFijosMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activosFijosMapper.fromId(null)).isNull();
    }
}
