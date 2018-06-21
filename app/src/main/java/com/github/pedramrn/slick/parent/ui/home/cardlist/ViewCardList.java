package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.support.annotation.NonNull;

import com.xwray.groupie.Item;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public interface ViewCardList {

    void loading(boolean isLoading);
    void updateList(List<Item> items);

    int pageSize();

    @NonNull
    Observable<Object> trigger();
}
