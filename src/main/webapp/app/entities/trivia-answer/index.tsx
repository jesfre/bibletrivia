import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TriviaAnswer from './trivia-answer';
import TriviaAnswerDetail from './trivia-answer-detail';
import TriviaAnswerUpdate from './trivia-answer-update';
import TriviaAnswerDeleteDialog from './trivia-answer-delete-dialog';

const TriviaAnswerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TriviaAnswer />} />
    <Route path="new" element={<TriviaAnswerUpdate />} />
    <Route path=":id">
      <Route index element={<TriviaAnswerDetail />} />
      <Route path="edit" element={<TriviaAnswerUpdate />} />
      <Route path="delete" element={<TriviaAnswerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TriviaAnswerRoutes;
