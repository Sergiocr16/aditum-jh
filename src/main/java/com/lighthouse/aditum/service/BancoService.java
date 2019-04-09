package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.repository.BancoRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.BancoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing Banco.
 */
@Service
@Transactional
public class BancoService {

    private final Logger log = LoggerFactory.getLogger(BancoService.class);

    private final BancoRepository bancoRepository;

    private final BancoMapper bancoMapper;

    private final BalanceByAccountService balanceByAccountService;

    private final EgressService egressService;

    private final TransferenciaService transferenciaService;

    private final PaymentService paymentService;

    private final CommonAreaReservationsService commonAreaReservationsService;

    private final HouseService houseService;

    private final BitacoraAccionesService bitacoraAccionesService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;

    public BancoService(UserService userService, AdminInfoService adminInfoService, BitacoraAccionesService bitacoraAccionesService, BancoRepository bancoRepository, BancoMapper bancoMapper, BalanceByAccountService balanceByAccountService, @Lazy EgressService egressService, TransferenciaService transferenciaService, @Lazy PaymentService paymentService, @Lazy CommonAreaReservationsService commonAreaReservationsService, @Lazy HouseService houseService) {
        this.bancoRepository = bancoRepository;
        this.bancoMapper = bancoMapper;
        this.balanceByAccountService = balanceByAccountService;
        this.egressService = egressService;
        this.transferenciaService = transferenciaService;
        this.paymentService = paymentService;
        this.commonAreaReservationsService = commonAreaReservationsService;
        this.houseService = houseService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
    }

    /**
     * Save a banco.
     *
     * @param bancoDTO the entity to save
     * @return the persisted entity
     */

    public BancoDTO save(BancoDTO bancoDTO) {
        log.debug("Request to save Banco : {}", bancoDTO);
        Banco banco = bancoMapper.toEntity(bancoDTO);
        banco.setCompany(bancoMapper.companyFromId(bancoDTO.getCompanyId()));
        banco = bancoRepository.save(banco);

        if (bancoDTO.getId() != null && bancoDTO.getDeleted() != 1 || bancoDTO.getId() == null) {
            LocalDateTime today = LocalDateTime.now();
            ZoneId id = ZoneId.of("America/Costa_Rica");  //Create timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.of(today, id);
            BitacoraAccionesDTO bitacoraAccionesDTO = new BitacoraAccionesDTO();

            if (bancoDTO.getId() == null) {
                bitacoraAccionesDTO.setConcept("Registro de nuevo banco: " + bancoDTO.getBeneficiario());
            } else if (bancoDTO.getId() != null && bancoDTO.getDeleted() == 0) {
                bitacoraAccionesDTO.setConcept("Eliminación del banco: " + bancoDTO.getBeneficiario());
            }

            bitacoraAccionesDTO.setType(2);
            bitacoraAccionesDTO.setEjecutionDate(zonedDateTime);
            bitacoraAccionesDTO.setCategory("Bancos");

            bitacoraAccionesDTO.setIdReference(banco.getId());
            bitacoraAccionesDTO.setIdResponsable(adminInfoService.findOneByUserId(userService.getUserWithAuthorities().getId()).getId());
            bitacoraAccionesDTO.setCompanyId(banco.getCompany().getId());
            bitacoraAccionesService.save(bitacoraAccionesDTO);
        }
        return bancoMapper.toDto(banco);
    }

