package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface ApiOmdb {

    @GET("/")
    Observable<MovieOmdb> get(@Query("i") String id);
}
