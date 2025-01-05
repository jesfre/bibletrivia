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
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';

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
  const loading = useAppSelector(state => state.triviaGameQuestion.loading);
  
  return (
        <div>
          <p>Question</p>
          <dt><span>Question {questionEntity.id}.</span></dt>
          <dd><span>{questionEntity.question}</span></dd>
          <dd><span>{questionEntity.questionType}</span></dd>
          <dd><span>{questionEntity.answerType}</span></dd>
          
          <br/><br/>

          {questionEntity.triviaAnswers && questionEntity.triviaAnswers.length > 0 ? (
            <div className="table-responsive">
              {questionEntity.triviaAnswers.map((answer, i) => (
                <div className="answer">
                    <span>
                      <input type={questionEntity.answerType == AnswerType.MULTIPLE ? 'checkbox' : 'radio'} name="selectedAnswers" value="{answer.id}"/>
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

        </div>
    );
}
export default TriviaGameQuestion;