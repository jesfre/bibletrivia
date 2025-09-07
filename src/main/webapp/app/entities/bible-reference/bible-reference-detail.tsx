import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bible-reference.reducer';

export const BibleReferenceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bibleReferenceEntity = useAppSelector(state => state.bibleReference.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bibleReferenceDetailsHeading">
          <Translate contentKey="bibletriviaApp.bibleReference.detail.title">BibleReference</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.id}</dd>
          <dt>
            <span id="bibleVerse">
              <Translate contentKey="bibletriviaApp.bibleReference.bibleVerse">Bible Verse</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.bibleVerse}</dd>
          <dt>
            <span id="version">
              <Translate contentKey="bibletriviaApp.bibleReference.version">Version</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.version}</dd>
          <dt>
            <span id="book">
              <Translate contentKey="bibletriviaApp.bibleReference.book">Book</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.book}</dd>
          <dt>
            <span id="chapter">
              <Translate contentKey="bibletriviaApp.bibleReference.chapter">Chapter</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.chapter}</dd>
          <dt>
            <span id="verse">
              <Translate contentKey="bibletriviaApp.bibleReference.verse">Verse</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.verse}</dd>
          <dt>
            <span id="testament">
              <Translate contentKey="bibletriviaApp.bibleReference.testament">Testament</Translate>
            </span>
          </dt>
          <dd>{bibleReferenceEntity.testament}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="bibletriviaApp.bibleReference.url">Url</Translate>
            </span>
          </dt>
          <dd><Link to={`${bibleReferenceEntity.url}`}>{bibleReferenceEntity.url}</Link></dd>
          <dt>
            <Translate contentKey="bibletriviaApp.bibleReference.triviaAnswer">Trivia Answer</Translate>
          </dt>
          <dd>
            {bibleReferenceEntity.triviaAnswers
              ? bibleReferenceEntity.triviaAnswers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {bibleReferenceEntity.triviaAnswers && i === bibleReferenceEntity.triviaAnswers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/bible-reference" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bible-reference/${bibleReferenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BibleReferenceDetail;
