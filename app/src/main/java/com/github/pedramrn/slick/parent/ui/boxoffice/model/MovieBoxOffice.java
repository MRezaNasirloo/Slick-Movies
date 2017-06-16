package com.github.pedramrn.slick.parent.ui.boxoffice.model;

import android.os.Parcelable;

import com.github.pedramrn.slick.parent.autovalue.IncludeHashEquals;
import com.google.auto.value.AutoValue;

import java.util.Locale;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */
@AutoValue
public abstract class MovieBoxOffice implements Parcelable {

    public String rank(int position) {
        return String.format(Locale.getDefault(), "#%s", position + 1);
    }

    public abstract String name();

    public abstract String revenue();

    public abstract String poster();

    public abstract String scoreMeta();

    public abstract String scoreImdb();

    public abstract String votesImdb();

    protected abstract String rated();

    public abstract String runtime();

    public abstract String genre();

    public abstract String director();

    public abstract String writer();

    public abstract String actors();

    public abstract String plot();

    public abstract String production();

    public abstract String released();

    @IncludeHashEquals
    public abstract String imdb();

    public abstract int trakt();

    public abstract int tmdb();

    public String certification(){
     return " " + rated() + " ";
    }

    public static MovieBoxOffice create(String name, String revenue, String poster, String scoreMeta, String scoreImdb, String votesImdb, String rated,
                                        String runtime, String genre, String director, String writer, String actors, String plot, String production,
                                        String released, String imdb, int trakt, int tmdb) {
        return builder()
                .name(name)
                .revenue(revenue)
                .poster(poster)
                .scoreMeta(scoreMeta)
                .scoreImdb(scoreImdb)
                .votesImdb(votesImdb)
                .rated(rated)
                .runtime(runtime)
                .genre(genre)
                .director(director)
                .writer(writer)
                .actors(actors)
                .plot(plot)
                .production(production)
                .released(released)
                .imdb(imdb)
                .trakt(trakt)
                .tmdb(tmdb)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_MovieBoxOffice.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder name(String name);

        public abstract Builder revenue(String revenue);

        public abstract Builder poster(String poster);

        public abstract Builder scoreMeta(String scoreMeta);

        public abstract Builder scoreImdb(String scoreImdb);

        public abstract Builder votesImdb(String votesImdb);

        public abstract Builder runtime(String runtime);

        public abstract Builder genre(String genre);

        public abstract Builder director(String director);

        public abstract Builder writer(String writer);

        public abstract Builder actors(String actors);

        public abstract Builder plot(String plot);

        public abstract Builder production(String production);

        public abstract Builder released(String released);

        public abstract Builder imdb(String imdb);

        public abstract Builder trakt(int trakt);

        public abstract Builder rated(String rated);

        public abstract Builder tmdb(int tmdb);

        public abstract MovieBoxOffice build();
    }
}
