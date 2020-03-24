package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.RevisionConfig;
import com.lighthouse.aditum.repository.RevisionConfigRepository;
import com.lighthouse.aditum.service.RevisionConfigService;
import com.lighthouse.aditum.service.dto.RevisionConfigDTO;
import com.lighthouse.aditum.service.mapper.RevisionConfigMapper;
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
 * Test class for the RevisionConfigResource REST controller.
 *
 * @see RevisionConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class RevisionConfigResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RevisionConfigRepository revisionConfigRepository;

    @Autowired
    private RevisionConfigMapper revisionConfigMapper;

    @Autowired
    private RevisionConfigService revisionConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRevisionConfigMockMvc;

    private RevisionConfig revisionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RevisionConfigResource revisionConfigResource = new RevisionConfigResource(revisionConfigService);
        this.restRevisionConfigMockMvc = MockMvcBuilders.standaloneSetup(revisionConfigResource)
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
    public static RevisionConfig createEntity(EntityManager em) {
        RevisionConfig revisionConfig = new RevisionConfig()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return revisionConfig;
    }

    @Before
    public void initTest() {
        revisionConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevisionConfig() throws Exception {
        int databaseSizeBeforeCreate = revisionConfigRepository.findAll().size();

        // Create the RevisionConfig
        RevisionConfigDTO revisionConfigDTO = revisionConfigMapper.toDto(revisionConfig);
        restRevisionConfigMockMvc.perform(post("/api/revision-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionConfig in the database
        List<RevisionConfig> revisionConfigList = revisionConfigRepository.findAll();
        assertThat(revisionConfigList).hasSize(databaseSizeBeforeCreate + 1);
        RevisionConfig testRevisionConfig = revisionConfigList.get(revisionConfigList.size() - 1);
        assertThat(testRevisionConfig.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRevisionConfig.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRevisionConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revisionConfigRepository.findAll().size();

        // Create the RevisionConfig with an existing ID
        revisionConfig.setId(1L);
        RevisionConfigDTO revisionConfigDTO = revisionConfigMapper.toDto(revisionConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionConfigMockMvc.perform(post("/api/revision-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RevisionConfig in the database
        List<RevisionConfig> revisionConfigList = revisionConfigRepository.findAll();
        assertThat(revisionConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRevisionConfigs() throws Exception {
        // Initialize the database
        revisionConfigRepository.saveAndFlush(revisionConfig);

        // Get all the revisionConfigList
        restRevisionConfigMockMvc.perform(get("/api/revision-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revisionConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRevisionConfig() throws Exception {
        // Initialize the database
        revisionConfigRepository.saveAndFlush(revisionConfig);

        // Get the revisionConfig
        restRevisionConfigMockMvc.perform(get("/api/revision-configs/{id}", revisionConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(revisionConfig.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRevisionConfig() throws Exception {
        // Get the revisionConfig
        restRevisionConfigMockMvc.perform(get("/api/revision-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevisionConfig() throws Exception {
        // Initialize the database
        revisionConfigRepository.saveAndFlush(revisionConfig);
        int databaseSizeBeforeUpdate = revisionConfigRepository.findAll().size();

        // Update the revisionConfig
        RevisionConfig updatedRevisionConfig = revisionConfigRepository.findOne(revisionConfig.getId());
        // Disconnect from session so that the updates on updatedRevisionConfig are not directly saved in db
        em.detach(updatedRevisionConfig);
        updatedRevisionConfig
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        RevisionConfigDTO revisionConfigDTO = revisionConfigMapper.toDto(updatedRevisionConfig);

        restRevisionConfigMockMvc.perform(put("/api/revision-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigDTO)))
            .andExpect(status().isOk());

        // Validate the RevisionConfig in the database
        List<RevisionConfig> revisionConfigList = revisionConfigRepository.findAll();
        assertThat(revisionConfigList).hasSize(databaseSizeBeforeUpdate);
        RevisionConfig testRevisionConfig = revisionConfigList.get(revisionConfigList.size() - 1);
        assertThat(testRevisionConfig.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRevisionConfig.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRevisionConfig() throws Exception {
        int databaseSizeBeforeUpdate = revisionConfigRepository.findAll().size();

        // Create the RevisionConfig
        RevisionConfigDTO revisionConfigDTO = revisionConfigMapper.toDto(revisionConfig);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRevisionConfigMockMvc.perform(put("/api/revision-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(revisionConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the RevisionConfig in the database
        List<RevisionConfig> revisionConfigList = revisionConfigRepository.findAll();
        assertThat(revisionConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRevisionConfig() throws Exception {
        // Initialize the database
        revisionConfigRepository.saveAndFlush(revisionConfig);
        int databaseSizeBeforeDelete = revisionConfigRepository.findAll().size();

        // Get the revisionConfig
        restRevisionConfigMockMvc.perform(delete("/api/revision-configs/{id}", revisionConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RevisionConfig> revisionConfigList = revisionConfigRepository.findAll();
        assertThat(revisionConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionConfig.class);
        RevisionConfig revisionConfig1 = new RevisionConfig();
        revisionConfig1.setId(1L);
        RevisionConfig revisionConfig2 = new RevisionConfig();
        revisionConfig2.setId(revisionConfig1.getId());
        assertThat(revisionConfig1).isEqualTo(revisionConfig2);
        revisionConfig2.setId(2L);
        assertThat(revisionConfig1).isNotEqualTo(revisionConfig2);
        revisionConfig1.setId(null);
        assertThat(revisionConfig1).isNotEqualTo(revisionConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RevisionConfigDTO.class);
        RevisionConfigDTO revisionConfigDTO1 = new RevisionConfigDTO();
        revisionConfigDTO1.setId(1L);
        RevisionConfigDTO revisionConfigDTO2 = new RevisionConfigDTO();
        assertThat(revisionConfigDTO1).isNotEqualTo(revisionConfigDTO2);
        revisionConfigDTO2.setId(revisionConfigDTO1.getId());
        assertThat(revisionConfigDTO1).isEqualTo(revisionConfigDTO2);
        revisionConfigDTO2.setId(2L);
        assertThat(revisionConfigDTO1).isNotEqualTo(revisionConfigDTO2);
        revisionConfigDTO1.setId(null);
        assertThat(revisionConfigDTO1).isNotEqualTo(revisionConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(revisionConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(revisionConfigMapper.fromId(null)).isNull();
    }
}
