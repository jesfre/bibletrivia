import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTriviaAnswers } from 'app/entities/trivia-answer/trivia-answer.reducer';
import { Book } from 'app/shared/model/enumerations/book.model';
import { Testament } from 'app/shared/model/enumerations/testament.model';
import { createEntity, getEntity, reset, updateEntity } from './bible-reference.reducer';

export const BibleReferenceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const triviaAnswers = useAppSelector(state => state.triviaAnswer.entities);
  const bibleReferenceEntity = useAppSelector(state => state.bibleReference.entity);
  const loading = useAppSelector(state => state.bibleReference.loading);
  const updating = useAppSelector(state => state.bibleReference.updating);
  const updateSuccess = useAppSelector(state => state.bibleReference.updateSuccess);
  const bookValues = Object.keys(Book);
  const testamentValues = Object.keys(Testament);

  const handleClose = () => {
    navigate('/bible-reference');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTriviaAnswers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.chapter !== undefined && typeof values.chapter !== 'number') {
      values.chapter = Number(values.chapter);
    }
    if (values.verse !== undefined && typeof values.verse !== 'number') {
      values.verse = Number(values.verse);
    }

    const entity = {
      ...bibleReferenceEntity,
      ...values,
      triviaAnswers: mapIdList(values.triviaAnswers),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          book: 'Genesis',
          testament: 'OLD_TESTAMENT',
          ...bibleReferenceEntity,
          triviaAnswers: bibleReferenceEntity?.triviaAnswers?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bibletriviaApp.bibleReference.home.createOrEditLabel" data-cy="BibleReferenceCreateUpdateHeading">
            <Translate contentKey="bibletriviaApp.bibleReference.home.createOrEditLabel">Create or edit a BibleReference</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="bible-reference-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.bibleVerse')}
                id="bible-reference-bibleVerse"
                name="bibleVerse"
                data-cy="bibleVerse"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.version')}
                id="bible-reference-version"
                name="version"
                data-cy="version"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.book')}
                id="bible-reference-book"
                name="book"
                data-cy="book"
                type="select"
              >
                {bookValues.map(book => (
                  <option value={book} key={book}>
                    {translate(`bibletriviaApp.Book.${book}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.chapter')}
                id="bible-reference-chapter"
                name="chapter"
                data-cy="chapter"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.verse')}
                id="bible-reference-verse"
                name="verse"
                data-cy="verse"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.testament')}
                id="bible-reference-testament"
                name="testament"
                data-cy="testament"
                type="select"
              >
                {testamentValues.map(testament => (
                  <option value={testament} key={testament}>
                    {translate(`bibletriviaApp.Testament.${testament}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.url')}
                id="bible-reference-url"
                name="url"
                data-cy="url"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.bibleReference.triviaAnswer')}
                id="bible-reference-triviaAnswer"
                data-cy="triviaAnswer"
                type="select"
                multiple
                name="triviaAnswers"
              >
                <option value="" key="0" />
                {triviaAnswers
                  ? triviaAnswers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bible-reference" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BibleReferenceUpdate;
