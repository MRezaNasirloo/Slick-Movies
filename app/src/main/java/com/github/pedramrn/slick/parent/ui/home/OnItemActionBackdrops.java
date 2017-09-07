package com.github.pedramrn.slick.parent.ui.home;

import android.view.View;

import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.image.ControllerImage;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-16
 */
public class OnItemActionBackdrops implements OnItemClickListener {

    private final RouterProvider router;
    private final MovieProvider movie;
    private final GroupAdapter adapter;

    public OnItemActionBackdrops(RouterProvider router, MovieProvider movie, GroupAdapter adapter) {
        this.router = router;
        this.movie = movie;
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(Item item, View view) {
        if (this.movie.get() instanceof Movie) {
            Movie movie = (Movie) this.movie.get();
            List<String> backdrops = Observable.fromIterable(movie.images().backdrops())
                    .map(new Function<Backdrop, String>() {
                        @Override
                        public String apply(@NonNull Backdrop backdrop) throws Exception {
                            return backdrop.backdropPath();
                        }
                    })
                    .distinct()
                    .buffer(movie.images().backdrops().size())
                    .blockingFirst();
            ControllerImage.start(router.get(), movie.title(), ((ArrayList<String>) backdrops), adapter.getAdapterPosition(item));
        }
    }

    private static final String TAG = OnItemActionBackdrops.class.getSimpleName();
}
