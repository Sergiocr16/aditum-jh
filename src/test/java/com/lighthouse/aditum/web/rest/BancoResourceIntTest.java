package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.AditumApp;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.repository.BancoRepository;
import com.lighthouse.aditum.service.BancoService;
import com.lighthouse.aditum.service.dto.BancoDTO;
import com.lighthouse.aditum.service.mapper.BancoMapper;
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
 * Test class for the BancoResource REST controller.
 *
 * @see BancoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AditumApp.class)
public class BancoResourceIntTest {

    private static final String DEFAULT_BENEFICIARIO = "AAAAAAAAAA";
    private static final String UPDATED_BENEFICIARIO = "BBBBBBBBBB";

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_CORRIENTE = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_CORRIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_CLIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_MONEDA = "AAAAAAAAAA";
    private static final String UPDATED_MONEDA = "BBBBBBBBBB";

    private static final String DEFAULT_CUENTA_CONTABLE = "AAAAAAAAAA";
    private static final String UPDATED_CUENTA_CONTABLE = "BBBBBBBBBB";

    private static final String DEFAULT_CAPITAL_INICIAL = "AAAAAAAAAA";
    private static final String UPDATED_CAPITAL_INICIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_MOSTRAR_FACTURA = 1;
    private static final Integer UPDATED_MOSTRAR_FACTURA = 2;

    private static final ZonedDateTime DEFAULT_FECHA_CAPITAL_INICIAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_CAPITAL_INICIAL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SALDO = "AAAAAAAAAA";
    private static final String UPDATED_SALDO = "BBBBBBBBBB";

    @Autowired
    private BancoRepository bancoRepository;

    @Autowired
    private BancoMapper bancoMapper;

    @Autowired
    private BancoService bancoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBancoMockMvc;

    private Banco banco;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BancoResource bancoResource = new BancoResource(bancoService);
        this.restBancoMockMvc = MockMvcBuilders.standaloneSetup(bancoResource)
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
    public static Banco createEntity(EntityManager em) {
        Banco banco = new Banco()
            .beneficiario(DEFAULT_BENEFICIARIO)
            .cedula(DEFAULT_CEDULA)
            .cuentaCorriente(DEFAULT_CUENTA_CORRIENTE)
            .cuentaCliente(DEFAULT_CUENTA_CLIENTE)
            .moneda(DEFAULT_MONEDA)
            .cuentaContable(DEFAULT_CUENTA_CONTABLE)
            .capitalInicial(DEFAULT_CAPITAL_INICIAL)
            .mostrarFactura(DEFAULT_MOSTRAR_FACTURA)
            .fechaCapitalInicial(DEFAULT_FECHA_CAPITAL_INICIAL)
            .saldo(DEFAULT_SALDO);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        banco.setCompany(company);
        return banco;
    }

