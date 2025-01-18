import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuizEntry from './quiz-entry';
import QuizEntryDetail from './quiz-entry-detail';
import QuizEntryUpdate from './quiz-entry-update';
import QuizEntryDeleteDialog from './quiz-entry-delete-dialog';

const QuizEntryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuizEntry />} />
    <Route path="new" element={<QuizEntryUpdate />} />
    <Route path=":id">
      <Route index element={<QuizEntryDetail />} />
      <Route path="edit" element={<QuizEntryUpdate />} />
      <Route path="delete" element={<QuizEntryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuizEntryRoutes;
