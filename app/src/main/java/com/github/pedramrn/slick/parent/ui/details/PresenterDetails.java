package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.router.RouterComments;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterSimilar;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperCommentDomainComment;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.router.RouterCommentsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsIMDBImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterSimilarImpl;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
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

import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.Comments;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.CommentsLoaded;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.CommentsProgressive;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.ErrorDismissed;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.MovieBackdrops;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.MovieBackdropsProgressive;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.MovieCast;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.MovieCastsProgressive;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.MovieFull;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.Similar;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.SimilarLoaded;
import static com.github.pedramrn.slick.parent.ui.details.PartialViewStateDetails.SimilarProgressive;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-15
 */

public class PresenterDetails extends SlickPresenterUni<ViewDetails, ViewStateDetails> {
    private static final String TAG = PresenterDetails.class.getSimpleName();

    private RouterMovieDetails routerMovieDetails;
    private final RouterMovieDetails routerMovieDetailsIMDB;
    private final RouterSimilar routerSimilar;
    private final RouterComments routerComments;

    private final MapperMovieSmallDomainMovieSmall mapperSmall;
    private final MapperCommentDomainComment mapperComment;
    private final MapperMovieDomainMovie mapper;

    private final String CASTS = "CASTS";
    private final String SIMILAR = "SIMILAR";
    private final String BACKDROPS = "BACKDROPS";
    private final String COMMENTS = "COMMENTS";
    private MovieBasic movieBasic;

    @Inject
    public PresenterDetails(
            RouterMovieDetailsImpl routerMovieDetailsTMDB,
            RouterMovieDetailsIMDBImpl routerMovieDetailsIMDB,
            RouterSimilarImpl routerSimilar,
            RouterCommentsImpl routerComments,
            MapperCommentDomainComment mapperComment,
            MapperMovieDomainMovie mapper,
            MapperMovieSmallDomainMovieSmall mapperSmall,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main
    ) {
        super(main, io);
        this.routerMovieDetailsIMDB = routerMovieDetailsIMDB;
        this.routerMovieDetails = routerMovieDetailsTMDB;
        this.routerSimilar = routerSimilar;
        this.routerComments = routerComments;
        this.mapperComment = mapperComment;
        this.mapper = mapper;
        this.mapperSmall = mapperSmall;
    }


    @Override
    protected void start(@NonNull ViewDetails view) {
        movieBasic = view.getMovie();
        final String movieId;
        if (movieBasic.id() == -1) {
            movieId = movieBasic.imdbId();
            routerMovieDetails = this.routerMovieDetailsIMDB;
        } else {
            movieId = movieBasic.id().toString();
        }

        if (movieId == null || movieId.equals("-1")) {
            throw new IllegalStateException("movie id cannot be null: " + movieBasic.toString());
        }

        Observable<Object> triggerRetry = command(ViewDetails::onRetryAll).share().startWith(1);

        final Observable<Movie> movieFull = triggerRetry.flatMap(o -> routerMovieDetails.get(movieId).subscribeOn(io)
                //Maps the domains Models to View Models which have android dependency
                .map(mapper)
        ).share();

        Observable<PartialViewState<ViewStateDetails>> film = triggerRetry.flatMap(o -> movieFull
                .map((Function<Movie, PartialViewState<ViewStateDetails>>) MovieFull::new)
                .onErrorReturn(PartialViewStateDetails.Error::new));

        Observable<PartialViewState<ViewStateDetails>> backdrops = triggerRetry.flatMap(o -> movieFull
                .filter(movie -> movie.images() != null && !movie.images().backdrops().isEmpty())
                .take(1)
                .concatMap(movie -> Observable.fromIterable(movie.images().backdrops()))
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemView -> itemView.render(BACKDROPS))
                .buffer(100)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) MovieBackdrops::new)
                .startWith(new MovieBackdropsProgressive(5, BACKDROPS))
                .onErrorReturn(PartialViewStateDetails.Error::new));

        Observable<PartialViewState<ViewStateDetails>> casts = triggerRetry.flatMap(o -> movieFull
                .filter(movie -> movie.casts() != null && !movie.casts().isEmpty())
                .take(1)
                .concatMap(movie -> Observable.fromIterable(movie.casts()).take(6))
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemView -> itemView.render(CASTS))
                .buffer(20)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) MovieCast::new)
                .startWith(new MovieCastsProgressive(6, CASTS))
                .onErrorReturn(PartialViewStateDetails.Error::new));


        Observable<PartialViewState<ViewStateDetails>> comments = triggerRetry.flatMap(o -> movieFull
                .filter(movie -> movie.imdbId() != null)
                .take(1)
                .flatMap(movie -> routerComments.comments(movie.imdbId(), 1, 15).subscribeOn(io))
                .concatMap(comments1 -> Observable.fromIterable(comments1.data()))
                .map(mapperComment)
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemView -> itemView.render(COMMENTS))
                .buffer(20)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) Comments::new)
                .lift(new OnCompleteReturn<PartialViewState<ViewStateDetails>>() {
                    @Override
                    public PartialViewState<ViewStateDetails> apply(@NonNull Boolean hadError) {
                        return new CommentsLoaded(hadError);
                    }
                })
                .onErrorReturn(PartialViewStateDetails.Error::new)
                .startWith(new CommentsProgressive(2, COMMENTS)));


        Observable<PartialViewState<ViewStateDetails>> similar = triggerRetry.flatMap(o -> movieFull
                .take(1)
                .flatMap(movieDomain -> routerSimilar.similar(movieDomain.id(), 1).subscribeOn(io))
                .concatMap(Observable::fromIterable)
                .map(mapperSmall)
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemCard -> itemCard.render(SIMILAR))
                .buffer(20)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) Similar::new)
                .lift(new OnCompleteReturn<PartialViewState<ViewStateDetails>>() {
                    @Override
                    public PartialViewState<ViewStateDetails> apply(Boolean hadError) {
                        return new SimilarLoaded(hadError);
                    }
                })
                .startWith(new SimilarProgressive(5, SIMILAR))
                .onErrorReturn(PartialViewStateDetails.Error::new));


        Observable<PartialViewState<ViewStateDetails>> errorDismissed =
                command(ViewDetails::errorDismissed).map(o -> new ErrorDismissed());


        ViewStateDetails initial = ViewStateDetails.builder()
                .casts(Collections.emptyList())
                .backdrops(Collections.emptyList())
                .similar(Collections.emptyList())
                .comments(Collections.emptyList())
                .movieBasic(movieBasic)
                .build();

        reduce(initial, merge(
                film,
                casts,
                backdrops,
                similar,
                comments,
                errorDismissed
        )).subscribe(this);

    }

    @Override
    protected void render(@NonNull ViewStateDetails state, @NonNull ViewDetails view) {
        if (state.error() != null) view.error(ErrorHandler.handle(state.error()));
        movieBasic = state.movieBasic();
        view.render(state);
    }
}
