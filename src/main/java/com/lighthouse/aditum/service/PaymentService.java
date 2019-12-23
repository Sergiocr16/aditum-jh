package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Payment;
import com.lighthouse.aditum.repository.PaymentRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;
import static com.lighthouse.aditum.service.util.RandomUtil.formatDateTime;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;


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


    public PaymentService(CompanyConfigurationService companyConfigurationService,UserService userService, AdminInfoService adminInfoService, BitacoraAccionesService bitacoraAccionesService, BalanceByAccountService balanceByAccountService, @Lazy HouseService houseService, ResidentService residentService, PaymentDocumentService paymentEmailSenderService, PaymentRepository paymentRepository, PaymentMapper paymentMapper, ChargeService chargeService, BancoService bancoService) {
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

    public PaymentDTO save(CreatePaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(createPaymentDTOtoPaymentDTO(paymentDTO));
        if (payment.getTransaction().equals("2")) {
            payment.setAmmountLeft(paymentDTO.getAmmount());
        }
        payment.setHouse(paymentMapper.houseFromId(paymentDTO.getHouseId()));
        payment.setAccount(paymentDTO.getAccount().split(";")[1]);
        payment.setDate(formatDateTime(payment.getDate()));
        payment = paymentRepository.save(payment);
        bancoService.increaseSaldo(Long.valueOf(paymentDTO.getAccount().split(";")[1]).longValue(), paymentDTO.getAmmount());
        List<ChargeDTO> paymentCharges = this.filterCharges(paymentDTO);
        for (int i = 0; i < paymentCharges.size(); i++) {
            this.payCharge(paymentCharges.get(i), payment);
        }
        PaymentDTO paymentDTo = paymentMapper.toDto(payment);
        paymentDTo.setCharges(paymentCharges);
        paymentDTo.setEmailTo(paymentDTO.getEmailTo());
        if (paymentDTo.getEmailTo().size() > 0) {
            this.paymentEmailSenderService.sendPaymentEmail(paymentDTo, false);
        }
        this.balanceByAccountService.modifyBalancesInPastPayment(payment);
        String currency = companyConfigurationService.getByCompanyId(null,Long.parseLong(paymentDTO.getCompanyId()+"")).getContent().get(0).getCurrency();

        String concepto = "";
        if (paymentDTO.getHouseId() != null) {
            concepto = "Captura de ingreso de la filial " + houseService.findOne(paymentDTO.getHouseId()).getHousenumber() + ", por " + formatMoney(currency,Integer.parseInt(paymentDTO.getAmmount()));
        } else {
            concepto = "Captura de ingreso en la categoría otros: " + paymentDTO.getConcept() + " por " + formatMoney(currency,Integer.parseInt(paymentDTO.getAmmount()));

        }

//        bitacoraAccionesService.save(createBitacoraAcciones(concepto, 5, null, "Ingresos", payment.getId(), Long.parseLong(paymentDTO.getCompanyId() + ""), payment.getHouse().getId()));

        return paymentMapper.toDto(payment);
    }


    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByDatesBetweenAndCompany(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Payments in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        Page<Payment> payments = paymentRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
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
        Page<Payment> payments = paymentRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            if (Integer.parseInt(paymentDTO.getTransaction()) != 3) {
                paymentDTO.setHouseNumber(this.houseService.findOne(paymentDTO.getHouseId()).getHousenumber());
            }
            paymentDTO.setCategories(this.findCategoriesInPayment(paymentDTO));
        }
        IncomeReportDTO incomeReport = new IncomeReportDTO(this.houseService);
        incomeReport.setPayments(this.filterPaymentsForIncome(paymentsDTO.getContent(), houseId, paymentMethod, category, account));
        int totalMaint = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 1);
        int totalExtra = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 2);
        int totalAreas = this.getTotalAmmoutPerTypeOfPayment(incomeReport, 3);
        int totalOtherIngress = this.findTotalOtherIngressByDatesBetweenAndCompany(initialTime, finalTime, companyId);
        String currency = companyConfigurationService.getByCompanyId(null,Long.parseLong(companyId+"")).getContent().get(0).getCurrency();

        incomeReport.setTotalMaintenance(formatMoney(currency,totalMaint));
        incomeReport.setTotalExtraordinary(formatMoney(currency,totalExtra));
        incomeReport.setTotalCommonArea(formatMoney(currency,totalAreas));
        incomeReport.setTotalOtherIngress(formatMoney(currency,totalOtherIngress));
        incomeReport.setTotal(formatMoney(currency,totalMaint + totalAreas + totalExtra + totalOtherIngress));
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
    public List<PaymentDTO> findOtherIngressByDatesBetweenAndCompany(ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        return paymentRepository.findAdelantosByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId, "3").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public int findTotalOtherIngressByDatesBetweenAndCompany(ZonedDateTime initialTime, ZonedDateTime finalTime, int companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        int total = 0;
        List<PaymentDTO> payments = paymentRepository.findAdelantosByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId, "3").stream()
            .map(paymentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        for (int i = 0; i < payments.size(); i++) {
            total = total + Integer.parseInt(payments.get(i).getAmmount());
        }
        return total;
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAdelantosByHouse(Long houseId) {
        return paymentRepository.findAdelantosByHouse(houseId, "2").stream()
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
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public PaymentDTO findOneComplete(Long id) {
        Payment payment = paymentRepository.findOne(id);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft(payment.getAmmountLeft());
        if(!paymentDTO.getTransaction().equals("3")) {
            paymentDTO.setHouseNumber(this.houseService.findOne(paymentDTO.getHouseId()).getHousenumber());
        }
        return paymentDTO;
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouseUnderDate(Pageable pageable, Long houseId, ZonedDateTime initialTime) {
        log.debug("Request to get all Payments");
        Page<Payment> payments = paymentRepository.findUnderDateAndHouseId(pageable, initialTime, houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }
        return paymentsDTO;
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByHouse(Pageable pageable, Long houseId) {
        log.debug("Request to get all Payments");
        Page<Payment> payments = paymentRepository.findByHouseId(pageable, houseId);
        Page<PaymentDTO> paymentsDTO = payments.map(paymentMapper::toDto);
        for (int i = 0; i < paymentsDTO.getContent().size(); i++) {
            PaymentDTO paymentDTO = paymentsDTO.getContent().get(i);
            paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
            paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
            paymentDTO.setAmmountLeft(payments.getContent().get(i).getAmmountLeft());
        }

        return paymentsDTO;
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
        paymentRepository.delete(id);
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
        return paymentDTO;
    }

    private List<ChargeDTO> filterCharges(CreatePaymentDTO payment) {
        List<ChargeDTO> listaCargos = payment.getCharges();
        List<ChargeDTO> cargosFiltrados = new ArrayList<>();
        for (int i = 0; i < listaCargos.size(); i++) {
            if (Integer.parseInt(listaCargos.get(i).getPaymentAmmount()) != 0) {
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
        String currency = companyConfigurationService.getByCompanyId(null,chargeDTO.getCompanyId()).getContent().get(0).getCurrency();

        return new ChargeDTO(currency,null, chargeDTO.getType(), formatDateTime(chargeDTO.getDate()), chargeDTO.getConcept(),
            chargeDTO.getAmmount(), chargeDTO.getState(), chargeDTO.getDeleted(),
            chargeDTO.getPaymentDate(), chargeDTO.getSubcharge(),
            chargeDTO.getPaymentAmmount(), chargeDTO.getLeft(), chargeDTO.getTotal(),
            chargeDTO.getHouseId(), chargeDTO.getPaymentId(), chargeDTO.getCompanyId());

    }

    private void payCharge(ChargeDTO charge, Payment payment) {
        if (Double.parseDouble(charge.getLeft()) > 0) {

            ChargeDTO newCharge = newCharge(charge);
            if (Double.parseDouble(newCharge.getPaymentAmmount()) <= Double.parseDouble(newCharge.getSubcharge())) {
                newCharge.setSubcharge(Double.parseDouble(newCharge.getSubcharge()) - Double.parseDouble(newCharge.getPaymentAmmount()) + "");
                charge.setSubcharge(newCharge.getPaymentAmmount());
                charge.setAmmount("0");
            } else {
                double left = Double.parseDouble(newCharge.getPaymentAmmount()) - Double.parseDouble(newCharge.getSubcharge());
                newCharge.setAmmount(charge.getLeft());
                newCharge.setSubcharge("0");
                charge.setAmmount(left + "");
            }
            newCharge.setSplited(1);
            charge.setSplitedCharge(chargeService.create(newCharge).getId().intValue());
            chargeService.pay(charge, payment);
            newCharge.setPayedSubcharge(true);
        } else {
            chargeService.pay(charge, payment);
        }
    }


    public File obtainFileToPrint(Long paymentId) {
        PaymentDTO paymentDTO = this.findOne(paymentId);
        paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
        paymentDTO.getCharges().forEach(chargeDTO -> {
            chargeDTO.setPaymentAmmount(chargeDTO.getTotal() + "");
        });
        if (paymentDTO.getCharges().size() == 0) {
            paymentDTO.setCharges(new ArrayList<>());
        }
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft("0");
        Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null, paymentDTO.getHouseId());
        List<ResidentDTO> emailTo = new ArrayList<>();
        for (int i = 0; i < residents.getContent().size(); i++) {
            if (residents.getContent().get(i).getPrincipalContact() == 1) {
                emailTo.add(residents.getContent().get(i));
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
        paymentDTO.setCharges(chargeService.findAllByPayment(paymentDTO.getId()).getContent());
        paymentDTO.getCharges().forEach(chargeDTO -> {
            chargeDTO.setPaymentAmmount(chargeDTO.getAmmount());
        });
        if (paymentDTO.getCharges().size() == 0) {
            paymentDTO.setCharges(new ArrayList<>());
        }
        paymentDTO.setAccount(bancoService.findOne((Long.valueOf(paymentDTO.getAccount()))).getBeneficiario());
        paymentDTO.setAmmountLeft("0");
        Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null, paymentDTO.getHouseId());
        List<ResidentDTO> emailTo = new ArrayList<>();
        for (int i = 0; i < residents.getContent().size(); i++) {
            if (residents.getContent().get(i).getPrincipalContact() == 1) {
                emailTo.add(residents.getContent().get(i));
            }
        }

        paymentDTO.setEmailTo(emailTo);
        paymentEmailSenderService.sendPaymentEmail(paymentDTO, false);
    }


    private String findCategoriesInPayment(PaymentDTO payment) {
        String categoriesFinalString = "";
        List<Integer> categories = new ArrayList<>();
        List<ChargeDTO> cuotas = payment.getCharges();
        cuotas.forEach(chargeDTO -> {
            if (!categories.contains(chargeDTO.getType())) {
                categories.add(chargeDTO.getType());
            }
        });
        for (int i = 0; i < categories.size(); i++) {
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
            }
        }
        if (payment.getCharges().size() == 0) {
            return "Adelanto de condómino";
        }
        return categoriesFinalString;
    }


    private int getTotalAmmoutPerTypeOfPayment(IncomeReportDTO incomeReport, int type) {
        int total = 0;
        for (int i = 0; i < incomeReport.getPayments().size(); i++) {
            PaymentDTO payment = incomeReport.getPayments().get(i);
            if (payment.getTransaction().equals("2") && type == 1) {
                total += Double.parseDouble(payment.getAmmount());
            } else {
                for (int j = 0; j < payment.getCharges().size(); j++) {
                    ChargeDTO charge = payment.getCharges().get(j);
                    if (charge.getType() == type) {
                        total += charge.getTotal();
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
}
