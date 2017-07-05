package com.github.pedramrn.slick.parent.ui.details.model;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.autovalue.IncludeHashEquals;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */

@AutoValue
public abstract class Movie extends AutoBase implements ItemCard {

    public abstract Integer id();

    @IncludeHashEquals
    public abstract Integer uniqueId();

    public abstract String imdbId();

    public abstract Boolean adult();

    public abstract String backdropPath();

    public abstract Integer budget();

    public abstract List<String> genres();

    public abstract String homepage();

    public abstract String overview();

    public abstract Float popularity();

    protected abstract String posterPath();

    public abstract List<String> productionCompanies();

    public abstract List<String> productionCountries();

    public abstract String releaseDate();

    public abstract Long revenue();

    public abstract Integer runtime();

    public abstract List<String> spokenLanguages();

    public abstract String status();

    public abstract String tagline();

    public abstract String title();

    public abstract Boolean video();

    public abstract Float voteAverage();

    public abstract Integer voteCount();

    public abstract List<Cast> casts();

    public abstract Image images();

    public abstract List<Video> videos();

    @Nullable
    public String posterThumbnail() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + posterPath();
    }

    @Override
    public Item render(int id) {
        return new ItemCardMovie(uniqueId(), this);
    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ItemCard) {
            ItemCard that = (ItemCard) o;
            return (this.itemId() == that.itemId());
        }
        return false;
    }

    public abstract Builder toBuilder();

    public static Movie create(Integer id, Integer uniqueId, String imdbId, Boolean adult, String backdropPath, Integer budget, List<String> genres,
                               String homepage, String overview, Float popularity, String posterPath, List<String> productionCompanies,
                               List<String> productionCountries, String releaseDate, Long revenue, Integer runtime, List<String> spokenLanguages,
                               String status, String tagline, String title, Boolean video, Float voteAverage, Integer voteCount, List<Cast> casts,
                               Image images, List<Video> videos) {
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
                .voteAverage(voteAverage)
                .voteCount(voteCount)
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

        public abstract Builder voteAverage(Float voteAverage);

        public abstract Builder voteCount(Integer voteCount);

        public abstract Builder casts(List<Cast> casts);

        public abstract Builder images(Image images);

        public abstract Builder videos(List<Video> videos);

        public abstract Movie build();
    }
}
