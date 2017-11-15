package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.pedramrn.slick.parent.BuildConfig;
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

    protected final Throwable error;

    public Error(Throwable throwable) {
        this.error = throwable;
    }

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator(), null);

        final String message;
        if (error instanceof UnknownHostException || error instanceof SocketTimeoutException) {
            // TODO: 2017-11-13 extract String? how
            message = "Network Error, Are you Connected?";
            Crashlytics.log(Log.INFO, error.getClass().getSimpleName(), error.getMessage());
        } else {
            message = "Internal Error";
            if (BuildConfig.DEBUG) error.printStackTrace();
            Crashlytics.logException(error);
        }


        Item itemError = new ItemError(Integer.MAX_VALUE, PresenterCardList.MOVIES_CARD, message);
        movies.put(movies.size(), itemError);

        return state.toBuilder()
                .error(error)
                .isLoading(true)
                .itemLoadedCount(movies.size())
                .movies(movies)
                .build();
    }
}
