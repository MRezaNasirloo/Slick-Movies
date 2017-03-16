package com.github.pedramrn.slick.parent.datasource.database;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-12
 */

public interface VoteRepository {
    String voteUp(String id);
    Observable<String> voteDown(String id);
    Completable deleteVote(String id);
}
