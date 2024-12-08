package com.blogspot.jesfre.bibletrivia.service;

import com.blogspot.jesfre.bibletrivia.domain.Trivia;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blogspot.jesfre.bibletrivia.domain.Trivia}.
 */
public interface TriviaService {
    /**
     * Save a trivia.
     *
     * @param trivia the entity to save.
     * @return the persisted entity.
     */
    Trivia save(Trivia trivia);

    /**
     * Updates a trivia.
     *
     * @param trivia the entity to update.
     * @return the persisted entity.
     */
    Trivia update(Trivia trivia);

    /**
     * Partially updates a trivia.
     *
     * @param trivia the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Trivia> partialUpdate(Trivia trivia);

    /**
     * Get all the trivias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Trivia> findAll(Pageable pageable);

    /**
     * Get all the trivias with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Trivia> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" trivia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Trivia> findOne(Long id);

    /**
     * Delete the "id" trivia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
