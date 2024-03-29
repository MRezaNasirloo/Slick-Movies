package com.github.pedramrn.slick.parent.ui.favorite;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.domain.router.RouterFavorite;
import com.github.pedramrn.slick.parent.domain.router.RouterMovie;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImpl;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterFavoriteImplSlick;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterMovieImpl;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteList;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteListEmpty;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteListError;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteListNoOp;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteListProgressive;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.util.ScanToMap;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
import com.xwray.groupie.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterFavorite extends SlickPresenterUni<ViewFavorite, ViewStateFavorite> {

    private final RouterMovie routerMovie;
    private final RouterAuth routerAuth;
    private final RouterFavorite routerFavorite;
    private final MapperMovieDomainMovie mapper;
    private final ScanToMap<Item> scanToMap = new ScanToMap<>();

    @Inject
    public PresenterFavorite(
            RouterMovieImpl routerMovie,
            RouterAuthImpl routerAuth,
            RouterFavoriteImplSlick routerFavorite,
            MapperMovieDomainMovie mapper,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main
    ) {
        super(main, io);
        this.routerMovie = routerMovie;
        this.routerAuth = routerAuth;
        this.routerFavorite = routerFavorite;
        this.mapper = mapper;
    }

    @Override
    protected void start(ViewFavorite view) {
        Observable<Object> commandRefresh = command(ViewFavorite::triggerRefresh);
        // flatMapping on the routerFavorite in case of whenever it failed, we don't lost the whole stream
        Observable<PartialViewState<ViewStateFavorite>> favoriteList =
                commandRefresh.startWith(1).flatMap(o -> routerAuth.firebaseUserSignInStateStream()
                        .doOnNext(aBoolean -> Log.e(TAG, "Signed In: " + aBoolean))
                        .filter(signedIn -> signedIn)
                        .doOnComplete(() -> Log.e(TAG, "Completed-2"))
                        .flatMap(user -> routerFavorite.updateStream()
                                // ^ read the comment above
                                .doOnComplete(() -> Log.e(TAG, "Completed-1"))
                                .doOnError(Throwable::printStackTrace)
                                .doOnNext(favoriteDomains -> Log.e(TAG, "Size: " + favoriteDomains.size()))
                                .concatMap(favorites -> routerMovie.movie(favorites).subscribeOn(io)
                                        .map(mapper)
                                        .map(new MapProgressive())
                                        .cast(Movie.class)
                                        .map(movie -> movie.render("FAVORITE"))
                                        .compose(new ScanToMap<>())
                                        .map(new Function<Map<Integer, Item>, PartialViewState<ViewStateFavorite>>() {
                                            @Override
                                            public PartialViewState<ViewStateFavorite> apply(Map<Integer, Item> favoriteMap) throws Exception {
                                                Log.e(TAG, "new FavoriteList size: " + favoriteMap.size());
                                                return new FavoriteList(favoriteMap);
                                            }
                                        })
                                        .lift(new OnCompleteReturn<PartialViewState<ViewStateFavorite>>() {
                                            @Override
                                            public PartialViewState<ViewStateFavorite> apply(@NonNull Boolean hadError) throws Exception {
                                                return new FavoriteListEmpty(hadError, false);
                                            }
                                        })
                                        .startWith(new FavoriteListProgressive())
                                        .onErrorReturn(FavoriteListError::new)
                                        .doOnComplete(() -> Log.e(TAG, "Completed1"))
                                )
                                .onErrorReturn(FavoriteListError::new)//Let's survive from routerFavorite possible termination.
                                .takeUntil(routerAuth.firebaseUserSignInStateStream().filter(signedIn2 -> !signedIn2))

                        )
                        .onErrorReturn(FavoriteListError::new)//Let's survive from routerFavorite possible termination.
                )
                        .doOnComplete(() -> Log.e(TAG, "Completed2"));

        Observable<PartialViewState<ViewStateFavorite>> clearListOnSignOut = routerAuth.firebaseUserSignInStateStream().filter(signedIn -> !signedIn)
                .map(aBoolean -> new FavoriteListEmpty(false, true));

        Observable<PartialViewState<ViewStateFavorite>> clearOnEmpty = routerAuth.firebaseUserSignInStateStream()
                .filter(signedIn -> signedIn)
                .flatMap(aBoolean -> routerFavorite.updateStream()
                        .filter(favoriteDomains -> favoriteDomains.size() == 0)
                        .map((Function<List<FavoriteDomain>, PartialViewState<ViewStateFavorite>>) favoriteDomains -> new FavoriteListEmpty(false, true))
                        .onErrorReturn(throwable -> new FavoriteListNoOp())
                );


        ViewStateFavorite initialState = ViewStateFavorite.builder().favorites(Collections.emptyMap()).build();
        reduce(initialState, merge(favoriteList, clearListOnSignOut, clearOnEmpty)).subscribe(this);
    }

    private static final String TAG = PresenterFavorite.class.getSimpleName();

    @Override
    protected void render(@NonNull ViewStateFavorite state, @NonNull ViewFavorite view) {
        view.updateFavorites(Arrays.asList(state.favorites().values().toArray(new Item[state.favorites().size()])));
    }
}
