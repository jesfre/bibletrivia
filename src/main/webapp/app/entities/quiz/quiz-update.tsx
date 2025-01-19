import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './quiz.reducer';

export const QuizUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const quizEntity = useAppSelector(state => state.quiz.entity);
  const loading = useAppSelector(state => state.quiz.loading);
  const updating = useAppSelector(state => state.quiz.updating);
  const updateSuccess = useAppSelector(state => state.quiz.updateSuccess);

  const handleClose = () => {
    navigate(`/quiz${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.totalQuestions !== undefined && typeof values.totalQuestions !== 'number') {
      values.totalQuestions = Number(values.totalQuestions);
    }
    if (values.correctQuestions !== undefined && typeof values.correctQuestions !== 'number') {
      values.correctQuestions = Number(values.correctQuestions);
    }

    const entity = {
      ...quizEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner?.toString()),
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
        }
      : {
          ...quizEntity,
          startDate: convertDateTimeFromServer(quizEntity.startDate),
          owner: quizEntity?.owner?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bibletriviaApp.quiz.home.createOrEditLabel" data-cy="QuizCreateUpdateHeading">
            <Translate contentKey="bibletriviaApp.quiz.home.createOrEditLabel">Create or edit a Quiz</Translate>
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
                  id="quiz-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('bibletriviaApp.quiz.quizTaker')}
                id="quiz-quizTaker"
                name="quizTaker"
                data-cy="quizTaker"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 120, message: translate('entity.validation.maxlength', { max: 120 }) },
                }}
              />
              <ValidatedField
                label={translate('bibletriviaApp.quiz.startDate')}
                id="quiz-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('bibletriviaApp.quiz.totalQuestions')}
                id="quiz-totalQuestions"
                name="totalQuestions"
                data-cy="totalQuestions"
                type="text"
              />
              <ValidatedField
                label={translate('bibletriviaApp.quiz.correctQuestions')}
                id="quiz-correctQuestions"
                name="correctQuestions"
                data-cy="correctQuestions"
                type="text"
              />
              <ValidatedField id="quiz-owner" name="owner" data-cy="owner" label={translate('bibletriviaApp.quiz.owner')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.firstName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quiz" replace color="info">
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

export default QuizUpdate;
