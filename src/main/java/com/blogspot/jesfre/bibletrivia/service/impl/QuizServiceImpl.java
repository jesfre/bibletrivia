package com.blogspot.jesfre.bibletrivia.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogspot.jesfre.bibletrivia.domain.Quiz;
import com.blogspot.jesfre.bibletrivia.repository.QuizRepository;
import com.blogspot.jesfre.bibletrivia.service.QuizService;

/**
 * Service Implementation for managing {@link com.blogspot.jesfre.bibletrivia.domain.Quiz}.
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private static final Logger LOG = LoggerFactory.getLogger(QuizServiceImpl.class);

    private final QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Quiz save(Quiz quiz) {
        LOG.debug("Request to save Quiz : {}", quiz);
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz update(Quiz quiz) {
        LOG.debug("Request to update Quiz : {}", quiz);
        return quizRepository.save(quiz);
    }

    @Override
    public Optional<Quiz> partialUpdate(Quiz quiz) {
        LOG.debug("Request to partially update Quiz : {}", quiz);

        return quizRepository
            .findById(quiz.getId())
            .map(existingQuiz -> {
                if (quiz.getQuizTaker() != null) {
                    existingQuiz.setQuizTaker(quiz.getQuizTaker());
                }
                if (quiz.getStartDate() != null) {
                    existingQuiz.setStartDate(quiz.getStartDate());
                }
                if (quiz.getTotalQuestions() != null) {
                    existingQuiz.setTotalQuestions(quiz.getTotalQuestions());
                }
                if (quiz.getCorrectQuestions() != null) {
                    existingQuiz.setCorrectQuestions(quiz.getCorrectQuestions());
                }

                return existingQuiz;
            })
            .map(quizRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Quiz> findAll(Pageable pageable) {
        LOG.debug("Request to get all Quizzes");
        return quizRepository.findAll(pageable);
    }

    public Page<Quiz> findAllWithEagerRelationships(Pageable pageable) {
        return quizRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findOne(Long id) {
        LOG.debug("Request to get Quiz : {}", id);
        return quizRepository.findOneWithEagerRelationships(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findOneWithEntries(Long id) {
        LOG.debug("Request to get Quiz with entries: {}", id);
        return quizRepository.findOneWithQuizEntries(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Quiz : {}", id);
        quizRepository.deleteById(id);
    }
    
    @Cacheable(cacheNames = "com.blogspot.jesfre.bibletrivia.domain.Quiz", key = "#sessionId")
    public Quiz addOrGetCached(String sessionId, Quiz quiz) {
        LOG.debug("Adding Quiz to cache...");
        return quiz;
    }
    
    @Cacheable(cacheNames = "com.blogspot.jesfre.bibletrivia.domain.Quiz", key = "#sessionId")
    public Quiz updateCached(String sessionId, Quiz quiz) {
        LOG.debug("Updating Quiz in cache...");
    	return quiz;
    }
    
    @CacheEvict(cacheNames = "com.blogspot.jesfre.bibletrivia.domain.Quiz", key = "#sessionId")
    public void removeCached(String sessionId) {
        LOG.debug("Removing Quiz from cache...");
    }
}
