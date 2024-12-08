import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BibleReference from './bible-reference';
import Trivia from './trivia';
import TriviaAnswer from './trivia-answer';
import TriviaQuestion from './trivia-question';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="bible-reference/*" element={<BibleReference />} />
        <Route path="trivia/*" element={<Trivia />} />
        <Route path="trivia-answer/*" element={<TriviaAnswer />} />
        <Route path="trivia-question/*" element={<TriviaQuestion />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
