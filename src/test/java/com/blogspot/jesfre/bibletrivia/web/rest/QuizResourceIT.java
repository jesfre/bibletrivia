package com.blogspot.jesfre.bibletrivia.web.rest;

import static com.blogspot.jesfre.bibletrivia.domain.QuizAsserts.*;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blogspot.jesfre.bibletrivia.IntegrationTest;
import com.blogspot.jesfre.bibletrivia.domain.Quiz;
import com.blogspot.jesfre.bibletrivia.repository.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuizResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizResourceIT {

    private static final String DEFAULT_QUIZ_TAKER = "AAAAAAAAAA";
    private static final String UPDATED_QUIZ_TAKER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_TOTAL_QUESTIONS = 1;
    private static final Integer UPDATED_TOTAL_QUESTIONS = 2;

    private static final Integer DEFAULT_CORRECT_QUESTIONS = 1;
    private static final Integer UPDATED_CORRECT_QUESTIONS = 2;

    private static final String ENTITY_API_URL = "/api/quizzes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizMockMvc;

    private Quiz quiz;

    private Quiz insertedQuiz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiz createEntity() {
        return new Quiz()
            .quizTaker(DEFAULT_QUIZ_TAKER)
            .startDate(DEFAULT_START_DATE)
            .totalQuestions(DEFAULT_TOTAL_QUESTIONS)
            .correctQuestions(DEFAULT_CORRECT_QUESTIONS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quiz createUpdatedEntity() {
        return new Quiz()
            .quizTaker(UPDATED_QUIZ_TAKER)
            .startDate(UPDATED_START_DATE)
            .totalQuestions(UPDATED_TOTAL_QUESTIONS)
            .correctQuestions(UPDATED_CORRECT_QUESTIONS);
    }

    @BeforeEach
    public void initTest() {
        quiz = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedQuiz != null) {
            quizRepository.delete(insertedQuiz);
            insertedQuiz = null;
        }
    }

    @Test
    @Transactional
    void createQuiz() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Quiz
        var returnedQuiz = om.readValue(
            restQuizMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quiz)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Quiz.class
        );

        // Validate the Quiz in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuizUpdatableFieldsEquals(returnedQuiz, getPersistedQuiz(returnedQuiz));

        insertedQuiz = returnedQuiz;
    }

    @Test
    @Transactional
    void createQuizWithExistingId() throws Exception {
        // Create the Quiz with an existing ID
        quiz.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quiz)))
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuizTakerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quiz.setQuizTaker(null);

        // Create the Quiz, which fails.

        restQuizMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quiz)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quiz.setStartDate(null);

        // Create the Quiz, which fails.

        restQuizMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quiz)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizzes() throws Exception {
        // Initialize the database
        insertedQuiz = quizRepository.saveAndFlush(quiz);

        // Get all the quizList
        restQuizMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quiz.getId().intValue())))
            .andExpect(jsonPath("$.[*].quizTaker").value(hasItem(DEFAULT_QUIZ_TAKER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].totalQuestions").value(hasItem(DEFAULT_TOTAL_QUESTIONS)))
            .andExpect(jsonPath("$.[*].correctQuestions").value(hasItem(DEFAULT_CORRECT_QUESTIONS)));
    }

    @Test
    @Transactional
    void getQuiz() throws Exception {
        // Initialize the database
        insertedQuiz = quizRepository.saveAndFlush(quiz);

        // Get the quiz
        restQuizMockMvc
            .perform(get(ENTITY_API_URL_ID, quiz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quiz.getId().intValue()))
            .andExpect(jsonPath("$.quizTaker").value(DEFAULT_QUIZ_TAKER))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.totalQuestions").value(DEFAULT_TOTAL_QUESTIONS))
            .andExpect(jsonPath("$.correctQuestions").value(DEFAULT_CORRECT_QUESTIONS));
    }

    @Test
    @Transactional
    void getNonExistingQuiz() throws Exception {
        // Get the quiz
        restQuizMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuiz() throws Exception {
        // Initialize the database
        insertedQuiz = quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quiz
        Quiz updatedQuiz = quizRepository.findById(quiz.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuiz are not directly saved in db
        em.detach(updatedQuiz);
        updatedQuiz
            .quizTaker(UPDATED_QUIZ_TAKER)
            .startDate(UPDATED_START_DATE)
            .totalQuestions(UPDATED_TOTAL_QUESTIONS)
            .correctQuestions(UPDATED_CORRECT_QUESTIONS);

        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuiz.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizToMatchAllProperties(updatedQuiz);
    }

    @Test
    @Transactional
    void putNonExistingQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quiz.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quiz))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quiz))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quiz)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizWithPatch() throws Exception {
        // Initialize the database
        insertedQuiz = quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quiz using partial update
        Quiz partialUpdatedQuiz = new Quiz();
        partialUpdatedQuiz.setId(quiz.getId());

        partialUpdatedQuiz.quizTaker(UPDATED_QUIZ_TAKER).totalQuestions(UPDATED_TOTAL_QUESTIONS);

        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiz.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuiz, quiz), getPersistedQuiz(quiz));
    }

    @Test
    @Transactional
    void fullUpdateQuizWithPatch() throws Exception {
        // Initialize the database
        insertedQuiz = quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quiz using partial update
        Quiz partialUpdatedQuiz = new Quiz();
        partialUpdatedQuiz.setId(quiz.getId());

        partialUpdatedQuiz
            .quizTaker(UPDATED_QUIZ_TAKER)
            .startDate(UPDATED_START_DATE)
            .totalQuestions(UPDATED_TOTAL_QUESTIONS)
            .correctQuestions(UPDATED_CORRECT_QUESTIONS);

        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuiz.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuiz))
            )
            .andExpect(status().isOk());

        // Validate the Quiz in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizUpdatableFieldsEquals(partialUpdatedQuiz, getPersistedQuiz(partialUpdatedQuiz));
    }

    @Test
    @Transactional
    void patchNonExistingQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quiz.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quiz))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quiz))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuiz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quiz.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quiz)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quiz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuiz() throws Exception {
        // Initialize the database
        insertedQuiz = quizRepository.saveAndFlush(quiz);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quiz
        restQuizMockMvc
            .perform(delete(ENTITY_API_URL_ID, quiz.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Quiz getPersistedQuiz(Quiz quiz) {
        return quizRepository.findById(quiz.getId()).orElseThrow();
    }

    protected void assertPersistedQuizToMatchAllProperties(Quiz expectedQuiz) {
        assertQuizAllPropertiesEquals(expectedQuiz, getPersistedQuiz(expectedQuiz));
    }

    protected void assertPersistedQuizToMatchUpdatableProperties(Quiz expectedQuiz) {
        assertQuizAllUpdatablePropertiesEquals(expectedQuiz, getPersistedQuiz(expectedQuiz));
    }
}
