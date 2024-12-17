import React from 'react';
import { Route } from 'react-router-dom';
import Trivia1 from './trivia1';
//import Trivia2 from './trivia2';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        {/*<Route path="trivia1/*" element={<Trivia1 />} />*/}
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
