package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.domain.Egress;
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

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


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

    private final HouseService houseService;

    private final CommonAreaReservationsService commonAreaReservationsService;


    private final BalanceByAccountService balanceByAccountService;

    public EgressService(BalanceByAccountService balanceByAccountService, EgressRepository egressRepository, EgressMapper egressMapper, EgressCategoryService egressCategoryService, ProveedorService proveedorService, BancoService bancoService, HouseService houseService, @Lazy CommonAreaReservationsService commonAreaReservationsService) {
        this.egressRepository = egressRepository;
        this.egressMapper = egressMapper;
        this.egressCategoryService = egressCategoryService;
        this.proveedorService = proveedorService;
        this.bancoService = bancoService;
        this.balanceByAccountService = balanceByAccountService;
        this.commonAreaReservationsService = commonAreaReservationsService;
        this.houseService = houseService;
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
            ZonedDateTime n = ZonedDateTime.now().plusHours(5);
            egress.setPaymentDate(egress.getPaymentDate().withHour(n.getHour()).withMinute(n.getMinute()).withSecond(n.getSecond()));
            this.balanceByAccountService.modifyBalancesInPastEgress(egress);

        }
        egress = egressRepository.save(egress);
        return egressMapper.toDto(egress);
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompany(Pageable pageable, String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        Page<Egress> result = egressRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompany(String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        return new PageImpl<>(egressRepository.findByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId))
            .map(egressMapper::toDto);

    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findPaymentEgressByDatesBetweenAndCompany(String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        return new PageImpl<>(egressRepository.findPaymentEgressByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId))
            .map(egressMapper::toDto);

    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByCobroDatesBetweenAndCompany(Pageable pageable, String initialTime, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        Page<Egress> result = egressRepository.findByCobroDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }

    @Transactional(readOnly = true)
    public EgressReportDTO egressReport(Pageable pageable, String initialTime, String finalTime, Long companyId, String selectedProveedors, String selectedCampos) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        String[] proveedorsParts = selectedProveedors.split(",");
        String[] camposParts = selectedCampos.split(",");
        EgressReportDTO egressReportDTO = new EgressReportDTO();
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
                    }
                } else {
                    List<EgressDTO> egresses = egressRepository.findDevolutionsBetweenDatesAndCompany(zd_initialTime, zd_finalTime, companyId).stream()
                        .map(egressMapper::toDto)
                        .collect(Collectors.toCollection(LinkedList::new));
                    if (egresses.size() > 0) {
                        EgressReportItemsDTO egressReportItemsDTO = new EgressReportItemsDTO("Devoluciones de dinero", egresses, camposParts);
                        egressReportDTO.getEgressByProveedor().add(egressReportItemsDTO);
                        for (int j = 0; j < egressReportItemsDTO.getEgresosFormatted().size(); j++) {
                            CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsService.findOne(egressReportItemsDTO.getEgresosFormatted().get(j).getId());
                            HouseDTO houseDTO = houseService.findOne(commonAreaReservationsDTO.getHouseId());
                            egressReportItemsDTO.getEgresosFormatted().get(j).setFolio("N/A");


                            egressReportItemsDTO.getEgresosFormatted().get(j).setBillNumber("N/A");
                            egressReportItemsDTO.getEgresosFormatted().get(j).setConcept("Devolución por uso de áreas comunes - Filial " + houseDTO.getHousenumber());
                            if (egressReportItemsDTO.getEgresosFormatted().get(j).getAccount() != null) {
                                BancoDTO bancoDTO = bancoService.findOne(Long.parseLong(egressReportItemsDTO.getEgresosFormatted().get(j).getAccount()));
                                egressReportItemsDTO.getEgresosFormatted().get(j).setAccount(bancoDTO.getBeneficiario());
                            }
                        }
                    }
                }


            }

        }
        String a = "a";
        return egressReportDTO;
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompanyAndAccount(Pageable pageable, String initialTime, String finalTime, Long companyId, String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        Page<Egress> result = egressRepository.findByDatesBetweenAndCompanyAndAccount(pageable, zd_initialTime, zd_finalTime, companyId, accountId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> getEgressToPay(Pageable pageable, String finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
        Page<Egress> result = egressRepository.findEgressToPayByCompany(pageable, zd_finalTime, companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }

    @Transactional(readOnly = true)
    public List<EgressDTO> findByDatesBetweenAndCompanyAndAccount(String initialTime, String finalTime, Long companyId, String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime + "[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime + "[America/Regina]").replace("00:00:00", "23:59:59"));
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
