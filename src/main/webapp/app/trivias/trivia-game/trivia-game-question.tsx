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
import { getQuestion, updateTrivia } from './trivia-game.reducer';
import { getEntitiesForQuestion } from 'app/entities/trivia-answer/trivia-answer.reducer';
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';
import { getTriviaQuestionInLevel } from 'app/entities/trivia-question/trivia-question.reducer';

{/* prettier-ignore */}

const TriviaGameQuestion = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const complexityLevel = location.state?.complexityLevel;
  const navigate = useNavigate();
  const [level, setLevel] = useState('0');
  const [isChecked, setIsChecked] = useState(false);
  const [selectedValues, setSelectedValues] = useState([]);

  useEffect(() => {
    dispatch(getTriviaQuestionInLevel(complexityLevel));
  }, []);

  const questionEntity = useAppSelector(state => state.triviaQuestion.entity);
  const loading = useAppSelector(state => state.triviaQuestion.loading);
  const isLastQuestion = useAppSelector(state => state.triviaQuestion.isLastQuestion);
  

  const handleNextQuestionPageClick = () => {
    var data = {questionId:questionEntity.id, answers:selectedValues};
    dispatch(updateTrivia(data));
    dispatch(getTriviaQuestionInLevel(complexityLevel));
  };

  const handleChkChanged = (event) => {
    setIsChecked(event.target.checked);
    if(event.target.checked && !selectedValues.includes(event.target.value)) {
      setSelectedValues([...selectedValues, event.target.value]);
      console.log (`val=${selectedValues}`);
    }
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
                      <input type={questionEntity.answerType === AnswerType.MULTIPLE ? 'checkbox' : 'radio'} name="selectedAnswers" value={answer.id}
                        onChange={handleChkChanged}/>
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
              <Button tag={Button} onClick={handleNextQuestionPageClick} replace color="info" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              {isLastQuestion == 'Y' ? (
              <Button tag={Button} replace color="primary" disabled={true}>
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.end">End</Translate>
                </span>
              </Button>
              ):(
              <Button tag={Button} onClick={handleNextQuestionPageClick} 
                  replace color="info" data-cy="entityDetailsBackButton">
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