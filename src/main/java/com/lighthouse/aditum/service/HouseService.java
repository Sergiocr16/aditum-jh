package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.Subsidiary;
import com.lighthouse.aditum.repository.HouseRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import com.lighthouse.aditum.service.mapper.SubsidiaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service Implementation for managing House.
 */
@Service
@Transactional
public class HouseService {

    private final Logger log = LoggerFactory.getLogger(HouseService.class);

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper;

    private final BalanceService balanceService;

    private final ChargeService chargeService;

    private final PaymentService paymentService;

    private final SubsidiaryService subsidiaryService;

    private final SubsidiaryTypeService subsidiaryTypeService;

    private final SubsidiaryMapper subsidiaryMapper;

    private final CompanyConfigurationService companyConfigurationService;


    public HouseService(CompanyConfigurationService companyConfigurationService,SubsidiaryTypeService subsidiaryTypeService, SubsidiaryMapper subsidiaryMapper, SubsidiaryService subsidiaryService, @Lazy PaymentService paymentService, ChargeService chargeService, HouseRepository houseRepository, HouseMapper houseMapper, BalanceService balanceService) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
        this.balanceService = balanceService;
        this.chargeService = chargeService;
        this.paymentService = paymentService;
        this.subsidiaryService = subsidiaryService;
        this.subsidiaryMapper = subsidiaryMapper;
        this.subsidiaryTypeService = subsidiaryTypeService;
        this.companyConfigurationService = companyConfigurationService;
    }

    /**
     * Save a house.
     *
     * @param houseDTO the entity to save
     * @return the persisted entity
     */
    public HouseDTO save(HouseDTO houseDTO) {
        log.debug("Request to save House : {}", houseDTO);
        House house = houseMapper.houseDTOToHouse(houseDTO);
        house.setCodeStatus(houseDTO.getCodeStatus());
        house.loginCode(houseDTO.getLoginCode());
        house.setHasOwner(houseDTO.getHasOwner());
        house.setHousenumber(houseDTO.getHousenumber().toUpperCase());
        if (house.getId() == null) {
            house = houseRepository.save(house);
        }
        if (houseDTO.getSubsidiaries() != null) {
            Set<Subsidiary> subsidiaries = new HashSet<>();
            House finalHouse = house;
            houseDTO.getSubsidiaries().forEach(
                subsidiaryDTO -> {
                    if (subsidiaryDTO.getDeleted() == 1) {
                        if (subsidiaryDTO.getId() != null) {
                            subsidiaryService.delete(subsidiaryDTO.getId());
                        }
                    } else {
                        subsidiaryDTO.setHouseId(finalHouse.getId());
                        SubsidiaryDTO subsidiary = subsidiaryService.save(subsidiaryDTO);
                        if (subsidiary != null) {
                            Subsidiary sub = subsidiaryMapper.toEntity(subsidiary);
                            subsidiaries.add(sub);
                        }
                    }
                }
            );
            house.subsidiaries(subsidiaries);
        }
        house = houseRepository.save(house);
        HouseDTO result = houseMapper.houseToHouseDTO(house);
        return result;
    }


    /**
     * Get all the houses.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseDTO> findAll(Long companyId) {
        log.debug("Request to get all Houses");
        List<House> result = houseRepository.findByCompanyId(companyId);
        return new PageImpl<>(orderHouses(result)).map(house -> {
            HouseDTO house1 = houseMapper.houseToHouseDTO(house);
            house1.setHasOwner(house.getHasOwner());
            if (house.getHasOwner() != null) {
                house1.setHouseForRent(house.getHasOwner());
            }
            return house1;
        });
    }

    /**
     * Get all the houses.
     *
     * @return the list of entities
     */

    @Transactional(readOnly = true)
    public Page<HouseDTO> findAllFilter(Pageable pageable, Long companyId, String desocupated, String houseNumber) {
        log.debug("Request to get all Houses");
        Page<House> result;
        if (houseNumber.equals(" ")) {
            if (desocupated.equals("empty")) {
                result = houseRepository.findByCompanyId(pageable, companyId);
            } else {
                result = houseRepository.findByCompanyIdAndIsdesocupated(pageable, companyId, Integer.parseInt(desocupated));
            }
            result = new PageImpl<>(orderHouses(result.getContent()), pageable, result.getTotalElements());
        } else {
            if (desocupated.equals("empty")) {
                result = houseRepository.findByCompanyIdAndHousenumberContains(pageable, companyId, houseNumber);
            } else {
                result = houseRepository.findByCompanyIdAndIsdesocupatedAndHousenumberContains(pageable, companyId, Integer.parseInt(desocupated), houseNumber);
            }
            result = new PageImpl<>(orderHouses(result.getContent()), pageable, result.getTotalElements());
        }
        return result.map(house -> {
            HouseDTO house1 = houseMapper.houseToHouseDTO(house);
            house1.setType(this.subsidiaryTypeService.findOne(house1.getSubsidiaryTypeId()));
            house1.getSubsidiaries().forEach(subsidiaryDTO -> {
                subsidiaryDTO.setType(this.subsidiaryTypeService.findOne(subsidiaryDTO.getSubsidiaryTypeId()));
            });
            house1.setHasOwner(house.getHasOwner());
            house1.setTypeTotal(this.defineHouseSubsidiaryTotal(house1.getType(), house1.getSubsidiaries()));
            if (house1.getHasOwner() != null) {
                house1.setHouseForRent(house.getHasOwner());
            }
            return house1;
        });
    }

    private List<House> filterIfName(Page<House> houses, String houseNumber) {
        List<House> filteredList = new ArrayList<>();
        houses.getContent().forEach(h -> {
            if (h.getHousenumber().toUpperCase().contains(houseNumber.toUpperCase())) {
                filteredList.add(h);
            }
        });
        return filteredList;
    }

    public List<House> orderHouses(List<House> result) {
        List<House> onlyHousesLetters = new ArrayList<>();
        List<House> onlyHousesNumber = new ArrayList<>();
        List<House> allHouses;
        Character[] letras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-', '/', '*', '+', '!', '@', '#', '$', '%', '.', ',', '(', ')'};
        result.forEach(house -> {
            int existe = 0;
            for (int i = 0; i < letras.length; i++) {
                if (Character.toLowerCase(house.getHousenumber().charAt(0)) == (letras[i])) {
                    existe++;
                }
            }
            if (existe == 0) {
                onlyHousesNumber.add(house);
            } else {
                house.setHousenumber(house.getHousenumber().toUpperCase());
                onlyHousesLetters.add(house);
            }
        });
        Collections.sort(onlyHousesNumber, Comparator.comparing(House::getHouseNumberInt));
        Collections.sort(onlyHousesLetters, Comparator.comparing(House::getHousenumber));
        allHouses = onlyHousesNumber;
        allHouses.addAll(onlyHousesLetters);
        return allHouses;
    }

    @Transactional(readOnly = true)
    public Page<HouseDTO> findWithBalance(Long companyId) {
        log.debug("Request to get all Houses");
        List<House> result = houseRepository.findByCompanyId(companyId);
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        return new PageImpl<>(orderHouses(result)).map(house -> {
            HouseDTO house1 = houseMapper.houseToHouseDTO(house);
            house1.setBalance(this.getBalanceByHouse(currency,house1.getId()));
            return house1;
        });
    }

    @Transactional(readOnly = true)
    public Page<HouseDTO> findAllWithMaintenance(Long companyId) {
        return this.findAll(companyId);
    }

    /**
     * Get one house by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public HouseDTO findOne(Long id) {
        log.debug("Request to get House : {}", id);
        HouseDTO houseDTO = null;
        if (id != null) {
            House house = houseRepository.findOne(id);
            houseDTO = houseMapper.houseToHouseDTO(house);
            if(houseDTO.getId()!=null){
                String currency = companyConfigurationService.getByCompanyId(null, house.getCompany().getId()).getContent().get(0).getCurrency();
                houseDTO.setBalance(this.getBalanceByHouse(currency,houseDTO.getId()));
            }
            houseDTO.setCodeStatus(house.getCodeStatus());
            houseDTO.setLoginCode(house.getLoginCode());
            houseDTO.setType(this.subsidiaryTypeService.findOne(houseDTO.getSubsidiaryTypeId()));
            houseDTO.getSubsidiaries().forEach(subsidiaryDTO -> {
                subsidiaryDTO.setType(this.subsidiaryTypeService.findOne(subsidiaryDTO.getSubsidiaryTypeId()));
            });
            houseDTO.setHasOwner(house.getHasOwner());
            if (house.getHasOwner() != null) {
                houseDTO.setHouseForRent(house.getHasOwner());
            }
            houseDTO.setTypeTotal(this.defineHouseSubsidiaryTotal(houseDTO.getType(), houseDTO.getSubsidiaries()));
        }
        return houseDTO;
    }

    @Transactional(readOnly = true)
    public String getHouseNumberById(Long id) {
        return houseRepository.findHouseNumber(id);
    }


    @Transactional(readOnly = true)
    public HouseDTO findOneClean(Long id) {
        log.debug("Request to get House : {}", id);
        House house = houseRepository.findOne(id);
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);
        return houseDTO;
    }


    @Transactional(readOnly = true)
    public HouseDTO findByLoginCodde(String loginCode) {
        log.debug("Request to get House : {}", loginCode);
        House house = houseRepository.findByLoginCode(loginCode);
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);
        houseDTO.setLoginCode(house.getLoginCode());
        houseDTO.setCodeStatus(house.getCodeStatus());
        houseDTO.setHasOwner(house.getHasOwner());
           if (houseDTO.getHasOwner()!=null) {
            houseDTO.setHouseForRent(house.getHasOwner());
        }
        return houseDTO;
    }

    @Transactional(readOnly = true)
    public Integer countByCompany(Long companyid) {
        log.debug("Request to get House : {}", companyid);
        return houseRepository.countByCompanyId(companyid);
    }

    @Transactional(readOnly = true)
    public Integer countByCompanyAndDesocupated(Long companyid) {
        log.debug("Request to get House : {}", companyid);
        return houseRepository.countByCompanyIdAndIsdesocupated(companyid, 1);
    }


    @Transactional(readOnly = true)
    public HouseDTO validateHouse(String houseNumber, String extension, Long companyId) {
        House rHouse = null;
        House house = houseRepository.findByhousenumberAndCompanyId(houseNumber, companyId);
        House house1 = houseRepository.findByExtensionAndCompanyId(extension, companyId);

        if (house != null) {
            rHouse = house;
        }
        if (house1 != null) {
            rHouse = house1;
        }
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(rHouse);
        if (rHouse != null) {
            houseDTO.setCodeStatus(rHouse.getCodeStatus());
            houseDTO.setLoginCode(rHouse.getLoginCode());
        }
        return houseDTO;
    }

    @Transactional(readOnly = true)
    public HouseDTO validateUpdatedHouse(Long houseId, String houseNumber, String extension, Long companyId) {
        House rHouse = null;
        House house = houseRepository.findByhousenumberAndAndCompanyIdAndIdNot(houseNumber, companyId, houseId);
        House house1 = houseRepository.findByExtensionAndCompanyIdAndIdNot(extension, companyId, houseId);
        if (house != null) {
            rHouse = house;
        }
        if (house1 != null) {
            rHouse = house1;
        }
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(rHouse);
        if (rHouse != null) {
            houseDTO.setCodeStatus(rHouse.getCodeStatus());
            houseDTO.setLoginCode(rHouse.getLoginCode());
        }
        return houseDTO;
    }

    /**
     * Delete the  house by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete House : {}", id);
        houseRepository.delete(id);
    }

    private BalanceDTO getBalanceByHouse(String currency,Long houseId) {
        BalanceDTO balance = new BalanceDTO();
        balance.setMaintenance(this.getBalanceByType(currency,houseId, 1) + "");
        balance.setCommonAreas(this.getBalanceByType(currency,houseId, 3) + "");
        balance.setExtraordinary(this.getBalanceByType(currency,houseId, 2) + "");
        balance.setMulta(this.getBalanceByType(currency,houseId, 5) + "");
        balance.setWaterCharge(this.getBalanceByType(currency,houseId, 6) + "");
        balance.setTotal(this.getTotalBalanceByHouse(currency,houseId) + "");
        return balance;
    }

    private double getBalanceByType(String currency, Long houseId, int type) {
        ZonedDateTime today = ZonedDateTime.now().withHour(23).withSecond(59).withMinute(59);
        List<ChargeDTO> charges = this.chargeService.findBeforeDateAndHouseAndTypeAndState(currency,today, houseId, type, 1);
        double ammountCharges = charges.stream().mapToDouble(o -> o.getLeftToPay()).sum();
        if (type != 1) {
            return -ammountCharges;
        } else {
            List<PaymentDTO> payments = this.paymentService.findAdelantosByHouse(houseId);
            double ammountPaymentInAdvance = payments.stream().mapToDouble(o -> Double.parseDouble(o.getAmmountLeft())).sum();
            double total = ammountPaymentInAdvance - ammountCharges;
            return total;
        }
    }

    private double getTotalBalanceByHouse(String currency,Long houseId) {
        ZonedDateTime today = ZonedDateTime.now().withHour(23).withSecond(59).withMinute(59);
        List<ChargeDTO> chargesMaint = this.chargeService.findBeforeDateAndHouseAndTypeAndState(currency,today, houseId, 1, 1);
        double ammountChargesMaint = chargesMaint.stream().mapToDouble(o -> o.getLeftToPay()).sum();
        List<ChargeDTO> chargesExtra = this.chargeService.findBeforeDateAndHouseAndTypeAndState(currency,today, houseId, 2, 1);
        double ammountChargesExtra = chargesExtra.stream().mapToDouble(o -> o.getLeftToPay()).sum();
        List<ChargeDTO> chargesAreas = this.chargeService.findBeforeDateAndHouseAndTypeAndState(currency,today, houseId, 3, 1);
        double ammountChargesArea = chargesAreas.stream().mapToDouble(o -> o.getLeftToPay()).sum();
        List<ChargeDTO> chargesMulta = this.chargeService.findBeforeDateAndHouseAndTypeAndState(currency,today, houseId, 5, 1);
        double ammountChargesMulta = chargesMulta.stream().mapToDouble(o -> o.getLeftToPay()).sum();
        List<ChargeDTO> chargesWater = this.chargeService.findBeforeDateAndHouseAndTypeAndState(currency,today, houseId, 6, 1);
        double ammountChargesWater = chargesWater.stream().mapToDouble(o -> o.getLeftToPay()).sum();

        return -(ammountChargesArea + ammountChargesExtra + ammountChargesMaint + ammountChargesMulta + ammountChargesWater);
    }


    private SubsidiaryTypeDTO defineHouseSubsidiaryTotal(SubsidiaryTypeDTO mainType, Set<SubsidiaryDTO> subsidiaries) {
        SubsidiaryTypeDTO subsidiaryTypeDTO = new SubsidiaryTypeDTO();
        AtomicReference<Double> totalAmmount = new AtomicReference<>(Double.parseDouble(mainType.getAmmount()));
        AtomicReference<Double> jointOwnershipPercentage = new AtomicReference<>(mainType.getJointOwnershipPercentage());
        AtomicReference<Double> size = new AtomicReference<>(Double.parseDouble(mainType.getSize()));
        subsidiaries.forEach(SubsidiaryDTO -> {
            totalAmmount.updateAndGet(v -> (double) (v + Double.parseDouble(SubsidiaryDTO.getType().getAmmount())));
            jointOwnershipPercentage.updateAndGet(v -> (double) (v + SubsidiaryDTO.getType().getJointOwnershipPercentage()));
            size.updateAndGet(v -> (double) (v + Double.parseDouble(SubsidiaryDTO.getType().getSize())));
        });
        subsidiaryTypeDTO.setAmmount(round(totalAmmount.get()) + "");
        subsidiaryTypeDTO.setSize(round(size.get()) + "");
        subsidiaryTypeDTO.setJointOwnershipPercentage(round(jointOwnershipPercentage.get()));
        return subsidiaryTypeDTO;
    }

    private Double round(Double number) {
        DecimalFormat df = new DecimalFormat("0.000###");
        String result = df.format(number);
        return Double.parseDouble(result);
    }
}
