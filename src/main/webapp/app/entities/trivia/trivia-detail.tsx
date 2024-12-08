import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trivia.reducer';

export const TriviaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const triviaEntity = useAppSelector(state => state.trivia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="triviaDetailsHeading">
          <Translate contentKey="bibletriviaApp.trivia.detail.title">Trivia</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{triviaEntity.id}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="bibletriviaApp.trivia.level">Level</Translate>
            </span>
          </dt>
          <dd>{triviaEntity.level}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="bibletriviaApp.trivia.name">Name</Translate>
            </span>
          </dt>
          <dd>{triviaEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="bibletriviaApp.trivia.type">Type</Translate>
            </span>
          </dt>
          <dd>{triviaEntity.type}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="bibletriviaApp.trivia.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{triviaEntity.startDate ? <TextFormat value={triviaEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="bibletriviaApp.trivia.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{triviaEntity.endDate ? <TextFormat value={triviaEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="bibletriviaApp.trivia.triviaQuestion">Trivia Question</Translate>
          </dt>
          <dd>
            {triviaEntity.triviaQuestions
              ? triviaEntity.triviaQuestions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.questionId}</a>
                    {triviaEntity.triviaQuestions && i === triviaEntity.triviaQuestions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/trivia" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trivia/${triviaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TriviaDetail;
