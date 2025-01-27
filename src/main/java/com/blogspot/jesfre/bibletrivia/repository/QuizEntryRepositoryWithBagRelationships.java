package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface QuizEntryRepositoryWithBagRelationships {
    Optional<QuizEntry> fetchBagRelationships(Optional<QuizEntry> quizEntry);

    List<QuizEntry> fetchBagRelationships(List<QuizEntry> quizEntries);

    Page<QuizEntry> fetchBagRelationships(Page<QuizEntry> quizEntries);
}
