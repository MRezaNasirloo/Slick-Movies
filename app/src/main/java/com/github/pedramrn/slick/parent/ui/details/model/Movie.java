package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemView;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;
import java.util.Locale;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */

@AutoValue
public abstract class Movie extends AutoBase implements Parcelable, ItemView, MovieBasic {

    public abstract String imdbId();

    public abstract Boolean adult();

    public abstract Integer budget();

    public abstract List<String> genres();

    public abstract String homepage();

    public abstract Float popularity();

    public abstract List<String> productionCompanies();

    public abstract List<String> productionCountries();

    public abstract Long revenue();

    public abstract Integer runtime();

    public abstract List<String> spokenLanguages();

    public abstract String status();

    public abstract String tagline();

    public abstract Boolean video();

    public abstract List<Cast> casts();

    public abstract Image images();

    public abstract List<Video> videos();

    public abstract Builder toBuilder();

    @Nullable
    @Override
    public String thumbnailPoster() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + posterPath();
    }

    @Nullable
    @Override
    public String thumbnailBackdrop() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + backdropPath();
    }

    @Override
    public Item render(String tag) {
        return new ItemCardMovie(uniqueId(), this, tag);
    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    public String rank(int position) {
        return String.format(Locale.getDefault(), "#%s", position + 1);
    }

    @Nullable
    @Override
    public String runtimePretty() {
        return runtime() + " min";
    }


    public static Movie create(Integer id, Integer uniqueId, String imdbId, Boolean adult, String backdropPath, Integer budget, List<String> genres,
                               String homepage, String overview, Float popularity, String posterPath, List<String> productionCompanies,
                               List<String> productionCountries, String releaseDate, Long revenue, Integer runtime, List<String> spokenLanguages,
                               String status, String tagline, String title, Boolean video, Float voteAverageTmdb, Integer voteCountTmdb,
                               Float voteAverageTrakt, Integer voteCountTrakt, String certification, List<Cast> casts, Image images,
                               List<Video> videos) {
        return builder()
                .id(id)
                .uniqueId(uniqueId)
                .imdbId(imdbId)
                .adult(adult)
                .backdropPath(backdropPath)
                .budget(budget)
                .genres(genres)
                .homepage(homepage)
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
        return new AutoValue_Movie.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder extends BuilderBase {
        public abstract Builder id(Integer id);

        public abstract Builder uniqueId(Integer id);

        public abstract Builder imdbId(String imdbId);

        public abstract Builder adult(Boolean adult);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder budget(Integer budget);

        public abstract Builder genres(List<String> genres);

        public abstract Builder homepage(String homepage);

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

        public abstract Builder casts(List<Cast> casts);

        public abstract Builder images(Image images);

        public abstract Builder videos(List<Video> videos);

        public abstract Builder voteAverageTmdb(Float voteAverageTmdb);

        public abstract Builder voteCountTmdb(Integer voteCountTmdb);

        public abstract Builder voteAverageTrakt(Float voteAverageTrakt);

        public abstract Builder voteCountTrakt(Integer voteCountTrakt);

        public abstract Builder certification(String certification);

        public abstract Movie build();
    }
}
