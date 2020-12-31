package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Balance;
import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.BalanceRepository;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
import com.lighthouse.aditum.service.dto.BalanceDTO;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.mapper.BalanceMapper;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Balance.
 */
@Service
@Transactional
public class BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceService.class);

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    private final HouseMapper houseMapper;

    private final AdministrationConfigurationService administrationConfigurationService;

    private final HouseService houseService;


    public BalanceService(HouseMapper houseMapper, @Lazy HouseService houseService, AdministrationConfigurationService administrationConfigurationService, BalanceRepository balanceRepository, BalanceMapper balanceMapper) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
        this.administrationConfigurationService = administrationConfigurationService;
        this.houseService = houseService;
        this.houseMapper = houseMapper;
    }

    /**
     * Save a balance.
     *
     * @param balanceDTO the entity to save
     * @return the persisted entity
     */
    public BalanceDTO save(BalanceDTO balanceDTO) {
        log.debug("Request to save Balance : {}", balanceDTO);
        Balance balance = balanceMapper.toEntity(balanceDTO);
        balance.setHouse(balanceMapper.houseFromId(balanceDTO.getHouseId()));
        balance.setCompany(houseMapper.companyFromId(balanceDTO.getCompanyId()));
        balance = balanceRepository.save(balance);
        return balanceMapper.toDto(balance);
    }

    /**
     *  Get all the balances.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Balances");
        return balanceRepository.findAll(pageable)
            .map(balanceMapper::toDto);
    }


    public void formatCompany(Long companyId,int progress,int total) {
        List<HouseDTO> hs = this.houseService.findWithBalance(companyId).getContent();
        for (int i = 0; i < hs.size(); i++) {
            HouseDTO h = hs.get(i);
            BalanceDTO b = new BalanceDTO();
            b.setHouseId(h.getId());
            b.setOthers("0");
            b.setExtraordinary("0");
            b.setWaterCharge("0");
            b.setCommonAreas("0");
            if(Double.parseDouble(h.getBalance().getMaintenance())>0){
                b.setMaintenance(setDouble(h.getBalance().getMaintenance()));
            }else{
                b.setMaintenance("0");
            }
            b.setMulta("0");
            b.setCompanyId(companyId);
            this.save(b);
        }
    }

    public String setDouble(String a ){
        return a.equals("-0.0")?"0":a;
    }

    /**
     *  Get one balance by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BalanceDTO findOne(Long id) {
        log.debug("Request to get Balance : {}", id);
        Balance balance = balanceRepository.findOne(id);
        return balanceMapper.toDto(balance);
    }

    @Transactional(readOnly = true)
    public BalanceDTO findOneByHouse(Long id) {
        log.debug("Request to get Balance : {}", id);
        Balance balance = balanceRepository.findOneByHouseId(id);
        BalanceDTO b = balanceMapper.toDto(balance);
        return b;
    }

    /**
     *  Delete the  balance by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Balance : {}", id);
        balanceRepository.delete(id);
    }
}
