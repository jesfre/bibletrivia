import React, { useEffect, useState } from 'react';
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
          <br/>{quiz.quizTaker}
          <br/>{quiz.totalQuestions}

          <br/><br/>

        </div>
    );
}
export default TriviaGameResult;