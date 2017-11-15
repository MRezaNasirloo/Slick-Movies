package com.github.pedramrn.slick.parent.ui.search;


import com.github.pedramrn.slick.parent.ui.error.ErrorMessageHandler;
import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * A simple View interface
 */
public interface ViewSearch extends ErrorMessageHandler {

    Observable<String> queryNexText();

    Observable<Boolean> searchOpenClose();

    void update(List<Item> items);


    void showLoading(boolean isLoading);

    @Override
    void error(String message);
}
