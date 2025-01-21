/**
 * 
 */
package com.blogspot.jesfre.bibletrivia.web.rest;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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
import org.springframework.web.bind.annotation.RestController;

import com.blogspot.jesfre.bibletrivia.domain.Quiz;
import com.blogspot.jesfre.bibletrivia.domain.TriviaGameQuestion;
import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.domain.User;
import com.blogspot.jesfre.bibletrivia.repository.TriviaQuestionRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaQuestionService;
import com.blogspot.jesfre.bibletrivia.service.UserService;
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
	private final UserService userService;

    public TriviaGameResource(TriviaQuestionService triviaQuestionService, UserService userService) {
        this.triviaQuestionService = triviaQuestionService;
        this.userService = userService;
    }
	
	@GetMapping("/create")
	public ResponseEntity<Quiz> createTriviaGame() throws URISyntaxException {
		LOG.debug("In createTriviaGame... REST request...");

		User usr = getLoggedInUser();
		
		LOG.debug("Got user {}", usr);
		
		// TODO implement user.getFullName() 
		String ownerName = usr != null ? usr.getFirstName() : "Aonymous" ; 
		
		LOG.debug("Got quiztaker {}", ownerName);
		
		Quiz quiz = new Quiz();
		quiz.setOwner(usr);
		quiz.setQuizTaker(ownerName);
		quiz.setStartDate(ZonedDateTime.now());
		
		return ResponseUtil.wrapOrNotFound(Optional.of(quiz));
	}
	
	public String isQuizOwnerName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			  return "Anonymous";
		}
		return authentication.getName();
	}
	
	public User getLoggedInUser() {
		LOG.debug("In getLoggedInUser...");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  if (authentication != null || authentication instanceof AnonymousAuthenticationToken) {
			  String login = authentication.getName();
			  LOG.debug("Got Principal's login {}", login);
              User user = userService.getUserWithAuthoritiesByLogin(login)
                .orElseThrow( () -> new IllegalStateException("User not found when creating a new trivia game.") );
              return user;
		}
		return null;
	}
}