    /**
     * Get all the bancos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BancoDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Bancos");
        Page<Banco> result = bancoRepository.findByCompanyIdAndDeleted(pageable, companyId, 1);
        return result.map(banco -> bancoMapper.toDto(banco));
    }

    @Transactional(readOnly = true)
    public BancoDTO getInicialBalance(ZonedDateTime firstMonthDay,BancoDTO bancoDTO,ZonedDateTime initialTime){
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        double saldo;
        List<BalanceByAccountDTO> balances = balanceByAccountService.findByDatesBetweenAndAccount(firstMonthDay, firstMonthDay, bancoDTO.getId());
        String f = "a";
        if (balances.size() > 0) {
            bancoDTO.setCapitalInicialTemporal(balances.get(0).getBalance());
            saldo = balances.get(0).getBalance();
        } else {
            saldo = Double.parseDouble(bancoDTO.getCapitalInicial());
        }
        String a = "a";
        List<BancoMovementDTO> bancoMovements = bancoMovements(firstMonthDay, initialTime, bancoDTO.getId(), bancoDTO.getCompanyId());
        bancoDTO = calculateBalance(saldo, bancoMovements, bancoDTO);
        bancoDTO.setCapitalInicialFormatted(currencyFormatter.format(bancoDTO.getTotalBalance()).substring(1));
        bancoDTO.setCapitalInicialTemporal(bancoDTO.getTotalBalance());
        return bancoDTO;
    }

    private BancoDTO calculateBalance(double saldo, List<BancoMovementDTO> bancoMovements, BancoDTO bancoDTO) {
        double totalEgress = 0;
        double totalIngress = 0;
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "ES"));
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        for (int i = 0; i < bancoMovements.size(); i++) {

            bancoMovements.get(i).setDateFormatted(spanish.format(bancoMovements.get(i).getDate()));
            bancoMovements.get(i).setEgressFormatted(currencyFormatter.format(bancoMovements.get(i).getEgress()).substring(1));
            bancoMovements.get(i).setIngressFormatted(currencyFormatter.format(bancoMovements.get(i).getIngress()).substring(1));
            if (bancoMovements.get(i).getType() == 1 || bancoMovements.get(i).getType() == 2) {
                saldo = saldo - bancoMovements.get(i).getEgress();
                totalEgress = totalEgress + bancoMovements.get(i).getEgress();
            } else {
                saldo = saldo + bancoMovements.get(i).getIngress();

                totalIngress = totalIngress + bancoMovements.get(i).getIngress();
            }
            bancoMovements.get(i).setBalance(saldo);

        }

        bancoDTO.setTotalBalance(saldo);
        bancoDTO.setTotalEgress(totalEgress);
        bancoDTO.setTotalIngress(totalIngress);
        bancoDTO.setTotalEgressFormatted(currencyFormatter.format(bancoDTO.getTotalEgress()).substring(1));
        bancoDTO.setTotalIngressFormatted(currencyFormatter.format(bancoDTO.getTotalIngress()).substring(1));
        return bancoDTO;
    }

    @Transactional(readOnly = true)
    public BancoDTO getAccountStatus(ZonedDateTime firstMonthDay,ZonedDateTime final_capital_date,ZonedDateTime initialTime, ZonedDateTime finalTime, Long accountId) {
        BancoDTO bancoDTO = this.findOne(accountId);
        bancoDTO = getInicialBalance(firstMonthDay,bancoDTO,final_capital_date);
        List<BancoMovementDTO> bancoMovements = bancoMovements(initialTime,finalTime,bancoDTO.getId(),bancoDTO.getCompanyId());
        bancoDTO.setMovimientos(bancoMovements);
        bancoDTO = calculateBalance(bancoDTO.getCapitalInicialTemporal(), bancoDTO.getMovimientos(), bancoDTO);
        bancoDTO.setTotalBalance(bancoDTO.getTotalBalance());

        if (bancoDTO.getTotalBalance() > 0) {
            bancoDTO.setBalanceColor("green");
        } else if (bancoDTO.getTotalBalance() < 0) {
            bancoDTO.setBalanceColor("red");
        }
        return bancoDTO;

    }

    private List<BancoMovementDTO> bancoMovements(ZonedDateTime initialTime, ZonedDateTime finalTime,Long accountId,Long companyId){
        List<BancoMovementDTO> movements = new ArrayList<>();

        Page<EgressDTO> egresos = egressService.findByDatesBetweenAndCompanyAndAccount(null, initialTime, finalTime, companyId, accountId + "");
        for (int i = 0; i < egresos.getContent().size(); i++) {
            if (egresos.getContent().get(i).getState() == 2 || egresos.getContent().get(i).getState() == 5) {
                if (egresos.getContent().get(i).getState() == 5) {
                    CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsService.findByEgressId(egresos.getContent().get(i).getId());
                    HouseDTO houseDTO = houseService.findOne(commonAreaReservationsDTO.getHouseId());
                    egresos.getContent().get(i).setConcept(egresos.getContent().get(i).getConcept() + " - Filial " + houseDTO.getHousenumber());
                }
                BancoMovementDTO bancoMovementDTO = new BancoMovementDTO(egresos.getContent().get(i).getFolio(), egresos.getContent().get(i).getConcept(), egresos.getContent().get(i).getPaymentDate(), 1, 0, Double.parseDouble(egresos.getContent().get(i).getTotal()));
                movements.add(bancoMovementDTO);
            }
        }
        Page<Transferencia> transferenciasSalientes = transferenciaService.getBetweenDatesByOutgoingTransfer(null, initialTime, finalTime, Integer.parseInt(accountId + ""));
        for (int i = 0; i < transferenciasSalientes.getContent().size(); i++) {

            BancoMovementDTO bancoMovementDTO = new BancoMovementDTO(null, transferenciasSalientes.getContent().get(i).getConcepto(), transferenciasSalientes.getContent().get(i).getFecha(), 2, 0, Double.parseDouble(transferenciasSalientes.getContent().get(i).getMonto()), transferenciasSalientes.getContent().get(i).getCuentaDestino());
            movements.add(bancoMovementDTO);

        }
        Page<Transferencia> transferenciasEntrantes = transferenciaService.getBetweenDatesByInComingTransfer(null, initialTime, finalTime, Integer.parseInt(accountId + ""));
        for (int i = 0; i < transferenciasEntrantes.getContent().size(); i++) {

            BancoMovementDTO bancoMovementDTO = new BancoMovementDTO(null, transferenciasEntrantes.getContent().get(i).getConcepto(), transferenciasEntrantes.getContent().get(i).getFecha(), 3, Double.parseDouble(transferenciasEntrantes.getContent().get(i).getMonto()), 0, transferenciasEntrantes.getContent().get(i).getCuentaOrigen());
            movements.add(bancoMovementDTO);

        }
        Page<PaymentDTO> ingresos = paymentService.findByDatesBetweenAndCompanyAndAccount(null, initialTime, finalTime, Integer.parseInt(companyId + ""), accountId + "");
        for (int i = 0; i < ingresos.getContent().size(); i++) {

            BancoMovementDTO bancoMovementDTO = new BancoMovementDTO(ingresos.getContent().get(i).getReceiptNumber(), ingresos.getContent().get(i).getConcept(), ingresos.getContent().get(i).getDate(), 4, Double.parseDouble(ingresos.getContent().get(i).getAmmount()), 0);
            movements.add(bancoMovementDTO);

        }
        Collections.sort(movements, Comparator.comparing(BancoMovementDTO::getDate));
        return movements;
    }


    @Transactional(readOnly = true)
    public List<Banco> findAllCompanies(Pageable pageable) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findAll(pageable).getContent();

    }

    @Transactional(readOnly = true)
    public List<BancoDTO> findAll(Long companyId) {
        log.debug("Request to get all Bancos");
        return bancoRepository.findByCompanyIdAndDeleted(companyId, 1).stream()
            .map(bancoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));

    }

    /**
     * Get one banco by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BancoDTO findOne(Long id) {
        log.debug("Request to get Banco : {}", id);
        Banco banco = bancoRepository.findOne(id);
        return bancoMapper.toDto(banco);
    }

    /**
     * Delete the  banco by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Banco : {}", id);
        bancoRepository.delete(id);
    }

    public void increaseSaldo(Long id, String saldoToIncrease) {
        BancoDTO banco = this.findOne(id);
        double newSaldo = Double.parseDouble(banco.getSaldo()) + Double.parseDouble(saldoToIncrease);
        banco.setSaldo(newSaldo + "");
        this.save(banco);
    }
}
