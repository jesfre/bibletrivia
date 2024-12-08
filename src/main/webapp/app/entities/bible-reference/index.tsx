import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BibleReference from './bible-reference';
import BibleReferenceDetail from './bible-reference-detail';
import BibleReferenceUpdate from './bible-reference-update';
import BibleReferenceDeleteDialog from './bible-reference-delete-dialog';

const BibleReferenceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BibleReference />} />
    <Route path="new" element={<BibleReferenceUpdate />} />
    <Route path=":id">
      <Route index element={<BibleReferenceDetail />} />
      <Route path="edit" element={<BibleReferenceUpdate />} />
      <Route path="delete" element={<BibleReferenceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BibleReferenceRoutes;
