package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.App;
import com.mrezanasirloo.slick.Presenter;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import static com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList.POPULAR;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-09-17
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
        if (isInEditMode()) { return; }
        super.onAttachedToWindow();
        Logger.d("onAttachedToWindow() called: " + getUniqueId());
        PresenterCardList_Slick.onAttach(this);
        getLayoutManager().scrollToPosition(scrollPosition);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (isInEditMode()) { return; }
        super.onDetachedFromWindow();
        Logger.d("onDetachedFromWindow() called: " + getUniqueId());
        PresenterCardList_Slick.onDetach(this);
    }

    @Override
    public void onBind(@NonNull String instanceId) {
        id = instanceId;
        App.componentMain().inject(this);
        PresenterCardList_Slick.bind(this);
    }
}
