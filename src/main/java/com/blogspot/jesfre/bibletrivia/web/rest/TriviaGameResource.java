/**
 * 
 */
package com.blogspot.jesfre.bibletrivia.web.rest;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
import com.blogspot.jesfre.bibletrivia.service.QuizEntryService;
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

	private static final String ENTITY_NAME = "triviaGameQuestion";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	
	private final TriviaQuestionService triviaQuestionService;
	private final QuizService quizService;
	private final QuizEntryService quizEntryService;
	private final UserService userService;

    public TriviaGameResource(TriviaQuestionService triviaQuestionService, 
    		QuizService quizService, QuizEntryService quizEntryService, UserService userService) {
        this.triviaQuestionService = triviaQuestionService;
        this.quizService = quizService;
        this.quizEntryService = quizEntryService;
        this.userService = userService;
    }
    
    @GetMapping("/reset")
    public ResponseEntity<Void> resetQuiz(HttpServletRequest request) {
        LOG.debug("REST request to reset the quiz");
        
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        quizService.removeCached(sessionId);
        
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/next/{currentQuestionOrder}")
	public ResponseEntity<QuizEntry> getNextQuestion(HttpServletRequest request, 
			@PathVariable Long currentQuestionOrder) throws URISyntaxException {
    	HttpSession session = request.getSession();
        String sessionId = session.getId();
        Quiz currentQuiz = quizService.addOrGetCached(sessionId, null);
        
        LOG.debug("Found quiz i cache: {}", currentQuiz);
        
        
        QuizEntry nextQentry = currentQuiz.getQuizEntries().stream()
        		.filter(qe -> qe.getOrderNum() == currentQuestionOrder+1)
        		.findFirst().orElse(null);
        
        
        HttpHeaders headers = new HttpHeaders();
        if(nextQentry.getOrderNum() == currentQuiz.getTotalQuestions()) {
            headers.add("x-" + applicationName + "-last-question", "Y");
        } else {
        	headers.add("x-" + applicationName + "-last-question", "N");
        }
        if(currentQuestionOrder == 0) {
        	headers.add("x-" + applicationName + "-first-question", "Y");
        } else {
        	headers.add("x-" + applicationName + "-first-question", "N");
        }
        LOG.debug("Added header for NextQ: {}", headers.toString());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nextQentry), headers);
    }
    
    @GetMapping("/previous/{currentQuestionOrder}")
	public ResponseEntity<QuizEntry> getPreviousQuestion(HttpServletRequest request, @PathVariable Long currentQuestionOrder) throws URISyntaxException {
    	HttpSession session = request.getSession();
        String sessionId = session.getId();
        Quiz currentQuiz = quizService.addOrGetCached(sessionId, null);
        
        QuizEntry previousQentry = currentQuiz.getQuizEntries().stream()
        		.filter(qe -> qe.getOrderNum() == currentQuestionOrder-1)
        		.findFirst().orElseThrow();
        
        HttpHeaders headers = new HttpHeaders();
        if(previousQentry.getOrderNum() == 1) {
        	// Is the very first question created in the current quiz
            headers.add("x-" + applicationName + "-first-question", "Y");
        } else {
        	headers.add("x-" + applicationName + "-first-question", "N");
        }
        headers.add("x-" + applicationName + "-last-question", "N");
        LOG.debug("Added heade for PrevQ: {}", headers.toString());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(previousQentry), headers);
    }
	
	@GetMapping("/create/{level}")
	public ResponseEntity<Quiz> createTriviaGame(HttpServletRequest request, @PathVariable("level") TriviaLevel level) throws URISyntaxException {
		LOG.debug("In createTriviaGame... REST request...");
		
		HttpSession session = request.getSession();
        String sessionId = session.getId();
		
		Quiz quiz = quizService.addOrGetCached(sessionId, null);
		
		if(quiz == null) {
			// Create new quiz
			// TODO implement user.getFullName() 
			User usr = getLoggedInUser();
			String ownerName = usr != null ? usr.getFirstName() : "Aonymous" ; 
			
			List<TriviaQuestion> questionsInLevel = triviaQuestionService.findInLevel(level);
	        if(questionsInLevel.isEmpty()) {
	        	return ResponseEntity.noContent()
	                    .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "noquestionsfound", "No questions found."))
	                    .build();
	        }
	        
	        quiz = new Quiz();
			quiz.setOwner(usr);
			quiz.setQuizTaker(ownerName);
			quiz.setStartDate(ZonedDateTime.now());
	        
	        AtomicInteger counter = new AtomicInteger(0);
	        Set<QuizEntry> quizEntries = questionsInLevel.stream()
	        		.unordered()
	        		.limit(Constants.MAX_NUMBER_OF_QUESTIONS)
	        		.map(q -> new QuizEntry()
	        			.orderNum(counter.incrementAndGet())
	        			.triviaQuestion(q)
	        			.triviaAnswers(new HashSet<TriviaAnswer>(q.getTriviaAnswers())))
	        		.collect(Collectors.toSet());
	        quiz.setQuizEntries(quizEntries);
	        quiz.setTotalQuestions(quizEntries.size());
	        
	        quizService.removeCached(sessionId);
	        quizService.addOrGetCached(sessionId, quiz);
		}
		LOG.debug("Retrieving Quiz {}", quiz);
		
		return ResponseUtil.wrapOrNotFound(Optional.of(quiz));
	}
	
	@GetMapping("/update/{questionNum}")
	public ResponseEntity<Void> updateTriviaGame(HttpServletRequest request, 
			@PathVariable("questionNum") Integer questionNum, @RequestParam("answers") List<Long> answers) throws URISyntaxException {
		LOG.debug("Got questionNum={}, asnwers={}", questionNum, answers);
		
		HttpSession session = request.getSession();
        String sessionId = session.getId();
		
		Quiz quiz = quizService.addOrGetCached(sessionId, null);
		quiz.getQuizEntries().stream()
			.filter(qe -> qe.getOrderNum() == questionNum)
			.forEach(qe -> {
				List<Long> correctAnswers = qe.getTriviaAnswers().stream()
						.filter( ta -> ta.getCorrect() )
						.map( ta -> ta.getId() )
						.collect(Collectors.toList());

				boolean isAllCorrectAnswers = answers.containsAll(correctAnswers);
				qe.setCorrect(isAllCorrectAnswers);
				quizService.updateCached(sessionId, quiz);
			});
		
		LOG.debug("Cached quiz: {}", quizService.addOrGetCached(sessionId, null));
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/score/get")
	public ResponseEntity<Quiz> calculateResults(HttpServletRequest request) throws URISyntaxException {
		LOG.debug("Calculating results");
		
		HttpSession session = request.getSession();
        String sessionId = session.getId();
		
		Quiz quiz = quizService.addOrGetCached(sessionId, null);
		int correctCount = (int) quiz.getQuizEntries().stream().filter(qe -> qe.getCorrect()).count();
		quiz.setCorrectQuestions(correctCount);
		
		LOG.debug("Saving quiz: {}", quiz);
		quiz = quizService.save(quiz);
		LOG.debug("Quiz saved");
		
//		Optional<Quiz> fromDb = quizService.findOneWithEntries(quiz.getId());
		
		quiz.setErrorCount(quiz.getTotalQuestions() - correctCount);
		LOG.debug("Returning quiz {}", quiz);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quiz));
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
