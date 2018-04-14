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

import static com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList.TRENDING;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-09-17
 */
public class RecyclerViewCardListTrending extends RecyclerViewCardListAbs {

    @Inject
    @Named(TRENDING)
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
        if (isInEditMode()) { return; }
        super.onAttachedToWindow();
        Logger.d("onAttachedToWindow() called: " + getUniqueId());
        PresenterCardList_Slick.onAttach(this);
        getLayoutManager().scrollToPosition(scrollPosition);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (isInEditMode()) { return; }
        Logger.d("onDetachedFromWindow() called: " + getUniqueId());
        super.onDetachedFromWindow();
        PresenterCardList_Slick.onDetach(this);
    }

    @Override
    public void onBind(@NonNull String instanceId) {
        id = instanceId;
        App.componentMain().inject(this);
        PresenterCardList_Slick.bind(this);
    }
}
