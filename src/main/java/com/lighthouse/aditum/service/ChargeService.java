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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public ChargeService(ChargeRepository chargeRepository, ChargeMapper chargeMapper, BalanceService balanceService) {
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
        BalanceDTO balanceDTO = balanceService.findOneByHouse(chargeDTO.getHouseId());
        switch (chargeDTO.getType()) {
            case 1:
                int newMaintBalance = 0;
                if (chargeDTO.getDeleted() == 1) {
                    if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(chargeDTO.getAmmount());
                    } else {
                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(chargeDTO.getAmmount());

                    }
                } else {
                    newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(chargeDTO.getAmmount());
                }
                balanceDTO.setMaintenance(newMaintBalance + "");
                break;
            case 2:
                int newExtraBalance = 0;
                if (chargeDTO.getDeleted() == 1) {
                    if (Integer.parseInt(balanceDTO.getExtraordinary()) >= 0) {
                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(chargeDTO.getAmmount());
                    } else {
                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(chargeDTO.getAmmount());
                    }
                } else {
                    newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(chargeDTO.getAmmount());
                }
                balanceDTO.setExtraordinary(newExtraBalance + "");
                break;
            case 3:
                int newCommonBalance = 0;
                if (chargeDTO.getDeleted() == 1) {
                    if (Integer.parseInt(balanceDTO.getCommonAreas()) >= 0) {
                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(chargeDTO.getAmmount());
                    } else {
                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(chargeDTO.getAmmount());

                    }
                } else {
                    newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(chargeDTO.getAmmount());
                }
                balanceDTO.setCommonAreas(newCommonBalance + "");
                break;
        }
        balanceService.save(balanceDTO);
        return chargeMapper.toDto(charge);
    }
    public ChargeDTO update(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge newCharge = chargeMapper.toEntity(chargeDTO);
        newCharge.setHouse(chargeMapper.houseFromId(chargeDTO.getHouseId()));
        Charge oldCharge = chargeRepository.getOne(chargeDTO.getId());
        if(newCharge.getAmmount().equals(oldCharge.getAmmount()) && newCharge.getDeleted()==0 && oldCharge.getType()==newCharge.getType()){
           chargeRepository.save(newCharge);
        }else {
            int newAmmount = Integer.parseInt(newCharge.getAmmount());
            int oldAmmount = Integer.parseInt(oldCharge.getAmmount());
            int ammountModyfyingBalance = 0;
            if(oldCharge.getType()!=newCharge.getType()){
                 ammountModyfyingBalance =  -newAmmount;
            }else {
                 ammountModyfyingBalance = oldAmmount - newAmmount;
            }
            BalanceDTO balanceDTO = balanceService.findOneByHouse(chargeDTO.getHouseId());

            switch (chargeDTO.getType()) {
                case 1:
                    balanceDTO = modifyIfTypeChanged(oldCharge,newCharge);
                    int newMaintBalance = 0;
                    if (chargeDTO.getDeleted() == 1) {
                        if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
                            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(chargeDTO.getAmmount());
                        } else {
                            newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(chargeDTO.getAmmount());
                        }
                    } else {
                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + ammountModyfyingBalance;

                    }

                    balanceDTO.setMaintenance(newMaintBalance + "");
                    break;
                case 2:
                    int newExtraBalance = 0;
                    balanceDTO = modifyIfTypeChanged(oldCharge,newCharge);

                    if (chargeDTO.getDeleted() == 1) {
                        if (Integer.parseInt(balanceDTO.getExtraordinary()) >= 0) {
                            newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(chargeDTO.getAmmount());
                        } else {
                            newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(chargeDTO.getAmmount());

                        }
                    } else {
                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + ammountModyfyingBalance;
                    }
                        balanceDTO.setExtraordinary(newExtraBalance+ "");
                    break;
                case 3:
                    int newCommonBalance = 0;
                    balanceDTO = modifyIfTypeChanged(oldCharge,newCharge);

                    if (chargeDTO.getDeleted() == 1) {
                        if (Integer.parseInt(balanceDTO.getCommonAreas()) >= 0) {
                            newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(chargeDTO.getAmmount());
                        } else {
                            newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(chargeDTO.getAmmount());

                        }
                    } else {
                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + ammountModyfyingBalance;
                    }
                    balanceDTO.setCommonAreas(newCommonBalance+ "");
                    break;
            }
            chargeRepository.save(newCharge);
            balanceService.save(balanceDTO);
        }
        return chargeMapper.toDto(newCharge);
    }

    private BalanceDTO modifyIfTypeChanged(Charge oldCharge, Charge newCharge){
        BalanceDTO balanceDTO = balanceService.findOneByHouse(oldCharge.getHouse().getId());

        if(oldCharge.getType()!=newCharge.getType()){
            switch (oldCharge.getType()) {
                case 1:
                    int newMaintBalance= 0;
                    if (Integer.parseInt(balanceDTO.getMaintenance()) >= 0) {
                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) - Integer.parseInt(oldCharge.getAmmount());
                    } else {
                        newMaintBalance = Integer.parseInt(balanceDTO.getMaintenance()) + Integer.parseInt(oldCharge.getAmmount());
                    }
                    balanceDTO.setMaintenance(newMaintBalance + "");
                    break;
                case 2:
                    int newExtraBalance = 0;
                    if (Integer.parseInt(balanceDTO.getExtraordinary()) >= 0) {
                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) - Integer.parseInt(oldCharge.getAmmount());
                    } else {
                        newExtraBalance = Integer.parseInt(balanceDTO.getExtraordinary()) + Integer.parseInt(oldCharge.getAmmount());
                    }
                    balanceDTO.setExtraordinary(newExtraBalance+ "");
                    break;
                case 3:
                    int newCommonBalance = 0;
                    if (Integer.parseInt(balanceDTO.getCommonAreas()) >= 0) {
                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) - Integer.parseInt(oldCharge.getAmmount());
                    } else {
                        newCommonBalance = Integer.parseInt(balanceDTO.getCommonAreas()) + Integer.parseInt(oldCharge.getAmmount());
                    }
                    balanceDTO.setCommonAreas(newCommonBalance+ "");
                    break;
            }
        }
        return balanceDTO;
    }
    /**
     *  Get all the charges.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page < ChargeDTO > findAll(Pageable pageable) {
        log.debug("Request to get all Charges");
        return chargeRepository.findAll(pageable)
            .map(chargeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page < ChargeDTO > findAllByHouse(Long houseId) {
        log.debug("Request to get all Charges");
        return new PageImpl < > (chargeRepository.findByHouseIdAndDeleted(houseId, 0))
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
