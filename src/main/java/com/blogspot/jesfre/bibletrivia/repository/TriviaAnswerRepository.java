package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TriviaAnswer entity.
 *
 * When extending this class, extend TriviaAnswerRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TriviaAnswerRepository extends TriviaAnswerRepositoryWithBagRelationships, JpaRepository<TriviaAnswer, Long> {
    default Optional<TriviaAnswer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<TriviaAnswer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<TriviaAnswer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
    
    @Query("select triviaAnswer from TriviaAnswer triviaAnswer where triviaQuestion.id=:questionId")
    List<TriviaAnswer> findAllByQuestion(@Param("questionId") Long questionId);
}
