import React, { useEffect, useState } from 'react';
import dayjs from 'dayjs';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getScore } from './trivia-game-score.reducer';
import { IQuiz } from 'app/shared/model/quiz.model';

{/* prettier-ignore */}

const TriviaGameResult = () => {
	const dispatch = useAppDispatch();
	
	const quiz: IQuiz = useAppSelector(state => state.triviaGameScore.entity);
	const loading = useAppSelector(state => state.triviaGameScore.loading);
	
  	useEffect(() => {
		dispatch(getScore());
	}, []);

  	return (
        <div>
          <h3>Quiz Results</h3>
          <br/>
          <h4>{quiz.quizTaker}'s Quiz</h4>
          <h5>Started at {dayjs(quiz.startDate).format('MM/DD/YYYY HH:mm:ss')}</h5>
          <br/>
          <h5>Questions: {quiz.totalQuestions}</h5>
          <h5>Correct answers: {quiz.correctQuestions}</h5>
          <h5>Errors: {quiz.errorCount}</h5>
          <br/>

        </div>
    );
}
export default TriviaGameResult;