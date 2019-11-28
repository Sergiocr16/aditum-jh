package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Protocol;
import com.lighthouse.aditum.repository.ProtocolRepository;
import com.lighthouse.aditum.service.ProtocolService;
import com.lighthouse.aditum.service.dto.ProtocolDTO;
import com.lighthouse.aditum.service.mapper.ProtocolMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProtocolResource REST controller.
 *
 * @see ProtocolResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class ProtocolResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private ProtocolMapper protocolMapper;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProtocolMockMvc;

    private Protocol protocol;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProtocolResource protocolResource = new ProtocolResource(protocolService);
        this.restProtocolMockMvc = MockMvcBuilders.standaloneSetup(protocolResource)
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
    public static Protocol createEntity(EntityManager em) {
        Protocol protocol = new Protocol()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .deleted(DEFAULT_DELETED)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return protocol;
    }

    @Before
    public void initTest() {
        protocol = createEntity(em);
    }

    @Test
    @Transactional
    public void createProtocol() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // Create the Protocol
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);
        restProtocolMockMvc.perform(post("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isCreated());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate + 1);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProtocol.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProtocol.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testProtocol.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProtocol.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createProtocolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // Create the Protocol with an existing ID
        protocol.setId(1L);
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProtocolMockMvc.perform(post("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = protocolRepository.findAll().size();
        // set the field null
        protocol.setName(null);

        // Create the Protocol, which fails.
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        restProtocolMockMvc.perform(post("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = protocolRepository.findAll().size();
        // set the field null
        protocol.setDescription(null);

        // Create the Protocol, which fails.
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        restProtocolMockMvc.perform(post("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProtocols() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList
        restProtocolMockMvc.perform(get("/api/protocols?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protocol.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get the protocol
        restProtocolMockMvc.perform(get("/api/protocols/{id}", protocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(protocol.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingProtocol() throws Exception {
        // Get the protocol
        restProtocolMockMvc.perform(get("/api/protocols/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Update the protocol
        Protocol updatedProtocol = protocolRepository.findOne(protocol.getId());
        // Disconnect from session so that the updates on updatedProtocol are not directly saved in db
        em.detach(updatedProtocol);
        updatedProtocol
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .deleted(UPDATED_DELETED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ProtocolDTO protocolDTO = protocolMapper.toDto(updatedProtocol);

        restProtocolMockMvc.perform(put("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isOk());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProtocol.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProtocol.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testProtocol.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProtocol.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Create the Protocol
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProtocolMockMvc.perform(put("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isCreated());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);
        int databaseSizeBeforeDelete = protocolRepository.findAll().size();

        // Get the protocol
        restProtocolMockMvc.perform(delete("/api/protocols/{id}", protocol.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Protocol.class);
        Protocol protocol1 = new Protocol();
        protocol1.setId(1L);
        Protocol protocol2 = new Protocol();
        protocol2.setId(protocol1.getId());
        assertThat(protocol1).isEqualTo(protocol2);
        protocol2.setId(2L);
        assertThat(protocol1).isNotEqualTo(protocol2);
        protocol1.setId(null);
        assertThat(protocol1).isNotEqualTo(protocol2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProtocolDTO.class);
        ProtocolDTO protocolDTO1 = new ProtocolDTO();
        protocolDTO1.setId(1L);
        ProtocolDTO protocolDTO2 = new ProtocolDTO();
        assertThat(protocolDTO1).isNotEqualTo(protocolDTO2);
        protocolDTO2.setId(protocolDTO1.getId());
        assertThat(protocolDTO1).isEqualTo(protocolDTO2);
        protocolDTO2.setId(2L);
        assertThat(protocolDTO1).isNotEqualTo(protocolDTO2);
        protocolDTO1.setId(null);
        assertThat(protocolDTO1).isNotEqualTo(protocolDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(protocolMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(protocolMapper.fromId(null)).isNull();
    }
}
