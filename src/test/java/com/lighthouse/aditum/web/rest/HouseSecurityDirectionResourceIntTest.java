package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.HouseSecurityDirection;
import com.lighthouse.aditum.repository.HouseSecurityDirectionRepository;
import com.lighthouse.aditum.service.HouseSecurityDirectionService;
import com.lighthouse.aditum.service.dto.HouseSecurityDirectionDTO;
import com.lighthouse.aditum.service.mapper.HouseSecurityDirectionMapper;
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
 * Test class for the HouseSecurityDirectionResource REST controller.
 *
 * @see HouseSecurityDirectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class HouseSecurityDirectionResourceIntTest {

    private static final String DEFAULT_DIRECTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADITIONAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_ADITIONAL_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSE_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_PICTURE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SMALL_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_SMALL_DIRECTION = "BBBBBBBBBB";

    @Autowired
    private HouseSecurityDirectionRepository houseSecurityDirectionRepository;

    @Autowired
    private HouseSecurityDirectionMapper houseSecurityDirectionMapper;

    @Autowired
    private HouseSecurityDirectionService houseSecurityDirectionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHouseSecurityDirectionMockMvc;

    private HouseSecurityDirection houseSecurityDirection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HouseSecurityDirectionResource houseSecurityDirectionResource = new HouseSecurityDirectionResource(houseSecurityDirectionService);
        this.restHouseSecurityDirectionMockMvc = MockMvcBuilders.standaloneSetup(houseSecurityDirectionResource)
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
    public static HouseSecurityDirection createEntity(EntityManager em) {
        HouseSecurityDirection houseSecurityDirection = new HouseSecurityDirection()
            .directionDescription(DEFAULT_DIRECTION_DESCRIPTION)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .houseDescription(DEFAULT_HOUSE_DESCRIPTION)
            .aditionalNotes(DEFAULT_ADITIONAL_NOTES)
            .housePictureUrl(DEFAULT_HOUSE_PICTURE_URL)
            .smallDirection(DEFAULT_SMALL_DIRECTION);
        return houseSecurityDirection;
    }

    @Before
    public void initTest() {
        houseSecurityDirection = createEntity(em);
    }

    @Test
    @Transactional
    public void createHouseSecurityDirection() throws Exception {
        int databaseSizeBeforeCreate = houseSecurityDirectionRepository.findAll().size();

        // Create the HouseSecurityDirection
        HouseSecurityDirectionDTO houseSecurityDirectionDTO = houseSecurityDirectionMapper.toDto(houseSecurityDirection);
        restHouseSecurityDirectionMockMvc.perform(post("/api/house-security-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseSecurityDirectionDTO)))
            .andExpect(status().isCreated());

        // Validate the HouseSecurityDirection in the database
        List<HouseSecurityDirection> houseSecurityDirectionList = houseSecurityDirectionRepository.findAll();
        assertThat(houseSecurityDirectionList).hasSize(databaseSizeBeforeCreate + 1);
        HouseSecurityDirection testHouseSecurityDirection = houseSecurityDirectionList.get(houseSecurityDirectionList.size() - 1);
        assertThat(testHouseSecurityDirection.getDirectionDescription()).isEqualTo(DEFAULT_DIRECTION_DESCRIPTION);
        assertThat(testHouseSecurityDirection.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testHouseSecurityDirection.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testHouseSecurityDirection.getHouseDescription()).isEqualTo(DEFAULT_HOUSE_DESCRIPTION);
        assertThat(testHouseSecurityDirection.getAditionalNotes()).isEqualTo(DEFAULT_ADITIONAL_NOTES);
        assertThat(testHouseSecurityDirection.getHousePictureUrl()).isEqualTo(DEFAULT_HOUSE_PICTURE_URL);
        assertThat(testHouseSecurityDirection.getSmallDirection()).isEqualTo(DEFAULT_SMALL_DIRECTION);
    }

    @Test
    @Transactional
    public void createHouseSecurityDirectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = houseSecurityDirectionRepository.findAll().size();

        // Create the HouseSecurityDirection with an existing ID
        houseSecurityDirection.setId(1L);
        HouseSecurityDirectionDTO houseSecurityDirectionDTO = houseSecurityDirectionMapper.toDto(houseSecurityDirection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseSecurityDirectionMockMvc.perform(post("/api/house-security-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseSecurityDirectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HouseSecurityDirection in the database
        List<HouseSecurityDirection> houseSecurityDirectionList = houseSecurityDirectionRepository.findAll();
        assertThat(houseSecurityDirectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHouseSecurityDirections() throws Exception {
        // Initialize the database
        houseSecurityDirectionRepository.saveAndFlush(houseSecurityDirection);

        // Get all the houseSecurityDirectionList
        restHouseSecurityDirectionMockMvc.perform(get("/api/house-security-directions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseSecurityDirection.getId().intValue())))
            .andExpect(jsonPath("$.[*].directionDescription").value(hasItem(DEFAULT_DIRECTION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())))
            .andExpect(jsonPath("$.[*].houseDescription").value(hasItem(DEFAULT_HOUSE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].aditionalNotes").value(hasItem(DEFAULT_ADITIONAL_NOTES.toString())))
            .andExpect(jsonPath("$.[*].housePictureUrl").value(hasItem(DEFAULT_HOUSE_PICTURE_URL.toString())))
            .andExpect(jsonPath("$.[*].smallDirection").value(hasItem(DEFAULT_SMALL_DIRECTION.toString())));
    }

    @Test
    @Transactional
    public void getHouseSecurityDirection() throws Exception {
        // Initialize the database
        houseSecurityDirectionRepository.saveAndFlush(houseSecurityDirection);

        // Get the houseSecurityDirection
        restHouseSecurityDirectionMockMvc.perform(get("/api/house-security-directions/{id}", houseSecurityDirection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(houseSecurityDirection.getId().intValue()))
            .andExpect(jsonPath("$.directionDescription").value(DEFAULT_DIRECTION_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()))
            .andExpect(jsonPath("$.houseDescription").value(DEFAULT_HOUSE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.aditionalNotes").value(DEFAULT_ADITIONAL_NOTES.toString()))
            .andExpect(jsonPath("$.housePictureUrl").value(DEFAULT_HOUSE_PICTURE_URL.toString()))
            .andExpect(jsonPath("$.smallDirection").value(DEFAULT_SMALL_DIRECTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHouseSecurityDirection() throws Exception {
        // Get the houseSecurityDirection
        restHouseSecurityDirectionMockMvc.perform(get("/api/house-security-directions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouseSecurityDirection() throws Exception {
        // Initialize the database
        houseSecurityDirectionRepository.saveAndFlush(houseSecurityDirection);
        int databaseSizeBeforeUpdate = houseSecurityDirectionRepository.findAll().size();

        // Update the houseSecurityDirection
        HouseSecurityDirection updatedHouseSecurityDirection = houseSecurityDirectionRepository.findOne(houseSecurityDirection.getId());
        // Disconnect from session so that the updates on updatedHouseSecurityDirection are not directly saved in db
        em.detach(updatedHouseSecurityDirection);
        updatedHouseSecurityDirection
            .directionDescription(UPDATED_DIRECTION_DESCRIPTION)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .houseDescription(UPDATED_HOUSE_DESCRIPTION)
            .aditionalNotes(UPDATED_ADITIONAL_NOTES)
            .housePictureUrl(UPDATED_HOUSE_PICTURE_URL)
            .smallDirection(UPDATED_SMALL_DIRECTION);
        HouseSecurityDirectionDTO houseSecurityDirectionDTO = houseSecurityDirectionMapper.toDto(updatedHouseSecurityDirection);

        restHouseSecurityDirectionMockMvc.perform(put("/api/house-security-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseSecurityDirectionDTO)))
            .andExpect(status().isOk());

        // Validate the HouseSecurityDirection in the database
        List<HouseSecurityDirection> houseSecurityDirectionList = houseSecurityDirectionRepository.findAll();
        assertThat(houseSecurityDirectionList).hasSize(databaseSizeBeforeUpdate);
        HouseSecurityDirection testHouseSecurityDirection = houseSecurityDirectionList.get(houseSecurityDirectionList.size() - 1);
        assertThat(testHouseSecurityDirection.getDirectionDescription()).isEqualTo(UPDATED_DIRECTION_DESCRIPTION);
        assertThat(testHouseSecurityDirection.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testHouseSecurityDirection.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHouseSecurityDirection.getHouseDescription()).isEqualTo(UPDATED_HOUSE_DESCRIPTION);
        assertThat(testHouseSecurityDirection.getAditionalNotes()).isEqualTo(UPDATED_ADITIONAL_NOTES);
        assertThat(testHouseSecurityDirection.getHousePictureUrl()).isEqualTo(UPDATED_HOUSE_PICTURE_URL);
        assertThat(testHouseSecurityDirection.getSmallDirection()).isEqualTo(UPDATED_SMALL_DIRECTION);
    }

    @Test
    @Transactional
    public void updateNonExistingHouseSecurityDirection() throws Exception {
        int databaseSizeBeforeUpdate = houseSecurityDirectionRepository.findAll().size();

        // Create the HouseSecurityDirection
        HouseSecurityDirectionDTO houseSecurityDirectionDTO = houseSecurityDirectionMapper.toDto(houseSecurityDirection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHouseSecurityDirectionMockMvc.perform(put("/api/house-security-directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(houseSecurityDirectionDTO)))
            .andExpect(status().isCreated());

        // Validate the HouseSecurityDirection in the database
        List<HouseSecurityDirection> houseSecurityDirectionList = houseSecurityDirectionRepository.findAll();
        assertThat(houseSecurityDirectionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHouseSecurityDirection() throws Exception {
        // Initialize the database
        houseSecurityDirectionRepository.saveAndFlush(houseSecurityDirection);
        int databaseSizeBeforeDelete = houseSecurityDirectionRepository.findAll().size();

        // Get the houseSecurityDirection
        restHouseSecurityDirectionMockMvc.perform(delete("/api/house-security-directions/{id}", houseSecurityDirection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HouseSecurityDirection> houseSecurityDirectionList = houseSecurityDirectionRepository.findAll();
        assertThat(houseSecurityDirectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseSecurityDirection.class);
        HouseSecurityDirection houseSecurityDirection1 = new HouseSecurityDirection();
        houseSecurityDirection1.setId(1L);
        HouseSecurityDirection houseSecurityDirection2 = new HouseSecurityDirection();
        houseSecurityDirection2.setId(houseSecurityDirection1.getId());
        assertThat(houseSecurityDirection1).isEqualTo(houseSecurityDirection2);
        houseSecurityDirection2.setId(2L);
        assertThat(houseSecurityDirection1).isNotEqualTo(houseSecurityDirection2);
        houseSecurityDirection1.setId(null);
        assertThat(houseSecurityDirection1).isNotEqualTo(houseSecurityDirection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseSecurityDirectionDTO.class);
        HouseSecurityDirectionDTO houseSecurityDirectionDTO1 = new HouseSecurityDirectionDTO();
        houseSecurityDirectionDTO1.setId(1L);
        HouseSecurityDirectionDTO houseSecurityDirectionDTO2 = new HouseSecurityDirectionDTO();
        assertThat(houseSecurityDirectionDTO1).isNotEqualTo(houseSecurityDirectionDTO2);
        houseSecurityDirectionDTO2.setId(houseSecurityDirectionDTO1.getId());
        assertThat(houseSecurityDirectionDTO1).isEqualTo(houseSecurityDirectionDTO2);
        houseSecurityDirectionDTO2.setId(2L);
        assertThat(houseSecurityDirectionDTO1).isNotEqualTo(houseSecurityDirectionDTO2);
        houseSecurityDirectionDTO1.setId(null);
        assertThat(houseSecurityDirectionDTO1).isNotEqualTo(houseSecurityDirectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(houseSecurityDirectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(houseSecurityDirectionMapper.fromId(null)).isNull();
    }
}
