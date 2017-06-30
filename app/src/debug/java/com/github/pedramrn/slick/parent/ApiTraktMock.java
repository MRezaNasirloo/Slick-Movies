package com.github.pedramrn.slick.parent;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.TraktPageMetadata;
import com.google.gson.Gson;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.NetworkBehavior;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-24
 */

public class ApiTraktMock extends ApiMockBase<ApiTrakt> implements ApiTrakt {

    private List<TraktPageMetadata> trendingList;

    public ApiTraktMock(BehaviorDelegate<ApiTrakt> delegate, Gson gson, List<TraktPageMetadata> trendingList) {
        super(delegate, gson);
        this.trendingList = trendingList;
    }

    public ApiTraktMock(NetworkBehavior behavior, Gson gson, List<TraktPageMetadata> trendingList) {
        super(behavior, gson);
        this.trendingList = trendingList;
    }

    public ApiTraktMock(Gson gson, List<TraktPageMetadata> trendingList) {
        super(gson);
        this.trendingList = trendingList;
    }

    @Override
    protected Class<ApiTrakt> getApiClassType() {
        return ApiTrakt.class;
    }


    @Override
    public Observable<List<BoxOfficeItem>> get() {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    public Observable<AnticipatedTrakt> anticipated() {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    public Observable<List<TraktPageMetadata>> anticipatedMetadata() {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    public Observable<List<TraktPageMetadata>> anticipatedMetadata(@Query("page") int page, @Query("limit") int size) {
        throw new NotImplementedException(new Throwable());
    }

    @Override
    public Observable<List<TraktPageMetadata>> trending(@Query("page") int page, @Query("limit") int size) {
        List<TraktPageMetadata> response = Observable.fromIterable(trendingList)
                .skip((page - 1) * size)
                .take(size)
                .toList()
                .blockingGet();
        return delegate.returningResponse(response).trending(page, size);
    }
}
