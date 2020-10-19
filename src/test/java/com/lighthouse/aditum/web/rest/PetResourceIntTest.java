package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Pet;
import com.lighthouse.aditum.repository.PetRepository;
import com.lighthouse.aditum.service.PetService;
import com.lighthouse.aditum.service.dto.PetDTO;
import com.lighthouse.aditum.service.mapper.PetMapper;
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
 * Test class for the PetResource REST controller.
 *
 * @see PetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class PetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELETED = 1;
    private static final Integer UPDATED_DELETED = 2;

    private static final String DEFAULT_RAZE = "AAAAAAAAAA";
    private static final String UPDATED_RAZE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private PetService petService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPetMockMvc;

    private Pet pet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PetResource petResource = new PetResource(petService);
        this.restPetMockMvc = MockMvcBuilders.standaloneSetup(petResource)
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
    public static Pet createEntity(EntityManager em) {
        Pet pet = new Pet()
            .name(DEFAULT_NAME)
            .deleted(DEFAULT_DELETED)
            .raze(DEFAULT_RAZE)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .contact(DEFAULT_CONTACT)
            .imageUrl(DEFAULT_IMAGE_URL);
        return pet;
    }

    @Before
    public void initTest() {
        pet = createEntity(em);
    }

    @Test
    @Transactional
    public void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);
        restPetMockMvc.perform(post("/api/pets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testPet.getRaze()).isEqualTo(DEFAULT_RAZE);
        assertThat(testPet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPet.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testPet.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    public void createPetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // Create the Pet with an existing ID
        pet.setId(1L);
        PetDTO petDTO = petMapper.toDto(pet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetMockMvc.perform(post("/api/pets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPets() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList
        restPetMockMvc.perform(get("/api/pets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].raze").value(hasItem(DEFAULT_RAZE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())));
    }

    @Test
    @Transactional
    public void getPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get the pet
        restPetMockMvc.perform(get("/api/pets/{id}", pet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.raze").value(DEFAULT_RAZE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get("/api/pets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);
        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = petRepository.findOne(pet.getId());
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);
        updatedPet
            .name(UPDATED_NAME)
            .deleted(UPDATED_DELETED)
            .raze(UPDATED_RAZE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .contact(UPDATED_CONTACT)
            .imageUrl(UPDATED_IMAGE_URL);
        PetDTO petDTO = petMapper.toDto(updatedPet);

        restPetMockMvc.perform(put("/api/pets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testPet.getRaze()).isEqualTo(UPDATED_RAZE);
        assertThat(testPet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPet.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testPet.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Create the Pet
        PetDTO petDTO = petMapper.toDto(pet);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPetMockMvc.perform(put("/api/pets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(petDTO)))
            .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);
        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Get the pet
        restPetMockMvc.perform(delete("/api/pets/{id}", pet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pet.class);
        Pet pet1 = new Pet();
        pet1.setId(1L);
        Pet pet2 = new Pet();
        pet2.setId(pet1.getId());
        assertThat(pet1).isEqualTo(pet2);
        pet2.setId(2L);
        assertThat(pet1).isNotEqualTo(pet2);
        pet1.setId(null);
        assertThat(pet1).isNotEqualTo(pet2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetDTO.class);
        PetDTO petDTO1 = new PetDTO();
        petDTO1.setId(1L);
        PetDTO petDTO2 = new PetDTO();
        assertThat(petDTO1).isNotEqualTo(petDTO2);
        petDTO2.setId(petDTO1.getId());
        assertThat(petDTO1).isEqualTo(petDTO2);
        petDTO2.setId(2L);
        assertThat(petDTO1).isNotEqualTo(petDTO2);
        petDTO1.setId(null);
        assertThat(petDTO1).isNotEqualTo(petDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(petMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(petMapper.fromId(null)).isNull();
    }
}
