package com.github.pedramrn.slick.parent.ui.details.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.CommentTrakt;
import com.github.pedramrn.slick.parent.domain.mapper.MapperCommentTraktCommentDomain;
import com.github.pedramrn.slick.parent.domain.model.CommentDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterComments;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Headers;
import retrofit2.Response;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

public class RouterCommentsImpl implements RouterComments {

    private final ApiTrakt apiTrakt;

    @Inject
    public RouterCommentsImpl(ApiTrakt apiTrakt) {
        this.apiTrakt = apiTrakt;
    }

    @Override
    public Observable<PagedDomain<CommentDomain>> comments(String imdb, int page, int size) {
        final PagedDomain.Builder<CommentDomain> builder = PagedDomain.builder();
        return apiTrakt.comments(imdb, 1, 10)
                .flatMap(new Function<Response<List<CommentTrakt>>, ObservableSource<CommentTrakt>>() {
                    @Override
                    public ObservableSource<CommentTrakt> apply(@NonNull Response<List<CommentTrakt>> listResponse) throws Exception {
                        Headers headers = listResponse.headers();
                        // builder.headers.get("x-pagination-limit");
                        builder.count(Integer.valueOf(headers.get("x-pagination-item-count")));
                        builder.pages(Integer.valueOf(headers.get("x-pagination-page-count")));
                        builder.page(Integer.valueOf(headers.get("x-pagination-page")));
                        return Observable.fromIterable(listResponse.body());
                    }
                })
                .map(new MapperCommentTraktCommentDomain())
                .toList()
                .map(new Function<List<CommentDomain>, PagedDomain<CommentDomain>>() {
                    @Override
                    public PagedDomain<CommentDomain> apply(@NonNull List<CommentDomain> commentDomains) throws Exception {
                        return builder.data(commentDomains).build();
                    }
                })
                .toObservable();
    }


}
