package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.repository.EgressRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.EgressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;


/**
 * Service Implementation for managing Egress.
 */
@Service
@Transactional
public class  EgressService {

    private final Logger log = LoggerFactory.getLogger(EgressService.class);

    private final EgressRepository egressRepository;

    private final EgressMapper egressMapper;

    private final EgressCategoryService egressCategoryService;

    private final ProveedorService proveedorService;

    private final BancoService bancoService;

    private final AdminInfoService adminInfoService;

    private final HouseService houseService;

    private final UserService userService;


    private final CommonAreaReservationsService commonAreaReservationsService;

    private final CompanyConfigurationService companyConfigurationService;

    private final BitacoraAccionesService bitacoraAccionesService;

    private final BalanceByAccountService balanceByAccountService;

    private final PushNotificationService pNotification;

    private final CompanyService companyService;


    public EgressService(@Lazy CompanyConfigurationService companyConfigurationService, CompanyService companyService, PushNotificationService pNotification,AdminInfoService adminInfoService, UserService userService, BitacoraAccionesService bitacoraAccionesService, BalanceByAccountService balanceByAccountService, EgressRepository egressRepository, EgressMapper egressMapper, EgressCategoryService egressCategoryService, ProveedorService proveedorService, BancoService bancoService, HouseService houseService, @Lazy CommonAreaReservationsService commonAreaReservationsService) {
        this.egressRepository = egressRepository;
        this.egressMapper = egressMapper;
        this.egressCategoryService = egressCategoryService;
        this.proveedorService = proveedorService;
        this.bancoService = bancoService;
        this.balanceByAccountService = balanceByAccountService;
        this.commonAreaReservationsService = commonAreaReservationsService;
        this.houseService = houseService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.userService = userService;
        this.adminInfoService = adminInfoService;
        this.pNotification = pNotification;
        this.companyService = companyService;
        this.companyConfigurationService = companyConfigurationService;
    }

