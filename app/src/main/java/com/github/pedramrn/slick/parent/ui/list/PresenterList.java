package com.github.pedramrn.slick.parent.ui.list;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.ui.list.state.ViewStateList;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterList extends SlickPresenterUni<ViewList, ViewStateList> {

    @Inject
    public PresenterList(
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);

    }

    private static final String TAG = PresenterList.class.getSimpleName();

    @Override
    protected void start(ViewList view) {
        Log.d(TAG, "start() called");
        Observable<PartialViewState<ViewStateList>> data = Observable.fromIterable(view.data())
                .map(itemViewListParcelable -> itemViewListParcelable.render("LIST"))
                .buffer(view.data().size())
                .map((Function<List<Item>, PartialViewState<ViewStateList>>) items -> {
                    Log.d(TAG, "apply() called");
                    return new ViewStateList.ItemList(items);
                })
                .subscribeOn(io);

        ViewStateList initialState = ViewStateList.builder()
                .items(Collections.emptyList())
                .build();

        subscribe(initialState, data);
    }

    @Override
    protected void render(@NonNull ViewStateList state, @NonNull ViewList view) {
        view.update(state);
    }
}
