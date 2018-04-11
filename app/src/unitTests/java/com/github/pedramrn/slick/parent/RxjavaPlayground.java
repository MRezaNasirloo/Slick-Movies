package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.utils.LogIt;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2018-04-11
 */
public class RxjavaPlayground {

    @Test
    public void testReplay() throws Exception {
        Observable<Integer> replay = Observable.range(0, 10).subscribeOn(Schedulers.trampoline())
                .replay(1).autoConnect()
                .observeOn(Schedulers.trampoline())
                .doOnEach(new LogIt<>());

        replay.subscribe();

        replay.subscribe();

        Thread.sleep(100);
    }


}
