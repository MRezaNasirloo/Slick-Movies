package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.github.pedramrn.slick.parent.mock.ApiMockProvider;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieSmallDomainMovieSmall;
import com.github.pedramrn.slick.parent.ui.home.router.RouterUpcomingImpl;
import com.github.pedramrn.slick.parent.ui.home.state.ViewStateHome;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-09
 */
public class PresenterHomeTest {

    private PresenterHome presenterHome;

    @Before
    public void setUp() throws Exception {
        ApiMockProvider apiMockProvider = new ApiMockProvider();
        ApiTmdb apiTmdbMock = apiMockProvider.apiTmdb();

        RouterUpcomingImpl routerUpcoming = new RouterUpcomingImpl(apiTmdbMock, new MapperMovieSmall());
        presenterHome = new PresenterHome(
                routerUpcoming,
                new MapperMovieSmallDomainMovieSmall(),
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
    }

    @Test
    public void start() throws Exception {
        presenterHome.onViewUp(viewHome);
        presenterHome.updateStream().test().await(1, TimeUnit.SECONDS);
    }

    @Test
    public void testTrendingTrigger() throws Exception {
        presenterHome.onViewUp(viewHome);
        presenterHome.updateStream().test()
                .assertValueCount(6)
                .assertNoErrors();
    }

    private PublishSubject<Integer> triggerTrending = PublishSubject.create();
    private ViewHome viewHome = new ViewHome() {
        @Override
        public void onRetry(String tag) {

        }

        @Override
        public void render(ViewStateHome state) {
            System.out.println("PresenterHomeTest.render");
        }

        @Override
        public Observable<Object> retryUpcoming() {
            return Observable.never();
        }
    };

}