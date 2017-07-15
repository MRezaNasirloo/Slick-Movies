package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdropProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastProgressive;
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

    interface ItemRenderer {
        Item render(long id, String tag);
    }

    protected static abstract class ItemProgressive implements PartialViewState<ViewStateDetails> {

        protected final List<Item> progressive;


        public ItemProgressive(int count, String tag, ItemRenderer itemRenderer) {
            progressive = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(itemRenderer.render(id, tag));
            }
        }

        public ItemProgressive(String tag, ItemRenderer itemRenderer) {
            progressive = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                int id = IdBank.nextId(tag);
                progressive.add(itemRenderer.render(id, tag));
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

    static class ItemProgressiveSimilar extends ItemProgressive {

        public ItemProgressiveSimilar(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
        }

        public ItemProgressiveSimilar(String tag) {
            super(tag, new ItemRendererProgressiveCard());
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().similar(reduce(state.similar())).build();
        }

        private static class ItemRendererProgressiveCard implements ItemRenderer {
            @Override
            public Item render(long id, String tag) {
                return ItemCardProgressiveImpl.create(id).render(tag);
            }
        }
    }

    static class Similar implements PartialViewState<ViewStateDetails> {

        private final List<Item> movies;

        public Similar(List<Item> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().similar(new ArrayList<>(movies)).build();
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

    static class MovieFull implements PartialViewState<ViewStateDetails> {

        private final Movie movie;

        public MovieFull(Movie movie) {
            this.movie = movie;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().movieBasic(movie).build();
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

    static class MovieCast implements PartialViewState<ViewStateDetails> {

        private final List<Item> casts;

        public MovieCast(List<Item> casts) {
            this.casts = casts;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().casts(new ArrayList<>(casts)).build();
        }
    }

    static class MovieBackdrops implements PartialViewState<ViewStateDetails> {

        private final List<Item> backdrops;

        public MovieBackdrops(List<Item> backdrops) {
            this.backdrops = backdrops;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().backdrops(new ArrayList<>(backdrops)).build();
        }
    }

    static class ErrorMovieCast extends Error {
        public ErrorMovieCast(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().errorMovieCast(throwable).build();
        }
    }

    static class ErrorMovieBackdrop extends Error {
        public ErrorMovieBackdrop(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().errorMovieBackdrop(throwable).build();
        }
    }

    static class MovieBackdropsProgressive extends ItemProgressive {

        public MovieBackdropsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererBackdrops());
        }

        public MovieBackdropsProgressive(String tag) {
            super(tag, new ItemRendererBackdrops());
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().backdrops(reduce(state.backdrops())).build();
        }

        private static class ItemRendererBackdrops implements ItemRenderer {
            @Override
            public Item render(long id, String tag) {
                return new ItemBackdropProgressive(id);
            }
        }
    }

    static class MovieCastsProgressive extends ItemProgressive {

        public MovieCastsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererCasts());
        }

        public MovieCastsProgressive(String tag) {
            super(tag, new ItemRendererCasts());
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().casts(reduce(state.casts())).build();
        }

        private static class ItemRendererCasts implements ItemRenderer {
            @Override
            public Item render(long id, String tag) {
                return new ItemCastProgressive(id);
            }
        }
    }


}
