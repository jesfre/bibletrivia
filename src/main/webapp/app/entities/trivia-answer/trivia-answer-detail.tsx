import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trivia-answer.reducer';

export const TriviaAnswerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const triviaAnswerEntity = useAppSelector(state => state.triviaAnswer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="triviaAnswerDetailsHeading">
          <Translate contentKey="bibletriviaApp.triviaAnswer.detail.title">TriviaAnswer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{triviaAnswerEntity.id}</dd>
          <dt>
            <span id="answerId">
              <Translate contentKey="bibletriviaApp.triviaAnswer.answerId">Answer Id</Translate>
            </span>
          </dt>
          <dd>{triviaAnswerEntity.answerId}</dd>
          <dt>
            <span id="answer">
              <Translate contentKey="bibletriviaApp.triviaAnswer.answer">Answer</Translate>
            </span>
          </dt>
          <dd>{triviaAnswerEntity.answer}</dd>
          <dt>
            <span id="explanation">
              <Translate contentKey="bibletriviaApp.triviaAnswer.explanation">Explanation</Translate>
            </span>
          </dt>
          <dd>{triviaAnswerEntity.explanation}</dd>
          <dt>
            <span id="correct">
              <Translate contentKey="bibletriviaApp.triviaAnswer.correct">Correct</Translate>
            </span>
          </dt>
          <dd>{triviaAnswerEntity.correct ? 'true' : 'false'}</dd>
          <dt>
            <span id="picture">
              <Translate contentKey="bibletriviaApp.triviaAnswer.picture">Picture</Translate>
            </span>
          </dt>
          <dd>{triviaAnswerEntity.picture}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.triviaAnswer.bibleReference">Bible Reference</Translate>
          </dt>
          <dd>
            {triviaAnswerEntity.bibleReferences
              ? triviaAnswerEntity.bibleReferences.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.bibleVerse}</a>
                    {triviaAnswerEntity.bibleReferences && i === triviaAnswerEntity.bibleReferences.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="bibletriviaApp.triviaAnswer.triviaQuestion">Trivia Question</Translate>
          </dt>
          <dd>{triviaAnswerEntity.triviaQuestion ? triviaAnswerEntity.triviaQuestion.id : ''}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.triviaAnswer.quizEntries">Quiz Entries</Translate>
          </dt>
          <dd>
            {triviaAnswerEntity.quizEntries
              ? triviaAnswerEntity.quizEntries.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {triviaAnswerEntity.quizEntries && i === triviaAnswerEntity.quizEntries.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/trivia-answer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trivia-answer/${triviaAnswerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TriviaAnswerDetail;
