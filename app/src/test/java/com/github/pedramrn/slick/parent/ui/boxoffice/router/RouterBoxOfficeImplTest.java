package com.github.pedramrn.slick.parent.ui.boxoffice.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-15
 */
public class RouterBoxOfficeImplTest {

    @Mock
    ApiTrakt apiTrakt;
    @Mock
    ApiOmdb apiOmdb;

    private RouterBoxOfficeImpl routerBoxOffice;
    private Type type = new TypeToken<List<BoxOfficeItem>>() {}.getType();

    @Before
    public void setUp() throws Exception {
        final InputStream inputStreamTrakt = ClassLoader.getSystemResourceAsStream("api_trakt.json");
        final InputStream inputStreamOmdb = ClassLoader.getSystemResourceAsStream("api_omdb.json");

        final Gson gson = new Gson();
        final List<BoxOfficeItem> boxOfficeItems = gson.fromJson(new JsonReader(new InputStreamReader(inputStreamTrakt)), type);
        final MovieOmdb movieOmdb = gson.fromJson(new JsonReader(new InputStreamReader(inputStreamOmdb)), MovieOmdb.class);

        MockitoAnnotations.initMocks(this);
        Mockito.when(apiTrakt.get()).thenReturn(Observable.just(boxOfficeItems));
        Mockito.when(apiOmdb.get(Mockito.anyString())).thenReturn(Observable.just(movieOmdb));

        routerBoxOffice = new RouterBoxOfficeImpl(apiTrakt, apiOmdb);
    }

    @Test
    public void boxOffice() throws Exception {
        routerBoxOffice.boxOffice(Observable.just(1, 2), 2).test()
                .await()
                .assertSubscribed()
                .assertValueCount(4)
                .assertComplete();
    }

}