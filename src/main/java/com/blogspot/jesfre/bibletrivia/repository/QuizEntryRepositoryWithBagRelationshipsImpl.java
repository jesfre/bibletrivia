package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class QuizEntryRepositoryWithBagRelationshipsImpl implements QuizEntryRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String QUIZENTRIES_PARAMETER = "quizEntries";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<QuizEntry> fetchBagRelationships(Optional<QuizEntry> quizEntry) {
        return quizEntry.map(this::fetchTriviaAnswers);
    }

    @Override
    public Page<QuizEntry> fetchBagRelationships(Page<QuizEntry> quizEntries) {
        return new PageImpl<>(fetchBagRelationships(quizEntries.getContent()), quizEntries.getPageable(), quizEntries.getTotalElements());
    }

    @Override
    public List<QuizEntry> fetchBagRelationships(List<QuizEntry> quizEntries) {
        return Optional.of(quizEntries).map(this::fetchTriviaAnswers).orElse(Collections.emptyList());
    }

    QuizEntry fetchTriviaAnswers(QuizEntry result) {
        return entityManager
            .createQuery(
                "select quizEntry from QuizEntry quizEntry left join fetch quizEntry.selectedAnswers where quizEntry.id = :id",
                QuizEntry.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<QuizEntry> fetchTriviaAnswers(List<QuizEntry> quizEntries) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, quizEntries.size()).forEach(index -> order.put(quizEntries.get(index).getId(), index));
        List<QuizEntry> result = entityManager
            .createQuery(
                "select quizEntry from QuizEntry quizEntry left join fetch quizEntry.selectedAnswers where quizEntry in :quizEntries",
                QuizEntry.class
            )
            .setParameter(QUIZENTRIES_PARAMETER, quizEntries)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
