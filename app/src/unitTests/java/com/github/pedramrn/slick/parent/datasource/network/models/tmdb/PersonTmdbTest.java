package com.github.pedramrn.slick.parent.datasource.network.models.tmdb;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.interceptor.InterceptorAuthToken;
import com.github.pedramrn.slick.parent.di.ModuleNetwork;
import com.github.pedramrn.slick.parent.di.ModuleNetworkBase;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-27
 */
public class PersonTmdbTest {

    @Rule public TemporaryFolder folder = new TemporaryFolder();
    private ApiTmdb apiTmdb;

    @Before
    public void setUp() throws Exception {
        File file = folder.newFile();
        file.deleteOnExit();
        ModuleNetworkBase moduleNetwork = new ModuleNetwork();
        Gson gson = moduleNetwork.baseGsonConverterFactory();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        apiTmdb = moduleNetwork.baseApiTmdb(moduleNetwork.baseApiUrlTmdb(),
                moduleNetwork.baseOkHttpClient(
                        Arrays.asList(loggingInterceptor, new InterceptorAuthToken()),
                        Collections.<Interceptor>emptyList(),
                        new Cache(file, 10 * 1024)),
                moduleNetwork.baseRetrofit(gson),
                gson);
    }

    @Test
    public void test() throws Exception {
        List<PersonTmdb> list = apiTmdb.personDetailsWithCredits(4491).test()
                .assertNoErrors()
                .assertComplete()
                .values();
    }

    @Test
    public void images() throws Exception {
        List<PersonTmdb> list = apiTmdb.personDetails(4491).test()
                .assertNoErrors()
                .assertComplete()
                .values();
    }
}