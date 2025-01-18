package com.blogspot.jesfre.bibletrivia.service.impl;

import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import com.blogspot.jesfre.bibletrivia.repository.QuizEntryRepository;
import com.blogspot.jesfre.bibletrivia.service.QuizEntryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blogspot.jesfre.bibletrivia.domain.QuizEntry}.
 */
@Service
@Transactional
public class QuizEntryServiceImpl implements QuizEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(QuizEntryServiceImpl.class);

    private final QuizEntryRepository quizEntryRepository;

    public QuizEntryServiceImpl(QuizEntryRepository quizEntryRepository) {
        this.quizEntryRepository = quizEntryRepository;
    }

    @Override
    public QuizEntry save(QuizEntry quizEntry) {
        LOG.debug("Request to save QuizEntry : {}", quizEntry);
        return quizEntryRepository.save(quizEntry);
    }

    @Override
    public QuizEntry update(QuizEntry quizEntry) {
        LOG.debug("Request to update QuizEntry : {}", quizEntry);
        return quizEntryRepository.save(quizEntry);
    }

    @Override
    public Optional<QuizEntry> partialUpdate(QuizEntry quizEntry) {
        LOG.debug("Request to partially update QuizEntry : {}", quizEntry);

        return quizEntryRepository
            .findById(quizEntry.getId())
            .map(existingQuizEntry -> {
                if (quizEntry.getOrderNum() != null) {
                    existingQuizEntry.setOrderNum(quizEntry.getOrderNum());
                }
                if (quizEntry.getCorrect() != null) {
                    existingQuizEntry.setCorrect(quizEntry.getCorrect());
                }

                return existingQuizEntry;
            })
            .map(quizEntryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuizEntry> findAll(Pageable pageable) {
        LOG.debug("Request to get all QuizEntries");
        return quizEntryRepository.findAll(pageable);
    }

    public Page<QuizEntry> findAllWithEagerRelationships(Pageable pageable) {
        return quizEntryRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuizEntry> findOne(Long id) {
        LOG.debug("Request to get QuizEntry : {}", id);
        return quizEntryRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuizEntry : {}", id);
        quizEntryRepository.deleteById(id);
    }
}
