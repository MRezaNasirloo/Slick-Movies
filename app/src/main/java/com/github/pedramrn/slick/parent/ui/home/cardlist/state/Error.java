package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList;
import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemError;
import com.xwray.groupie.Item;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
        removeRemovables(movies.values().iterator(), null);

        final String message;
        if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
            // TODO: 2017-11-13 extract String? how
            message = "Network Error, Are you Connected?";
        } else {
            message = "Internal Error";
            throwable.printStackTrace();
            // TODO: 2017-11-13 log to fabric
        }


        Item itemError = new ItemError(Integer.MAX_VALUE, PresenterCardList.MOVIES_CARD, message);
        movies.put(movies.size(), itemError);

        return state.toBuilder()
                .error(throwable)
                .isLoading(true)
                .itemLoadedCount(movies.size())
                .movies(movies)
                .build();
    }
}
