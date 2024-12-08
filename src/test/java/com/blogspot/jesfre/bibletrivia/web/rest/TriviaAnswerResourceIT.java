package com.blogspot.jesfre.bibletrivia.web.rest;

import static com.blogspot.jesfre.bibletrivia.domain.TriviaAnswerAsserts.*;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blogspot.jesfre.bibletrivia.IntegrationTest;
import com.blogspot.jesfre.bibletrivia.domain.TriviaAnswer;
import com.blogspot.jesfre.bibletrivia.repository.TriviaAnswerRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaAnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TriviaAnswerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TriviaAnswerResourceIT {

    private static final Long DEFAULT_ANSWER_ID = 1L;
    private static final Long UPDATED_ANSWER_ID = 2L;

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    private static final String DEFAULT_PICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trivia-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TriviaAnswerRepository triviaAnswerRepository;

    @Mock
    private TriviaAnswerRepository triviaAnswerRepositoryMock;

    @Mock
    private TriviaAnswerService triviaAnswerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTriviaAnswerMockMvc;

    private TriviaAnswer triviaAnswer;

    private TriviaAnswer insertedTriviaAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TriviaAnswer createEntity() {
        return new TriviaAnswer()
            .answerId(DEFAULT_ANSWER_ID)
            .answer(DEFAULT_ANSWER)
            .explanation(DEFAULT_EXPLANATION)
            .correct(DEFAULT_CORRECT)
            .picture(DEFAULT_PICTURE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TriviaAnswer createUpdatedEntity() {
        return new TriviaAnswer()
            .answerId(UPDATED_ANSWER_ID)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .correct(UPDATED_CORRECT)
            .picture(UPDATED_PICTURE);
    }

    @BeforeEach
    public void initTest() {
        triviaAnswer = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTriviaAnswer != null) {
            triviaAnswerRepository.delete(insertedTriviaAnswer);
            insertedTriviaAnswer = null;
        }
    }

    @Test
    @Transactional
    void createTriviaAnswer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TriviaAnswer
        var returnedTriviaAnswer = om.readValue(
            restTriviaAnswerMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(triviaAnswer))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TriviaAnswer.class
        );

        // Validate the TriviaAnswer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTriviaAnswerUpdatableFieldsEquals(returnedTriviaAnswer, getPersistedTriviaAnswer(returnedTriviaAnswer));

        insertedTriviaAnswer = returnedTriviaAnswer;
    }

    @Test
    @Transactional
    void createTriviaAnswerWithExistingId() throws Exception {
        // Create the TriviaAnswer with an existing ID
        triviaAnswer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTriviaAnswerMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(triviaAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTriviaAnswers() throws Exception {
        // Initialize the database
        insertedTriviaAnswer = triviaAnswerRepository.saveAndFlush(triviaAnswer);

        // Get all the triviaAnswerList
        restTriviaAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(triviaAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].answerId").value(hasItem(DEFAULT_ANSWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTriviaAnswersWithEagerRelationshipsIsEnabled() throws Exception {
        when(triviaAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTriviaAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(triviaAnswerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTriviaAnswersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(triviaAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTriviaAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(triviaAnswerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTriviaAnswer() throws Exception {
        // Initialize the database
        insertedTriviaAnswer = triviaAnswerRepository.saveAndFlush(triviaAnswer);

        // Get the triviaAnswer
        restTriviaAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, triviaAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(triviaAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answerId").value(DEFAULT_ANSWER_ID.intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE));
    }

    @Test
    @Transactional
    void getNonExistingTriviaAnswer() throws Exception {
        // Get the triviaAnswer
        restTriviaAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTriviaAnswer() throws Exception {
        // Initialize the database
        insertedTriviaAnswer = triviaAnswerRepository.saveAndFlush(triviaAnswer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the triviaAnswer
        TriviaAnswer updatedTriviaAnswer = triviaAnswerRepository.findById(triviaAnswer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTriviaAnswer are not directly saved in db
        em.detach(updatedTriviaAnswer);
        updatedTriviaAnswer
            .answerId(UPDATED_ANSWER_ID)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .correct(UPDATED_CORRECT)
            .picture(UPDATED_PICTURE);

        restTriviaAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTriviaAnswer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTriviaAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTriviaAnswerToMatchAllProperties(updatedTriviaAnswer);
    }

    @Test
    @Transactional
    void putNonExistingTriviaAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaAnswer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriviaAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, triviaAnswer.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(triviaAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTriviaAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaAnswer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(triviaAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTriviaAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaAnswer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaAnswerMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(triviaAnswer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTriviaAnswerWithPatch() throws Exception {
        // Initialize the database
        insertedTriviaAnswer = triviaAnswerRepository.saveAndFlush(triviaAnswer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the triviaAnswer using partial update
        TriviaAnswer partialUpdatedTriviaAnswer = new TriviaAnswer();
        partialUpdatedTriviaAnswer.setId(triviaAnswer.getId());

        partialUpdatedTriviaAnswer
            .answerId(UPDATED_ANSWER_ID)
            .explanation(UPDATED_EXPLANATION)
            .correct(UPDATED_CORRECT)
            .picture(UPDATED_PICTURE);

        restTriviaAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTriviaAnswer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTriviaAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TriviaAnswer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTriviaAnswerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTriviaAnswer, triviaAnswer),
            getPersistedTriviaAnswer(triviaAnswer)
        );
    }

    @Test
    @Transactional
    void fullUpdateTriviaAnswerWithPatch() throws Exception {
        // Initialize the database
        insertedTriviaAnswer = triviaAnswerRepository.saveAndFlush(triviaAnswer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the triviaAnswer using partial update
        TriviaAnswer partialUpdatedTriviaAnswer = new TriviaAnswer();
        partialUpdatedTriviaAnswer.setId(triviaAnswer.getId());

        partialUpdatedTriviaAnswer
            .answerId(UPDATED_ANSWER_ID)
            .answer(UPDATED_ANSWER)
            .explanation(UPDATED_EXPLANATION)
            .correct(UPDATED_CORRECT)
            .picture(UPDATED_PICTURE);

        restTriviaAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTriviaAnswer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTriviaAnswer))
            )
            .andExpect(status().isOk());

        // Validate the TriviaAnswer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTriviaAnswerUpdatableFieldsEquals(partialUpdatedTriviaAnswer, getPersistedTriviaAnswer(partialUpdatedTriviaAnswer));
    }

    @Test
    @Transactional
    void patchNonExistingTriviaAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaAnswer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriviaAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, triviaAnswer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(triviaAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTriviaAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaAnswer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(triviaAnswer))
            )
            .andExpect(status().isBadRequest());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTriviaAnswer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        triviaAnswer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(triviaAnswer))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TriviaAnswer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTriviaAnswer() throws Exception {
        // Initialize the database
        insertedTriviaAnswer = triviaAnswerRepository.saveAndFlush(triviaAnswer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the triviaAnswer
        restTriviaAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, triviaAnswer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return triviaAnswerRepository.count();
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

    protected TriviaAnswer getPersistedTriviaAnswer(TriviaAnswer triviaAnswer) {
        return triviaAnswerRepository.findById(triviaAnswer.getId()).orElseThrow();
    }

    protected void assertPersistedTriviaAnswerToMatchAllProperties(TriviaAnswer expectedTriviaAnswer) {
        assertTriviaAnswerAllPropertiesEquals(expectedTriviaAnswer, getPersistedTriviaAnswer(expectedTriviaAnswer));
    }

    protected void assertPersistedTriviaAnswerToMatchUpdatableProperties(TriviaAnswer expectedTriviaAnswer) {
        assertTriviaAnswerAllUpdatablePropertiesEquals(expectedTriviaAnswer, getPersistedTriviaAnswer(expectedTriviaAnswer));
    }
}
