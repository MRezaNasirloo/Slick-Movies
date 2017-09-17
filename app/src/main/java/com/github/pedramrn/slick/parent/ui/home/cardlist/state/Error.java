package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList;
import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemError;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class Error implements PartialViewState<ViewStateCardList> {

    protected final Throwable throwable;

    public Error(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator());

        Item itemError = new ItemError(-1, PresenterCardList.MOVIES_CARD, throwable.getMessage());
        movies.put(((int) itemError.getId()), itemError);

        return state.toBuilder()
                .error(throwable)
                .isLoading(true)
                .movies(movies)
                .build();
    }
}
