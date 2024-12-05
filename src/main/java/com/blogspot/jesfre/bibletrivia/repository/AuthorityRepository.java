package com.blogspot.jesfre.bibletrivia.repository;

import com.blogspot.jesfre.bibletrivia.domain.Authority;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
