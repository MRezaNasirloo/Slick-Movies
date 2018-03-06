package com.github.pedramrn.slick.parent.ui.videos;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavoriteProgressive;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterMovieImpl;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieVideosImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.github.pedramrn.slick.parent.ui.videos.state.Error;
import com.github.pedramrn.slick.parent.ui.videos.state.ErrorDismissed;
import com.github.pedramrn.slick.parent.ui.videos.state.Header;
import com.github.pedramrn.slick.parent.ui.videos.state.HeaderProgressive;
import com.github.pedramrn.slick.parent.ui.videos.state.Videos;
import com.github.pedramrn.slick.parent.ui.videos.state.VideosProgressive;
import com.github.pedramrn.slick.parent.ui.videos.state.ViewStateVideos;
import com.github.pedramrn.slick.parent.util.ScanList;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A simple Presenter
 */
class PresenterVideos extends PresenterBase<ViewVideos, ViewStateVideos> {

    private final RouterMovieVideosImpl routerMovieVideos;
    private final RouterMovieImpl routerMovie;
    private final MapperMovieDomainMovie mapper;
    private final String VIDEOS = "VIDEOS";

    @Inject
    PresenterVideos(RouterMovieVideosImpl routerMovieVideos,
                    RouterMovieImpl routerMovie,
                    MapperMovieDomainMovie mapper,
                    @Named("io") Scheduler io,
                    @Named("main") Scheduler main) {
        super(main, io);

        this.routerMovieVideos = routerMovieVideos;
        this.routerMovie = routerMovie;
        this.mapper = mapper;
    }

    @Override
    public void start(ViewVideos view) {
        Integer id = view.movie().id();

        Observable<PartialViewState<ViewStateVideos>> header = command(ViewVideos::onRetry).startWith(1)
                .flatMap(o -> routerMovie.movie(id).subscribeOn(io)
                        .map(mapper)
                        .cast(ItemView.class)
                        .map(itemView -> itemView.render(Movie.FAVORITE))
                        .map((Function<Item, PartialViewState<ViewStateVideos>>) Header::new)
                        .startWith(new HeaderProgressive())
                        .onErrorReturn(Error::new)
                );


        Observable<PartialViewState<ViewStateVideos>> videos = command(ViewVideos::onRetry).startWith(1)
                .flatMap(o -> routerMovieVideos.get(id)
                        .concatMap(Observable::fromIterable)
                        .map(vd -> Video.create(vd.key().hashCode(), vd.tmdb(), vd.type(), vd.key(), vd.name()))
                        .sorted((o1, o2) -> o2.type().compareTo(o1.type()))
                        .map(new MapProgressive())
                        .cast(ItemView.class)
                        .map(itemView -> itemView.render(VIDEOS))
                        .buffer(20)
                        .compose(new ScanList<>())
                        .map((Function<List<Item>, PartialViewState<ViewStateVideos>>) Videos::new)
                        .startWith(new VideosProgressive(2, VIDEOS))
                        .onErrorReturn(Error::new)
                        .subscribeOn(io));

        Observable<PartialViewState<ViewStateVideos>> errorDismissed =
                command(ViewVideos::onErrorDismissed).map(o -> new ErrorDismissed());


        ViewStateVideos initialState = ViewStateVideos.builder()
                .videos(Collections.emptyList())
                .header(new ItemFavoriteProgressive(0))
                .build();

        reduce(initialState, merge(videos, header, errorDismissed)).subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStateVideos state, @NonNull ViewVideos view) {
        ArrayList<Item> items = new ArrayList<>(state.videos().size() + 1);
        items.add(state.header());
        items.addAll(state.videos());
        view.update(items);

        if (state.error() != null) view.error(ErrorHandler.handle(state.error()));
    }
}
