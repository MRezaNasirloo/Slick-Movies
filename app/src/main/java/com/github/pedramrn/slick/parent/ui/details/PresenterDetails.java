package com.github.pedramrn.slick.parent.ui.details;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterAuth;
import com.github.pedramrn.slick.parent.domain.router.RouterComments;
import com.github.pedramrn.slick.parent.domain.router.RouterFavorite;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.domain.router.RouterSimilar;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.auth.router.RouterAuthImpl;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperCommentDomainComment;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.router.RouterCommentsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterSimilarImpl;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.favorite.router.RouterFavoriteImplSlick;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
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
    private final RouterFavorite routerFavorite;
    private final RouterAuth routerAuth;

    private final MapperMovieSmallDomainMovieSmall mapperSmall;
    private final MapperCommentDomainComment mapperComment;
    private final MapperMovieDomainMovie mapper;

    private final String CASTS = "CASTS";
    private final String SIMILAR = "SIMILAR";
    private final String BACKDROPS = "BACKDROPS";
    private final String COMMENTS = "COMMENTS";

    @Inject
    public PresenterDetails(
            RouterMovieDetailsImpl routerMovieDetails,
            RouterSimilarImpl routerSimilar,
            RouterCommentsImpl routerComments,
            RouterAuthImpl routerAuth,
            RouterFavoriteImplSlick routerFavorite,
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
        this.routerAuth = routerAuth;
        this.routerFavorite = routerFavorite;
        this.mapperComment = mapperComment;
        this.mapper = mapper;
        this.mapperSmall = mapperSmall;
    }


    @Override
    protected void start(ViewDetails view) {
        MovieBasic movieBasic = view.getMovie();
        final Integer movieId = movieBasic.id();

        Observable<Object> triggerRetry = command(ViewDetails::onRetryAll).startWith(1);

        final Observable<Movie> movieFull = triggerRetry.flatMap(o -> routerMovieDetails.get(movieId).subscribeOn(io)
                //Maps the domains Models to View Models which have android dependency
                .map(mapper)
        ).share();

        Observable<PartialViewState<ViewStateDetails>> movie = triggerRetry.flatMap(o -> movieFull
                .map((Function<Movie, PartialViewState<ViewStateDetails>>) PartialViewStateDetails.MovieFull::new)
                .onErrorReturn(PartialViewStateDetails.Error::new));

        Observable<PartialViewState<ViewStateDetails>> backdrops = triggerRetry.flatMap(o -> movieFull
                .take(1)
                .concatMap(movie1 -> Observable.fromIterable(movie1.images().backdrops()))
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemView -> itemView.render(BACKDROPS))
                .buffer(100)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) PartialViewStateDetails.MovieBackdrops::new)
                .startWith(new PartialViewStateDetails.MovieBackdropsProgressive(5, BACKDROPS))
                .onErrorReturn(PartialViewStateDetails.Error::new));

        Observable<PartialViewState<ViewStateDetails>> casts = triggerRetry.flatMap(o -> movieFull
                .take(1)
                .concatMap(movie13 -> Observable.fromIterable(movie13.casts()).take(6))
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemView -> itemView.render(CASTS))
                .buffer(20)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) PartialViewStateDetails.MovieCast::new)
                .startWith(new PartialViewStateDetails.MovieCastsProgressive(6, CASTS))
                .onErrorReturn(PartialViewStateDetails.Error::new));


        Observable<PartialViewState<ViewStateDetails>> comments = triggerRetry.flatMap(o -> movieFull
                .take(1)
                .flatMap(movie12 -> routerComments.comments(movie12.imdbId(), 1, 15).subscribeOn(io))
                .concatMap(comments1 -> Observable.fromIterable(comments1.data()))
                .map(mapperComment)
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemView -> itemView.render(COMMENTS))
                .buffer(20)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) PartialViewStateDetails.Comments::new)
                .lift(new OnCompleteReturn<PartialViewState<ViewStateDetails>>() {
                    @Override
                    public PartialViewState<ViewStateDetails> apply(@NonNull Boolean hadError) throws Exception {
                        return new PartialViewStateDetails.CommentsLoaded(hadError);
                    }
                })
                .onErrorReturn(PartialViewStateDetails.Error::new)
                .startWith(new PartialViewStateDetails.CommentsProgressive(2, COMMENTS)));


        Observable<PartialViewState<ViewStateDetails>> similar = triggerRetry.flatMap(o -> routerSimilar.similar(movieId, 1)
                .subscribeOn(io)
                .concatMap(Observable::fromIterable)
                .map(mapperSmall)
                .map(new MapProgressive())
                .cast(ItemView.class)
                .map(itemCard -> itemCard.render(SIMILAR))
                .buffer(20)
                .map((Function<List<Item>, PartialViewState<ViewStateDetails>>) PartialViewStateDetails.Similar::new)
                .startWith(new PartialViewStateDetails.SimilarProgressive(5, SIMILAR))
                .onErrorReturn(PartialViewStateDetails.Error::new));

        Observable<Boolean> commandFavorite = command(ViewDetails::commandFavorite);

        FavoriteDomain favoriteDomain = FavoriteDomain.create(movieBasic.imdbId(), movieBasic.id(), movieBasic.title(), "movie");

        Observable<PartialViewState<ViewStateDetails>> favorite = commandFavorite
                .flatMap(add -> (add ? routerFavorite.add(favoriteDomain) : routerFavorite.remove(favoriteDomain)).subscribeOn(io)
                        .map((Function<Object, PartialViewState<ViewStateDetails>>) isFavorite -> new PartialViewStateDetails.NoOp())
                        .onErrorReturn(PartialViewStateDetails.Error::new));

        Observable<PartialViewState<ViewStateDetails>> favoriteUpdate = commandFavorite
                .filter(add -> add)
                .flatMap(ignored -> routerAuth.firebaseUserSignInStateStream())
                .filter(signedIn -> signedIn)
                .take(1)
                .flatMap(ignored -> movieFull.flatMap(imdbId -> routerFavorite.add(favoriteDomain.toBuilder().imdbId(imdbId.imdbId()).build()))
                        .map((Function<Object, PartialViewState<ViewStateDetails>>) isFavorite -> new PartialViewStateDetails.NoOp())
                        .onErrorReturn(PartialViewStateDetails.Error::new));


        Observable<Boolean> signInStream = routerAuth.firebaseUserSignInStateStream().share();
        Observable<PartialViewState<ViewStateDetails>> favoriteStream = signInStream
                .filter(signedIn -> signedIn)
                .flatMap(ignored -> routerFavorite.updateStream(favoriteDomain.tmdb())
                        .takeUntil(signInStream.filter(signedIn2 -> !signedIn2)))
                .map((Function<Boolean, PartialViewState<ViewStateDetails>>) PartialViewStateDetails.Favorite::new)
                .onErrorReturn(PartialViewStateDetails.Error::new)
                .doOnComplete(() -> Log.e(TAG, "routerFavorite.updateStream Completed"))
                .subscribeOn(io);

        Observable<PartialViewState<ViewStateDetails>> errorDismissed =
                command(ViewDetails::errorDismissed).map(o -> new PartialViewStateDetails.ErrorDismissed());


        ViewStateDetails initial = ViewStateDetails.builder()
                .casts(Collections.emptyList())
                .backdrops(Collections.emptyList())
                .similar(Collections.emptyList())
                .comments(Collections.emptyList())
                .movieBasic(movieBasic)
                .build();

        reduce(initial, merge(
                movie,
                casts,
                backdrops,
                similar,
                comments,
                favorite,
                favoriteStream,
                favoriteUpdate,
                errorDismissed
        )).subscribe(this);

    }

    @Override
    protected void render(@NonNull ViewStateDetails state, @NonNull ViewDetails view) {
        Boolean favorite = state.isFavorite();
        if (favorite != null && favorite) {
            view.favorite();
        } else {
            view.notFavorite();
        }
        if (state.error() != null) view.error(ErrorHandler.handle(state.error()));
    }
}
