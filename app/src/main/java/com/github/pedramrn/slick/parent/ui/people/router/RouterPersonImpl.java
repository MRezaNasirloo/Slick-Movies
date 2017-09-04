package com.github.pedramrn.slick.parent.ui.people.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.domain.model.PersonDetailsDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterPerson;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public class RouterPersonImpl implements RouterPerson {

    private final ApiTmdb apiTmdb;
    private final MapperPersonTmdbPersonDetailsDomain mapper;

    @Inject
    public RouterPersonImpl(ApiTmdb apiTmdb, MapperPersonTmdbPersonDetailsDomain mapper) {
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<PersonDetailsDomain> person(int id) {
        return apiTmdb.personDetails(id).map(mapper);
    }

    public Observable<PersonDetailsDomain> works(int id) {
        return apiTmdb.personDetailsWithCredits(id).map(mapper);
    }
}
