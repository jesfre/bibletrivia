package com.blogspot.jesfre.bibletrivia.service;

import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion}.
 */
public interface TriviaQuestionService {
    /**
     * Save a triviaQuestion.
     *
     * @param triviaQuestion the entity to save.
     * @return the persisted entity.
     */
    TriviaQuestion save(TriviaQuestion triviaQuestion);

    /**
     * Updates a triviaQuestion.
     *
     * @param triviaQuestion the entity to update.
     * @return the persisted entity.
     */
    TriviaQuestion update(TriviaQuestion triviaQuestion);

    /**
     * Partially updates a triviaQuestion.
     *
     * @param triviaQuestion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TriviaQuestion> partialUpdate(TriviaQuestion triviaQuestion);

    /**
     * Get all the triviaQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TriviaQuestion> findAll(Pageable pageable);

    /**
     * Get the "id" triviaQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TriviaQuestion> findOne(Long id);

    /**
     * Delete the "id" triviaQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
