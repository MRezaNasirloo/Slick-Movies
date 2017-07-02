package com.github.pedramrn.slick.parent.home.mapper;

import com.github.pedramrn.slick.parent.test.AutoTest;
import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-02
 */
public class MapProgressiveTest {

    private ArrayList<AutoTest> list;
    private MapProgressive mapProgressive;

    @Before
    public void setUp() throws Exception {
        mapProgressive = new MapProgressive(5);
        Random random = new Random();
        list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(AutoTest.create(random.nextInt()));
        }


    }

    @Test
    public void testApply() throws Exception {
        Observable.fromIterable(list)
                .map(mapProgressive)
                .test()
                .assertNoErrors()
                .assertValueAt(0, new Predicate<AutoBase>() {
                    @Override
                    public boolean test(@NonNull AutoBase autoBase) throws Exception {
                        return autoBase.uniqueId().equals(0);
                    }
                })
                .assertValueAt(1, new Predicate<AutoBase>() {
                    @Override
                    public boolean test(@NonNull AutoBase autoBase) throws Exception {
                        return autoBase.uniqueId().equals(1);
                    }
                })
                .assertValueAt(4, new Predicate<AutoBase>() {
                    @Override
                    public boolean test(@NonNull AutoBase autoBase) throws Exception {
                        return autoBase.uniqueId().equals(4);
                    }
                })
                .assertValueAt(5, new Predicate<AutoBase>() {
                    @Override
                    public boolean test(@NonNull AutoBase autoBase) throws Exception {
                        return !autoBase.uniqueId().equals(5);
                    }
                })
                .assertValueCount(10)
                .assertComplete();
    }


}