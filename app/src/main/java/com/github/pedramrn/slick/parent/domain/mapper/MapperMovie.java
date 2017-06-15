package com.github.pedramrn.slick.parent.domain.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.tmdb.MovieTmdb;
import com.github.pedramrn.slick.parent.domain.model.CastDomain;
import com.github.pedramrn.slick.parent.domain.model.ImageDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDetails;

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

        List<String> backdrops = Observable.fromIterable(movieTmdb.images().backdrops())
                .map(mapperData)
                .toList(/*TODO null check*/movieTmdb.images().backdrops().size())
                .blockingGet();
        List<String> posters = Observable.fromIterable(movieTmdb.images().posters())
                .map(mapperData)
                .toList(/*TODO null check*/movieTmdb.images().posters().size())
                .blockingGet();
        List<String> genres = Observable.fromIterable(movieTmdb.genres())
                .map(mapperData)
                .toList(/*TODO null check*/movieTmdb.genres().size())
                .blockingGet();
        List<String> productionCompanies = Observable.fromIterable(movieTmdb.productionCompanies())
                .map(mapperData)
                .toList(/*TODO null check*/movieTmdb.productionCompanies().size())
                .blockingGet();
        List<String> productionCountries = Observable.fromIterable(movieTmdb.productionCountries())
                .map(mapperData)
                .toList(/*TODO null check*/movieTmdb.productionCountries().size())
                .blockingGet();
        List<String> spokenLanguages = Observable.fromIterable(movieTmdb.spokenLanguages())
                .map(mapperData)
                .toList(/*TODO null check*/movieTmdb.spokenLanguages().size())
                .blockingGet();

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
                ImageDomain.create(movieTmdb.images().id(), backdrops, posters));
    }
}
