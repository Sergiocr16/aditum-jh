package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.TokenNotificationsService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.service.dto.TokenNotificationsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TokenNotifications.
 */
@RestController
@RequestMapping("/api")
public class TokenNotificationsResource {

    private final Logger log = LoggerFactory.getLogger(TokenNotificationsResource.class);

    private static final String ENTITY_NAME = "tokenNotifications";

    private final TokenNotificationsService tokenNotificationsService;

    public TokenNotificationsResource(TokenNotificationsService tokenNotificationsService) {
        this.tokenNotificationsService = tokenNotificationsService;
    }

    /**
     * POST  /token-notifications : Create a new tokenNotifications.
     *
     * @param tokenNotificationsDTO the tokenNotificationsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tokenNotificationsDTO, or with status 400 (Bad Request) if the tokenNotifications has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/token-notifications")
    @Timed
    public ResponseEntity<TokenNotificationsDTO> createTokenNotifications(@RequestBody TokenNotificationsDTO tokenNotificationsDTO) throws URISyntaxException {
        log.debug("REST request to save TokenNotifications : {}", tokenNotificationsDTO);
        if (tokenNotificationsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        TokenNotificationsDTO result = tokenNotificationsService.save(tokenNotificationsDTO);
        return ResponseEntity.created(new URI("/api/token-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /token-notifications : Updates an existing tokenNotifications.
     *
     * @param tokenNotificationsDTO the tokenNotificationsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tokenNotificationsDTO,
     * or with status 400 (Bad Request) if the tokenNotificationsDTO is not valid,
     * or with status 500 (Internal Server Error) if the tokenNotificationsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/token-notifications")
    @Timed
    public ResponseEntity<TokenNotificationsDTO> updateTokenNotifications(@RequestBody TokenNotificationsDTO tokenNotificationsDTO) throws URISyntaxException {
        log.debug("REST request to update TokenNotifications : {}", tokenNotificationsDTO);
        if (tokenNotificationsDTO.getId() == null) {
            return createTokenNotifications(tokenNotificationsDTO);
        }
        TokenNotificationsDTO result = tokenNotificationsService.save(tokenNotificationsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tokenNotificationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /token-notifications : get all the tokenNotifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tokenNotifications in body
     */
    @GetMapping("/token-notifications-login/{userId}/{token}")
    @Timed
    public TokenNotificationsDTO saveAtLogin(@PathVariable Long userId, @PathVariable String token) {
        log.debug("REST request to get all TokenNotifications");
        return tokenNotificationsService.saveAtLogin(userId, token);
    }

    /**
     * GET  /token-notifications : get all the tokenNotifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tokenNotifications in body
     */
    @GetMapping("/token-notifications-logout/{userId}/{token}")
    @Timed
    public TokenNotificationsDTO saveAtLogout(@PathVariable Long userId, @PathVariable String token) {
        log.debug("REST request to get all TokenNotifications");
        return tokenNotificationsService.saveAtLogout(userId,token);
    }

    @GetMapping("/token-notifications")
    @Timed
    public List<TokenNotificationsDTO> getAllTokenNotifications() {
        log.debug("REST request to get all TokenNotifications");
        return tokenNotificationsService.findAll();
    }


    /**
     * GET  /token-notifications/:id : get the "id" tokenNotifications.
     *
     * @param id the id of the tokenNotificationsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tokenNotificationsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/token-notifications/{id}")
    @Timed
    public ResponseEntity<TokenNotificationsDTO> getTokenNotifications(@PathVariable Long id) {
        log.debug("REST request to get TokenNotifications : {}", id);
        TokenNotificationsDTO tokenNotificationsDTO = tokenNotificationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tokenNotificationsDTO));
    }

    /**
     * DELETE  /token-notifications/:id : delete the "id" tokenNotifications.
     *
     * @param id the id of the tokenNotificationsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/token-notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteTokenNotifications(@PathVariable Long id) {
        log.debug("REST request to delete TokenNotifications : {}", id);
        tokenNotificationsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
