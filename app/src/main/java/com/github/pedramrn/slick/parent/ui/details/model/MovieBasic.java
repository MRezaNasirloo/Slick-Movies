package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.domain.model.MovieMetadata;

import java.util.List;
import java.util.Locale;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */
public interface MovieBasic extends Parcelable, MovieMetadata {
    Integer id();

    @Nullable
    String imdbId();

    String title();

    @Nullable
    String overview();

    @Nullable
    String posterPath();

    @Nullable
    String thumbnailPoster();

    @Nullable
    String thumbnailTinyPoster();

    @Nullable
    String backdropPath();

    @Nullable
    String releaseDate();

    List<String> genres();

    @Nullable
    Float voteAverageTmdb();

    @Nullable
    Integer voteCountTmdb();

    @Nullable
    Float voteAverageTrakt();

    @Nullable
    Integer voteCountTrakt();

    @Nullable
    String certification();

    @Nullable
    String thumbnailBackdrop();

    String runtimePretty();

    default String rank(int position) {
        return String.format(Locale.getDefault(), "#%s", position + 1);
    }
}
