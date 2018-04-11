package com.github.pedramrn.slick.parent.ui.details.favorite;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.jakewharton.rxbinding2.view.RxView;
import com.mrezanasirloo.slick.OnDestroyListener;
import com.mrezanasirloo.slick.Presenter;
import com.mrezanasirloo.slick.SlickUniqueId;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.mrezanasirloo.slick.SlickDelegateActivity.SLICK_UNIQUE_KEY;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public class FloatingFavorite extends FloatingActionButton implements ViewFloatingFavorite, OnDestroyListener, SlickUniqueId {

    @Inject
    Provider<PresenterFloatingFavorite> provider;
    @Presenter
    PresenterFloatingFavorite presenter;

    BehaviorSubject<MovieBasic> movieId = BehaviorSubject.create();
    private static Drawable drawableFav;
    private static Drawable drawableUnFav;

    public FloatingFavorite(Context context) {
        super(context);
        init(context);
    }

    public FloatingFavorite(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatingFavorite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        setEnabled(false);
        if (drawableFav == null) {
            Resources resources = getResources();
            drawableUnFav = ResourcesCompat.getDrawable(resources, R.drawable.ic_unlike_black_24dp, null);
            drawableFav = ResourcesCompat.getDrawable(resources, R.drawable.ic_like_red_24dp, null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("LOG_IT_FloatingFavorite.onAttachedToWindow");
        App.componentMain().inject(this);
        PresenterFloatingFavorite_Slick.bind(this);
        PresenterFloatingFavorite_Slick.onAttach(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        System.out.println("LOG_IT_FloatingFavorite.onDetachedFromWindow");
        super.onDetachedFromWindow();
        setOnClickListener(null);
        PresenterFloatingFavorite_Slick.onDetach(this);
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            setImageDrawable(drawableFav);
            return;
        }
        setImageDrawable(drawableUnFav);
    }

    @Override
    public Observable<Boolean> commandFavorite() {
        return RxView.clicks(this).throttleLast(1, TimeUnit.SECONDS)
                .map(o -> drawableUnFav.equals(getDrawable()))
                ;
    }

    @Override
    public Observable<MovieBasic> movie() {
        return movieId;
    }

    @Override
    public void setMovie(MovieBasic movie) {
        setEnabled(true);
        movieId.onNext(movie);
    }

    @Override
    public void onDestroy() {
        System.out.println("LOG_IT_FloatingFavorite.onDestroy");
        drawableFav = null;
        drawableUnFav = null;

        if (presenter == null) return;
        PresenterFloatingFavorite_Slick.onDestroy(this);

    }

    @Nullable
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString(SLICK_UNIQUE_KEY, this.id);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.id = bundle.getString(SLICK_UNIQUE_KEY);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    private String id;

    @Override
    public String getUniqueId() {
        return id = (id != null ? id : UUID.randomUUID().toString());
    }
}
