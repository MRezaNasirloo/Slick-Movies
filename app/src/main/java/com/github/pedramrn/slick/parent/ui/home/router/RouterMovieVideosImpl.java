package com.github.pedramrn.slick.parent.ui.home.router;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.domain.router.RouterMovieVideos;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */

public class RouterMovieVideosImpl implements RouterMovieVideos {

    private final ApiTmdb apiTmdb;

    @Inject
    public RouterMovieVideosImpl(ApiTmdb apiTmdb) {
        this.apiTmdb = apiTmdb;
    }

    @Override
    public Observable<List<VideoDomain>> get(final Integer tmdbId) {
        return apiTmdb.justVideos(tmdbId).map(new Function<VideoTmdbResults, List<VideoDomain>>() {
            @Override
            public List<VideoDomain> apply(@NonNull VideoTmdbResults videoTmdbResults) throws Exception {
                List<VideoTmdb> results = videoTmdbResults.results();
                int size = results.size();
                return Observable.fromIterable(results).map(new Function<VideoTmdb, VideoDomain>() {
                    @Override
                    public VideoDomain apply(@NonNull VideoTmdb vt) throws Exception {
                        return VideoDomain.create(tmdbId, vt.type(), vt.key(), vt.name());
                    }
                }).toList(size == 0 ? 1 : size).blockingGet();
            }
        });
    }
}
