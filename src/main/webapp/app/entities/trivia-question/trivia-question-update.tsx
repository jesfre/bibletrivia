import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTrivias } from 'app/entities/trivia/trivia.reducer';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { TriviaType } from 'app/shared/model/enumerations/trivia-type.model';
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';
import { createEntity, getEntity, updateEntity } from './trivia-question.reducer';

export const TriviaQuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trivias = useAppSelector(state => state.trivia.entities);
  const triviaQuestionEntity = useAppSelector(state => state.triviaQuestion.entity);
  const loading = useAppSelector(state => state.triviaQuestion.loading);
  const updating = useAppSelector(state => state.triviaQuestion.updating);
  const updateSuccess = useAppSelector(state => state.triviaQuestion.updateSuccess);
  const triviaLevelValues = Object.keys(TriviaLevel);
  const triviaTypeValues = Object.keys(TriviaType);
  const answerTypeValues = Object.keys(AnswerType);

  const handleClose = () => {
    navigate('/trivia-question');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getTrivias({}));
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
    if (values.questionId !== undefined && typeof values.questionId !== 'number') {
      values.questionId = Number(values.questionId);
    }
    if (values.value !== undefined && typeof values.value !== 'number') {
      values.value = Number(values.value);
    }

    const entity = {
      ...triviaQuestionEntity,
      ...values,
      trivias: mapIdList(values.trivias),
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
          level: 'EASY',
          questionType: 'BIBLICAL',
          answerType: 'SINGLE',
          ...triviaQuestionEntity,
          trivias: triviaQuestionEntity?.trivias?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bibletriviaApp.triviaQuestion.home.createOrEditLabel" data-cy="TriviaQuestionCreateUpdateHeading">
            <Translate contentKey="bibletriviaApp.triviaQuestion.home.createOrEditLabel">Create or edit a TriviaQuestion</Translate>
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
                  id="trivia-question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.questionId')}
                id="trivia-question-questionId"
                name="questionId"
                data-cy="questionId"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.level')}
                id="trivia-question-level"
                name="level"
                data-cy="level"
                type="select"
              >
                {triviaLevelValues.map(triviaLevel => (
                  <option value={triviaLevel} key={triviaLevel}>
                    {translate(`bibletriviaApp.TriviaLevel.${triviaLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.questionType')}
                id="trivia-question-questionType"
                name="questionType"
                data-cy="questionType"
                type="select"
              >
                {triviaTypeValues.map(triviaType => (
                  <option value={triviaType} key={triviaType}>
                    {translate(`bibletriviaApp.TriviaType.${triviaType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.question')}
                id="trivia-question-question"
                name="question"
                data-cy="question"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.answerType')}
                id="trivia-question-answerType"
                name="answerType"
                data-cy="answerType"
                type="select"
              >
                {answerTypeValues.map(answerType => (
                  <option value={answerType} key={answerType}>
                    {translate(`bibletriviaApp.AnswerType.${answerType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.value')}
                id="trivia-question-value"
                name="value"
                data-cy="value"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.picture')}
                id="trivia-question-picture"
                name="picture"
                data-cy="picture"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.triviaQuestion.trivia')}
                id="trivia-question-trivia"
                data-cy="trivia"
                type="select"
                multiple
                name="trivias"
              >
                <option value="" key="0" />
                {trivias
                  ? trivias.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trivia-question" replace color="info">
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

export default TriviaQuestionUpdate;
