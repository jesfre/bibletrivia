import React, { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Button, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const TriviaGame = () => {

    const triviaLevelValues = Object.keys(TriviaLevel);
    const [level, setLevel] = useState('0');

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
           <Link to={{pathname: '/game/trivia-game/question'}} state={{complexityLevel : level}}
              replace color="info" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-right" />{' '}
                <span className="d-none d-md-inline">Go go</span>
            </Link>
           <br/>
           <p>Trivia 2</p>
           <Button tag={Link} to={{pathname: '/game/trivia-game/question', state:{ complexityLevel: level}}}
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