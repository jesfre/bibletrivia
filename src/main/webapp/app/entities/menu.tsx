import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate } from 'react-jhipster';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/bible-reference">
        <Translate contentKey="global.menu.entities.bibleReference" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trivia">
        <Translate contentKey="global.menu.entities.trivia" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trivia-answer">
        <Translate contentKey="global.menu.entities.triviaAnswer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trivia-question">
        <Translate contentKey="global.menu.entities.triviaQuestion" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
