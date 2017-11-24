package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.CommentTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktFull;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageFull;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface ApiTrakt {

    @GET("/movies/boxoffice")
    Observable<List<BoxOfficeItem>> boxOffice();

    @GET("/movies/anticipated?extended=full")
    Observable<AnticipatedTrakt> anticipated();

    @GET("/movies/anticipated")
    Observable<List<MovieTraktPageMetadata>> anticipatedMetadata();

    @GET("/movies/anticipated")
    Observable<List<MovieTraktPageMetadata>> anticipatedMetadata(@Query("page") int page, @Query("limit") int size);

    @GET("/movies/trending")
    Observable<List<MovieTraktPageMetadata>> trending(@Query("page") int page, @Query("limit") int size);

    @GET("/movies/trending?extended=full")
    Observable<List<MovieTraktPageFull>> trendingFull(@Query("page") int page, @Query("limit") int size);

    @GET("/movies/popular")
    Observable<List<MovieTraktMetadata>> popular(@Query("page") int page, @Query("limit") int size);

    @GET("/movies/{id}/ratings")
    Observable<Object> rating(@Path("id") int id);

    @GET("/movies/{id}?extended=full")
    Observable<MovieTraktFull> movie(@Path("id") String imdb);

    @GET("/movies/{id}/comments/likes")
    Observable<Response<List<CommentTrakt>>> comments(@Path("id") String imdb, @Query("page") int page, @Query("limit") int size);

    @GET("/movies/{id}/comments/likes?extended=full")
    Observable<Response<List<CommentTrakt>>> commentsFull(@Path("id") String imdb, @Query("page") int page, @Query("limit") int size);


}
