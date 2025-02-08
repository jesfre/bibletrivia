import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TriviaGame from './trivia-game';
import TriviaGameQuestion from './trivia-game-question';
import TriviaGameScore from './trivia-game-score';

const TriviaGameRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TriviaGame />} />
    <Route path="question" element={<TriviaGameQuestion />} />
    <Route path="score" element={<TriviaGameScore />} />
  </ErrorBoundaryRoutes>
);

export default TriviaGameRoutes;