    @Before
    public void initTest() {
        banco = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanco() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);
        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isCreated());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate + 1);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getBeneficiario()).isEqualTo(DEFAULT_BENEFICIARIO);
        assertThat(testBanco.getCedula()).isEqualTo(DEFAULT_CEDULA);
        assertThat(testBanco.getCuentaCorriente()).isEqualTo(DEFAULT_CUENTA_CORRIENTE);
        assertThat(testBanco.getCuentaCliente()).isEqualTo(DEFAULT_CUENTA_CLIENTE);
        assertThat(testBanco.getMoneda()).isEqualTo(DEFAULT_MONEDA);
        assertThat(testBanco.getCuentaContable()).isEqualTo(DEFAULT_CUENTA_CONTABLE);
        assertThat(testBanco.getCapitalInicial()).isEqualTo(DEFAULT_CAPITAL_INICIAL);
        assertThat(testBanco.getMostrarFactura()).isEqualTo(DEFAULT_MOSTRAR_FACTURA);
        assertThat(testBanco.getFechaCapitalInicial()).isEqualTo(DEFAULT_FECHA_CAPITAL_INICIAL);
        assertThat(testBanco.getSaldo()).isEqualTo(DEFAULT_SALDO);
    }

    @Test
    @Transactional
    public void createBancoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bancoRepository.findAll().size();

        // Create the Banco with an existing ID
        banco.setId(1L);
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCuentaCorrienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancoRepository.findAll().size();
        // set the field null
        banco.setCuentaCorriente(null);

        // Create the Banco, which fails.
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCuentaClienteIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancoRepository.findAll().size();
        // set the field null
        banco.setCuentaCliente(null);

        // Create the Banco, which fails.
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        restBancoMockMvc.perform(post("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isBadRequest());

        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBancos() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get all the bancoList
        restBancoMockMvc.perform(get("/api/bancos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banco.getId().intValue())))
            .andExpect(jsonPath("$.[*].beneficiario").value(hasItem(DEFAULT_BENEFICIARIO.toString())))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA.toString())))
            .andExpect(jsonPath("$.[*].cuentaCorriente").value(hasItem(DEFAULT_CUENTA_CORRIENTE.toString())))
            .andExpect(jsonPath("$.[*].cuentaCliente").value(hasItem(DEFAULT_CUENTA_CLIENTE.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].cuentaContable").value(hasItem(DEFAULT_CUENTA_CONTABLE.toString())))
            .andExpect(jsonPath("$.[*].capitalInicial").value(hasItem(DEFAULT_CAPITAL_INICIAL.toString())))
            .andExpect(jsonPath("$.[*].mostrarFactura").value(hasItem(DEFAULT_MOSTRAR_FACTURA)))
            .andExpect(jsonPath("$.[*].fechaCapitalInicial").value(hasItem(sameInstant(DEFAULT_FECHA_CAPITAL_INICIAL))))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.toString())));
    }

    @Test
    @Transactional
    public void getBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);

        // Get the banco
        restBancoMockMvc.perform(get("/api/bancos/{id}", banco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(banco.getId().intValue()))
            .andExpect(jsonPath("$.beneficiario").value(DEFAULT_BENEFICIARIO.toString()))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA.toString()))
            .andExpect(jsonPath("$.cuentaCorriente").value(DEFAULT_CUENTA_CORRIENTE.toString()))
            .andExpect(jsonPath("$.cuentaCliente").value(DEFAULT_CUENTA_CLIENTE.toString()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.cuentaContable").value(DEFAULT_CUENTA_CONTABLE.toString()))
            .andExpect(jsonPath("$.capitalInicial").value(DEFAULT_CAPITAL_INICIAL.toString()))
            .andExpect(jsonPath("$.mostrarFactura").value(DEFAULT_MOSTRAR_FACTURA))
            .andExpect(jsonPath("$.fechaCapitalInicial").value(sameInstant(DEFAULT_FECHA_CAPITAL_INICIAL)))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBanco() throws Exception {
        // Get the banco
        restBancoMockMvc.perform(get("/api/bancos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Update the banco
        Banco updatedBanco = bancoRepository.findOne(banco.getId());
        updatedBanco
            .beneficiario(UPDATED_BENEFICIARIO)
            .cedula(UPDATED_CEDULA)
            .cuentaCorriente(UPDATED_CUENTA_CORRIENTE)
            .cuentaCliente(UPDATED_CUENTA_CLIENTE)
            .moneda(UPDATED_MONEDA)
            .cuentaContable(UPDATED_CUENTA_CONTABLE)
            .capitalInicial(UPDATED_CAPITAL_INICIAL)
            .mostrarFactura(UPDATED_MOSTRAR_FACTURA)
            .fechaCapitalInicial(UPDATED_FECHA_CAPITAL_INICIAL)
            .saldo(UPDATED_SALDO);
        BancoDTO bancoDTO = bancoMapper.toDto(updatedBanco);

        restBancoMockMvc.perform(put("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isOk());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate);
        Banco testBanco = bancoList.get(bancoList.size() - 1);
        assertThat(testBanco.getBeneficiario()).isEqualTo(UPDATED_BENEFICIARIO);
        assertThat(testBanco.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testBanco.getCuentaCorriente()).isEqualTo(UPDATED_CUENTA_CORRIENTE);
        assertThat(testBanco.getCuentaCliente()).isEqualTo(UPDATED_CUENTA_CLIENTE);
        assertThat(testBanco.getMoneda()).isEqualTo(UPDATED_MONEDA);
        assertThat(testBanco.getCuentaContable()).isEqualTo(UPDATED_CUENTA_CONTABLE);
        assertThat(testBanco.getCapitalInicial()).isEqualTo(UPDATED_CAPITAL_INICIAL);
        assertThat(testBanco.getMostrarFactura()).isEqualTo(UPDATED_MOSTRAR_FACTURA);
        assertThat(testBanco.getFechaCapitalInicial()).isEqualTo(UPDATED_FECHA_CAPITAL_INICIAL);
        assertThat(testBanco.getSaldo()).isEqualTo(UPDATED_SALDO);
    }

    @Test
    @Transactional
    public void updateNonExistingBanco() throws Exception {
        int databaseSizeBeforeUpdate = bancoRepository.findAll().size();

        // Create the Banco
        BancoDTO bancoDTO = bancoMapper.toDto(banco);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBancoMockMvc.perform(put("/api/bancos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bancoDTO)))
            .andExpect(status().isCreated());

        // Validate the Banco in the database
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBanco() throws Exception {
        // Initialize the database
        bancoRepository.saveAndFlush(banco);
        int databaseSizeBeforeDelete = bancoRepository.findAll().size();

        // Get the banco
        restBancoMockMvc.perform(delete("/api/bancos/{id}", banco.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Banco> bancoList = bancoRepository.findAll();
        assertThat(bancoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banco.class);
        Banco banco1 = new Banco();
        banco1.setId(1L);
        Banco banco2 = new Banco();
        banco2.setId(banco1.getId());
        assertThat(banco1).isEqualTo(banco2);
        banco2.setId(2L);
        assertThat(banco1).isNotEqualTo(banco2);
        banco1.setId(null);
        assertThat(banco1).isNotEqualTo(banco2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BancoDTO.class);
        BancoDTO bancoDTO1 = new BancoDTO();
        bancoDTO1.setId(1L);
        BancoDTO bancoDTO2 = new BancoDTO();
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
        bancoDTO2.setId(bancoDTO1.getId());
        assertThat(bancoDTO1).isEqualTo(bancoDTO2);
        bancoDTO2.setId(2L);
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
        bancoDTO1.setId(null);
        assertThat(bancoDTO1).isNotEqualTo(bancoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bancoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bancoMapper.fromId(null)).isNull();
    }
}
