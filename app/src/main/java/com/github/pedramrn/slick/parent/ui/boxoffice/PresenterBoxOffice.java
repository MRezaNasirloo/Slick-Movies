package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.boxoffice.state.ErrorBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.state.MoviesBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.state.ViewStateBoxOffice;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.home.MapperMovieMetadataToMovieBasic;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.util.ScanToMap;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private final MapperMovieMetadataToMovieBasic mapperMetadata;


    @Inject
    public PresenterBoxOffice(
            RouterBoxOffice routerBoxOffice,
            MapperMovieMetadataToMovieBasic mapperMetadata,
            MapperMovieDomainMovie mapper,
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);
        this.routerBoxOffice = routerBoxOffice;
        this.mapperMetadata = mapperMetadata;
        this.mapper = mapper;
    }

    private Observable<List<Movie>> boxOffice(Observable<Integer> trigger, int pageSize) {
        return routerBoxOffice.boxOfficePagination(trigger, pageSize)
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
        Observable<Object> commandRetry = command(ViewBoxOffice::onRetry);

        Observable<PartialViewState<ViewStateBoxOffice>> boxOffice =
                commandRetry.startWith(1).flatMap(o -> routerBoxOffice.boxOffice().subscribeOn(io)
                        .map(mapperMetadata)
                        .cast(AutoBase.class)
                        .map(new MapProgressive())
                        .cast(ItemView.class)
                        .map(itemView -> itemView.render(MovieSmall.BOX_OFFICE))
                        .compose(new ScanToMap<>())
                        .map((Function<Map<Integer, Item>, PartialViewState<ViewStateBoxOffice>>) MoviesBoxOffice::new)
                        .startWith(new MoviesBoxOffice(Collections.emptyMap()))
                        .onErrorReturn(ErrorBoxOffice::new)
                );

        reduce(ViewStateBoxOffice.builder().movies(Collections.emptyMap()).build(), boxOffice).subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStateBoxOffice state, @NonNull ViewBoxOffice view) {
        view.update(Arrays.asList(state.movies().values().toArray(new Item[state.movies().size()])));
    }
}
