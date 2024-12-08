package com.blogspot.jesfre.bibletrivia.web.rest;

import static com.blogspot.jesfre.bibletrivia.domain.TriviaAsserts.*;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blogspot.jesfre.bibletrivia.IntegrationTest;
import com.blogspot.jesfre.bibletrivia.domain.Trivia;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaLevel;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaType;
import com.blogspot.jesfre.bibletrivia.repository.TriviaRepository;
import com.blogspot.jesfre.bibletrivia.service.TriviaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TriviaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TriviaResourceIT {

    private static final TriviaLevel DEFAULT_LEVEL = TriviaLevel.EASY;
    private static final TriviaLevel UPDATED_LEVEL = TriviaLevel.DIFFICULT;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final TriviaType DEFAULT_TYPE = TriviaType.BIBLICAL;
    private static final TriviaType UPDATED_TYPE = TriviaType.HISTORY;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/trivias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TriviaRepository triviaRepository;

    @Mock
    private TriviaRepository triviaRepositoryMock;

    @Mock
    private TriviaService triviaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTriviaMockMvc;

    private Trivia trivia;

    private Trivia insertedTrivia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trivia createEntity() {
        return new Trivia()
            .level(DEFAULT_LEVEL)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trivia createUpdatedEntity() {
        return new Trivia()
            .level(UPDATED_LEVEL)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
    }

    @BeforeEach
    public void initTest() {
        trivia = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTrivia != null) {
            triviaRepository.delete(insertedTrivia);
            insertedTrivia = null;
        }
    }

    @Test
    @Transactional
    void createTrivia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trivia
        var returnedTrivia = om.readValue(
            restTriviaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trivia)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Trivia.class
        );

        // Validate the Trivia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTriviaUpdatableFieldsEquals(returnedTrivia, getPersistedTrivia(returnedTrivia));

        insertedTrivia = returnedTrivia;
    }

    @Test
    @Transactional
    void createTriviaWithExistingId() throws Exception {
        // Create the Trivia with an existing ID
        trivia.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTriviaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trivia)))
            .andExpect(status().isBadRequest());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrivias() throws Exception {
        // Initialize the database
        insertedTrivia = triviaRepository.saveAndFlush(trivia);

        // Get all the triviaList
        restTriviaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trivia.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTriviasWithEagerRelationshipsIsEnabled() throws Exception {
        when(triviaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTriviaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(triviaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTriviasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(triviaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTriviaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(triviaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrivia() throws Exception {
        // Initialize the database
        insertedTrivia = triviaRepository.saveAndFlush(trivia);

        // Get the trivia
        restTriviaMockMvc
            .perform(get(ENTITY_API_URL_ID, trivia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trivia.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTrivia() throws Exception {
        // Get the trivia
        restTriviaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrivia() throws Exception {
        // Initialize the database
        insertedTrivia = triviaRepository.saveAndFlush(trivia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trivia
        Trivia updatedTrivia = triviaRepository.findById(trivia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrivia are not directly saved in db
        em.detach(updatedTrivia);
        updatedTrivia.level(UPDATED_LEVEL).name(UPDATED_NAME).type(UPDATED_TYPE).startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE);

        restTriviaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrivia.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTrivia))
            )
            .andExpect(status().isOk());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTriviaToMatchAllProperties(updatedTrivia);
    }

    @Test
    @Transactional
    void putNonExistingTrivia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trivia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriviaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trivia.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trivia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrivia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trivia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trivia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrivia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trivia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trivia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTriviaWithPatch() throws Exception {
        // Initialize the database
        insertedTrivia = triviaRepository.saveAndFlush(trivia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trivia using partial update
        Trivia partialUpdatedTrivia = new Trivia();
        partialUpdatedTrivia.setId(trivia.getId());

        partialUpdatedTrivia.name(UPDATED_NAME);

        restTriviaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrivia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrivia))
            )
            .andExpect(status().isOk());

        // Validate the Trivia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTriviaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrivia, trivia), getPersistedTrivia(trivia));
    }

    @Test
    @Transactional
    void fullUpdateTriviaWithPatch() throws Exception {
        // Initialize the database
        insertedTrivia = triviaRepository.saveAndFlush(trivia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trivia using partial update
        Trivia partialUpdatedTrivia = new Trivia();
        partialUpdatedTrivia.setId(trivia.getId());

        partialUpdatedTrivia
            .level(UPDATED_LEVEL)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restTriviaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrivia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrivia))
            )
            .andExpect(status().isOk());

        // Validate the Trivia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTriviaUpdatableFieldsEquals(partialUpdatedTrivia, getPersistedTrivia(partialUpdatedTrivia));
    }

    @Test
    @Transactional
    void patchNonExistingTrivia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trivia.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriviaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trivia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trivia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrivia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trivia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trivia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrivia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trivia.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTriviaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trivia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trivia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrivia() throws Exception {
        // Initialize the database
        insertedTrivia = triviaRepository.saveAndFlush(trivia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trivia
        restTriviaMockMvc
            .perform(delete(ENTITY_API_URL_ID, trivia.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return triviaRepository.count();
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

    protected Trivia getPersistedTrivia(Trivia trivia) {
        return triviaRepository.findById(trivia.getId()).orElseThrow();
    }

    protected void assertPersistedTriviaToMatchAllProperties(Trivia expectedTrivia) {
        assertTriviaAllPropertiesEquals(expectedTrivia, getPersistedTrivia(expectedTrivia));
    }

    protected void assertPersistedTriviaToMatchUpdatableProperties(Trivia expectedTrivia) {
        assertTriviaAllUpdatablePropertiesEquals(expectedTrivia, getPersistedTrivia(expectedTrivia));
    }
}
