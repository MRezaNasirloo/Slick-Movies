package com.github.pedramrn.slick.parent.ui.details.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovieSmall;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-15
 */
public class RouterSimilarImplTest {
    private ApiTmdb apiTmdb;
    private RouterSimilarImpl routerSimilar;

    @Before
    public void setUp() throws Exception {
        ModuleNetwork moduleNetwork = new ModuleNetwork();
        Gson gson = moduleNetwork.baseGsonConverterFactory();
        apiTmdb = moduleNetwork.apiTmdb(moduleNetwork.apiUrlTmdb(), moduleNetwork.baseOkHttpClient(moduleNetwork.interceptors()),
                moduleNetwork.baseRetrofit(gson), gson, null, null);
        routerSimilar = new RouterSimilarImpl(apiTmdb, new MapperMovieSmall());
    }


    @Test
    public void testSimilar() throws Exception {
        routerSimilar.similar(297762, 1).test()
                .assertNoErrors()
                .assertComplete();

    }
}