import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TriviaGame from './trivia-game';

const TriviaGameRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TriviaGame />} />
  </ErrorBoundaryRoutes>
);

export default TriviaGameRoutes;
