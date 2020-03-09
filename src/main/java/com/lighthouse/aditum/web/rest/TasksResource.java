package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.TasksService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.TasksDTO;
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
 * REST controller for managing Tasks.
 */
@RestController
@RequestMapping("/api")
public class TasksResource {

    private final Logger log = LoggerFactory.getLogger(TasksResource.class);

    private static final String ENTITY_NAME = "tasks";

    private final TasksService tasksService;

    public TasksResource(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    /**
     * POST  /tasks : Create a new tasks.
     *
     * @param tasksDTO the tasksDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tasksDTO, or with status 400 (Bad Request) if the tasks has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tasks")
    @Timed
    public ResponseEntity<TasksDTO> createTasks(@Valid @RequestBody TasksDTO tasksDTO) throws URISyntaxException {
        log.debug("REST request to save Tasks : {}", tasksDTO);
        if (tasksDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proveedor cannot already have an ID")).body(null);
        }
        TasksDTO result = tasksService.save(tasksDTO);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks : Updates an existing tasks.
     *
     * @param tasksDTO the tasksDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tasksDTO,
     * or with status 400 (Bad Request) if the tasksDTO is not valid,
     * or with status 500 (Internal Server Error) if the tasksDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tasks")
    @Timed
    public ResponseEntity<TasksDTO> updateTasks(@Valid @RequestBody TasksDTO tasksDTO) throws URISyntaxException {
        log.debug("REST request to update Tasks : {}", tasksDTO);
        if (tasksDTO.getId() == null) {
            return createTasks(tasksDTO);
        }
        TasksDTO result = tasksService.save(tasksDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tasksDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks")
    @Timed
    public ResponseEntity<List<TasksDTO>> getAllTasks(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Tasks");
        Page<TasksDTO> page = tasksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/tasks/by-company/{companyId}/{status}")
    @Timed
    public ResponseEntity<List<TasksDTO>> getAllTasksByCompany(Pageable pageable,@PathVariable Long companyId,@PathVariable int status) throws URISyntaxException {
        log.debug("REST request to get a page of Tasks");
        Page<TasksDTO> page = tasksService.findAllByCompany(pageable,companyId,status);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /tasks/:id : get the "id" tasks.
     *
     * @param id the id of the tasksDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tasksDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<TasksDTO> getTasks(@PathVariable Long id) {
        log.debug("REST request to get Tasks : {}", id);
        TasksDTO tasksDTO = tasksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tasksDTO));
    }

    /**
     * DELETE  /tasks/:id : delete the "id" tasks.
     *
     * @param id the id of the tasksDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tasks/{id}")
    @Timed
    public ResponseEntity<Void> deleteTasks(@PathVariable Long id) {
        log.debug("REST request to delete Tasks : {}", id);
        tasksService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
