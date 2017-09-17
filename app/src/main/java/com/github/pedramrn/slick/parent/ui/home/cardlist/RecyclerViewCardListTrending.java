package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.ui.home.PresenterHome;
import com.github.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class RecyclerViewCardListTrending extends RecyclerViewCardListAbs {

    @Inject
    @Named(PresenterHome.TRENDING)
    Provider<PresenterCardList> provider;

    @Presenter
    PresenterCardList presenter;

    public RecyclerViewCardListTrending(Context context) {
        super(context);
    }

    public RecyclerViewCardListTrending(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCardListTrending(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        App.componentMain().inject(this);
        RecyclerViewCardListTrending_Slick.bind(this);
        RecyclerViewCardListTrending_Slick.onAttach(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RecyclerViewCardListTrending_Slick.onDetach(this);
    }
}
