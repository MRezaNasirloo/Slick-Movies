package com.github.pedramrn.slick.parent.ui.details.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MoviePageTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterSimilar;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

public class RouterSimilarImpl implements RouterSimilar {

    private final ApiTmdb apiTmdb;
    private final MapperMovieSmall mapper;

    @Inject
    public RouterSimilarImpl(ApiTmdb apiTmdb, MapperMovieSmall mapper) {
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<MovieSmallDomain>> similar(int id, int page) {
        return apiTmdb.similar(id, page).map(new Function<MoviePageTmdb, List<MovieSmallDomain>>() {
            @Override
            public List<MovieSmallDomain> apply(@NonNull MoviePageTmdb moviePageTmdb) throws Exception {
                return Observable.fromIterable(moviePageTmdb.movies()).map(mapper).toList().blockingGet();
            }
        });
    }
}
