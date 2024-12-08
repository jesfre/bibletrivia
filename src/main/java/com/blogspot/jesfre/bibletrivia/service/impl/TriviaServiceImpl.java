package com.blogspot.jesfre.bibletrivia.service.impl;

import com.blogspot.jesfre.bibletrivia.domain.Trivia;
import com.blogspot.jesfre.bibletrivia.repository.TriviaRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blogspot.jesfre.bibletrivia.domain.Trivia}.
 */
@Service
@Transactional
public class TriviaServiceImpl implements TriviaService {

    private static final Logger LOG = LoggerFactory.getLogger(TriviaServiceImpl.class);

    private final TriviaRepository triviaRepository;

    public TriviaServiceImpl(TriviaRepository triviaRepository) {
        this.triviaRepository = triviaRepository;
    }

    @Override
    public Trivia save(Trivia trivia) {
        LOG.debug("Request to save Trivia : {}", trivia);
        return triviaRepository.save(trivia);
    }

    @Override
    public Trivia update(Trivia trivia) {
        LOG.debug("Request to update Trivia : {}", trivia);
        return triviaRepository.save(trivia);
    }

    @Override
    public Optional<Trivia> partialUpdate(Trivia trivia) {
        LOG.debug("Request to partially update Trivia : {}", trivia);

        return triviaRepository
            .findById(trivia.getId())
            .map(existingTrivia -> {
                if (trivia.getLevel() != null) {
                    existingTrivia.setLevel(trivia.getLevel());
                }
                if (trivia.getName() != null) {
                    existingTrivia.setName(trivia.getName());
                }
                if (trivia.getType() != null) {
                    existingTrivia.setType(trivia.getType());
                }
                if (trivia.getStartDate() != null) {
                    existingTrivia.setStartDate(trivia.getStartDate());
                }
                if (trivia.getEndDate() != null) {
                    existingTrivia.setEndDate(trivia.getEndDate());
                }

                return existingTrivia;
            })
            .map(triviaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Trivia> findAll(Pageable pageable) {
        LOG.debug("Request to get all Trivias");
        return triviaRepository.findAll(pageable);
    }

    public Page<Trivia> findAllWithEagerRelationships(Pageable pageable) {
        return triviaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trivia> findOne(Long id) {
        LOG.debug("Request to get Trivia : {}", id);
        return triviaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Trivia : {}", id);
        triviaRepository.deleteById(id);
    }
}
