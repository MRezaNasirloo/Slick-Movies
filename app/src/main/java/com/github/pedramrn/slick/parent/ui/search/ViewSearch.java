package com.github.pedramrn.slick.parent.ui.search;


import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewSearch {

    Observable<String> queryNexText();

    Observable<Boolean> searchOpenClose();

    void update(List<Item> items);


    void showLoading(boolean isLoading);

    void error(String message);
}
