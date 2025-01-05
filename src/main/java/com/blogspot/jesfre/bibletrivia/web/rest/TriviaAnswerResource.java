package com.blogspot.jesfre.bibletrivia.web.rest;

import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import com.blogspot.jesfre.bibletrivia.repository.TriviaAnswerRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaAnswerService;
import com.blogspot.jesfre.bibletrivia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer}.
 */
@RestController
@RequestMapping("/api/trivia-answers")
public class TriviaAnswerResource {

    private static final Logger LOG = LoggerFactory.getLogger(TriviaAnswerResource.class);

    private static final String ENTITY_NAME = "triviaAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TriviaAnswerService triviaAnswerService;

    private final TriviaAnswerRepository triviaAnswerRepository;

    public TriviaAnswerResource(TriviaAnswerService triviaAnswerService, TriviaAnswerRepository triviaAnswerRepository) {
        this.triviaAnswerService = triviaAnswerService;
        this.triviaAnswerRepository = triviaAnswerRepository;
    }

    /**
     * {@code POST  /trivia-answers} : Create a new triviaAnswer.
     *
     * @param triviaAnswer the triviaAnswer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new triviaAnswer, or with status {@code 400 (Bad Request)} if the triviaAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TriviaAnswer> createTriviaAnswer(@RequestBody TriviaAnswer triviaAnswer) throws URISyntaxException {
        LOG.debug("REST request to save TriviaAnswer : {}", triviaAnswer);
        if (triviaAnswer.getId() != null) {
            throw new BadRequestAlertException("A new triviaAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        triviaAnswer = triviaAnswerService.save(triviaAnswer);
        return ResponseEntity.created(new URI("/api/trivia-answers/" + triviaAnswer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, triviaAnswer.getId().toString()))
            .body(triviaAnswer);
    }

    /**
     * {@code PUT  /trivia-answers/:id} : Updates an existing triviaAnswer.
     *
     * @param id the id of the triviaAnswer to save.
     * @param triviaAnswer the triviaAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated triviaAnswer,
     * or with status {@code 400 (Bad Request)} if the triviaAnswer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the triviaAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TriviaAnswer> updateTriviaAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TriviaAnswer triviaAnswer
    ) throws URISyntaxException {
        LOG.debug("REST request to update TriviaAnswer : {}, {}", id, triviaAnswer);
        if (triviaAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, triviaAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!triviaAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        triviaAnswer = triviaAnswerService.update(triviaAnswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, triviaAnswer.getId().toString()))
            .body(triviaAnswer);
    }

    /**
     * {@code PATCH  /trivia-answers/:id} : Partial updates given fields of an existing triviaAnswer, field will ignore if it is null
     *
     * @param id the id of the triviaAnswer to save.
     * @param triviaAnswer the triviaAnswer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated triviaAnswer,
     * or with status {@code 400 (Bad Request)} if the triviaAnswer is not valid,
     * or with status {@code 404 (Not Found)} if the triviaAnswer is not found,
     * or with status {@code 500 (Internal Server Error)} if the triviaAnswer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TriviaAnswer> partialUpdateTriviaAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TriviaAnswer triviaAnswer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TriviaAnswer partially : {}, {}", id, triviaAnswer);
        if (triviaAnswer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, triviaAnswer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!triviaAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TriviaAnswer> result = triviaAnswerService.partialUpdate(triviaAnswer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, triviaAnswer.getId().toString())
        );
    }

    /**
     * {@code GET  /trivia-answers} : get all the triviaAnswers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of triviaAnswers in body.
     */
    @GetMapping("")
    public List<TriviaAnswer> getAllTriviaAnswers(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all TriviaAnswers");
        return triviaAnswerService.findAll();
    }

    /**
     * {@code GET  /trivia-answers/:id} : get the "id" triviaAnswer.
     *
     * @param id the id of the triviaAnswer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the triviaAnswer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TriviaAnswer> getTriviaAnswer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TriviaAnswer : {}", id);
        Optional<TriviaAnswer> triviaAnswer = triviaAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(triviaAnswer);
    }

    /**
     * {@code DELETE  /trivia-answers/:id} : delete the "id" triviaAnswer.
     *
     * @param id the id of the triviaAnswer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTriviaAnswer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TriviaAnswer : {}", id);
        triviaAnswerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    
    @GetMapping("/question/{questionId}")
    public List<TriviaAnswer> getAnswersForQuestion(@PathVariable("questionId") Long questionId) {
        LOG.debug("REST request to get TriviaAnswers for question {}", questionId);
        return triviaAnswerService.findAll().stream()
        		.filter(ans -> ans.getTriviaQuestion() != null)
        		.filter(ans -> ans.getTriviaQuestion().getId() == questionId)
        		.toList();
    }
}
