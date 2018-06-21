package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.datasource.network.models.IpLocation;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 *         Created on: 2018-06-21
 */
public interface ApiIpLocation {

    @GET("/json")
    Observable<IpLocation> location();
}
