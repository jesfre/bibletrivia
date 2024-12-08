package com.blogspot.jesfre.bibletrivia.web.rest;

import com.blogspot.jesfre.bibletrivia.domain.BibleReference;
import com.blogspot.jesfre.bibletrivia.repository.BibleReferenceRepository;
import com.blogspot.jesfre.bibletrivia.service.BibleReferenceService;
import com.blogspot.jesfre.bibletrivia.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blogspot.jesfre.bibletrivia.domain.BibleReference}.
 */
@RestController
@RequestMapping("/api/bible-references")
public class BibleReferenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(BibleReferenceResource.class);

    private static final String ENTITY_NAME = "bibleReference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BibleReferenceService bibleReferenceService;

    private final BibleReferenceRepository bibleReferenceRepository;

    public BibleReferenceResource(BibleReferenceService bibleReferenceService, BibleReferenceRepository bibleReferenceRepository) {
        this.bibleReferenceService = bibleReferenceService;
        this.bibleReferenceRepository = bibleReferenceRepository;
    }

    /**
     * {@code POST  /bible-references} : Create a new bibleReference.
     *
     * @param bibleReference the bibleReference to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bibleReference, or with status {@code 400 (Bad Request)} if the bibleReference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BibleReference> createBibleReference(@RequestBody BibleReference bibleReference) throws URISyntaxException {
        LOG.debug("REST request to save BibleReference : {}", bibleReference);
        if (bibleReference.getId() != null) {
            throw new BadRequestAlertException("A new bibleReference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bibleReference = bibleReferenceService.save(bibleReference);
        return ResponseEntity.created(new URI("/api/bible-references/" + bibleReference.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bibleReference.getId().toString()))
            .body(bibleReference);
    }

    /**
     * {@code PUT  /bible-references/:id} : Updates an existing bibleReference.
     *
     * @param id the id of the bibleReference to save.
     * @param bibleReference the bibleReference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bibleReference,
     * or with status {@code 400 (Bad Request)} if the bibleReference is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bibleReference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BibleReference> updateBibleReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BibleReference bibleReference
    ) throws URISyntaxException {
        LOG.debug("REST request to update BibleReference : {}, {}", id, bibleReference);
        if (bibleReference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bibleReference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bibleReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bibleReference = bibleReferenceService.update(bibleReference);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bibleReference.getId().toString()))
            .body(bibleReference);
    }

    /**
     * {@code PATCH  /bible-references/:id} : Partial updates given fields of an existing bibleReference, field will ignore if it is null
     *
     * @param id the id of the bibleReference to save.
     * @param bibleReference the bibleReference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bibleReference,
     * or with status {@code 400 (Bad Request)} if the bibleReference is not valid,
     * or with status {@code 404 (Not Found)} if the bibleReference is not found,
     * or with status {@code 500 (Internal Server Error)} if the bibleReference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BibleReference> partialUpdateBibleReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BibleReference bibleReference
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BibleReference partially : {}, {}", id, bibleReference);
        if (bibleReference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bibleReference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bibleReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BibleReference> result = bibleReferenceService.partialUpdate(bibleReference);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bibleReference.getId().toString())
        );
    }

    /**
     * {@code GET  /bible-references} : get all the bibleReferences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bibleReferences in body.
     */
    @GetMapping("")
    public List<BibleReference> getAllBibleReferences() {
        LOG.debug("REST request to get all BibleReferences");
        return bibleReferenceService.findAll();
    }

    /**
     * {@code GET  /bible-references/:id} : get the "id" bibleReference.
     *
     * @param id the id of the bibleReference to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bibleReference, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BibleReference> getBibleReference(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BibleReference : {}", id);
        Optional<BibleReference> bibleReference = bibleReferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bibleReference);
    }

    /**
     * {@code DELETE  /bible-references/:id} : delete the "id" bibleReference.
     *
     * @param id the id of the bibleReference to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBibleReference(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BibleReference : {}", id);
        bibleReferenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
