package com.github.pedramrn.slick.parent.ui.header;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.router.RouterMovieDetails;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsIMDBImpl;
import com.github.pedramrn.slick.parent.ui.details.router.RouterMovieDetailsImpl;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavoriteProgressive;
import com.github.pedramrn.slick.parent.ui.header.state.ErrorDismissedHeader;
import com.github.pedramrn.slick.parent.ui.header.state.ErrorHeader;
import com.github.pedramrn.slick.parent.ui.header.state.Header;
import com.github.pedramrn.slick.parent.ui.header.state.HeaderProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
import com.xwray.groupie.Item;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A simple Presenter responsible for showing the header info
 */
public class PresenterHeader extends SlickPresenterUni<ViewHeader, StateHeader> {

    private RouterMovieDetails routerMovieDetails;
    private final RouterMovieDetails routerMovieDetailsIMDB;
    private final MapperMovieDomainMovie mapper;

    @Inject
    PresenterHeader(
            RouterMovieDetailsImpl routerMovieDetails,
            RouterMovieDetailsIMDBImpl routerMovieDetailsIMDB,
            MapperMovieDomainMovie mapper,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main) {
        super(main, io);
        this.routerMovieDetails = routerMovieDetails;
        this.routerMovieDetailsIMDB = routerMovieDetailsIMDB;

        this.mapper = mapper;
    }

    @Override
    public void start(@NonNull ViewHeader view) {
        MovieBasic movieBasic = view.movie();
        final String movieId;
        if (movieBasic.id() == -1) {
            movieId = movieBasic.imdbId();
            routerMovieDetails = this.routerMovieDetailsIMDB;
        } else {
            movieId = movieBasic.id().toString();
        }

        Observable<Object> trigger = command(ViewHeader::onRetry).share().startWith(1);

        final Observable<Movie> movieFull = trigger.flatMap(o -> routerMovieDetails.get(movieId).subscribeOn(io)
                //Maps the domains Models to View Models which have android dependency
                .map(mapper)
        ).share();

        Observable<PartialViewState<StateHeader>> movie = trigger.flatMap(o -> movieFull
                .map((Function<Movie, PartialViewState<StateHeader>>) MovieFull::new)
                .onErrorReturn(ErrorHeader::new));

        Observable<PartialViewState<StateHeader>> header = trigger.flatMap(o -> movieFull
                .cast(ItemView.class)
                .map(itemView -> itemView.render(MovieSmall.HEADER))
                .map((Function<Item, PartialViewState<StateHeader>>) Header::new)
                .startWith(new HeaderProgressive())
                .onErrorReturn(ErrorHeader::new)
        ).onErrorReturn(ErrorHeader::new);

        Observable<PartialViewState<StateHeader>> errorDismissed =
                command(ViewHeader::errorDismissed).map(o -> new ErrorDismissedHeader());

        StateHeader initialState = StateHeader.builder()
                .header(new ItemFavoriteProgressive(0))
                .movie(movieBasic)
                .build();

        subscribe(initialState, merge(movie, header, errorDismissed));
    }

    @Override
    protected void render(@NonNull StateHeader state, @NonNull ViewHeader view) {
        view.update(state.header());
        if (state.error() != null) view.error(ErrorHandler.handle(state.error()));
    }

    static class MovieFull implements PartialViewState<StateHeader> {

        private final Movie movie;

        public MovieFull(Movie movie) {
            this.movie = movie;
        }

        @NonNull
        @Override
        public StateHeader reduce(@NonNull StateHeader state) {
            return state.toBuilder().movie(movie).build();
        }
    }
}
