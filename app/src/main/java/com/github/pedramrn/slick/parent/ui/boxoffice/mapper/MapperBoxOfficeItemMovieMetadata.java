package com.github.pedramrn.slick.parent.ui.boxoffice.mapper;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.trakt.MovieTraktMetadata;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadataImpl;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-24
 */
public class MapperBoxOfficeItemMovieMetadata implements Function<BoxOfficeItem, MovieMetadataImpl> {

    @Inject
    public MapperBoxOfficeItemMovieMetadata() {
    }

    @Override
    public MovieMetadataImpl apply(BoxOfficeItem boi) throws Exception {
        MovieTraktMetadata movie = boi.movie();
        return MovieMetadataImpl.create(movie.ids().tmdb(), movie.ids().imdb(), movie.title(), movie.year());
    }
}
