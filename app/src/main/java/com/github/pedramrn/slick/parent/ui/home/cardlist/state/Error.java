package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.error.ErrorMessageHandler;
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
public class Error implements PartialViewState<ViewStateCardList>, ErrorMessageHandler {

    protected final Throwable error;
    private String message;

    public Error(Throwable throwable) {
        this.error = throwable;
    }

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator(), null);

        ErrorHandler.handle(state.error(), this);

        Item itemError = new ItemError(Integer.MAX_VALUE, PresenterCardList.MOVIES_CARD, message);
        movies.put(movies.size(), itemError);

        return state.toBuilder()
                .error(error)
                .isLoading(true)
                .itemLoadedCount(movies.size())
                .movies(movies)
                .build();
    }

    @Override
    public void error(String message) {
        this.message = message;
    }

}
