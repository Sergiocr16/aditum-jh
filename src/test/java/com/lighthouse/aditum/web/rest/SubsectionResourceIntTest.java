package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Subsection;
import com.lighthouse.aditum.repository.SubsectionRepository;
import com.lighthouse.aditum.service.SubsectionService;
import com.lighthouse.aditum.service.dto.SubsectionDTO;
import com.lighthouse.aditum.service.mapper.SubsectionMapper;
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
 * Test class for the SubsectionResource REST controller.
 *
 * @see SubsectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class SubsectionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private SubsectionRepository subsectionRepository;

    @Autowired
    private SubsectionMapper subsectionMapper;

    @Autowired
    private SubsectionService subsectionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSubsectionMockMvc;

    private Subsection subsection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubsectionResource subsectionResource = new SubsectionResource(subsectionService);
        this.restSubsectionMockMvc = MockMvcBuilders.standaloneSetup(subsectionResource)
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
    public static Subsection createEntity(EntityManager em) {
        Subsection subsection = new Subsection()
            .description(DEFAULT_DESCRIPTION)
            .order(DEFAULT_ORDER)
            .deleted(DEFAULT_DELETED)
            .notes(DEFAULT_NOTES);
        return subsection;
    }

    @Before
    public void initTest() {
        subsection = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubsection() throws Exception {
        int databaseSizeBeforeCreate = subsectionRepository.findAll().size();

        // Create the Subsection
        SubsectionDTO subsectionDTO = subsectionMapper.toDto(subsection);
        restSubsectionMockMvc.perform(post("/api/subsections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsectionDTO)))
            .andExpect(status().isCreated());

        // Validate the Subsection in the database
        List<Subsection> subsectionList = subsectionRepository.findAll();
        assertThat(subsectionList).hasSize(databaseSizeBeforeCreate + 1);
        Subsection testSubsection = subsectionList.get(subsectionList.size() - 1);
        assertThat(testSubsection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubsection.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testSubsection.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testSubsection.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createSubsectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subsectionRepository.findAll().size();

        // Create the Subsection with an existing ID
        subsection.setId(1L);
        SubsectionDTO subsectionDTO = subsectionMapper.toDto(subsection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubsectionMockMvc.perform(post("/api/subsections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subsection in the database
        List<Subsection> subsectionList = subsectionRepository.findAll();
        assertThat(subsectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSubsections() throws Exception {
        // Initialize the database
        subsectionRepository.saveAndFlush(subsection);

        // Get all the subsectionList
        restSubsectionMockMvc.perform(get("/api/subsections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subsection.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getSubsection() throws Exception {
        // Initialize the database
        subsectionRepository.saveAndFlush(subsection);

        // Get the subsection
        restSubsectionMockMvc.perform(get("/api/subsections/{id}", subsection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subsection.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSubsection() throws Exception {
        // Get the subsection
        restSubsectionMockMvc.perform(get("/api/subsections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubsection() throws Exception {
        // Initialize the database
        subsectionRepository.saveAndFlush(subsection);
        int databaseSizeBeforeUpdate = subsectionRepository.findAll().size();

        // Update the subsection
        Subsection updatedSubsection = subsectionRepository.findOne(subsection.getId());
        // Disconnect from session so that the updates on updatedSubsection are not directly saved in db
        em.detach(updatedSubsection);
        updatedSubsection
            .description(UPDATED_DESCRIPTION)
            .order(UPDATED_ORDER)
            .deleted(UPDATED_DELETED)
            .notes(UPDATED_NOTES);
        SubsectionDTO subsectionDTO = subsectionMapper.toDto(updatedSubsection);

        restSubsectionMockMvc.perform(put("/api/subsections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsectionDTO)))
            .andExpect(status().isOk());

        // Validate the Subsection in the database
        List<Subsection> subsectionList = subsectionRepository.findAll();
        assertThat(subsectionList).hasSize(databaseSizeBeforeUpdate);
        Subsection testSubsection = subsectionList.get(subsectionList.size() - 1);
        assertThat(testSubsection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubsection.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testSubsection.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testSubsection.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingSubsection() throws Exception {
        int databaseSizeBeforeUpdate = subsectionRepository.findAll().size();

        // Create the Subsection
        SubsectionDTO subsectionDTO = subsectionMapper.toDto(subsection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSubsectionMockMvc.perform(put("/api/subsections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsectionDTO)))
            .andExpect(status().isCreated());

        // Validate the Subsection in the database
        List<Subsection> subsectionList = subsectionRepository.findAll();
        assertThat(subsectionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSubsection() throws Exception {
        // Initialize the database
        subsectionRepository.saveAndFlush(subsection);
        int databaseSizeBeforeDelete = subsectionRepository.findAll().size();

        // Get the subsection
        restSubsectionMockMvc.perform(delete("/api/subsections/{id}", subsection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Subsection> subsectionList = subsectionRepository.findAll();
        assertThat(subsectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subsection.class);
        Subsection subsection1 = new Subsection();
        subsection1.setId(1L);
        Subsection subsection2 = new Subsection();
        subsection2.setId(subsection1.getId());
        assertThat(subsection1).isEqualTo(subsection2);
        subsection2.setId(2L);
        assertThat(subsection1).isNotEqualTo(subsection2);
        subsection1.setId(null);
        assertThat(subsection1).isNotEqualTo(subsection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubsectionDTO.class);
        SubsectionDTO subsectionDTO1 = new SubsectionDTO();
        subsectionDTO1.setId(1L);
        SubsectionDTO subsectionDTO2 = new SubsectionDTO();
        assertThat(subsectionDTO1).isNotEqualTo(subsectionDTO2);
        subsectionDTO2.setId(subsectionDTO1.getId());
        assertThat(subsectionDTO1).isEqualTo(subsectionDTO2);
        subsectionDTO2.setId(2L);
        assertThat(subsectionDTO1).isNotEqualTo(subsectionDTO2);
        subsectionDTO1.setId(null);
        assertThat(subsectionDTO1).isNotEqualTo(subsectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(subsectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(subsectionMapper.fromId(null)).isNull();
    }
}
