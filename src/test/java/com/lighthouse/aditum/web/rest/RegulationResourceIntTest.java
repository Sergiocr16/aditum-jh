package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Regulation;
import com.lighthouse.aditum.repository.RegulationRepository;
import com.lighthouse.aditum.service.ChapterService;
import com.lighthouse.aditum.service.RegulationService;
import com.lighthouse.aditum.service.dto.RegulationDTO;
import com.lighthouse.aditum.service.mapper.RegulationMapper;
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
 * Test class for the RegulationResource REST controller.
 *
 * @see RegulationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RegulationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private RegulationRepository regulationRepository;

    @Autowired
    private RegulationMapper regulationMapper;

    @Autowired
    private RegulationService regulationService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegulationMockMvc;

    private Regulation regulation;

//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        final RegulationResource regulationResource = new RegulationResource(chapterService,regulationService);
//        this.restRegulationMockMvc = MockMvcBuilders.standaloneSetup(regulationResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setControllerAdvice(exceptionTranslator)
//            .setMessageConverters().build();
//    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regulation createEntity(EntityManager em) {
        Regulation regulation = new Regulation()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .deleted(DEFAULT_DELETED)
            .notes(DEFAULT_NOTES);
        return regulation;
    }

    @Before
    public void initTest() {
        regulation = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegulation() throws Exception {
        int databaseSizeBeforeCreate = regulationRepository.findAll().size();

        // Create the Regulation
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);
        restRegulationMockMvc.perform(post("/api/regulations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regulationDTO)))
            .andExpect(status().isCreated());

        // Validate the Regulation in the database
        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeCreate + 1);
        Regulation testRegulation = regulationList.get(regulationList.size() - 1);
        assertThat(testRegulation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRegulation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRegulation.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testRegulation.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createRegulationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regulationRepository.findAll().size();

        // Create the Regulation with an existing ID
        regulation.setId(1L);
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegulationMockMvc.perform(post("/api/regulations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regulationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regulation in the database
        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = regulationRepository.findAll().size();
        // set the field null
        regulation.setName(null);

        // Create the Regulation, which fails.
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);

        restRegulationMockMvc.perform(post("/api/regulations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regulationDTO)))
            .andExpect(status().isBadRequest());

        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = regulationRepository.findAll().size();
        // set the field null
        regulation.setType(null);

        // Create the Regulation, which fails.
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);

        restRegulationMockMvc.perform(post("/api/regulations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regulationDTO)))
            .andExpect(status().isBadRequest());

        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegulations() throws Exception {
        // Initialize the database
        regulationRepository.saveAndFlush(regulation);

        // Get all the regulationList
        restRegulationMockMvc.perform(get("/api/regulations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regulation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getRegulation() throws Exception {
        // Initialize the database
        regulationRepository.saveAndFlush(regulation);

        // Get the regulation
        restRegulationMockMvc.perform(get("/api/regulations/{id}", regulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(regulation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegulation() throws Exception {
        // Get the regulation
        restRegulationMockMvc.perform(get("/api/regulations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegulation() throws Exception {
        // Initialize the database
        regulationRepository.saveAndFlush(regulation);
        int databaseSizeBeforeUpdate = regulationRepository.findAll().size();

        // Update the regulation
        Regulation updatedRegulation = regulationRepository.findOne(regulation.getId());
        // Disconnect from session so that the updates on updatedRegulation are not directly saved in db
        em.detach(updatedRegulation);
        updatedRegulation
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .deleted(UPDATED_DELETED)
            .notes(UPDATED_NOTES);
        RegulationDTO regulationDTO = regulationMapper.toDto(updatedRegulation);

        restRegulationMockMvc.perform(put("/api/regulations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regulationDTO)))
            .andExpect(status().isOk());

        // Validate the Regulation in the database
        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeUpdate);
        Regulation testRegulation = regulationList.get(regulationList.size() - 1);
        assertThat(testRegulation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegulation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRegulation.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testRegulation.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingRegulation() throws Exception {
        int databaseSizeBeforeUpdate = regulationRepository.findAll().size();

        // Create the Regulation
        RegulationDTO regulationDTO = regulationMapper.toDto(regulation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegulationMockMvc.perform(put("/api/regulations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regulationDTO)))
            .andExpect(status().isCreated());

        // Validate the Regulation in the database
        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegulation() throws Exception {
        // Initialize the database
        regulationRepository.saveAndFlush(regulation);
        int databaseSizeBeforeDelete = regulationRepository.findAll().size();

        // Get the regulation
        restRegulationMockMvc.perform(delete("/api/regulations/{id}", regulation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Regulation> regulationList = regulationRepository.findAll();
        assertThat(regulationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regulation.class);
        Regulation regulation1 = new Regulation();
        regulation1.setId(1L);
        Regulation regulation2 = new Regulation();
        regulation2.setId(regulation1.getId());
        assertThat(regulation1).isEqualTo(regulation2);
        regulation2.setId(2L);
        assertThat(regulation1).isNotEqualTo(regulation2);
        regulation1.setId(null);
        assertThat(regulation1).isNotEqualTo(regulation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegulationDTO.class);
        RegulationDTO regulationDTO1 = new RegulationDTO();
        regulationDTO1.setId(1L);
        RegulationDTO regulationDTO2 = new RegulationDTO();
        assertThat(regulationDTO1).isNotEqualTo(regulationDTO2);
        regulationDTO2.setId(regulationDTO1.getId());
        assertThat(regulationDTO1).isEqualTo(regulationDTO2);
        regulationDTO2.setId(2L);
        assertThat(regulationDTO1).isNotEqualTo(regulationDTO2);
        regulationDTO1.setId(null);
        assertThat(regulationDTO1).isNotEqualTo(regulationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(regulationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(regulationMapper.fromId(null)).isNull();
    }
}
