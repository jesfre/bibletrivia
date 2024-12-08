import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTriviaQuestions } from 'app/entities/trivia-question/trivia-question.reducer';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { TriviaType } from 'app/shared/model/enumerations/trivia-type.model';
import { createEntity, getEntity, reset, updateEntity } from './trivia.reducer';

export const TriviaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const triviaQuestions = useAppSelector(state => state.triviaQuestion.entities);
  const triviaEntity = useAppSelector(state => state.trivia.entity);
  const loading = useAppSelector(state => state.trivia.loading);
  const updating = useAppSelector(state => state.trivia.updating);
  const updateSuccess = useAppSelector(state => state.trivia.updateSuccess);
  const triviaLevelValues = Object.keys(TriviaLevel);
  const triviaTypeValues = Object.keys(TriviaType);

  const handleClose = () => {
    navigate(`/trivia${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTriviaQuestions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    const entity = {
      ...triviaEntity,
      ...values,
      triviaQuestions: mapIdList(values.triviaQuestions),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          level: 'EASY',
          type: 'BIBLICAL',
          ...triviaEntity,
          startDate: convertDateTimeFromServer(triviaEntity.startDate),
          endDate: convertDateTimeFromServer(triviaEntity.endDate),
          triviaQuestions: triviaEntity?.triviaQuestions?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bibletriviaApp.trivia.home.createOrEditLabel" data-cy="TriviaCreateUpdateHeading">
            <Translate contentKey="bibletriviaApp.trivia.home.createOrEditLabel">Create or edit a Trivia</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="trivia-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('bibletriviaApp.trivia.level')} id="trivia-level" name="level" data-cy="level" type="select">
                {triviaLevelValues.map(triviaLevel => (
                  <option value={triviaLevel} key={triviaLevel}>
                    {translate(`bibletriviaApp.TriviaLevel.${triviaLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('bibletriviaApp.trivia.name')} id="trivia-name" name="name" data-cy="name" type="text" />
              <ValidatedField label={translate('bibletriviaApp.trivia.type')} id="trivia-type" name="type" data-cy="type" type="select">
                {triviaTypeValues.map(triviaType => (
                  <option value={triviaType} key={triviaType}>
                    {translate(`bibletriviaApp.TriviaType.${triviaType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('bibletriviaApp.trivia.startDate')}
                id="trivia-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('bibletriviaApp.trivia.endDate')}
                id="trivia-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('bibletriviaApp.trivia.triviaQuestion')}
                id="trivia-triviaQuestion"
                data-cy="triviaQuestion"
                type="select"
                multiple
                name="triviaQuestions"
              >
                <option value="" key="0" />
                {triviaQuestions
                  ? triviaQuestions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.questionId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trivia" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TriviaUpdate;
