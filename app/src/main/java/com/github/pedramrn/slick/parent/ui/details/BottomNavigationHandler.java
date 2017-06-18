package com.github.pedramrn.slick.parent.ui.details;

import android.support.v7.widget.RecyclerView;

import com.github.pedramrn.slick.parent.ui.main.BottomBarHost;

import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-18
 */

interface BottomNavigationHandler {
    Disposable handle(BottomBarHost bottomBarHost, RecyclerView recyclerView);
}
