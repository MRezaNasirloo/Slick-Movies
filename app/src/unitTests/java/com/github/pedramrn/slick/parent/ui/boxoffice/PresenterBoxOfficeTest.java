package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.domain.router.RouterBoxOffice;
import com.github.pedramrn.slick.parent.ui.boxoffice.item.ItemBoxOfficeError;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeImplBaseTest;
import com.github.pedramrn.slick.parent.ui.boxoffice.state.ViewStateBoxOffice;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.pedramrn.slick.parent.ui.home.MapperMovieMetadataToMovieBasic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-21
 */
public class PresenterBoxOfficeTest extends RouterBoxOfficeImplBaseTest {

    private PresenterBoxOffice presenter;
    private TestScheduler testScheduler;
    private ViewBoxOffice viewBoxOffice;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        testScheduler = new TestScheduler();
        presenter = new PresenterBoxOffice(
                        routerBoxOffice,
                        new MapperMovieMetadataToMovieBasic(),
                        new MapperMovieDomainMovie(),
                testScheduler,
                testScheduler
                );
        viewBoxOffice = Mockito.mock(ViewBoxOffice.class);
        Mockito.when(viewBoxOffice.onRetry()).thenReturn(PublishSubject.create());
        ErrorHandler.disable();
    }

    @Test
    public void updateStream() throws Exception {

        presenter.onViewUp(viewBoxOffice);

        TestObserver<ViewStateBoxOffice> updateStream = presenter.updateStream().test();

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS);

        Mockito.verify(viewBoxOffice, Mockito.atLeastOnce()).update(Mockito.anyList());

    }

    @Test
    public void error() throws Exception {

        RouterBoxOffice routerBoxOffice = Mockito.mock(RouterBoxOffice.class);
        Mockito.when(routerBoxOffice.boxOffice()).thenReturn(Observable.error(new SocketTimeoutException()));
        PresenterBoxOffice presenterBoxOffice = presenter = new PresenterBoxOffice(
                routerBoxOffice,
                new MapperMovieMetadataToMovieBasic(),
                new MapperMovieDomainMovie(),
                testScheduler,
                testScheduler
        );
        presenterBoxOffice.onViewUp(viewBoxOffice);

        TestObserver<ViewStateBoxOffice> updateStream = presenterBoxOffice.updateStream().test();

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS);

        Mockito.verify(viewBoxOffice, Mockito.atLeastOnce()).update(Mockito.argThat(argument -> argument.size() > 0 && argument.get(0) instanceof ItemBoxOfficeError));

    }
}