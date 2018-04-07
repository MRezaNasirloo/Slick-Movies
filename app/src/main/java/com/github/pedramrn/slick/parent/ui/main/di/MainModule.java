package com.github.pedramrn.slick.parent.ui.main.di;

import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.android.ImageLoaderPicassoImpl;
import com.github.pedramrn.slick.parent.ui.home.MapperMovieMetadataToMovieBasic;
import com.github.pedramrn.slick.parent.ui.home.cardlist.PresenterCardList;
import com.github.pedramrn.slick.parent.ui.home.router.RouterPopularImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterTrendingImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-01
 */
@Module
public class MainModule {

    @Provides
    public ImageLoader imageLoader() {
        return new ImageLoaderPicassoImpl();
    }

    @Provides
    @Named(PresenterCardList.TRENDING)
    public PresenterCardList presenterCardListTrending(
            RouterTrendingImpl router,
            MapperMovieMetadataToMovieBasic mapper,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main
    ) {
        return new PresenterCardList(router, mapper, PresenterCardList.TRENDING, main, io);
    }

    @Provides
    @Named(PresenterCardList.POPULAR)
    public PresenterCardList presenterCardListPopular(
            RouterPopularImpl router,
            MapperMovieMetadataToMovieBasic mapper,
            @Named("io") Scheduler io,
            @Named("main") Scheduler main
    ) {
        return new PresenterCardList(router, mapper, PresenterCardList.POPULAR, main, io);
    }
}
