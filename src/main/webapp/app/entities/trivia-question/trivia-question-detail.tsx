import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trivia-question.reducer';

export const TriviaQuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const triviaQuestionEntity = useAppSelector(state => state.triviaQuestion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="triviaQuestionDetailsHeading">
          <Translate contentKey="bibletriviaApp.triviaQuestion.detail.title">TriviaQuestion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.id}</dd>
          <dt>
            <span id="questionId">
              <Translate contentKey="bibletriviaApp.triviaQuestion.questionId">Question Id</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.questionId}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="bibletriviaApp.triviaQuestion.level">Level</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.level}</dd>
          <dt>
            <span id="questionType">
              <Translate contentKey="bibletriviaApp.triviaQuestion.questionType">Question Type</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.questionType}</dd>
          <dt>
            <span id="question">
              <Translate contentKey="bibletriviaApp.triviaQuestion.question">Question</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.question}</dd>
          <dt>
            <span id="answerType">
              <Translate contentKey="bibletriviaApp.triviaQuestion.answerType">Answer Type</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.answerType}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="bibletriviaApp.triviaQuestion.value">Value</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.value}</dd>
          <dt>
            <span id="picture">
              <Translate contentKey="bibletriviaApp.triviaQuestion.picture">Picture</Translate>
            </span>
          </dt>
          <dd>{triviaQuestionEntity.picture}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.triviaQuestion.trivia">Trivia</Translate>
          </dt>
          <dd>
            {triviaQuestionEntity.trivias
              ? triviaQuestionEntity.trivias.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {triviaQuestionEntity.trivias && i === triviaQuestionEntity.trivias.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/trivia-question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trivia-question/${triviaQuestionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TriviaQuestionDetail;
