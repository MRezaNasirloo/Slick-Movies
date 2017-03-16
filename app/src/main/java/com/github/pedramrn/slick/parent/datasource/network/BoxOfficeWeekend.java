package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-03-09
 */

public interface BoxOfficeWeekend {

//    @GET("/?i=tt0944947&Season=1")
//    @GET("movie/550?api_key=413d5af6c55f8b73b74d947fa6542ba1")
    @GET("/movies/boxoffice")
//    @GET("/search/repositories?q=butterknife+language:java&sort=stars&order=desc")
    Observable<List<BoxOfficeItem>> get();
}
