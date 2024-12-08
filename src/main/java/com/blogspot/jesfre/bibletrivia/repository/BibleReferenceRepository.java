package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.BibleReference;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BibleReference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BibleReferenceRepository extends JpaRepository<BibleReference, Long> {}
