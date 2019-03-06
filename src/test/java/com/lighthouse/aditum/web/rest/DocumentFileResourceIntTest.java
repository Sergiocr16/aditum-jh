package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.DocumentFile;
import com.lighthouse.aditum.repository.DocumentFileRepository;
import com.lighthouse.aditum.service.DocumentFileService;
import com.lighthouse.aditum.service.dto.DocumentFileDTO;
import com.lighthouse.aditum.service.mapper.DocumentFileMapper;
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
 * Test class for the DocumentFileResource REST controller.
 *
 * @see DocumentFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class DocumentFileResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    @Autowired
    private DocumentFileRepository documentFileRepository;

    @Autowired
    private DocumentFileMapper documentFileMapper;

    @Autowired
    private DocumentFileService documentFileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentFileMockMvc;

    private DocumentFile documentFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentFileResource documentFileResource = new DocumentFileResource(documentFileService);
        this.restDocumentFileMockMvc = MockMvcBuilders.standaloneSetup(documentFileResource)
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
    public static DocumentFile createEntity(EntityManager em) {
        DocumentFile documentFile = new DocumentFile()
            .description(DEFAULT_DESCRIPTION)
            .name(DEFAULT_NAME)
            .reference(DEFAULT_REFERENCE)
            .status(DEFAULT_STATUS)
            .deleted(DEFAULT_DELETED);
        return documentFile;
    }

    @Before
    public void initTest() {
        documentFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentFile() throws Exception {
        int databaseSizeBeforeCreate = documentFileRepository.findAll().size();

        // Create the DocumentFile
        DocumentFileDTO documentFileDTO = documentFileMapper.toDto(documentFile);
        restDocumentFileMockMvc.perform(post("/api/document-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentFileDTO)))
            .andExpect(status().isCreated());

        // Validate the DocumentFile in the database
        List<DocumentFile> documentFileList = documentFileRepository.findAll();
        assertThat(documentFileList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentFile testDocumentFile = documentFileList.get(documentFileList.size() - 1);
        assertThat(testDocumentFile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocumentFile.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testDocumentFile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDocumentFile.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createDocumentFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentFileRepository.findAll().size();

        // Create the DocumentFile with an existing ID
        documentFile.setId(1L);
        DocumentFileDTO documentFileDTO = documentFileMapper.toDto(documentFile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentFileMockMvc.perform(post("/api/document-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentFileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentFile in the database
        List<DocumentFile> documentFileList = documentFileRepository.findAll();
        assertThat(documentFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDocumentFiles() throws Exception {
        // Initialize the database
        documentFileRepository.saveAndFlush(documentFile);

        // Get all the documentFileList
        restDocumentFileMockMvc.perform(get("/api/document-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    public void getDocumentFile() throws Exception {
        // Initialize the database
        documentFileRepository.saveAndFlush(documentFile);

        // Get the documentFile
        restDocumentFileMockMvc.perform(get("/api/document-files/{id}", documentFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentFile.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingDocumentFile() throws Exception {
        // Get the documentFile
        restDocumentFileMockMvc.perform(get("/api/document-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentFile() throws Exception {
        // Initialize the database
        documentFileRepository.saveAndFlush(documentFile);
        int databaseSizeBeforeUpdate = documentFileRepository.findAll().size();

        // Update the documentFile
        DocumentFile updatedDocumentFile = documentFileRepository.findOne(documentFile.getId());
        // Disconnect from session so that the updates on updatedDocumentFile are not directly saved in db
        em.detach(updatedDocumentFile);
        updatedDocumentFile
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .reference(UPDATED_REFERENCE)
            .status(UPDATED_STATUS)
            .deleted(UPDATED_DELETED);
        DocumentFileDTO documentFileDTO = documentFileMapper.toDto(updatedDocumentFile);

        restDocumentFileMockMvc.perform(put("/api/document-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentFileDTO)))
            .andExpect(status().isOk());

        // Validate the DocumentFile in the database
        List<DocumentFile> documentFileList = documentFileRepository.findAll();
        assertThat(documentFileList).hasSize(databaseSizeBeforeUpdate);
        DocumentFile testDocumentFile = documentFileList.get(documentFileList.size() - 1);
        assertThat(testDocumentFile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentFile.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testDocumentFile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentFile.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentFile() throws Exception {
        int databaseSizeBeforeUpdate = documentFileRepository.findAll().size();

        // Create the DocumentFile
        DocumentFileDTO documentFileDTO = documentFileMapper.toDto(documentFile);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentFileMockMvc.perform(put("/api/document-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentFileDTO)))
            .andExpect(status().isCreated());

        // Validate the DocumentFile in the database
        List<DocumentFile> documentFileList = documentFileRepository.findAll();
        assertThat(documentFileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocumentFile() throws Exception {
        // Initialize the database
        documentFileRepository.saveAndFlush(documentFile);
        int databaseSizeBeforeDelete = documentFileRepository.findAll().size();

        // Get the documentFile
        restDocumentFileMockMvc.perform(delete("/api/document-files/{id}", documentFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DocumentFile> documentFileList = documentFileRepository.findAll();
        assertThat(documentFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentFile.class);
        DocumentFile documentFile1 = new DocumentFile();
        documentFile1.setId(1L);
        DocumentFile documentFile2 = new DocumentFile();
        documentFile2.setId(documentFile1.getId());
        assertThat(documentFile1).isEqualTo(documentFile2);
        documentFile2.setId(2L);
        assertThat(documentFile1).isNotEqualTo(documentFile2);
        documentFile1.setId(null);
        assertThat(documentFile1).isNotEqualTo(documentFile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentFileDTO.class);
        DocumentFileDTO documentFileDTO1 = new DocumentFileDTO();
        documentFileDTO1.setId(1L);
        DocumentFileDTO documentFileDTO2 = new DocumentFileDTO();
        assertThat(documentFileDTO1).isNotEqualTo(documentFileDTO2);
        documentFileDTO2.setId(documentFileDTO1.getId());
        assertThat(documentFileDTO1).isEqualTo(documentFileDTO2);
        documentFileDTO2.setId(2L);
        assertThat(documentFileDTO1).isNotEqualTo(documentFileDTO2);
        documentFileDTO1.setId(null);
        assertThat(documentFileDTO1).isNotEqualTo(documentFileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(documentFileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(documentFileMapper.fromId(null)).isNull();
    }
}
