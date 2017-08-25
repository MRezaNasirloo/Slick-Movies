package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.PersonDetailsDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

public interface RouterPerson {

    /**
     * @param id person's id
     * @return details about the person requested with the given id
     */
    Observable<PersonDetailsDomain> person(int id);
}
