package com.blogspot.jesfre.bibletrivia.web.rest;

import com.blogspot.jesfre.bibletrivia.domain.Trivia;
import com.blogspot.jesfre.bibletrivia.repository.TriviaRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaService;
import com.blogspot.jesfre.bibletrivia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blogspot.jesfre.bibletrivia.domain.Trivia}.
 */
@RestController
@RequestMapping("/api/trivias")
public class TriviaResource {

    private static final Logger LOG = LoggerFactory.getLogger(TriviaResource.class);

    private static final String ENTITY_NAME = "trivia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TriviaService triviaService;

    private final TriviaRepository triviaRepository;

    public TriviaResource(TriviaService triviaService, TriviaRepository triviaRepository) {
        this.triviaService = triviaService;
        this.triviaRepository = triviaRepository;
    }

    /**
     * {@code POST  /trivias} : Create a new trivia.
     *
     * @param trivia the trivia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trivia, or with status {@code 400 (Bad Request)} if the trivia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Trivia> createTrivia(@RequestBody Trivia trivia) throws URISyntaxException {
        LOG.debug("REST request to save Trivia : {}", trivia);
        if (trivia.getId() != null) {
            throw new BadRequestAlertException("A new trivia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trivia = triviaService.save(trivia);
        return ResponseEntity.created(new URI("/api/trivias/" + trivia.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, trivia.getId().toString()))
            .body(trivia);
    }

    /**
     * {@code PUT  /trivias/:id} : Updates an existing trivia.
     *
     * @param id the id of the trivia to save.
     * @param trivia the trivia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trivia,
     * or with status {@code 400 (Bad Request)} if the trivia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trivia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trivia> updateTrivia(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trivia trivia)
        throws URISyntaxException {
        LOG.debug("REST request to update Trivia : {}, {}", id, trivia);
        if (trivia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trivia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!triviaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trivia = triviaService.update(trivia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trivia.getId().toString()))
            .body(trivia);
    }

    /**
     * {@code PATCH  /trivias/:id} : Partial updates given fields of an existing trivia, field will ignore if it is null
     *
     * @param id the id of the trivia to save.
     * @param trivia the trivia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trivia,
     * or with status {@code 400 (Bad Request)} if the trivia is not valid,
     * or with status {@code 404 (Not Found)} if the trivia is not found,
     * or with status {@code 500 (Internal Server Error)} if the trivia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trivia> partialUpdateTrivia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Trivia trivia
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Trivia partially : {}, {}", id, trivia);
        if (trivia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trivia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!triviaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trivia> result = triviaService.partialUpdate(trivia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trivia.getId().toString())
        );
    }

    /**
     * {@code GET  /trivias} : get all the trivias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trivias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Trivia>> getAllTrivias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Trivias");
        Page<Trivia> page;
        if (eagerload) {
            page = triviaService.findAllWithEagerRelationships(pageable);
        } else {
            page = triviaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trivias/:id} : get the "id" trivia.
     *
     * @param id the id of the trivia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trivia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trivia> getTrivia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Trivia : {}", id);
        Optional<Trivia> trivia = triviaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trivia);
    }

    /**
     * {@code DELETE  /trivias/:id} : delete the "id" trivia.
     *
     * @param id the id of the trivia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrivia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Trivia : {}", id);
        triviaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
