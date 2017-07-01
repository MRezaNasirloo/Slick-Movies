package com.github.pedramrn.slick.parent.ui.details.mapper;

import com.github.pedramrn.slick.parent.domain.model.CastDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.ui.details.model.Backdrop;
import com.github.pedramrn.slick.parent.ui.details.model.Cast;
import com.github.pedramrn.slick.parent.ui.details.model.Image;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.model.Video;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-01
 */
public class MovieDomainMovieMapper implements Function<MovieDomain, Movie> {

    @Inject
    public MovieDomainMovieMapper() {
    }

    @Override
    public Movie apply(@NonNull MovieDomain md) throws Exception {
        // TODO: 2017-06-20 Extract mapper
        //casts
        List<CastDomain> castDomains = md.casts();
        int size = castDomains.size();
        List<Cast> casts = Observable.fromIterable(castDomains).map(new Function<CastDomain, Cast>() {
            @Override
            public Cast apply(@NonNull CastDomain cd) throws Exception {
                return Cast.create(cd.id(), cd.castId(), cd.creditId(), cd.name(),
                        cd.profilePath(), cd.character(), cd.gender(), cd.order());
            }
        }).toList(size == 0 ? 1 : size).blockingGet();

        //backdrops
        List<String> backdropsDomain = md.images().backdrops();
        size = backdropsDomain.size();
        final List<Backdrop> backdrops = Observable.fromIterable(backdropsDomain).map(new Function<String, Backdrop>() {
            @Override
            public Backdrop apply(@NonNull String s) throws Exception {
                return Backdrop.create(s);
            }
        }).toList(size == 0 ? 1 : size).blockingGet();

        //videos
        List<VideoDomain> videosDomain = md.videos();
        size = videosDomain.size();
        final List<Video> videos = Observable.fromIterable(videosDomain).map(new Function<VideoDomain, Video>() {
            @Override
            public Video apply(@NonNull VideoDomain vd) throws Exception {
                return Video.create(vd.tmdb(), vd.type(), vd.key(), vd.name());
            }
        }).toList(size == 0 ? 1 : size).blockingGet();


        //image
        Image image = Image.create(backdrops, md.images().posters());

        return Movie.create(md.id(), md.imdbId(), md.adult(), md.backdropPath(), md.budget(), md.genres(), md.homepage(),
                md.overview(), md.popularity(), md.posterPath(), md.productionCompanies(), md.productionCountries(),
                md.releaseDate(), md.revenue(), md.runtime(), md.spokenLanguages(), md.status(), md.tagline(), md.title(),
                md.video(), md.voteAverage(), md.voteCount(), casts, image, videos);
    }
}
