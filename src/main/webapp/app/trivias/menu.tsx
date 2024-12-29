import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate } from 'react-jhipster';

const TriviasMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/game/trivia-game">
        <Translate contentKey="global.menu.trivias.trivia-game" />
      </MenuItem>
    </>
  );
};

export default TriviasMenu;
