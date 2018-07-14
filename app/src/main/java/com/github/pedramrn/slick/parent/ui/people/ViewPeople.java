package com.github.pedramrn.slick.parent.ui.people;


import com.github.pedramrn.slick.parent.ui.home.Retryable;
import com.github.pedramrn.slick.parent.ui.people.state.ViewStatePeople;

/**
 * A simple View interface
 */
public interface ViewPeople extends Retryable {

    int personId();

    void render(ViewStatePeople state);

}
