package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.MensualBillingFile;
import com.lighthouse.aditum.repository.MensualBillingFileRepository;
import com.lighthouse.aditum.service.MensualBillingFileService;
import com.lighthouse.aditum.service.dto.MensualBillingFileDTO;
import com.lighthouse.aditum.service.mapper.MensualBillingFileMapper;
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
 * Test class for the MensualBillingFileResource REST controller.
 *
 * @see MensualBillingFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class MensualBillingFileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private MensualBillingFileRepository mensualBillingFileRepository;

    @Autowired
    private MensualBillingFileMapper mensualBillingFileMapper;

    @Autowired
    private MensualBillingFileService mensualBillingFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMensualBillingFileMockMvc;

    private MensualBillingFile mensualBillingFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MensualBillingFileResource mensualBillingFileResource = new MensualBillingFileResource(mensualBillingFileService);
        this.restMensualBillingFileMockMvc = MockMvcBuilders.standaloneSetup(mensualBillingFileResource)
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
    public static MensualBillingFile createEntity(EntityManager em) {
        MensualBillingFile mensualBillingFile = new MensualBillingFile()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .deleted(DEFAULT_DELETED);
        return mensualBillingFile;
    }

    @Before
    public void initTest() {
        mensualBillingFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createMensualBillingFile() throws Exception {
        int databaseSizeBeforeCreate = mensualBillingFileRepository.findAll().size();

        // Create the MensualBillingFile
        MensualBillingFileDTO mensualBillingFileDTO = mensualBillingFileMapper.toDto(mensualBillingFile);
        restMensualBillingFileMockMvc.perform(post("/api/mensual-billing-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensualBillingFileDTO)))
            .andExpect(status().isCreated());

        // Validate the MensualBillingFile in the database
        List<MensualBillingFile> mensualBillingFileList = mensualBillingFileRepository.findAll();
        assertThat(mensualBillingFileList).hasSize(databaseSizeBeforeCreate + 1);
        MensualBillingFile testMensualBillingFile = mensualBillingFileList.get(mensualBillingFileList.size() - 1);
        assertThat(testMensualBillingFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMensualBillingFile.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testMensualBillingFile.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testMensualBillingFile.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testMensualBillingFile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMensualBillingFile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMensualBillingFile.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createMensualBillingFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mensualBillingFileRepository.findAll().size();

        // Create the MensualBillingFile with an existing ID
        mensualBillingFile.setId(1L);
        MensualBillingFileDTO mensualBillingFileDTO = mensualBillingFileMapper.toDto(mensualBillingFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMensualBillingFileMockMvc.perform(post("/api/mensual-billing-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensualBillingFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MensualBillingFile in the database
        List<MensualBillingFile> mensualBillingFileList = mensualBillingFileRepository.findAll();
        assertThat(mensualBillingFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMensualBillingFiles() throws Exception {
        // Initialize the database
        mensualBillingFileRepository.saveAndFlush(mensualBillingFile);

        // Get all the mensualBillingFileList
        restMensualBillingFileMockMvc.perform(get("/api/mensual-billing-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensualBillingFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getMensualBillingFile() throws Exception {
        // Initialize the database
        mensualBillingFileRepository.saveAndFlush(mensualBillingFile);

        // Get the mensualBillingFile
        restMensualBillingFileMockMvc.perform(get("/api/mensual-billing-files/{id}", mensualBillingFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mensualBillingFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingMensualBillingFile() throws Exception {
        // Get the mensualBillingFile
        restMensualBillingFileMockMvc.perform(get("/api/mensual-billing-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMensualBillingFile() throws Exception {
        // Initialize the database
        mensualBillingFileRepository.saveAndFlush(mensualBillingFile);
        int databaseSizeBeforeUpdate = mensualBillingFileRepository.findAll().size();

        // Update the mensualBillingFile
        MensualBillingFile updatedMensualBillingFile = mensualBillingFileRepository.findOne(mensualBillingFile.getId());
        // Disconnect from session so that the updates on updatedMensualBillingFile are not directly saved in db
        em.detach(updatedMensualBillingFile);
        updatedMensualBillingFile
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED);
        MensualBillingFileDTO mensualBillingFileDTO = mensualBillingFileMapper.toDto(updatedMensualBillingFile);

        restMensualBillingFileMockMvc.perform(put("/api/mensual-billing-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensualBillingFileDTO)))
            .andExpect(status().isOk());

        // Validate the MensualBillingFile in the database
        List<MensualBillingFile> mensualBillingFileList = mensualBillingFileRepository.findAll();
        assertThat(mensualBillingFileList).hasSize(databaseSizeBeforeUpdate);
        MensualBillingFile testMensualBillingFile = mensualBillingFileList.get(mensualBillingFileList.size() - 1);
        assertThat(testMensualBillingFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMensualBillingFile.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testMensualBillingFile.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testMensualBillingFile.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testMensualBillingFile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMensualBillingFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMensualBillingFile.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingMensualBillingFile() throws Exception {
        int databaseSizeBeforeUpdate = mensualBillingFileRepository.findAll().size();

        // Create the MensualBillingFile
        MensualBillingFileDTO mensualBillingFileDTO = mensualBillingFileMapper.toDto(mensualBillingFile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMensualBillingFileMockMvc.perform(put("/api/mensual-billing-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mensualBillingFileDTO)))
            .andExpect(status().isCreated());

        // Validate the MensualBillingFile in the database
        List<MensualBillingFile> mensualBillingFileList = mensualBillingFileRepository.findAll();
        assertThat(mensualBillingFileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMensualBillingFile() throws Exception {
        // Initialize the database
        mensualBillingFileRepository.saveAndFlush(mensualBillingFile);
        int databaseSizeBeforeDelete = mensualBillingFileRepository.findAll().size();

        // Get the mensualBillingFile
        restMensualBillingFileMockMvc.perform(delete("/api/mensual-billing-files/{id}", mensualBillingFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MensualBillingFile> mensualBillingFileList = mensualBillingFileRepository.findAll();
        assertThat(mensualBillingFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensualBillingFile.class);
        MensualBillingFile mensualBillingFile1 = new MensualBillingFile();
        mensualBillingFile1.setId(1L);
        MensualBillingFile mensualBillingFile2 = new MensualBillingFile();
        mensualBillingFile2.setId(mensualBillingFile1.getId());
        assertThat(mensualBillingFile1).isEqualTo(mensualBillingFile2);
        mensualBillingFile2.setId(2L);
        assertThat(mensualBillingFile1).isNotEqualTo(mensualBillingFile2);
        mensualBillingFile1.setId(null);
        assertThat(mensualBillingFile1).isNotEqualTo(mensualBillingFile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensualBillingFileDTO.class);
        MensualBillingFileDTO mensualBillingFileDTO1 = new MensualBillingFileDTO();
        mensualBillingFileDTO1.setId(1L);
        MensualBillingFileDTO mensualBillingFileDTO2 = new MensualBillingFileDTO();
        assertThat(mensualBillingFileDTO1).isNotEqualTo(mensualBillingFileDTO2);
        mensualBillingFileDTO2.setId(mensualBillingFileDTO1.getId());
        assertThat(mensualBillingFileDTO1).isEqualTo(mensualBillingFileDTO2);
        mensualBillingFileDTO2.setId(2L);
        assertThat(mensualBillingFileDTO1).isNotEqualTo(mensualBillingFileDTO2);
        mensualBillingFileDTO1.setId(null);
        assertThat(mensualBillingFileDTO1).isNotEqualTo(mensualBillingFileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(mensualBillingFileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(mensualBillingFileMapper.fromId(null)).isNull();
    }
}
