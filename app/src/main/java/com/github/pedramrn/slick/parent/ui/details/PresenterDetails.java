package com.github.pedramrn.slick.parent.ui.details;

import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.CastDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Image;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.slick.SlickPresenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class PresenterDetails extends SlickPresenter<ViewDetails> implements Observer<Movie> {
    private static final String TAG = PresenterDetails.class.getSimpleName();
    private final RouterMovieDetails routerMovieDetails;
    private final Scheduler io;
    private final Scheduler main;
    private BehaviorSubject<ViewStateDetails> state = BehaviorSubject.create();
    private Observable<Movie> details;
    private Disposable disposable;

    @Inject
    public PresenterDetails(RouterMovieDetailsImpl routerMovieDetails, @Named("io") Scheduler io, @Named("main") Scheduler main) {
        this.routerMovieDetails = routerMovieDetails;
        this.io = io;
        this.main = main;
    }

    Observable<ViewStateDetails> updateStream() {
        return state;
    }

    void getMovieDetails(Integer tmdbId) {
        if (details == null) {
            details = routerMovieDetails.get(tmdbId)
                    //Maps the domains Models to View Models which have android dependency
                    .map(new Function<MovieDetails, Movie>() {
                        @Override
                        public Movie apply(@NonNull MovieDetails md) throws Exception {
                            //casts
                            List<Cast> casts = Observable.fromIterable(md.casts()).map(new Function<CastDomain, Cast>() {
                                @Override
                                public Cast apply(@NonNull CastDomain cd) throws Exception {
                                    return Cast.create(cd.id(), cd.castId(), cd.creditId(), cd.name(),
                                            cd.profilePath(), cd.character(), cd.gender(), cd.order());
                                }
                            }).toList().blockingGet();

                            //backdrops
                            final List<Backdrop> backdrops = Observable.fromIterable(md.images().backdrops()).map(new Function<String, Backdrop>() {
                                @Override
                                public Backdrop apply(@NonNull String s) throws Exception {
                                    return Backdrop.create(s);
                                }
                            }).toList(md.images().backdrops().size()).blockingGet();

                            //image
                            Image image = Image.create(backdrops, md.images().posters());

                            return Movie.create(md.id(), md.imdbId(), md.adult(), md.backdropPath(), md.budget(), md.genres(), md.homepage(),
                                    md.overview(), md.popularity(), md.posterPath(), md.productionCompanies(), md.productionCountries(),
                                    md.releaseDate(), md.revenue(), md.runtime(), md.spokenLanguages(), md.status(), md.tagline(), md.title(),
                                    md.video(), md.voteAverage(), md.voteCount(), casts, image);
                        }
                    }).subscribeOn(io).observeOn(main);
            details.subscribe(this);
        }

    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(Movie movie) {
        Log.d(TAG, "onNext() called");
        state.onNext(new ViewStateDetails(movie));
    }

    @Override
    public void onError(Throwable e) {
        // state.onNext(new ViewStateDetailsError(e));
        // TODO: 2017-06-15 show error with view state
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) disposable.dispose();
    }
}
