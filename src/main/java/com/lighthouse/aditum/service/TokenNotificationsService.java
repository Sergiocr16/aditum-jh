package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Resident;
import com.lighthouse.aditum.domain.TokenNotifications;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.repository.TokenNotificationsRepository;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.dto.TokenNotificationsDTO;
import com.lighthouse.aditum.service.dto.UserDTO;
import com.lighthouse.aditum.service.mapper.TokenNotificationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TokenNotifications.
 */
@Service
@Transactional
public class TokenNotificationsService {

    private final Logger log = LoggerFactory.getLogger(TokenNotificationsService.class);

    private final TokenNotificationsRepository tokenNotificationsRepository;

    private final TokenNotificationsMapper tokenNotificationsMapper;



    public TokenNotificationsService(UserService userService, ResidentService residentService, TokenNotificationsRepository tokenNotificationsRepository, TokenNotificationsMapper tokenNotificationsMapper) {
        this.tokenNotificationsRepository = tokenNotificationsRepository;
        this.tokenNotificationsMapper = tokenNotificationsMapper;

    }

    /**
     * Save a tokenNotifications.
     *
     * @param tokenNotificationsDTO the entity to save
     * @return the persisted entity
     */
    public TokenNotificationsDTO save(TokenNotificationsDTO tokenNotificationsDTO) {
        log.debug("Request to save TokenNotifications : {}", tokenNotificationsDTO);
        TokenNotifications tokenNotifications = tokenNotificationsMapper.toEntity(tokenNotificationsDTO);
        tokenNotifications = tokenNotificationsRepository.save(tokenNotifications);
        return tokenNotificationsMapper.toDto(tokenNotifications);
    }



    public TokenNotificationsDTO saveAtLogin(Long userId, String token) {
        TokenNotifications tokenNotifications = this.findByToken(token);
        if (tokenNotifications != null) {
            TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsMapper.toDto(tokenNotifications);
            tokenNotificationsDTO.setUserId(userId);
            tokenNotifications = tokenNotificationsMapper.toEntity(tokenNotificationsDTO);
            tokenNotifications = tokenNotificationsRepository.save(tokenNotifications);
        }else{
            TokenNotificationsDTO tokenNotificationsDTO = new TokenNotificationsDTO();
            tokenNotificationsDTO.setUserId(userId);
            tokenNotificationsDTO.setToken(token);
            tokenNotifications = tokenNotificationsMapper.toEntity(tokenNotificationsDTO);
            tokenNotifications = tokenNotificationsRepository.save(tokenNotifications);
        }
        return tokenNotificationsMapper.toDto(tokenNotifications);
    }





    public TokenNotificationsDTO saveAtLogout(Long userId, String token) {
        TokenNotifications tokenNotifications = this.findByToken(token);
        if (tokenNotifications != null) {
            TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsMapper.toDto(tokenNotifications);
            this.delete(tokenNotificationsDTO.getId());
            return tokenNotificationsMapper.toDto(tokenNotifications);
        }
        return null;
    }


    public TokenNotifications findByToken(String token) {
        TokenNotifications tokenNotifications = tokenNotificationsRepository.findByToken(token);
        return tokenNotifications;
    }

    /**
     * Get all the tokenNotifications.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TokenNotificationsDTO> findAll() {
        log.debug("Request to get all TokenNotifications");
        return tokenNotificationsRepository.findAll().stream()
            .map(tokenNotificationsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<String> findAllByUserId(Long userId) {
        log.debug("Request to get all TokenNotifications");
        return tokenNotificationsRepository.findDistinctByUserId(userId);
    }

    /**
     * Get one tokenNotifications by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TokenNotificationsDTO findOne(Long id) {
        log.debug("Request to get TokenNotifications : {}", id);
        TokenNotifications tokenNotifications = tokenNotificationsRepository.findOne(id);
        return tokenNotificationsMapper.toDto(tokenNotifications);
    }

    /**
     * Delete the tokenNotifications by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TokenNotifications : {}", id);
        tokenNotificationsRepository.delete(id);
    }
}
