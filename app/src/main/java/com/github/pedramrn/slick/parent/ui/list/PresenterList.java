package com.github.pedramrn.slick.parent.ui.list;

import android.util.Log;

import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.github.pedramrn.slick.parent.ui.list.state.ViewStateList;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterList extends PresenterBase<ViewList, ViewStateList> {

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
                .map(new Function<ItemViewListParcelable, Item>() {
                    @Override
                    public Item apply(@NonNull ItemViewListParcelable itemViewListParcelable) throws Exception {
                        return itemViewListParcelable.render("LIST");
                    }
                })
                .buffer(view.data().size())
                .map(new Function<List<Item>, PartialViewState<ViewStateList>>() {
                    @Override
                    public PartialViewState<ViewStateList> apply(@NonNull List<Item> items) throws Exception {
                        Log.d(TAG, "apply() called");
                        return new ViewStateList.ItemList(items);
                    }
                })
                .subscribeOn(io);

        reduce(
                ViewStateList.builder()
                        .items(Collections.<Item>emptyList())
                        .build(),
                merge(data, Observable.<PartialViewState<ViewStateList>>never())
        ).subscribe(this);
    }
}
