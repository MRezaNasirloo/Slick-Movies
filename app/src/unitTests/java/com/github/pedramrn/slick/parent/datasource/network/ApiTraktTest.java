package com.github.pedramrn.slick.parent.datasource.network;

import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-15
 */
public class ApiTraktTest {

    private ApiTmdb apiTmdb;

    @Before
    public void setUp() throws Exception {
        ModuleNetwork moduleNetwork = new ModuleNetwork();
        Gson gson = moduleNetwork.baseGsonConverterFactory();
        apiTmdb = moduleNetwork.apiTmdb(moduleNetwork.apiUrlTmdb(), moduleNetwork.baseOkHttpClient(moduleNetwork.interceptors()),
                moduleNetwork.baseRetrofit(gson), gson, null, null);
    }

    @Test
    public void test() {
        apiTmdb.movie(297762).test()
                .assertNoErrors()
                .assertComplete();

        apiTmdb.similar(297762, 1).test()
                .assertNoErrors()
                .assertComplete();
    }

}