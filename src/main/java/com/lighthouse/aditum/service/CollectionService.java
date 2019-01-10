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
    public CollectionService(ChargeService chargeService, HouseService houseService,CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.houseService = houseService;
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
        this.chargeService = chargeService;
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
     *  Get all the collections.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CollectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Collections");
        return collectionRepository.findAll(pageable)
            .map(collectionMapper::toDto);
    }


    @Transactional(readOnly = true)
    public List<HouseYearCollectionDTO> findCollectionsByYear(Long companyId, String year) {
        log.debug("Request to get all Collections by year");
        List<HouseDTO> houses = this.houseService.findAll(companyId).getContent();
        List<HouseYearCollectionDTO> houseYearCollection = new ArrayList<>();
        for (int i = 0; i < houses.size(); i++) {
            HouseYearCollectionDTO houseYearCollectionDTO = new HouseYearCollectionDTO();
            houseYearCollectionDTO.setHouseNumber(houses.get(i).getHousenumber());
            houseYearCollectionDTO.setYearCollection(
                obtainColectionsPerMonth(houses.get(i).getId(),
                findChargesPerHouseAndYear(houses.get(i).getId(),year),year));

            houseYearCollection.add(houseYearCollectionDTO);
        }
        return houseYearCollection;
    }

    private List<ChargeDTO> findChargesPerHouseAndYear(Long houseId,String year){
        String initialTime = year+"-01-01T00:00:00-06:00";
        String finalTime = year+"-12-31T00:00:00-06:00";
        return this.chargeService.findAllByHouseAndBetweenDate(houseId,initialTime,finalTime).getContent();
    }

    private List<MensualCollectionDTO> obtainColectionsPerMonth(Long houseId,List<ChargeDTO> houseCharges,String year){
        List<MensualCollectionDTO> collectionsPerHouse = new ArrayList<>();
        String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Setiembre","Octubre","Noviembre","Diciembre"};
        for (int i = 1; i <= 12; i++) {
            MensualCollectionDTO monthCollection = new MensualCollectionDTO();
            int monthValue = 0;
            monthCollection = defineAmmountPerCharge(
                                    filterChargesPerMonth(houseCharges,i),
                                    monthCollection);
            monthCollection.setMonth(months[i-1]);
            monthCollection.setStyle(defineCollectionStyle(monthCollection,i,Integer.parseInt(year),filterChargesPerMonth(houseCharges,i)));
            collectionsPerHouse.add(monthCollection);
        }
        return collectionsPerHouse;
    }

     private String defineCollectionStyle(MensualCollectionDTO mensualCollection,int month,int year,List<ChargeDTO> houseCharges){
        String style = "";
         int ammount = mensualCollection.getMensualBalance();
        int noPayedAmmount = houseCharges.stream().filter(o -> o.getState() == 1).mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
        int totalCharges = houseCharges.stream().mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
        int finalAmmount = totalCharges-noPayedAmmount;
        if(totalCharges==noPayedAmmount){
            style = "background-color:#FFB8B8;";
        }
        if(finalAmmount!=0){
            style = "background-color:#FFD17A;";
        }
        if(finalAmmount == totalCharges){
            style = "background-color:#C2FFD0;";
        }
        if(ammount==0){
            style = "background-color:#F7F7F7;";
        }

        if(month> ZonedDateTime.now().getMonth().getValue() && ZonedDateTime.now().getYear()==year) {
            style += "opacity: 0.6;border - right:1 px solid #002 !important;border - left:1 px solid #B3B3B3 !important;";
        }
        return style;
     }

     private List<ChargeDTO> filterChargesPerMonth(List<ChargeDTO> houseCharges,int montValue){
         List<ChargeDTO> chargesPerMonth= houseCharges.stream().filter(o -> o.getDate().getMonth().getValue() == montValue).collect(Collectors.toList());
         String a = "";
         return chargesPerMonth;
     }
    private MensualCollectionDTO defineAmmountPerCharge(List<ChargeDTO> houseCharges,MensualCollectionDTO mensualCollectionDTO){
        int noPayedAmmount = 0;
        int payedAmmount = 0;
        noPayedAmmount= houseCharges.stream().filter(o -> o.getState() == 1).mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
        payedAmmount= houseCharges.stream().filter(o -> o.getState() == 2).mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
        int finalTotal = noPayedAmmount;
        if(noPayedAmmount>0){
            finalTotal = -finalTotal;
        }else{
            finalTotal = payedAmmount;
        }
        DecimalFormat format = new DecimalFormat("₡#,##0.00;₡-#,##0.00");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(this.locale);
        mensualCollectionDTO.setMensualBalanceToShow(format.format((double)finalTotal));
        mensualCollectionDTO.setMensualBalance(finalTotal);
        return mensualCollectionDTO;
    }

    @Transactional(readOnly = true)
    public CollectionDTO findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        Collection collection = collectionRepository.findOne(id);
        return collectionMapper.toDto(collection);
    }

    /**
     *  Delete the  collection by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.delete(id);
    }
}
