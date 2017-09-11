package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
@AutoValue
public abstract class MovieTinyDomain implements MovieBasicDomain {

    public abstract Integer id();

    @Nullable
    public abstract String imdbId();

    public abstract String title();

    @Nullable
    public abstract String overview();

    @Nullable
    public abstract String homepage();

    @Nullable
    public abstract String originalLanguage();

    @Nullable
    public abstract String releaseDate();

    @Nullable
    public abstract Integer runtime();

    @Nullable
    public abstract String tagline();

    public abstract List<String> genres();

    @Nullable
    public abstract Float voteAverageTrakt();

    @Nullable
    public abstract Integer voteCountTrakt();

    @Nullable
    public abstract String certification();

    public static MovieTinyDomain create(
            Integer id,
            String imdbId,
            Integer year,
            List<String> genres,
            String homepage,
            String originalLanguage,
            String overview,
            String releaseDate,
            Integer runtime,
            String tagline,
            String title,
            Float voteAverageTrakt,
            Integer voteCountTrakt,
            String certification
    ) {
        return builder()
                .id(id)
                .imdbId(imdbId)
                .year(year)
                .genres(genres)
                .homepage(homepage)
                .originalLanguage(originalLanguage)
                .overview(overview)
                .releaseDate(releaseDate)
                .runtime(runtime)
                .tagline(tagline)
                .title(title)
                .voteAverageTrakt(voteAverageTrakt)
                .voteCountTrakt(voteCountTrakt)
                .certification(certification)
                .build();
    }

    public static Builder builder() {return new AutoValue_MovieTinyDomain.Builder();}


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder imdbId(String imdbId);

        public abstract Builder genres(List<String> genres);

        public abstract Builder homepage(String homepage);

        public abstract Builder originalLanguage(String originalLanguage);

        public abstract Builder overview(String overview);

        public abstract Builder releaseDate(String releaseDate);

        public abstract Builder runtime(Integer runtime);

        public abstract Builder tagline(String tagline);

        public abstract Builder title(String title);

        public abstract Builder voteAverageTrakt(Float voteAverageTrakt);

        public abstract Builder voteCountTrakt(Integer voteCountTrakt);

        public abstract Builder certification(String certification);

        public abstract Builder year(Integer year);

        public abstract MovieTinyDomain build();
    }
}
