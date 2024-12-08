package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TriviaAnswerRepositoryWithBagRelationships {
    Optional<TriviaAnswer> fetchBagRelationships(Optional<TriviaAnswer> triviaAnswer);

    List<TriviaAnswer> fetchBagRelationships(List<TriviaAnswer> triviaAnswers);

    Page<TriviaAnswer> fetchBagRelationships(Page<TriviaAnswer> triviaAnswers);
}
