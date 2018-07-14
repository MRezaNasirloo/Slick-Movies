package com.github.pedramrn.slick.parent.ui.videos;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterMovieImpl;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieVideosImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.github.pedramrn.slick.parent.ui.videos.state.Error;
import com.github.pedramrn.slick.parent.ui.videos.state.ErrorDismissed;
import com.github.pedramrn.slick.parent.ui.videos.state.Videos;
import com.github.pedramrn.slick.parent.ui.videos.state.VideosItem;
import com.github.pedramrn.slick.parent.ui.videos.state.VideosProgressive;
import com.github.pedramrn.slick.parent.ui.videos.state.ViewStateVideos;
import com.github.pedramrn.slick.parent.util.ScanList;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
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
public class PresenterVideos extends SlickPresenterUni<ViewVideos, ViewStateVideos> {

    private final RouterMovieVideosImpl routerMovieVideos;
    private final RouterMovieImpl routerMovie;
    private final MapperMovieDomainMovie mapper;
    public static final String VIDEO_IN_VIDEOS_PAGE = "VIDEO_IN_VIDEOS_PAGE";
    public static final String VIDEO_IN_DETAILS_PAGE = "VIDEO_IN_DETAILS_PAGE";


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
    public void start(@NonNull ViewVideos view) {
        Integer id = view.movie().id();
        String viewTypes = view.viewType();

        Observable<Object> trigger = command(ViewVideos::onRetry).share().startWith(1);
        Observable<List<Video>> videos = trigger.flatMap(o -> routerMovieVideos.get(id)
                .concatMap(videoDomains -> Observable.fromIterable(videoDomains)
                        .map(vd -> Video.create(vd.key().hashCode(), vd.tmdb(), vd.type(), vd.key(), vd.name()))
                        .sorted((o1, o2) -> o2.type().compareTo(o1.type()))
                        .map(new MapProgressive())
                        .cast(Video.class)
                        .buffer(20)
                        .compose(new ScanList<>())
                ))
                .share()
                // .publish()
                // .autoConnect()
                // .replay(1)
                ;


        Observable<PartialViewState<ViewStateVideos>> videosSate = trigger.flatMap(o -> videos
                .map((Function<List<Video>, PartialViewState<ViewStateVideos>>) Videos::new)
                .onErrorReturn(Error::new)
        ).onErrorReturn(Error::new);


        // TODO: 2018-07-14 it's possible to change the view type at runtime
        Observable<PartialViewState<ViewStateVideos>> videosItem = trigger.flatMap(o -> videos
                .flatMap(source -> Observable.fromIterable(source)
                        .cast(ItemView.class)
                        .map(itemView -> itemView.render(viewTypes))
                        .buffer(20)
                        .compose(new ScanList<>())
                        .map((Function<List<Item>, PartialViewState<ViewStateVideos>>) VideosItem::new)
                        .startWith(new VideosProgressive(2, viewTypes))
                        .onErrorReturn(Error::new))
        ).onErrorReturn(Error::new);

        Observable<PartialViewState<ViewStateVideos>> errorDismissed =
                command(ViewVideos::errorDismissed).map(o -> new ErrorDismissed());


        ViewStateVideos initialState = ViewStateVideos.builder()
                .videos(Collections.emptyList())
                .videosItem(Collections.emptyList())
                .build();

        subscribe(initialState, merge(videosItem, videosSate, errorDismissed));
    }

    @Override
    protected void render(@NonNull ViewStateVideos state, @NonNull ViewVideos view) {
        view.update(state.videosItem());
        if (state.error() != null) view.error(ErrorHandler.handle(state.error()));
    }
}
