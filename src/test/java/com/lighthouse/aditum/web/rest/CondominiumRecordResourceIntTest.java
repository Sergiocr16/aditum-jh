package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.CondominiumRecord;
import com.lighthouse.aditum.repository.CondominiumRecordRepository;
import com.lighthouse.aditum.service.CondominiumRecordService;
import com.lighthouse.aditum.service.dto.CondominiumRecordDTO;
import com.lighthouse.aditum.service.mapper.CondominiumRecordMapper;
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
 * Test class for the CondominiumRecordResource REST controller.
 *
 * @see CondominiumRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CondominiumRecordResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPLOAD_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOAD_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private CondominiumRecordRepository condominiumRecordRepository;

    @Autowired
    private CondominiumRecordMapper condominiumRecordMapper;

    @Autowired
    private CondominiumRecordService condominiumRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCondominiumRecordMockMvc;

    private CondominiumRecord condominiumRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CondominiumRecordResource condominiumRecordResource = new CondominiumRecordResource(condominiumRecordService);
        this.restCondominiumRecordMockMvc = MockMvcBuilders.standaloneSetup(condominiumRecordResource)
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
    public static CondominiumRecord createEntity(EntityManager em) {
        CondominiumRecord condominiumRecord = new CondominiumRecord()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .fileUrl(DEFAULT_FILE_URL)
            .fileName(DEFAULT_FILE_NAME)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .deleted(DEFAULT_DELETED)
            .status(DEFAULT_STATUS);
        return condominiumRecord;
    }

    @Before
    public void initTest() {
        condominiumRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createCondominiumRecord() throws Exception {
        int databaseSizeBeforeCreate = condominiumRecordRepository.findAll().size();

        // Create the CondominiumRecord
        CondominiumRecordDTO condominiumRecordDTO = condominiumRecordMapper.toDto(condominiumRecord);
        restCondominiumRecordMockMvc.perform(post("/api/condominium-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condominiumRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the CondominiumRecord in the database
        List<CondominiumRecord> condominiumRecordList = condominiumRecordRepository.findAll();
        assertThat(condominiumRecordList).hasSize(databaseSizeBeforeCreate + 1);
        CondominiumRecord testCondominiumRecord = condominiumRecordList.get(condominiumRecordList.size() - 1);
        assertThat(testCondominiumRecord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCondominiumRecord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCondominiumRecord.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testCondominiumRecord.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testCondominiumRecord.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testCondominiumRecord.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testCondominiumRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCondominiumRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = condominiumRecordRepository.findAll().size();

        // Create the CondominiumRecord with an existing ID
        condominiumRecord.setId(1L);
        CondominiumRecordDTO condominiumRecordDTO = condominiumRecordMapper.toDto(condominiumRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCondominiumRecordMockMvc.perform(post("/api/condominium-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condominiumRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CondominiumRecord in the database
        List<CondominiumRecord> condominiumRecordList = condominiumRecordRepository.findAll();
        assertThat(condominiumRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCondominiumRecords() throws Exception {
        // Initialize the database
        condominiumRecordRepository.saveAndFlush(condominiumRecord);

        // Get all the condominiumRecordList
        restCondominiumRecordMockMvc.perform(get("/api/condominium-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condominiumRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL.toString())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(sameInstant(DEFAULT_UPLOAD_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getCondominiumRecord() throws Exception {
        // Initialize the database
        condominiumRecordRepository.saveAndFlush(condominiumRecord);

        // Get the condominiumRecord
        restCondominiumRecordMockMvc.perform(get("/api/condominium-records/{id}", condominiumRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(condominiumRecord.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL.toString()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.uploadDate").value(sameInstant(DEFAULT_UPLOAD_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCondominiumRecord() throws Exception {
        // Get the condominiumRecord
        restCondominiumRecordMockMvc.perform(get("/api/condominium-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCondominiumRecord() throws Exception {
        // Initialize the database
        condominiumRecordRepository.saveAndFlush(condominiumRecord);
        int databaseSizeBeforeUpdate = condominiumRecordRepository.findAll().size();

        // Update the condominiumRecord
        CondominiumRecord updatedCondominiumRecord = condominiumRecordRepository.findOne(condominiumRecord.getId());
        // Disconnect from session so that the updates on updatedCondominiumRecord are not directly saved in db
        em.detach(updatedCondominiumRecord);
        updatedCondominiumRecord
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .fileUrl(UPDATED_FILE_URL)
            .fileName(UPDATED_FILE_NAME)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);
        CondominiumRecordDTO condominiumRecordDTO = condominiumRecordMapper.toDto(updatedCondominiumRecord);

        restCondominiumRecordMockMvc.perform(put("/api/condominium-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condominiumRecordDTO)))
            .andExpect(status().isOk());

        // Validate the CondominiumRecord in the database
        List<CondominiumRecord> condominiumRecordList = condominiumRecordRepository.findAll();
        assertThat(condominiumRecordList).hasSize(databaseSizeBeforeUpdate);
        CondominiumRecord testCondominiumRecord = condominiumRecordList.get(condominiumRecordList.size() - 1);
        assertThat(testCondominiumRecord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCondominiumRecord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCondominiumRecord.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testCondominiumRecord.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testCondominiumRecord.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testCondominiumRecord.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testCondominiumRecord.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCondominiumRecord() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRecordRepository.findAll().size();

        // Create the CondominiumRecord
        CondominiumRecordDTO condominiumRecordDTO = condominiumRecordMapper.toDto(condominiumRecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCondominiumRecordMockMvc.perform(put("/api/condominium-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condominiumRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the CondominiumRecord in the database
        List<CondominiumRecord> condominiumRecordList = condominiumRecordRepository.findAll();
        assertThat(condominiumRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCondominiumRecord() throws Exception {
        // Initialize the database
        condominiumRecordRepository.saveAndFlush(condominiumRecord);
        int databaseSizeBeforeDelete = condominiumRecordRepository.findAll().size();

        // Get the condominiumRecord
        restCondominiumRecordMockMvc.perform(delete("/api/condominium-records/{id}", condominiumRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CondominiumRecord> condominiumRecordList = condominiumRecordRepository.findAll();
        assertThat(condominiumRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CondominiumRecord.class);
        CondominiumRecord condominiumRecord1 = new CondominiumRecord();
        condominiumRecord1.setId(1L);
        CondominiumRecord condominiumRecord2 = new CondominiumRecord();
        condominiumRecord2.setId(condominiumRecord1.getId());
        assertThat(condominiumRecord1).isEqualTo(condominiumRecord2);
        condominiumRecord2.setId(2L);
        assertThat(condominiumRecord1).isNotEqualTo(condominiumRecord2);
        condominiumRecord1.setId(null);
        assertThat(condominiumRecord1).isNotEqualTo(condominiumRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CondominiumRecordDTO.class);
        CondominiumRecordDTO condominiumRecordDTO1 = new CondominiumRecordDTO();
        condominiumRecordDTO1.setId(1L);
        CondominiumRecordDTO condominiumRecordDTO2 = new CondominiumRecordDTO();
        assertThat(condominiumRecordDTO1).isNotEqualTo(condominiumRecordDTO2);
        condominiumRecordDTO2.setId(condominiumRecordDTO1.getId());
        assertThat(condominiumRecordDTO1).isEqualTo(condominiumRecordDTO2);
        condominiumRecordDTO2.setId(2L);
        assertThat(condominiumRecordDTO1).isNotEqualTo(condominiumRecordDTO2);
        condominiumRecordDTO1.setId(null);
        assertThat(condominiumRecordDTO1).isNotEqualTo(condominiumRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(condominiumRecordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(condominiumRecordMapper.fromId(null)).isNull();
    }
}
