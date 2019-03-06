package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.DocumentFileService;
//import com.lighthouse.aditum.web.rest.errors.BadRequestAlertException;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.DocumentFileDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DocumentFile.
 */
@RestController
@RequestMapping("/api")
public class DocumentFileResource {

    private final Logger log = LoggerFactory.getLogger(DocumentFileResource.class);

    private static final String ENTITY_NAME = "documentFile";

    private final DocumentFileService documentFileService;

    public DocumentFileResource(DocumentFileService documentFileService) {
        this.documentFileService = documentFileService;
    }

    /**
     * POST  /document-files : Create a new documentFile.
     *
     * @param documentFileDTO the documentFileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentFileDTO, or with status 400 (Bad Request) if the documentFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/document-files")
    @Timed
    public ResponseEntity<DocumentFileDTO> createDocumentFile(@RequestBody DocumentFileDTO documentFileDTO) throws URISyntaxException {
        log.debug("REST request to save DocumentFile : {}", documentFileDTO);
        if (documentFileDTO.getId() != null) {
//            throw new BadRequestAlertException("A new documentFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentFileDTO result = documentFileService.save(documentFileDTO);
        return ResponseEntity.created(new URI("/api/document-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /document-files : Updates an existing documentFile.
     *
     * @param documentFileDTO the documentFileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documentFileDTO,
     * or with status 400 (Bad Request) if the documentFileDTO is not valid,
     * or with status 500 (Internal Server Error) if the documentFileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/document-files")
    @Timed
    public ResponseEntity<DocumentFileDTO> updateDocumentFile(@RequestBody DocumentFileDTO documentFileDTO) throws URISyntaxException {
        log.debug("REST request to update DocumentFile : {}", documentFileDTO);
        if (documentFileDTO.getId() == null) {
            return createDocumentFile(documentFileDTO);
        }
        DocumentFileDTO result = documentFileService.save(documentFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /document-files : get all the documentFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documentFiles in body
     */
    @GetMapping("/document-files")
    @Timed
    public ResponseEntity<List<DocumentFileDTO>> getAllDocumentFiles(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of DocumentFiles");
        Page<DocumentFileDTO> page = documentFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/document-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /document-files/:id : get the "id" documentFile.
     *
     * @param id the id of the documentFileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentFileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/document-files/{id}")
    @Timed
    public ResponseEntity<DocumentFileDTO> getDocumentFile(@PathVariable Long id) {
        log.debug("REST request to get DocumentFile : {}", id);
        DocumentFileDTO documentFileDTO = documentFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(documentFileDTO));
    }

    /**
     * DELETE  /document-files/:id : delete the "id" documentFile.
     *
     * @param id the id of the documentFileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/document-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumentFile(@PathVariable Long id) {
        log.debug("REST request to delete DocumentFile : {}", id);
        documentFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
