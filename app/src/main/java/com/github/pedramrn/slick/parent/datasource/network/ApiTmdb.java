package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface ApiTmdb {

    @GET("/movie/{movie_id}")
    Observable<MovieTmdb> get(@Path("movie_id") String id);
}
