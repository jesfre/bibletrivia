package com.blogspot.jesfre.bibletrivia.service.impl;

import com.blogspot.jesfre.bibletrivia.domain.BibleReference;
import com.blogspot.jesfre.bibletrivia.repository.BibleReferenceRepository;
import com.blogspot.jesfre.bibletrivia.service.BibleReferenceService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blogspot.jesfre.bibletrivia.domain.BibleReference}.
 */
@Service
@Transactional
public class BibleReferenceServiceImpl implements BibleReferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(BibleReferenceServiceImpl.class);

    private final BibleReferenceRepository bibleReferenceRepository;

    public BibleReferenceServiceImpl(BibleReferenceRepository bibleReferenceRepository) {
        this.bibleReferenceRepository = bibleReferenceRepository;
    }

    @Override
    public BibleReference save(BibleReference bibleReference) {
        LOG.debug("Request to save BibleReference : {}", bibleReference);
        return bibleReferenceRepository.save(bibleReference);
    }

    @Override
    public BibleReference update(BibleReference bibleReference) {
        LOG.debug("Request to update BibleReference : {}", bibleReference);
        return bibleReferenceRepository.save(bibleReference);
    }

    @Override
    public Optional<BibleReference> partialUpdate(BibleReference bibleReference) {
        LOG.debug("Request to partially update BibleReference : {}", bibleReference);

        return bibleReferenceRepository
            .findById(bibleReference.getId())
            .map(existingBibleReference -> {
                if (bibleReference.getBibleVerse() != null) {
                    existingBibleReference.setBibleVerse(bibleReference.getBibleVerse());
                }
                if (bibleReference.getVersion() != null) {
                    existingBibleReference.setVersion(bibleReference.getVersion());
                }
                if (bibleReference.getBook() != null) {
                    existingBibleReference.setBook(bibleReference.getBook());
                }
                if (bibleReference.getChapter() != null) {
                    existingBibleReference.setChapter(bibleReference.getChapter());
                }
                if (bibleReference.getVerse() != null) {
                    existingBibleReference.setVerse(bibleReference.getVerse());
                }
                if (bibleReference.getTestament() != null) {
                    existingBibleReference.setTestament(bibleReference.getTestament());
                }
                if (bibleReference.getUrl() != null) {
                    existingBibleReference.setUrl(bibleReference.getUrl());
                }

                return existingBibleReference;
            })
            .map(bibleReferenceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BibleReference> findAll() {
        LOG.debug("Request to get all BibleReferences");
        return bibleReferenceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BibleReference> findOne(Long id) {
        LOG.debug("Request to get BibleReference : {}", id);
        return bibleReferenceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BibleReference : {}", id);
        bibleReferenceRepository.deleteById(id);
    }
}
