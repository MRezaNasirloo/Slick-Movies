package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.domain.model.CommentDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterComments;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterSimilar;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperCommentDomainComment;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.router.RouterCommentsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterSimilarImpl;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.util.IdBank;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class PresenterDetails extends PresenterBase<ViewDetails, ViewStateDetails> {
    private static final String TAG = PresenterDetails.class.getSimpleName();
    private final RouterMovieDetails routerMovieDetails;
    private final RouterSimilar routerSimilar;
    private final RouterComments routerComments;
    private final MapperCommentDomainComment mapperComment;
    private final MapperMovieDomainMovie mapper;
    private final MapperMovieSmallDomainMovieSmall mapperSmall;
    private final Scheduler io;
    private final Scheduler main;
    private final String CASTS = "CASTS";
    private final String SIMILAR = "SIMILAR";
    private final String BACKDROPS = "BACKDROPS";
    private final String COMMENTS = "COMMENTS";

    @Inject
    public PresenterDetails(RouterMovieDetailsImpl routerMovieDetails,
                            RouterSimilarImpl routerSimilar,
                            RouterCommentsImpl routerComments,
                            MapperCommentDomainComment mapperComment,
                            MapperMovieDomainMovie mapper,
                            MapperMovieSmallDomainMovieSmall mapperSmall,
                            @Named("io") Scheduler io,
                            @Named("main") Scheduler main
    ) {
        super(main, io);
        this.routerMovieDetails = routerMovieDetails;
        this.routerSimilar = routerSimilar;
        this.routerComments = routerComments;
        this.mapperComment = mapperComment;
        this.mapper = mapper;
        this.mapperSmall = mapperSmall;
        this.io = io;
        this.main = main;
    }

    void getMovieDetails(MovieBasic movieBasic) {
        if (!hasSubscribed()) {
            IdBank.reset(SIMILAR);
            IdBank.reset(CASTS);
            IdBank.reset(BACKDROPS);
            IdBank.reset(COMMENTS);

            Observable<Movie> movieFull = routerMovieDetails.get(movieBasic.id())
                    //Maps the domains Models to View Models which have android dependency
                    .map(mapper)
                    .subscribeOn(io)
                    .share();

            Observable<PartialViewState<ViewStateDetails>> movie = movieFull
                    .map(new Function<Movie, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Movie movie) throws Exception {
                            return new PartialViewStateDetails.MovieFull(movie);
                        }
                    })
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorMovieFull(throwable);
                        }
                    });

            Observable<PartialViewState<ViewStateDetails>> backdrops = movieFull
                    .take(1)

                    .concatMap(new Function<Movie, ObservableSource<Backdrop>>() {
                        @Override
                        public ObservableSource<Backdrop> apply(@NonNull Movie movie) throws Exception {
                            return Observable.fromIterable(movie.images().backdrops());
                        }
                    })
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemView) throws Exception {
                            return itemView.render(BACKDROPS);
                        }
                    })
                    .buffer(200)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateDetails.MovieBackdrops(items);
                        }
                    })
                    .startWith(new PartialViewStateDetails.MovieBackdropsProgressive(5, BACKDROPS))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorMovieBackdrop(throwable);
                        }
                    });

            Observable<PartialViewState<ViewStateDetails>> casts = movieFull
                    .take(1)
                    .concatMap(new Function<Movie, ObservableSource<Cast>>() {
                        @Override
                        public ObservableSource<Cast> apply(@NonNull Movie movie) throws Exception {
                            return Observable.fromIterable(movie.casts()).take(6);
                        }
                    })
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemView) throws Exception {
                            return itemView.render(CASTS);
                        }
                    })
                    .buffer(20)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateDetails.MovieCast(items);
                        }
                    })
                    .startWith(new PartialViewStateDetails.MovieCastsProgressive(5, CASTS))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorMovieCast(throwable);
                        }
                    });

            Observable<PartialViewState<ViewStateDetails>> comments = movieFull.take(1)
                    .flatMap(new Function<Movie, ObservableSource<PagedDomain<CommentDomain>>>() {
                        @Override
                        public ObservableSource<PagedDomain<CommentDomain>> apply(@NonNull Movie movie) throws Exception {
                            return routerComments.comments(movie.imdbId(), 1, 2);
                        }
                    })
                    .concatMap(new Function<PagedDomain<CommentDomain>, ObservableSource<CommentDomain>>() {
                        @Override
                        public ObservableSource<CommentDomain> apply(@NonNull PagedDomain<CommentDomain> comments) throws Exception {
                            return Observable.fromIterable(comments.data());
                        }
                    })
                    .take(2)
                    .map(mapperComment)
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemView) throws Exception {
                            return itemView.render(COMMENTS);
                        }
                    })
                    .buffer(2)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> items) throws Exception {
                            return new PartialViewStateDetails.Comments(items);
                        }
                    })
                    .startWith(new PartialViewStateDetails.CommentsProgressive(2, COMMENTS))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorComments(throwable);
                        }
                    })
                    .subscribeOn(io);


            Observable<PartialViewState<ViewStateDetails>> similar = routerSimilar.similar(movieBasic.id(), 1)
                    .concatMap(new Function<List<MovieSmallDomain>, ObservableSource<MovieSmallDomain>>() {
                        @Override
                        public ObservableSource<MovieSmallDomain> apply(@NonNull List<MovieSmallDomain> movieSmallDomains) throws Exception {
                            return Observable.fromIterable(movieSmallDomains);
                        }
                    })
                    .map(mapperSmall)
                    .map(new MapProgressive())
                    .cast(ItemView.class)
                    .map(new Function<ItemView, Item>() {
                        @Override
                        public Item apply(@NonNull ItemView itemCard) throws Exception {
                            return itemCard.render(SIMILAR);
                        }
                    })
                    .buffer(20)
                    .map(new Function<List<Item>, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull List<Item> movies) throws Exception {
                            return new PartialViewStateDetails.Similar(movies);
                        }
                    })
                    .startWith(new PartialViewStateDetails.SimilarProgressive(5, SIMILAR))
                    .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateDetails>>() {
                        @Override
                        public PartialViewState<ViewStateDetails> apply(@NonNull Throwable throwable) throws Exception {
                            return new PartialViewStateDetails.ErrorSimilar(throwable);
                        }
                    })
                    .subscribeOn(io);

            ViewStateDetails initial = ViewStateDetails.builder()
                    .casts(Collections.<Item>emptyList())
                    .backdrops(Collections.<Item>emptyList())
                    .similar(Collections.<Item>emptyList())
                    .comments(Collections.<Item>emptyList())
                    .movieBasic(movieBasic)
                    .build();

            reduce(initial, merge(movie, casts, backdrops, similar, comments)).subscribe(this);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IdBank.dispose(SIMILAR);
        IdBank.dispose(CASTS);
        IdBank.dispose(BACKDROPS);
        IdBank.dispose(COMMENTS);
    }

}
