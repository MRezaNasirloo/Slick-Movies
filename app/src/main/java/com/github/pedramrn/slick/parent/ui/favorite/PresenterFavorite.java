package com.github.pedramrn.slick.parent.ui.favorite;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.domain.router.RouterFavorite;
import com.github.pedramrn.slick.parent.domain.router.RouterMovie;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImpl;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterFavoriteImplSlick;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterMovieImpl;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteList;
import com.github.pedramrn.slick.parent.ui.favorite.state.FavoriteListError;
import com.github.pedramrn.slick.parent.util.ScanToMap;
import com.xwray.groupie.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
public class PresenterFavorite extends PresenterBase<ViewFavorite, ViewStateFavorite> {

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
                commandRefresh.startWith(1).flatMap(o -> routerAuth.firebaseUserSignInStateStream())
                        .filter(signedIn -> signedIn)
                        .flatMap(ignored -> routerAuth.currentFirebaseUser())
                        .flatMap(user -> routerFavorite.updateStream(user.id())// ^ read the comment above
                                .concatMap(favorites -> routerMovie.movie(favorites).subscribeOn(io)
                                        .map(mapper)
                                        .map(movie -> movie.render("FAVORITE"))
                                        .compose(new ScanToMap<>())
                                        .map((Function<Map<Integer, Item>, PartialViewState<ViewStateFavorite>>) FavoriteList::new)
                                        .onErrorReturn(FavoriteListError::new)
                                        .doOnComplete(() -> Log.e(TAG, "Completed1"))
                                        .takeUntil(routerAuth.firebaseUserSignInStateStream().filter(signedIn2 -> !signedIn2))
                                ).onErrorReturn(FavoriteListError::new)//Let's survive from routerFavorite possible termination.
                        ).doOnComplete(() -> Log.e(TAG, "Completed2"));

        reduce(ViewStateFavorite.builder().favorites(Collections.emptyMap()).build(), merge(favoriteList)).subscribe(this);
    }

    private static final String TAG = PresenterFavorite.class.getSimpleName();

    @Override
    protected void render(@NonNull ViewStateFavorite state, @NonNull ViewFavorite view) {
        view.renderError(state.errorFavorites());
        view.updateFavorites(Arrays.asList(state.favorites().values().toArray(new Item[state.favorites().size()])));
    }
}
