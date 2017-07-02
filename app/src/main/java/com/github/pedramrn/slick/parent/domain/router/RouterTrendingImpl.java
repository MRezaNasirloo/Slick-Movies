package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.ui.home.TransformerTraktToMovieDomain;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-23
 */

public class RouterTrendingImpl implements RouterTrending {

    private final ApiTrakt apiTrakt;
    private TransformerTraktToMovieDomain transformer;

    @Inject
    public RouterTrendingImpl(ApiTrakt apiTrakt, TransformerTraktToMovieDomain transformer) {
        this.apiTrakt = apiTrakt;
        this.transformer = transformer;
    }

    @Override
    public Observable<MovieDomain> trending() {
        return apiTrakt.trending(1, 10).compose(transformer);
    }

    @Override
    public Observable<MovieDomain> trending(int page, int size) {
        return apiTrakt.trending(page, size).compose(transformer);
    }

}
