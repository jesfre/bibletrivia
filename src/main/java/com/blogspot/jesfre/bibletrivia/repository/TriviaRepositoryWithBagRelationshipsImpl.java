package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.Trivia;
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
public class TriviaRepositoryWithBagRelationshipsImpl implements TriviaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRIVIAS_PARAMETER = "trivias";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Trivia> fetchBagRelationships(Optional<Trivia> trivia) {
        return trivia.map(this::fetchTriviaQuestions);
    }

    @Override
    public Page<Trivia> fetchBagRelationships(Page<Trivia> trivias) {
        return new PageImpl<>(fetchBagRelationships(trivias.getContent()), trivias.getPageable(), trivias.getTotalElements());
    }

    @Override
    public List<Trivia> fetchBagRelationships(List<Trivia> trivias) {
        return Optional.of(trivias).map(this::fetchTriviaQuestions).orElse(Collections.emptyList());
    }

    Trivia fetchTriviaQuestions(Trivia result) {
        return entityManager
            .createQuery("select trivia from Trivia trivia left join fetch trivia.triviaQuestions where trivia.id = :id", Trivia.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Trivia> fetchTriviaQuestions(List<Trivia> trivias) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, trivias.size()).forEach(index -> order.put(trivias.get(index).getId(), index));
        List<Trivia> result = entityManager
            .createQuery("select trivia from Trivia trivia left join fetch trivia.triviaQuestions where trivia in :trivias", Trivia.class)
            .setParameter(TRIVIAS_PARAMETER, trivias)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
