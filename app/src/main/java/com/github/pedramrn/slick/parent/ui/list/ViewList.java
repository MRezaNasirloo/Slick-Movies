package com.github.pedramrn.slick.parent.ui.list;


import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.state.ViewStateList;

import java.util.ArrayList;

/**
 * A simple View interface
 */
public interface ViewList {

    ArrayList<ItemViewListParcelable> data();

    void update(ViewStateList state);
}
