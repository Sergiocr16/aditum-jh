package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.CommonArea;
import com.lighthouse.aditum.repository.CommonAreaRepository;
import com.lighthouse.aditum.service.CommonAreaService;
import com.lighthouse.aditum.service.dto.CommonAreaDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.lighthouse.aditum.web.rest.TestUtil.sameInstant;
//import static com.lighthouse.aditum.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommonAreaResource REST controller.
 *
 * @see CommonAreaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class CommonAreaResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESERVATION_CHARGE = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_CHARGE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVOLUTION_AMMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DEVOLUTION_AMMOUNT = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHARGE_REQUIRED = 1;
    private static final Integer UPDATED_CHARGE_REQUIRED = 0;

    private static final Integer DEFAULT_RESERVATION_WITH_DEBT = 1;
    private static final Integer UPDATED_RESERVATION_WITH_DEBT = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_MAXIMUN_HOURS = 1;
    private static final Integer UPDATED_MAXIMUN_HOURS = 2;

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final Integer DEFAULT_COMPANY_ID = 1;
    private static final Integer UPDATED_COMPANY_ID = 2;

    private static final Integer DEFAULT_HAS_BLOCKS = 1;
    private static final Integer UPDATED_HAS_BLOCKS = 2;

    private static final Integer DEFAULT_HAS_DAYS_BEFORE_TO_RESERVE = 1;
    private static final Integer UPDATED_HAS_DAYS_BEFORE_TO_RESERVE = 2;

    private static final Integer DEFAULT_DAYS_BEFORE_TO_RESERVE = 1;
    private static final Integer UPDATED_DAYS_BEFORE_TO_RESERVE = 2;

    private static final Integer DEFAULT_HAS_DAYS_TO_RESERVE_IF_FREE = 1;
    private static final Integer UPDATED_HAS_DAYS_TO_RESERVE_IF_FREE = 2;

    private static final String DEFAULT_DAYS_TO_RESERVE_IF_FREE = "AAAAAAAAAA";
    private static final String UPDATED_DAYS_TO_RESERVE_IF_FREE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HAS_DISTANCE_BETWEEN_RESERVATIONS = 1;
    private static final Integer UPDATED_HAS_DISTANCE_BETWEEN_RESERVATIONS = 2;

    private static final Integer DEFAULT_DISTANCE_BETWEEN_RESERVATIONS = 1;
    private static final Integer UPDATED_DISTANCE_BETWEEN_RESERVATIONS = 2;

    private static final Integer DEFAULT_NEEDS_APPROVAL = 1;
    private static final Integer UPDATED_NEEDS_APPROVAL = 2;

    private static final Integer DEFAULT_HAS_VALIDITY_TIME = 1;
    private static final Integer UPDATED_HAS_VALIDITY_TIME = 2;

    private static final Integer DEFAULT_VALIDITY_TIME_HOURS = 1;
    private static final Integer UPDATED_VALIDITY_TIME_HOURS = 2;

    private static final Integer DEFAULT_HAS_RESERVATIONS_LIMIT = 1;
    private static final Integer UPDATED_HAS_RESERVATIONS_LIMIT = 2;

    private static final ZonedDateTime DEFAULT_PERIOD_BEGIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PERIOD_BEGIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PERIOD_TIMES = 1;
    private static final Integer UPDATED_PERIOD_TIMES = 2;

    private static final Integer DEFAULT_PERIOD_MONTH_END = 1;
    private static final Integer UPDATED_PERIOD_MONTH_END = 2;

    private static final Integer DEFAULT_LIMIT_PEOPLE_PER_RESERVATION = 1;
    private static final Integer UPDATED_LIMIT_PEOPLE_PER_RESERVATION = 2;

    private static final Integer DEFAULT_LIMIT_ACTIVE_RESERVATIONS = 1;
    private static final Integer UPDATED_LIMIT_ACTIVE_RESERVATIONS = 2;

    private static final Boolean DEFAULT_HAS_MAXIMUN_DAYS_IN_ADVANCE = false;
    private static final Boolean UPDATED_HAS_MAXIMUN_DAYS_IN_ADVANCE = true;

    private static final Integer DEFAULT_MAXIMUN_DAYS_IN_ADVANCE = 1;
    private static final Integer UPDATED_MAXIMUN_DAYS_IN_ADVANCE = 2;

    private static final Boolean DEFAULT_HAS_DEFINE_PEOPLE_QUANTITY = false;
    private static final Boolean UPDATED_HAS_DEFINE_PEOPLE_QUANTITY = true;

    private static final Integer DEFAULT_QUANTITY_GUEST_LIMIT = 1;
    private static final Integer UPDATED_QUANTITY_GUEST_LIMIT = 2;

    private static final Integer DEFAULT_TIMES_PER_DAY = 1;
    private static final Integer UPDATED_TIMES_PER_DAY = 2;

    @Autowired
    private CommonAreaRepository commonAreaRepository;

    @Autowired
    private CommonAreaMapper commonAreaMapper;

    @Autowired
    private CommonAreaService commonAreaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommonAreaMockMvc;

    private CommonArea commonArea;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommonAreaResource commonAreaResource = new CommonAreaResource(commonAreaService);
        this.restCommonAreaMockMvc = MockMvcBuilders.standaloneSetup(commonAreaResource)
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
    public static CommonArea createEntity(EntityManager em) {
        CommonArea commonArea = new CommonArea()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .reservationCharge(DEFAULT_RESERVATION_CHARGE)
            .devolutionAmmount(DEFAULT_DEVOLUTION_AMMOUNT)
            .chargeRequired(DEFAULT_CHARGE_REQUIRED)
            .reservationWithDebt(DEFAULT_RESERVATION_WITH_DEBT)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .maximunHours(DEFAULT_MAXIMUN_HOURS)
            .deleted(DEFAULT_DELETED)
            .companyId(DEFAULT_COMPANY_ID)
            .hasBlocks(DEFAULT_HAS_BLOCKS)
            .hasDaysBeforeToReserve(DEFAULT_HAS_DAYS_BEFORE_TO_RESERVE)
            .daysBeforeToReserve(DEFAULT_DAYS_BEFORE_TO_RESERVE)
            .hasDaysToReserveIfFree(DEFAULT_HAS_DAYS_TO_RESERVE_IF_FREE)
            .daysToReserveIfFree(DEFAULT_DAYS_TO_RESERVE_IF_FREE)
            .hasDistanceBetweenReservations(DEFAULT_HAS_DISTANCE_BETWEEN_RESERVATIONS)
            .distanceBetweenReservations(DEFAULT_DISTANCE_BETWEEN_RESERVATIONS)
            .needsApproval(DEFAULT_NEEDS_APPROVAL)
            .hasValidityTime(DEFAULT_HAS_VALIDITY_TIME)
            .validityTimeHours(DEFAULT_VALIDITY_TIME_HOURS)
            .hasReservationsLimit(DEFAULT_HAS_RESERVATIONS_LIMIT)
            .periodBegin(DEFAULT_PERIOD_BEGIN)
            .periodTimes(DEFAULT_PERIOD_TIMES)
            .periodMonthEnd(DEFAULT_PERIOD_MONTH_END)
            .limitPeoplePerReservation(DEFAULT_LIMIT_PEOPLE_PER_RESERVATION)
            .limitActiveReservations(DEFAULT_LIMIT_ACTIVE_RESERVATIONS)
            .hasMaximunDaysInAdvance(DEFAULT_HAS_MAXIMUN_DAYS_IN_ADVANCE)
            .maximunDaysInAdvance(DEFAULT_MAXIMUN_DAYS_IN_ADVANCE)
            .hasDefinePeopleQuantity(DEFAULT_HAS_DEFINE_PEOPLE_QUANTITY)
            .quantityGuestLimit(DEFAULT_QUANTITY_GUEST_LIMIT)
            .timesPerDay(DEFAULT_TIMES_PER_DAY);
        return commonArea;
    }

    @Before
    public void initTest() {
        commonArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommonArea() throws Exception {
        int databaseSizeBeforeCreate = commonAreaRepository.findAll().size();

        // Create the CommonArea
        CommonAreaDTO commonAreaDTO = commonAreaMapper.toDto(commonArea);
        restCommonAreaMockMvc.perform(post("/api/common-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the CommonArea in the database
        List<CommonArea> commonAreaList = commonAreaRepository.findAll();
        assertThat(commonAreaList).hasSize(databaseSizeBeforeCreate + 1);
        CommonArea testCommonArea = commonAreaList.get(commonAreaList.size() - 1);
        assertThat(testCommonArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonArea.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommonArea.getReservationCharge()).isEqualTo(DEFAULT_RESERVATION_CHARGE);
        assertThat(testCommonArea.getDevolutionAmmount()).isEqualTo(DEFAULT_DEVOLUTION_AMMOUNT);
        assertThat(testCommonArea.getChargeRequired()).isEqualTo(DEFAULT_CHARGE_REQUIRED);
        assertThat(testCommonArea.getReservationWithDebt()).isEqualTo(DEFAULT_RESERVATION_WITH_DEBT);
        assertThat(testCommonArea.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCommonArea.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testCommonArea.getMaximunHours()).isEqualTo(DEFAULT_MAXIMUN_HOURS);
        assertThat(testCommonArea.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testCommonArea.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testCommonArea.getHasBlocks()).isEqualTo(DEFAULT_HAS_BLOCKS);
        assertThat(testCommonArea.getHasDaysBeforeToReserve()).isEqualTo(DEFAULT_HAS_DAYS_BEFORE_TO_RESERVE);
        assertThat(testCommonArea.getDaysBeforeToReserve()).isEqualTo(DEFAULT_DAYS_BEFORE_TO_RESERVE);
        assertThat(testCommonArea.getHasDaysToReserveIfFree()).isEqualTo(DEFAULT_HAS_DAYS_TO_RESERVE_IF_FREE);
        assertThat(testCommonArea.getDaysToReserveIfFree()).isEqualTo(DEFAULT_DAYS_TO_RESERVE_IF_FREE);
        assertThat(testCommonArea.getHasDistanceBetweenReservations()).isEqualTo(DEFAULT_HAS_DISTANCE_BETWEEN_RESERVATIONS);
        assertThat(testCommonArea.getDistanceBetweenReservations()).isEqualTo(DEFAULT_DISTANCE_BETWEEN_RESERVATIONS);
        assertThat(testCommonArea.getNeedsApproval()).isEqualTo(DEFAULT_NEEDS_APPROVAL);
        assertThat(testCommonArea.getHasValidityTime()).isEqualTo(DEFAULT_HAS_VALIDITY_TIME);
        assertThat(testCommonArea.getValidityTimeHours()).isEqualTo(DEFAULT_VALIDITY_TIME_HOURS);
        assertThat(testCommonArea.getHasReservationsLimit()).isEqualTo(DEFAULT_HAS_RESERVATIONS_LIMIT);
        assertThat(testCommonArea.getPeriodBegin()).isEqualTo(DEFAULT_PERIOD_BEGIN);
        assertThat(testCommonArea.getPeriodTimes()).isEqualTo(DEFAULT_PERIOD_TIMES);
        assertThat(testCommonArea.getPeriodMonthEnd()).isEqualTo(DEFAULT_PERIOD_MONTH_END);
        assertThat(testCommonArea.getLimitPeoplePerReservation()).isEqualTo(DEFAULT_LIMIT_PEOPLE_PER_RESERVATION);
        assertThat(testCommonArea.getLimitActiveReservations()).isEqualTo(DEFAULT_LIMIT_ACTIVE_RESERVATIONS);
        assertThat(testCommonArea.isHasMaximunDaysInAdvance()).isEqualTo(DEFAULT_HAS_MAXIMUN_DAYS_IN_ADVANCE);
        assertThat(testCommonArea.getMaximunDaysInAdvance()).isEqualTo(DEFAULT_MAXIMUN_DAYS_IN_ADVANCE);
        assertThat(testCommonArea.isHasDefinePeopleQuantity()).isEqualTo(DEFAULT_HAS_DEFINE_PEOPLE_QUANTITY);
        assertThat(testCommonArea.getQuantityGuestLimit()).isEqualTo(DEFAULT_QUANTITY_GUEST_LIMIT);
        assertThat(testCommonArea.getTimesPerDay()).isEqualTo(DEFAULT_TIMES_PER_DAY);
    }

    @Test
    @Transactional
    public void createCommonAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commonAreaRepository.findAll().size();

        // Create the CommonArea with an existing ID
        commonArea.setId(1L);
        CommonAreaDTO commonAreaDTO = commonAreaMapper.toDto(commonArea);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonAreaMockMvc.perform(post("/api/common-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CommonArea in the database
        List<CommonArea> commonAreaList = commonAreaRepository.findAll();
        assertThat(commonAreaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonAreaRepository.findAll().size();
        // set the field null
        commonArea.setName(null);

        // Create the CommonArea, which fails.
        CommonAreaDTO commonAreaDTO = commonAreaMapper.toDto(commonArea);

        restCommonAreaMockMvc.perform(post("/api/common-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaDTO)))
            .andExpect(status().isBadRequest());

        List<CommonArea> commonAreaList = commonAreaRepository.findAll();
        assertThat(commonAreaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommonAreas() throws Exception {
        // Initialize the database
        commonAreaRepository.saveAndFlush(commonArea);

        // Get all the commonAreaList
        restCommonAreaMockMvc.perform(get("/api/common-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].reservationCharge").value(hasItem(DEFAULT_RESERVATION_CHARGE.toString())))
            .andExpect(jsonPath("$.[*].devolutionAmmount").value(hasItem(DEFAULT_DEVOLUTION_AMMOUNT.toString())))
            .andExpect(jsonPath("$.[*].chargeRequired").value(hasItem(DEFAULT_CHARGE_REQUIRED)))
            .andExpect(jsonPath("$.[*].reservationWithDebt").value(hasItem(DEFAULT_RESERVATION_WITH_DEBT)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].maximunHours").value(hasItem(DEFAULT_MAXIMUN_HOURS)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].hasBlocks").value(hasItem(DEFAULT_HAS_BLOCKS)))
            .andExpect(jsonPath("$.[*].hasDaysBeforeToReserve").value(hasItem(DEFAULT_HAS_DAYS_BEFORE_TO_RESERVE)))
            .andExpect(jsonPath("$.[*].daysBeforeToReserve").value(hasItem(DEFAULT_DAYS_BEFORE_TO_RESERVE)))
            .andExpect(jsonPath("$.[*].hasDaysToReserveIfFree").value(hasItem(DEFAULT_HAS_DAYS_TO_RESERVE_IF_FREE)))
            .andExpect(jsonPath("$.[*].daysToReserveIfFree").value(hasItem(DEFAULT_DAYS_TO_RESERVE_IF_FREE.toString())))
            .andExpect(jsonPath("$.[*].hasDistanceBetweenReservations").value(hasItem(DEFAULT_HAS_DISTANCE_BETWEEN_RESERVATIONS)))
            .andExpect(jsonPath("$.[*].distanceBetweenReservations").value(hasItem(DEFAULT_DISTANCE_BETWEEN_RESERVATIONS)))
            .andExpect(jsonPath("$.[*].needsApproval").value(hasItem(DEFAULT_NEEDS_APPROVAL)))
            .andExpect(jsonPath("$.[*].hasValidityTime").value(hasItem(DEFAULT_HAS_VALIDITY_TIME)))
            .andExpect(jsonPath("$.[*].validityTimeHours").value(hasItem(DEFAULT_VALIDITY_TIME_HOURS)))
            .andExpect(jsonPath("$.[*].hasReservationsLimit").value(hasItem(DEFAULT_HAS_RESERVATIONS_LIMIT)))
            .andExpect(jsonPath("$.[*].periodBegin").value(hasItem(sameInstant(DEFAULT_PERIOD_BEGIN))))
            .andExpect(jsonPath("$.[*].periodTimes").value(hasItem(DEFAULT_PERIOD_TIMES)))
            .andExpect(jsonPath("$.[*].periodMonthEnd").value(hasItem(DEFAULT_PERIOD_MONTH_END)))
            .andExpect(jsonPath("$.[*].limitPeoplePerReservation").value(hasItem(DEFAULT_LIMIT_PEOPLE_PER_RESERVATION)))
            .andExpect(jsonPath("$.[*].limitActiveReservations").value(hasItem(DEFAULT_LIMIT_ACTIVE_RESERVATIONS)))
            .andExpect(jsonPath("$.[*].hasMaximunDaysInAdvance").value(hasItem(DEFAULT_HAS_MAXIMUN_DAYS_IN_ADVANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].maximunDaysInAdvance").value(hasItem(DEFAULT_MAXIMUN_DAYS_IN_ADVANCE)))
            .andExpect(jsonPath("$.[*].hasDefinePeopleQuantity").value(hasItem(DEFAULT_HAS_DEFINE_PEOPLE_QUANTITY.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityGuestLimit").value(hasItem(DEFAULT_QUANTITY_GUEST_LIMIT)))
            .andExpect(jsonPath("$.[*].timesPerDay").value(hasItem(DEFAULT_TIMES_PER_DAY)));
    }

    @Test
    @Transactional
    public void getCommonArea() throws Exception {
        // Initialize the database
        commonAreaRepository.saveAndFlush(commonArea);

        // Get the commonArea
        restCommonAreaMockMvc.perform(get("/api/common-areas/{id}", commonArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commonArea.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.reservationCharge").value(DEFAULT_RESERVATION_CHARGE.toString()))
            .andExpect(jsonPath("$.devolutionAmmount").value(DEFAULT_DEVOLUTION_AMMOUNT.toString()))
            .andExpect(jsonPath("$.chargeRequired").value(DEFAULT_CHARGE_REQUIRED))
            .andExpect(jsonPath("$.reservationWithDebt").value(DEFAULT_RESERVATION_WITH_DEBT))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.maximunHours").value(DEFAULT_MAXIMUN_HOURS))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID))
            .andExpect(jsonPath("$.hasBlocks").value(DEFAULT_HAS_BLOCKS))
            .andExpect(jsonPath("$.hasDaysBeforeToReserve").value(DEFAULT_HAS_DAYS_BEFORE_TO_RESERVE))
            .andExpect(jsonPath("$.daysBeforeToReserve").value(DEFAULT_DAYS_BEFORE_TO_RESERVE))
            .andExpect(jsonPath("$.hasDaysToReserveIfFree").value(DEFAULT_HAS_DAYS_TO_RESERVE_IF_FREE))
            .andExpect(jsonPath("$.daysToReserveIfFree").value(DEFAULT_DAYS_TO_RESERVE_IF_FREE.toString()))
            .andExpect(jsonPath("$.hasDistanceBetweenReservations").value(DEFAULT_HAS_DISTANCE_BETWEEN_RESERVATIONS))
            .andExpect(jsonPath("$.distanceBetweenReservations").value(DEFAULT_DISTANCE_BETWEEN_RESERVATIONS))
            .andExpect(jsonPath("$.needsApproval").value(DEFAULT_NEEDS_APPROVAL))
            .andExpect(jsonPath("$.hasValidityTime").value(DEFAULT_HAS_VALIDITY_TIME))
            .andExpect(jsonPath("$.validityTimeHours").value(DEFAULT_VALIDITY_TIME_HOURS))
            .andExpect(jsonPath("$.hasReservationsLimit").value(DEFAULT_HAS_RESERVATIONS_LIMIT))
            .andExpect(jsonPath("$.periodBegin").value(sameInstant(DEFAULT_PERIOD_BEGIN)))
            .andExpect(jsonPath("$.periodTimes").value(DEFAULT_PERIOD_TIMES))
            .andExpect(jsonPath("$.periodMonthEnd").value(DEFAULT_PERIOD_MONTH_END))
            .andExpect(jsonPath("$.limitPeoplePerReservation").value(DEFAULT_LIMIT_PEOPLE_PER_RESERVATION))
            .andExpect(jsonPath("$.limitActiveReservations").value(DEFAULT_LIMIT_ACTIVE_RESERVATIONS))
            .andExpect(jsonPath("$.hasMaximunDaysInAdvance").value(DEFAULT_HAS_MAXIMUN_DAYS_IN_ADVANCE.booleanValue()))
            .andExpect(jsonPath("$.maximunDaysInAdvance").value(DEFAULT_MAXIMUN_DAYS_IN_ADVANCE))
            .andExpect(jsonPath("$.hasDefinePeopleQuantity").value(DEFAULT_HAS_DEFINE_PEOPLE_QUANTITY.booleanValue()))
            .andExpect(jsonPath("$.quantityGuestLimit").value(DEFAULT_QUANTITY_GUEST_LIMIT))
            .andExpect(jsonPath("$.timesPerDay").value(DEFAULT_TIMES_PER_DAY));
    }

    @Test
    @Transactional
    public void getNonExistingCommonArea() throws Exception {
        // Get the commonArea
        restCommonAreaMockMvc.perform(get("/api/common-areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommonArea() throws Exception {
        // Initialize the database
        commonAreaRepository.saveAndFlush(commonArea);
        int databaseSizeBeforeUpdate = commonAreaRepository.findAll().size();

        // Update the commonArea
        CommonArea updatedCommonArea = commonAreaRepository.findOne(commonArea.getId());
        // Disconnect from session so that the updates on updatedCommonArea are not directly saved in db
        em.detach(updatedCommonArea);
        updatedCommonArea
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reservationCharge(UPDATED_RESERVATION_CHARGE)
            .devolutionAmmount(UPDATED_DEVOLUTION_AMMOUNT)
            .chargeRequired(UPDATED_CHARGE_REQUIRED)
            .reservationWithDebt(UPDATED_RESERVATION_WITH_DEBT)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .maximunHours(UPDATED_MAXIMUN_HOURS)
            .deleted(UPDATED_DELETED)
            .companyId(UPDATED_COMPANY_ID)
            .hasBlocks(UPDATED_HAS_BLOCKS)
            .hasDaysBeforeToReserve(UPDATED_HAS_DAYS_BEFORE_TO_RESERVE)
            .daysBeforeToReserve(UPDATED_DAYS_BEFORE_TO_RESERVE)
            .hasDaysToReserveIfFree(UPDATED_HAS_DAYS_TO_RESERVE_IF_FREE)
            .daysToReserveIfFree(UPDATED_DAYS_TO_RESERVE_IF_FREE)
            .hasDistanceBetweenReservations(UPDATED_HAS_DISTANCE_BETWEEN_RESERVATIONS)
            .distanceBetweenReservations(UPDATED_DISTANCE_BETWEEN_RESERVATIONS)
            .needsApproval(UPDATED_NEEDS_APPROVAL)
            .hasValidityTime(UPDATED_HAS_VALIDITY_TIME)
            .validityTimeHours(UPDATED_VALIDITY_TIME_HOURS)
            .hasReservationsLimit(UPDATED_HAS_RESERVATIONS_LIMIT)
            .periodBegin(UPDATED_PERIOD_BEGIN)
            .periodTimes(UPDATED_PERIOD_TIMES)
            .periodMonthEnd(UPDATED_PERIOD_MONTH_END)
            .limitPeoplePerReservation(UPDATED_LIMIT_PEOPLE_PER_RESERVATION)
            .limitActiveReservations(UPDATED_LIMIT_ACTIVE_RESERVATIONS)
            .hasMaximunDaysInAdvance(UPDATED_HAS_MAXIMUN_DAYS_IN_ADVANCE)
            .maximunDaysInAdvance(UPDATED_MAXIMUN_DAYS_IN_ADVANCE)
            .hasDefinePeopleQuantity(UPDATED_HAS_DEFINE_PEOPLE_QUANTITY)
            .quantityGuestLimit(UPDATED_QUANTITY_GUEST_LIMIT)
            .timesPerDay(UPDATED_TIMES_PER_DAY);
        CommonAreaDTO commonAreaDTO = commonAreaMapper.toDto(updatedCommonArea);

        restCommonAreaMockMvc.perform(put("/api/common-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaDTO)))
            .andExpect(status().isOk());

        // Validate the CommonArea in the database
        List<CommonArea> commonAreaList = commonAreaRepository.findAll();
        assertThat(commonAreaList).hasSize(databaseSizeBeforeUpdate);
        CommonArea testCommonArea = commonAreaList.get(commonAreaList.size() - 1);
        assertThat(testCommonArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommonArea.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonArea.getReservationCharge()).isEqualTo(UPDATED_RESERVATION_CHARGE);
        assertThat(testCommonArea.getDevolutionAmmount()).isEqualTo(UPDATED_DEVOLUTION_AMMOUNT);
        assertThat(testCommonArea.getChargeRequired()).isEqualTo(UPDATED_CHARGE_REQUIRED);
        assertThat(testCommonArea.getReservationWithDebt()).isEqualTo(UPDATED_RESERVATION_WITH_DEBT);
        assertThat(testCommonArea.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCommonArea.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testCommonArea.getMaximunHours()).isEqualTo(UPDATED_MAXIMUN_HOURS);
        assertThat(testCommonArea.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testCommonArea.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testCommonArea.getHasBlocks()).isEqualTo(UPDATED_HAS_BLOCKS);
        assertThat(testCommonArea.getHasDaysBeforeToReserve()).isEqualTo(UPDATED_HAS_DAYS_BEFORE_TO_RESERVE);
        assertThat(testCommonArea.getDaysBeforeToReserve()).isEqualTo(UPDATED_DAYS_BEFORE_TO_RESERVE);
        assertThat(testCommonArea.getHasDaysToReserveIfFree()).isEqualTo(UPDATED_HAS_DAYS_TO_RESERVE_IF_FREE);
        assertThat(testCommonArea.getDaysToReserveIfFree()).isEqualTo(UPDATED_DAYS_TO_RESERVE_IF_FREE);
        assertThat(testCommonArea.getHasDistanceBetweenReservations()).isEqualTo(UPDATED_HAS_DISTANCE_BETWEEN_RESERVATIONS);
        assertThat(testCommonArea.getDistanceBetweenReservations()).isEqualTo(UPDATED_DISTANCE_BETWEEN_RESERVATIONS);
        assertThat(testCommonArea.getNeedsApproval()).isEqualTo(UPDATED_NEEDS_APPROVAL);
        assertThat(testCommonArea.getHasValidityTime()).isEqualTo(UPDATED_HAS_VALIDITY_TIME);
        assertThat(testCommonArea.getValidityTimeHours()).isEqualTo(UPDATED_VALIDITY_TIME_HOURS);
        assertThat(testCommonArea.getHasReservationsLimit()).isEqualTo(UPDATED_HAS_RESERVATIONS_LIMIT);
        assertThat(testCommonArea.getPeriodBegin()).isEqualTo(UPDATED_PERIOD_BEGIN);
        assertThat(testCommonArea.getPeriodTimes()).isEqualTo(UPDATED_PERIOD_TIMES);
        assertThat(testCommonArea.getPeriodMonthEnd()).isEqualTo(UPDATED_PERIOD_MONTH_END);
        assertThat(testCommonArea.getLimitPeoplePerReservation()).isEqualTo(UPDATED_LIMIT_PEOPLE_PER_RESERVATION);
        assertThat(testCommonArea.getLimitActiveReservations()).isEqualTo(UPDATED_LIMIT_ACTIVE_RESERVATIONS);
        assertThat(testCommonArea.isHasMaximunDaysInAdvance()).isEqualTo(UPDATED_HAS_MAXIMUN_DAYS_IN_ADVANCE);
        assertThat(testCommonArea.getMaximunDaysInAdvance()).isEqualTo(UPDATED_MAXIMUN_DAYS_IN_ADVANCE);
        assertThat(testCommonArea.isHasDefinePeopleQuantity()).isEqualTo(UPDATED_HAS_DEFINE_PEOPLE_QUANTITY);
        assertThat(testCommonArea.getQuantityGuestLimit()).isEqualTo(UPDATED_QUANTITY_GUEST_LIMIT);
        assertThat(testCommonArea.getTimesPerDay()).isEqualTo(UPDATED_TIMES_PER_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingCommonArea() throws Exception {
        int databaseSizeBeforeUpdate = commonAreaRepository.findAll().size();

        // Create the CommonArea
        CommonAreaDTO commonAreaDTO = commonAreaMapper.toDto(commonArea);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommonAreaMockMvc.perform(put("/api/common-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commonAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the CommonArea in the database
        List<CommonArea> commonAreaList = commonAreaRepository.findAll();
        assertThat(commonAreaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommonArea() throws Exception {
        // Initialize the database
        commonAreaRepository.saveAndFlush(commonArea);
        int databaseSizeBeforeDelete = commonAreaRepository.findAll().size();

        // Get the commonArea
        restCommonAreaMockMvc.perform(delete("/api/common-areas/{id}", commonArea.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CommonArea> commonAreaList = commonAreaRepository.findAll();
        assertThat(commonAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonArea.class);
        CommonArea commonArea1 = new CommonArea();
        commonArea1.setId(1L);
        CommonArea commonArea2 = new CommonArea();
        commonArea2.setId(commonArea1.getId());
        assertThat(commonArea1).isEqualTo(commonArea2);
        commonArea2.setId(2L);
        assertThat(commonArea1).isNotEqualTo(commonArea2);
        commonArea1.setId(null);
        assertThat(commonArea1).isNotEqualTo(commonArea2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonAreaDTO.class);
        CommonAreaDTO commonAreaDTO1 = new CommonAreaDTO();
        commonAreaDTO1.setId(1L);
        CommonAreaDTO commonAreaDTO2 = new CommonAreaDTO();
        assertThat(commonAreaDTO1).isNotEqualTo(commonAreaDTO2);
        commonAreaDTO2.setId(commonAreaDTO1.getId());
        assertThat(commonAreaDTO1).isEqualTo(commonAreaDTO2);
        commonAreaDTO2.setId(2L);
        assertThat(commonAreaDTO1).isNotEqualTo(commonAreaDTO2);
        commonAreaDTO1.setId(null);
        assertThat(commonAreaDTO1).isNotEqualTo(commonAreaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commonAreaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commonAreaMapper.fromId(null)).isNull();
    }
}
