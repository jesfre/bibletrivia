package com.blogspot.jesfre.bibletrivia.service;

import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.blogspot.jesfre.bibletrivia.domain.QuizEntry}.
 */
public interface QuizEntryService {
    /**
     * Save a quizEntry.
     *
     * @param quizEntry the entity to save.
     * @return the persisted entity.
     */
    QuizEntry save(QuizEntry quizEntry);

    /**
     * Updates a quizEntry.
     *
     * @param quizEntry the entity to update.
     * @return the persisted entity.
     */
    QuizEntry update(QuizEntry quizEntry);

    /**
     * Partially updates a quizEntry.
     *
     * @param quizEntry the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuizEntry> partialUpdate(QuizEntry quizEntry);

    /**
     * Get all the quizEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuizEntry> findAll(Pageable pageable);

    /**
     * Get all the quizEntries with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuizEntry> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" quizEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuizEntry> findOne(Long id);

    /**
     * Delete the "id" quizEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
