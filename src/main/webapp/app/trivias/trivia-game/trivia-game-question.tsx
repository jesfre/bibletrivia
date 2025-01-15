import React, { useEffect, useState } from 'react';
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
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';
import { getTriviaQuestionInLevel } from 'app/entities/trivia-question/trivia-question.reducer';

{/* prettier-ignore */}

const TriviaGameQuestion = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const complexityLevel = location.state?.complexityLevel;
   const [level, setLevel] = useState('0');
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(getTriviaQuestionInLevel(complexityLevel));
  }, []);

  const questionEntity = useAppSelector(state => state.triviaQuestion.entity);
  const loading = useAppSelector(state => state.triviaQuestion.loading);
  const isLastQuestion = useAppSelector(state => state.triviaQuestion.isLastQuestion);
  

  const handleNextQuestionPageClick = () => {
    dispatch(getTriviaQuestionInLevel(complexityLevel));
  };
  
  return (
        <div>
          <h3>{complexityLevel} Quiz</h3>
          <dd><span>{questionEntity.questionType}</span></dd>
          <dd><span>Question {questionEntity.id}.</span></dd>
          <dt><span>{questionEntity.question}</span></dt>
          
          <br/><br/>

          {questionEntity.triviaAnswers && questionEntity.triviaAnswers.length > 0 ? (
            <div className="table-responsive">
              {questionEntity.triviaAnswers.map((answer, i) => (
                <div key={`entity-${i}`}  className="answer">
                    <span>
                      <input type={questionEntity.answerType === AnswerType.MULTIPLE ? 'checkbox' : 'radio'} name="selectedAnswers" value="{answer.id}"/>
                    </span>
                    &nbsp;&nbsp;<span>{answer.answer}</span>
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
          <div className="button-section">
              {isLastQuestion == 'Y' ? (
              <Button tag={Button} onClick={handleNextQuestionPageClick} 
                  replace color="primary" data-cy="entityDetailsBackButton">
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.end">End</Translate>
                </span>
              </Button>
              ):(
              <Button tag={Button} onClick={handleNextQuestionPageClick} 
                  replace color="info" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-right" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.next">Next</Translate>
                </span>
              </Button>
              )}
          </div>

        </div>
    );
}
export default TriviaGameQuestion;