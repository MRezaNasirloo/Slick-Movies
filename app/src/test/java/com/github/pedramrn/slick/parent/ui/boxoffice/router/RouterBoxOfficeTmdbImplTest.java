package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import org.junit.Test;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-15
 */
public class RouterBoxOfficeTmdbImplTest extends RouterBoxOfficeImplBaseTest {

    @Test
    public void boxOffice() throws Exception {
        routerBoxOffice.boxOffice(Observable.just(1, 2), 2).test()
                .assertSubscribed()
                .assertValueCount(4)
                .assertComplete();
    }

}