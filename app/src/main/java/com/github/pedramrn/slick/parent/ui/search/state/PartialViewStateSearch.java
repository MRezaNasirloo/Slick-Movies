package com.github.pedramrn.slick.parent.ui.search.state;

import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Collections;
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
            return state.toBuilder().movies(movies).errorMovies(null).loadingMovies(false).build();
        }
    }

    public static class ErrorMovie implements PartialViewState<ViewStateSearch> {

        private final Throwable throwable;

        public ErrorMovie(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateSearch reduce(ViewStateSearch state) {
            return state.toBuilder().errorMovies(throwable).loadingMovies(false).build();
        }
    }


    public static class Loading implements PartialViewState<ViewStateSearch> {

        private final boolean isLoading;

        public Loading(boolean isLoading) {
            this.isLoading = isLoading;
        }

        @Override
        public ViewStateSearch reduce(ViewStateSearch state) {
            return state.toBuilder().loadingMovies(isLoading).errorMovies(null).build();
        }
    }

    public static class SearchOpenClose implements PartialViewState<ViewStateSearch> {

        private final boolean isOpen;

        public SearchOpenClose(boolean isOpen) {
            this.isOpen = isOpen;
        }

        @Override
        public ViewStateSearch reduce(ViewStateSearch state) {
            if (!isOpen) {
                return state.toBuilder()
                        .movies(Collections.emptyList())
                        .loadingMovies(false)
                        .errorMovies(null)
                        .build();
            }
            return state;
        }
    }

}
