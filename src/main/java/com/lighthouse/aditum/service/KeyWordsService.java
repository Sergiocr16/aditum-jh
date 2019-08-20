package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.KeyWords;
import com.lighthouse.aditum.repository.KeyWordsRepository;
import com.lighthouse.aditum.service.dto.KeyWordsDTO;
import com.lighthouse.aditum.service.mapper.KeyWordsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing KeyWords.
 */
@Service
@Transactional
public class KeyWordsService {

    private final Logger log = LoggerFactory.getLogger(KeyWordsService.class);

    private final KeyWordsRepository keyWordsRepository;

    private final KeyWordsMapper keyWordsMapper;

    public KeyWordsService(KeyWordsRepository keyWordsRepository, KeyWordsMapper keyWordsMapper) {
        this.keyWordsRepository = keyWordsRepository;
        this.keyWordsMapper = keyWordsMapper;
    }

    /**
     * Save a keyWords.
     *
     * @param keyWordsDTO the entity to save
     * @return the persisted entity
     */
    public KeyWordsDTO save(KeyWordsDTO keyWordsDTO) {
        log.debug("Request to save KeyWords : {}", keyWordsDTO);
        KeyWords keyWords = keyWordsMapper.toEntity(keyWordsDTO);
        keyWords = keyWordsRepository.save(keyWords);
        return keyWordsMapper.toDto(keyWords);
    }

    /**
     * Get all the keyWords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<KeyWordsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all KeyWords");
        return keyWordsRepository.findByDeleted(pageable,0)
            .map(keyWordsMapper::toDto);
    }

    /**
     * Get one keyWords by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public KeyWordsDTO findOne(Long id) {
        log.debug("Request to get KeyWords : {}", id);
        KeyWords keyWords = keyWordsRepository.findOne(id);
        return keyWordsMapper.toDto(keyWords);
    }

    /**
     * Delete the keyWords by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete KeyWords : {}", id);
        keyWordsRepository.delete(id);
    }
}
