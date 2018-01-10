package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Destinies;
import com.lighthouse.aditum.repository.DestiniesRepository;
import com.lighthouse.aditum.service.DestiniesService;
import com.lighthouse.aditum.service.dto.DestiniesDTO;
import com.lighthouse.aditum.service.mapper.DestiniesMapper;
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
 * Test class for the DestiniesResource REST controller.
 *
 * @see DestiniesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class DestiniesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DestiniesRepository destiniesRepository;

    @Autowired
    private DestiniesMapper destiniesMapper;

    @Autowired
    private DestiniesService destiniesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDestiniesMockMvc;

    private Destinies destinies;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DestiniesResource destiniesResource = new DestiniesResource(destiniesService);
        this.restDestiniesMockMvc = MockMvcBuilders.standaloneSetup(destiniesResource)
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
    public static Destinies createEntity(EntityManager em) {
        Destinies destinies = new Destinies()
                .name(DEFAULT_NAME);
        return destinies;
    }

    @Before
    public void initTest() {
        destinies = createEntity(em);
    }

    @Test
    @Transactional
    public void createDestinies() throws Exception {
        int databaseSizeBeforeCreate = destiniesRepository.findAll().size();

        // Create the Destinies
        DestiniesDTO destiniesDTO = destiniesMapper.destiniesToDestiniesDTO(destinies);

        restDestiniesMockMvc.perform(post("/api/destinies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(destiniesDTO)))
            .andExpect(status().isCreated());

        // Validate the Destinies in the database
        List<Destinies> destiniesList = destiniesRepository.findAll();
        assertThat(destiniesList).hasSize(databaseSizeBeforeCreate + 1);
        Destinies testDestinies = destiniesList.get(destiniesList.size() - 1);
        assertThat(testDestinies.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createDestiniesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = destiniesRepository.findAll().size();

        // Create the Destinies with an existing ID
        Destinies existingDestinies = new Destinies();
        existingDestinies.setId(1L);
        DestiniesDTO existingDestiniesDTO = destiniesMapper.destiniesToDestiniesDTO(existingDestinies);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDestiniesMockMvc.perform(post("/api/destinies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDestiniesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Destinies> destiniesList = destiniesRepository.findAll();
        assertThat(destiniesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = destiniesRepository.findAll().size();
        // set the field null
        destinies.setName(null);

        // Create the Destinies, which fails.
        DestiniesDTO destiniesDTO = destiniesMapper.destiniesToDestiniesDTO(destinies);

        restDestiniesMockMvc.perform(post("/api/destinies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(destiniesDTO)))
            .andExpect(status().isBadRequest());

        List<Destinies> destiniesList = destiniesRepository.findAll();
        assertThat(destiniesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDestinies() throws Exception {
        // Initialize the database
        destiniesRepository.saveAndFlush(destinies);

        // Get all the destiniesList
        restDestiniesMockMvc.perform(get("/api/destinies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(destinies.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDestinies() throws Exception {
        // Initialize the database
        destiniesRepository.saveAndFlush(destinies);

        // Get the destinies
        restDestiniesMockMvc.perform(get("/api/destinies/{id}", destinies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(destinies.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDestinies() throws Exception {
        // Get the destinies
        restDestiniesMockMvc.perform(get("/api/destinies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDestinies() throws Exception {
        // Initialize the database
        destiniesRepository.saveAndFlush(destinies);
        int databaseSizeBeforeUpdate = destiniesRepository.findAll().size();

        // Update the destinies
        Destinies updatedDestinies = destiniesRepository.findOne(destinies.getId());
        updatedDestinies
                .name(UPDATED_NAME);
        DestiniesDTO destiniesDTO = destiniesMapper.destiniesToDestiniesDTO(updatedDestinies);

        restDestiniesMockMvc.perform(put("/api/destinies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(destiniesDTO)))
            .andExpect(status().isOk());

        // Validate the Destinies in the database
        List<Destinies> destiniesList = destiniesRepository.findAll();
        assertThat(destiniesList).hasSize(databaseSizeBeforeUpdate);
        Destinies testDestinies = destiniesList.get(destiniesList.size() - 1);
        assertThat(testDestinies.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDestinies() throws Exception {
        int databaseSizeBeforeUpdate = destiniesRepository.findAll().size();

        // Create the Destinies
        DestiniesDTO destiniesDTO = destiniesMapper.destiniesToDestiniesDTO(destinies);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDestiniesMockMvc.perform(put("/api/destinies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(destiniesDTO)))
            .andExpect(status().isCreated());

        // Validate the Destinies in the database
        List<Destinies> destiniesList = destiniesRepository.findAll();
        assertThat(destiniesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDestinies() throws Exception {
        // Initialize the database
        destiniesRepository.saveAndFlush(destinies);
        int databaseSizeBeforeDelete = destiniesRepository.findAll().size();

        // Get the destinies
        restDestiniesMockMvc.perform(delete("/api/destinies/{id}", destinies.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Destinies> destiniesList = destiniesRepository.findAll();
        assertThat(destiniesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Destinies.class);
    }
}
