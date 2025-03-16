import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button, Table, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IQuizEntry, defaultValue } from 'app/shared/model/quiz-entry.model';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createQuiz, getNextQuestion, getPreviousQuestion, resetQuiz, updateQuiz, updateQuizEntry } from './trivia-game.reducer';
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
  
  const [selectedAnswers, setSelectedAnswers] = useState(quizEntry.selectedAnswers);

  useEffect(() => {
	  /*
	  var containsEntry = false;
        quizEntry.forEach(te => te.id == state.entity.id ? containsEntry=true : null);
        if(!containsEntry) {
        	state.entities = [...state.entities, state.entity];
		}
        state.entity = action.payload.data;
	  */
    dispatch(getNextQuestion(questionNumber ? questionNumber-1 : 0));
  }, []);

  const handleNextQuestionPageClick = () => {
	  const fetchData = async () => {
		if(quizId < 1) {
			// Will update only when quiz is not finished
		    var updateParams = {questionNum:quizEntry.orderNum ?? 0, answers:selectedValues ?? []};
		    await dispatch(updateQuiz(updateParams));
		}
	   
	    dispatch(getNextQuestion(quizEntry.orderNum ?? 0));
    };
    fetchData();
  };
  
  const handlePreviousQuestionPageClick = () => {
	const fetchData = async () => {
		if(quizId < 1) {
			// Will update only when quiz is not finished
			var updateParams = {questionNum:quizEntry.orderNum ?? 0, answers:selectedValues ?? []};
		    await dispatch(updateQuiz(updateParams));
	    }
	    dispatch(getPreviousQuestion(quizEntry.orderNum ?? 0));
	};
	fetchData();
  };

  const handleChkChanged = (event) => {
    dispatch(updateQuizEntry({ansId:event.target.value, ansChk:event.target.checked}));

    if(event.target.checked && !selectedValues.includes(event.target.value)) {
      setSelectedValues([...selectedValues, event.target.value]);
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

          {quizEntry.triviaQuestion?.triviaAnswers && quizEntry.triviaQuestion?.triviaAnswers.length > 0 ? (
            <div className="table-responsive">
              {quizEntry.triviaQuestion?.triviaAnswers.map((answer: ITriviaAnswer, i) => (
                <div key={`entity-${i}`}  className="answer">
                    <span>
                      <input type={quizEntry.triviaQuestion.answerType === AnswerType.MULTIPLE ? 'checkbox' : 'radio'} 
                      	name="selectedAnswers" value={answer.id}
                        onChange={handleChkChanged} disabled={quizId > 0} 
                        checked={answer.selected} />
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
			  <Button tag={Button} color="primary" disabled={true}>
			    <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
            ):(
			  <Button tag={Button} onClick={handlePreviousQuestionPageClick} color="info" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
			)}
              &nbsp;
              {isLastQuestion == 'Y' ? (
	              <Button tag={Link} color="primary" to="/game/trivia-game/score"  >
					{quizId > 0 ? (
					<React.Fragment>
						<FontAwesomeIcon icon="list-check" />
						<span className="d-none d-md-inline">
	                 		<Translate contentKey="entity.action.viewScore">View Score</Translate>
	                 	</span>
	                </React.Fragment>
	                ):(
					<React.Fragment>
						<FontAwesomeIcon icon="check" />
						<span className="d-none d-md-inline">
							<Translate contentKey="entity.action.finishQuiz">Finish</Translate>
						</span>
					</React.Fragment>
					)}
	              </Button>
              ):(
	              <Button tag={Button} onClick={handleNextQuestionPageClick} color="info" data-cy="entityDetailsBackButton">
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