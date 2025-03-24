package com.blogspot.jesfre.bibletrivia.service.impl;

import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import com.blogspot.jesfre.bibletrivia.repository.TriviaAnswerRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaAnswerService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer}.
 */
@Service
@Transactional
public class TriviaAnswerServiceImpl implements TriviaAnswerService {

    private static final Logger LOG = LoggerFactory.getLogger(TriviaAnswerServiceImpl.class);

    private final TriviaAnswerRepository triviaAnswerRepository;

    public TriviaAnswerServiceImpl(TriviaAnswerRepository triviaAnswerRepository) {
        this.triviaAnswerRepository = triviaAnswerRepository;
    }

    @Override
    public TriviaAnswer save(TriviaAnswer triviaAnswer) {
        LOG.debug("Request to save TriviaAnswer : {}", triviaAnswer);
        return triviaAnswerRepository.save(triviaAnswer);
    }

    @Override
    public TriviaAnswer update(TriviaAnswer triviaAnswer) {
        LOG.debug("Request to update TriviaAnswer : {}", triviaAnswer);
        return triviaAnswerRepository.save(triviaAnswer);
    }

    @Override
    public Optional<TriviaAnswer> partialUpdate(TriviaAnswer triviaAnswer) {
        LOG.debug("Request to partially update TriviaAnswer : {}", triviaAnswer);

        return triviaAnswerRepository
            .findById(triviaAnswer.getId())
            .map(existingTriviaAnswer -> {
                if (triviaAnswer.getAnswerId() != null) {
                    existingTriviaAnswer.setAnswerId(triviaAnswer.getAnswerId());
                }
                if (triviaAnswer.getAnswer() != null) {
                    existingTriviaAnswer.setAnswer(triviaAnswer.getAnswer());
                }
                if (triviaAnswer.getExplanation() != null) {
                    existingTriviaAnswer.setExplanation(triviaAnswer.getExplanation());
                }
                if (triviaAnswer.getCorrect() != null) {
                    existingTriviaAnswer.setCorrect(triviaAnswer.getCorrect());
                }
                if (triviaAnswer.getPicture() != null) {
                    existingTriviaAnswer.setPicture(triviaAnswer.getPicture());
                }

                return existingTriviaAnswer;
            })
            .map(triviaAnswerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TriviaAnswer> findAll() {
        LOG.debug("Request to get all TriviaAnswers");
        return triviaAnswerRepository.findAll();
    }

    public Page<TriviaAnswer> findAllWithEagerRelationships(Pageable pageable) {
        return triviaAnswerRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TriviaAnswer> findOne(Long id) {
        LOG.debug("Request to get TriviaAnswer : {}", id);
        return triviaAnswerRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TriviaAnswer : {}", id);
        triviaAnswerRepository.deleteById(id);
    }
    
    @Override
    public List<TriviaAnswer> findAllByQuestionId(Long questionId) {
    	LOG.debug("Request to find all TriviaAnswer by questionId: {}", questionId);
        return triviaAnswerRepository.findAllByQuestion(questionId);
    }
}
