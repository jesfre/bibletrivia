/**
 * 
 */
package com.blogspot.jesfre.bibletrivia.web.rest;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogspot.jesfre.bibletrivia.domain.Quiz;
import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.domain.User;
import com.blogspot.jesfre.bibletrivia.service.QuizService;
import com.blogspot.jesfre.bibletrivia.service.TriviaQuestionService;
import com.blogspot.jesfre.bibletrivia.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
	private final QuizService quizService;
	private final UserService userService;

    public TriviaGameResource(TriviaQuestionService triviaQuestionService, QuizService quizService, UserService userService) {
        this.triviaQuestionService = triviaQuestionService;
        this.quizService = quizService;
        this.userService = userService;
    }
	
	@GetMapping("/create")
	public ResponseEntity<Quiz> createTriviaGame(HttpServletRequest request) throws URISyntaxException {
		LOG.debug("In createTriviaGame... REST request...");

		User usr = getLoggedInUser();
		
		// TODO implement user.getFullName() 
		String ownerName = usr != null ? usr.getFirstName() : "Aonymous" ; 
		
		Quiz quiz = new Quiz();
		quiz.setOwner(usr);
		quiz.setQuizTaker(ownerName);
		quiz.setStartDate(ZonedDateTime.now());
		quiz.setTotalQuestions(0);
		
		HttpSession session = request.getSession();
		LOG.debug("Session: {}", session);
		
        String sessionId = session.getId();
		LOG.debug("Session ID: {}", sessionId);
		
		quizService.addOrGetCached(sessionId, quiz);
		
		return ResponseUtil.wrapOrNotFound(Optional.of(quiz));
	}
	
	@GetMapping("/update/{questionId}")
	public ResponseEntity<Void> updateTriviaGame(HttpServletRequest request, 
			@PathVariable("questionId") Long questionId, @RequestParam("answers") List<Long> answers) throws URISyntaxException {
		LOG.debug("Got questionId={}, asnwers={}", questionId, answers);
		
		HttpSession session = request.getSession();
        String sessionId = session.getId();
		
		Quiz quiz = quizService.addOrGetCached(sessionId, null);
		
		TriviaQuestion question = new TriviaQuestion();
		question.setId(questionId);
		
		QuizEntry quizEntry = new QuizEntry();
		quizEntry.setTriviaQuestion(question);

		if(answers != null && answers.size() > 0) {
			answers.forEach(aid -> {
				TriviaAnswer answer = new TriviaAnswer();
				answer.setId(aid);
				quizEntry.addTriviaAnswers(answer);
			});
		}
				
		quiz.addQuizEntries(quizEntry)
			.incrementTotalQuestions();
		
		quizService.updateCached(sessionId, quiz);
		
		LOG.debug("Cached quiz: {}", quizService.addOrGetCached(sessionId, null));
		
		return ResponseEntity.noContent().build();
	}
	
	private String isQuizOwnerName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			  return "Anonymous";
		}
		return authentication.getName();
	}
	
	private User getLoggedInUser() {
		LOG.debug("In getLoggedInUser...");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  if (authentication != null || authentication instanceof AnonymousAuthenticationToken) {
			  String login = authentication.getName();
              User user = userService.getUserWithAuthoritiesByLogin(login)
                .orElseThrow( () -> new IllegalStateException("User not found when creating a new trivia game.") );
              return user;
		}
		return null;
	}
}
