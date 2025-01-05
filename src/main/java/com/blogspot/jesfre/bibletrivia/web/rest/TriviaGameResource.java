/**
 * 
 */
package com.blogspot.jesfre.bibletrivia.web.rest;

import java.net.URISyntaxException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogspot.jesfre.bibletrivia.domain.TriviaGameQuestion;
import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.repository.TriviaQuestionRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaQuestionService;
import com.blogspot.jesfre.bibletrivia.web.rest.errors.BadRequestAlertException;

import tech.jhipster.web.util.ResponseUtil;

/**
 * Rest controller for executing a trivia game
 * @author jorge
 *
 */
@RestController
@RequestMapping("/api/trivia-game")
public class TriviaGameResource {

	private static final Logger LOG = LoggerFactory.getLogger(TriviaGameResource.class);

	private static final String ENTITY_NAME = "triviaGameQuestion";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	private final TriviaQuestionService triviaQuestionService;

    public TriviaGameResource(TriviaQuestionService triviaQuestionService) {
        this.triviaQuestionService = triviaQuestionService;
    }
	
	@GetMapping("/question/{questionNumber}")
	public ResponseEntity<TriviaQuestion> getQuestion(@PathVariable("questionNumber") Integer questionNumber) throws URISyntaxException {
		LOG.debug("In newTriviaQuestion... REST request to get question: {}", questionNumber);
		if (questionNumber < 0) {
			throw new BadRequestAlertException("Question numbers cannot be " + questionNumber, ENTITY_NAME, "badquestionnumber");
		}
		
		// TODO logic to get next question
		
		// TODO store answered questions
		
		if(questionNumber == null || questionNumber < 1) {
			questionNumber = 0;
		}
		questionNumber++;
		
		long id = 1L;
		
		return ResponseUtil.wrapOrNotFound(triviaQuestionService.findOne(id));
	}
}
