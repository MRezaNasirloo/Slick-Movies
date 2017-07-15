package com.github.pedramrn.slick.parent.ui.home;

import android.view.View;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-16
 */
public class OnItemClickListenerDetails implements OnItemClickListener {

    private final RouterProvider router;

    public OnItemClickListenerDetails(RouterProvider router) {
        this.router = router;
    }

    @Override
    public void onItemClick(Item item, View view) {
        ItemCardMovie itemCardMovie = (ItemCardMovie) item;
        if (itemCardMovie.getMovie() == null) return;
        router.get().pushController(RouterTransaction.with(new ControllerDetails(itemCardMovie.getMovie(), itemCardMovie.getTransitionName()))
                .pushChangeHandler(new HorizontalChangeHandler())
                .popChangeHandler(new HorizontalChangeHandler())
        );
    }
}
