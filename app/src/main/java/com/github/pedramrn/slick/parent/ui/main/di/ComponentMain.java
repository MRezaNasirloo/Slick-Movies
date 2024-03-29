package com.github.pedramrn.slick.parent.ui.main.di;

import com.github.pedramrn.slick.parent.ui.ActivityMain;
import com.github.pedramrn.slick.parent.ui.auth.ControllerAuth;
import com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.favorite.FloatingFavorite;
import com.github.pedramrn.slick.parent.ui.favorite.ControllerFavorite;
import com.github.pedramrn.slick.parent.ui.home.ControllerHome;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListPopular;
import com.github.pedramrn.slick.parent.ui.home.cardlist.RecyclerViewCardListTrending;
import com.github.pedramrn.slick.parent.ui.image.ControllerImage;
import com.github.pedramrn.slick.parent.ui.list.ControllerList;
import com.github.pedramrn.slick.parent.ui.main.ControllerMain;
import com.github.pedramrn.slick.parent.ui.people.ControllerPeople;
import com.github.pedramrn.slick.parent.ui.search.SearchViewImpl;
import com.github.pedramrn.slick.parent.ui.videos.ControllerVideos;

import dagger.Subcomponent;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface ComponentMain {
    void inject(ActivityMain activity);

    void inject(ControllerMain homeController);

    void inject(ControllerBoxOffice controllerBoxOffice);

    void inject(ControllerDetails controllerDetails);

    void inject(ControllerHome controllerHome);

    void inject(ControllerVideos controllerVideos);

    void inject(SearchViewImpl searchView);

    void inject(ControllerPeople controllerPeople);

    void inject(ControllerList controllerList);

    void inject(ControllerImage controllerImage);

    void inject(RecyclerViewCardListTrending recyclerViewCardListTrending);

    void inject(RecyclerViewCardListPopular recyclerViewCardListPopular);

    void inject(ControllerAuth controllerAuth);

    void inject(ControllerFavorite controllerFavorite);

    void inject(FloatingFavorite floatingFavorite);

    @Subcomponent.Builder
    interface Builder {
        Builder mainModule(MainModule module);

        ComponentMain build();

    }

}
