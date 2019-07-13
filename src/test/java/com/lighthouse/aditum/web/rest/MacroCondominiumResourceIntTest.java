package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.MacroCondominium;
import com.lighthouse.aditum.repository.MacroCondominiumRepository;
import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.MacroCondominiumService;
import com.lighthouse.aditum.service.dto.MacroCondominiumDTO;
import com.lighthouse.aditum.service.mapper.MacroCondominiumMapper;
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

//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MacroCondominiumResource REST controller.
 *
 * @see MacroCondominiumResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class MacroCondominiumResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private MacroCondominiumRepository macroCondominiumRepository;

    @Autowired
    private MacroCondominiumMapper macroCondominiumMapper;

    @Autowired
    private MacroCondominiumService macroCondominiumService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMacroCondominiumMockMvc;

    private MacroCondominium macroCondominium;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MacroCondominiumResource macroCondominiumResource = new MacroCondominiumResource(houseService,macroCondominiumService);
        this.restMacroCondominiumMockMvc = MockMvcBuilders.standaloneSetup(macroCondominiumResource)
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
    public static MacroCondominium createEntity(EntityManager em) {
        MacroCondominium macroCondominium = new MacroCondominium()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return macroCondominium;
    }

    @Before
    public void initTest() {
        macroCondominium = createEntity(em);
    }

    @Test
    @Transactional
    public void createMacroCondominium() throws Exception {
        int databaseSizeBeforeCreate = macroCondominiumRepository.findAll().size();

        // Create the MacroCondominium
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumMapper.toDto(macroCondominium);
        restMacroCondominiumMockMvc.perform(post("/api/macro-condominiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroCondominiumDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroCondominium in the database
        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeCreate + 1);
        MacroCondominium testMacroCondominium = macroCondominiumList.get(macroCondominiumList.size() - 1);
        assertThat(testMacroCondominium.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMacroCondominium.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createMacroCondominiumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = macroCondominiumRepository.findAll().size();

        // Create the MacroCondominium with an existing ID
        macroCondominium.setId(1L);
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumMapper.toDto(macroCondominium);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMacroCondominiumMockMvc.perform(post("/api/macro-condominiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroCondominiumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MacroCondominium in the database
        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroCondominiumRepository.findAll().size();
        // set the field null
        macroCondominium.setName(null);

        // Create the MacroCondominium, which fails.
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumMapper.toDto(macroCondominium);

        restMacroCondominiumMockMvc.perform(post("/api/macro-condominiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroCondominiumDTO)))
            .andExpect(status().isBadRequest());

        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroCondominiumRepository.findAll().size();
        // set the field null
        macroCondominium.setEnabled(null);

        // Create the MacroCondominium, which fails.
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumMapper.toDto(macroCondominium);

        restMacroCondominiumMockMvc.perform(post("/api/macro-condominiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroCondominiumDTO)))
            .andExpect(status().isBadRequest());

        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMacroCondominiums() throws Exception {
        // Initialize the database
        macroCondominiumRepository.saveAndFlush(macroCondominium);

        // Get all the macroCondominiumList
        restMacroCondominiumMockMvc.perform(get("/api/macro-condominiums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macroCondominium.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMacroCondominium() throws Exception {
        // Initialize the database
        macroCondominiumRepository.saveAndFlush(macroCondominium);

        // Get the macroCondominium
        restMacroCondominiumMockMvc.perform(get("/api/macro-condominiums/{id}", macroCondominium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(macroCondominium.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMacroCondominium() throws Exception {
        // Get the macroCondominium
        restMacroCondominiumMockMvc.perform(get("/api/macro-condominiums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacroCondominium() throws Exception {
        // Initialize the database
        macroCondominiumRepository.saveAndFlush(macroCondominium);
        int databaseSizeBeforeUpdate = macroCondominiumRepository.findAll().size();

        // Update the macroCondominium
        MacroCondominium updatedMacroCondominium = macroCondominiumRepository.findOne(macroCondominium.getId());
        // Disconnect from session so that the updates on updatedMacroCondominium are not directly saved in db
        em.detach(updatedMacroCondominium);
        updatedMacroCondominium
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumMapper.toDto(updatedMacroCondominium);

        restMacroCondominiumMockMvc.perform(put("/api/macro-condominiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroCondominiumDTO)))
            .andExpect(status().isOk());

        // Validate the MacroCondominium in the database
        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeUpdate);
        MacroCondominium testMacroCondominium = macroCondominiumList.get(macroCondominiumList.size() - 1);
        assertThat(testMacroCondominium.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMacroCondominium.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingMacroCondominium() throws Exception {
        int databaseSizeBeforeUpdate = macroCondominiumRepository.findAll().size();

        // Create the MacroCondominium
        MacroCondominiumDTO macroCondominiumDTO = macroCondominiumMapper.toDto(macroCondominium);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMacroCondominiumMockMvc.perform(put("/api/macro-condominiums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(macroCondominiumDTO)))
            .andExpect(status().isCreated());

        // Validate the MacroCondominium in the database
        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMacroCondominium() throws Exception {
        // Initialize the database
        macroCondominiumRepository.saveAndFlush(macroCondominium);
        int databaseSizeBeforeDelete = macroCondominiumRepository.findAll().size();

        // Get the macroCondominium
        restMacroCondominiumMockMvc.perform(delete("/api/macro-condominiums/{id}", macroCondominium.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MacroCondominium> macroCondominiumList = macroCondominiumRepository.findAll();
        assertThat(macroCondominiumList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroCondominium.class);
        MacroCondominium macroCondominium1 = new MacroCondominium();
        macroCondominium1.setId(1L);
        MacroCondominium macroCondominium2 = new MacroCondominium();
        macroCondominium2.setId(macroCondominium1.getId());
        assertThat(macroCondominium1).isEqualTo(macroCondominium2);
        macroCondominium2.setId(2L);
        assertThat(macroCondominium1).isNotEqualTo(macroCondominium2);
        macroCondominium1.setId(null);
        assertThat(macroCondominium1).isNotEqualTo(macroCondominium2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MacroCondominiumDTO.class);
        MacroCondominiumDTO macroCondominiumDTO1 = new MacroCondominiumDTO();
        macroCondominiumDTO1.setId(1L);
        MacroCondominiumDTO macroCondominiumDTO2 = new MacroCondominiumDTO();
        assertThat(macroCondominiumDTO1).isNotEqualTo(macroCondominiumDTO2);
        macroCondominiumDTO2.setId(macroCondominiumDTO1.getId());
        assertThat(macroCondominiumDTO1).isEqualTo(macroCondominiumDTO2);
        macroCondominiumDTO2.setId(2L);
        assertThat(macroCondominiumDTO1).isNotEqualTo(macroCondominiumDTO2);
        macroCondominiumDTO1.setId(null);
        assertThat(macroCondominiumDTO1).isNotEqualTo(macroCondominiumDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(macroCondominiumMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(macroCondominiumMapper.fromId(null)).isNull();
    }
}
