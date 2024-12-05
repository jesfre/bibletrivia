package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.PersistentToken;
import com.blogspot.jesfre.bibletrivia.domain.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link PersistentToken} entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {
    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);
}
