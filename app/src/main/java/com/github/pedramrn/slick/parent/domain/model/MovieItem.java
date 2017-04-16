package com.github.pedramrn.slick.parent.domain.model;

import com.github.pedramrn.slick.parent.autovalue.IncludeHashEquals;
import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */
@AutoValue
public abstract class MovieItem {

    public abstract String name();

    public abstract String revenue();

    public abstract String poster();

    public abstract String scoreMeta();

    public abstract String scoreImdb();

    public abstract String votesImdb();

    public abstract String rated();

    public abstract String runtime();

    public abstract String genre();

    public abstract String director();

    public abstract String writer();

    public abstract String actors();

    public abstract String plot();

    public abstract String production();

    @IncludeHashEquals
    public abstract String imdb();

    public abstract int trakt();

    public static MovieItem create(String name, String revenue, String poster, String scoreMeta, String scoreImdb, String votesImdb, String rated,
                                   String runtime, String genre, String director, String writer, String actors, String plot, String production,
                                   String imdb, int trakt) {
        return new AutoValue_MovieItem(name, revenue, poster, scoreMeta, scoreImdb, votesImdb, rated, runtime, genre, director, writer, actors, plot,
                production, imdb, trakt);
    }


}
