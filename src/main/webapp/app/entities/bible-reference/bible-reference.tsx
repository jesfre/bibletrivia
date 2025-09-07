import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bible-reference.reducer';

export const BibleReference = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const bibleReferenceList = useAppSelector(state => state.bibleReference.entities);
  const loading = useAppSelector(state => state.bibleReference.loading);

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
      <h2 id="bible-reference-heading" data-cy="BibleReferenceHeading">
        <Translate contentKey="bibletriviaApp.bibleReference.home.title">Bible References</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="bibletriviaApp.bibleReference.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/bible-reference/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="bibletriviaApp.bibleReference.home.createLabel">Create new Bible Reference</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bibleReferenceList && bibleReferenceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('bibleVerse')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.bibleVerse">Bible Verse</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bibleVerse')} />
                </th>
                <th className="hand" onClick={sort('version')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.version">Version</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('version')} />
                </th>
                <th className="hand" onClick={sort('book')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.book">Book</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('book')} />
                </th>
                <th className="hand" onClick={sort('chapter')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.chapter">Chapter</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('chapter')} />
                </th>
                <th className="hand" onClick={sort('verse')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.verse">Verse</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('verse')} />
                </th>
                <th className="hand" onClick={sort('testament')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.testament">Testament</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('testament')} />
                </th>
                <th className="hand" onClick={sort('url')}>
                  <Translate contentKey="bibletriviaApp.bibleReference.url">Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                </th>
                <th>
                  <Translate contentKey="bibletriviaApp.bibleReference.triviaAnswer">Trivia Answer</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bibleReferenceList.map((bibleReference, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bible-reference/${bibleReference.id}`} color="link" size="sm">
                      {bibleReference.id}
                    </Button>
                  </td>
                  <td>{bibleReference.bibleVerse}</td>
                  <td>{bibleReference.version}</td>
                  <td>
                    <Translate contentKey={`bibletriviaApp.Book.${bibleReference.book}`} />
                  </td>
                  <td>{bibleReference.chapter}</td>
                  <td>{bibleReference.verse}</td>
                  <td>
                    <Translate contentKey={`bibletriviaApp.Testament.${bibleReference.testament}`} />
                  </td>
                  <td><Link to={`${bibleReference.url}`}>{bibleReference.url}</Link></td>
                  <td>
                    {bibleReference.triviaAnswers
                      ? bibleReference.triviaAnswers.map((val, j) => (
                          <span key={j}>
                            <Link to={`/trivia-answer/${val.id}`}>{val.id}</Link>
                            {j === bibleReference.triviaAnswers.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bible-reference/${bibleReference.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bible-reference/${bibleReference.id}/edit`}
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
                        onClick={() => (window.location.href = `/bible-reference/${bibleReference.id}/delete`)}
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
              <Translate contentKey="bibletriviaApp.bibleReference.home.notFound">No Bible References found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default BibleReference;
