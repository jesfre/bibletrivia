import React, { useEffect, useState } from 'react';
import dayjs from 'dayjs';
import { Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getScore } from './trivia-game-score.reducer';
import { IQuiz } from 'app/shared/model/quiz.model';

import { IQuizEntry } from 'app/shared/model/quiz-entry.model';
import { Button, Table } from 'reactstrap';
import { ITriviaQuestion } from 'app/shared/model/trivia-question.model';

{/* prettier-ignore */}

const TriviaGameResult = () => {
	const dispatch = useAppDispatch();
	
	const quiz: IQuiz = useAppSelector(state => state.triviaGameScore.entity);
	const loading = useAppSelector(state => state.triviaGameScore.loading);
	const quizEntryList: IQuizEntry[] = useAppSelector(state => state.triviaGameScore.entity.quizEntries);
	
  	useEffect(() => {
		dispatch(getScore());
	}, []);
	
  	return (
        <div>
          <h3>Quiz Results {quiz.id}</h3>
          <br/>
          <h4>{quiz.quizTaker}'s Quiz</h4>
          <h5>Started at {dayjs(quiz.startDate).format('MM/DD/YYYY HH:mm:ss')}</h5>
          <br/>
          <h5>Questions: {quiz.totalQuestions}</h5>
          <h5>Correct answers: {quiz.correctQuestions}</h5>
          <h5>Errors: {quiz.errorCount}</h5>
          <br/>

		<h5>List of Questions</h5>
		<div className="table-responsive">
	          {quizEntryList && quizEntryList.length > 0 ? (
	            <Table responsive>
	              <thead>
	                <tr>
	                  <th >
	                    <Translate contentKey="bibletriviaApp.quizEntry.orderNum">Order Num</Translate>{' '}
	                  </th>
	                  <th >
	                    <Translate contentKey="bibletriviaApp.quizEntry.correct">Correct</Translate>{' '}
	                  </th>
	                  <th>
	                    <Translate contentKey="bibletriviaApp.quizEntry.triviaQuestion">Trivia Question</Translate>{' '}
	                  </th>
	                  <th />
	                </tr>
	              </thead>
	              <tbody>
	                {quizEntryList.map((quizEntry, i) => (
	                  <tr key={`entity-${i}`} data-cy="entityTable">
	                    <td>
	                      <Button tag={Link} to={`/game/trivia-game/question`} 
	                      		state={{ questionNumber: quizEntry.orderNum }}
	                      		color="link" size="sm">
	                        {quizEntry.orderNum}
	                      </Button>
	                    </td>
	                    <td>{quizEntry.correct ? 'Yes' : 'No'}</td>
	                    <td>{quizEntry.triviaQuestion?.question}
	                      {quizEntry.triviaQuestion?  quizEntry.triviaQuestion.question : '-' }
	                    </td>
	                    <td className="text-end">
	                      <div className="btn-group flex-btn-group-container">
	                        <Button tag={Link} to={`/quiz-entry/${quizEntry.id}`} color="info" size="sm" data-cy="entityDetailsButton">
	                          <FontAwesomeIcon icon="eye" />{' '}
	                          <span className="d-none d-md-inline">
	                            <Translate contentKey="entity.action.view">View</Translate>
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
	                <Translate contentKey="bibletriviaApp.quizEntry.home.notFound">No Quiz Entries found</Translate>
	              </div>
	            )
	          )}
	      </div>
		
        </div>
    );
}
export default TriviaGameResult;