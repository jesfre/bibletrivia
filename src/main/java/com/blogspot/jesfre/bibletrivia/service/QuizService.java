package com.blogspot.jesfre.bibletrivia.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blogspot.jesfre.bibletrivia.domain.Quiz;

/**
 * Service Interface for managing {@link com.blogspot.jesfre.bibletrivia.domain.Quiz}.
 */
public interface QuizService {
    /**
     * Save a quiz.
     *
     * @param quiz the entity to save.
     * @return the persisted entity.
     */
    Quiz save(Quiz quiz);

    /**
     * Updates a quiz.
     *
     * @param quiz the entity to update.
     * @return the persisted entity.
     */
    Quiz update(Quiz quiz);

    /**
     * Partially updates a quiz.
     *
     * @param quiz the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Quiz> partialUpdate(Quiz quiz);

    /**
     * Get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Quiz> findAll(Pageable pageable);

    /**
     * Get all the quizzes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Quiz> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" quiz.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Quiz> findOne(Long id);

    /**
     * Delete the "id" quiz.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
    Quiz addOrGetCached(String sessionId, Quiz quiz);
    
    Quiz updateCached(String sessionId, Quiz quiz);
    
    void removeCached(String sessionId);
}
