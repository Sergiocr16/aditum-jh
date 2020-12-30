package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.CustomChargeType;
import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.domain.PaymentProof;
import com.lighthouse.aditum.repository.PaymentRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;
import static com.lighthouse.aditum.service.util.RandomUtil.formatDateTime;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;
import static java.lang.Math.toIntExact;


/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final ChargeService chargeService;

    private final BancoService bancoService;

    private final PaymentDocumentService paymentEmailSenderService;

    private final ResidentService residentService;

    private final HouseService houseService;

    private final BalanceByAccountService balanceByAccountService;

    private Locale locale = new Locale("es", "CR");

    private final BitacoraAccionesService bitacoraAccionesService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;

    private final CompanyConfigurationService companyConfigurationService;

    private final PaymentProofService paymentProofService;

    private final PushNotificationService pNotification;

    private final CustomChargeTypeService customChargeTypeService;

    private final PaymentChargeService paymentChargeService;

    private final HistoricalDefaulterService historicalDefaulterService;

    private final WaterConsumptionService waterConsumptionService;


    public PaymentService(WaterConsumptionService waterConsumptionService, @Lazy HistoricalDefaulterService historicalDefaulterService, PaymentChargeService paymentChargeService, CustomChargeTypeService customChargeTypeService, PushNotificationService pNotification, PaymentProofService paymentProofService, CompanyConfigurationService companyConfigurationService, UserService userService, AdminInfoService adminInfoService, BitacoraAccionesService bitacoraAccionesService, BalanceByAccountService balanceByAccountService, @Lazy HouseService houseService, ResidentService residentService, PaymentDocumentService paymentEmailSenderService, PaymentRepository paymentRepository, PaymentMapper paymentMapper, ChargeService chargeService, BancoService bancoService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.chargeService = chargeService;
        this.bancoService = bancoService;
        this.paymentEmailSenderService = paymentEmailSenderService;
        this.residentService = residentService;
        this.houseService = houseService;
        this.balanceByAccountService = balanceByAccountService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
        this.companyConfigurationService = companyConfigurationService;
        this.paymentProofService = paymentProofService;
        this.pNotification = pNotification;
        this.customChargeTypeService = customChargeTypeService;
        this.paymentChargeService = paymentChargeService;
        this.historicalDefaulterService = historicalDefaulterService;
        this.waterConsumptionService = waterConsumptionService;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save
     * @return the persisted entity
     */

    public PaymentDTO update(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setHouse(paymentMapper.houseFromId(paymentDTO.getHouseId()));
        payment.setAccount(paymentDTO.getAccount().split(";")[1]);
        payment.setAmmountLeft(paymentDTO.getAmmountLeft());
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    public PaymentDTO save(CreatePaymentDTO paymentDTO) throws URISyntaxException {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(createPaymentDTOtoPaymentDTO(paymentDTO));
        if (payment.getTransaction().equals("2")) {
            payment.setAmmountLeft(paymentDTO.getAmmount());
        }
        payment.setHouse(paymentMapper.houseFromId(paymentDTO.getHouseId()));
        payment.setAccount(paymentDTO.getAccount().split(";")[1]);
        payment.setDate(formatDateTime(payment.getDate()));
        payment.setAmmountDollar(paymentDTO.getAmmountDollar());
        payment.setExchangeRate(paymentDTO.getExchangeRate());
        payment.setDoubleMoney(paymentDTO.getDoubleMoney());
        payment = paymentRepository.save(payment);
       if(!paymentDTO.getCancellingFavorBalance()){
           if (payment.getDoubleMoney() == 1) {
               bancoService.increaseSaldo(Long.valueOf(paymentDTO.getAccount().split(";")[1]).longValue(), paymentDTO.getAmmountDollar());
           } else {
               bancoService.increaseSaldo(Long.valueOf(paymentDTO.getAccount().split(";")[1]).longValue(), paymentDTO.getAmmount());
           }
       }
        List<PaymentChargeDTO> paymentCharges = new ArrayList<>();
        for (int i = 0; i < paymentDTO.getCharges().size(); i++) {
            if (Double.parseDouble(paymentDTO.getCharges().get(i).getPaymentAmmount()) > 0) {
                paymentCharges.add(this.payCharge(paymentDTO.getCharges().get(i), payment));
            }
        }
        PaymentDTO paymentDTo = paymentMapper.toDto(payment);
        paymentDTo.setCharges(paymentCharges);
        paymentDTo.setEmailTo(paymentDTO.getEmailTo());
        if (paymentDTo.getEmailTo().size() > 0) {
            this.paymentEmailSenderService.sendPaymentEmail(paymentDTo, false);
        }
        this.balanceByAccountService.modifyBalancesInPastPayment(payment);
        String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(paymentDTO.getCompanyId() + "")).getContent().get(0).getCurrency();
        String concepto = "";
        if (paymentDTO.getHouseId() != null) {
            concepto = "Captura de ingreso de la filial " + houseService.findOne(paymentDTO.getHouseId()).getHousenumber() + ", por " + formatMoney(currency, Double.parseDouble(paymentDTO.getAmmount()));
        } else {
            concepto = "Captura de ingreso en la categoría otros: " + paymentDTO.getConcept() + " por " + formatMoney(currency, Double.parseDouble(paymentDTO.getAmmount()));
        }
        if (!payment.getTransaction().equals("3")) {
            this.pNotification.sendNotificationsToOwnersByHouse(paymentDTO.getHouseId(), this.pNotification.createPushNotification(
                paymentDTO.getConcept(),
                "Se ha registrado un pago en su filial por un monto de " + currency + formatMoney(currency, Double.parseDouble(paymentDTO.getAmmount())) + " con el número de recibo " + paymentDTO.getReceiptNumber() + "."
            ));
        }
        this.pNotification.sendNotificationAllAdminsByCompanyId(paymentDTO.getCompanyId().longValue(), this.pNotification.createPushNotification(
            paymentDTO.getConcept(),
            "Se ha registrado un pago en la filial por un monto de " + currency + formatMoney(currency, Double.parseDouble(paymentDTO.getAmmount())) + " con el número de recibo " + paymentDTO.getReceiptNumber() + "."
        ));

        if (paymentDTO.getPaymentProofs() != null) {
            for (int i = 0; i < paymentDTO.getPaymentProofs().size(); i++) {
                PaymentProofDTO paymentProofDTO = paymentDTO.getPaymentProofs().get(i);
                paymentProofDTO.setPaymentId(payment.getId());
                paymentProofDTO.setStatus(2);
                this.paymentProofService.save(paymentProofDTO);
            }
        }

        this.historicalDefaulterService.formatHistoricalReportByHouse(paymentDTO.getHouseId(), paymentDTO.getDate(), currency, paymentDTO.getCompanyId());
        return paymentMapper.toDto(payment);
    }


    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByDatesBetweenAndCompany(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Payments in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        Page<Payment> payments = paymentRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
        String currency = companyConfigurationService.getByCompanyId(null, (long) companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) companyId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setHouseNumber(this.houseService.findOne(paymentDTO.getHouseId()).getHousenumber());
            paymentDTO.setCategories(this.findCategoriesInPayment(paymentDTO));
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public IncomeReportDTO findIncomeReportByCompanyAndDatesBetween(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId, String houseId, String paymentMethod, String category, String account) {
        log.debug("Request to get all Payments in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        List<Payment> payments = new ArrayList<>();
        if (houseId.equals("empty")) {
            payments = paymentRepository.findByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId);
        } else {
            payments = paymentRepository.findByDatesBetweenAndHouseId(null, zd_initialTime, zd_finalTime, Long.parseLong(houseId)).getContent();
        }

        List<PaymentDTO> paymentsDTO = new ArrayList<>();
        payments.forEach(payment -> {
            paymentsDTO.add(this.paymentMapper.toDto(payment));
        });
        String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(companyId + "")).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) companyId);

        for (int i = 0; i < paymentsDTO.size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.get(i);
            paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            if (Double.parseDouble(paymentDTO.getTransaction()) != 3) {
                paymentDTO.setHouseNumber(this.houseService.findOne(paymentDTO.getHouseId()).getHousenumber());
            }
            paymentDTO.setCategories(this.findCategoriesInPayment(paymentDTO));
        }
        IncomeReportDTO incomeReport = new IncomeReportDTO(this.houseService);
        incomeReport.setPayments(this.filterPaymentsForIncome(paymentsDTO, houseId, paymentMethod, category, account));
        double totalMaint = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 1);
        double totalExtra = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 2);
        double totalAreas = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 3);
        double totalMultas = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 5);
        double totalWaterCharge = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 6);
        double totalAdelanto = this.getAdelantos(zd_initialTime, zd_finalTime, companyId, houseId, currency, customChargeTypes);
        double totalOtherIngress = this.findTotalOtherIngressByDatesBetweenAndCompany(account, initialTime, finalTime, companyId);
        totalOtherIngress = totalOtherIngress + this.getTotalAmmoutPerTypeMoreeThanOfPayment(incomeReport, 7);
        incomeReport.setTotalMaintenance(totalMaint);
        incomeReport.setTotalMaintenanceFormatted(formatMoney(currency, incomeReport.getTotalMaintenance()));
        incomeReport.setTotalExtraordinary(totalExtra);
        incomeReport.setTotalExtraordinaryFormatted(formatMoney(currency, incomeReport.getTotalExtraordinary()));
        incomeReport.setTotalCommonArea(totalAreas);
        incomeReport.setTotalCommonAreaFormatted(formatMoney(currency, incomeReport.getTotalCommonArea()));
        incomeReport.setTotalOtherIngress(totalOtherIngress);
        incomeReport.setTotalOtherIngressFormatted(formatMoney(currency, incomeReport.getTotalOtherIngress()));
        incomeReport.setTotalMulta(totalMultas);
        incomeReport.setTotalMultaFormatted(formatMoney(currency, incomeReport.getTotalMulta()));
        incomeReport.setTotalWaterCharge(totalWaterCharge);
        incomeReport.setTotalWaterChargeFormatted(formatMoney(currency, incomeReport.getTotalWaterCharge()));
        incomeReport.setTotalAdelanto(totalAdelanto);
        incomeReport.setTotalAdelantoFormatted(formatMoney(currency, incomeReport.getTotalAdelanto()));
        incomeReport.setTotal((totalMaint + totalAreas + totalExtra + totalOtherIngress + totalMultas + totalWaterCharge + totalAdelanto));
        incomeReport.setTotalFormatted(formatMoney(currency, incomeReport.getTotal()));
        incomeReport.defineFilter(houseId, paymentMethod, category, account);
        return incomeReport;
    }


    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByDatesBetweenAndCompanyAndAccount(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId, String accountId) {
        log.debug("Request to get all Payments by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        Page<Payment> result = paymentRepository.findByDatesBetweenAndCompanyAndAccount(pageable, zd_initialTime, zd_finalTime, companyId, accountId);
//        Collections.reverse(result);
        return result.map(payment -> paymentMapper.toDto(payment));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findByDatesBetweenAndCompanyAndAccount(ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId, String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findByDatesBetweenAndCompanyAndAccount(zd_initialTime, zd_finalTime, companyId, accountId).stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosByDatesBetweenAndCompany(ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId, "2").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosByDatesBetweenAndHouseId(ZonedDateTime initialTime, ZonedDateTime finalTime, String houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosByDatesBetweenAndHouseId(zd_initialTime, zd_finalTime, Long.parseLong(houseId), "2").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findOtherIngressByDatesBetweenAndCompany(ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId, "3").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public double findTotalOtherIngressByDatesBetweenAndCompany(String account, ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        double total = 0;
        List<PaymentDTO> payments = paymentRepository.findAdelantosByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId, "3").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        for (int i = 0; i < payments.size(); i++) {
            String banco = this.bancoService.findOne(Long.parseLong(payments.get(i).getAccount())).getBeneficiario();
            if (account.equals("empty")) {
                total = total + Double.parseDouble(payments.get(i).getAmmount());
            } else if (banco.toUpperCase().equals(account.toUpperCase())) {
                total = total + Double.parseDouble(payments.get(i).getAmmount());
            }
        }
        return total;
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosByHouse(Long houseId) {
        return paymentRepository.findAdelantosByHouse(houseId, "2").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosByDatesBetweenAndHouseId(ZonedDateTime initialTime, ZonedDateTime finalTime, long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosByDatesBetweenAndHouseId(zd_initialTime, zd_finalTime, houseId, "2").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosUntilDatesAndHouseId(ZonedDateTime finalTime, long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosUntilDateBetweenAndHouseId(zd_finalTime, houseId).stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosUntilDatesAndCompanyId(ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosUntilDateBetweenAndCompanyId(zd_finalTime, companyId).stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the payments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouseFilteredByDate(Pageable pageable, Long houseId, ZonedDateTime initialTime, ZonedDateTime finalTime) {
        log.debug("Request to get all Payments");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        Page<Payment> payments = paymentRepository.findByDatesBetweenAndHouseId(pageable, zd_initialTime, zd_finalTime, houseId);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) companyId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
            if(!paymentDTO.getAccount().equals("-")){
                paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            }else{
                paymentDTO.setAccount("-");
            }
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public PaymentDTO findOneComplete(Long id) {
        Payment payment = paymentRepository.findOne(id);
        String currency = companyConfigurationService.getByCompanyId(null, (long) payment.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) payment.getCompanyId());
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
        if(!paymentDTO.getAccount().equals("-")){
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        }else{
            paymentDTO.setAccount("-");
        }
        paymentDTO.setAmmountLeft(payment.getAmmountLeft());
        if (!paymentDTO.getTransaction().equals("3")) {
            paymentDTO.setHouseNumber(this.houseService.findOne(paymentDTO.getHouseId()).getHousenumber());
        }
        double abonadoCharges = 0;
        double saldoAFavorEnPago = 0;
        for (int i = 0; i < paymentDTO.getCharges().size(); i++) {
            abonadoCharges = Double.parseDouble(paymentDTO.getCharges().get(i).getAbonado());
        }
        if(paymentDTO.getCharges().size()>0 && abonadoCharges>Double.parseDouble(paymentDTO.getAmmount())){
            saldoAFavorEnPago = abonadoCharges - Double.parseDouble(payment.getAmmount());
            saldoAFavorEnPago = saldoAFavorEnPago < 0 ? -saldoAFavorEnPago : saldoAFavorEnPago;
            paymentDTO.setAmmountPayedSaldoFavor(saldoAFavorEnPago+"");
            paymentDTO.setAmmountPayedSaldoFavorFormatted(currency,saldoAFavorEnPago+"");
        }
        paymentDTO.setPaymentProofs(paymentProofService.getPaymentProofsByPaymentId(paymentDTO.getId()));
        return paymentDTO;
    }


    public PaymentDTO findOneCompleteOld(Long id, String currency, List<CustomChargeTypeDTO> customChargeTypes) {
        Payment payment = paymentRepository.findOne(id);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        paymentDTO.setChargesOld(chargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()).getContent());
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft(payment.getAmmountLeft());
        if (!paymentDTO.getTransaction().equals("3")) {
            paymentDTO.setHouseNumber(this.houseService.findOne(paymentDTO.getHouseId()).getHousenumber());
        }
        paymentDTO.setPaymentProofs(paymentProofService.getPaymentProofsByPaymentId(paymentDTO.getId()));
        return paymentDTO;
    }

    @Transactional(readOnly = true)
    public PaymentDTO findOneCompleteClean(Long id, String currency, List<CustomChargeTypeDTO> customChargeTypeDTOS) {
        Payment payment = paymentRepository.findOne(id);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypeDTOS, currency, paymentDTO.getId()));
        paymentDTO.setAmmountLeft(payment.getAmmountLeft());
        if (!paymentDTO.getTransaction().equals("3")) {
            paymentDTO.setHouseNumber(this.houseService.findOneClean(paymentDTO.getHouseId()).getHousenumber());
        }
        paymentDTO.setPaymentProofs(paymentProofService.getPaymentProofsByPaymentId(paymentDTO.getId()));
        return paymentDTO;
    }


    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouseUnderDate(Pageable pageable, Long houseId, ZonedDateTime initialTime) {
        log.debug("Request to get all Payments");
        Page<Payment> payments = paymentRepository.findUnderDateAndHouseId(pageable, initialTime, houseId);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouse(Pageable pageable, Long houseId) {
        log.debug("Request to get all Payments");
        Page<Payment> payments = paymentRepository.findByHouseIdOrderByIdDesc(pageable, houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
            if(!paymentDTO.getAccount().equals("-")){
                paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            }else{
                paymentDTO.setAccount("-");
            }
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findWaterPaymentsByHouse(Pageable pageable, Long houseId) {
        log.debug("Request to get all Payments");
        Page<Payment> payments = paymentRepository.findByHouseIdOrderByIdDesc(pageable, houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        List<PaymentDTO> finalList = new ArrayList<>();
        Long companyId = this.houseService.findOneClean(houseId).getCompanyId();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            List<PaymentChargeDTO> charges = paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
            paymentDTO.setCharges(charges);
            for (int e = 0; e < charges.size(); i++) {
                if (charges.get(e).getType() == 6) {
                    finalList.add(paymentDTO);
                }
            }
        }

        return finalList;
    }


    /**
     * Get one payment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PaymentDTO findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        Payment payment = paymentRepository.findOne(id);
        return paymentMapper.toDto(payment);
    }

    /**
     * Delete the  payment by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        PaymentDTO p = this.findOne(id);
        Long companyId = p.getCompanyId().longValue();
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(companyId);
        List<PaymentChargeDTO> paymentCharges = this.paymentChargeService.findAllByPayment(customChargeTypes, currency, id);
        for (PaymentChargeDTO c : paymentCharges) {
            this.chargeService.removeChargeFromPayment(currency, c, companyId);
            this.paymentChargeService.delete(c.getId());
        }
        List<PaymentProofDTO> proofs = this.paymentProofService.getPaymentProofsByPaymentId(id);
        proofs.forEach(paymentProofDTO -> {
            paymentProofDTO.setPaymentId(null);
            try {
                this.paymentProofService.save(paymentProofDTO);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        paymentRepository.delete(id);
        this.historicalDefaulterService.formatHistoricalReportByHouse(p.getHouseId(), p.getDate(), currency, companyId.intValue());
    }

    public PaymentDTO createPaymentDTOtoPaymentDTO(CreatePaymentDTO cPaymentDTO) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAccount(cPaymentDTO.getAccount());
        paymentDTO.setAmmount(cPaymentDTO.getAmmount());
        paymentDTO.setComments(cPaymentDTO.getComments());
        paymentDTO.setCompanyId(cPaymentDTO.getCompanyId());
        paymentDTO.setConcept(cPaymentDTO.getConcept());
        paymentDTO.setDate(cPaymentDTO.getDate());
        paymentDTO.setHouseId(cPaymentDTO.getHouseId());
        paymentDTO.setPaymentMethod(cPaymentDTO.getPaymentMethod());
        paymentDTO.setReceiptNumber(cPaymentDTO.getReceiptNumber());
        paymentDTO.setTransaction(cPaymentDTO.getTransaction());
        paymentDTO.setDocumentReference(cPaymentDTO.getDocumentReference());
        return paymentDTO;
    }

    private List<ChargeDTO> filterCharges(CreatePaymentDTO payment) {
        List<ChargeDTO> listaCargos = payment.getCharges();
        List<ChargeDTO> cargosFiltrados = new ArrayList<>();
        for (int i = 0; i < listaCargos.size(); i++) {
            if (Double.parseDouble(listaCargos.get(i).getPaymentAmmount()) != 0) {
                cargosFiltrados.add(listaCargos.get(i));
            }
        }
        return cargosFiltrados;
    }

    public PaymentDTO findPaymentInAdvance(Long houseId) {
        List<Payment> payments = paymentRepository.findPaymentsInAdvance(null, "2", "0", houseId).getContent();
        Payment paymentToUse = null;
        if (payments.size() > 0) {
            paymentToUse = payments.get(0);
            for (int i = 0; i < payments.size(); i++) {
                Payment payment = payments.get(i);
                if (payment.getDate().isBefore(paymentToUse.getDate())) {
                    paymentToUse = payment;
                }
            }
        }

        if (paymentToUse != null) {
            PaymentDTO paymentDTO = paymentMapper.toDto(paymentToUse);
            paymentDTO.setAmmountLeft(paymentToUse.getAmmountLeft());
            return paymentDTO;
        } else {
            return null;
        }
    }

    private ChargeDTO newCharge(ChargeDTO chargeDTO) {
        String currency = companyConfigurationService.getByCompanyId(null, this.houseService.findOne(chargeDTO.getHouseId()).getCompanyId()).getContent().get(0).getCurrency();
        return new ChargeDTO(currency, null, chargeDTO.getType(), formatDateTime(chargeDTO.getDate()), chargeDTO.getConcept(),
            chargeDTO.getAmmount(), chargeDTO.getState(), chargeDTO.getDeleted(),
            chargeDTO.getPaymentDate(), "0",
            chargeDTO.getPaymentAmmount(), chargeDTO.getLeft(), chargeDTO.getTotal(),
            chargeDTO.getHouseId(), chargeDTO.getPaymentId(), chargeDTO.getCompanyId());
    }

    //    private void payCharge(ChargeDTO charge, Payment payment) {
//        if (Double.parseDouble(charge.getLeft()) > 0) {
//            ChargeDTO newCharge = newCharge(charge);
//            if (Double.parseDouble(newCharge.getPaymentAmmount()) <= Double.parseDouble(newCharge.getSubcharge())) {
//                newCharge.setSubcharge(Double.parseDouble(newCharge.getSubcharge()) - Double.parseDouble(newCharge.getPaymentAmmount()) + "");
//                charge.setSubcharge(newCharge.getPaymentAmmount());
//                charge.setAmmount("0");
//            } else {
//                double left = Double.parseDouble(newCharge.getPaymentAmmount()) - Double.parseDouble(newCharge.getSubcharge());
//                newCharge.setAmmount(charge.getLeft());
//                newCharge.setSubcharge("0");
//                charge.setAmmount(left + "");
//            }
//            newCharge.setSplited(1);
//            newCharge.setConsecutive(charge.getConsecutive());
//            charge.setSplitedCharge(chargeService.create(newCharge).getId().intValue());
//            chargeService.pay(charge, payment);
//            newCharge.setPayedSubcharge(true);
//        } else {
//            charge.setAmmount(charge.getPaymentAmmount());
//            chargeService.pay(charge, payment);
//        }
//    }
    private PaymentChargeDTO payCharge(ChargeDTO c, Payment payment) {
        ChargeDTO cN = null;
        PaymentChargeDTO paymentCharge = null;
        if (!c.getCategory().equals("10")) {
            double ammountPaid = Double.parseDouble(c.getPaymentAmmount());
            c.setLeftToPay(Double.parseDouble(c.getLeft()));
            c.setAbonado((c.getAbonado() + ammountPaid));
            if (c.getAbonado() == Double.parseDouble(c.getAmmount())) {
                c.setState(2);
            }
            if (c.getLeftToPay() == 0) {
                c.setState(2);
            }
            cN = this.chargeService.saveFormat(c);
            paymentCharge = new PaymentChargeDTO(null, c.getType(), c.getDate(), c.getConcept(), c.getAmmount(), c.getId(), c.getConsecutive() + "", c.getAbonado() + "", c.getLeftToPay() + "", 0, payment.getId());
            paymentCharge.setOriginalCharge(cN.getId());
        } else {
            Long cId = c.getId() != null ? c.getId() : -1;
            paymentCharge = new PaymentChargeDTO(null, c.getType(), ZonedDateTime.now(), c.getConcept(), c.getAmmount(), cId, c.getConsecutive() + "", c.getAmmount() + "", c.getLeftToPay() + "", 1, payment.getId());
        }
        return this.paymentChargeService.save(paymentCharge);
    }

    public File obtainFileToPrint(Long paymentId) {
        PaymentDTO paymentDTO = this.findOne(paymentId);
        String currency = companyConfigurationService.getByCompanyId(null, (long) paymentDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) paymentDTO.getCompanyId());
        paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
        if (paymentDTO.getCharges().size() == 0) {
            paymentDTO.setCharges(new ArrayList<>());
        }
        List<ResidentDTO> residents = residentService.findOwnerByHouse(paymentDTO.getHouseId() + "");
        List<ResidentDTO> emailTo = new ArrayList<>();
        for (int i = 0; i < residents.size(); i++) {
            if (residents.get(i).getPrincipalContact() == 1) {
                emailTo.add(residents.get(i));
            }
        }

        paymentDTO.setEmailTo(emailTo);
        boolean cancelingFromPayment = false;
        if (paymentDTO.getTransaction().equals("2") && paymentDTO.getCharges().size() > 0) {
            cancelingFromPayment = true;
        }
        return paymentEmailSenderService.obtainFileToPrint(paymentDTO, cancelingFromPayment);
    }

    public File obtainIncomeReportToPrint(Pageable pageable, int companyId, ZonedDateTime initial_time, ZonedDateTime final_time, String houseId, String paymentMethod, String category, String account) {
        IncomeReportDTO income = this.findIncomeReportByCompanyAndDatesBetween(pageable, initial_time, final_time, companyId, houseId, paymentMethod, category, account);
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initial_time + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((final_time + "[America/Regina]").replace("00:00:00", "23:59:59"));
        return paymentEmailSenderService.obtainIncomeReportToPrint(income, Long.valueOf(companyId + ""), zd_initialTime, zd_finalTime);
    }

    @Async
    public void sendPaymentEmail(Long paymentId) {
        PaymentDTO paymentDTO = this.findOne(paymentId);
        String currency = companyConfigurationService.getByCompanyId(null, (long) paymentDTO.getCompanyId()).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) paymentDTO.getCompanyId());
        paymentDTO.setCharges(paymentChargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()));
//        paymentDTO.getCharges().forEach(chargeDTO -> {
//            chargeDTO.setPaymentAmmount(chargeDTO.getAmmount());
//        });
        if (paymentDTO.getCharges().size() == 0) {
            paymentDTO.setCharges(new ArrayList<>());
        }
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft("0");
        List<ResidentDTO> residents = residentService.findOwnerByHouse(paymentDTO.getHouseId() + "");
        List<ResidentDTO> emailTo = new ArrayList<>();
        for (int i = 0; i < residents.size(); i++) {
            if (residents.get(i).getPrincipalContact() == 1) {
                emailTo.add(residents.get(i));
            }
        }

        paymentDTO.setEmailTo(emailTo);
        paymentEmailSenderService.sendPaymentEmail(paymentDTO, false);
    }


    private String findCategoriesInPayment(PaymentDTO payment) {
        if (Integer.parseInt(payment.getTransaction()) == 3) {
            return "Otro ingreso";
        }
        String categoriesFinalString = "";
        List<Integer> categories = new ArrayList<>();
        List<PaymentChargeDTO> cuotas = payment.getCharges();
        cuotas.forEach(chargeDTO -> {
            if (!categories.contains(chargeDTO.getType())) {
                categories.add(chargeDTO.getType());
            }
        });
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i) < 7) {
                switch (categories.get(i)) {
                    case 1:
                        categoriesFinalString += "Mantenimiento";
                        if (i != categories.size() - 1) {
                            categoriesFinalString += " / ";
                        }
                        break;
                    case 2:
                        categoriesFinalString += "Extraordinaria";
                        if (i != categories.size() - 1) {
                            categoriesFinalString += " / ";
                        }
                        break;
                    case 3:
                        categoriesFinalString += "Áreas Comunes";
                        if (i != categories.size() - 1) {
                            categoriesFinalString += " / ";
                        }
                        break;
                    case 5:
                        categoriesFinalString += "Multa";
                        if (i != categories.size() - 1) {
                            categoriesFinalString += " / ";
                        }
                        break;
                    case 6:
                        categoriesFinalString += "Cuota agua";
                        if (i != categories.size() - 1) {
                            categoriesFinalString += " / ";
                        }
                        break;
                }
            } else {
                categoriesFinalString += "Otro ingreso";
                if (i != categories.size() - 1) {
                    categoriesFinalString += " / ";
                }
            }
        }
        if (payment.getCharges().size() == 0) {
            return "Adelanto de condómino";
        }
        return categoriesFinalString;
    }


    private double getAdelantos(ZonedDateTime zd_initialTime, ZonedDateTime zd_finalTime, int companyId, String houseId, String currency, List<CustomChargeTypeDTO> customChargeTypes) {
        double total = 0.0;
        List<PaymentDTO> payments = new ArrayList<>();
        if (houseId.equals("empty")) {
            payments = this.findAdelantosByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId);
        } else {
            payments = this.findAdelantosByDatesBetweenAndHouseId(zd_initialTime, zd_finalTime, houseId);
        }
        for (int p = 0; p < payments.size(); p++) {
            total = total + Double.parseDouble(payments.get(p).getAmmount());
        }
        return total;
    }

    private double getTotalAmmoutPerTypeOfPayment(IncomeReportDTO incomeReport, int type) {
        double total = 0.0;
        for (int i = 0; i < incomeReport.getPayments().size(); i++) {
            PaymentDTO payment = incomeReport.getPayments().get(i);
            if (payment.getTransaction().equals("2") && type == 7) {
                total = total + Double.parseDouble(payment.getAmmount());
            } else {
                if (payment.getTransaction().equals("1")) {
                    for (int j = 0; j < payment.getCharges().size(); j++) {
                        PaymentChargeDTO charge = payment.getCharges().get(j);
                        if (charge.getType() == type) {
                            total += Double.parseDouble(charge.getAmmount());
                        }
                    }
                }
            }
        }
        return total;
    }


    private double getTotalAmmoutPerTypeMoreeThanOfPayment(IncomeReportDTO incomeReport, int type) {
        double total = 0.0;
        for (int i = 0; i < incomeReport.getPayments().size(); i++) {
            PaymentDTO payment = incomeReport.getPayments().get(i);
            if (payment.getTransaction().equals("2") && type > 7) {
                total = total + Double.parseDouble(payment.getAmmount());
            } else {
                if (payment.getTransaction().equals("1")) {
                    for (int j = 0; j < payment.getCharges().size(); j++) {
                        PaymentChargeDTO charge = payment.getCharges().get(j);
                        if (charge.getType() > type) {
                            total += Double.parseDouble(charge.getAmmount());
                        }
                    }
                }
            }
        }
        return total;
    }

    private List<PaymentDTO> filterPaymentsForIncome(List<PaymentDTO> payments, String houseId, String paymentMethod, String category, String account) {
        List<PaymentDTO> filteredPayments = new ArrayList<>();
        for (int i = 0; i < payments.size(); i++) {
            PaymentDTO p = payments.get(i);
            int pConditions = 0;
            if (account.equals("empty") || p.getAccount().toUpperCase().equals(account.toUpperCase())) {
                pConditions++;
            }
            if (paymentMethod.equals("empty") || p.getPaymentMethod().toUpperCase().equals(paymentMethod.toUpperCase())) {
                pConditions++;
            }
            if (category.equals("empty") || p.getCategories().toUpperCase().contains(category.toUpperCase())) {
                pConditions++;
            }

            if (houseId.equals("empty") || Long.valueOf(houseId).equals(p.getHouseId())) {
                pConditions++;
            }
            if (pConditions == 4) {
                filteredPayments.add(p);
            }
        }
        Collections.sort(filteredPayments, new Comparator<PaymentDTO>() {
            public int compare(PaymentDTO o1, PaymentDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return filteredPayments;
    }

    public List<PaymentDTO> formatNewPayments(Long companyId) {
        log.debug("Request to get all Payments in last month by house");
        List<Payment> payments = paymentRepository.findByCompanyId(companyId.intValue());
        String currency = companyConfigurationService.getByCompanyId(null, (long) companyId).getContent().get(0).getCurrency();
        List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany((long) companyId);
        List<PaymentDTO> paymentsDTO = new PageImpl<>(payments).map(paymentMapper::toDto).getContent();
        for (int i = 0; i < paymentsDTO.size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.get(i);
            List<ChargeDTO> paymentCharges = chargeService.findAllByPayment(customChargeTypes, currency, paymentDTO.getId()).getContent();
            for (int j = 0; j < paymentCharges.size(); j++) {
                ChargeDTO c = paymentCharges.get(j);
                PaymentChargeDTO paymentCharge = new PaymentChargeDTO(null, c.getType(), c.getDate(), c.getConcept(), c.getAmmount(), c.getId(), c.getConsecutive() + "", c.getAmmount(), null, 1, c.getPaymentId());
                this.paymentChargeService.save(paymentCharge);
            }
        }
        return paymentsDTO;
    }

    public List<ChargeDTO> formatOldCharges(Long companyId) {
        log.debug("Request to get all Payments in last month by house");
        List<ChargeDTO> charges = chargeService.findAllByCompany(companyId);
        List<ChargeDTO> Allcharges = chargeService.findAllByCompanyAll(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, Long.parseLong(companyId + "")).getContent().get(0).getCurrency();
        List<ChargeDTO> chargesNew = new ArrayList<>();
        charges.forEach(chargeDTO -> {
            ChargeDTO oldC = chargeDTO;
            ChargeDTO newC = new ChargeDTO();
            newC.setAmmount(oldC.getTotal() + "");
            newC.setAbonado(currency, oldC.getAbonado());
            newC.setLeftToPay(currency, oldC.getLeftToPay());
            newC.setConsecutive(oldC.getConsecutive());
            newC.setPaymentDate(oldC.getPaymentDate());
            newC.setDeleted(oldC.getDeleted());
            newC.setDate(oldC.getDate());
            newC.setHouseId(oldC.getHouseId());
            newC.setCompanyId(oldC.getCompanyId());
            newC.setConcept(oldC.getConcept());
            newC.setType(oldC.getType());
            newC.setOldChargeId(oldC.getId());
            if (Double.parseDouble(newC.getLeftToPay() + "") == 0) {
                newC.setState(2);
            } else {
                newC.setState(1);
            }

            chargesNew.add(newC);
        });
        chargesNew.forEach(chargeDTO -> {
            ChargeDTO nC = this.chargeService.saveFormatOld(chargeDTO);
            List<PaymentChargeDTO> p = this.paymentChargeService.findAllByConsecutive(chargeDTO.getConsecutive() + "");
            for (int i = 0; i < p.size(); i++) {
                PaymentChargeDTO nP = p.get(i);
                nP.setOriginalCharge(nC.getId());
                this.paymentChargeService.save(nP);
            }
            if (chargeDTO.getType() == 6) {
                WaterConsumptionDTO wc = this.waterConsumptionService.findOneByChargeIdFormating(chargeDTO.getOldChargeId());
                if (wc != null) {
                    wc.setChargeId(nC.getId());
                    this.waterConsumptionService.update(wc);
                }
            }
        });

        for (int j = 0; j < Allcharges.size(); j++) {
            ChargeDTO c = Allcharges.get(j);
            this.chargeService.delete(c.getId());
        }
        return null;
    }

}
