package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
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
 *         Created on: 2017-07-02
 */
public class TransformerTraktToMovieDomain implements ObservableTransformer<List<MovieTraktPageMetadata>, MovieDomain> {
    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;

    @Inject
    public TransformerTraktToMovieDomain(ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public ObservableSource<MovieDomain> apply(Observable<List<MovieTraktPageMetadata>> upstream) {
        return upstream.flatMap(new Function<List<MovieTraktPageMetadata>, ObservableSource<MovieTmdb>>() {
            @Override
            public ObservableSource<MovieTmdb> apply(@NonNull List<MovieTraktPageMetadata> tpm) throws Exception {
                return Observable.fromIterable(tpm).concatMap(new Function<MovieTraktPageMetadata, Observable<MovieTmdb>>() {
                    @Override
                    public Observable<MovieTmdb> apply(@NonNull MovieTraktPageMetadata tpm) throws Exception {
                        return apiTmdb.movie(tpm.movie().ids().tmdb());
                    }
                });
            }
        }).map(mapper);
    }
}
