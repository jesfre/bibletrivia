package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuizEntry entity.
 *
 * When extending this class, extend QuizEntryRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface QuizEntryRepository extends QuizEntryRepositoryWithBagRelationships, JpaRepository<QuizEntry, Long> {
    default Optional<QuizEntry> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<QuizEntry> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<QuizEntry> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select quizEntry from QuizEntry quizEntry left join fetch quizEntry.triviaQuestion",
        countQuery = "select count(quizEntry) from QuizEntry quizEntry"
    )
    Page<QuizEntry> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quizEntry from QuizEntry quizEntry left join fetch quizEntry.triviaQuestion")
    List<QuizEntry> findAllWithToOneRelationships();

    @Query("select quizEntry from QuizEntry quizEntry left join fetch quizEntry.triviaQuestion where quizEntry.id =:id")
    Optional<QuizEntry> findOneWithToOneRelationships(@Param("id") Long id);
}
