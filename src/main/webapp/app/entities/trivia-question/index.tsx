import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TriviaQuestion from './trivia-question';
import TriviaQuestionDetail from './trivia-question-detail';
import TriviaQuestionUpdate from './trivia-question-update';
import TriviaQuestionDeleteDialog from './trivia-question-delete-dialog';

const TriviaQuestionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TriviaQuestion />} />
    <Route path="new" element={<TriviaQuestionUpdate />} />
    <Route path=":id">
      <Route index element={<TriviaQuestionDetail />} />
      <Route path="edit" element={<TriviaQuestionUpdate />} />
      <Route path="delete" element={<TriviaQuestionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TriviaQuestionRoutes;
