package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieSmallDomain;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */

public interface RouterSimilar {
    Observable<List<MovieSmallDomain>> similar(int id, int page);
}
