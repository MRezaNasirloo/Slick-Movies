package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.boxoffice.item.ItemBoxOffice;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBanner;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.people.model.CastOrCrewPersonDetails;
import com.github.pedramrn.slick.parent.util.DateUtils;
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

    public static final String BOX_OFFICE = "BOX_OFFICE";
    public static final String UPCOMING = "UPCOMING";

    @Nullable
    public abstract String originalTitle();

    @Nullable
    public abstract String originalLanguage();

    @Nullable
    public abstract Boolean adult();

    @Nullable
    public abstract Float popularity();

    @Nullable
    public abstract Boolean video();

    public abstract Builder toBuilder();

    @Nullable
    @Override
    public String thumbnailPoster() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w342" + posterPath();
    }

    @Nullable
    @Override
    public String thumbnailTinyPoster() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w92" + posterPath();
    }

    @Nullable
    @Override
    public String thumbnailBackdrop() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + backdropPath();
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
        if (UPCOMING.equals(tag)) {
            return new ItemBanner(uniqueId(), this);
        } else if (BOX_OFFICE.equals(tag)) {
            return new ItemBoxOffice(uniqueId(), this, tag + uniqueId());
        } else {
            return new ItemCardMovie(uniqueId(), this, tag);

        }
    }

    public Item render(ItemView itemView, String tag) {
        return itemView.render(tag);
    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    public static MovieSmall create(Integer id, String imdbId, String title, Integer year, String overview, String posterPath, String backdropPath,
                                    String releaseDate, Float voteAverageTmdb, Integer voteCountTmdb, Float voteAverageTrakt, Integer voteCountTrakt,
                                    String certification, Long revenue, String originalTitle, String originalLanguage, Boolean adult,
                                    Float popularity,
                                    Boolean video) {
        return builder()
                .id(id)
                .uniqueId(id)
                .imdbId(imdbId)
                .title(title)
                .year(year)
                .overview(overview)
                .posterPath(posterPath)
                .backdropPath(backdropPath)
                .releaseDate(releaseDate)
                .voteAverageTmdb(voteAverageTmdb)
                .voteCountTmdb(voteCountTmdb)
                .voteAverageTrakt(voteAverageTrakt)
                .voteCountTrakt(voteCountTrakt)
                .certification(certification)
                .revenue(revenue)
                .originalTitle(originalTitle)
                .originalLanguage(originalLanguage)
                .adult(adult)
                .popularity(popularity)
                .video(video)
                .build();
    }

    public static MovieSmall create(CastOrCrewPersonDetails coc) {
        return create(
                coc.id(),
                null,
                coc.title(),
                DateUtils.year(coc.releaseDate()),
                coc.overview(),
                coc.posterPath(),
                coc.backdropPath(),
                coc.releaseDate(),
                coc.voteAverage(),
                coc.voteCount(),
                null,
                null,
                null,
                null,
                coc.originalTitle(),
                coc.originalLanguage(),
                coc.adult(),
                coc.popularity(),
                false
        );
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

        public abstract Builder imdbId(String imdbId);

        public abstract Builder year(Integer year);

        public abstract Builder revenue(Long revenue);

        public abstract MovieSmall build();
    }
}
