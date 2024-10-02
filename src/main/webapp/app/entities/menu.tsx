import React from 'react';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/student">
        <Translate contentKey="global.menu.entities.student" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/department">
        <Translate contentKey="global.menu.entities.department" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
