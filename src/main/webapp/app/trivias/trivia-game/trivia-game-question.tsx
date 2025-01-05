import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button, Table, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';
import { createAsyncThunk } from '@reduxjs/toolkit';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ITriviaGameQuestion, defaultValue } from 'app/shared/model/trivia-game-question.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getQuestion } from './trivia-game.reducer';
import { getEntitiesForQuestion } from 'app/entities/trivia-answer/trivia-answer.reducer';

const TriviaGameQuestion = () => {
  const dispatch = useAppDispatch();

  const questionNum = 1;

  useEffect(() => {
    dispatch(getQuestion(questionNum));
  }, []);

  useEffect(() => {
    dispatch(getEntitiesForQuestion(questionNum));
  }, []);

  const questionEntity = useAppSelector(state => state.triviaGameQuestion.entity);
  const answerList = useAppSelector(state => state.triviaAnswer.entities);
  const loading = useAppSelector(state => state.triviaAnswer.loading);
  
  return (
        <div>
          <p>Question</p>
          <dt><span>Question {questionEntity.questionNumber}.</span></dt>
          <dd><span>{questionEntity.questionText}</span></dd>
          
          <br/><br/>

          {answerList && answerList.length > 0 ? (
            <div className="table-responsive">
              {answerList.map((answer, i) => (
                <div className="answer">
                  <span>{answer.id}</span>
                    <span>{answer.answerId}</span>
                    <span>{answer.answer}</span>
                    <span>{answer.explanation}</span>
                    <span>{answer.correct ? 'true' : 'false'}</span>
                    <span>{answer.picture}</span>
                </div>
                ))}
            </div>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="bibletriviaApp.answer.home.notFound">No Trivia Answers found</Translate>
              </div>
            )
          )}

 <br/><br/>
          
          <div className="table-responsive">
          {answerList && answerList.length > 0 ? (
            <Table responsive>
              <tbody>
                {answerList.map((answer, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{answer.id}</td>
                    <td>{answer.answerId}</td>
                    <td>{answer.answer}</td>
                    <td>{answer.explanation}</td>
                    <td>{answer.correct ? 'true' : 'false'}</td>
                    <td>{answer.picture}</td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="bibletriviaApp.answer.home.notFound">No Trivia Answers found</Translate>
              </div>
            )
          )}
        </div>

        </div>
    );
}
export default TriviaGameQuestion;