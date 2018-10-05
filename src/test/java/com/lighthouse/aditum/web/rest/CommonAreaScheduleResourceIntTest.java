package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.CommonAreaSchedule;
import com.lighthouse.aditum.repository.CommonAreaScheduleRepository;
import com.lighthouse.aditum.service.CommonAreaScheduleService;
import com.lighthouse.aditum.service.dto.CommonAreaScheduleDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaScheduleMapper;
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
 * Test class for the CommonAreaScheduleResource REST controller.
 *
 * @see CommonAreaScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CommonAreaScheduleResourceIntTest {

    private static final String DEFAULT_LUNES = "AAAAAAAAAA";
    private static final String UPDATED_LUNES = "BBBBBBBBBB";

    private static final String DEFAULT_MARTES = "AAAAAAAAAA";
    private static final String UPDATED_MARTES = "BBBBBBBBBB";

    private static final String DEFAULT_MIERCOLES = "AAAAAAAAAA";
    private static final String UPDATED_MIERCOLES = "BBBBBBBBBB";

    private static final String DEFAULT_JUEVES = "AAAAAAAAAA";
    private static final String UPDATED_JUEVES = "BBBBBBBBBB";

    private static final String DEFAULT_VIERNES = "AAAAAAAAAA";
    private static final String UPDATED_VIERNES = "BBBBBBBBBB";

    private static final String DEFAULT_SABADO = "AAAAAAAAAA";
    private static final String UPDATED_SABADO = "BBBBBBBBBB";

    private static final String DEFAULT_DOMINGO = "AAAAAAAAAA";
    private static final String UPDATED_DOMINGO = "BBBBBBBBBB";

    private static final Long DEFAULT_COMMON_AREA_ID = 1L;
    private static final Long UPDATED_COMMON_AREA_ID = 2L;

    @Autowired
    private CommonAreaScheduleRepository commonAreaScheduleRepository;

    @Autowired
    private CommonAreaScheduleMapper commonAreaScheduleMapper;

    @Autowired
    private CommonAreaScheduleService commonAreaScheduleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommonAreaScheduleMockMvc;

    private CommonAreaSchedule commonAreaSchedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommonAreaScheduleResource commonAreaScheduleResource = new CommonAreaScheduleResource(commonAreaScheduleService);
        this.restCommonAreaScheduleMockMvc = MockMvcBuilders.standaloneSetup(commonAreaScheduleResource)
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
    public static CommonAreaSchedule createEntity(EntityManager em) {
        CommonAreaSchedule commonAreaSchedule = new CommonAreaSchedule()
            .lunes(DEFAULT_LUNES)
            .martes(DEFAULT_MARTES)
            .miercoles(DEFAULT_MIERCOLES)
            .jueves(DEFAULT_JUEVES)
            .viernes(DEFAULT_VIERNES)
            .sabado(DEFAULT_SABADO)
            .domingo(DEFAULT_DOMINGO)
            .commonAreaId(DEFAULT_COMMON_AREA_ID);
        return commonAreaSchedule;
    }

    @Before
    public void initTest() {
        commonAreaSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommonAreaSchedule() throws Exception {
        int databaseSizeBeforeCreate = commonAreaScheduleRepository.findAll().size();

        // Create the CommonAreaSchedule
        CommonAreaScheduleDTO commonAreaScheduleDTO = commonAreaScheduleMapper.toDto(commonAreaSchedule);
        restCommonAreaScheduleMockMvc.perform(post("/api/common-area-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaScheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the CommonAreaSchedule in the database
        List<CommonAreaSchedule> commonAreaScheduleList = commonAreaScheduleRepository.findAll();
        assertThat(commonAreaScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        CommonAreaSchedule testCommonAreaSchedule = commonAreaScheduleList.get(commonAreaScheduleList.size() - 1);
        assertThat(testCommonAreaSchedule.getLunes()).isEqualTo(DEFAULT_LUNES);
        assertThat(testCommonAreaSchedule.getMartes()).isEqualTo(DEFAULT_MARTES);
        assertThat(testCommonAreaSchedule.getMiercoles()).isEqualTo(DEFAULT_MIERCOLES);
        assertThat(testCommonAreaSchedule.getJueves()).isEqualTo(DEFAULT_JUEVES);
        assertThat(testCommonAreaSchedule.getViernes()).isEqualTo(DEFAULT_VIERNES);
        assertThat(testCommonAreaSchedule.getSabado()).isEqualTo(DEFAULT_SABADO);
        assertThat(testCommonAreaSchedule.getDomingo()).isEqualTo(DEFAULT_DOMINGO);
        assertThat(testCommonAreaSchedule.getCommonAreaId()).isEqualTo(DEFAULT_COMMON_AREA_ID);
    }

    @Test
    @Transactional
    public void createCommonAreaScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commonAreaScheduleRepository.findAll().size();

        // Create the CommonAreaSchedule with an existing ID
        commonAreaSchedule.setId(1L);
        CommonAreaScheduleDTO commonAreaScheduleDTO = commonAreaScheduleMapper.toDto(commonAreaSchedule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonAreaScheduleMockMvc.perform(post("/api/common-area-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CommonAreaSchedule> commonAreaScheduleList = commonAreaScheduleRepository.findAll();
        assertThat(commonAreaScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCommonAreaSchedules() throws Exception {
        // Initialize the database
        commonAreaScheduleRepository.saveAndFlush(commonAreaSchedule);

        // Get all the commonAreaScheduleList
        restCommonAreaScheduleMockMvc.perform(get("/api/common-area-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonAreaSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].lunes").value(hasItem(DEFAULT_LUNES.toString())))
            .andExpect(jsonPath("$.[*].martes").value(hasItem(DEFAULT_MARTES.toString())))
            .andExpect(jsonPath("$.[*].miercoles").value(hasItem(DEFAULT_MIERCOLES.toString())))
            .andExpect(jsonPath("$.[*].jueves").value(hasItem(DEFAULT_JUEVES.toString())))
            .andExpect(jsonPath("$.[*].viernes").value(hasItem(DEFAULT_VIERNES.toString())))
            .andExpect(jsonPath("$.[*].sabado").value(hasItem(DEFAULT_SABADO.toString())))
            .andExpect(jsonPath("$.[*].domingo").value(hasItem(DEFAULT_DOMINGO.toString())))
            .andExpect(jsonPath("$.[*].commonAreaId").value(hasItem(DEFAULT_COMMON_AREA_ID.intValue())));
    }

    @Test
    @Transactional
    public void getCommonAreaSchedule() throws Exception {
        // Initialize the database
        commonAreaScheduleRepository.saveAndFlush(commonAreaSchedule);

        // Get the commonAreaSchedule
        restCommonAreaScheduleMockMvc.perform(get("/api/common-area-schedules/{id}", commonAreaSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commonAreaSchedule.getId().intValue()))
            .andExpect(jsonPath("$.lunes").value(DEFAULT_LUNES.toString()))
            .andExpect(jsonPath("$.martes").value(DEFAULT_MARTES.toString()))
            .andExpect(jsonPath("$.miercoles").value(DEFAULT_MIERCOLES.toString()))
            .andExpect(jsonPath("$.jueves").value(DEFAULT_JUEVES.toString()))
            .andExpect(jsonPath("$.viernes").value(DEFAULT_VIERNES.toString()))
            .andExpect(jsonPath("$.sabado").value(DEFAULT_SABADO.toString()))
            .andExpect(jsonPath("$.domingo").value(DEFAULT_DOMINGO.toString()))
            .andExpect(jsonPath("$.commonAreaId").value(DEFAULT_COMMON_AREA_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCommonAreaSchedule() throws Exception {
        // Get the commonAreaSchedule
        restCommonAreaScheduleMockMvc.perform(get("/api/common-area-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommonAreaSchedule() throws Exception {
        // Initialize the database
        commonAreaScheduleRepository.saveAndFlush(commonAreaSchedule);
        int databaseSizeBeforeUpdate = commonAreaScheduleRepository.findAll().size();

        // Update the commonAreaSchedule
        CommonAreaSchedule updatedCommonAreaSchedule = commonAreaScheduleRepository.findOne(commonAreaSchedule.getId());
        updatedCommonAreaSchedule
            .lunes(UPDATED_LUNES)
            .martes(UPDATED_MARTES)
            .miercoles(UPDATED_MIERCOLES)
            .jueves(UPDATED_JUEVES)
            .viernes(UPDATED_VIERNES)
            .sabado(UPDATED_SABADO)
            .domingo(UPDATED_DOMINGO)
            .commonAreaId(UPDATED_COMMON_AREA_ID);
        CommonAreaScheduleDTO commonAreaScheduleDTO = commonAreaScheduleMapper.toDto(updatedCommonAreaSchedule);

        restCommonAreaScheduleMockMvc.perform(put("/api/common-area-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaScheduleDTO)))
            .andExpect(status().isOk());

        // Validate the CommonAreaSchedule in the database
        List<CommonAreaSchedule> commonAreaScheduleList = commonAreaScheduleRepository.findAll();
        assertThat(commonAreaScheduleList).hasSize(databaseSizeBeforeUpdate);
        CommonAreaSchedule testCommonAreaSchedule = commonAreaScheduleList.get(commonAreaScheduleList.size() - 1);
        assertThat(testCommonAreaSchedule.getLunes()).isEqualTo(UPDATED_LUNES);
        assertThat(testCommonAreaSchedule.getMartes()).isEqualTo(UPDATED_MARTES);
        assertThat(testCommonAreaSchedule.getMiercoles()).isEqualTo(UPDATED_MIERCOLES);
        assertThat(testCommonAreaSchedule.getJueves()).isEqualTo(UPDATED_JUEVES);
        assertThat(testCommonAreaSchedule.getViernes()).isEqualTo(UPDATED_VIERNES);
        assertThat(testCommonAreaSchedule.getSabado()).isEqualTo(UPDATED_SABADO);
        assertThat(testCommonAreaSchedule.getDomingo()).isEqualTo(UPDATED_DOMINGO);
        assertThat(testCommonAreaSchedule.getCommonAreaId()).isEqualTo(UPDATED_COMMON_AREA_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingCommonAreaSchedule() throws Exception {
        int databaseSizeBeforeUpdate = commonAreaScheduleRepository.findAll().size();

        // Create the CommonAreaSchedule
        CommonAreaScheduleDTO commonAreaScheduleDTO = commonAreaScheduleMapper.toDto(commonAreaSchedule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommonAreaScheduleMockMvc.perform(put("/api/common-area-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaScheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the CommonAreaSchedule in the database
        List<CommonAreaSchedule> commonAreaScheduleList = commonAreaScheduleRepository.findAll();
        assertThat(commonAreaScheduleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommonAreaSchedule() throws Exception {
        // Initialize the database
        commonAreaScheduleRepository.saveAndFlush(commonAreaSchedule);
        int databaseSizeBeforeDelete = commonAreaScheduleRepository.findAll().size();

        // Get the commonAreaSchedule
        restCommonAreaScheduleMockMvc.perform(delete("/api/common-area-schedules/{id}", commonAreaSchedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CommonAreaSchedule> commonAreaScheduleList = commonAreaScheduleRepository.findAll();
        assertThat(commonAreaScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonAreaSchedule.class);
        CommonAreaSchedule commonAreaSchedule1 = new CommonAreaSchedule();
        commonAreaSchedule1.setId(1L);
        CommonAreaSchedule commonAreaSchedule2 = new CommonAreaSchedule();
        commonAreaSchedule2.setId(commonAreaSchedule1.getId());
        assertThat(commonAreaSchedule1).isEqualTo(commonAreaSchedule2);
        commonAreaSchedule2.setId(2L);
        assertThat(commonAreaSchedule1).isNotEqualTo(commonAreaSchedule2);
        commonAreaSchedule1.setId(null);
        assertThat(commonAreaSchedule1).isNotEqualTo(commonAreaSchedule2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonAreaScheduleDTO.class);
        CommonAreaScheduleDTO commonAreaScheduleDTO1 = new CommonAreaScheduleDTO();
        commonAreaScheduleDTO1.setId(1L);
        CommonAreaScheduleDTO commonAreaScheduleDTO2 = new CommonAreaScheduleDTO();
        assertThat(commonAreaScheduleDTO1).isNotEqualTo(commonAreaScheduleDTO2);
        commonAreaScheduleDTO2.setId(commonAreaScheduleDTO1.getId());
        assertThat(commonAreaScheduleDTO1).isEqualTo(commonAreaScheduleDTO2);
        commonAreaScheduleDTO2.setId(2L);
        assertThat(commonAreaScheduleDTO1).isNotEqualTo(commonAreaScheduleDTO2);
        commonAreaScheduleDTO1.setId(null);
        assertThat(commonAreaScheduleDTO1).isNotEqualTo(commonAreaScheduleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commonAreaScheduleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commonAreaScheduleMapper.fromId(null)).isNull();
    }
}
