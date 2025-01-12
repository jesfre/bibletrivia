package com.blogspot.jesfre.bibletrivia.web.rest;

import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaLevel;
import com.blogspot.jesfre.bibletrivia.repository.TriviaQuestionRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaQuestionService;
import com.blogspot.jesfre.bibletrivia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

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
 * REST controller for managing {@link com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion}.
 */
@RestController
@RequestMapping("/api/trivia-questions")
public class TriviaQuestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TriviaQuestionResource.class);

    private static final String ENTITY_NAME = "triviaQuestion";

    private static final List<Long> QUESTIONS_ANSWERED = new ArrayList<Long>();

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TriviaQuestionService triviaQuestionService;

    private final TriviaQuestionRepository triviaQuestionRepository;

    public TriviaQuestionResource(TriviaQuestionService triviaQuestionService, TriviaQuestionRepository triviaQuestionRepository) {
        this.triviaQuestionService = triviaQuestionService;
        this.triviaQuestionRepository = triviaQuestionRepository;
    }

    /**
     * {@code POST  /trivia-questions} : Create a new triviaQuestion.
     *
     * @param triviaQuestion the triviaQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new triviaQuestion, or with status {@code 400 (Bad Request)} if the triviaQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TriviaQuestion> createTriviaQuestion(@RequestBody TriviaQuestion triviaQuestion) throws URISyntaxException {
        LOG.debug("REST request to save TriviaQuestion : {}", triviaQuestion);
        if (triviaQuestion.getId() != null) {
            throw new BadRequestAlertException("A new triviaQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        triviaQuestion = triviaQuestionService.save(triviaQuestion);
        return ResponseEntity.created(new URI("/api/trivia-questions/" + triviaQuestion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, triviaQuestion.getId().toString()))
            .body(triviaQuestion);
    }

    /**
     * {@code PUT  /trivia-questions/:id} : Updates an existing triviaQuestion.
     *
     * @param id the id of the triviaQuestion to save.
     * @param triviaQuestion the triviaQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated triviaQuestion,
     * or with status {@code 400 (Bad Request)} if the triviaQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the triviaQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TriviaQuestion> updateTriviaQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TriviaQuestion triviaQuestion
    ) throws URISyntaxException {
        LOG.debug("REST request to update TriviaQuestion : {}, {}", id, triviaQuestion);
        if (triviaQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, triviaQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!triviaQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        triviaQuestion = triviaQuestionService.update(triviaQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, triviaQuestion.getId().toString()))
            .body(triviaQuestion);
    }

    /**
     * {@code PATCH  /trivia-questions/:id} : Partial updates given fields of an existing triviaQuestion, field will ignore if it is null
     *
     * @param id the id of the triviaQuestion to save.
     * @param triviaQuestion the triviaQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated triviaQuestion,
     * or with status {@code 400 (Bad Request)} if the triviaQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the triviaQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the triviaQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TriviaQuestion> partialUpdateTriviaQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TriviaQuestion triviaQuestion
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TriviaQuestion partially : {}, {}", id, triviaQuestion);
        if (triviaQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, triviaQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!triviaQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TriviaQuestion> result = triviaQuestionService.partialUpdate(triviaQuestion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, triviaQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /trivia-questions} : get all the triviaQuestions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of triviaQuestions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TriviaQuestion>> getAllTriviaQuestions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TriviaQuestions");
        Page<TriviaQuestion> page = triviaQuestionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trivia-questions/:id} : get the "id" triviaQuestion.
     *
     * @param id the id of the triviaQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the triviaQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TriviaQuestion> getTriviaQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TriviaQuestion : {}", id);
        Optional<TriviaQuestion> triviaQuestion = triviaQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(triviaQuestion);
    }

    /**
     * {@code DELETE  /trivia-questions/:id} : delete the "id" triviaQuestion.
     *
     * @param id the id of the triviaQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTriviaQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TriviaQuestion : {}", id);
        triviaQuestionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<TriviaQuestion> getTriviaQuestionInLevel(@PathVariable("level") TriviaLevel level) {
        LOG.debug("REST request to get a TriviaQuestion for level {}", level);
        
//        Optional<TriviaQuestion> opt = triviaQuestionService
//            .findInLevel(level)
//            .stream()
//            .filter(q -> !QUESTIONS_ANSWERED.contains(q.getId()))
//            .findAny();
//     // TODO fix this query to avoid NoSuchElementException and to avoid two calls to the service
//        return ResponseUtil.wrapOrNotFound(triviaQuestionService.findOne(opt.get().getId()));
        
        // TODO exclude already answered questions
        Random random = new Random();
        List<TriviaQuestion> triviasInLevel = triviaQuestionService.findInLevel(level);
        int nextQnumber = random.nextInt(triviasInLevel.size());
        Optional<TriviaQuestion> result = Optional.ofNullable(triviasInLevel.get(nextQnumber));
        return ResponseUtil.wrapOrNotFound(result);
    }
}
