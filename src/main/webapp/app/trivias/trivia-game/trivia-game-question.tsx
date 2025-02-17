import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button, Table, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IQuizEntry, defaultValue } from 'app/shared/model/quiz-entry.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createQuiz, getNextQuestion, getPreviousQuestion, resetQuiz, updateQuiz } from './trivia-game.reducer';
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';
import { ITriviaAnswer } from 'app/shared/model/trivia-answer.model';

{/* prettier-ignore */}

const TriviaGameQuestion = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const complexityLevel = location.state?.complexityLevel;
  const questionNumber = location.state?.questionNumber;
  const navigate = useNavigate();
  const [level, setLevel] = useState('0');
  const [isChecked, setIsChecked] = useState(false);
  const [selectedValues, setSelectedValues] = useState([]);

  const quizEntry: IQuizEntry = useAppSelector(state => state.triviaGameQuestion.entity);
  const loading = useAppSelector(state => state.triviaGameQuestion.loading);
  const isLastQuestion = useAppSelector(state => state.triviaGameQuestion.isLastQuestion);
  const isFirstQuestion = useAppSelector(state => state.triviaGameQuestion.isFirstQuestion);
  const quizId:number = quizEntry.quiz?quizEntry.quiz.id:0;

  useEffect(() => {
    dispatch(getNextQuestion(questionNumber ? questionNumber-1 : 0));
  }, []);

  const handleNextQuestionPageClick = () => {
	  const fetchData = async () => {
	    var updateParams = {questionNum:quizEntry.orderNum ?? 0, answers:selectedValues ?? []};
	    await dispatch(updateQuiz(updateParams));
	   
	    dispatch(getNextQuestion(quizEntry.orderNum ?? 0));
    };
    fetchData();
  };
  
  const handlePreviousQuestionPageClick = () => {
	const fetchData = async () => {
	var updateParams = {questionNum:quizEntry.orderNum ?? 0, answers:selectedValues ?? []};
	    await dispatch(updateQuiz(updateParams));
	    dispatch(getPreviousQuestion(quizEntry.orderNum ?? 0));
	};
	fetchData();
  };

  const handleChkChanged = (event) => {
    setIsChecked(event.target.checked);
    if(event.target.checked && !selectedValues.includes(event.target.value)) {
      setSelectedValues([...selectedValues, event.target.value]);
      console.log (`val=${selectedValues}`);
    }
  };
  
  const handleFinishClick = () => {
	const execute = async () => {
	  	//await dispatch(createQuiz(level));
	  	//await dispatch(getResults());
      	navigate('/game/trivia-game/score');
	  };
      execute();  
  };
  
  return (
        <div>
          <h3>{complexityLevel} Quiz {quizId}</h3>
          <dd><span>Question #{quizEntry.orderNum}.</span></dd>
          <dd><span>Question ID: {quizEntry.triviaQuestion? quizEntry.triviaQuestion.id : ''}.</span></dd>
          <dd><span>{quizEntry.triviaQuestion? quizEntry.triviaQuestion.questionType : ''}</span></dd>
          <dt><span>{quizEntry.triviaQuestion? quizEntry.triviaQuestion.question: ''}</span></dt>
          <br/><br/>

          {quizEntry.triviaAnswers && quizEntry.triviaAnswers.length > 0 ? (
            <div className="table-responsive">
              {quizEntry.triviaAnswers.map((answer: ITriviaAnswer, i) => (
                <div key={`entity-${i}`}  className="answer">
                    <span>
                      <input type={quizEntry.triviaQuestion.answerType === AnswerType.MULTIPLE ? 'checkbox' : 'radio'} name="selectedAnswers" value={answer.id}
                        onChange={handleChkChanged} disabled={quizId > 0} />
                    </span>
                    &nbsp;&nbsp;<span>{answer.answer}</span> -- {answer.correct ? 'true' : 'false'}
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
          	{isFirstQuestion == 'Y' ? (
			  <Button tag={Button} replace color="primary" disabled={true}>
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
            ):(
			  <Button tag={Button} onClick={handlePreviousQuestionPageClick} replace color="info" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
			)}
              &nbsp;
              {isLastQuestion == 'Y' ? (
	              <Button tag={Link} color="primary" to="/game/trivia-game/score"  >
	                <span className="d-none d-md-inline">
	                  <Translate contentKey="entity.action.finishQuiz">Finish</Translate>
	                </span>
	              </Button>
              ):(
	              <Button tag={Button} onClick={handleNextQuestionPageClick} replace color="info" data-cy="entityDetailsBackButton">
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