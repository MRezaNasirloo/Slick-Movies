package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.IdBank;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

public class PartialViewStateDetails {

    private PartialViewStateDetails() {
        //no instance
    }

    protected static abstract class CardProgressive implements PartialViewState<ViewStateDetails> {

        protected final List<Item> progressive;


        public CardProgressive(int count, String tag) {
            progressive = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(ItemCardProgressiveImpl.create(id).render(null));
            }
        }

        public CardProgressive(String tag) {
            progressive = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(ItemCardProgressiveImpl.create(id).render(null));
            }
        }

        protected List<Item> reduce(List<Item> items) {
            if (items != null && items.size() > 0) {
                items.addAll(progressive);
            } else {
                items = progressive;
            }
            return items;
        }
    }

    static class CardProgressiveSimilar extends CardProgressive {

        public CardProgressiveSimilar(int count, String tag) {
            super(count, tag);
        }

        public CardProgressiveSimilar(String tag) {
            super(tag);
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().similar(reduce(state.similar())).build();
        }
    }

    static class Similar implements PartialViewState<ViewStateDetails> {

        private final List<Item> movies;

        public Similar(List<Item> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().similar(movies).build();
        }
    }

    static class ErrorSimilar extends Error {

        public ErrorSimilar(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().errorSimilar(throwable).build();
        }
    }

    static class MovieFull implements PartialViewState<ViewStateDetails>{

        private final Movie movie;

        public MovieFull(Movie movie) {
            this.movie = movie;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().movie(movie).build();
        }
    }

    static class ErrorMovieFull extends Error {

        public ErrorMovieFull(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().errorMovie(throwable).build();
        }
    }

}
