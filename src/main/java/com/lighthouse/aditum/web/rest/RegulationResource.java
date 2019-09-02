package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.domain.Regulation;
import com.lighthouse.aditum.service.ArticleService;
import com.lighthouse.aditum.service.RegulationService;
import com.lighthouse.aditum.service.dto.CategoriesKeyWordsQueryDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.RegulationDTO;
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
 * REST controller for managing Regulation.
 */
@RestController
@RequestMapping("/api")
public class RegulationResource {

    private final Logger log = LoggerFactory.getLogger(RegulationResource.class);

    private static final String ENTITY_NAME = "regulation";

    private final RegulationService regulationService;

    private final ArticleService articleService;

    public RegulationResource(ArticleService articleService, RegulationService regulationService) {
        this.regulationService = regulationService;
        this.articleService = articleService;
    }

    /**
     * POST  /regulations : Create a new regulation.
     *
     * @param regulationDTO the regulationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regulationDTO, or with status 400 (Bad Request) if the regulation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/regulations")
    @Timed
    public ResponseEntity<RegulationDTO> createRegulation(@Valid @RequestBody RegulationDTO regulationDTO) throws URISyntaxException {
        log.debug("REST request to save Regulation : {}", regulationDTO);
        if (regulationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new regulation cannot already have an ID")).body(null);

        }
        RegulationDTO result = regulationService.save(regulationDTO);
        return ResponseEntity.created(new URI("/api/regulations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /regulations : Updates an existing regulation.
     *
     * @param regulationDTO the regulationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regulationDTO,
     * or with status 400 (Bad Request) if the regulationDTO is not valid,
     * or with status 500 (Internal Server Error) if the regulationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/regulations")
    @Timed
    public ResponseEntity<RegulationDTO> updateRegulation(@Valid @RequestBody RegulationDTO regulationDTO) throws URISyntaxException {
        log.debug("REST request to update Regulation : {}", regulationDTO);
        if (regulationDTO.getId() == null) {
            return createRegulation(regulationDTO);
        }
        RegulationDTO result = regulationService.save(regulationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regulationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /regulations : get all the regulations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of regulations in body
     */
    @GetMapping("/regulations")
    @Timed
    public ResponseEntity<List<RegulationDTO>> getAllRegulations(Pageable pageable) throws URISyntaxException{
        log.debug("REST request to get a page of Regulations");
        Page<RegulationDTO> page = regulationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/regulations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /regulations/:id : get the "id" regulation.
     *
     * @param id the id of the regulationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regulationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/regulations/complete-regulations/{id}")
    @Timed
    public ResponseEntity<RegulationDTO> getCompleteRegulation(@PathVariable Long id) {
        log.debug("REST request to get Regulation : {}", id);
        RegulationDTO regulationDTO = regulationService.getCompleteRegulation(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(regulationDTO));
    }



    @PutMapping("/regulations/searchInfoByCategoriesAndKeyWords")
    @Timed
    public ResponseEntity<RegulationDTO> findByCategoriesAndKeyWords(@Valid @RequestBody CategoriesKeyWordsQueryDTO categoriesKeyWordsQueryDTO){
        log.debug("REST request to update Regulation : {}", categoriesKeyWordsQueryDTO);
        RegulationDTO result = articleService.findByCategoriesAndKeyWords(categoriesKeyWordsQueryDTO);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }


    /**
     * DELETE  /regulations/:id : delete the "id" regulation.
     *
     * @param id the id of the regulationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/regulations/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegulation(@PathVariable Long id) {
        log.debug("REST request to delete Regulation : {}", id);
        regulationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
