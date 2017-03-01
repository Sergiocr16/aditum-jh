package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.AccessDoor;
import com.lighthouse.aditum.repository.AccessDoorRepository;
import com.lighthouse.aditum.service.AccessDoorService;
import com.lighthouse.aditum.service.dto.AccessDoorDTO;
import com.lighthouse.aditum.service.mapper.AccessDoorMapper;
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
 * Test class for the AccessDoorResource REST controller.
 *
 * @see AccessDoorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class AccessDoorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AccessDoorRepository accessDoorRepository;

    @Autowired
    private AccessDoorMapper accessDoorMapper;

    @Autowired
    private AccessDoorService accessDoorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccessDoorMockMvc;

    private AccessDoor accessDoor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccessDoorResource accessDoorResource = new AccessDoorResource(accessDoorService);
        this.restAccessDoorMockMvc = MockMvcBuilders.standaloneSetup(accessDoorResource)
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
    public static AccessDoor createEntity(EntityManager em) {
        AccessDoor accessDoor = new AccessDoor()
                .name(DEFAULT_NAME);
        return accessDoor;
    }

    @Before
    public void initTest() {
        accessDoor = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccessDoor() throws Exception {
        int databaseSizeBeforeCreate = accessDoorRepository.findAll().size();

        // Create the AccessDoor
        AccessDoorDTO accessDoorDTO = accessDoorMapper.accessDoorToAccessDoorDTO(accessDoor);

        restAccessDoorMockMvc.perform(post("/api/access-doors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessDoorDTO)))
            .andExpect(status().isCreated());

        // Validate the AccessDoor in the database
        List<AccessDoor> accessDoorList = accessDoorRepository.findAll();
        assertThat(accessDoorList).hasSize(databaseSizeBeforeCreate + 1);
        AccessDoor testAccessDoor = accessDoorList.get(accessDoorList.size() - 1);
        assertThat(testAccessDoor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAccessDoorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accessDoorRepository.findAll().size();

        // Create the AccessDoor with an existing ID
        AccessDoor existingAccessDoor = new AccessDoor();
        existingAccessDoor.setId(1L);
        AccessDoorDTO existingAccessDoorDTO = accessDoorMapper.accessDoorToAccessDoorDTO(existingAccessDoor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessDoorMockMvc.perform(post("/api/access-doors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAccessDoorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AccessDoor> accessDoorList = accessDoorRepository.findAll();
        assertThat(accessDoorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessDoorRepository.findAll().size();
        // set the field null
        accessDoor.setName(null);

        // Create the AccessDoor, which fails.
        AccessDoorDTO accessDoorDTO = accessDoorMapper.accessDoorToAccessDoorDTO(accessDoor);

        restAccessDoorMockMvc.perform(post("/api/access-doors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessDoorDTO)))
            .andExpect(status().isBadRequest());

        List<AccessDoor> accessDoorList = accessDoorRepository.findAll();
        assertThat(accessDoorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccessDoors() throws Exception {
        // Initialize the database
        accessDoorRepository.saveAndFlush(accessDoor);

        // Get all the accessDoorList
        restAccessDoorMockMvc.perform(get("/api/access-doors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessDoor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAccessDoor() throws Exception {
        // Initialize the database
        accessDoorRepository.saveAndFlush(accessDoor);

        // Get the accessDoor
        restAccessDoorMockMvc.perform(get("/api/access-doors/{id}", accessDoor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accessDoor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccessDoor() throws Exception {
        // Get the accessDoor
        restAccessDoorMockMvc.perform(get("/api/access-doors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccessDoor() throws Exception {
        // Initialize the database
        accessDoorRepository.saveAndFlush(accessDoor);
        int databaseSizeBeforeUpdate = accessDoorRepository.findAll().size();

        // Update the accessDoor
        AccessDoor updatedAccessDoor = accessDoorRepository.findOne(accessDoor.getId());
        updatedAccessDoor
                .name(UPDATED_NAME);
        AccessDoorDTO accessDoorDTO = accessDoorMapper.accessDoorToAccessDoorDTO(updatedAccessDoor);

        restAccessDoorMockMvc.perform(put("/api/access-doors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessDoorDTO)))
            .andExpect(status().isOk());

        // Validate the AccessDoor in the database
        List<AccessDoor> accessDoorList = accessDoorRepository.findAll();
        assertThat(accessDoorList).hasSize(databaseSizeBeforeUpdate);
        AccessDoor testAccessDoor = accessDoorList.get(accessDoorList.size() - 1);
        assertThat(testAccessDoor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAccessDoor() throws Exception {
        int databaseSizeBeforeUpdate = accessDoorRepository.findAll().size();

        // Create the AccessDoor
        AccessDoorDTO accessDoorDTO = accessDoorMapper.accessDoorToAccessDoorDTO(accessDoor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccessDoorMockMvc.perform(put("/api/access-doors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessDoorDTO)))
            .andExpect(status().isCreated());

        // Validate the AccessDoor in the database
        List<AccessDoor> accessDoorList = accessDoorRepository.findAll();
        assertThat(accessDoorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccessDoor() throws Exception {
        // Initialize the database
        accessDoorRepository.saveAndFlush(accessDoor);
        int databaseSizeBeforeDelete = accessDoorRepository.findAll().size();

        // Get the accessDoor
        restAccessDoorMockMvc.perform(delete("/api/access-doors/{id}", accessDoor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccessDoor> accessDoorList = accessDoorRepository.findAll();
        assertThat(accessDoorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessDoor.class);
    }
}
