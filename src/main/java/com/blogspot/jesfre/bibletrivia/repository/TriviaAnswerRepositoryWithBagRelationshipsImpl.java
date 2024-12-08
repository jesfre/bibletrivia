package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
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
public class TriviaAnswerRepositoryWithBagRelationshipsImpl implements TriviaAnswerRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRIVIAANSWERS_PARAMETER = "triviaAnswers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TriviaAnswer> fetchBagRelationships(Optional<TriviaAnswer> triviaAnswer) {
        return triviaAnswer.map(this::fetchBibleReferences);
    }

    @Override
    public Page<TriviaAnswer> fetchBagRelationships(Page<TriviaAnswer> triviaAnswers) {
        return new PageImpl<>(
            fetchBagRelationships(triviaAnswers.getContent()),
            triviaAnswers.getPageable(),
            triviaAnswers.getTotalElements()
        );
    }

    @Override
    public List<TriviaAnswer> fetchBagRelationships(List<TriviaAnswer> triviaAnswers) {
        return Optional.of(triviaAnswers).map(this::fetchBibleReferences).orElse(Collections.emptyList());
    }

    TriviaAnswer fetchBibleReferences(TriviaAnswer result) {
        return entityManager
            .createQuery(
                "select triviaAnswer from TriviaAnswer triviaAnswer left join fetch triviaAnswer.bibleReferences where triviaAnswer.id = :id",
                TriviaAnswer.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<TriviaAnswer> fetchBibleReferences(List<TriviaAnswer> triviaAnswers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, triviaAnswers.size()).forEach(index -> order.put(triviaAnswers.get(index).getId(), index));
        List<TriviaAnswer> result = entityManager
            .createQuery(
                "select triviaAnswer from TriviaAnswer triviaAnswer left join fetch triviaAnswer.bibleReferences where triviaAnswer in :triviaAnswers",
                TriviaAnswer.class
            )
            .setParameter(TRIVIAANSWERS_PARAMETER, triviaAnswers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
