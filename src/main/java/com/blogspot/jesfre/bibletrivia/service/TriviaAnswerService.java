package com.blogspot.jesfre.bibletrivia.service;

import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer}.
 */
public interface TriviaAnswerService {
    /**
     * Save a triviaAnswer.
     *
     * @param triviaAnswer the entity to save.
     * @return the persisted entity.
     */
    TriviaAnswer save(TriviaAnswer triviaAnswer);

    /**
     * Updates a triviaAnswer.
     *
     * @param triviaAnswer the entity to update.
     * @return the persisted entity.
     */
    TriviaAnswer update(TriviaAnswer triviaAnswer);

    /**
     * Partially updates a triviaAnswer.
     *
     * @param triviaAnswer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TriviaAnswer> partialUpdate(TriviaAnswer triviaAnswer);

    /**
     * Get all the triviaAnswers.
     *
     * @return the list of entities.
     */
    List<TriviaAnswer> findAll();

    /**
     * Get all the triviaAnswers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TriviaAnswer> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" triviaAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TriviaAnswer> findOne(Long id);

    /**
     * Delete the "id" triviaAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
    List<TriviaAnswer> findAllByQuestionId(Long questionId);
}
