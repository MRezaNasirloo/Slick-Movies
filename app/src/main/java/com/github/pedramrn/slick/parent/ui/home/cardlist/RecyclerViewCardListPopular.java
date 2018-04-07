package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.App;
import com.mrezanasirloo.slick.Presenter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import static com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList.POPULAR;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class RecyclerViewCardListPopular extends RecyclerViewCardListAbs {

    @Inject
    @Named(POPULAR)
    Provider<PresenterCardList> provider;
    @Presenter
    PresenterCardList presenter;

    public RecyclerViewCardListPopular(Context context) {
        super(context);
    }

    public RecyclerViewCardListPopular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCardListPopular(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        if (isInEditMode()) return;
        super.onAttachedToWindow();
        App.componentMain().inject(this);
        PresenterCardList_Slick.bind(this);
        PresenterCardList_Slick.onAttach(this);
        getLayoutManager().scrollToPosition(scrollPosition);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (isInEditMode()) return;
        super.onDetachedFromWindow();
        PresenterCardList_Slick.onDetach(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PresenterCardList_Slick.onDestroy(this);
    }
}
