package com.blogspot.jesfre.bibletrivia.web.rest;

import static com.blogspot.jesfre.bibletrivia.domain.BibleReferenceAsserts.*;
import static com.blogspot.jesfre.bibletrivia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blogspot.jesfre.bibletrivia.IntegrationTest;
import com.blogspot.jesfre.bibletrivia.domain.BibleReference;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.Book;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.Testament;
import com.blogspot.jesfre.bibletrivia.repository.BibleReferenceRepository;
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
 * Integration tests for the {@link BibleReferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BibleReferenceResourceIT {

    private static final String DEFAULT_BIBLE_VERSE = "AAAAAAAAAA";
    private static final String UPDATED_BIBLE_VERSE = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Book DEFAULT_BOOK = Book.GENESIS;
    private static final Book UPDATED_BOOK = Book.EXODUS;

    private static final Integer DEFAULT_CHAPTER = 1;
    private static final Integer UPDATED_CHAPTER = 2;

    private static final Integer DEFAULT_VERSE = 1;
    private static final Integer UPDATED_VERSE = 2;

    private static final Testament DEFAULT_TESTAMENT = Testament.OLD_TESTAMENT;
    private static final Testament UPDATED_TESTAMENT = Testament.NEW_TESTAMENT;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bible-references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BibleReferenceRepository bibleReferenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBibleReferenceMockMvc;

    private BibleReference bibleReference;

    private BibleReference insertedBibleReference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BibleReference createEntity() {
        return new BibleReference()
            .bibleVerse(DEFAULT_BIBLE_VERSE)
            .version(DEFAULT_VERSION)
            .book(DEFAULT_BOOK)
            .chapter(DEFAULT_CHAPTER)
            .verse(DEFAULT_VERSE)
            .testament(DEFAULT_TESTAMENT)
            .url(DEFAULT_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BibleReference createUpdatedEntity() {
        return new BibleReference()
            .bibleVerse(UPDATED_BIBLE_VERSE)
            .version(UPDATED_VERSION)
            .book(UPDATED_BOOK)
            .chapter(UPDATED_CHAPTER)
            .verse(UPDATED_VERSE)
            .testament(UPDATED_TESTAMENT)
            .url(UPDATED_URL);
    }

    @BeforeEach
    public void initTest() {
        bibleReference = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBibleReference != null) {
            bibleReferenceRepository.delete(insertedBibleReference);
            insertedBibleReference = null;
        }
    }

    @Test
    @Transactional
    void createBibleReference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BibleReference
        var returnedBibleReference = om.readValue(
            restBibleReferenceMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bibleReference))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BibleReference.class
        );

        // Validate the BibleReference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBibleReferenceUpdatableFieldsEquals(returnedBibleReference, getPersistedBibleReference(returnedBibleReference));

        insertedBibleReference = returnedBibleReference;
    }

    @Test
    @Transactional
    void createBibleReferenceWithExistingId() throws Exception {
        // Create the BibleReference with an existing ID
        bibleReference.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBibleReferenceMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bibleReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBibleReferences() throws Exception {
        // Initialize the database
        insertedBibleReference = bibleReferenceRepository.saveAndFlush(bibleReference);

        // Get all the bibleReferenceList
        restBibleReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bibleReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].bibleVerse").value(hasItem(DEFAULT_BIBLE_VERSE)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].book").value(hasItem(DEFAULT_BOOK.toString())))
            .andExpect(jsonPath("$.[*].chapter").value(hasItem(DEFAULT_CHAPTER)))
            .andExpect(jsonPath("$.[*].verse").value(hasItem(DEFAULT_VERSE)))
            .andExpect(jsonPath("$.[*].testament").value(hasItem(DEFAULT_TESTAMENT.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getBibleReference() throws Exception {
        // Initialize the database
        insertedBibleReference = bibleReferenceRepository.saveAndFlush(bibleReference);

        // Get the bibleReference
        restBibleReferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, bibleReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bibleReference.getId().intValue()))
            .andExpect(jsonPath("$.bibleVerse").value(DEFAULT_BIBLE_VERSE))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.book").value(DEFAULT_BOOK.toString()))
            .andExpect(jsonPath("$.chapter").value(DEFAULT_CHAPTER))
            .andExpect(jsonPath("$.verse").value(DEFAULT_VERSE))
            .andExpect(jsonPath("$.testament").value(DEFAULT_TESTAMENT.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingBibleReference() throws Exception {
        // Get the bibleReference
        restBibleReferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBibleReference() throws Exception {
        // Initialize the database
        insertedBibleReference = bibleReferenceRepository.saveAndFlush(bibleReference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bibleReference
        BibleReference updatedBibleReference = bibleReferenceRepository.findById(bibleReference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBibleReference are not directly saved in db
        em.detach(updatedBibleReference);
        updatedBibleReference
            .bibleVerse(UPDATED_BIBLE_VERSE)
            .version(UPDATED_VERSION)
            .book(UPDATED_BOOK)
            .chapter(UPDATED_CHAPTER)
            .verse(UPDATED_VERSE)
            .testament(UPDATED_TESTAMENT)
            .url(UPDATED_URL);

        restBibleReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBibleReference.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBibleReference))
            )
            .andExpect(status().isOk());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBibleReferenceToMatchAllProperties(updatedBibleReference);
    }

    @Test
    @Transactional
    void putNonExistingBibleReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bibleReference.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBibleReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bibleReference.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bibleReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBibleReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bibleReference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBibleReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bibleReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBibleReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bibleReference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBibleReferenceMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bibleReference)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBibleReferenceWithPatch() throws Exception {
        // Initialize the database
        insertedBibleReference = bibleReferenceRepository.saveAndFlush(bibleReference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bibleReference using partial update
        BibleReference partialUpdatedBibleReference = new BibleReference();
        partialUpdatedBibleReference.setId(bibleReference.getId());

        partialUpdatedBibleReference.book(UPDATED_BOOK).testament(UPDATED_TESTAMENT).url(UPDATED_URL);

        restBibleReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBibleReference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBibleReference))
            )
            .andExpect(status().isOk());

        // Validate the BibleReference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBibleReferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBibleReference, bibleReference),
            getPersistedBibleReference(bibleReference)
        );
    }

    @Test
    @Transactional
    void fullUpdateBibleReferenceWithPatch() throws Exception {
        // Initialize the database
        insertedBibleReference = bibleReferenceRepository.saveAndFlush(bibleReference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bibleReference using partial update
        BibleReference partialUpdatedBibleReference = new BibleReference();
        partialUpdatedBibleReference.setId(bibleReference.getId());

        partialUpdatedBibleReference
            .bibleVerse(UPDATED_BIBLE_VERSE)
            .version(UPDATED_VERSION)
            .book(UPDATED_BOOK)
            .chapter(UPDATED_CHAPTER)
            .verse(UPDATED_VERSE)
            .testament(UPDATED_TESTAMENT)
            .url(UPDATED_URL);

        restBibleReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBibleReference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBibleReference))
            )
            .andExpect(status().isOk());

        // Validate the BibleReference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBibleReferenceUpdatableFieldsEquals(partialUpdatedBibleReference, getPersistedBibleReference(partialUpdatedBibleReference));
    }

    @Test
    @Transactional
    void patchNonExistingBibleReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bibleReference.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBibleReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bibleReference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bibleReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBibleReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bibleReference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBibleReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bibleReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBibleReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bibleReference.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBibleReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bibleReference))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BibleReference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBibleReference() throws Exception {
        // Initialize the database
        insertedBibleReference = bibleReferenceRepository.saveAndFlush(bibleReference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bibleReference
        restBibleReferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, bibleReference.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bibleReferenceRepository.count();
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

    protected BibleReference getPersistedBibleReference(BibleReference bibleReference) {
        return bibleReferenceRepository.findById(bibleReference.getId()).orElseThrow();
    }

    protected void assertPersistedBibleReferenceToMatchAllProperties(BibleReference expectedBibleReference) {
        assertBibleReferenceAllPropertiesEquals(expectedBibleReference, getPersistedBibleReference(expectedBibleReference));
    }

    protected void assertPersistedBibleReferenceToMatchUpdatableProperties(BibleReference expectedBibleReference) {
        assertBibleReferenceAllUpdatablePropertiesEquals(expectedBibleReference, getPersistedBibleReference(expectedBibleReference));
    }
}
