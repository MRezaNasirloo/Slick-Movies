package com.github.pedramrn.slick.parent.ui.home.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.PresenterHome;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBannerError;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBannerProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.xwray.groupie.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-22
 */

public final class PartialViewStateHome {

    private PartialViewStateHome() {
        //no instance
    }

    public static class Upcoming implements PartialViewState<ViewStateHome> {
        private final List<Item> movies;

        public Upcoming(List<Item> movies) {
            this.movies = movies;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            return state.toBuilder().upcoming(movies).build();
        }
    }

    public static class UpcomingError implements PartialViewState<ViewStateHome> {

        private final Throwable throwable;

        public UpcomingError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public ViewStateHome reduce(ViewStateHome state) {
            List<Item> upcoming = state.upcoming();
            Iterator<Item> iterator = upcoming.iterator();
            removeRemovables(iterator, null);
            short code = ErrorHandler.handle(throwable);
            Item itemError = new ItemBannerError(-1, PresenterHome.UPCOMING, code);
            upcoming.add(itemError);

            return state.toBuilder()
                    .upcoming(new ArrayList<>(upcoming))
                    .errorUpcoming(throwable)
                    .build();
        }
    }

    public static class ProgressiveBannerImpl extends PartialProgressive implements PartialViewState<ViewStateHome> {


        public ProgressiveBannerImpl(int count, String tag) {
            super(count, tag, new ItemRendererBanner());
        }

        @Override
        public ViewStateHome reduce(ViewStateHome viewStateHome) {
            return viewStateHome.toBuilder().upcoming(reduce(viewStateHome.upcoming())).build();
        }

        public static class ItemRendererBanner implements ItemRenderer {
            @Override
            public Item render(long id, String tag) {
                return new ItemBannerProgressive(id, tag);
            }
        }
    }
}
