package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TriviaQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TriviaQuestionRepository extends JpaRepository<TriviaQuestion, Long> {}
