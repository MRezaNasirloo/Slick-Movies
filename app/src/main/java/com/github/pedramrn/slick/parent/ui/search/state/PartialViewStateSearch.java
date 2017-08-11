package com.github.pedramrn.slick.parent.ui.search.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class PartialViewStateSearch {
    private PartialViewStateSearch() {
        //no instance
    }

    public static class Movies implements PartialViewState<ViewStateSearch> {

        private final List<Item> movies;

        public Movies(List<Item> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateSearch reduce(ViewStateSearch state) {
            return state.toBuilder().movies(movies).build();
        }
    }

    public static class ErrorMovie implements PartialViewState<ViewStateSearch> {

        private final Throwable throwable;

        public ErrorMovie(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateSearch reduce(ViewStateSearch state) {
            return state.toBuilder().errorMovies(throwable).build();
        }
    }


    public static class Loading implements PartialViewState<ViewStateSearch> {

        private final boolean isLoading;

        public Loading(boolean isLoading) {
            this.isLoading = isLoading;
        }

        @Override
        public ViewStateSearch reduce(ViewStateSearch state) {
            return state.toBuilder().loadingMovies(isLoading).build();
        }
    }
}
