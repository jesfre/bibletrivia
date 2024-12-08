package com.blogspot.jesfre.bibletrivia.service;

import com.blogspot.jesfre.bibletrivia.domain.BibleReference;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.blogspot.jesfre.bibletrivia.domain.BibleReference}.
 */
public interface BibleReferenceService {
    /**
     * Save a bibleReference.
     *
     * @param bibleReference the entity to save.
     * @return the persisted entity.
     */
    BibleReference save(BibleReference bibleReference);

    /**
     * Updates a bibleReference.
     *
     * @param bibleReference the entity to update.
     * @return the persisted entity.
     */
    BibleReference update(BibleReference bibleReference);

    /**
     * Partially updates a bibleReference.
     *
     * @param bibleReference the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BibleReference> partialUpdate(BibleReference bibleReference);

    /**
     * Get all the bibleReferences.
     *
     * @return the list of entities.
     */
    List<BibleReference> findAll();

    /**
     * Get the "id" bibleReference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BibleReference> findOne(Long id);

    /**
     * Delete the "id" bibleReference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
