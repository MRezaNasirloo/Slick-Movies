package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-14
 */
public interface MovieBasic extends Parcelable {
    Integer id();

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

    Float voteAverageTmdb();

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
}
