package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.KeyWordsService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.KeyWordsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing KeyWords.
 */
@RestController
@RequestMapping("/api")
public class KeyWordsResource {

    private final Logger log = LoggerFactory.getLogger(KeyWordsResource.class);

    private static final String ENTITY_NAME = "keyWords";

    private final KeyWordsService keyWordsService;

    public KeyWordsResource(KeyWordsService keyWordsService) {
        this.keyWordsService = keyWordsService;
    }

    /**
     * POST  /key-words : Create a new keyWords.
     *
     * @param keyWordsDTO the keyWordsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new keyWordsDTO, or with status 400 (Bad Request) if the keyWords has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/key-words")
    @Timed
    public ResponseEntity<KeyWordsDTO> createKeyWords(@Valid @RequestBody KeyWordsDTO keyWordsDTO) throws URISyntaxException {
        log.debug("REST request to save KeyWords : {}", keyWordsDTO);
        if (keyWordsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);

        }
        KeyWordsDTO result = keyWordsService.save(keyWordsDTO);
        return ResponseEntity.created(new URI("/api/key-words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /key-words : Updates an existing keyWords.
     *
     * @param keyWordsDTO the keyWordsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated keyWordsDTO,
     * or with status 400 (Bad Request) if the keyWordsDTO is not valid,
     * or with status 500 (Internal Server Error) if the keyWordsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/key-words")
    @Timed
    public ResponseEntity<KeyWordsDTO> updateKeyWords(@Valid @RequestBody KeyWordsDTO keyWordsDTO) throws URISyntaxException {
        log.debug("REST request to update KeyWords : {}", keyWordsDTO);
        if (keyWordsDTO.getId() == null) {
            return createKeyWords(keyWordsDTO);
        }
        KeyWordsDTO result = keyWordsService.save(keyWordsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, keyWordsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /key-words : get all the keyWords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of keyWords in body
     */
    @GetMapping("/key-words")
    @Timed
    public ResponseEntity<List<KeyWordsDTO>> getAllKeyWords(Pageable pageable) throws URISyntaxException  {
        log.debug("REST request to get a page of KeyWords");
        Page<KeyWordsDTO> page = keyWordsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/key-words");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /key-words/:id : get the "id" keyWords.
     *
     * @param id the id of the keyWordsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the keyWordsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/key-words/{id}")
    @Timed
    public ResponseEntity<KeyWordsDTO> getKeyWords(@PathVariable Long id) {
        log.debug("REST request to get KeyWords : {}", id);
        KeyWordsDTO keyWordsDTO = keyWordsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(keyWordsDTO));
    }

    /**
     * DELETE  /key-words/:id : delete the "id" keyWords.
     *
     * @param id the id of the keyWordsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/key-words/{id}")
    @Timed
    public ResponseEntity<Void> deleteKeyWords(@PathVariable Long id) {
        log.debug("REST request to delete KeyWords : {}", id);
        keyWordsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
