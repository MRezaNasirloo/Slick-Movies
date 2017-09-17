package com.github.pedramrn.slick.parent.domain.router;

import android.support.annotation.IntRange;

import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public interface PagedRouter {
    Observable<MovieMetadata> page(
            @IntRange(from = 1, to = Short.MAX_VALUE) int page,
            @IntRange(from = 1, to = Short.MAX_VALUE) int size
    );
}
