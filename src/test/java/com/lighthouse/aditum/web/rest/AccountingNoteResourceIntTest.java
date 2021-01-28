package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.AccountingNote;
import com.lighthouse.aditum.repository.AccountingNoteRepository;
import com.lighthouse.aditum.service.AccountingNoteService;
import com.lighthouse.aditum.service.dto.AccountingNoteDTO;
import com.lighthouse.aditum.service.mapper.AccountingNoteMapper;
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
 * Test class for the AccountingNoteResource REST controller.
 *
 * @see AccountingNoteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class AccountingNoteResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIXED = 1;
    private static final Integer UPDATED_FIXED = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    @Autowired
    private AccountingNoteRepository accountingNoteRepository;

    @Autowired
    private AccountingNoteMapper accountingNoteMapper;

    @Autowired
    private AccountingNoteService accountingNoteService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountingNoteMockMvc;

    private AccountingNote accountingNote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountingNoteResource accountingNoteResource = new AccountingNoteResource(accountingNoteService);
        this.restAccountingNoteMockMvc = MockMvcBuilders.standaloneSetup(accountingNoteResource)
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
    public static AccountingNote createEntity(EntityManager em) {
        AccountingNote accountingNote = new AccountingNote()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .color(DEFAULT_COLOR)
            .fixed(DEFAULT_FIXED)
            .deleted(DEFAULT_DELETED)
            .creationDate(DEFAULT_CREATION_DATE)
            .modificationDate(DEFAULT_MODIFICATION_DATE)
            .fileName(DEFAULT_FILE_NAME)
            .fileUrl(DEFAULT_FILE_URL);
        return accountingNote;
    }

    @Before
    public void initTest() {
        accountingNote = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountingNote() throws Exception {
        int databaseSizeBeforeCreate = accountingNoteRepository.findAll().size();

        // Create the AccountingNote
        AccountingNoteDTO accountingNoteDTO = accountingNoteMapper.toDto(accountingNote);
        restAccountingNoteMockMvc.perform(post("/api/accounting-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingNoteDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountingNote in the database
        List<AccountingNote> accountingNoteList = accountingNoteRepository.findAll();
        assertThat(accountingNoteList).hasSize(databaseSizeBeforeCreate + 1);
        AccountingNote testAccountingNote = accountingNoteList.get(accountingNoteList.size() - 1);
        assertThat(testAccountingNote.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAccountingNote.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccountingNote.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testAccountingNote.getFixed()).isEqualTo(DEFAULT_FIXED);
        assertThat(testAccountingNote.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testAccountingNote.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAccountingNote.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testAccountingNote.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAccountingNote.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
    }

    @Test
    @Transactional
    public void createAccountingNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountingNoteRepository.findAll().size();

        // Create the AccountingNote with an existing ID
        accountingNote.setId(1L);
        AccountingNoteDTO accountingNoteDTO = accountingNoteMapper.toDto(accountingNote);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountingNoteMockMvc.perform(post("/api/accounting-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingNoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccountingNote in the database
        List<AccountingNote> accountingNoteList = accountingNoteRepository.findAll();
        assertThat(accountingNoteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountingNotes() throws Exception {
        // Initialize the database
        accountingNoteRepository.saveAndFlush(accountingNote);

        // Get all the accountingNoteList
        restAccountingNoteMockMvc.perform(get("/api/accounting-notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountingNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].fixed").value(hasItem(DEFAULT_FIXED)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(sameInstant(DEFAULT_MODIFICATION_DATE))))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL.toString())));
    }

    @Test
    @Transactional
    public void getAccountingNote() throws Exception {
        // Initialize the database
        accountingNoteRepository.saveAndFlush(accountingNote);

        // Get the accountingNote
        restAccountingNoteMockMvc.perform(get("/api/accounting-notes/{id}", accountingNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountingNote.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.fixed").value(DEFAULT_FIXED))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.modificationDate").value(sameInstant(DEFAULT_MODIFICATION_DATE)))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountingNote() throws Exception {
        // Get the accountingNote
        restAccountingNoteMockMvc.perform(get("/api/accounting-notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountingNote() throws Exception {
        // Initialize the database
        accountingNoteRepository.saveAndFlush(accountingNote);
        int databaseSizeBeforeUpdate = accountingNoteRepository.findAll().size();

        // Update the accountingNote
        AccountingNote updatedAccountingNote = accountingNoteRepository.findOne(accountingNote.getId());
        // Disconnect from session so that the updates on updatedAccountingNote are not directly saved in db
        em.detach(updatedAccountingNote);
        updatedAccountingNote
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .color(UPDATED_COLOR)
            .fixed(UPDATED_FIXED)
            .deleted(UPDATED_DELETED)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE)
            .fileName(UPDATED_FILE_NAME)
            .fileUrl(UPDATED_FILE_URL);
        AccountingNoteDTO accountingNoteDTO = accountingNoteMapper.toDto(updatedAccountingNote);

        restAccountingNoteMockMvc.perform(put("/api/accounting-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingNoteDTO)))
            .andExpect(status().isOk());

        // Validate the AccountingNote in the database
        List<AccountingNote> accountingNoteList = accountingNoteRepository.findAll();
        assertThat(accountingNoteList).hasSize(databaseSizeBeforeUpdate);
        AccountingNote testAccountingNote = accountingNoteList.get(accountingNoteList.size() - 1);
        assertThat(testAccountingNote.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAccountingNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccountingNote.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testAccountingNote.getFixed()).isEqualTo(UPDATED_FIXED);
        assertThat(testAccountingNote.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testAccountingNote.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAccountingNote.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testAccountingNote.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAccountingNote.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountingNote() throws Exception {
        int databaseSizeBeforeUpdate = accountingNoteRepository.findAll().size();

        // Create the AccountingNote
        AccountingNoteDTO accountingNoteDTO = accountingNoteMapper.toDto(accountingNote);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountingNoteMockMvc.perform(put("/api/accounting-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountingNoteDTO)))
            .andExpect(status().isCreated());

        // Validate the AccountingNote in the database
        List<AccountingNote> accountingNoteList = accountingNoteRepository.findAll();
        assertThat(accountingNoteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountingNote() throws Exception {
        // Initialize the database
        accountingNoteRepository.saveAndFlush(accountingNote);
        int databaseSizeBeforeDelete = accountingNoteRepository.findAll().size();

        // Get the accountingNote
        restAccountingNoteMockMvc.perform(delete("/api/accounting-notes/{id}", accountingNote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountingNote> accountingNoteList = accountingNoteRepository.findAll();
        assertThat(accountingNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingNote.class);
        AccountingNote accountingNote1 = new AccountingNote();
        accountingNote1.setId(1L);
        AccountingNote accountingNote2 = new AccountingNote();
        accountingNote2.setId(accountingNote1.getId());
        assertThat(accountingNote1).isEqualTo(accountingNote2);
        accountingNote2.setId(2L);
        assertThat(accountingNote1).isNotEqualTo(accountingNote2);
        accountingNote1.setId(null);
        assertThat(accountingNote1).isNotEqualTo(accountingNote2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountingNoteDTO.class);
        AccountingNoteDTO accountingNoteDTO1 = new AccountingNoteDTO();
        accountingNoteDTO1.setId(1L);
        AccountingNoteDTO accountingNoteDTO2 = new AccountingNoteDTO();
        assertThat(accountingNoteDTO1).isNotEqualTo(accountingNoteDTO2);
        accountingNoteDTO2.setId(accountingNoteDTO1.getId());
        assertThat(accountingNoteDTO1).isEqualTo(accountingNoteDTO2);
        accountingNoteDTO2.setId(2L);
        assertThat(accountingNoteDTO1).isNotEqualTo(accountingNoteDTO2);
        accountingNoteDTO1.setId(null);
        assertThat(accountingNoteDTO1).isNotEqualTo(accountingNoteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(accountingNoteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(accountingNoteMapper.fromId(null)).isNull();
    }
}
