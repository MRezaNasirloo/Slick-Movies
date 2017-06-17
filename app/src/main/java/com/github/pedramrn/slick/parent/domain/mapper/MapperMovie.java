package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Backdrop;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Genre;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ImageTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.Poster;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ProductionCompany;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.ProductionCountry;
import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.SpokenLanguage;
import com.github.pedramrn.slick.parent.domain.model.CastDomain;
import com.github.pedramrn.slick.parent.domain.model.ImageDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDetails;

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
public class MapperMovie implements Function<MovieTmdb, MovieDetails> {
    private final MapperCast mapperCast;
    private final MapperSimpleData mapperData;

    @Inject
    public MapperMovie(MapperCast mapperCast, MapperSimpleData mapperData) {
        this.mapperCast = mapperCast;
        this.mapperData = mapperData;
    }

    @Override
    public MovieDetails apply(@NonNull MovieTmdb movieTmdb) throws Exception {

        List<CastDomain> castDomains = Observable.fromIterable(movieTmdb.credits().cast())
                .map(mapperCast)
                .toList(/*TODO: check null*/movieTmdb.credits().cast().size())
                .blockingGet();

        ImageTmdb images = movieTmdb.images();
        List<String> backdrops = Collections.emptyList();
        List<String> posters = Collections.emptyList();
        List<String> genres = Collections.emptyList();
        List<String> productionCompanies = Collections.emptyList();
        List<String> productionCountries = Collections.emptyList();
        List<String> spokenLanguages = Collections.emptyList();

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

        List<Genre> genresTemp = movieTmdb.genres();
        if (genresTemp != null) {
            int size = genresTemp.size();
            genres = Observable.fromIterable(genresTemp)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<ProductionCompany> companies = movieTmdb.productionCompanies();
        if (companies != null) {
            int size = companies.size();
            productionCompanies = Observable.fromIterable(companies)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<ProductionCountry> countries = movieTmdb.productionCountries();
        if (countries != null) {
            int size = countries.size();
            productionCountries = Observable.fromIterable(countries)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        List<SpokenLanguage> languages = movieTmdb.spokenLanguages();
        if (languages != null) {
            int size = languages.size();
            spokenLanguages = Observable.fromIterable(languages)
                    .map(mapperData)
                    .toList(size == 0 ? 1 : size)
                    .blockingGet();
        }

        return MovieDetails.create(movieTmdb.id(), movieTmdb.imdbId(), movieTmdb.adult(), movieTmdb.backdropPath(),
                movieTmdb.belongsToCollection(), movieTmdb.budget(),
                genres,
                movieTmdb.homepage(), movieTmdb.originalLanguage(),
                movieTmdb.originalTitle(), movieTmdb.overview(), movieTmdb.popularity(), movieTmdb.posterPath(),
                productionCompanies,
                productionCountries,
                movieTmdb.releaseDate(), movieTmdb.revenue(), movieTmdb.runtime(),
                spokenLanguages,
                movieTmdb.status(), movieTmdb.tagline(),
                movieTmdb.title(), movieTmdb.video(), movieTmdb.voteAverage(), movieTmdb.voteCount(), castDomains,
                ImageDomain.create(backdrops, posters));
    }
}
