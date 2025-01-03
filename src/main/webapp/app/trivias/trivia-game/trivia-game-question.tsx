import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';
import { createAsyncThunk } from '@reduxjs/toolkit';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ITriviaGameQuestion, defaultValue } from 'app/shared/model/trivia-game-question.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getQuestion } from './trivia-game.reducer';

const TriviaGameQuestion = () => {
  const dispatch = useAppDispatch();

  const questionNum = 1;

  useEffect(() => {
    dispatch(getQuestion(questionNum));
  }, []);

  const questionEntity = useAppSelector(state => state.triviaGameQuestion.entity);
  
  return (
        <div>
          <p>Question</p>
          <span>Question {questionEntity.questionNumber}.</span> <br/>
          <span>{questionEntity.questionText}</span>
        </div>
    );
}
export default TriviaGameQuestion;