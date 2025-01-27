import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './quiz-entry.reducer';

export const QuizEntry = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const quizEntryList = useAppSelector(state => state.quizEntry.entities);
  const loading = useAppSelector(state => state.quizEntry.loading);
  const links = useAppSelector(state => state.quizEntry.links);
  const updateSuccess = useAppSelector(state => state.quizEntry.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="quiz-entry-heading" data-cy="QuizEntryHeading">
        <Translate contentKey="bibletriviaApp.quizEntry.home.title">Quiz Entries</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bibletriviaApp.quizEntry.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/quiz-entry/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bibletriviaApp.quizEntry.home.createLabel">Create new Quiz Entry</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={quizEntryList ? quizEntryList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {quizEntryList && quizEntryList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="bibletriviaApp.quizEntry.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('orderNum')}>
                    <Translate contentKey="bibletriviaApp.quizEntry.orderNum">Order Num</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('orderNum')} />
                  </th>
                  <th className="hand" onClick={sort('correct')}>
                    <Translate contentKey="bibletriviaApp.quizEntry.correct">Correct</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('correct')} />
                  </th>
                  <th>
                    <Translate contentKey="bibletriviaApp.quizEntry.triviaQuestion">Trivia Question</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="bibletriviaApp.quizEntry.quiz">Quiz</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {quizEntryList.map((quizEntry, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/quiz-entry/${quizEntry.id}`} color="link" size="sm">
                        {quizEntry.id}
                      </Button>
                    </td>
                    <td>{quizEntry.orderNum}</td>
                    <td>{quizEntry.correct ? 'true' : 'false'}</td>
                    <td>
                      {quizEntry.triviaQuestion ? (
                        <Link to={`/trivia-question/${quizEntry.triviaQuestion.id}`}>{quizEntry.triviaQuestion.question}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{quizEntry.quiz ? <Link to={`/quiz/${quizEntry.quiz.id}`}>{quizEntry.quiz.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/quiz-entry/${quizEntry.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/quiz-entry/${quizEntry.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/quiz-entry/${quizEntry.id}/delete`)}
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
                <Translate contentKey="bibletriviaApp.quizEntry.home.notFound">No Quiz Entries found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default QuizEntry;
