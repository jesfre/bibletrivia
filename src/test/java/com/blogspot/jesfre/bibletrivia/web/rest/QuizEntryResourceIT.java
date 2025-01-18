package com.blogspot.jesfre.bibletrivia.web.rest;

import static com.blogspot.jesfre.bibletrivia.domain.QuizEntryAsserts.*;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blogspot.jesfre.bibletrivia.IntegrationTest;
import com.blogspot.jesfre.bibletrivia.domain.QuizEntry;
import com.blogspot.jesfre.bibletrivia.domain.TriviaQuestion;
import com.blogspot.jesfre.bibletrivia.repository.QuizEntryRepository;
import com.blogspot.jesfre.bibletrivia.service.QuizEntryService;
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
 * Integration tests for the {@link QuizEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuizEntryResourceIT {

    private static final Integer DEFAULT_ORDER_NUM = 1;
    private static final Integer UPDATED_ORDER_NUM = 2;

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    private static final String ENTITY_API_URL = "/api/quiz-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuizEntryRepository quizEntryRepository;

    @Mock
    private QuizEntryRepository quizEntryRepositoryMock;

    @Mock
    private QuizEntryService quizEntryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizEntryMockMvc;

    private QuizEntry quizEntry;

    private QuizEntry insertedQuizEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizEntry createEntity(EntityManager em) {
        QuizEntry quizEntry = new QuizEntry().orderNum(DEFAULT_ORDER_NUM).correct(DEFAULT_CORRECT);
        // Add required entity
        TriviaQuestion triviaQuestion;
        if (TestUtil.findAll(em, TriviaQuestion.class).isEmpty()) {
            triviaQuestion = TriviaQuestionResourceIT.createEntity();
            em.persist(triviaQuestion);
            em.flush();
        } else {
            triviaQuestion = TestUtil.findAll(em, TriviaQuestion.class).get(0);
        }
        quizEntry.setTriviaQuestion(triviaQuestion);
        return quizEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizEntry createUpdatedEntity(EntityManager em) {
        QuizEntry updatedQuizEntry = new QuizEntry().orderNum(UPDATED_ORDER_NUM).correct(UPDATED_CORRECT);
        // Add required entity
        TriviaQuestion triviaQuestion;
        if (TestUtil.findAll(em, TriviaQuestion.class).isEmpty()) {
            triviaQuestion = TriviaQuestionResourceIT.createUpdatedEntity();
            em.persist(triviaQuestion);
            em.flush();
        } else {
            triviaQuestion = TestUtil.findAll(em, TriviaQuestion.class).get(0);
        }
        updatedQuizEntry.setTriviaQuestion(triviaQuestion);
        return updatedQuizEntry;
    }

    @BeforeEach
    public void initTest() {
        quizEntry = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedQuizEntry != null) {
            quizEntryRepository.delete(insertedQuizEntry);
            insertedQuizEntry = null;
        }
    }

    @Test
    @Transactional
    void createQuizEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuizEntry
        var returnedQuizEntry = om.readValue(
            restQuizEntryMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizEntry)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuizEntry.class
        );

        // Validate the QuizEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQuizEntryUpdatableFieldsEquals(returnedQuizEntry, getPersistedQuizEntry(returnedQuizEntry));

        insertedQuizEntry = returnedQuizEntry;
    }

    @Test
    @Transactional
    void createQuizEntryWithExistingId() throws Exception {
        // Create the QuizEntry with an existing ID
        quizEntry.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizEntryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizEntry)))
            .andExpect(status().isBadRequest());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderNumIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quizEntry.setOrderNum(null);

        // Create the QuizEntry, which fails.

        restQuizEntryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizEntry)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizEntries() throws Exception {
        // Initialize the database
        insertedQuizEntry = quizEntryRepository.saveAndFlush(quizEntry);

        // Get all the quizEntryList
        restQuizEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuizEntriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(quizEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuizEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quizEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuizEntriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quizEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuizEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quizEntryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuizEntry() throws Exception {
        // Initialize the database
        insertedQuizEntry = quizEntryRepository.saveAndFlush(quizEntry);

        // Get the quizEntry
        restQuizEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, quizEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizEntry.getId().intValue()))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuizEntry() throws Exception {
        // Get the quizEntry
        restQuizEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizEntry() throws Exception {
        // Initialize the database
        insertedQuizEntry = quizEntryRepository.saveAndFlush(quizEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizEntry
        QuizEntry updatedQuizEntry = quizEntryRepository.findById(quizEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizEntry are not directly saved in db
        em.detach(updatedQuizEntry);
        updatedQuizEntry.orderNum(UPDATED_ORDER_NUM).correct(UPDATED_CORRECT);

        restQuizEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuizEntry.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQuizEntry))
            )
            .andExpect(status().isOk());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuizEntryToMatchAllProperties(updatedQuizEntry);
    }

    @Test
    @Transactional
    void putNonExistingQuizEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizEntry.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizEntry.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizEntry.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quizEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizEntry.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizEntryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quizEntry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizEntryWithPatch() throws Exception {
        // Initialize the database
        insertedQuizEntry = quizEntryRepository.saveAndFlush(quizEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizEntry using partial update
        QuizEntry partialUpdatedQuizEntry = new QuizEntry();
        partialUpdatedQuizEntry.setId(quizEntry.getId());

        partialUpdatedQuizEntry.orderNum(UPDATED_ORDER_NUM).correct(UPDATED_CORRECT);

        restQuizEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizEntry))
            )
            .andExpect(status().isOk());

        // Validate the QuizEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuizEntry, quizEntry),
            getPersistedQuizEntry(quizEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuizEntryWithPatch() throws Exception {
        // Initialize the database
        insertedQuizEntry = quizEntryRepository.saveAndFlush(quizEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quizEntry using partial update
        QuizEntry partialUpdatedQuizEntry = new QuizEntry();
        partialUpdatedQuizEntry.setId(quizEntry.getId());

        partialUpdatedQuizEntry.orderNum(UPDATED_ORDER_NUM).correct(UPDATED_CORRECT);

        restQuizEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuizEntry))
            )
            .andExpect(status().isOk());

        // Validate the QuizEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuizEntryUpdatableFieldsEquals(partialUpdatedQuizEntry, getPersistedQuizEntry(partialUpdatedQuizEntry));
    }

    @Test
    @Transactional
    void patchNonExistingQuizEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizEntry.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizEntry.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizEntry.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quizEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quizEntry.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizEntryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quizEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizEntry() throws Exception {
        // Initialize the database
        insertedQuizEntry = quizEntryRepository.saveAndFlush(quizEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quizEntry
        restQuizEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizEntry.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quizEntryRepository.count();
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

    protected QuizEntry getPersistedQuizEntry(QuizEntry quizEntry) {
        return quizEntryRepository.findById(quizEntry.getId()).orElseThrow();
    }

    protected void assertPersistedQuizEntryToMatchAllProperties(QuizEntry expectedQuizEntry) {
        assertQuizEntryAllPropertiesEquals(expectedQuizEntry, getPersistedQuizEntry(expectedQuizEntry));
    }

    protected void assertPersistedQuizEntryToMatchUpdatableProperties(QuizEntry expectedQuizEntry) {
        assertQuizEntryAllUpdatablePropertiesEquals(expectedQuizEntry, getPersistedQuizEntry(expectedQuizEntry));
    }
}
