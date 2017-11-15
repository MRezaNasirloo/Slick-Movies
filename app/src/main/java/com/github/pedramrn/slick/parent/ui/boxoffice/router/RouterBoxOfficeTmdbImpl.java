package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;

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

    @Inject
    public RouterBoxOfficeTmdbImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper, @Named("io") Scheduler io) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
        this.io = io;
    }

    @Override
    public Observable<MovieDomain> boxOffice(Observable<Integer> trigger, int buffer) {
        return apiTrakt.get()
                .flatMap(Observable::fromIterable)
                .buffer(buffer)
                .zipWith(trigger, (boxOfficeItem, ignored) -> boxOfficeItem)
                .concatMap(boxOfficeItem -> Observable.fromIterable(boxOfficeItem)
                        .concatMap(boxOfficeItem1 -> apiTmdb.movie(boxOfficeItem1.movie().ids().tmdb())
                                .subscribeOn(io)
                                .map(mapper)));
    }
}
