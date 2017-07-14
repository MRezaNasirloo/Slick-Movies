package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Backdrop;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.CastTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Credit;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Genre;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ImageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Poster;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ProductionCompany;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ProductionCountry;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.SpokenLanguage;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.VideoTmdbResults;
import com.github.pedramrn.slick.parent.domain.model.CastDomain;
import com.github.pedramrn.slick.parent.domain.model.ImageDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-15
 */
public class MapperMovie implements Function<MovieTmdb, MovieDomain> {
    private final MapperCast mapperCast;
    private final MapperSimpleData mapperData;

    @Inject
    public MapperMovie(MapperCast mapperCast, MapperSimpleData mapperData) {
        this.mapperCast = mapperCast;
        this.mapperData = mapperData;
    }

    @Override
    public MovieDomain apply(@NonNull final MovieTmdb mt) throws Exception {

        Credit credits = mt.credits();
        ImageTmdb images = mt.images();
        List<String> backdrops = Collections.emptyList();
        List<String> posters = Collections.emptyList();
        List<String> genres = Collections.emptyList();
        List<String> productionCompanies = Collections.emptyList();
        List<String> productionCountries = Collections.emptyList();
        List<String> spokenLanguages = Collections.emptyList();
        List<CastDomain> castDomains = Collections.emptyList();
        List<VideoDomain> videosDomains = Collections.emptyList();

        if (images != null) {
            List<Backdrop> backdropsTemp = images.backdrops();
            int size = backdropsTemp.size();
            backdrops = Observable.fromIterable(backdropsTemp)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
            List<Poster> postersTemp = images.posters();
            size = postersTemp.size();
            posters = Observable.fromIterable(postersTemp)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        if (credits != null) {
            List<CastTmdb> castTmdb = credits.cast();
            int size = castTmdb.size();
            castDomains = Observable.fromIterable(castTmdb)
                    .map(mapperCast)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<Genre> genresTemp = mt.genres();
        if (genresTemp != null) {
            int size = genresTemp.size();
            genres = Observable.fromIterable(genresTemp)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<ProductionCompany> companies = mt.productionCompanies();
        if (companies != null) {
            int size = companies.size();
            productionCompanies = Observable.fromIterable(companies)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<ProductionCountry> countries = mt.productionCountries();
        if (countries != null) {
            int size = countries.size();
            productionCountries = Observable.fromIterable(countries)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<SpokenLanguage> languages = mt.spokenLanguages();
        if (languages != null) {
            int size = languages.size();
            spokenLanguages = Observable.fromIterable(languages)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        VideoTmdbResults videos = mt.videos();
        if (videos != null && videos.results() != null) {
            List<VideoTmdb> videoTmdb = videos.results();
            int size = videoTmdb.size();
            videosDomains = Observable.fromIterable(videoTmdb).map(new Function<VideoTmdb, VideoDomain>() {
                @Override
                public VideoDomain apply(@NonNull VideoTmdb vt) throws Exception {
                    return VideoDomain.create(mt.id(), vt.type(), vt.key(), vt.name());
                }
            }).toList(size == 0 ? 1 : size).blockingGet();
        }




        return MovieDomain.create(mt.id(),
                mt.imdbId(),
                mt.adult(),
                mt.backdropPath(),
                mt.belongsToCollection(),
                mt.budget(),
                genres,
                mt.homepage(),
                mt.originalLanguage(),
                mt.originalTitle(),
                mt.overview(),
                mt.popularity(),
                mt.posterPath(),
                productionCompanies,
                productionCountries,
                mt.releaseDate(),
                mt.revenue(),
                mt.runtime(),
                spokenLanguages,
                mt.status(),
                mt.tagline(),
                mt.title(),
                mt.video(),
                mt.voteAverage(),
                mt.voteCount(),
                null,
                null,
                null,
                castDomains,
                ImageDomain.create(backdrops, posters),
                videosDomains);
    }
}
