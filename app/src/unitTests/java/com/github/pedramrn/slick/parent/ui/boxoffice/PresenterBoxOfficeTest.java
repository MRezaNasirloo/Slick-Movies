package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeImplBaseTest;
import com.github.pedramrn.slick.parent.ui.details.mapper.MapperMovieDomainMovie;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-21
 */
public class PresenterBoxOfficeTest extends RouterBoxOfficeImplBaseTest {

    private PresenterBoxOffice presenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        presenter = new PresenterBoxOffice(routerBoxOffice, new MapperMovieDomainMovie(), Schedulers.trampoline(), Schedulers.trampoline());
    }

    @Test
    public void updateStream() throws Exception {
        TestObserver<ViewStateBoxOffice> test = presenter.updateStream().test();
        PublishSubject<Integer> subject = PublishSubject.create();
        presenter.getBoxOffice(subject, 2);
        subject.onNext(1);

        test.assertValueCount(2).assertNotComplete();
        subject.onNext(1);
        subject.onNext(1);
        subject.onNext(1);
        test.assertValueCount(8).assertNotComplete();
        subject.onNext(1);
        test.assertValueCount(10).assertNotComplete();
        subject.onNext(1);
        //the Update stream does not complete in case of any future change to anticipated.
        test.assertValueCount(10).assertNotComplete();
    }
}