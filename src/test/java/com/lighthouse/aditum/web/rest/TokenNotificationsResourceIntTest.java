package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.TokenNotifications;
import com.lighthouse.aditum.repository.TokenNotificationsRepository;
import com.lighthouse.aditum.service.TokenNotificationsService;
import com.lighthouse.aditum.service.dto.TokenNotificationsDTO;
import com.lighthouse.aditum.service.mapper.TokenNotificationsMapper;
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
 * Test class for the TokenNotificationsResource REST controller.
 *
 * @see TokenNotificationsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class TokenNotificationsResourceIntTest {

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    @Autowired
    private TokenNotificationsRepository tokenNotificationsRepository;

    @Autowired
    private TokenNotificationsMapper tokenNotificationsMapper;

    @Autowired
    private TokenNotificationsService tokenNotificationsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTokenNotificationsMockMvc;

    private TokenNotifications tokenNotifications;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TokenNotificationsResource tokenNotificationsResource = new TokenNotificationsResource(tokenNotificationsService);
        this.restTokenNotificationsMockMvc = MockMvcBuilders.standaloneSetup(tokenNotificationsResource)
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
    public static TokenNotifications createEntity(EntityManager em) {
        TokenNotifications tokenNotifications = new TokenNotifications()
            .token(DEFAULT_TOKEN);
        return tokenNotifications;
    }

    @Before
    public void initTest() {
        tokenNotifications = createEntity(em);
    }

    @Test
    @Transactional
    public void createTokenNotifications() throws Exception {
        int databaseSizeBeforeCreate = tokenNotificationsRepository.findAll().size();

        // Create the TokenNotifications
        TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsMapper.toDto(tokenNotifications);
        restTokenNotificationsMockMvc.perform(post("/api/token-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tokenNotificationsDTO)))
            .andExpect(status().isCreated());

        // Validate the TokenNotifications in the database
        List<TokenNotifications> tokenNotificationsList = tokenNotificationsRepository.findAll();
        assertThat(tokenNotificationsList).hasSize(databaseSizeBeforeCreate + 1);
        TokenNotifications testTokenNotifications = tokenNotificationsList.get(tokenNotificationsList.size() - 1);
        assertThat(testTokenNotifications.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    public void createTokenNotificationsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tokenNotificationsRepository.findAll().size();

        // Create the TokenNotifications with an existing ID
        tokenNotifications.setId(1L);
        TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsMapper.toDto(tokenNotifications);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTokenNotificationsMockMvc.perform(post("/api/token-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tokenNotificationsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TokenNotifications in the database
        List<TokenNotifications> tokenNotificationsList = tokenNotificationsRepository.findAll();
        assertThat(tokenNotificationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTokenNotifications() throws Exception {
        // Initialize the database
        tokenNotificationsRepository.saveAndFlush(tokenNotifications);

        // Get all the tokenNotificationsList
        restTokenNotificationsMockMvc.perform(get("/api/token-notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tokenNotifications.getId().intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())));
    }

    @Test
    @Transactional
    public void getTokenNotifications() throws Exception {
        // Initialize the database
        tokenNotificationsRepository.saveAndFlush(tokenNotifications);

        // Get the tokenNotifications
        restTokenNotificationsMockMvc.perform(get("/api/token-notifications/{id}", tokenNotifications.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tokenNotifications.getId().intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTokenNotifications() throws Exception {
        // Get the tokenNotifications
        restTokenNotificationsMockMvc.perform(get("/api/token-notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTokenNotifications() throws Exception {
        // Initialize the database
        tokenNotificationsRepository.saveAndFlush(tokenNotifications);
        int databaseSizeBeforeUpdate = tokenNotificationsRepository.findAll().size();

        // Update the tokenNotifications
        TokenNotifications updatedTokenNotifications = tokenNotificationsRepository.findOne(tokenNotifications.getId());
        // Disconnect from session so that the updates on updatedTokenNotifications are not directly saved in db
        em.detach(updatedTokenNotifications);
        updatedTokenNotifications
            .token(UPDATED_TOKEN);
        TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsMapper.toDto(updatedTokenNotifications);

        restTokenNotificationsMockMvc.perform(put("/api/token-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tokenNotificationsDTO)))
            .andExpect(status().isOk());

        // Validate the TokenNotifications in the database
        List<TokenNotifications> tokenNotificationsList = tokenNotificationsRepository.findAll();
        assertThat(tokenNotificationsList).hasSize(databaseSizeBeforeUpdate);
        TokenNotifications testTokenNotifications = tokenNotificationsList.get(tokenNotificationsList.size() - 1);
        assertThat(testTokenNotifications.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    public void updateNonExistingTokenNotifications() throws Exception {
        int databaseSizeBeforeUpdate = tokenNotificationsRepository.findAll().size();

        // Create the TokenNotifications
        TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsMapper.toDto(tokenNotifications);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTokenNotificationsMockMvc.perform(put("/api/token-notifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tokenNotificationsDTO)))
            .andExpect(status().isCreated());

        // Validate the TokenNotifications in the database
        List<TokenNotifications> tokenNotificationsList = tokenNotificationsRepository.findAll();
        assertThat(tokenNotificationsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTokenNotifications() throws Exception {
        // Initialize the database
        tokenNotificationsRepository.saveAndFlush(tokenNotifications);
        int databaseSizeBeforeDelete = tokenNotificationsRepository.findAll().size();

        // Get the tokenNotifications
        restTokenNotificationsMockMvc.perform(delete("/api/token-notifications/{id}", tokenNotifications.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TokenNotifications> tokenNotificationsList = tokenNotificationsRepository.findAll();
        assertThat(tokenNotificationsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenNotifications.class);
        TokenNotifications tokenNotifications1 = new TokenNotifications();
        tokenNotifications1.setId(1L);
        TokenNotifications tokenNotifications2 = new TokenNotifications();
        tokenNotifications2.setId(tokenNotifications1.getId());
        assertThat(tokenNotifications1).isEqualTo(tokenNotifications2);
        tokenNotifications2.setId(2L);
        assertThat(tokenNotifications1).isNotEqualTo(tokenNotifications2);
        tokenNotifications1.setId(null);
        assertThat(tokenNotifications1).isNotEqualTo(tokenNotifications2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenNotificationsDTO.class);
        TokenNotificationsDTO tokenNotificationsDTO1 = new TokenNotificationsDTO();
        tokenNotificationsDTO1.setId(1L);
        TokenNotificationsDTO tokenNotificationsDTO2 = new TokenNotificationsDTO();
        assertThat(tokenNotificationsDTO1).isNotEqualTo(tokenNotificationsDTO2);
        tokenNotificationsDTO2.setId(tokenNotificationsDTO1.getId());
        assertThat(tokenNotificationsDTO1).isEqualTo(tokenNotificationsDTO2);
        tokenNotificationsDTO2.setId(2L);
        assertThat(tokenNotificationsDTO1).isNotEqualTo(tokenNotificationsDTO2);
        tokenNotificationsDTO1.setId(null);
        assertThat(tokenNotificationsDTO1).isNotEqualTo(tokenNotificationsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tokenNotificationsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tokenNotificationsMapper.fromId(null)).isNull();
    }
}
