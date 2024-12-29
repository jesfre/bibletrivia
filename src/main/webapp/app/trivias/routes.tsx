import React from 'react';
import { Route } from 'react-router-dom';
import TriviaGame from './trivia-game';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="trivia-game/*" element={<TriviaGame />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
