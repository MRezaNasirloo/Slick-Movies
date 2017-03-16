package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface OmdbClient {

    @GET("/movies/boxoffice")
    Observable<List<BoxOfficeItem>> get();
}
