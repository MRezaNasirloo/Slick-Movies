package com.github.pedramrn.slick.parent.ui.videos;

import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterMovieVideosImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.github.pedramrn.slick.parent.ui.videos.state.PartialViewStateVideos;
import com.github.pedramrn.slick.parent.ui.videos.state.ViewStateVideos;
import com.github.pedramrn.slick.parent.util.IdBank;
import com.github.pedramrn.slick.parent.util.ScanToList;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
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

    void get(Integer id) {
        if (!hasSubscribed()) {
            IdBank.reset(VIDEOS);
            Observable<PartialViewState<ViewStateVideos>> videos = routerMovieVideos.get(id)
                    .concatMap(new Function<List<VideoDomain>, ObservableSource<VideoDomain>>() {
                        @Override
                        public ObservableSource<VideoDomain> apply(@NonNull List<VideoDomain> videoDomains) throws Exception {
                            return Observable.fromIterable(videoDomains);
                        }
                    })
                    .map(new Function<VideoDomain, Video>() {
                        @Override
                        public Video apply(@NonNull VideoDomain vd) throws Exception {
                            return Video.create(vd.key().hashCode(), vd.tmdb(), vd.type(), vd.key(), vd.name());
                        }
                    })
                    .sorted(new Comparator<Video>() {
                        @Override
                        public int compare(Video o1, Video o2) {
                            return o2.type().compareTo(o1.type());
                        }
                    })
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemView) throws Exception {
                            return itemView.render(VIDEOS);
                        }
                    })
                    .compose(new ScanToList<Item>())
                    .map(new Function<List<Item>, PartialViewState<ViewStateVideos>>() {
                        @Override
                        public PartialViewState<ViewStateVideos> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateVideos.Videos(items);
                        }
                    })
                    .startWith(new PartialViewStateVideos.VideosProgressive(1, VIDEOS))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateVideos>>() {
                        @Override
                        public PartialViewState<ViewStateVideos> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateVideos.VideosError(throwable);
                        }
                    })
                    .subscribeOn(io);

            Observable<PartialViewState<ViewStateVideos>> observable = Observable.never();

            ViewStateVideos initialState = ViewStateVideos.builder()
                    .videos(Collections.<Item>emptyList())
                    .build();

            reduce(initialState, merge(videos, observable)).subscribe(this);
        }
    }

    @Override
    public void onDestroy() {
        IdBank.dispose(VIDEOS);
        super.onDestroy();
    }
}
