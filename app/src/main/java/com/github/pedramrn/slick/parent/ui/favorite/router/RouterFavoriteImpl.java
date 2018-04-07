package com.github.pedramrn.slick.parent.ui.favorite.router;

import com.github.pedramrn.slick.parent.datasource.database.model.ItemFavoriteModel;
import com.github.pedramrn.slick.parent.datasource.database.repository.RepositoryFavoriteImpl;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuth;
import com.github.pedramrn.slick.parent.datasource.network.repository.RepositoryAuthImpl;
import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterFavorite;
import com.github.pedramrn.slick.parent.ui.middleware.MiddlewareLogin;
import com.mrezanasirloo.slick.Middleware;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-09
 */

public class RouterFavoriteImpl implements RouterFavorite {

    private final RepositoryFavoriteImpl repositoryFavorite;
    private final RepositoryAuth repositoryAuth;

    @Inject
    public RouterFavoriteImpl(RepositoryFavoriteImpl repositoryFavorite, RepositoryAuthImpl repositoryAuth) {
        this.repositoryFavorite = repositoryFavorite;
        this.repositoryAuth = repositoryAuth;
    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<Object> add(FavoriteDomain favorite) {
        return repositoryAuth.currentUser()
                .flatMap(firebaseUser -> repositoryFavorite.add(firebaseUser.getUid(),
                        new ItemFavoriteModel(favorite)));
    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<Object> remove(FavoriteDomain favorite) {
        return repositoryAuth.currentUser()
                .flatMap(firebaseUser -> repositoryFavorite.remove(firebaseUser.getUid(),
                        new ItemFavoriteModel(favorite)));

    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<Boolean> updateStream(Integer tmdbId) {
        return repositoryAuth.currentUser()
                .flatMap(firebaseUser -> repositoryFavorite.updateStream(firebaseUser.getUid(), tmdbId));

    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<List<FavoriteDomain>> updateStream() {
        return repositoryAuth.currentUser().flatMap(firebaseUser -> repositoryFavorite.updateStream(firebaseUser.getUid())
                .map(source -> Observable.fromIterable(source)
                        .sorted((o1, o2) -> o2.dateAdded.compareTo(o1.dateAdded)).map(new MapperFavoriteModelToDomain())
                        .toList(source.size() > 0 ? source.size() : 1).blockingGet()).distinctUntilChanged());
    }

    private static class MapperFavoriteModelToDomain implements Function<ItemFavoriteModel, FavoriteDomain> {
        @Override
        public FavoriteDomain apply(ItemFavoriteModel ifm) throws Exception {
            return FavoriteDomain.create(ifm.imdbId, ifm.tmdb, ifm.name, ifm.type);
        }
    }
}
