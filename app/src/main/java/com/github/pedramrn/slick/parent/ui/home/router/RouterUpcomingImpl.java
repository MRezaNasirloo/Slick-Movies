package com.github.pedramrn.slick.parent.ui.home.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MoviePageTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterUpcoming;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-18
 */

public class RouterUpcomingImpl implements RouterUpcoming {

    private final ApiTmdb apiTmdb;
    private final MapperMovieSmall mapper;

    @Inject
    public RouterUpcomingImpl(ApiTmdb apiTmdb, MapperMovieSmall mapper) {
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<PagedDomain<MovieSmallDomain>> upcoming(int page) {
        return apiTmdb.upcoming(page).flatMap(new Function<MoviePageTmdb, ObservableSource<PagedDomain<MovieSmallDomain>>>() {
            @Override
            public ObservableSource<PagedDomain<MovieSmallDomain>> apply(@NonNull final MoviePageTmdb mpt) throws Exception {
                return Observable.fromIterable(mpt.movies())
                        .map(mapper)
                        .buffer(20)
                        .map(new Function<List<MovieSmallDomain>, PagedDomain<MovieSmallDomain>>() {
                            @Override
                            public PagedDomain<MovieSmallDomain> apply(@NonNull List<MovieSmallDomain> movieSmallDomains) throws Exception {
                                return PagedDomain.create(movieSmallDomains, mpt.page(), mpt.totalPages(), mpt.totalResults());
                            }
                        });
            }
        });
    }
}
