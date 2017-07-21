package com.github.pedramrn.slick.parent.ui.home;

import android.view.View;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.ItemMovie;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-16
 */
public class OnItemClickListenerVideos implements OnItemClickListener {

    private final RouterProvider router;

    public OnItemClickListenerVideos(RouterProvider router) {
        this.router = router;
    }

    @Override
    public void onItemClick(Item item, View view) {
        ItemMovie itemCardMovie = (ItemMovie) item;
        MovieBasic movie = itemCardMovie.movie();
        if (movie == null) return;
        router.get().pushController(RouterTransaction.with(new ControllerDetails(movie, itemCardMovie.transitionName()))
                .pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler())
        );
    }
}
