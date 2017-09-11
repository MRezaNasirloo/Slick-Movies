package com.github.pedramrn.slick.parent.ui.home;

import android.view.View;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.ItemMovie;
import com.github.pedramrn.slick.parent.ui.list.OnItemAction;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-16
 */
public class OnItemClickListenerAction implements OnItemClickListener {

    private final RouterProvider router;
    private final ControllerProvider provider;

    public OnItemClickListenerAction(RouterProvider router, ControllerProvider provider) {
        this.router = router;
        this.provider = provider;
    }

    @Override
    public void onItemClick(Item item, View view) {
        if (item instanceof OnItemAction) { ((OnItemAction) item).action(router); }
        else if (item instanceof ItemMovie) {
            ItemMovie itemCardMovie = (ItemMovie) item;
            MovieBasic movie = itemCardMovie.movie();
            if (movie == null) return;
            router.get().pushController(RouterTransaction.with(provider.get(movie, itemCardMovie.transitionName()))
                                                .pushChangeHandler(new HorizontalChangeHandler())
                                                .popChangeHandler(new HorizontalChangeHandler())
            );
        }
    }
}
