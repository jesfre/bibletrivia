import React from 'react';
import { Route } from 'react-router-dom';
import TriviaGame from './trivia-game';

{/* prettier-ignore */}
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        <Route path="trivia-game/*" element={<TriviaGame />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
