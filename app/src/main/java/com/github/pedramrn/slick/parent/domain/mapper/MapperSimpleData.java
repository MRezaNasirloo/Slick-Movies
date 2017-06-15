package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.domain.SimpleData;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */

public class MapperSimpleData implements Function<SimpleData, String> {

    @Inject
    public MapperSimpleData() {
    }

    @Override
    public String apply(@NonNull SimpleData simpleData) throws Exception {
        return simpleData.get();
    }
}
