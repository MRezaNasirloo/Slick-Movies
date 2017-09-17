package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;
import com.github.pedramrn.slick.parent.domain.model.MovieMetadataImpl;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.details.model.MovieBasic;
import com.github.pedramrn.slick.parent.ui.details.model.MovieSmall;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
public class MapperMovieMetadataToMovieBasic implements Function<MovieMetadata, MovieBasic> {

    @Inject
    public MapperMovieMetadataToMovieBasic() {
    }

    @Override
    public MovieBasic apply(@NonNull MovieMetadata mm) throws Exception {
        if (mm instanceof MovieMetadataImpl) {
            return MovieSmall.builder()
                    .id(mm.id())
                    .uniqueId(mm.id())
                    .imdbId(mm.imdbId())
                    .title(mm.title())
                    .year(mm.year())
                    .build();
        }
        else if (mm instanceof MovieDomain) { return Movie.create(((MovieDomain) mm)); }
        else {
            throw new IllegalStateException("Cannot map this type " + mm.getClass());
        }
    }
}
