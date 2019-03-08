package com.github.pedramrn.slick.parent.ui.details.favorite;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.jakewharton.rxbinding2.view.RxView;
import com.mrezanasirloo.slick.Presenter;
import com.mrezanasirloo.slick.SlickLifecycleListener;
import com.mrezanasirloo.slick.SlickUniqueId;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public class FloatingFavorite extends FloatingActionButton
        implements ViewFloatingFavorite, SlickLifecycleListener, SlickUniqueId {

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
        System.out.println("LOG_IT_FloatingFavorite.onAttachedToWindow");
        super.onAttachedToWindow();
        if (presenter == null) return;
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

    @NonNull
    @Override
    public Observable<Boolean> commandFavorite() {
        return RxView.clicks(this).throttleLast(1, TimeUnit.SECONDS)
                .map(o -> drawableUnFav.equals(getDrawable()))
                ;
    }

    @NonNull
    @Override
    public Observable<MovieBasic> movie() {
        return movieId;
    }

    @Override
    public void setMovie(MovieBasic movie) {
        setEnabled(true);
        movieId.onNext(movie);
    }

    public void onDestroy() {
        System.out.println("LOG_IT_FloatingFavorite.onDestroy");
        drawableFav = null;
        drawableUnFav = null;
        presenter = null;
    }

    private String id;

    @NonNull
    @Override
    public String getUniqueId() {
        return id;
    }

    @Override
    public void onBind(@NonNull String instanceId) {
        System.out.println("LOG_IT_FloatingFavorite.onBind");
        id = instanceId;
        App.componentMain().inject(this);
        PresenterFloatingFavorite_Slick.bind(this);
    }
}
