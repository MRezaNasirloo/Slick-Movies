package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.CastTmdb;
import com.github.pedramrn.slick.parent.domain.model.CastDomain;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */
public class MapperCast implements Function<CastTmdb, CastDomain> {

    @Inject
    public MapperCast() {
    }

    @Override
    public CastDomain apply(@NonNull CastTmdb castTmdb) throws Exception {
        return CastDomain.create(castTmdb.id(), castTmdb.castId(), castTmdb.creditId(), castTmdb.name(), castTmdb.profilePath(),
                castTmdb.character(), castTmdb.gender(), castTmdb.order());
    }
}
