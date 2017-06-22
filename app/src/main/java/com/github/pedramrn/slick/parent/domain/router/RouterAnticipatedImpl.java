package com.github.pedramrn.slick.parent.domain.router;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.AnticipatedTraktMetadata;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktFull;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.model.MovieDetails;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */

public class RouterAnticipatedImpl implements RouterAnticipated {


    private final ApiTrakt apiTrakt;
    private final ApiTmdb apiTmdb;
    private final MapperMovie mapper;

    @Inject
    public RouterAnticipatedImpl(ApiTrakt apiTrakt, ApiTmdb apiTmdb, MapperMovie mapper) {
        this.apiTrakt = apiTrakt;
        this.apiTmdb = apiTmdb;
        this.mapper = mapper;
    }

    @Override
    public Observable<MovieTraktFull> anticipated() {
        return apiTrakt.anticipated().flatMap(new Function<AnticipatedTrakt, Observable<MovieTraktFull>>() {
            @Override
            public Observable<MovieTraktFull> apply(@NonNull AnticipatedTrakt at) throws Exception {
                return Observable.fromIterable(at.movie());
            }
        });
    }


    @Override
    public Observable<MovieDetails> anticipated2() {
        return apiTrakt.anticipated().flatMap(new Function<AnticipatedTrakt, Observable<MovieTraktFull>>() {
            @Override
            public Observable<MovieTraktFull> apply(@NonNull AnticipatedTrakt at) throws Exception {
                return Observable.fromIterable(at.movie());
            }
        }).concatMap(new Function<MovieTraktFull, ObservableSource<? extends MovieDetails>>() {
            @Override
            public ObservableSource<? extends MovieDetails> apply(@io.reactivex.annotations.NonNull MovieTraktFull movieTraktFull) throws Exception {
                return apiTmdb.withVideos(movieTraktFull.ids().tmdb()).map(mapper);
            }
        });
    }

    @Override
    public Observable<VideoDomain> anticipated3() {
        return apiTrakt.anticipatedMetadata().flatMap(new Function<List<AnticipatedTraktMetadata>, Observable<MovieTraktMetadata>>() {
            @Override
            public Observable<MovieTraktMetadata> apply(@NonNull List<AnticipatedTraktMetadata> at) throws Exception {
                return Observable.fromIterable(at).map(new Function<AnticipatedTraktMetadata, MovieTraktMetadata>() {
                    @Override
                    public MovieTraktMetadata apply(@NonNull AnticipatedTraktMetadata atm) throws Exception {
                        return atm.movie();
                    }
                });
            }
        }).concatMap(new Function<MovieTraktMetadata, Observable<VideoDomain>>() {
            @Override
            public Observable<VideoDomain> apply(@NonNull final MovieTraktMetadata mtf) throws Exception {
                return apiTmdb.justVideos(mtf.ids().tmdb()).flatMap(new Function<VideoTmdbResults, Observable<VideoDomain>>() {
                    @Override
                    public Observable<VideoDomain> apply(@NonNull VideoTmdbResults vtr) throws Exception {
                        return Observable.fromIterable(vtr.results()).map(new Function<VideoTmdb, VideoDomain>() {
                            @Override
                            public VideoDomain apply(@NonNull VideoTmdb vt) throws Exception {
                                return VideoDomain.create(mtf.ids().tmdb(), vt.type(), vt.key(), vt.name());
                            }
                        });
                    }
                });
            }
        });
    }
}
