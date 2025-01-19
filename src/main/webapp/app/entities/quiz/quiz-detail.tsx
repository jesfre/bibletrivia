import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quiz.reducer';

export const QuizDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const quizEntity = useAppSelector(state => state.quiz.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="quizDetailsHeading">
          <Translate contentKey="bibletriviaApp.quiz.detail.title">Quiz</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{quizEntity.id}</dd>
          <dt>
            <span id="quizTaker">
              <Translate contentKey="bibletriviaApp.quiz.quizTaker">Quiz Taker</Translate>
            </span>
          </dt>
          <dd>{quizEntity.quizTaker}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="bibletriviaApp.quiz.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{quizEntity.startDate ? <TextFormat value={quizEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="totalQuestions">
              <Translate contentKey="bibletriviaApp.quiz.totalQuestions">Total Questions</Translate>
            </span>
          </dt>
          <dd>{quizEntity.totalQuestions}</dd>
          <dt>
            <span id="correctQuestions">
              <Translate contentKey="bibletriviaApp.quiz.correctQuestions">Correct Questions</Translate>
            </span>
          </dt>
          <dd>{quizEntity.correctQuestions}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.quiz.owner">Owner</Translate>
          </dt>
          <dd>{quizEntity.owner ? quizEntity.owner.firstName : ''}</dd>
        </dl>
        <Button tag={Link} to="/quiz" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quiz/${quizEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuizDetail;
