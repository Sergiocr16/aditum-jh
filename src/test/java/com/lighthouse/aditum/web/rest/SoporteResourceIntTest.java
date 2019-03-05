package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Soporte;
import com.lighthouse.aditum.repository.SoporteRepository;
import com.lighthouse.aditum.service.SoporteService;
import com.lighthouse.aditum.service.dto.SoporteDTO;
import com.lighthouse.aditum.service.mapper.SoporteMapper;
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
 * Test class for the SoporteResource REST controller.
 *
 * @see SoporteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class SoporteResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SoporteRepository soporteRepository;

    @Autowired
    private SoporteMapper soporteMapper;

    @Autowired
    private SoporteService soporteService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSoporteMockMvc;

    private Soporte soporte;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SoporteResource soporteResource = new SoporteResource(soporteService);
        this.restSoporteMockMvc = MockMvcBuilders.standaloneSetup(soporteResource)
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
    public static Soporte createEntity(EntityManager em) {
        Soporte soporte = new Soporte()
            .description(DEFAULT_DESCRIPTION)
            .fullName(DEFAULT_FULL_NAME)
            .email(DEFAULT_EMAIL)
            .subject(DEFAULT_SUBJECT)
            .username(DEFAULT_USERNAME)
            .creationDate(DEFAULT_CREATION_DATE);
        return soporte;
    }

    @Before
    public void initTest() {
        soporte = createEntity(em);
    }

    @Test
    @Transactional
    public void createSoporte() throws Exception {
        int databaseSizeBeforeCreate = soporteRepository.findAll().size();

        // Create the Soporte
        SoporteDTO soporteDTO = soporteMapper.toDto(soporte);
        restSoporteMockMvc.perform(post("/api/soportes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soporteDTO)))
            .andExpect(status().isCreated());

        // Validate the Soporte in the database
        List<Soporte> soporteList = soporteRepository.findAll();
        assertThat(soporteList).hasSize(databaseSizeBeforeCreate + 1);
        Soporte testSoporte = soporteList.get(soporteList.size() - 1);
        assertThat(testSoporte.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSoporte.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testSoporte.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSoporte.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testSoporte.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSoporte.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createSoporteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = soporteRepository.findAll().size();

        // Create the Soporte with an existing ID
        soporte.setId(1L);
        SoporteDTO soporteDTO = soporteMapper.toDto(soporte);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoporteMockMvc.perform(post("/api/soportes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soporteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Soporte in the database
        List<Soporte> soporteList = soporteRepository.findAll();
        assertThat(soporteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSoportes() throws Exception {
        // Initialize the database
        soporteRepository.saveAndFlush(soporte);

        // Get all the soporteList
        restSoporteMockMvc.perform(get("/api/soportes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(soporte.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
    }

    @Test
    @Transactional
    public void getSoporte() throws Exception {
        // Initialize the database
        soporteRepository.saveAndFlush(soporte);

        // Get the soporte
        restSoporteMockMvc.perform(get("/api/soportes/{id}", soporte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(soporte.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSoporte() throws Exception {
        // Get the soporte
        restSoporteMockMvc.perform(get("/api/soportes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSoporte() throws Exception {
        // Initialize the database
        soporteRepository.saveAndFlush(soporte);
        int databaseSizeBeforeUpdate = soporteRepository.findAll().size();

        // Update the soporte
        Soporte updatedSoporte = soporteRepository.findOne(soporte.getId());
        // Disconnect from session so that the updates on updatedSoporte are not directly saved in db
        em.detach(updatedSoporte);
        updatedSoporte
            .description(UPDATED_DESCRIPTION)
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .subject(UPDATED_SUBJECT)
            .username(UPDATED_USERNAME)
            .creationDate(UPDATED_CREATION_DATE);
        SoporteDTO soporteDTO = soporteMapper.toDto(updatedSoporte);

        restSoporteMockMvc.perform(put("/api/soportes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soporteDTO)))
            .andExpect(status().isOk());

        // Validate the Soporte in the database
        List<Soporte> soporteList = soporteRepository.findAll();
        assertThat(soporteList).hasSize(databaseSizeBeforeUpdate);
        Soporte testSoporte = soporteList.get(soporteList.size() - 1);
        assertThat(testSoporte.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSoporte.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testSoporte.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSoporte.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testSoporte.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSoporte.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSoporte() throws Exception {
        int databaseSizeBeforeUpdate = soporteRepository.findAll().size();

        // Create the Soporte
        SoporteDTO soporteDTO = soporteMapper.toDto(soporte);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSoporteMockMvc.perform(put("/api/soportes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(soporteDTO)))
            .andExpect(status().isCreated());

        // Validate the Soporte in the database
        List<Soporte> soporteList = soporteRepository.findAll();
        assertThat(soporteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSoporte() throws Exception {
        // Initialize the database
        soporteRepository.saveAndFlush(soporte);
        int databaseSizeBeforeDelete = soporteRepository.findAll().size();

        // Get the soporte
        restSoporteMockMvc.perform(delete("/api/soportes/{id}", soporte.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Soporte> soporteList = soporteRepository.findAll();
        assertThat(soporteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Soporte.class);
        Soporte soporte1 = new Soporte();
        soporte1.setId(1L);
        Soporte soporte2 = new Soporte();
        soporte2.setId(soporte1.getId());
        assertThat(soporte1).isEqualTo(soporte2);
        soporte2.setId(2L);
        assertThat(soporte1).isNotEqualTo(soporte2);
        soporte1.setId(null);
        assertThat(soporte1).isNotEqualTo(soporte2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoporteDTO.class);
        SoporteDTO soporteDTO1 = new SoporteDTO();
        soporteDTO1.setId(1L);
        SoporteDTO soporteDTO2 = new SoporteDTO();
        assertThat(soporteDTO1).isNotEqualTo(soporteDTO2);
        soporteDTO2.setId(soporteDTO1.getId());
        assertThat(soporteDTO1).isEqualTo(soporteDTO2);
        soporteDTO2.setId(2L);
        assertThat(soporteDTO1).isNotEqualTo(soporteDTO2);
        soporteDTO1.setId(null);
        assertThat(soporteDTO1).isNotEqualTo(soporteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(soporteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(soporteMapper.fromId(null)).isNull();
    }
}
