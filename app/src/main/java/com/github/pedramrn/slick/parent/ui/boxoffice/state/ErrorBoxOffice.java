package com.github.pedramrn.slick.parent.ui.boxoffice.state;

import com.github.pedramrn.slick.parent.ui.boxoffice.item.ItemBoxOfficeError;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.xwray.groupie.Item;

import java.util.Map;
import java.util.TreeMap;

import static com.github.pedramrn.slick.parent.util.Utils.removeRemovables;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-24
 */

public class ErrorBoxOffice implements PartialViewState<ViewStateBoxOffice> {

    private final Throwable error;

    public ErrorBoxOffice(Throwable error) {
        this.error = error;
    }

    @Override
    public ViewStateBoxOffice reduce(ViewStateBoxOffice state) {
        Map<Integer, Item> movies = new TreeMap<>(state.movies());
        removeRemovables(movies.values().iterator(), null);
        String message = ErrorHandler.handle(error);
        Item itemError = new ItemBoxOfficeError(Integer.MAX_VALUE);
        movies.put(movies.size(), itemError);
        return state.toBuilder().error(error).movies(movies).build();
    }
}
