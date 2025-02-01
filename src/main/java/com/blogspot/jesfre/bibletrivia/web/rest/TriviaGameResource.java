/**
 * 
 */
package com.blogspot.jesfre.bibletrivia.web.rest;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogspot.jesfre.bibletrivia.config.Constants;
import com.blogspot.jesfre.bibletrivia.domain.Quiz;
import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.domain.User;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaLevel;
import com.blogspot.jesfre.bibletrivia.service.QuizService;
import com.blogspot.jesfre.bibletrivia.service.TriviaQuestionService;
import com.blogspot.jesfre.bibletrivia.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import tech.jhipster.web.util.HeaderUtil;
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

	// TODO update with information stored in DB
    public static final List<Long> QUESTIONS_ANSWERED = new ArrayList<Long>();

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
    
    @GetMapping("/reset/{level}")
    public ResponseEntity<Void> resetTrivia(@PathVariable("level") TriviaLevel level) {
        LOG.debug("REST request to reset the trivia for: {}", level);
        QUESTIONS_ANSWERED.clear();
        LOG.debug("The Trivia questions have been reset for level {}", level);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<TriviaQuestion> getTriviaQuestionInLevel(@PathVariable("level") TriviaLevel level) {
        LOG.debug("REST request to get a TriviaQuestion for level {}", level);
        
        // TODO exclude already answered questions but using Database instead of static field
        List<TriviaQuestion> questionsInLevel = triviaQuestionService.findInLevel(level);
        if(questionsInLevel.isEmpty()) {
        	return ResponseEntity.noContent()
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "nomorequestions", "No more questions"))
                    .build();
        }
        
        List<TriviaQuestion> availableQuestions = questionsInLevel.stream()
        		.filter(q -> !QUESTIONS_ANSWERED.contains(q.getId()))
        		.collect(Collectors.toList());

        int nextQnumber = new Random().nextInt(questionsInLevel.size());
        Optional<TriviaQuestion> result = Optional.ofNullable(questionsInLevel.get(nextQnumber));

        result.ifPresent(q -> QUESTIONS_ANSWERED.add(q.getId()));
        HttpHeaders headers = new HttpHeaders();
        if(availableQuestions.size() == 1 || QUESTIONS_ANSWERED.size() >= Constants.MAX_NUMBER_OF_QUESTIONS) {
        	// TODO Create a list of state values. Here 2 will be "last question of a trivia"  
            headers.add("x-" + applicationName + "-last-question", "Y");
        }

        return ResponseUtil.wrapOrNotFound(result, headers);
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
