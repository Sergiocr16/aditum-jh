package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.repository.TransferenciaRepository;
import com.lighthouse.aditum.service.TransferenciaService;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.lighthouse.aditum.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransferenciaResource REST controller.
 *
 * @see TransferenciaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class TransferenciaResourceIntTest {

    private static final String DEFAULT_CONCEPTO = "AAAAAAAAAA";
    private static final String UPDATED_CONCEPTO = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_ORIGEN = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_ORIGEN = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_DESTINO = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_DESTINO = "BBBBBBBBBB";

    private static final String DEFAULT_MONTO = "AAAAAAAAAA";
    private static final String UPDATED_MONTO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_COMPANY = 1;
    private static final Integer UPDATED_ID_COMPANY = 2;

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_ID_BANCO_ORIGEN = 1;
    private static final Integer UPDATED_ID_BANCO_ORIGEN = 2;

    private static final Integer DEFAULT_ID_BANCO_DESTINO = 1;
    private static final Integer UPDATED_ID_BANCO_DESTINO = 2;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransferenciaMockMvc;

    private Transferencia transferencia;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferenciaResource transferenciaResource = new TransferenciaResource(transferenciaService);
        this.restTransferenciaMockMvc = MockMvcBuilders.standaloneSetup(transferenciaResource)
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
    public static Transferencia createEntity(EntityManager em) {
        Transferencia transferencia = new Transferencia()
            .concepto(DEFAULT_CONCEPTO)
            .cuentaOrigen(DEFAULT_CUENTA_ORIGEN)
            .cuentaDestino(DEFAULT_CUENTA_DESTINO)
            .monto(DEFAULT_MONTO)
            .idCompany(DEFAULT_ID_COMPANY)
            .fecha(DEFAULT_FECHA)
            .idBancoOrigen(DEFAULT_ID_BANCO_ORIGEN)
            .idBancoDestino(DEFAULT_ID_BANCO_DESTINO);
        return transferencia;
    }

    @Before
    public void initTest() {
        transferencia = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferencia() throws Exception {
        int databaseSizeBeforeCreate = transferenciaRepository.findAll().size();

        // Create the Transferencia
        restTransferenciaMockMvc.perform(post("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isCreated());

        // Validate the Transferencia in the database
        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Transferencia testTransferencia = transferenciaList.get(transferenciaList.size() - 1);
        assertThat(testTransferencia.getConcepto()).isEqualTo(DEFAULT_CONCEPTO);
        assertThat(testTransferencia.getCuentaOrigen()).isEqualTo(DEFAULT_CUENTA_ORIGEN);
        assertThat(testTransferencia.getCuentaDestino()).isEqualTo(DEFAULT_CUENTA_DESTINO);
        assertThat(testTransferencia.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testTransferencia.getIdCompany()).isEqualTo(DEFAULT_ID_COMPANY);
        assertThat(testTransferencia.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTransferencia.getIdBancoOrigen()).isEqualTo(DEFAULT_ID_BANCO_ORIGEN);
        assertThat(testTransferencia.getIdBancoDestino()).isEqualTo(DEFAULT_ID_BANCO_DESTINO);
    }

    @Test
    @Transactional
    public void createTransferenciaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transferenciaRepository.findAll().size();

        // Create the Transferencia with an existing ID
        transferencia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferenciaMockMvc.perform(post("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkConceptoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaRepository.findAll().size();
        // set the field null
        transferencia.setConcepto(null);

        // Create the Transferencia, which fails.

        restTransferenciaMockMvc.perform(post("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isBadRequest());

        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCuentaOrigenIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaRepository.findAll().size();
        // set the field null
        transferencia.setCuentaOrigen(null);

        // Create the Transferencia, which fails.

        restTransferenciaMockMvc.perform(post("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isBadRequest());

        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCuentaDestinoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaRepository.findAll().size();
        // set the field null
        transferencia.setCuentaDestino(null);

        // Create the Transferencia, which fails.

        restTransferenciaMockMvc.perform(post("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isBadRequest());

        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferenciaRepository.findAll().size();
        // set the field null
        transferencia.setMonto(null);

        // Create the Transferencia, which fails.

        restTransferenciaMockMvc.perform(post("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isBadRequest());

        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransferencias() throws Exception {
        // Initialize the database
        transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList
        restTransferenciaMockMvc.perform(get("/api/transferencias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].concepto").value(hasItem(DEFAULT_CONCEPTO.toString())))
            .andExpect(jsonPath("$.[*].cuentaOrigen").value(hasItem(DEFAULT_CUENTA_ORIGEN.toString())))
            .andExpect(jsonPath("$.[*].cuentaDestino").value(hasItem(DEFAULT_CUENTA_DESTINO.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.toString())))
            .andExpect(jsonPath("$.[*].idCompany").value(hasItem(DEFAULT_ID_COMPANY)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(sameInstant(DEFAULT_FECHA))))
            .andExpect(jsonPath("$.[*].idBancoOrigen").value(hasItem(DEFAULT_ID_BANCO_ORIGEN)))
            .andExpect(jsonPath("$.[*].idBancoDestino").value(hasItem(DEFAULT_ID_BANCO_DESTINO)));
    }

    @Test
    @Transactional
    public void getTransferencia() throws Exception {
        // Initialize the database
        transferenciaRepository.saveAndFlush(transferencia);

        // Get the transferencia
        restTransferenciaMockMvc.perform(get("/api/transferencias/{id}", transferencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferencia.getId().intValue()))
            .andExpect(jsonPath("$.concepto").value(DEFAULT_CONCEPTO.toString()))
            .andExpect(jsonPath("$.cuentaOrigen").value(DEFAULT_CUENTA_ORIGEN.toString()))
            .andExpect(jsonPath("$.cuentaDestino").value(DEFAULT_CUENTA_DESTINO.toString()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.toString()))
            .andExpect(jsonPath("$.idCompany").value(DEFAULT_ID_COMPANY))
            .andExpect(jsonPath("$.fecha").value(sameInstant(DEFAULT_FECHA)))
            .andExpect(jsonPath("$.idBancoOrigen").value(DEFAULT_ID_BANCO_ORIGEN))
            .andExpect(jsonPath("$.idBancoDestino").value(DEFAULT_ID_BANCO_DESTINO));
    }

    @Test
    @Transactional
    public void getNonExistingTransferencia() throws Exception {
        // Get the transferencia
        restTransferenciaMockMvc.perform(get("/api/transferencias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferencia() throws Exception {
        // Initialize the database
        transferenciaService.save(transferencia);

        int databaseSizeBeforeUpdate = transferenciaRepository.findAll().size();

        // Update the transferencia
        Transferencia updatedTransferencia = transferenciaRepository.findOne(transferencia.getId());
        updatedTransferencia
            .concepto(UPDATED_CONCEPTO)
            .cuentaOrigen(UPDATED_CUENTA_ORIGEN)
            .cuentaDestino(UPDATED_CUENTA_DESTINO)
            .monto(UPDATED_MONTO)
            .idCompany(UPDATED_ID_COMPANY)
            .fecha(UPDATED_FECHA)
            .idBancoOrigen(UPDATED_ID_BANCO_ORIGEN)
            .idBancoDestino(UPDATED_ID_BANCO_DESTINO);

        restTransferenciaMockMvc.perform(put("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransferencia)))
            .andExpect(status().isOk());

        // Validate the Transferencia in the database
        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeUpdate);
        Transferencia testTransferencia = transferenciaList.get(transferenciaList.size() - 1);
        assertThat(testTransferencia.getConcepto()).isEqualTo(UPDATED_CONCEPTO);
        assertThat(testTransferencia.getCuentaOrigen()).isEqualTo(UPDATED_CUENTA_ORIGEN);
        assertThat(testTransferencia.getCuentaDestino()).isEqualTo(UPDATED_CUENTA_DESTINO);
        assertThat(testTransferencia.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testTransferencia.getIdCompany()).isEqualTo(UPDATED_ID_COMPANY);
        assertThat(testTransferencia.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTransferencia.getIdBancoOrigen()).isEqualTo(UPDATED_ID_BANCO_ORIGEN);
        assertThat(testTransferencia.getIdBancoDestino()).isEqualTo(UPDATED_ID_BANCO_DESTINO);
    }

    @Test
    @Transactional
    public void updateNonExistingTransferencia() throws Exception {
        int databaseSizeBeforeUpdate = transferenciaRepository.findAll().size();

        // Create the Transferencia

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransferenciaMockMvc.perform(put("/api/transferencias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferencia)))
            .andExpect(status().isCreated());

        // Validate the Transferencia in the database
        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransferencia() throws Exception {
        // Initialize the database
        transferenciaService.save(transferencia);

        int databaseSizeBeforeDelete = transferenciaRepository.findAll().size();

        // Get the transferencia
        restTransferenciaMockMvc.perform(delete("/api/transferencias/{id}", transferencia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transferencia> transferenciaList = transferenciaRepository.findAll();
        assertThat(transferenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transferencia.class);
        Transferencia transferencia1 = new Transferencia();
        transferencia1.setId(1L);
        Transferencia transferencia2 = new Transferencia();
        transferencia2.setId(transferencia1.getId());
        assertThat(transferencia1).isEqualTo(transferencia2);
        transferencia2.setId(2L);
        assertThat(transferencia1).isNotEqualTo(transferencia2);
        transferencia1.setId(null);
        assertThat(transferencia1).isNotEqualTo(transferencia2);
    }
}