    /**
     * Save a egress.
     *
     * @param egressDTO the entity to save
     * @return the persisted entity
     */
    public EgressDTO save(EgressDTO egressDTO) throws URISyntaxException {
        log.debug("Request to save Egress : {}", egressDTO);
        Egress egress = egressMapper.toEntity(egressDTO);
        if (egress.getPaymentDate() != null) {
            ZonedDateTime n = ZonedDateTime.now();
            egress.setPaymentDate(egress.getPaymentDate().withHour(n.getHour()).withMinute(n.getMinute()).withSecond(n.getSecond()));
            this.balanceByAccountService.modifyBalancesInPastEgress(egress);
        }
        if (egress.getHasComission() != null) {
            if (egress.getHasComission() == 1) {
                Egress comission = new Egress();
                comission.setConcept("Comisión por transferencia bancaria de " + egress.getConcept());
                comission.setDate(egress.getDate());
                comission.setState(2);
                comission.setTotal(egress.getComission());
                comission.setExpirationDate(egress.getExpirationDate());
                comission.setPaymentDate(egress.getPaymentDate());
                comission.deleted(0);
                comission.setCurrency(egress.getCurrency());
                comission.setAccount(egress.getAccount());
                List<EgressCategoryDTO> egressCategoryDTOS = egressCategoryService.findAll(null, egressDTO.getCompanyId());
                egressCategoryDTOS.forEach(egressCategory -> {
                    if (egressCategory.getGroup().equals("Otros gastos")  && egressCategory.getCategory().equals("Comisiones Bancarias")) {
                        comission.setCategory(egressCategory.getId()+"");
                    }
                });
                comission.account(egress.getAccount());
                comission.setCompany(egress.getCompany());
                comission.setPaymentMethod(egress.getPaymentMethod());
                Page<ProveedorDTO> proveedores = proveedorService.findAll(null, egress.getCompany().getId());
                if(comission.getAccount()!=null) {
                    BancoDTO banco = bancoService.findOne(Long.parseLong(comission.getAccount()));
                    comission.setCurrency(banco.getCurrency());
                    proveedores.getContent().forEach(proveedorDTO -> {
                        if (proveedorDTO.getEmpresa().equals(banco.getBeneficiario())) {
                            comission.setProveedor(proveedorDTO.getId() + "");
                        }
                    });
                }

                egressRepository.save(comission);

            }
        }
        egress.setDeleted(egressDTO.getDeleted());
        egress = egressRepository.save(egress);

        String concepto = "";
        String conceptoNoti = "";

        CompanyDTO company = this.companyService.findOne(egressDTO.getCompanyId());
        String currency = this.companyConfigurationService.findOne(egressDTO.getCompanyId()).getCurrency();

        if (egressDTO.getId() == null) {
            concepto = "Registro de egreso: " + egressDTO.getConcept() + " por " + formatMoney(currency,Double.parseDouble(egressDTO.getTotal())) + ".";
            conceptoNoti = "Se realizó la creación del egreso " + egressDTO.getConcept() + " por un monto de "+currency + formatMoney(currency,Double.parseDouble(egressDTO.getTotal())) + ".";
            this.pNotification.sendNotificationAllAdminsByCompanyId(egressDTO.getCompanyId(),
                this.pNotification.createPushNotification("Registro de egreso - "+company.getName(),
                    conceptoNoti));
        } else if (egressDTO.getId() != null && egressDTO.getDeleted() == 0) {
            concepto = "Pago de egreso: " + egressDTO.getConcept() + " por " + formatMoney(currency,Double.parseDouble(egressDTO.getTotal())) + ".";
            conceptoNoti = "Se realizó la pago del egreso " + egressDTO.getConcept() + " por un monto de "+currency + formatMoney(currency,Double.parseDouble(egressDTO.getTotal())) + ".";
            this.pNotification.sendNotificationAllAdminsByCompanyId(egressDTO.getCompanyId(),
                this.pNotification.createPushNotification("Pago de egreso  - "+company.getName(),
                    conceptoNoti));
        } else if (egressDTO.getId() != null && egressDTO.getDeleted() == 1) {
            concepto = "Eliminación de egreso: " + egressDTO.getConcept() + " por " + formatMoney(currency,Double.parseDouble(egressDTO.getTotal())) + ".";
            conceptoNoti = "Se realizó la eliminación del egreso " + egressDTO.getConcept() + " de un monto de "+currency + formatMoney(currency,Double.parseDouble(egressDTO.getTotal())) + ".";
            this.pNotification.sendNotificationAllAdminsByCompanyId(egressDTO.getCompanyId(),
                this.pNotification.createPushNotification("Eliminación de egreso - "+company.getName(),
                    conceptoNoti));
        }
        bitacoraAccionesService.save(createBitacoraAcciones(concepto, 1, "egress-detail", "Egresos", egress.getId(), egress.getCompany().getId(), null));

        return egressMapper.toDto(egress);
    }

//    @Transactional(readOnly = true)
//    public Page<EgressDTO> findByDatesBetweenAndCompany(Pageable pageable, String initialTime, String finalTime, Long companyId) {
//        log.debug("Request to get all Visitants in last month by house");
//        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
//        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
//        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
//        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
//        Page<Egress> result = egressRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
////        Collections.reverse(result);
//        return result.map(egress -> egressMapper.toDto(egress));
//    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompany(String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        return new PageImpl<>(egressRepository.findByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId))
            .map(egressMapper::toDto);

    }

    private String formatColonesD(double text) {
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if (text == 0) {
            return currencyFormatter.format(text).substring(1);
        } else {
            String t = currencyFormatter.format(text).substring(1);
            return t.substring(0, t.length() - 3).replace(",", ".");
        }
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findPaymentEgressByDatesBetweenAndCompany(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        return new PageImpl<>(egressRepository.findPaymentEgressByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId))
            .map(egressMapper::toDto);

    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByCobroDatesBetweenAndCompany(String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        return new PageImpl<>(egressRepository.findByCobroDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId))
            .map(egressMapper::toDto);
    }

    @Transactional(readOnly = true)
    public EgressReportDTO egressReport(Pageable pageable, String initialTime, String finalTime, Long companyId, String selectedProveedors, String selectedCampos) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        String[] proveedorsParts = selectedProveedors.split(",");
        String[] camposParts = selectedCampos.split(",");
        EgressReportDTO egressReportDTO = new EgressReportDTO();
        double total = 0;
        for (int i = 0; i < proveedorsParts.length; i++) {
            if (!proveedorsParts[i].equals("a")) {
                if (!proveedorsParts[i].equals("devolucion")) {
                    ProveedorDTO proveedorDTO = proveedorService.findOne(Long.parseLong(proveedorsParts[i]));
                    List<EgressDTO> egresses = egressRepository.findByCobroDatesBetweenAndCompanyAndProveedor(zd_initialTime, zd_finalTime, companyId, proveedorsParts[i]).stream()
                        .map(egressMapper::toDto)
                        .collect(Collectors.toCollection(LinkedList::new));

                    if (egresses.size() > 0) {
                        String currency = this.companyConfigurationService.findOne(companyId).getCurrency();

                        EgressReportItemsDTO egressReportItemsDTO = new EgressReportItemsDTO(proveedorDTO.getEmpresa(), egresses, camposParts,currency);
                        egressReportDTO.getEgressByProveedor().add(egressReportItemsDTO);
                        for (int j = 0; j < egressReportItemsDTO.getEgresosFormatted().size(); j++) {
                            if (egressReportItemsDTO.getEgresosFormatted().get(j).getAccount() != null) {
                                if (egressReportItemsDTO.getEgresosFormatted().get(j).getAccount().equals("0")) {
                                    egressReportItemsDTO.getEgresosFormatted().get(j).setAccount("No pagado");
                                } else {
                                    BancoDTO bancoDTO = bancoService.findOne(Long.parseLong(egressReportItemsDTO.getEgresosFormatted().get(j).getAccount()));
                                    egressReportItemsDTO.getEgresosFormatted().get(j).setAccount(bancoDTO.getBeneficiario());
                                }
                            }
                        }
                        total = total + egressReportItemsDTO.getTotal();
                    }
                } else {
                    List<EgressDTO> egresses = egressRepository.findDevolutionsBetweenDatesAndCompany(zd_initialTime, zd_finalTime, companyId).stream()
                        .map(egressMapper::toDto)
                        .collect(Collectors.toCollection(LinkedList::new));
                    if (egresses.size() > 0) {
                        String currency = this.companyConfigurationService.findOne(companyId).getCurrency();
                        EgressReportItemsDTO egressReportItemsDTO = new EgressReportItemsDTO("Devoluciones de dinero", egresses, camposParts,currency);
                        egressReportDTO.getEgressByProveedor().add(egressReportItemsDTO);
                        for (int j = 0; j < egressReportItemsDTO.getEgresosFormatted().size(); j++) {
                            CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsService.findByEgressId(egressReportItemsDTO.getEgresosFormatted().get(j).getId());
                            HouseDTO houseDTO = houseService.findOne(commonAreaReservationsDTO.getHouseId());

                            egressReportItemsDTO.getEgresosFormatted().get(j).setFolio("N/A");


                            egressReportItemsDTO.getEgresosFormatted().get(j).setBillNumber("N/A");
                            egressReportItemsDTO.getEgresosFormatted().get(j).setConcept("Devolución por uso de áreas comunes - Filial " + houseDTO.getHousenumber());
                            if (egressReportItemsDTO.getEgresosFormatted().get(j).getAccount() != null) {
                                BancoDTO bancoDTO = bancoService.findOne(Long.parseLong(egressReportItemsDTO.getEgresosFormatted().get(j).getAccount()));
                                egressReportItemsDTO.getEgresosFormatted().get(j).setAccount(bancoDTO.getBeneficiario());
                            }
                        }
                        total = total + egressReportItemsDTO.getTotal();
                    }

                }


            }

        }

        egressReportDTO.setTotal(total);
        String a = "a";
        return egressReportDTO;
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompanyAndAccount(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId, String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);

        return new PageImpl<>(egressRepository.findByDatesBetweenAndCompanyAndAccount(zd_initialTime, zd_finalTime, companyId, accountId))
            .map(egressMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> getEgressToPay(Pageable pageable, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);

        return new PageImpl<>(egressRepository.findEgressToPayByCompany(zd_finalTime, companyId))
            .map(egressMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<EgressDTO> findByDatesBetweenAndCompanyAndAccount(String initialTime, String finalTime, Long companyId, String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        return egressRepository.findByDatesBetweenAndCompanyAndAccount(zd_initialTime, zd_finalTime, companyId, accountId).stream()
            .map(egressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the egresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EgressDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all Egresses");


        return new PageImpl<>(egressRepository.findByCompanyId(companyId))
            .map(egressMapper::toDto);
    }

    /**
     * Get one egress by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public EgressDTO findOne(Long id) {
        log.debug("Request to get Egress : {}", id);
        Egress egress = egressRepository.findOne(id);
        EgressDTO egreesDTO = egressMapper.toDto(egress);
        egreesDTO.setCategoryName(egressCategoryService.findOne(Long.parseLong(egreesDTO.getCategory())).getCategory());
        egreesDTO.setPaymentMethod(egress.getPaymentMethod());
        return egreesDTO;
    }

    /**
     * Delete the  egress by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Egress : {}", id);
        egressRepository.delete(id);
    }
}
