package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Collection;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.CollectionRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CollectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;


/**
 * Service Implementation for managing Collection.
 */
@Service
@Transactional
public class CollectionService {

    private final Logger log = LoggerFactory.getLogger(CollectionService.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    private final HouseService houseService;

    private final ChargeService chargeService;
    private Locale locale = new Locale("es", "CR");
    private final CompanyConfigurationService companyConfigurationService;

    public CollectionService(CompanyConfigurationService companyConfigurationService,ChargeService chargeService, HouseService houseService, CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.houseService = houseService;
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
        this.chargeService = chargeService;
        this.companyConfigurationService = companyConfigurationService;
    }

    /**
     * Save a collection.
     *
     * @param collectionDTO the entity to save
     * @return the persisted entity
     */
    public CollectionDTO save(CollectionDTO collectionDTO) {
        log.debug("Request to save Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection.setHouse(collectionMapper.houseFromId(collectionDTO.getHouseId()));
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    /**
     * Get all the collections.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CollectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Collections");
        return collectionRepository.findAll(pageable)
            .map(collectionMapper::toDto);
    }


    @Transactional(readOnly = true)
    public List<HouseYearCollectionDTO> findCollectionsByYear(String currency,Long companyId, String year) {
        log.debug("Request to get all Collections by year");
        List<HouseDTO> houses = this.houseService.findAll(companyId).getContent();
        List<HouseYearCollectionDTO> houseYearCollection = new ArrayList<>();
        for (int i = 0; i < houses.size(); i++) {
            HouseYearCollectionDTO houseYearCollectionDTO = new HouseYearCollectionDTO();
            houseYearCollectionDTO.setHouseNumber(houses.get(i).getHousenumber());
            houseYearCollectionDTO.setYearCollection(
                obtainColectionsPerMonth(currency,companyId,houses.get(i).getId(),
                    findChargesPerHouseAndYear(companyId,currency,houses.get(i).getId(), year), year));
            houseYearCollection.add(houseYearCollectionDTO);
        }
        return houseYearCollection;
    }

    private List<ChargeDTO> findChargesPerHouseAndYear(Long companyId, String currency,Long houseId, String year) {
        ZonedDateTime initialTime = ZonedDateTime.now().withYear(Integer.parseInt(year)).withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime finalTime = ZonedDateTime.now().withYear(Integer.parseInt(year)).withMonth(12).withDayOfMonth(31).withHour(23).withMinute(59).withSecond(59);
        if(houseId==137){
            String a = "";
        }
        List<ChargeDTO> b = this.chargeService.findAllByHouseAndBetweenDateCollection(currency,houseId,initialTime, finalTime).getContent();
        return b;
    }

    private List<MensualCollectionDTO> obtainColectionsPerMonth(String currency,Long companyId,Long houseId, List<ChargeDTO> houseCharges, String year) {
        List<MensualCollectionDTO> collectionsPerHouse = new ArrayList<>();
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"};
       if(houseId==137){
           String a = "";
       }
        for (int i = 1; i <= 12; i++) {
            MensualCollectionDTO monthCollection = new MensualCollectionDTO();
            double monthValue = 0;
            monthCollection = defineAmmountPerCharge(
                currency,
                houseId,
                filterChargesPerMonth(houseCharges, i),
                monthCollection);
            monthCollection.setMonth(months[i - 1]);
            monthCollection.setStyle(defineCollectionStyle(currency,companyId,monthCollection, i, Integer.parseInt(year), filterChargesPerMonth(houseCharges, i)));
            collectionsPerHouse.add(monthCollection);
        }
        return collectionsPerHouse;
    }

    private String defineCollectionStyle(String currency,Long companyId,MensualCollectionDTO mensualCollection, int month, int year, List<ChargeDTO> houseCharges) {
        String style = "";
        double ammount = mensualCollection.getMensualBalance();
        double noPayedAmmount = houseCharges.stream().filter(o -> o.getState() == 1).mapToDouble(o -> Double.parseDouble(o.getAmmount())).sum();
        double totalCharges = houseCharges.stream().mapToDouble(o -> o.getTotal()).sum();
        double finalAmmount = totalCharges - noPayedAmmount;
        mensualCollection.setDebt(currency, noPayedAmmount);
        mensualCollection.setPayedAmmount(currency,finalAmmount);
        if (noPayedAmmount > 0) {
            mensualCollection.setState(1);
        } else {
            mensualCollection.setState(0);
        }
        if (totalCharges == noPayedAmmount) {
            style = "background-color:#FFB8B8;";
        }
        if (finalAmmount != 0) {
            style = "background-color:#FFD17A;";
        }
        if (finalAmmount == totalCharges) {
            style = "background-color:#C2FFD0;";
        }
        if (ammount == 0) {
            style = "background-color:#e2e2e2;";
        }
        if (month > ZonedDateTime.now().getMonth().getValue() && ZonedDateTime.now().getYear() == year) {
            style += "opacity: 0.6;border - right:1 px solid #002 !important;border - left:1 px solid #B3B3B3 !important;";
        }
        return style;
    }

    private List<ChargeDTO> filterChargesPerMonth(List<ChargeDTO> houseCharges, int montValue) {
        List<ChargeDTO> chargesPerMonth = houseCharges.stream().filter(o -> o.getDate().getMonth().getValue() == montValue).collect(Collectors.toList());
        return chargesPerMonth;
    }

    private MensualCollectionDTO defineAmmountPerCharge(String currency,Long houseId,List<ChargeDTO> houseCharges, MensualCollectionDTO mensualCollectionDTO) {
        double noPayedAmmount = 0;
        double payedAmmount = 0;
        noPayedAmmount = houseCharges.stream().filter(o -> o.getState() == 1).mapToDouble(o -> Double.parseDouble(o.getAmmount())).sum();
        payedAmmount = houseCharges.stream().filter(o -> o.getState() == 2).mapToDouble(o -> Double.parseDouble(o.getAmmount())).sum();
        double finalTotal = noPayedAmmount;
        if(houseId==147){
            String a = "";
        }
        if (noPayedAmmount > 0) {
            finalTotal = -noPayedAmmount;
        } else {
            finalTotal = payedAmmount - noPayedAmmount;
        }
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        mensualCollectionDTO.setMensualBalanceToShow(formatMoney(currency,finalTotal));
        mensualCollectionDTO.setMensualBalance(finalTotal);
        return mensualCollectionDTO;
    }

    //    double noPayedAmmount = 0;
//    double finalTotal = 0;
//    double payedAmmount = 0;
//    noPayedAmmount = houseCharges.stream().filter(o-> o.getState()==1).mapToDouble(o -> o.getLeftToPay()).sum();
//    payedAmmount = houseCharges.stream().filter(o-> o.getState()==2).mapToDouble(o -> o.getAbonado()).sum();
//    finalTotal =houseCharges.stream().mapToDouble(o -> o.getTotal()).sum();
//        if(houseId == 147 ){
//        String  a = "";
//    }
//        if (noPayedAmmount > 0) {
//        finalTotal = -noPayedAmmount;
//    } else {
//        finalTotal = payedAmmount;
//    }
//    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
//        mensualCollectionDTO.setMensualBalanceToShow(formatMoney(currency,finalTotal));
//        mensualCollectionDTO.setMensualBalance(finalTotal);
//        return mensualCollectionDTO;

    @Transactional(readOnly = true)
    public CollectionDTO findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        Collection collection = collectionRepository.findOne(id);
        return collectionMapper.toDto(collection);
    }

    /**
     * Delete the  collection by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.delete(id);
    }


    public ArrayList<DefaultersMonthDTO> getDefaulters(Long companyId, String year) {
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<HouseYearCollectionDTO> houseYearCollectionDTOS = this.findCollectionsByYear(currency,companyId, year);
        ArrayList<DefaultersMonthDTO> defaultersMonthDTOList = new ArrayList<>();
        int monthNumber = 0;
        ZonedDateTime zd_actualMonth = ZonedDateTime.now();
        int finalMonth = zd_actualMonth.getMonthValue();
        if(zd_actualMonth.getYear()!=Integer.parseInt(year)){
            finalMonth = 12;
        }
        for (int y = 0; y < finalMonth; y++) {
            DefaultersMonthDTO defaultersMonthDTO = new DefaultersMonthDTO();
            int casasConDeuda = 0;
            int casasSinDeuda = 0;
            double totalDeuda = 0;
            double totalLiquidado = 0;
            for (int h = 0; h < houseYearCollectionDTOS.size(); h++) {
                int casaConDeuda = 0;
                int casaSinDeuda = 0;
                for (int i = 0; i < houseYearCollectionDTOS.get(h).getYearCollection().size(); i++) {
                    if (monthNumber == i) {
                        MensualCollectionDTO mensualCollectionDTO = houseYearCollectionDTOS.get(h).getYearCollection().get(i);
                        if (mensualCollectionDTO.getState() == 1) {
                            casaConDeuda++;
                            totalDeuda = totalDeuda + mensualCollectionDTO.getDebt();
                        } else if (mensualCollectionDTO.getState() == 0) {
                            casaSinDeuda++;
                        }
                        totalLiquidado = totalLiquidado + Math.abs(mensualCollectionDTO.getPayedAmmount());
                    }
                }
                if(casaConDeuda>0){
                    casasConDeuda++;
                }
                if(casaSinDeuda>0){
                    casasSinDeuda++;
                }
            }
            defaultersMonthDTO.setDebt(currency,totalDeuda);
            defaultersMonthDTO.setTotal(currency,totalLiquidado);
            defaultersMonthDTO.setTotalHousesDefaulter(casasConDeuda);
            defaultersMonthDTO.setTotalHousesOnTime(casasSinDeuda);
            defaultersMonthDTOList.add(defaultersMonthDTO);
            monthNumber++;
        }
        return defaultersMonthDTOList;
    }
}
