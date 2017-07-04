package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.util.ListToObserable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */
public class RouterPopularImpl implements RouterPopular {

    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final Transformer transformer = new Transformer();
    private final MapperMovie mapper;

    @Inject
    public RouterPopularImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<MovieDomain> popular() {
        return apiTrakt.popular(1, 10).compose(transformer);
    }

    @Override
    public Observable<MovieDomain> popular(int page, int size) {
        return apiTrakt.popular(page, size).compose(transformer);
    }

    private class Transformer implements ObservableTransformer<List<MovieTraktMetadata>, MovieDomain> {
        @Override
        public ObservableSource<MovieDomain> apply(Observable<List<MovieTraktMetadata>> upstream) {
            return upstream.concatMap(new ListToObserable<MovieTraktMetadata>())
                    .concatMap(new Function<MovieTraktMetadata, ObservableSource<? extends MovieDomain>>() {
                        @Override
                        public ObservableSource<? extends MovieDomain> apply(@NonNull MovieTraktMetadata movieTraktMetadata) throws Exception {
                            return apiTmdb.movie(movieTraktMetadata.ids().tmdb()).map(mapper);
                        }
                    });
        }
    }
}