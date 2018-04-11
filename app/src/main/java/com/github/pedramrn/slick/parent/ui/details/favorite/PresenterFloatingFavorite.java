package com.github.pedramrn.slick.parent.ui.details.favorite;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.domain.router.RouterFavorite;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImplSlick;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterFavoriteImplSlick;
import com.github.pedramrn.slick.parent.utils.LogIt;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public class PresenterFloatingFavorite extends SlickPresenterUni<ViewFloatingFavorite, Boolean> {

    private final RouterAuth routerAuth;
    private final RouterFavorite routerFav;

    @Inject
    public PresenterFloatingFavorite(
            RouterAuthImplSlick routerAuth,
            RouterFavoriteImplSlick routerFav,
            @Named("main") Scheduler main, @Named("io") Scheduler io
    ) {
        super(main, io);
        this.routerAuth = routerAuth;
        this.routerFav = routerFav;
    }

    @Override
    protected void start(ViewFloatingFavorite view) {
        Observable<Boolean> commandFavorite = command(ViewFloatingFavorite::commandFavorite);

        Observable<MovieBasic> movie = command(ViewFloatingFavorite::movie).replay(1).autoConnect();

        @SuppressWarnings("ConstantConditions")
        Observable<PartialViewState<Boolean>> favorite = commandFavorite.skipUntil(movie).doOnEach(new LogIt<>("LOG_IT_COMMAND"))
                .flatMap((Function<Boolean, ObservableSource<Pair<Boolean, FavoriteDomain>>>) add ->
                        movie.doOnEach(new LogIt<>("LOG_IT_COMMAND_MOVIE"))
                                .map(this::toFavoriteDomain)
                                .map(item -> Pair.create(add, item))
                )
                .flatMap(pair -> pair.first ? routerFav.add(pair.second) : routerFav.remove(pair.second).subscribeOn(io))
                .map(isFavorite -> (PartialViewState<Boolean>) state -> state)
                .onErrorReturn(throwable -> state -> state)
                .doOnEach(new LogIt<>("LOG_IT_COMMAND_MAIN"));

        Observable<Boolean> signInStream = routerAuth.firebaseUserSignInStateStream().share().replay(1).autoConnect();
        Observable<PartialViewState<Boolean>> favoriteStream = movie.take(1)
                .doOnEach(new LogIt<>("LOG_IT_TRIGGER"))
                .flatMap(o -> signInStream.doOnEach(new LogIt<>("LOG_IT_SIGN")))
                .filter(signedIn -> signedIn)
                .flatMap(o -> movie.take(1))
                .flatMap(movie1 -> routerFav.updateStream(movie1.id())
                        .takeUntil(signInStream.filter(signedIn2 -> !signedIn2).doOnEach(new LogIt<>("LOG_IT_UPDATE"))))
                .map((Function<Boolean, PartialViewState<Boolean>>) added -> state -> added)
                .onErrorReturn(throwable -> state -> state)
                .doOnEach(new LogIt<>("LOG_IT_MAIN"))
                .subscribeOn(io);

        reduce(false, merge(favorite, favoriteStream)).subscribe(this);
    }


    @Override
    protected void render(@NonNull Boolean state, @NonNull ViewFloatingFavorite view) {
        System.out.println("LOG_IT_PresenterFloatingFavorite.render state = [" + state + "]");
        view.setFavorite(state);
    }

    private FavoriteDomain toFavoriteDomain(MovieBasic movieBasic) {
        return FavoriteDomain.create(movieBasic.imdbId(), movieBasic.id(), movieBasic.title(), "movie");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("LOG_IT_PresenterFloatingFavorite.onDestroy");
    }
}
