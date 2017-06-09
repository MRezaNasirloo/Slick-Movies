package com.github.pedramrn.slick.parent.ui.boxoffice;

import com.github.pedramrn.slick.parent.ui.boxoffice.model.Movie;
import com.github.pedramrn.slick.parent.ui.boxoffice.router.RouterBoxOfficeImplBaseTest;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-21
 */
public class ViewModelBoxOfficeTest extends RouterBoxOfficeImplBaseTest {

    @Mock
    ViewBoxOffice view;

    @Test
    public void test() throws Exception {
        PresenterBoxOffice presenter = new PresenterBoxOffice(routerBoxOffice, Schedulers.trampoline(), Schedulers.trampoline());
        ViewModelBoxOffice viewModel = new ViewModelBoxOffice(new CompositeDisposable(), presenter, view);

        TestObserver<List<Movie>> test = viewModel.boxOfficeList().test();
        PublishSubject<Integer> subject = PublishSubject.create();

        viewModel.pagination(subject, 2);
        subject.onNext(1);
        test.assertValueCount(2).assertNotComplete();
        subject.onNext(1);
        subject.onNext(1);
        subject.onNext(1);
        subject.onNext(1);
        subject.onNext(1);
        subject.onNext(1);
        subject.onNext(1);
        test.assertValueCount(10).assertNotComplete();


    }
}