import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBibleReferences } from 'app/entities/bible-reference/bible-reference.reducer';
import { getEntities as getTriviaQuestions } from 'app/entities/trivia-question/trivia-question.reducer';
import { createEntity, getEntity, reset, updateEntity } from './trivia-answer.reducer';

export const TriviaAnswerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bibleReferences = useAppSelector(state => state.bibleReference.entities);
  const triviaQuestions = useAppSelector(state => state.triviaQuestion.entities);
  const triviaAnswerEntity = useAppSelector(state => state.triviaAnswer.entity);
  const loading = useAppSelector(state => state.triviaAnswer.loading);
  const updating = useAppSelector(state => state.triviaAnswer.updating);
  const updateSuccess = useAppSelector(state => state.triviaAnswer.updateSuccess);

  const handleClose = () => {
    navigate('/trivia-answer');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBibleReferences({}));
    dispatch(getTriviaQuestions({}));
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
    if (values.answerId !== undefined && typeof values.answerId !== 'number') {
      values.answerId = Number(values.answerId);
    }

    const entity = {
      ...triviaAnswerEntity,
      ...values,
      bibleReferences: mapIdList(values.bibleReferences),
      triviaQuestion: triviaQuestions.find(it => it.id.toString() === values.triviaQuestion?.toString()),
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
          ...triviaAnswerEntity,
          bibleReferences: triviaAnswerEntity?.bibleReferences?.map(e => e.id.toString()),
          triviaQuestion: triviaAnswerEntity?.triviaQuestion?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bibletriviaApp.triviaAnswer.home.createOrEditLabel" data-cy="TriviaAnswerCreateUpdateHeading">
            <Translate contentKey="bibletriviaApp.triviaAnswer.home.createOrEditLabel">Create or edit a TriviaAnswer</Translate>
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
                  id="trivia-answer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bibletriviaApp.triviaAnswer.answerId')}
                id="trivia-answer-answerId"
                name="answerId"
                data-cy="answerId"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaAnswer.answer')}
                id="trivia-answer-answer"
                name="answer"
                data-cy="answer"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaAnswer.explanation')}
                id="trivia-answer-explanation"
                name="explanation"
                data-cy="explanation"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaAnswer.correct')}
                id="trivia-answer-correct"
                name="correct"
                data-cy="correct"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaAnswer.picture')}
                id="trivia-answer-picture"
                name="picture"
                data-cy="picture"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaAnswer.bibleReference')}
                id="trivia-answer-bibleReference"
                data-cy="bibleReference"
                type="select"
                multiple
                name="bibleReferences"
              >
                <option value="" key="0" />
                {bibleReferences
                  ? bibleReferences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.bibleVerse}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="trivia-answer-triviaQuestion"
                name="triviaQuestion"
                data-cy="triviaQuestion"
                label={translate('bibletriviaApp.triviaAnswer.triviaQuestion')}
                type="select"
              >
                <option value="" key="0" />
                {triviaQuestions
                  ? triviaQuestions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trivia-answer" replace color="info">
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

export default TriviaAnswerUpdate;
