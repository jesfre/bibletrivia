import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './trivia-answer.reducer';

export const TriviaAnswer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const triviaAnswerList = useAppSelector(state => state.triviaAnswer.entities);
  const loading = useAppSelector(state => state.triviaAnswer.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="trivia-answer-heading" data-cy="TriviaAnswerHeading">
        <Translate contentKey="bibletriviaApp.triviaAnswer.home.title">Trivia Answers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bibletriviaApp.triviaAnswer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/trivia-answer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bibletriviaApp.triviaAnswer.home.createLabel">Create new Trivia Answer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {triviaAnswerList && triviaAnswerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('answerId')}>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.answerId">Answer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('answerId')} />
                </th>
                <th className="hand" onClick={sort('answer')}>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.answer">Answer</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('answer')} />
                </th>
                <th className="hand" onClick={sort('explanation')}>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.explanation">Explanation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('explanation')} />
                </th>
                <th className="hand" onClick={sort('correct')}>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.correct">Correct</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correct')} />
                </th>
                <th className="hand" onClick={sort('picture')}>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.picture">Picture</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('picture')} />
                </th>
                <th>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.bibleReference">Bible Reference</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.triviaQuestion">Trivia Question</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="bibletriviaApp.triviaAnswer.quizEntries">Quiz Entries</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {triviaAnswerList.map((triviaAnswer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/trivia-answer/${triviaAnswer.id}`} color="link" size="sm">
                      {triviaAnswer.id}
                    </Button>
                  </td>
                  <td>{triviaAnswer.answerId}</td>
                  <td>{triviaAnswer.answer}</td>
                  <td>{triviaAnswer.explanation}</td>
                  <td>{triviaAnswer.correct ? 'true' : 'false'}</td>
                  <td>{triviaAnswer.picture}</td>
                  <td>
                    {triviaAnswer.bibleReferences
                      ? triviaAnswer.bibleReferences.map((val, j) => (
                          <span key={j}>
                            <Link to={`/bible-reference/${val.id}`}>{val.bibleVerse}</Link>
                            {j === triviaAnswer.bibleReferences.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {triviaAnswer.triviaQuestion ? (
                      <Link to={`/trivia-question/${triviaAnswer.triviaQuestion.id}`}>{triviaAnswer.triviaQuestion.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {triviaAnswer.quizEntries
                      ? triviaAnswer.quizEntries.map((val, j) => (
                          <span key={j}>
                            <Link to={`/quiz-entry/${val.id}`}>{val.id}</Link>
                            {j === triviaAnswer.quizEntries.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/trivia-answer/${triviaAnswer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/trivia-answer/${triviaAnswer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/trivia-answer/${triviaAnswer.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="bibletriviaApp.triviaAnswer.home.notFound">No Trivia Answers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TriviaAnswer;
