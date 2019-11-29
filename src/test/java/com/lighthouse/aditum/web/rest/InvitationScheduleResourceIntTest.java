package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.InvitationSchedule;
import com.lighthouse.aditum.repository.InvitationScheduleRepository;
import com.lighthouse.aditum.service.InvitationScheduleService;
import com.lighthouse.aditum.service.dto.InvitationScheduleDTO;
import com.lighthouse.aditum.service.mapper.InvitationScheduleMapper;
import com.lighthouse.aditum.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
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
 * Test class for the InvitationScheduleResource REST controller.
 *
 * @see InvitationScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class InvitationScheduleResourceIntTest {

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

    private static final Long DEFAULT_VISITANT_INVITATION_ID = 1L;
    private static final Long UPDATED_VISITANT_INVITATION_ID = 2L;

    @Autowired
    private InvitationScheduleRepository invitationScheduleRepository;

    @Autowired
    private InvitationScheduleMapper invitationScheduleMapper;

    @Autowired
    private InvitationScheduleService invitationScheduleService;


    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvitationScheduleMockMvc;

    private InvitationSchedule invitationSchedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvitationScheduleResource invitationScheduleResource = new InvitationScheduleResource(invitationScheduleService);
        this.restInvitationScheduleMockMvc = MockMvcBuilders.standaloneSetup(invitationScheduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvitationSchedule createEntity(EntityManager em) {
        InvitationSchedule invitationSchedule = new InvitationSchedule()
            .lunes(DEFAULT_LUNES)
            .martes(DEFAULT_MARTES)
            .miercoles(DEFAULT_MIERCOLES)
            .jueves(DEFAULT_JUEVES)
            .viernes(DEFAULT_VIERNES)
            .sabado(DEFAULT_SABADO)
            .domingo(DEFAULT_DOMINGO)
            .visitantInvitationId(DEFAULT_VISITANT_INVITATION_ID);
        return invitationSchedule;
    }

    @Before
    public void initTest() {
        invitationSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvitationSchedule() throws Exception {
        int databaseSizeBeforeCreate = invitationScheduleRepository.findAll().size();

        // Create the InvitationSchedule
        InvitationScheduleDTO invitationScheduleDTO = invitationScheduleMapper.toDto(invitationSchedule);
        restInvitationScheduleMockMvc.perform(post("/api/invitation-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationScheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the InvitationSchedule in the database
        List<InvitationSchedule> invitationScheduleList = invitationScheduleRepository.findAll();
        assertThat(invitationScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        InvitationSchedule testInvitationSchedule = invitationScheduleList.get(invitationScheduleList.size() - 1);
        assertThat(testInvitationSchedule.getLunes()).isEqualTo(DEFAULT_LUNES);
        assertThat(testInvitationSchedule.getMartes()).isEqualTo(DEFAULT_MARTES);
        assertThat(testInvitationSchedule.getMiercoles()).isEqualTo(DEFAULT_MIERCOLES);
        assertThat(testInvitationSchedule.getJueves()).isEqualTo(DEFAULT_JUEVES);
        assertThat(testInvitationSchedule.getViernes()).isEqualTo(DEFAULT_VIERNES);
        assertThat(testInvitationSchedule.getSabado()).isEqualTo(DEFAULT_SABADO);
        assertThat(testInvitationSchedule.getDomingo()).isEqualTo(DEFAULT_DOMINGO);
        assertThat(testInvitationSchedule.getVisitantInvitationId()).isEqualTo(DEFAULT_VISITANT_INVITATION_ID);
    }

    @Test
    @Transactional
    public void createInvitationScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invitationScheduleRepository.findAll().size();

        // Create the InvitationSchedule with an existing ID
        invitationSchedule.setId(1L);
        InvitationScheduleDTO invitationScheduleDTO = invitationScheduleMapper.toDto(invitationSchedule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvitationScheduleMockMvc.perform(post("/api/invitation-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvitationSchedule in the database
        List<InvitationSchedule> invitationScheduleList = invitationScheduleRepository.findAll();
        assertThat(invitationScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvitationSchedules() throws Exception {
        // Initialize the database
        invitationScheduleRepository.saveAndFlush(invitationSchedule);

        // Get all the invitationScheduleList
        restInvitationScheduleMockMvc.perform(get("/api/invitation-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitationSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].lunes").value(hasItem(DEFAULT_LUNES.toString())))
            .andExpect(jsonPath("$.[*].martes").value(hasItem(DEFAULT_MARTES.toString())))
            .andExpect(jsonPath("$.[*].miercoles").value(hasItem(DEFAULT_MIERCOLES.toString())))
            .andExpect(jsonPath("$.[*].jueves").value(hasItem(DEFAULT_JUEVES.toString())))
            .andExpect(jsonPath("$.[*].viernes").value(hasItem(DEFAULT_VIERNES.toString())))
            .andExpect(jsonPath("$.[*].sabado").value(hasItem(DEFAULT_SABADO.toString())))
            .andExpect(jsonPath("$.[*].domingo").value(hasItem(DEFAULT_DOMINGO.toString())))
            .andExpect(jsonPath("$.[*].visitantInvitationId").value(hasItem(DEFAULT_VISITANT_INVITATION_ID.intValue())));
    }

    @Test
    @Transactional
    public void getInvitationSchedule() throws Exception {
        // Initialize the database
        invitationScheduleRepository.saveAndFlush(invitationSchedule);

        // Get the invitationSchedule
        restInvitationScheduleMockMvc.perform(get("/api/invitation-schedules/{id}", invitationSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invitationSchedule.getId().intValue()))
            .andExpect(jsonPath("$.lunes").value(DEFAULT_LUNES.toString()))
            .andExpect(jsonPath("$.martes").value(DEFAULT_MARTES.toString()))
            .andExpect(jsonPath("$.miercoles").value(DEFAULT_MIERCOLES.toString()))
            .andExpect(jsonPath("$.jueves").value(DEFAULT_JUEVES.toString()))
            .andExpect(jsonPath("$.viernes").value(DEFAULT_VIERNES.toString()))
            .andExpect(jsonPath("$.sabado").value(DEFAULT_SABADO.toString()))
            .andExpect(jsonPath("$.domingo").value(DEFAULT_DOMINGO.toString()))
            .andExpect(jsonPath("$.visitantInvitationId").value(DEFAULT_VISITANT_INVITATION_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvitationSchedule() throws Exception {
        // Get the invitationSchedule
        restInvitationScheduleMockMvc.perform(get("/api/invitation-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvitationSchedule() throws Exception {
        // Initialize the database
        invitationScheduleRepository.saveAndFlush(invitationSchedule);
        int databaseSizeBeforeUpdate = invitationScheduleRepository.findAll().size();

        // Update the invitationSchedule
        InvitationSchedule updatedInvitationSchedule = invitationScheduleRepository.findOne(invitationSchedule.getId());
        // Disconnect from session so that the updates on updatedInvitationSchedule are not directly saved in db
        em.detach(updatedInvitationSchedule);
        updatedInvitationSchedule
            .lunes(UPDATED_LUNES)
            .martes(UPDATED_MARTES)
            .miercoles(UPDATED_MIERCOLES)
            .jueves(UPDATED_JUEVES)
            .viernes(UPDATED_VIERNES)
            .sabado(UPDATED_SABADO)
            .domingo(UPDATED_DOMINGO)
            .visitantInvitationId(UPDATED_VISITANT_INVITATION_ID);
        InvitationScheduleDTO invitationScheduleDTO = invitationScheduleMapper.toDto(updatedInvitationSchedule);

        restInvitationScheduleMockMvc.perform(put("/api/invitation-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationScheduleDTO)))
            .andExpect(status().isOk());

        // Validate the InvitationSchedule in the database
        List<InvitationSchedule> invitationScheduleList = invitationScheduleRepository.findAll();
        assertThat(invitationScheduleList).hasSize(databaseSizeBeforeUpdate);
        InvitationSchedule testInvitationSchedule = invitationScheduleList.get(invitationScheduleList.size() - 1);
        assertThat(testInvitationSchedule.getLunes()).isEqualTo(UPDATED_LUNES);
        assertThat(testInvitationSchedule.getMartes()).isEqualTo(UPDATED_MARTES);
        assertThat(testInvitationSchedule.getMiercoles()).isEqualTo(UPDATED_MIERCOLES);
        assertThat(testInvitationSchedule.getJueves()).isEqualTo(UPDATED_JUEVES);
        assertThat(testInvitationSchedule.getViernes()).isEqualTo(UPDATED_VIERNES);
        assertThat(testInvitationSchedule.getSabado()).isEqualTo(UPDATED_SABADO);
        assertThat(testInvitationSchedule.getDomingo()).isEqualTo(UPDATED_DOMINGO);
        assertThat(testInvitationSchedule.getVisitantInvitationId()).isEqualTo(UPDATED_VISITANT_INVITATION_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingInvitationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = invitationScheduleRepository.findAll().size();

        // Create the InvitationSchedule
        InvitationScheduleDTO invitationScheduleDTO = invitationScheduleMapper.toDto(invitationSchedule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvitationScheduleMockMvc.perform(put("/api/invitation-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitationScheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the InvitationSchedule in the database
        List<InvitationSchedule> invitationScheduleList = invitationScheduleRepository.findAll();
        assertThat(invitationScheduleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvitationSchedule() throws Exception {
        // Initialize the database
        invitationScheduleRepository.saveAndFlush(invitationSchedule);
        int databaseSizeBeforeDelete = invitationScheduleRepository.findAll().size();

        // Get the invitationSchedule
        restInvitationScheduleMockMvc.perform(delete("/api/invitation-schedules/{id}", invitationSchedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvitationSchedule> invitationScheduleList = invitationScheduleRepository.findAll();
        assertThat(invitationScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvitationSchedule.class);
        InvitationSchedule invitationSchedule1 = new InvitationSchedule();
        invitationSchedule1.setId(1L);
        InvitationSchedule invitationSchedule2 = new InvitationSchedule();
        invitationSchedule2.setId(invitationSchedule1.getId());
        assertThat(invitationSchedule1).isEqualTo(invitationSchedule2);
        invitationSchedule2.setId(2L);
        assertThat(invitationSchedule1).isNotEqualTo(invitationSchedule2);
        invitationSchedule1.setId(null);
        assertThat(invitationSchedule1).isNotEqualTo(invitationSchedule2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvitationScheduleDTO.class);
        InvitationScheduleDTO invitationScheduleDTO1 = new InvitationScheduleDTO();
        invitationScheduleDTO1.setId(1L);
        InvitationScheduleDTO invitationScheduleDTO2 = new InvitationScheduleDTO();
        assertThat(invitationScheduleDTO1).isNotEqualTo(invitationScheduleDTO2);
        invitationScheduleDTO2.setId(invitationScheduleDTO1.getId());
        assertThat(invitationScheduleDTO1).isEqualTo(invitationScheduleDTO2);
        invitationScheduleDTO2.setId(2L);
        assertThat(invitationScheduleDTO1).isNotEqualTo(invitationScheduleDTO2);
        invitationScheduleDTO1.setId(null);
        assertThat(invitationScheduleDTO1).isNotEqualTo(invitationScheduleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invitationScheduleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invitationScheduleMapper.fromId(null)).isNull();
    }
}
