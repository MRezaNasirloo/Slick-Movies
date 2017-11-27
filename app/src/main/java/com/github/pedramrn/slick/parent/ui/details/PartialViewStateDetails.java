package com.github.pedramrn.slick.parent.ui.details;

import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.boxoffice.item.ItemBoxOfficeError;
import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdropEmpty;
import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdropProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCastProgressive;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentEmpty;
import com.github.pedramrn.slick.parent.ui.details.item.ItemCommentProgressive;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.github.pedramrn.slick.parent.util.Utils;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.List;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

public class PartialViewStateDetails {

    public static final String DETAILS = "DETAILS";

    private PartialViewStateDetails() {
        //no instance
    }

    static class SimilarProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public SimilarProgressive(int count, String tag) {
            super(count, tag, new ItemRendererProgressiveCard());
        }


        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            List<Item> similar = state.similar();
            removeRemovables(similar.iterator(), "Details");
            return state.toBuilder().similar(reduce(similar)).build();
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

    static class SimilarLoaded implements PartialViewState<ViewStateDetails> {

        private final boolean hadError;

        public SimilarLoaded(boolean hadError) {
            this.hadError = hadError;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            ArrayList<Item> items = new ArrayList<>(state.similar());
            Utils.removeRemovables(items.iterator(), DETAILS);
            if (items.isEmpty()) items.add(0, new ItemCommentEmpty(0, DETAILS, R.string.empty_similar));
            return state.toBuilder().similar(items).build();
        }
    }

    static class Error extends ErrorAbs {

        public Error(Throwable throwable) {
            super(throwable);
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            if (state.error() == null) {
                state = state.toBuilder().error(throwable).build();
            }
            ArrayList<Item> items = new ArrayList<>(state.similar());
            Utils.removeRemovables(items.iterator(), DETAILS);
            items.add(new ItemBoxOfficeError(0, ErrorHandler.handle(throwable)));
            return state;
        }
    }

    static class ErrorDismissed implements PartialViewState<ViewStateDetails> {

        public ErrorDismissed() {
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().error(null).build();
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
            if (backdrops.isEmpty()) {
                backdrops.add(new ItemBackdropEmpty());
            }
            return state.toBuilder().backdrops(new ArrayList<>(backdrops)).build();
        }
    }

    static class MovieBackdropsProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public MovieBackdropsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererBackdrops());
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            List<Item> backdrops = state.backdrops();
            removeRemovables(backdrops.iterator(), "Details");
            return state.toBuilder().backdrops(reduce(backdrops)).build();
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
            List<Item> casts = state.casts();
            removeRemovables(casts.iterator(), "Details");
            return state.toBuilder().casts(reduce(casts)).build();
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

    static class Favorite implements PartialViewState<ViewStateDetails> {
        private final Boolean isFavorite;

        public Favorite(Boolean isFavorite) {
            this.isFavorite = isFavorite;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state.toBuilder().isFavorite(isFavorite).build();
        }
    }

    static class NoOp implements PartialViewState<ViewStateDetails> {

        public NoOp() {
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            return state;
        }
    }


    static class CommentsProgressive extends PartialProgressive implements PartialViewState<ViewStateDetails> {

        public CommentsProgressive(int count, String tag) {
            super(count, tag, new ItemRendererComments());
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            List<Item> comments = state.comments();
            removeRemovables(comments.iterator(), "Details");
            return state.toBuilder().comments(reduce(comments)).build();
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
            removeRemovables(comments.iterator(), null);
            if (comments.isEmpty()) {
                comments.add(new ItemCommentEmpty(0, "EMPTY", R.string.comments_empty));
            }
            return state.toBuilder().comments(comments).build();
        }
    }

    /*static class CommentsError implements PartialViewState<ViewStateDetails> {
        private final Throwable error;

        public CommentsError(Throwable error) {
            this.error = error;
        }

        @Override
        public ViewStateDetails reduce(ViewStateDetails state) {
            List<Item> comments = state.comments();
            removeRemovables(comments.iterator(), null);
            comments.add(new ItemCommentError(0, error.getMessage(), "COMMENTS_ERROR"));
            return state.toBuilder().comments(comments).errorComments(error).build();
        }
    }*/
}
