package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.BlockReservation;
import com.lighthouse.aditum.repository.BlockReservationRepository;
import com.lighthouse.aditum.service.BlockReservationService;
import com.lighthouse.aditum.service.dto.BlockReservationDTO;
import com.lighthouse.aditum.service.mapper.BlockReservationMapper;
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
 * Test class for the BlockReservationResource REST controller.
 *
 * @see BlockReservationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class BlockReservationResourceIntTest {

    private static final Integer DEFAULT_BLOCKED = 1;
    private static final Integer UPDATED_BLOCKED = 2;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private BlockReservationRepository blockReservationRepository;

    @Autowired
    private BlockReservationMapper blockReservationMapper;

    @Autowired
    private BlockReservationService blockReservationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBlockReservationMockMvc;

    private BlockReservation blockReservation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlockReservationResource blockReservationResource = new BlockReservationResource(blockReservationService);
        this.restBlockReservationMockMvc = MockMvcBuilders.standaloneSetup(blockReservationResource)
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
    public static BlockReservation createEntity(EntityManager em) {
        BlockReservation blockReservation = new BlockReservation()
            .blocked(DEFAULT_BLOCKED)
            .comments(DEFAULT_COMMENTS);
        return blockReservation;
    }

    @Before
    public void initTest() {
        blockReservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlockReservation() throws Exception {
        int databaseSizeBeforeCreate = blockReservationRepository.findAll().size();

        // Create the BlockReservation
        BlockReservationDTO blockReservationDTO = blockReservationMapper.toDto(blockReservation);
        restBlockReservationMockMvc.perform(post("/api/block-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockReservationDTO)))
            .andExpect(status().isCreated());

        // Validate the BlockReservation in the database
        List<BlockReservation> blockReservationList = blockReservationRepository.findAll();
        assertThat(blockReservationList).hasSize(databaseSizeBeforeCreate + 1);
        BlockReservation testBlockReservation = blockReservationList.get(blockReservationList.size() - 1);
        assertThat(testBlockReservation.getBlocked()).isEqualTo(DEFAULT_BLOCKED);
        assertThat(testBlockReservation.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createBlockReservationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blockReservationRepository.findAll().size();

        // Create the BlockReservation with an existing ID
        blockReservation.setId(1L);
        BlockReservationDTO blockReservationDTO = blockReservationMapper.toDto(blockReservation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockReservationMockMvc.perform(post("/api/block-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockReservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BlockReservation in the database
        List<BlockReservation> blockReservationList = blockReservationRepository.findAll();
        assertThat(blockReservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBlockReservations() throws Exception {
        // Initialize the database
        blockReservationRepository.saveAndFlush(blockReservation);

        // Get all the blockReservationList
        restBlockReservationMockMvc.perform(get("/api/block-reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blockReservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].blocked").value(hasItem(DEFAULT_BLOCKED)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getBlockReservation() throws Exception {
        // Initialize the database
        blockReservationRepository.saveAndFlush(blockReservation);

        // Get the blockReservation
        restBlockReservationMockMvc.perform(get("/api/block-reservations/{id}", blockReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blockReservation.getId().intValue()))
            .andExpect(jsonPath("$.blocked").value(DEFAULT_BLOCKED))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBlockReservation() throws Exception {
        // Get the blockReservation
        restBlockReservationMockMvc.perform(get("/api/block-reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlockReservation() throws Exception {
        // Initialize the database
        blockReservationRepository.saveAndFlush(blockReservation);
        int databaseSizeBeforeUpdate = blockReservationRepository.findAll().size();

        // Update the blockReservation
        BlockReservation updatedBlockReservation = blockReservationRepository.findOne(blockReservation.getId());
        // Disconnect from session so that the updates on updatedBlockReservation are not directly saved in db
        em.detach(updatedBlockReservation);
        updatedBlockReservation
            .blocked(UPDATED_BLOCKED)
            .comments(UPDATED_COMMENTS);
        BlockReservationDTO blockReservationDTO = blockReservationMapper.toDto(updatedBlockReservation);

        restBlockReservationMockMvc.perform(put("/api/block-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockReservationDTO)))
            .andExpect(status().isOk());

        // Validate the BlockReservation in the database
        List<BlockReservation> blockReservationList = blockReservationRepository.findAll();
        assertThat(blockReservationList).hasSize(databaseSizeBeforeUpdate);
        BlockReservation testBlockReservation = blockReservationList.get(blockReservationList.size() - 1);
        assertThat(testBlockReservation.getBlocked()).isEqualTo(UPDATED_BLOCKED);
        assertThat(testBlockReservation.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingBlockReservation() throws Exception {
        int databaseSizeBeforeUpdate = blockReservationRepository.findAll().size();

        // Create the BlockReservation
        BlockReservationDTO blockReservationDTO = blockReservationMapper.toDto(blockReservation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlockReservationMockMvc.perform(put("/api/block-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blockReservationDTO)))
            .andExpect(status().isCreated());

        // Validate the BlockReservation in the database
        List<BlockReservation> blockReservationList = blockReservationRepository.findAll();
        assertThat(blockReservationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBlockReservation() throws Exception {
        // Initialize the database
        blockReservationRepository.saveAndFlush(blockReservation);
        int databaseSizeBeforeDelete = blockReservationRepository.findAll().size();

        // Get the blockReservation
        restBlockReservationMockMvc.perform(delete("/api/block-reservations/{id}", blockReservation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BlockReservation> blockReservationList = blockReservationRepository.findAll();
        assertThat(blockReservationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockReservation.class);
        BlockReservation blockReservation1 = new BlockReservation();
        blockReservation1.setId(1L);
        BlockReservation blockReservation2 = new BlockReservation();
        blockReservation2.setId(blockReservation1.getId());
        assertThat(blockReservation1).isEqualTo(blockReservation2);
        blockReservation2.setId(2L);
        assertThat(blockReservation1).isNotEqualTo(blockReservation2);
        blockReservation1.setId(null);
        assertThat(blockReservation1).isNotEqualTo(blockReservation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockReservationDTO.class);
        BlockReservationDTO blockReservationDTO1 = new BlockReservationDTO();
        blockReservationDTO1.setId(1L);
        BlockReservationDTO blockReservationDTO2 = new BlockReservationDTO();
        assertThat(blockReservationDTO1).isNotEqualTo(blockReservationDTO2);
        blockReservationDTO2.setId(blockReservationDTO1.getId());
        assertThat(blockReservationDTO1).isEqualTo(blockReservationDTO2);
        blockReservationDTO2.setId(2L);
        assertThat(blockReservationDTO1).isNotEqualTo(blockReservationDTO2);
        blockReservationDTO1.setId(null);
        assertThat(blockReservationDTO1).isNotEqualTo(blockReservationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(blockReservationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(blockReservationMapper.fromId(null)).isNull();
    }
}
