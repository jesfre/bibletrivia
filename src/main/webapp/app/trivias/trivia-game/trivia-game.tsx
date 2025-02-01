import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { createTrivia, resetTrivia } from 'app/trivias/trivia-game/trivia-game.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

{/* prettier-ignore */}

const TriviaGame = () => {
    const account = useAppSelector(state => state.authentication.account);
    const dispatch = useAppDispatch();
    const triviaLevelValues = Object.keys(TriviaLevel);
    const [level, setLevel] = useState('0');
    const navigate = useNavigate();

    useEffect(() => {
      dispatch(resetTrivia('EASY'));
      dispatch(resetTrivia('DIFFICULT'));
    }, []);

    const handleButtonClick = () => {
      dispatch(createTrivia(''));
      navigate('/game/trivia-game/question', { state: { complexityLevel: level } });
    };

    return (
        <div>
           <p>Select Trivia Complexity</p>

          {account.login ? (
            <input type="hidden" name="owner" value={account.login}/>
          ):( <span/> )}
          <ValidatedField
              label={translate('bibletriviaApp.quiz.quizTaker')}
              id="quiz-quizTaker"
              name="quizTaker"
              type="text"
              //value={account.login}
              validate={{
                required: { value: true, message: translate('entity.validation.required') },
                maxLength: { value: 120, message: translate('entity.validation.maxlength', { max: 120 }) },
              }}
            />
           
           <ValidatedField label={translate('bibletriviaApp.trivia.level')} id="trivia-level" name="level" data-cy="level" type="select" 
              onChange={(e) => setLevel(e.target.value)}>
                  <option value="0"><Translate contentKey="entity.level.selectOne">Select a level</Translate></option>
                {triviaLevelValues.map(triviaLevel => (
                  <option value={triviaLevel} key={triviaLevel}>
                    {translate(`bibletriviaApp.TriviaLevel.${triviaLevel}`)}
                  </option>
                ))}
              </ValidatedField>

           <br/>
          
           <p>Trivia 2</p>
           <Button tag={Button} onClick={handleButtonClick}
              replace color="info" data-cy="entityDetailsBackButton">
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.start">Start</Translate>
            </span>
          </Button>
          &nbsp;
        </div>
    );
}
export default TriviaGame;