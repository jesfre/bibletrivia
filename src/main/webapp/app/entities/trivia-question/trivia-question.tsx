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

import { getEntities, reset } from './trivia-question.reducer';

export const TriviaQuestion = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const triviaQuestionList = useAppSelector(state => state.triviaQuestion.entities);
  const loading = useAppSelector(state => state.triviaQuestion.loading);
  const links = useAppSelector(state => state.triviaQuestion.links);
  const updateSuccess = useAppSelector(state => state.triviaQuestion.updateSuccess);

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
      <h2 id="trivia-question-heading" data-cy="TriviaQuestionHeading">
        <Translate contentKey="bibletriviaApp.triviaQuestion.home.title">Trivia Questions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bibletriviaApp.triviaQuestion.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/trivia-question/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bibletriviaApp.triviaQuestion.home.createLabel">Create new Trivia Question</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={triviaQuestionList ? triviaQuestionList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {triviaQuestionList && triviaQuestionList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('questionId')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.questionId">Question Id</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('questionId')} />
                  </th>
                  <th className="hand" onClick={sort('questionType')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.questionType">Question Type</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('questionType')} />
                  </th>
                  <th className="hand" onClick={sort('question')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.question">Question</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('question')} />
                  </th>
                  <th className="hand" onClick={sort('answerType')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.answerType">Answer Type</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('answerType')} />
                  </th>
                  <th className="hand" onClick={sort('value')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.value">Value</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('value')} />
                  </th>
                  <th className="hand" onClick={sort('picture')}>
                    <Translate contentKey="bibletriviaApp.triviaQuestion.picture">Picture</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('picture')} />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {triviaQuestionList.map((triviaQuestion, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/trivia-question/${triviaQuestion.id}`} color="link" size="sm">
                        {triviaQuestion.id}
                      </Button>
                    </td>
                    <td>{triviaQuestion.questionId}</td>
                    <td>
                      <Translate contentKey={`bibletriviaApp.TriviaType.${triviaQuestion.questionType}`} />
                    </td>
                    <td>{triviaQuestion.question}</td>
                    <td>
                      <Translate contentKey={`bibletriviaApp.AnswerType.${triviaQuestion.answerType}`} />
                    </td>
                    <td>{triviaQuestion.value}</td>
                    <td>{triviaQuestion.picture}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`/trivia-question/${triviaQuestion.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/trivia-question/${triviaQuestion.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/trivia-question/${triviaQuestion.id}/delete`)}
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
                <Translate contentKey="bibletriviaApp.triviaQuestion.home.notFound">No Trivia Questions found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default TriviaQuestion;
