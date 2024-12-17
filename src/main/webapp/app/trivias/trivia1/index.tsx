import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trivia1 from './trivia1';

const TriviasRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Trivia1 />} />
  </ErrorBoundaryRoutes>
);

export default TriviasRoutes;
