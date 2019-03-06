package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.DocumentFile;
import com.lighthouse.aditum.repository.DocumentFileRepository;
import com.lighthouse.aditum.service.dto.DocumentFileDTO;
import com.lighthouse.aditum.service.mapper.DocumentFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing DocumentFile.
 */
@Service
@Transactional
public class DocumentFileService {

    private final Logger log = LoggerFactory.getLogger(DocumentFileService.class);

    private final DocumentFileRepository documentFileRepository;

    private final DocumentFileMapper documentFileMapper;

    public DocumentFileService(DocumentFileRepository documentFileRepository, DocumentFileMapper documentFileMapper) {
        this.documentFileRepository = documentFileRepository;
        this.documentFileMapper = documentFileMapper;
    }

    /**
     * Save a documentFile.
     *
     * @param documentFileDTO the entity to save
     * @return the persisted entity
     */
    public DocumentFileDTO save(DocumentFileDTO documentFileDTO) {
        log.debug("Request to save DocumentFile : {}", documentFileDTO);
        DocumentFile documentFile = documentFileMapper.toEntity(documentFileDTO);
        documentFile = documentFileRepository.save(documentFile);
        return documentFileMapper.toDto(documentFile);
    }

    /**
     * Get all the documentFiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DocumentFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentFiles");
        return documentFileRepository.findAll(pageable)
            .map(documentFileMapper::toDto);
    }

    /**
     * Get one documentFile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public DocumentFileDTO findOne(Long id) {
        log.debug("Request to get DocumentFile : {}", id);
        DocumentFile documentFile = documentFileRepository.findOne(id);
        return documentFileMapper.toDto(documentFile);
    }

    /**
     * Delete the documentFile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DocumentFile : {}", id);
        documentFileRepository.delete(id);
    }
}
