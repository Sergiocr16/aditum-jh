package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.NoteService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.NoteDTO;
import io.swagger.annotations.ApiParam;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Note.
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    private final NoteService noteService;

    public NoteResource(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * POST  /notes : Create a new note.
     *
     * @param noteDTO the noteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new noteDTO, or with status 400 (Bad Request) if the note has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) throws URISyntaxException {
        log.debug("REST request to save Note : {}", noteDTO);
        if (noteDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new note cannot already have an ID")).body(null);
        }
        NoteDTO result = noteService.save(noteDTO);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notes : Updates an existing note.
     *
     * @param noteDTO the noteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated noteDTO,
     * or with status 400 (Bad Request) if the noteDTO is not valid,
     * or with status 500 (Internal Server Error) if the noteDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notes")
    @Timed
    public ResponseEntity<NoteDTO> updateNote(@Valid @RequestBody NoteDTO noteDTO) throws URISyntaxException {
        log.debug("REST request to update Note : {}", noteDTO);
        if (noteDTO.getId() == null) {
            return createNote(noteDTO);
        }
        NoteDTO result = noteService.save(noteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, noteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/notes")
    @Timed
    public ResponseEntity<List<NoteDTO>> getAllNotes(@ApiParam Pageable pageable,Long companyId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Notes");
        Page<NoteDTO> page = noteService.findAll(pageable,companyId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/notes/company/{companyId}/deleted/{deleted}/status/{status}")
    @Timed
    public ResponseEntity<List<NoteDTO>> getAllNotesByCompanyAndDeleted(@ApiParam Pageable pageable,@PathVariable  Long companyId,@PathVariable  int deleted,@PathVariable  int status)
        throws URISyntaxException {
        log.debug("REST request to get a page of Notes");
        Page<NoteDTO> page = noteService.findAllByCompanyAndDeletedAndStatus(pageable,companyId,deleted,status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/notes/house/{houseId}/deleted/{deleted}/status/{status}")
    @Timed
    public ResponseEntity<List<NoteDTO>> getAllNotesByHouseAndDeleted(@ApiParam Pageable pageable,@PathVariable  Long houseId,@PathVariable  int deleted,@PathVariable  int status)
        throws URISyntaxException {
        log.debug("REST request to get a page of Notes");
        Page<NoteDTO> page = noteService.findAllByHouseAndDeletedAndStatus(pageable,houseId,deleted,status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notes/:id : get the "id" note.
     *
     * @param id the id of the noteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the noteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notes/{id}")
    @Timed
    public ResponseEntity<NoteDTO> getNote(@PathVariable Long id) {
        log.debug("REST request to get Note : {}", id);
        NoteDTO noteDTO = noteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(noteDTO));
    }

    /**
     * DELETE  /notes/:id : delete the "id" note.
     *
     * @param id the id of the noteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/notes/restore/{id}")
    @Timed
    public ResponseEntity<NoteDTO> restoreNote(@PathVariable Long id) {
        log.debug("REST request to restore Note : {}", id);
        NoteDTO result = noteService.restore(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
