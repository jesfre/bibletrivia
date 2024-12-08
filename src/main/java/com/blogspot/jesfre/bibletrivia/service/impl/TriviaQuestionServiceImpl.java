package com.blogspot.jesfre.bibletrivia.service.impl;

import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.repository.TriviaQuestionRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaQuestionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion}.
 */
@Service
@Transactional
public class TriviaQuestionServiceImpl implements TriviaQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(TriviaQuestionServiceImpl.class);

    private final TriviaQuestionRepository triviaQuestionRepository;

    public TriviaQuestionServiceImpl(TriviaQuestionRepository triviaQuestionRepository) {
        this.triviaQuestionRepository = triviaQuestionRepository;
    }

    @Override
    public TriviaQuestion save(TriviaQuestion triviaQuestion) {
        LOG.debug("Request to save TriviaQuestion : {}", triviaQuestion);
        return triviaQuestionRepository.save(triviaQuestion);
    }

    @Override
    public TriviaQuestion update(TriviaQuestion triviaQuestion) {
        LOG.debug("Request to update TriviaQuestion : {}", triviaQuestion);
        return triviaQuestionRepository.save(triviaQuestion);
    }

    @Override
    public Optional<TriviaQuestion> partialUpdate(TriviaQuestion triviaQuestion) {
        LOG.debug("Request to partially update TriviaQuestion : {}", triviaQuestion);

        return triviaQuestionRepository
            .findById(triviaQuestion.getId())
            .map(existingTriviaQuestion -> {
                if (triviaQuestion.getQuestionId() != null) {
                    existingTriviaQuestion.setQuestionId(triviaQuestion.getQuestionId());
                }
                if (triviaQuestion.getQuestionType() != null) {
                    existingTriviaQuestion.setQuestionType(triviaQuestion.getQuestionType());
                }
                if (triviaQuestion.getQuestion() != null) {
                    existingTriviaQuestion.setQuestion(triviaQuestion.getQuestion());
                }
                if (triviaQuestion.getAnswerType() != null) {
                    existingTriviaQuestion.setAnswerType(triviaQuestion.getAnswerType());
                }
                if (triviaQuestion.getValue() != null) {
                    existingTriviaQuestion.setValue(triviaQuestion.getValue());
                }
                if (triviaQuestion.getPicture() != null) {
                    existingTriviaQuestion.setPicture(triviaQuestion.getPicture());
                }

                return existingTriviaQuestion;
            })
            .map(triviaQuestionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TriviaQuestion> findAll(Pageable pageable) {
        LOG.debug("Request to get all TriviaQuestions");
        return triviaQuestionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TriviaQuestion> findOne(Long id) {
        LOG.debug("Request to get TriviaQuestion : {}", id);
        return triviaQuestionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TriviaQuestion : {}", id);
        triviaQuestionRepository.deleteById(id);
    }
}
