package com.blogspot.jesfre.bibletrivia.web.rest;

import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import com.blogspot.jesfre.bibletrivia.repository.QuizEntryRepository;
import com.blogspot.jesfre.bibletrivia.service.QuizEntryService;
import com.blogspot.jesfre.bibletrivia.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.blogspot.jesfre.bibletrivia.domain.QuizEntry}.
 */
@RestController
@RequestMapping("/api/quiz-entries")
public class QuizEntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuizEntryResource.class);

    private static final String ENTITY_NAME = "quizEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizEntryService quizEntryService;

    private final QuizEntryRepository quizEntryRepository;

    public QuizEntryResource(QuizEntryService quizEntryService, QuizEntryRepository quizEntryRepository) {
        this.quizEntryService = quizEntryService;
        this.quizEntryRepository = quizEntryRepository;
    }

    /**
     * {@code POST  /quiz-entries} : Create a new quizEntry.
     *
     * @param quizEntry the quizEntry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizEntry, or with status {@code 400 (Bad Request)} if the quizEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizEntry> createQuizEntry(@Valid @RequestBody QuizEntry quizEntry) throws URISyntaxException {
        LOG.debug("REST request to save QuizEntry : {}", quizEntry);
        if (quizEntry.getId() != null) {
            throw new BadRequestAlertException("A new quizEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quizEntry = quizEntryService.save(quizEntry);
        return ResponseEntity.created(new URI("/api/quiz-entries/" + quizEntry.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, quizEntry.getId().toString()))
            .body(quizEntry);
    }

    /**
     * {@code PUT  /quiz-entries/:id} : Updates an existing quizEntry.
     *
     * @param id the id of the quizEntry to save.
     * @param quizEntry the quizEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizEntry,
     * or with status {@code 400 (Bad Request)} if the quizEntry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizEntry> updateQuizEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuizEntry quizEntry
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuizEntry : {}, {}", id, quizEntry);
        if (quizEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        quizEntry = quizEntryService.update(quizEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizEntry.getId().toString()))
            .body(quizEntry);
    }

    /**
     * {@code PATCH  /quiz-entries/:id} : Partial updates given fields of an existing quizEntry, field will ignore if it is null
     *
     * @param id the id of the quizEntry to save.
     * @param quizEntry the quizEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizEntry,
     * or with status {@code 400 (Bad Request)} if the quizEntry is not valid,
     * or with status {@code 404 (Not Found)} if the quizEntry is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizEntry> partialUpdateQuizEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuizEntry quizEntry
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuizEntry partially : {}, {}", id, quizEntry);
        if (quizEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizEntry> result = quizEntryService.partialUpdate(quizEntry);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizEntry.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-entries} : get all the quizEntries.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuizEntry>> getAllQuizEntries(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of QuizEntries");
        Page<QuizEntry> page;
        if (eagerload) {
            page = quizEntryService.findAllWithEagerRelationships(pageable);
        } else {
            page = quizEntryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quiz-entries/:id} : get the "id" quizEntry.
     *
     * @param id the id of the quizEntry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizEntry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizEntry> getQuizEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuizEntry : {}", id);
        Optional<QuizEntry> quizEntry = quizEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizEntry);
    }

    /**
     * {@code DELETE  /quiz-entries/:id} : delete the "id" quizEntry.
     *
     * @param id the id of the quizEntry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuizEntry : {}", id);
        quizEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
