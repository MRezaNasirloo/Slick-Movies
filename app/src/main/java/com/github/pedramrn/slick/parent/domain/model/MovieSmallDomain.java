package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */
@AutoValue
public abstract class MovieSmallDomain {

    public abstract Integer id();

    public abstract String title();

    @Nullable
    public abstract String overview();

    public abstract String originalTitle();

    @Nullable
    public abstract String posterPath();

    @Nullable
    public abstract String backdropPath();

    @Nullable
    public abstract String releaseDate();

    public abstract String originalLanguage();

    public abstract Boolean adult();

    public abstract Float popularity();

    public abstract Boolean video();

    public abstract Float voteAverageTmdb();

    public abstract Integer voteCountTmdb();

    public abstract Builder toBuilder();

    public static MovieSmallDomain create(Integer id, String title, String overview, String originalTitle, String posterPath, String backdropPath,
                                          String releaseDate, String originalLanguage, Boolean adult, Float popularity, Boolean video,
                                          Float voteAverageTmdb, Integer voteCountTmdb) {
        return builder()
                .id(id)
                .title(title)
                .overview(overview)
                .originalTitle(originalTitle)
                .posterPath(posterPath)
                .backdropPath(backdropPath)
                .releaseDate(releaseDate)
                .originalLanguage(originalLanguage)
                .adult(adult)
                .popularity(popularity)
                .video(video)
                .voteAverageTmdb(voteAverageTmdb)
                .voteCountTmdb(voteCountTmdb)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_MovieSmallDomain.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder adult(Boolean adult);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder originalLanguage(String originalLanguage);

        public abstract Builder originalTitle(String originalTitle);

        public abstract Builder overview(String overview);

        public abstract Builder popularity(Float popularity);

        public abstract Builder posterPath(String posterPath);

        public abstract Builder releaseDate(String releaseDate);

        public abstract Builder title(String title);

        public abstract Builder video(Boolean video);

        public abstract Builder voteAverageTmdb(Float voteAverageTmdb);

        public abstract Builder voteCountTmdb(Integer voteCountTmdb);

        public abstract MovieSmallDomain build();
    }
}
