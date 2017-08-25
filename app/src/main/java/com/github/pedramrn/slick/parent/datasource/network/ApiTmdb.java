package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MoviePageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonPageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.PersonTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface ApiTmdb {

    @GET("movie/{movie_id}?append_to_response=credits,images,videos")
    Observable<MovieTmdb> movieFull(@Path("movie_id") Integer id);

    @GET("movie/{movie_id}?append_to_response=")
    Observable<MovieTmdb> movie(@Path("movie_id") Integer id);

    @GET("movie/{movie_id}/similar")
    Observable<MoviePageTmdb> similar(@Path("movie_id") Integer id, @Query("page") int page);

    @GET("movie/upcoming")
    Observable<MoviePageTmdb> upcoming(@Query("page") int page);

    @GET("movie/now_playing")
    Observable<MoviePageTmdb> nowPlaying(@Query("page") int page);

    @GET("movie/{movie_id}/videos?append_to_response=")
    Observable<MovieTmdb> withVideos(@Path("movie_id") Integer id);

    @GET("movie/{movie_id}/videos?append_to_response=")
    Observable<VideoTmdbResults> justVideos(@Path("movie_id") Integer id);

    @GET("search/movie")
    Observable<MoviePageTmdb> searchMovie(@Query("query") String query, @Query("page") int page);

    @GET("search/person")
    Observable<PersonPageTmdb> searchPerson(@Query("query") String query, @Query("page") int page);

    @GET("person/{id}?append_to_response=")
    Observable<PersonTmdb> personDetails(@Path("id") int id);

}
