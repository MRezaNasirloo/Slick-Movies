package com.github.pedramrn.slick.parent.ui.details;

import android.support.v7.widget.RecyclerView;

import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-18
 */

public class BottomNavigationHandlerImpl implements BottomNavigationHandler {

    @Inject
    public BottomNavigationHandlerImpl() {
    }

    @Override
    public Disposable handle(final BottomBarHost bottomBarHost, RecyclerView recyclerView) {
        return RxRecyclerView.scrollEvents(recyclerView)
                .subscribe(new Consumer<RecyclerViewScrollEvent>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull RecyclerViewScrollEvent event) throws Exception {
                        if (event.dy() >= 0) {
                            bottomBarHost.hide();
                        } else {
                            bottomBarHost.show();
                        }
                    }
                });
    }
}
