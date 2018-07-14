package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.domain.model.CastDomain;
import com.github.pedramrn.slick.parent.domain.model.MovieDomain;
import com.github.pedramrn.slick.parent.domain.model.VideoDomain;
import com.github.pedramrn.slick.parent.ui.boxoffice.item.ItemBoxOffice;
import com.github.pedramrn.slick.parent.ui.details.item.ItemHeader;
import com.github.pedramrn.slick.parent.ui.favorite.item.ItemFavorite;
import com.github.pedramrn.slick.parent.ui.home.item.ItemBanner;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.videos.model.Video;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

import static com.github.pedramrn.slick.parent.ui.details.model.MovieSmall.BOX_OFFICE;
import static com.github.pedramrn.slick.parent.ui.details.model.MovieSmall.FAVORITE;
import static com.github.pedramrn.slick.parent.ui.details.model.MovieSmall.HEADER;
import static com.github.pedramrn.slick.parent.ui.details.model.MovieSmall.UPCOMING;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-09
 */

@AutoValue
public abstract class Movie extends AutoBase implements Parcelable, ItemView, MovieBasic {
    @Nullable
    public abstract String imdbId();

    public abstract Boolean adult();

    @Nullable
    public abstract Integer budget();

    public abstract List<String> genres();

    @Nullable
    public abstract String homepage();

    @Nullable
    public abstract Float popularity();

    public abstract List<String> productionCompanies();

    public abstract List<String> productionCountries();

    @Nullable
    public abstract Integer runtime();

    public abstract List<String> spokenLanguages();

    @Nullable
    public abstract String status();

    @Nullable
    public abstract String tagline();

    public abstract Boolean video();

    public abstract List<Cast> casts();

    public abstract Image images();

    public abstract List<Video> videos();

    public abstract Builder toBuilder();

    @Nullable
    @Override
    public String thumbnailPoster() {
        if (backdropPath() == null) {
            return null;
        }
        return "http://image.tmdb.org/t/p/w342" + posterPath();
    }

    @Nullable
    @Override
    public String thumbnailTinyPoster() {
        if (backdropPath() == null) {
            return null;
        }
        return "http://image.tmdb.org/t/p/w92" + posterPath();
    }

    @Nullable
    @Override
    public String thumbnailBackdrop() {
        if (backdropPath() == null) {
            return null;
        }
        return "http://image.tmdb.org/t/p/w300" + backdropPath();
    }

    @Override
    public Item render(String tag) {
        if (UPCOMING.equals(tag)) {
            return new ItemBanner(uniqueId(), this);
        } else if (BOX_OFFICE.equals(tag)) {
            return new ItemBoxOffice(uniqueId(), this, tag + uniqueId());
        } else if (FAVORITE.equals(tag)) {
            return new ItemFavorite(uniqueId(), this, tag + uniqueId());
        } else if (HEADER.equals(tag)) {
            return new ItemHeader(this, tag + uniqueId());
        } else {
            return new ItemCardMovie(uniqueId(), this, tag);
        }

    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    @Override
    public String runtimePretty() {
        return runtime() != null ? runtime() + " min" : "";
    }

    public static Movie create(MovieDomain md) {
        return map(md);
    }

    public static Movie create(
            Integer id,
            String title,
            Integer year,
            String overview,
            String posterPath,
            String backdropPath,
            String releaseDate,
            Float voteAverageTmdb,
            Integer voteCountTmdb,
            Float voteAverageTrakt,
            Integer voteCountTrakt,
            String certification,
            String imdbId,
            Boolean adult,
            Integer budget,
            List<String> genres,
            String homepage,
            Float popularity,
            List<String> productionCompanies,
            List<String> productionCountries,
            Long revenue,
            Integer runtime,
            List<String> spokenLanguages,
            String status,
            String tagline,
            Boolean video,
            List<Cast> casts,
            Image images,
            List<Video> videos
    ) {
        return builder()
                .id(id)
                .uniqueId(id)
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
                .imdbId(imdbId)
                .adult(adult)
                .budget(budget)
                .genres(genres)
                .homepage(homepage)
                .popularity(popularity)
                .productionCompanies(productionCompanies)
                .productionCountries(productionCountries)
                .revenue(revenue)
                .runtime(runtime)
                .spokenLanguages(spokenLanguages)
                .status(status)
                .tagline(tagline)
                .video(video)
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

        public abstract Builder year(Integer year);

        public abstract Movie build();
    }

    protected static Movie map(final MovieDomain md) {//casts
        List<CastDomain> castDomains = md.casts();
        int size = castDomains.size();
        List<Cast> casts = Observable.fromIterable(castDomains).map(cd -> Cast.create(
                cd.id(),
                cd.castId(),
                cd.creditId(),
                cd.name(),
                cd.profilePath(),
                cd.character(),
                cd.gender(),
                cd.order()
        )).toList(size == 0 ? 1 : size).blockingGet();

        //backdrops
        final List<String> backdropsDomain = md.images() != null ? md.images().backdrops() : Collections.emptyList();
        size = backdropsDomain.size();
        final List<Backdrop> backdrops = Observable.fromIterable(backdropsDomain)
                .distinct()
                .map(s -> Backdrop.create(-1, s.hashCode(), backdropsDomain, md.title(), s))
                .toList(size == 0 ? 1 : size).blockingGet();

        //videos
        List<VideoDomain> videosDomain = md.videos();
        size = videosDomain.size();
        final List<Video> videos = Observable.fromIterable(videosDomain)
                .map(vd -> Video.create(vd.key().hashCode(), vd.tmdb(), vd.type(), vd.key(), vd.name()))
                .toList(size == 0 ? 1 : size).blockingGet();


        //image
        Image image = Image.create(backdrops, md.images() != null ? md.images().posters() : Collections.emptyList());

        return Movie.create(
                md.id(),
                md.title(),
                md.year(),
                md.overview(),
                md.posterPath(),
                md.backdropPath(),
                md.releaseDate(),
                md.voteAverageTmdb(),
                md.voteCountTmdb(),
                md.voteAverageTrakt(),
                md.voteCountTrakt(),
                md.certification(),
                md.imdbId(),
                md.adult(),
                md.budget(),
                md.genres(),
                md.homepage(),
                md.popularity(),
                md.productionCompanies(),
                md.productionCountries(),
                md.revenue(),
                md.runtime(),
                md.spokenLanguages(),
                md.status(),
                md.tagline(),
                md.video(),
                casts,
                image,
                videos
        );
    }
}
