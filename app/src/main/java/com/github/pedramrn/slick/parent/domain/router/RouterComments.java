package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.CommentDomain;
import com.github.pedramrn.slick.parent.domain.model.PagedDomain;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-23
 */

public interface RouterComments {
    Observable<PagedDomain<CommentDomain>> comments(String imdb);
}
