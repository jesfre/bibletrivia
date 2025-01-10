import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

{/* prettier-ignore */}

const TriviaGame = () => {

    const triviaLevelValues = Object.keys(TriviaLevel);
    const [level, setLevel] = useState('0');
    const navigate = useNavigate();

    const handleButtonClick = () => {
      navigate('/game/trivia-game/question', { state: { complexityLevel: level } });
    };

    return (
        <div>
           <p>Select Trivia Complexity</p>
           
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
            <FontAwesomeIcon icon="arrow-right" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.start">Start</Translate>
            </span>
          </Button>
          &nbsp;
        </div>
    );
}
export default TriviaGame;