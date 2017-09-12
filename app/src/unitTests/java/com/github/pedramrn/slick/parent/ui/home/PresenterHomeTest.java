package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.ApiTmdbMock;
import com.github.pedramrn.slick.parent.ApiTraktMock;
import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.TypeAdapterFactoryGson;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.domain.mapper.MapperCast;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.domain.mapper.MapperSimpleData;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.home.router.RouterPopularImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterTrendingImpl;
import com.github.pedramrn.slick.parent.ui.home.router.RouterUpcomingImpl;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.mock.NetworkBehavior;

import static com.github.pedramrn.slick.parent.util.FileUtils.readFile;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-09
 */
public class PresenterHomeTest {

    private PresenterHome presenterHome;

    @Before
    public void setUp() throws Exception {
        final InputStream inputStreamTmdb = ClassLoader.getSystemResourceAsStream("api_tmdb_movie_list.json");
        final String bufferTmdb = readFile(inputStreamTmdb);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapterFactoryGson.create())
                .create();

        NetworkBehavior behavior = new ModuleNetwork().networkBehavior();

        List<MovieTmdb> movieTmdbList = gson.fromJson(bufferTmdb, new TypeToken<List<MovieTmdb>>() {}.getType());

        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt_trending_200.json");
        final String bufferTrakt = readFile(inputStreamTrakt);

        List<MovieTraktPageMetadata> trendingList =
                gson.fromJson(bufferTrakt, new TypeToken<List<MovieTraktPageMetadata>>() {}.getType());
        ApiTmdb apiTmdbMock = new ApiTmdbMock(behavior, gson, movieTmdbList, trendingList);
        ApiTrakt apiTraktMock = new ApiTraktMock(behavior, gson, trendingList, null);

        RouterTrendingImpl routerTrending = new RouterTrendingImpl(
                apiTraktMock,
                apiTmdbMock,
                new MapperMovie(new MapperCast(), new MapperSimpleData())
        );
        RouterPopularImpl routerPopular = new RouterPopularImpl(
                apiTraktMock,
                apiTmdbMock,
                new MapperMovie(new MapperCast(), new MapperSimpleData())
        );
        RouterUpcomingImpl routerUpcoming = new RouterUpcomingImpl(apiTmdbMock, new MapperMovieSmall());
        presenterHome = new PresenterHome(
                routerTrending,
                routerPopular,
                routerUpcoming,
                new MapperMovieDomainMovie(),
                new MapperMovieSmallDomainMovieSmall(),
                new MapperMovieMetadataToMovieBasic(),
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
    }

    @Test
    public void start() throws Exception {
        presenterHome.onViewUp(viewHome);
        presenterHome.updateStream().test().await(20, TimeUnit.SECONDS);
    }

    private ViewHome viewHome = new ViewHome() {
        @Override
        public void render(ViewStateHome state) {
            System.out.println("PresenterHomeTest.render");
        }

        @Override
        public Observable<Integer> triggerTrending() {
            System.out.println("PresenterHomeTest.triggerTrending");
            return Observable.intervalRange(1, 100, 0, 2, TimeUnit.SECONDS).map(new Function<Long, Integer>() {
                @Override
                public Integer apply(@NonNull Long aLong) throws Exception {
                    return aLong.intValue();
                }
            });
        }

        @Override
        public Observable<Integer> triggerPopular() {
            return Observable.never();
        }

        @Override
        public int pageSize() {
            System.out.println("PresenterHomeTest.pageSize");
            return 3;
        }

        @Override
        public Observable<Integer> retryTrending() {
            System.out.println("PresenterHomeTest.retryTrending");
            return Observable.intervalRange(0, 100, 1, 2, TimeUnit.SECONDS).map(new Function<Long, Integer>() {
                @Override
                public Integer apply(@NonNull Long aLong) throws Exception {
                    return aLong.intValue();
                }
            });
        }

        @Override
        public Observable<Object> retryUpcoming() {
            return Observable.never();
        }
    };

}