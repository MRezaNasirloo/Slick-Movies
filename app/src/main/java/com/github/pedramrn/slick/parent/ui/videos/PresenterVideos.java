package com.github.pedramrn.slick.parent.ui.videos;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieVideosImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.github.pedramrn.slick.parent.ui.videos.state.PartialViewStateVideos;
import com.github.pedramrn.slick.parent.ui.videos.state.ViewStateVideos;
import com.github.pedramrn.slick.parent.util.ScanList;
import com.xwray.groupie.Item;

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
    private final String VIDEOS = "VIDEOS";

    @Inject
    PresenterVideos(RouterMovieVideosImpl routerMovieVideos,
                    @Named("io") Scheduler io,
                    @Named("main") Scheduler main) {
        super(main, io);

        this.routerMovieVideos = routerMovieVideos;
    }

    @Override
    public void start(ViewVideos view) {
        Integer id = view.movie().id();

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
                        .map((Function<List<Item>, PartialViewState<ViewStateVideos>>) PartialViewStateVideos.Videos::new)
                        .startWith(new PartialViewStateVideos.VideosProgressive(2, VIDEOS))
                        .onErrorReturn(PartialViewStateVideos.Error::new)
                        .subscribeOn(io));

        Observable<PartialViewState<ViewStateVideos>> errorDismissed =
                command(ViewVideos::onErrorDismissed).map(o -> new PartialViewStateVideos.ErrorDismissed());


        Observable<PartialViewState<ViewStateVideos>> observable = Observable.never();

        ViewStateVideos initialState = ViewStateVideos.builder()
                .videos(Collections.emptyList())
                .build();

        reduce(initialState, merge(videos, observable, errorDismissed)).subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStateVideos state, @NonNull ViewVideos view) {
        view.update(state.videos());

        if (state.error() != null) view.error(ErrorHandler.handle(state.error()));
    }
}
