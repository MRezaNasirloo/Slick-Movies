package com.github.pedramrn.slick.parent.ui.boxoffice.model;

import android.os.Parcelable;

import com.github.pedramrn.slick.parent.autovalue.IncludeHashEquals;
import com.google.auto.value.AutoValue;

import java.util.List;
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

    protected abstract String poster();

    public abstract String scoreMeta();

    public abstract String scoreImdb();

    public abstract String votesImdb();

    protected abstract String rated();

    public abstract String runtime();

    public abstract List<String> genre();

    public abstract String director();

    public abstract String writer();

    public abstract String actors();

    public abstract String plot();

    public abstract List<String> production();

    public abstract String released();

    @IncludeHashEquals
    public abstract String imdb();

    public abstract int trakt();

    public abstract int tmdb();

    public String certification() {
        return " " + rated() + " ";
    }

    public String posterMedium() {
        return "http://image.tmdb.org/t/p/w342" + poster();
    }

    public static MovieBoxOffice create(String name, String revenue, String poster, String scoreMeta, String scoreImdb, String votesImdb,
                                        String rated,
                                        String runtime, List<String> genre, String director, String writer, String actors, String plot,
                                        List<String> production, String released, String imdb, int trakt, int tmdb) {
        return new AutoValue_MovieBoxOffice(name, revenue, poster, scoreMeta, scoreImdb, votesImdb, rated, runtime, genre, director, writer, actors,
                plot, production, released, imdb, trakt, tmdb);
    }


}
