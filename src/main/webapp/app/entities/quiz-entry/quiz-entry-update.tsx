import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTriviaQuestions } from 'app/entities/trivia-question/trivia-question.reducer';
import { getEntities as getTriviaAnswers } from 'app/entities/trivia-answer/trivia-answer.reducer';
import { getEntities as getQuizzes } from 'app/entities/quiz/quiz.reducer';
import { createEntity, getEntity, updateEntity } from './quiz-entry.reducer';

export const QuizEntryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const triviaQuestions = useAppSelector(state => state.triviaQuestion.entities);
  const triviaAnswers = useAppSelector(state => state.triviaAnswer.entities);
  const quizzes = useAppSelector(state => state.quiz.entities);
  const quizEntryEntity = useAppSelector(state => state.quizEntry.entity);
  const loading = useAppSelector(state => state.quizEntry.loading);
  const updating = useAppSelector(state => state.quizEntry.updating);
  const updateSuccess = useAppSelector(state => state.quizEntry.updateSuccess);

  const handleClose = () => {
    navigate('/quiz-entry');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getTriviaQuestions({}));
    dispatch(getTriviaAnswers({}));
    dispatch(getQuizzes({}));
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
    if (values.orderNum !== undefined && typeof values.orderNum !== 'number') {
      values.orderNum = Number(values.orderNum);
    }

    const entity = {
      ...quizEntryEntity,
      ...values,
      triviaQuestion: triviaQuestions.find(it => it.id.toString() === values.triviaQuestion?.toString()),
      triviaAnswers: mapIdList(values.selectedAnswers),
      quiz: quizzes.find(it => it.id.toString() === values.quiz?.toString()),
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
          ...quizEntryEntity,
          triviaQuestion: quizEntryEntity?.triviaQuestion?.id,
          triviaAnswers: quizEntryEntity?.selectedAnswers?.map(e => e.id.toString()),
          quiz: quizEntryEntity?.quiz?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bibletriviaApp.quizEntry.home.createOrEditLabel" data-cy="QuizEntryCreateUpdateHeading">
            <Translate contentKey="bibletriviaApp.quizEntry.home.createOrEditLabel">Create or edit a QuizEntry</Translate>
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
                  id="quiz-entry-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bibletriviaApp.quizEntry.orderNum')}
                id="quiz-entry-orderNum"
                name="orderNum"
                data-cy="orderNum"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('bibletriviaApp.quizEntry.correct')}
                id="quiz-entry-correct"
                name="correct"
                data-cy="correct"
                check
                type="checkbox"
              />
              <ValidatedField
                id="quiz-entry-triviaQuestion"
                name="triviaQuestion"
                data-cy="triviaQuestion"
                label={translate('bibletriviaApp.quizEntry.triviaQuestion')}
                type="select"
                required
              >
                <option value="" key="0" />
                {triviaQuestions
                  ? triviaQuestions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.question}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                label={translate('bibletriviaApp.quizEntry.selectedAnswers')}
                id="quiz-entry-selectedAnswers"
                data-cy="selectedAnswers"
                type="select"
                multiple
                name="selectedAnswers"
              >
                <option value="" key="0" />
                {triviaAnswers
                  ? triviaAnswers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.answer}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="quiz-entry-quiz"
                name="quiz"
                data-cy="quiz"
                label={translate('bibletriviaApp.quizEntry.quiz')}
                type="select"
              >
                <option value="" key="0" />
                {quizzes
                  ? quizzes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quiz-entry" replace color="info">
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

export default QuizEntryUpdate;
