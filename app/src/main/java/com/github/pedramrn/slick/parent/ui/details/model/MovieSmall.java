package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.home.item.ItemView;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */

@AutoValue
public abstract class MovieSmall extends AutoBase implements Parcelable, ItemView, MovieBasic {

    public abstract String originalTitle();

    public abstract String originalLanguage();

    public abstract Boolean adult();

    public abstract Float popularity();

    public abstract Boolean video();

    public abstract Builder toBuilder();

    @Nullable
    @Override
    public String posterThumbnail() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + posterPath();
    }

    @Nullable
    @Override
    public String runtimePretty() {
        return null;
    }

    @Override
    public List<String> genres() {
        return Collections.emptyList();
    }

    @Override
    public Item render(String tag) {
        return new ItemCardMovie(uniqueId(), this, tag);
    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    public static MovieSmall create(Integer id, String title, String overview, String posterPath, String backdropPath, String releaseDate,
                                    Float voteAverageTmdb, Integer voteCountTmdb, Float voteAverageTrakt, Integer voteCountTrakt,
                                    String certification,
                                    Integer uniqueId, String originalTitle, String originalLanguage, Boolean adult, Float popularity, Boolean video) {
        return builder()
                .id(id)
                .title(title)
                .overview(overview)
                .posterPath(posterPath)
                .backdropPath(backdropPath)
                .releaseDate(releaseDate)
                .voteAverageTmdb(voteAverageTmdb)
                .voteCountTmdb(voteCountTmdb)
                .voteAverageTrakt(voteAverageTrakt)
                .voteCountTrakt(voteCountTrakt)
                .certification(certification)
                .uniqueId(uniqueId)
                .originalTitle(originalTitle)
                .originalLanguage(originalLanguage)
                .adult(adult)
                .popularity(popularity)
                .video(video)
                .build();
    }


    public static Builder builder() {
        return new AutoValue_MovieSmall.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder extends AutoBase.BuilderBase {
        public abstract Builder id(Integer id);

        public abstract Builder title(String title);

        public abstract Builder overview(String overview);

        public abstract Builder originalTitle(String originalTitle);

        public abstract Builder posterPath(String posterPath);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder releaseDate(String releaseDate);

        public abstract Builder originalLanguage(String originalLanguage);

        public abstract Builder adult(Boolean adult);

        public abstract Builder popularity(Float popularity);

        public abstract Builder video(Boolean video);

        public abstract Builder voteAverageTmdb(Float voteAverageTmdb);

        public abstract Builder voteCountTmdb(Integer voteCountTmdb);

        public abstract Builder uniqueId(Integer uniqueId);

        public abstract Builder voteAverageTrakt(Float voteAverageTrakt);

        public abstract Builder voteCountTrakt(Integer voteCountTrakt);

        public abstract Builder certification(String certification);

        public abstract MovieSmall build();
    }
}
