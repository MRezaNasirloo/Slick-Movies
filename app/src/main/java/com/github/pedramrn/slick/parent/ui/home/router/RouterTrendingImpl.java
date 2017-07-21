package com.github.pedramrn.slick.parent.ui.home.router;

import android.support.annotation.IntRange;

import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterTrending;
import com.github.pedramrn.slick.parent.ui.home.mapper.TransformerTraktToMovieDomain;

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
        return trending(1, 10);
    }

    @Override
    public Observable<MovieDomain> trending(@IntRange(from = 1, to = Long.MAX_VALUE) int page, int size) {
        if (page < 1) throw new IllegalArgumentException("Page should be a positive number");
        if (size < 1) throw new IllegalArgumentException("Size should be a positive number");
        return apiTrakt.trending(page, size).compose(transformer);
    }

}
