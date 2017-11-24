package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.domain.rx.PassThroughMap;
import com.github.pedramrn.slick.parent.ui.boxoffice.mapper.MapperBoxOfficeItemMovieMetadata;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-10
 */

public class RouterBoxOfficeTmdbImpl implements RouterBoxOffice {

    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;
    private final Scheduler io;
    private static final String TAG = RouterBoxOfficeTmdbImpl.class.getSimpleName();
    private MapperBoxOfficeItemMovieMetadata mapperBoxOfficeItem = new MapperBoxOfficeItemMovieMetadata();

    @Inject
    public RouterBoxOfficeTmdbImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper, @Named("io") Scheduler io) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
        this.io = io;
    }

    @Override
    public Observable<MovieDomain> boxOfficePagination(Observable<Integer> trigger, int buffer) {
        return apiTrakt.boxOffice()
                .flatMap(Observable::fromIterable)
                .buffer(buffer)
                .zipWith(trigger, (boxOfficeItem, ignored) -> boxOfficeItem)
                .concatMap(boxOfficeItem -> Observable.fromIterable(boxOfficeItem)
                        .concatMap(boxOfficeItem1 -> apiTmdb.movie(boxOfficeItem1.movie().ids().tmdb())
                                .subscribeOn(io)
                                .map(mapper)));
    }

    @Override
    public Observable<MovieMetadata> boxOffice() {
        return apiTrakt.boxOffice()
                .flatMap(Observable::fromIterable)
                .map(mapperBoxOfficeItem)
                .lift(new PassThroughMap<MovieMetadata>(){
                    @Override
                    public Observable<MovieMetadata> apply(MovieMetadata movieMetadata) throws Exception {
                        return apiTmdb.movie(movieMetadata.id()).map(mapper).cast(MovieMetadata.class);
                    }
                });

    }

}
