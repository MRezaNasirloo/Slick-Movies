package com.github.pedramrn.slick.parent.ui.people;


import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeople;

/**
 * A simple View interface
 */
public interface ViewPeople {

    int personId();

    void render(ViewStatePeople state);

}
