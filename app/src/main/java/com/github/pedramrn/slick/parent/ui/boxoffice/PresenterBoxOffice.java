package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeTmdbImpl;
import com.github.pedramrn.slick.parent.ui.boxoffice.state.ViewStateBoxOffice;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */
public class PresenterBoxOffice extends PresenterBase<ViewBoxOffice, ViewStateBoxOffice> {

    private final RouterBoxOffice routerBoxOffice;
    private final MapperMovieDomainMovie mapper;
    private final Scheduler main;
    private final Scheduler io;


    @Inject
    public PresenterBoxOffice(
            RouterBoxOfficeTmdbImpl routerBoxOffice,
            MapperMovieDomainMovie mapper,
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);
        this.routerBoxOffice = routerBoxOffice;
        this.mapper = mapper;
        this.main = main;
        this.io = io;
    }

    private Observable<List<Movie>> boxOffice(Observable<Integer> trigger, int pageSize) {
        return routerBoxOffice.boxOffice(trigger, pageSize)
                .map(mapper)
                .map(new Function<Movie, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@NonNull Movie movieBoxOfficeItem) throws Exception {
                        final ArrayList<Movie> list = new ArrayList<>(1);
                        list.add(movieBoxOfficeItem);
                        return list;
                    }
                }).scan(new BiFunction<List<Movie>, List<Movie>, List<Movie>>() {
                    @Override
                    public List<Movie> apply(@NonNull List<Movie> boxOfficeList, @NonNull List<Movie> boxOfficeList2)
                            throws Exception {
                        boxOfficeList.addAll(boxOfficeList2);
                        return boxOfficeList;
                    }
                }).subscribeOn(io)
                .observeOn(main);
    }

    @Override
    protected void start(ViewBoxOffice view) {
        view.pageSize();
        Observable<Integer> commandLoadMore = command(ViewBoxOffice::onLoadMore);
        Observable<Object> commandRetry = command(ViewBoxOffice::onRetry);

        commandRetry.flatMap(o -> routerBoxOffice.boxOffice(Observable.just(1), 10)
                        .map(mapper)
                        .map(movie -> movie.render("BOX_OFFICE"))
                // TODO: 2017-11-16 Use PassTroughMap for speeding things up
        );

        reduce(ViewStateBoxOffice.builder().movieItems(Collections.emptyList()).build(),
                Observable.never()).subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStateBoxOffice state, @NonNull ViewBoxOffice view) {
        super.render(state, view);
    }
}
