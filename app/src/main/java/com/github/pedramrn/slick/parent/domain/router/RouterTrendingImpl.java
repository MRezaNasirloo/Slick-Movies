package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.TraktPageMetadata;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-23
 */

public class RouterTrendingImpl implements RouterTrending {

    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;
    private TransformerTraktToMovieDomain transformer = new TransformerTraktToMovieDomain();

    @Inject
    public RouterTrendingImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<MovieDomain> trending() {
        return apiTrakt.trending(1, 10).compose(transformer);
    }

    @Override
    public Observable<MovieDomain> trending(int page, int size) {
        return apiTrakt.trending(page, size).compose(transformer);
    }

    private class TransformerTraktToMovieDomain implements ObservableTransformer<List<TraktPageMetadata>, MovieDomain> {
        @Override
        public ObservableSource<MovieDomain> apply(Observable<List<TraktPageMetadata>> upstream) {
            return upstream.flatMap(new Function<List<TraktPageMetadata>, ObservableSource<MovieTmdb>>() {
                @Override
                public ObservableSource<MovieTmdb> apply(@NonNull List<TraktPageMetadata> tpm) throws Exception {
                    return Observable.fromIterable(tpm).concatMap(new Function<TraktPageMetadata, Observable<MovieTmdb>>() {
                        @Override
                        public Observable<MovieTmdb> apply(@NonNull TraktPageMetadata tpm) throws Exception {
                            return apiTmdb.movie(tpm.movie().ids().tmdb());
                        }
                    });
                }
            }).map(mapper);
        }
    }
}
