package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktFull;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktPageMetadata;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public class ApiTraktMock extends ApiMockBase<ApiTrakt> implements ApiTrakt {

    private List<MovieTraktPageMetadata> trendingList;
    private List<MovieTraktMetadata> popularList;

    public ApiTraktMock(BehaviorDelegate<ApiTrakt> delegate, Gson gson, List<MovieTraktPageMetadata> trendingList,
                        List<MovieTraktMetadata> popularList) {
        super(delegate, gson);
        this.trendingList = trendingList;
        this.popularList = popularList;
    }

    public ApiTraktMock(NetworkBehavior behavior, Gson gson, List<MovieTraktPageMetadata> trendingList, List<MovieTraktMetadata> popularList) {
        super(behavior, gson);
        this.trendingList = trendingList;
        this.popularList = popularList;
    }

    public ApiTraktMock(Gson gson, List<MovieTraktPageMetadata> trendingList, List<MovieTraktMetadata> popularList) {
        super(gson);
        this.trendingList = trendingList;
        this.popularList = popularList;
    }

    @Override
    protected Class<ApiTrakt> getApiClassType() {
        return ApiTrakt.class;
    }


    @Override
    public Observable<List<BoxOfficeItem>> get() {
        return Observable.never();
    }

    @Override
    public Observable<AnticipatedTrakt> anticipated() {
        return Observable.never();
    }

    @Override
    public Observable<List<MovieTraktPageMetadata>> anticipatedMetadata() {
        return Observable.never();
    }

    @Override
    public Observable<List<MovieTraktPageMetadata>> anticipatedMetadata(@Query("page") int page, @Query("limit") int size) {
        return Observable.never();
    }

    @Override
    public Observable<List<MovieTraktPageMetadata>> trending(@Query("page") int page, @Query("limit") int size) {
        if (page < 1) throw new IllegalArgumentException("Page should be a positive number");
        if (size < 1) throw new IllegalArgumentException("Size should be a positive number");
        List<MovieTraktPageMetadata> response = Observable.fromIterable(trendingList)
                .skip((page - 1) * size)
                .take(size)
                .toList()
                .blockingGet();
        return delegate.returningResponse(response).trending(page, size);
    }

    @Override
    public Observable<List<MovieTraktMetadata>> popular(@Query("page") int page, @Query("limit") int size) {
        if (page < 1) throw new IllegalArgumentException("Page should be a positive number");
        if (size < 1) throw new IllegalArgumentException("Size should be a positive number");
        List<MovieTraktMetadata> response = Observable.fromIterable(popularList)
                .skip((page - 1) * size)
                .take(size)
                .toList()
                .blockingGet();
        return delegate.returningResponse(response).popular(page, size);
    }

    @Override
    public Observable<Object> rating(@Path("id") int id) {
        return Observable.never();
    }

    @Override
    public Observable<MovieTraktFull> movie(@Path("id") String imdb) {
        return Observable.never();
    }
}
