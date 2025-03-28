import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quiz-entry.reducer';

export const QuizEntryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const quizEntryEntity = useAppSelector(state => state.quizEntry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="quizEntryDetailsHeading">
          <Translate contentKey="bibletriviaApp.quizEntry.detail.title">QuizEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{quizEntryEntity.id}</dd>
          <dt>
            <span id="orderNum">
              <Translate contentKey="bibletriviaApp.quizEntry.orderNum">Order Num</Translate>
            </span>
          </dt>
          <dd>{quizEntryEntity.orderNum}</dd>
          <dt>
            <span id="correct">
              <Translate contentKey="bibletriviaApp.quizEntry.correct">Correct</Translate>
            </span>
          </dt>
          <dd>{quizEntryEntity.correct ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.quizEntry.triviaQuestion">Trivia Question</Translate>
          </dt>
          <dd>{quizEntryEntity.triviaQuestion ? quizEntryEntity.triviaQuestion.question : ''}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.quizEntry.selectedAnswers">Trivia Answers</Translate>
          </dt>
          <dd>
            {quizEntryEntity.selectedAnswers
              ? quizEntryEntity.selectedAnswers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.answer}</a>
                    {quizEntryEntity.selectedAnswers && i === quizEntryEntity.selectedAnswers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="bibletriviaApp.quizEntry.quiz">Quiz</Translate>
          </dt>
          <dd>{quizEntryEntity.quiz ? quizEntryEntity.quiz.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/quiz-entry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quiz-entry/${quizEntryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuizEntryDetail;
