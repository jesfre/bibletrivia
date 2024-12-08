import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trivia from './trivia';
import TriviaDetail from './trivia-detail';
import TriviaUpdate from './trivia-update';
import TriviaDeleteDialog from './trivia-delete-dialog';

const TriviaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Trivia />} />
    <Route path="new" element={<TriviaUpdate />} />
    <Route path=":id">
      <Route index element={<TriviaDetail />} />
      <Route path="edit" element={<TriviaUpdate />} />
      <Route path="delete" element={<TriviaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TriviaRoutes;
