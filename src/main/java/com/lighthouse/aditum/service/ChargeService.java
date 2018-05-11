package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Balance;
import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.repository.ChargeRepository;
import com.lighthouse.aditum.repository.HouseRepository;
import com.lighthouse.aditum.service.dto.BalanceDTO;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.mapper.ChargeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


/**
 * Service Implementation for managing Charge.
 */
@Service
@Transactional
public class ChargeService {

    private final Logger log = LoggerFactory.getLogger(ChargeService.class);

    private final ChargeRepository chargeRepository;
    private final BalanceService balanceService;
    private final ChargeMapper chargeMapper;

    public ChargeService(ChargeRepository chargeRepository, ChargeMapper chargeMapper,BalanceService balanceService) {
        this.chargeRepository = chargeRepository;
        this.chargeMapper = chargeMapper;
        this.balanceService = balanceService;
    }

    /**
     * Save a charge.
     *
     * @param chargeDTO the entity to save
     * @return the persisted entity
     */
    public ChargeDTO save(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        charge = chargeRepository.save(charge);
        BalanceDTO balanceDTO= balanceService.findOneByHouse(chargeDTO.getHouseId());
        switch (chargeDTO.getType()){
            case 1:
                int newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance())-Integer.parseInt(chargeDTO.getAmmount());
                balanceDTO.setMaintenance(newMaintBalance+"");
                break;
            case 2:
                int newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary())-Integer.parseInt(chargeDTO.getAmmount());
                balanceDTO.setExtraordinary(newExtraBalance+"");
                break;
            case 3:
                int newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas())-Integer.parseInt(chargeDTO.getAmmount());
                balanceDTO.setCommonAreas(newCommonBalance+"");
             break;
        }
        balanceService.save(balanceDTO);
        return chargeMapper.toDto(charge);
    }

    /**
     *  Get all the charges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChargeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Charges");
        return chargeRepository.findAll(pageable)
            .map(chargeMapper::toDto);
    }

    /**
     *  Get one charge by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ChargeDTO findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        Charge charge = chargeRepository.findOne(id);
        return chargeMapper.toDto(charge);
    }

    /**
     *  Delete the  charge by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Charge : {}", id);
        chargeRepository.delete(id);
    }
}
