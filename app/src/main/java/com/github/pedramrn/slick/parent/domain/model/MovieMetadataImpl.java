package com.github.pedramrn.slick.parent.domain.model;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
@AutoValue
public abstract class MovieMetadataImpl implements MovieMetadata {
    public static MovieMetadataImpl create(
            Integer id,
            String imdbId,
            String title,
            Integer year
    ) {return new AutoValue_MovieMetadataImpl(id, imdbId, title, year);}


}
