package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.AdminInfo;
import com.lighthouse.aditum.repository.AdminInfoRepository;
import com.lighthouse.aditum.service.AdminInfoService;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.mapper.AdminInfoMapper;
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
 * Test class for the AdminInfoResource REST controller.
 *
 * @see AdminInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class AdminInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDLASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATIONNUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATIONNUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_ENABLED = 0;
    private static final Integer UPDATED_ENABLED = 1;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ADMINISTRADA_OFICIALES = 1;
    private static final Integer UPDATED_ADMINISTRADA_OFICIALES = 2;

    @Autowired
    private AdminInfoRepository adminInfoRepository;

    @Autowired
    private AdminInfoMapper adminInfoMapper;

    @Autowired
    private AdminInfoService adminInfoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdminInfoMockMvc;

    private AdminInfo adminInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdminInfoResource adminInfoResource = new AdminInfoResource(adminInfoService);
        this.restAdminInfoMockMvc = MockMvcBuilders.standaloneSetup(adminInfoResource)
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
    public static AdminInfo createEntity(EntityManager em) {
        AdminInfo adminInfo = new AdminInfo()
                .name(DEFAULT_NAME)
                .lastname(DEFAULT_LASTNAME)
                .secondlastname(DEFAULT_SECONDLASTNAME)
                .identificationnumber(DEFAULT_IDENTIFICATIONNUMBER)
                .email(DEFAULT_EMAIL)
                .image(DEFAULT_IMAGE)
                .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
                .enabled(DEFAULT_ENABLED)
                .image_url(DEFAULT_IMAGE_URL)
                .administradaOficiales(DEFAULT_ADMINISTRADA_OFICIALES);
        return adminInfo;
    }

    @Before
    public void initTest() {
        adminInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdminInfo() throws Exception {
        int databaseSizeBeforeCreate = adminInfoRepository.findAll().size();

        // Create the AdminInfo
        AdminInfoDTO adminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);

        restAdminInfoMockMvc.perform(post("/api/admin-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the AdminInfo in the database
        List<AdminInfo> adminInfoList = adminInfoRepository.findAll();
        assertThat(adminInfoList).hasSize(databaseSizeBeforeCreate + 1);
        AdminInfo testAdminInfo = adminInfoList.get(adminInfoList.size() - 1);
        assertThat(testAdminInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdminInfo.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testAdminInfo.getSecondlastname()).isEqualTo(DEFAULT_SECONDLASTNAME);
        assertThat(testAdminInfo.getIdentificationnumber()).isEqualTo(DEFAULT_IDENTIFICATIONNUMBER);
        assertThat(testAdminInfo.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAdminInfo.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAdminInfo.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testAdminInfo.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testAdminInfo.getImage_url()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testAdminInfo.getAdministradaOficiales()).isEqualTo(DEFAULT_ADMINISTRADA_OFICIALES);
    }

    @Test
    @Transactional
    public void createAdminInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adminInfoRepository.findAll().size();

        // Create the AdminInfo with an existing ID
        AdminInfo existingAdminInfo = new AdminInfo();
        existingAdminInfo.setId(1L);
        AdminInfoDTO existingAdminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(existingAdminInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminInfoMockMvc.perform(post("/api/admin-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAdminInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<AdminInfo> adminInfoList = adminInfoRepository.findAll();
        assertThat(adminInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdminInfos() throws Exception {
        // Initialize the database
        adminInfoRepository.saveAndFlush(adminInfo);

        // Get all the adminInfoList
        restAdminInfoMockMvc.perform(get("/api/admin-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())))
            .andExpect(jsonPath("$.[*].secondlastname").value(hasItem(DEFAULT_SECONDLASTNAME.toString())))
            .andExpect(jsonPath("$.[*].identificationnumber").value(hasItem(DEFAULT_IDENTIFICATIONNUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED)))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].administradaOficiales").value(hasItem(DEFAULT_ADMINISTRADA_OFICIALES)));
    }

    @Test
    @Transactional
    public void getAdminInfo() throws Exception {
        // Initialize the database
        adminInfoRepository.saveAndFlush(adminInfo);

        // Get the adminInfo
        restAdminInfoMockMvc.perform(get("/api/admin-infos/{id}", adminInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adminInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()))
            .andExpect(jsonPath("$.secondlastname").value(DEFAULT_SECONDLASTNAME.toString()))
            .andExpect(jsonPath("$.identificationnumber").value(DEFAULT_IDENTIFICATIONNUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED))
            .andExpect(jsonPath("$.image_url").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.administradaOficiales").value(DEFAULT_ADMINISTRADA_OFICIALES));
    }

    @Test
    @Transactional
    public void getNonExistingAdminInfo() throws Exception {
        // Get the adminInfo
        restAdminInfoMockMvc.perform(get("/api/admin-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdminInfo() throws Exception {
        // Initialize the database
        adminInfoRepository.saveAndFlush(adminInfo);
        int databaseSizeBeforeUpdate = adminInfoRepository.findAll().size();

        // Update the adminInfo
        AdminInfo updatedAdminInfo = adminInfoRepository.findOne(adminInfo.getId());
        updatedAdminInfo
                .name(UPDATED_NAME)
                .lastname(UPDATED_LASTNAME)
                .secondlastname(UPDATED_SECONDLASTNAME)
                .identificationnumber(UPDATED_IDENTIFICATIONNUMBER)
                .email(UPDATED_EMAIL)
                .image(UPDATED_IMAGE)
                .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
                .enabled(UPDATED_ENABLED)
                .image_url(UPDATED_IMAGE_URL)
                .administradaOficiales(UPDATED_ADMINISTRADA_OFICIALES);
        AdminInfoDTO adminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(updatedAdminInfo);

        restAdminInfoMockMvc.perform(put("/api/admin-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminInfoDTO)))
            .andExpect(status().isOk());

        // Validate the AdminInfo in the database
        List<AdminInfo> adminInfoList = adminInfoRepository.findAll();
        assertThat(adminInfoList).hasSize(databaseSizeBeforeUpdate);
        AdminInfo testAdminInfo = adminInfoList.get(adminInfoList.size() - 1);
        assertThat(testAdminInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdminInfo.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testAdminInfo.getSecondlastname()).isEqualTo(UPDATED_SECONDLASTNAME);
        assertThat(testAdminInfo.getIdentificationnumber()).isEqualTo(UPDATED_IDENTIFICATIONNUMBER);
        assertThat(testAdminInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdminInfo.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAdminInfo.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testAdminInfo.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testAdminInfo.getImage_url()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testAdminInfo.getAdministradaOficiales()).isEqualTo(UPDATED_ADMINISTRADA_OFICIALES);
    }

    @Test
    @Transactional
    public void updateNonExistingAdminInfo() throws Exception {
        int databaseSizeBeforeUpdate = adminInfoRepository.findAll().size();

        // Create the AdminInfo
        AdminInfoDTO adminInfoDTO = adminInfoMapper.adminInfoToAdminInfoDTO(adminInfo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdminInfoMockMvc.perform(put("/api/admin-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adminInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the AdminInfo in the database
        List<AdminInfo> adminInfoList = adminInfoRepository.findAll();
        assertThat(adminInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdminInfo() throws Exception {
        // Initialize the database
        adminInfoRepository.saveAndFlush(adminInfo);
        int databaseSizeBeforeDelete = adminInfoRepository.findAll().size();

        // Get the adminInfo
        restAdminInfoMockMvc.perform(delete("/api/admin-infos/{id}", adminInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AdminInfo> adminInfoList = adminInfoRepository.findAll();
        assertThat(adminInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminInfo.class);
    }
}
