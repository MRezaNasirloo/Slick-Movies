package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface ApiTmdb {

    @GET("movie/{movie_id}?append_to_response=credits,images,videos")
    Observable<MovieTmdb> movieFull(@Path("movie_id") Integer id);

    @GET("movie/{movie_id}?append_to_response=")
    Observable<MovieTmdb> movie(@Path("movie_id") Integer id);

    @GET("movie/{movie_id}/videos?append_to_response=")
    Observable<MovieTmdb> withVideos(@Path("movie_id") Integer id);

    @GET("movie/{movie_id}/videos?append_to_response=")
    Observable<VideoTmdbResults> justVideos(@Path("movie_id") Integer id);
}
