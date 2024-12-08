package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.Trivia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TriviaRepositoryWithBagRelationships {
    Optional<Trivia> fetchBagRelationships(Optional<Trivia> trivia);

    List<Trivia> fetchBagRelationships(List<Trivia> trivias);

    Page<Trivia> fetchBagRelationships(Page<Trivia> trivias);
}
