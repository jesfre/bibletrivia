import React from 'react';
import { translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import TriviasMenuItems from 'app/trivias/menu';

export const TriviasMenu = () => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.trivias.main')}
    id="trivias-menu"
    data-cy="trivias"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <TriviasMenuItems />
  </NavDropdown>
);
