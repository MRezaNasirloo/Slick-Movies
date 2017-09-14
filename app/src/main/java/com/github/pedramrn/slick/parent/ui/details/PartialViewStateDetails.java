package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdropProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentEmpty;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentError;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentProgressive;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

public class PartialViewStateDetails {

    private PartialViewStateDetails() {
        //no instance
    }

    static class SimilarProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public SimilarProgressive(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
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

    static class MovieBackdropsProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public MovieBackdropsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererBackdrops());
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

    static class MovieCastsProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public MovieCastsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererCasts());
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

    static class Comments implements PartialViewState<ViewStateDetails> {
        private final List<Item> comments;

        public Comments(List<Item> comments) {
            this.comments = comments;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().comments(comments).build();
        }
    }


    static class CommentsProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public CommentsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererComments());
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().comments(reduce(state.comments())).build();
        }

        static class ItemRendererComments implements ItemRenderer {

            @Override
            public Item render(long id, String tag) {
                return new ItemCommentProgressive(id, tag);
            }
        }
    }

    static class CommentsLoaded implements PartialViewState<ViewStateDetails> {
        private final boolean hadError;

        public CommentsLoaded(boolean hadError) {
            this.hadError = hadError;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            List<Item> comments = state.comments();
            removeRemovables(comments.iterator());
            if (comments.isEmpty()){
                comments.add(new ItemCommentEmpty(0, "EMPTY"));
            }
            return state.toBuilder().comments(comments).build();
        }
    }

    static class CommentsError implements PartialViewState<ViewStateDetails> {
        private final Throwable throwable;

        public CommentsError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            List<Item> comments = state.comments();
            removeRemovables(comments.iterator());
            comments.add(new ItemCommentError(0, throwable.getMessage(), "COMMENTS_ERROR"));
            return state.toBuilder().comments(comments).errorComments(throwable).build();
        }
    }
}
