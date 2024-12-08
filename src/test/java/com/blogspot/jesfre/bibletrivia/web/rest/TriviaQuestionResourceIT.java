package com.blogspot.jesfre.bibletrivia.web.rest;

import static com.blogspot.jesfre.bibletrivia.domain.TriviaQuestionAsserts.*;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blogspot.jesfre.bibletrivia.IntegrationTest;
import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.AnswerType;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaType;
import com.blogspot.jesfre.bibletrivia.repository.TriviaQuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TriviaQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TriviaQuestionResourceIT {

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;

    private static final TriviaType DEFAULT_QUESTION_TYPE = TriviaType.BIBLICAL;
    private static final TriviaType UPDATED_QUESTION_TYPE = TriviaType.HISTORY;

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final AnswerType DEFAULT_ANSWER_TYPE = AnswerType.SINGLE;
    private static final AnswerType UPDATED_ANSWER_TYPE = AnswerType.MULTIPLE;

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trivia-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TriviaQuestionRepository triviaQuestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTriviaQuestionMockMvc;

    private TriviaQuestion triviaQuestion;

    private TriviaQuestion insertedTriviaQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TriviaQuestion createEntity() {
        return new TriviaQuestion()
            .questionId(DEFAULT_QUESTION_ID)
            .questionType(DEFAULT_QUESTION_TYPE)
            .question(DEFAULT_QUESTION)
            .answerType(DEFAULT_ANSWER_TYPE)
            .value(DEFAULT_VALUE)
            .picture(DEFAULT_PICTURE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TriviaQuestion createUpdatedEntity() {
        return new TriviaQuestion()
            .questionId(UPDATED_QUESTION_ID)
            .questionType(UPDATED_QUESTION_TYPE)
            .question(UPDATED_QUESTION)
            .answerType(UPDATED_ANSWER_TYPE)
            .value(UPDATED_VALUE)
            .picture(UPDATED_PICTURE);
    }

    @BeforeEach
    public void initTest() {
        triviaQuestion = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTriviaQuestion != null) {
            triviaQuestionRepository.delete(insertedTriviaQuestion);
            insertedTriviaQuestion = null;
        }
    }

    @Test
    @Transactional
    void createTriviaQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TriviaQuestion
        var returnedTriviaQuestion = om.readValue(
            restTriviaQuestionMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(triviaQuestion))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TriviaQuestion.class
        );

        // Validate the TriviaQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTriviaQuestionUpdatableFieldsEquals(returnedTriviaQuestion, getPersistedTriviaQuestion(returnedTriviaQuestion));

        insertedTriviaQuestion = returnedTriviaQuestion;
    }

    @Test
    @Transactional
    void createTriviaQuestionWithExistingId() throws Exception {
        // Create the TriviaQuestion with an existing ID
        triviaQuestion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTriviaQuestionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(triviaQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTriviaQuestions() throws Exception {
        // Initialize the database
        insertedTriviaQuestion = triviaQuestionRepository.saveAndFlush(triviaQuestion);

        // Get all the triviaQuestionList
        restTriviaQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(triviaQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionId").value(hasItem(DEFAULT_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].questionType").value(hasItem(DEFAULT_QUESTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].answerType").value(hasItem(DEFAULT_ANSWER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    void getTriviaQuestion() throws Exception {
        // Initialize the database
        insertedTriviaQuestion = triviaQuestionRepository.saveAndFlush(triviaQuestion);

        // Get the triviaQuestion
        restTriviaQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, triviaQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(triviaQuestion.getId().intValue()))
            .andExpect(jsonPath("$.questionId").value(DEFAULT_QUESTION_ID.intValue()))
            .andExpect(jsonPath("$.questionType").value(DEFAULT_QUESTION_TYPE.toString()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.answerType").value(DEFAULT_ANSWER_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingTriviaQuestion() throws Exception {
        // Get the triviaQuestion
        restTriviaQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTriviaQuestion() throws Exception {
        // Initialize the database
        insertedTriviaQuestion = triviaQuestionRepository.saveAndFlush(triviaQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the triviaQuestion
        TriviaQuestion updatedTriviaQuestion = triviaQuestionRepository.findById(triviaQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTriviaQuestion are not directly saved in db
        em.detach(updatedTriviaQuestion);
        updatedTriviaQuestion
            .questionId(UPDATED_QUESTION_ID)
            .questionType(UPDATED_QUESTION_TYPE)
            .question(UPDATED_QUESTION)
            .answerType(UPDATED_ANSWER_TYPE)
            .value(UPDATED_VALUE)
            .picture(UPDATED_PICTURE);

        restTriviaQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTriviaQuestion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTriviaQuestion))
            )
            .andExpect(status().isOk());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTriviaQuestionToMatchAllProperties(updatedTriviaQuestion);
    }

    @Test
    @Transactional
    void putNonExistingTriviaQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaQuestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriviaQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, triviaQuestion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(triviaQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTriviaQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(triviaQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTriviaQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaQuestionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(triviaQuestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTriviaQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedTriviaQuestion = triviaQuestionRepository.saveAndFlush(triviaQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the triviaQuestion using partial update
        TriviaQuestion partialUpdatedTriviaQuestion = new TriviaQuestion();
        partialUpdatedTriviaQuestion.setId(triviaQuestion.getId());

        partialUpdatedTriviaQuestion.questionId(UPDATED_QUESTION_ID).value(UPDATED_VALUE).picture(UPDATED_PICTURE);

        restTriviaQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTriviaQuestion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTriviaQuestion))
            )
            .andExpect(status().isOk());

        // Validate the TriviaQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTriviaQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTriviaQuestion, triviaQuestion),
            getPersistedTriviaQuestion(triviaQuestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateTriviaQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedTriviaQuestion = triviaQuestionRepository.saveAndFlush(triviaQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the triviaQuestion using partial update
        TriviaQuestion partialUpdatedTriviaQuestion = new TriviaQuestion();
        partialUpdatedTriviaQuestion.setId(triviaQuestion.getId());

        partialUpdatedTriviaQuestion
            .questionId(UPDATED_QUESTION_ID)
            .questionType(UPDATED_QUESTION_TYPE)
            .question(UPDATED_QUESTION)
            .answerType(UPDATED_ANSWER_TYPE)
            .value(UPDATED_VALUE)
            .picture(UPDATED_PICTURE);

        restTriviaQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTriviaQuestion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTriviaQuestion))
            )
            .andExpect(status().isOk());

        // Validate the TriviaQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTriviaQuestionUpdatableFieldsEquals(partialUpdatedTriviaQuestion, getPersistedTriviaQuestion(partialUpdatedTriviaQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingTriviaQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaQuestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriviaQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, triviaQuestion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(triviaQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTriviaQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(triviaQuestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTriviaQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaQuestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(triviaQuestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TriviaQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTriviaQuestion() throws Exception {
        // Initialize the database
        insertedTriviaQuestion = triviaQuestionRepository.saveAndFlush(triviaQuestion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the triviaQuestion
        restTriviaQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, triviaQuestion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return triviaQuestionRepository.count();
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

    protected TriviaQuestion getPersistedTriviaQuestion(TriviaQuestion triviaQuestion) {
        return triviaQuestionRepository.findById(triviaQuestion.getId()).orElseThrow();
    }

    protected void assertPersistedTriviaQuestionToMatchAllProperties(TriviaQuestion expectedTriviaQuestion) {
        assertTriviaQuestionAllPropertiesEquals(expectedTriviaQuestion, getPersistedTriviaQuestion(expectedTriviaQuestion));
    }

    protected void assertPersistedTriviaQuestionToMatchUpdatableProperties(TriviaQuestion expectedTriviaQuestion) {
        assertTriviaQuestionAllUpdatablePropertiesEquals(expectedTriviaQuestion, getPersistedTriviaQuestion(expectedTriviaQuestion));
    }
}
