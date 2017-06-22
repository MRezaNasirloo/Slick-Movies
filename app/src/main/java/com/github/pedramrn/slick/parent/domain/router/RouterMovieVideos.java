package com.github.pedramrn.slick.parent.domain.router;

import com.github.pedramrn.slick.parent.domain.model.VideoDomain;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-02
 */

public interface RouterMovieVideos {

    Observable<List<VideoDomain>> get(Integer tmdbId);
}
