package com.github.pedramrn.slick.parent.ui.favorite.router;

import com.github.pedramrn.slick.parent.datasource.database.model.ItemFavoriteModel;
import com.github.pedramrn.slick.parent.datasource.database.repository.RepositoryFavoriteImpl;
import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterFavorite;
import com.github.pedramrn.slick.parent.ui.middleware.MiddlewareLogin;
import com.github.slick.Middleware;
import com.github.slick.middleware.BundleSlick;

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

    @Inject
    public RouterFavoriteImpl(RepositoryFavoriteImpl repositoryFavorite) {
        this.repositoryFavorite = repositoryFavorite;
    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<Object> add(BundleSlick bundle) {
        return repositoryFavorite.add(bundle.getSting("uid", null), new ItemFavoriteModel(bundle.getObject("fav", null, FavoriteDomain.class)));
    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<Object> remove(BundleSlick bundle) {
        return repositoryFavorite.remove(bundle.getSting("uid", null), new ItemFavoriteModel(bundle.getObject("fav", null, FavoriteDomain.class)));
    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<Boolean> updateStream(BundleSlick bundle) {
        return repositoryFavorite.updateStream(bundle.getSting("uid", null), bundle.getInteger("tmdb_id", null));
    }

    @Override
    @Middleware(MiddlewareLogin.class)
    public Observable<List<FavoriteDomain>> updateStream(String uid) {
        return repositoryFavorite.updateStream(uid)
                .map(source -> Observable.fromIterable(source).map(new MapperFavoriteModelToDomain())
                        .toList(source.size() > 0 ? source.size() : 1).blockingGet()).distinctUntilChanged();
    }

    private static class MapperFavoriteModelToDomain implements Function<ItemFavoriteModel, FavoriteDomain> {
        @Override
        public FavoriteDomain apply(ItemFavoriteModel ifm) throws Exception {
            return FavoriteDomain.create(ifm.imdbId, ifm.tmdb, ifm.name, ifm.type);
        }
    }
}
