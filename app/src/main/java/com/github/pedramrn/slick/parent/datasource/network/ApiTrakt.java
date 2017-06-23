package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTraktMetadata;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface ApiTrakt {

    @GET("/movies/boxoffice")
    Observable<List<BoxOfficeItem>> get();

    @GET("/movies/anticipated?extended=full")
    Observable<AnticipatedTrakt> anticipated();

    @GET("/movies/anticipated")
    Observable<List<AnticipatedTraktMetadata>> anticipatedMetadata();

    @GET("/movies/anticipated")
    Observable<List<AnticipatedTraktMetadata>> anticipatedMetadata(@Query("page") int page, @Query("limit") int size);
}
