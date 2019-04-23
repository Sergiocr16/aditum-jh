package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.domain.User;
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


/**
 * Service Implementation for managing Egress.
 */
@Service
@Transactional
public class EgressService {

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

    private final BitacoraAccionesService bitacoraAccionesService;

    private final BalanceByAccountService balanceByAccountService;

    public EgressService( AdminInfoService adminInfoService,UserService userService,BitacoraAccionesService bitacoraAccionesService, BalanceByAccountService balanceByAccountService, EgressRepository egressRepository, EgressMapper egressMapper, EgressCategoryService egressCategoryService, ProveedorService proveedorService, BancoService bancoService, HouseService houseService, @Lazy CommonAreaReservationsService commonAreaReservationsService) {
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
    }

    /**
     * Save a egress.
     *
     * @param egressDTO the entity to save
     * @return the persisted entity
     */
    public EgressDTO save(EgressDTO egressDTO) {
        log.debug("Request to save Egress : {}", egressDTO);
        Egress egress = egressMapper.toEntity(egressDTO);
        if (egress.getPaymentDate() != null) {
            ZonedDateTime n = ZonedDateTime.now();
            egress.setPaymentDate(egress.getPaymentDate().withHour(n.getHour()).withMinute(n.getMinute()).withSecond(n.getSecond()));
            this.balanceByAccountService.modifyBalancesInPastEgress(egress);

        }
        egress.setDeleted(egressDTO.getDeleted());
        egress = egressRepository.save(egress);

        String concepto = "";
        if(egressDTO.getId()==null){
            concepto = "Registro de nuevo egreso: " + egressDTO.getConcept() + " por " + formatColonesD(Integer.parseInt( egressDTO.getTotal()))   + " colones";
        }else if(egressDTO.getId()!=null && egressDTO.getDeleted()==0){
            concepto = "Pago de un egreso: " + egressDTO.getConcept() + " por " + formatColonesD(Integer.parseInt( egressDTO.getTotal())) + " colones";
        }
        else if(egressDTO.getId()!=null && egressDTO.getDeleted()==1){
            concepto = "Eliminación de un egreso: " + egressDTO.getConcept() + " por " + formatColonesD(Integer.parseInt( egressDTO.getTotal())) + " colones";
        }
        bitacoraAccionesService.save(createBitacoraAcciones(concepto,1, "egress-detail","Egresos",egress.getId(),egress.getCompany().getId()));

        return egressMapper.toDto(egress);
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompany(Pageable pageable, String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        Page<Egress> result = egressRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }

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
    public Page<EgressDTO> findByCobroDatesBetweenAndCompany(Pageable pageable, String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTimeNoFormatted = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_initialTime = zd_initialTimeNoFormatted.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        Page<Egress> result = egressRepository.findByCobroDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
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
                        EgressReportItemsDTO egressReportItemsDTO = new EgressReportItemsDTO(proveedorDTO.getEmpresa(), egresses, camposParts);
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
                        EgressReportItemsDTO egressReportItemsDTO = new EgressReportItemsDTO("Devoluciones de dinero", egresses, camposParts);
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
        Page<Egress> result = egressRepository.findByDatesBetweenAndCompanyAndAccount(pageable, zd_initialTime, zd_finalTime, companyId, accountId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> getEgressToPay(Pageable pageable, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_finalTimeNoFormatted = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        ZonedDateTime zd_finalTime = zd_finalTimeNoFormatted.withMinute(59).withHour(23).withSecond(59);
        Page<Egress> result = egressRepository.findEgressToPayByCompany(pageable, zd_finalTime, companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
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
        Page<Egress> result = egressRepository.findByCompanyId(pageable, companyId);
        return result.map(egress -> egressMapper.toDto(egress));
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
