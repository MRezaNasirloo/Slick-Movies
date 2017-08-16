package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */
@AutoValue
public abstract class MovieDomain {

    public abstract Integer id();

    @Nullable
    public abstract String imdbId();

    public abstract Boolean adult();

    @Nullable
    public abstract String backdropPath();

    // public abstract Object belongsToCollection();

    @Nullable
    public abstract Integer budget();

    public abstract List<String> genres();

    @Nullable
    public abstract String homepage();

    @Nullable
    public abstract String originalLanguage();

    @Nullable
    public abstract String originalTitle();

    @Nullable
    public abstract String overview();

    @Nullable
    public abstract Float popularity();

    @Nullable
    public abstract String posterPath();

    public abstract List<String> productionCompanies();

    public abstract List<String> productionCountries();

    @Nullable
    public abstract String releaseDate();

    @Nullable
    public abstract Long revenue();

    @Nullable
    public abstract Integer runtime();

    public abstract List<String> spokenLanguages();

    @Nullable
    public abstract String status();

    @Nullable
    public abstract String tagline();

    public abstract String title();

    public abstract Boolean video();

    public abstract Float voteAverageTmdb();

    public abstract Integer voteCountTmdb();

    @Nullable
    public abstract Float voteAverageTrakt();

    @Nullable
    public abstract Integer voteCountTrakt();

    @Nullable
    public abstract String certification();

    public abstract List<CastDomain> casts();

    public abstract ImageDomain images();

    public abstract List<VideoDomain> videos();

    public abstract Builder toBuilder();

    public static MovieDomain create(Integer id, String imdbId, Boolean adult, String backdropPath, Integer budget, List<String> genres,
                                     String homepage,
                                     String originalLanguage, String originalTitle, String overview, Float popularity, String posterPath,
                                     List<String> productionCompanies, List<String> productionCountries, String releaseDate, Long revenue,
                                     Integer runtime, List<String> spokenLanguages, String status, String tagline, String title, Boolean video,
                                     Float voteAverageTmdb, Integer voteCountTmdb, Float voteAverageTrakt, Integer voteCountTrakt,
                                     String certification,
                                     List<CastDomain> casts, ImageDomain images, List<VideoDomain> videos) {
        return builder()
                .id(id)
                .imdbId(imdbId)
                .adult(adult)
                .backdropPath(backdropPath)
                .budget(budget)
                .genres(genres)
                .homepage(homepage)
                .originalLanguage(originalLanguage)
                .originalTitle(originalTitle)
                .overview(overview)
                .popularity(popularity)
                .posterPath(posterPath)
                .productionCompanies(productionCompanies)
                .productionCountries(productionCountries)
                .releaseDate(releaseDate)
                .revenue(revenue)
                .runtime(runtime)
                .spokenLanguages(spokenLanguages)
                .status(status)
                .tagline(tagline)
                .title(title)
                .video(video)
                .voteAverageTmdb(voteAverageTmdb)
                .voteCountTmdb(voteCountTmdb)
                .voteAverageTrakt(voteAverageTrakt)
                .voteCountTrakt(voteCountTrakt)
                .certification(certification)
                .casts(casts)
                .images(images)
                .videos(videos)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_MovieDomain.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder imdbId(String imdbId);

        public abstract Builder adult(Boolean adult);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder budget(Integer budget);

        public abstract Builder genres(List<String> genres);

        public abstract Builder homepage(String homepage);

        public abstract Builder originalLanguage(String originalLanguage);

        public abstract Builder originalTitle(String originalTitle);

        public abstract Builder overview(String overview);

        public abstract Builder popularity(Float popularity);

        public abstract Builder posterPath(String posterPath);

        public abstract Builder productionCompanies(List<String> productionCompanies);

        public abstract Builder productionCountries(List<String> productionCountries);

        public abstract Builder releaseDate(String releaseDate);

        public abstract Builder revenue(Long revenue);

        public abstract Builder runtime(Integer runtime);

        public abstract Builder spokenLanguages(List<String> spokenLanguages);

        public abstract Builder status(String status);

        public abstract Builder tagline(String tagline);

        public abstract Builder title(String title);

        public abstract Builder video(Boolean video);

        public abstract Builder voteAverageTmdb(Float voteAverageTmdb);

        public abstract Builder voteCountTmdb(Integer voteCountTmdb);

        public abstract Builder voteAverageTrakt(Float voteAverageTrakt);

        public abstract Builder voteCountTrakt(Integer voteCountTrakt);

        public abstract Builder certification(String certification);

        public abstract Builder casts(List<CastDomain> casts);

        public abstract Builder images(ImageDomain images);

        public abstract Builder videos(List<VideoDomain> videos);

        public abstract MovieDomain build();
    }
}
