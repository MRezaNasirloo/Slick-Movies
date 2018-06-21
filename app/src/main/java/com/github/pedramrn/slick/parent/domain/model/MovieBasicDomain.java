package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
public interface MovieBasicDomain extends MovieMetadata {

    List<String> genres();

    @Nullable
    String homepage();

    @Nullable
    String originalLanguage();

    @Nullable
    String overview();

    @Nullable
    String releaseDate();

    @Nullable
    Integer runtime();

    @Nullable
    String tagline();

    @Nullable
    Float voteAverageTrakt();

    @Nullable
    Integer voteCountTrakt();

    @Nullable
    String certification();
}
