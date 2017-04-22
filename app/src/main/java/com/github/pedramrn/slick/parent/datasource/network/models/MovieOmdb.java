package com.github.pedramrn.slick.parent.datasource.network.models;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-11
 */

@AutoValue
public abstract class MovieOmdb {

    @SerializedName("Title")
    public abstract String title();

    @SerializedName("Year")
    public abstract String year();

    @SerializedName("Rated")
    public abstract String rated();

    @SerializedName("Released")
    public abstract String released();

    @SerializedName("Runtime")
    public abstract String runtime();

    @SerializedName("Genre")
    public abstract String genre();

    @SerializedName("Director")
    public abstract String director();

    @SerializedName("Writer")
    public abstract String writer();

    @SerializedName("Actors")
    public abstract String actors();

    @SerializedName("Plot")
    public abstract String plot();

    @SerializedName("Language")
    public abstract String language();

    @SerializedName("Country")
    public abstract String country();

    @SerializedName("Awards")
    public abstract String awards();

    @SerializedName("Poster")
    public abstract String poster();

    @SerializedName("Ratings")
    public abstract List<Rating> ratings();

    @SerializedName("Metascore")
    public abstract String metascore();

    @SerializedName("imdbRating")
    public abstract String imdbRating();

    @SerializedName("imdbVotes")
    public abstract String imdbVotes();

    @SerializedName("imdbID")
    public abstract String imdbID();

    @SerializedName("Type")
    public abstract String type();

    @SerializedName("DVD")
    public abstract String dVD();

    @SerializedName("BoxOffice")
    public abstract String boxOffice();

    @SerializedName("Production")
    public abstract String production();

    @SerializedName("Website")
    public abstract String website();

    @SerializedName("Response")
    public abstract String response();

    public static MovieOmdb create(String title, String year, String rated, String released, String runtime, String genre, String director,
                                   String writer,
                                   String actors, String plot, String language, String country, String awards, String poster, List<Rating> ratings,
                                   String metascore, String imdbRating, String imdbVotes, String imdbID, String type, String dVD, String boxOffice,
                                   String production, String website, String response) {
        return builder()
                .title(title)
                .year(year)
                .rated(rated)
                .released(released)
                .runtime(runtime)
                .genre(genre)
                .director(director)
                .writer(writer)
                .actors(actors)
                .plot(plot)
                .language(language)
                .country(country)
                .awards(awards)
                .poster(poster)
                .ratings(ratings)
                .metascore(metascore)
                .imdbRating(imdbRating)
                .imdbVotes(imdbVotes)
                .imdbID(imdbID)
                .type(type)
                .dVD(dVD)
                .boxOffice(boxOffice)
                .production(production)
                .website(website)
                .response(response)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_MovieOmdb.Builder();
    }

    public abstract Builder toBuilder();


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder title(String title);

        public abstract Builder year(String year);

        public abstract Builder rated(String rated);

        public abstract Builder released(String released);

        public abstract Builder runtime(String runtime);

        public abstract Builder genre(String genre);

        public abstract Builder director(String director);

        public abstract Builder writer(String writer);

        public abstract Builder actors(String actors);

        public abstract Builder plot(String plot);

        public abstract Builder language(String language);

        public abstract Builder country(String country);

        public abstract Builder awards(String awards);

        public abstract Builder poster(String poster);

        public abstract Builder ratings(List<Rating> ratings);

        public abstract Builder metascore(String metascore);

        public abstract Builder imdbRating(String imdbRating);

        public abstract Builder imdbVotes(String imdbVotes);

        public abstract Builder imdbID(String imdbID);

        public abstract Builder type(String type);

        public abstract Builder dVD(String dVD);

        public abstract Builder boxOffice(String boxOffice);

        public abstract Builder production(String production);

        public abstract Builder website(String website);

        public abstract Builder response(String response);

        public abstract MovieOmdb build();
    }

    public static TypeAdapter<MovieOmdb> typeAdapter(Gson gson) {
        return new AutoValue_MovieOmdb.GsonTypeAdapter(gson);
    }
}
