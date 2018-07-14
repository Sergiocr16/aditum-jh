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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
                findChargesPerHouseAndYear(houses.get(i).getId(),year)));

            houseYearCollection.add(houseYearCollectionDTO);
        }
        return houseYearCollection;
    }

    private List<ChargeDTO> findChargesPerHouseAndYear(Long houseId,String year){
        String initialTime = year+"-01-01T00:00:00-06:00";
        String finalTime = year+"-12-31T00:00:00-06:00";
        return this.chargeService.findAllByHouseAndBetweenDate(houseId,initialTime,finalTime).getContent();
    }

    private List<MensualCollectionDTO> obtainColectionsPerMonth(Long houseId,List<ChargeDTO> houseCharges){
        List<MensualCollectionDTO> collectionsPerHouse = new ArrayList<>();
        String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Setiembre","Octubre","Noviembre","Diciembre"};
        for (int i = 1; i <= 12; i++) {
            MensualCollectionDTO monthCollection = new MensualCollectionDTO();
            int monthValue = 0;
            monthCollection = defineAmmountPerCharge(
                                    filterChargesPerMonth(houseCharges,i),
                                    monthCollection);
            monthCollection.setMonth(months[i-1]);
            monthCollection.setBgColor(defineCollectionColor(monthCollection.getMensualBalance(),filterChargesPerMonth(houseCharges,i)));
            collectionsPerHouse.add(monthCollection);
        }
        return collectionsPerHouse;
    }

     private String defineCollectionColor(int ammount,List<ChargeDTO> houseCharges){
        String color = "";
        int noPayedAmmount = houseCharges.stream().filter(o -> o.getState() == 1).mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
        int totalCharges = houseCharges.stream().mapToInt(o -> Integer.parseInt(o.getAmmount())).sum();
        int finalAmmount = totalCharges-noPayedAmmount;
        if(totalCharges==noPayedAmmount){
            color = "hsl(0, 100%, 86%)";
        }
        if(finalAmmount!=0){
            color = "hsl(39, 100%, 74%)";
        }
        if(finalAmmount == totalCharges){
            color = "hsl(134, 100%, 88%);";
        }
        if(ammount==0){
            color = "hsla(0, 0%, 83%, 0.46)";
        }
        String a = "";
        return color;
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
