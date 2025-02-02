/**
 * 
 */
package com.blogspot.jesfre.bibletrivia.web.rest;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
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
    public ResponseEntity<Void> resetTrivia(HttpServletRequest request, @PathVariable("level") TriviaLevel level) {
        LOG.debug("REST request to reset the trivia for: {}", level);
        
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        quizService.removeCached(sessionId);
        
        LOG.debug("The Trivia questions have been reset for level {}", level);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<TriviaQuestion> getTriviaQuestionInLevel(HttpServletRequest request, @PathVariable("level") TriviaLevel level) {
        LOG.debug("REST request to get a TriviaQuestion for level {}", level);
        
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        Quiz currentQuiz = quizService.addOrGetCached(sessionId, null);
        
        List<TriviaQuestion> questionsInLevel = triviaQuestionService.findInLevel(level);
        if(questionsInLevel.isEmpty()) {
        	return ResponseEntity.noContent()
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "nomorequestions", "No more questions"))
                    .build();
        }
        
        List<Long> questonsAnswered = currentQuiz.getQuizEntries().stream().map(qe -> qe.getTriviaQuestion().getId()).collect(Collectors.toList());
        
        // Filter out questions already answered to select a new question to display
        List<TriviaQuestion> availableQuestions = questionsInLevel.stream()
        		.filter(q -> !questonsAnswered.contains(q.getId()))
        		.collect(Collectors.toList());

        int nextQnumber = new Random().nextInt(availableQuestions.size());
        Optional<TriviaQuestion> result = Optional.ofNullable(availableQuestions.get(nextQnumber));

        HttpHeaders headers = new HttpHeaders();
        if(availableQuestions.size() == 1 || currentQuiz.getTotalQuestions() >= Constants.MAX_NUMBER_OF_QUESTIONS - 1) {
        	// Is the last available question
        	// TODO Create a list of state values. Here 2 will be "last question of a trivia"  
            headers.add("x-" + applicationName + "-last-question", "Y");
        }

        return ResponseUtil.wrapOrNotFound(result, headers);
    }
    
    @GetMapping("/next/{level}/{currentQuestionId}")
	public ResponseEntity<TriviaQuestion> getNextQuestion(HttpServletRequest request, 
			@PathVariable("level") TriviaLevel level, 
			@PathVariable Long currentQuestionId) throws URISyntaxException {
    	HttpSession session = request.getSession();
        String sessionId = session.getId();
        Quiz currentQuiz = quizService.addOrGetCached(sessionId, null);
        
        int availableQuestionsInDb = Constants.MAX_NUMBER_OF_QUESTIONS;
        Integer currentQuestionOrderNumber = currentQuiz.getQuizEntries().stream()
	        	.filter(qe -> qe.getTriviaQuestion().getId() == currentQuestionId)
	        	.findFirst()
	        	.orElse(new QuizEntry().orderNum(0)) 
	        	.getOrderNum();
        
        QuizEntry nextQentry = currentQuiz.getQuizEntries().stream()
        		.filter(qe -> qe.getOrderNum() == currentQuestionOrderNumber+1)
        		.findFirst().orElse(null);
        
        List<TriviaQuestion> questionsInLevel = triviaQuestionService.findInLevel(level);
    	if(questionsInLevel.isEmpty()) {
    		return ResponseEntity.noContent()
    				.headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "nomorequestions", "No more questions"))
    				.build();
    	}
    	
    	List<Long> questionsAnswered = currentQuiz.getQuizEntries().stream()
    			.map(qe -> qe.getTriviaQuestion().getId())
    			.collect(Collectors.toList());
    	
    	// Filter out questions already answered to select a new question to display
    	List<TriviaQuestion> availableQuestions = questionsInLevel.stream()
    			.filter(q -> !questionsAnswered.contains(q.getId()))
    			.collect(Collectors.toList());
    	
    	availableQuestionsInDb = availableQuestions.size();
        
        if(nextQentry == null) {
        	// Get question from DB
        	int nextQnumber = new Random().nextInt(availableQuestions.size());
        	TriviaQuestion newQuestion = availableQuestions.get(nextQnumber);
        	
        	nextQentry = new QuizEntry()
    				.triviaQuestion(newQuestion)
    				.orderNum(currentQuiz.getTotalQuestions()+1);
        }
        
        
        HttpHeaders headers = new HttpHeaders();
        LOG.debug("nextQentry.getOrderNum()={}, currentQuiz.getTotalQuestions()={}, availableQuestionsInDb={}", 
        		nextQentry.getOrderNum(), currentQuiz.getTotalQuestions(), availableQuestionsInDb);
        if(nextQentry.getOrderNum() == currentQuiz.getTotalQuestions() && availableQuestionsInDb == 0 
        		|| (availableQuestionsInDb == 1 || currentQuiz.getTotalQuestions() >= Constants.MAX_NUMBER_OF_QUESTIONS - 1)) {
            headers.add("x-" + applicationName + "-last-question", "Y");
        } else {
        	headers.add("x-" + applicationName + "-last-question", "N");
        }
        headers.add("x-" + applicationName + "-first-question", "N");
        LOG.debug("Added header for NextQ: {}", headers.toString());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nextQentry.getTriviaQuestion()), headers);
    }
    
    @GetMapping("/previous/{currentQuestionId}")
	public ResponseEntity<TriviaQuestion> getPreviousQuestion(HttpServletRequest request, @PathVariable Long currentQuestionId) throws URISyntaxException {
    	HttpSession session = request.getSession();
        String sessionId = session.getId();
        Quiz currentQuiz = quizService.addOrGetCached(sessionId, null);
        
        currentQuiz.getQuizEntries().stream().forEach(qe -> LOG.debug("QuizEntry's Question: {}", qe.getTriviaQuestion()));
        
        Integer currentQuestionOrderNumber = currentQuiz.getQuizEntries().stream()
	        	.peek(qe -> LOG.debug("Picked question: {}", qe.getTriviaQuestion()))
	        	.filter(qe -> qe.getTriviaQuestion().getId() == currentQuestionId)
	        	.peek(qe -> LOG.debug("Filtered question: {}", qe.getTriviaQuestion()))
	        	.findFirst()
	        	.orElseThrow()
	        	.getOrderNum();
        
        LOG.debug("Got CURRENT QUESTION NUMBER: {}", currentQuestionOrderNumber);
        
        QuizEntry previousQentry = currentQuiz.getQuizEntries().stream()
        		.filter(qe -> qe.getOrderNum() == currentQuestionOrderNumber-1)
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
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(previousQentry.getTriviaQuestion()), headers);
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
        String sessionId = session.getId();
		
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
		boolean alreadyAnsweredQuestion = quiz.getQuizEntries().stream()
				.anyMatch(qe -> qe.getTriviaQuestion().getId() == questionId);
		
		if(alreadyAnsweredQuestion) {
			QuizEntry quizEntry = quiz.getQuizEntries().stream()
			.filter(qe -> qe.getTriviaQuestion().getId() == questionId)
			.collect(Collectors.toList()).get(0);
			
			if(answers != null && answers.size() > 0) {
				answers.forEach(aid -> {
					TriviaAnswer answer = new TriviaAnswer();
					answer.setId(aid);
					quizEntry.addTriviaAnswers(answer);
				});
			}
		} else {
			TriviaQuestion question = triviaQuestionService.findOne(questionId).orElseThrow();
			
			QuizEntry quizEntry = new QuizEntry()
					.triviaQuestion(question)
					.orderNum(quiz.getTotalQuestions()+1);
			
			if(answers != null && answers.size() > 0) {
				answers.forEach(aid -> {
					TriviaAnswer answer = new TriviaAnswer();
					answer.setId(aid);
					quizEntry.addTriviaAnswers(answer);
				});
			}
			
			quiz.addQuizEntries(quizEntry)
				.incrementTotalQuestions();
		}
		
		
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
