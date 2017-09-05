package com.github.pedramrn.slick.parent.ui.people.model;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;

import com.github.pedramrn.slick.parent.util.DateUtils;
import com.google.auto.value.AutoValue;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-22
 */

@AutoValue
public abstract class PersonDetails {
    public abstract Integer id();

    public abstract String imdbId();

    public abstract String name();

    @Nullable
    public abstract String biography();

    @Nullable
    public abstract String placeOfBirth();

    @Nullable
    public abstract String profilePicId();

    @Nullable
    public abstract Integer gender();

    @Nullable
    public abstract String birthday();

    @Nullable
    public abstract String deathday();

    public abstract List<String> alsoKnownAs();

    public abstract Float popularity();

    public abstract Boolean adult();

    @Nullable
    public abstract String homepage();

    public abstract List<String> images();

    public abstract List<CastOrCrewPersonDetails> movieCast();

    public abstract List<CastOrCrewPersonDetails> movieCrew();

    public abstract List<CastOrCrewPersonDetails> tvCast();

    public abstract List<CastOrCrewPersonDetails> tvCrew();

    public abstract Builder toBuilder();

    public CharSequence nameBorn() {
        String name = name() + "\n";

        String born;
        String placeOfBirth = placeOfBirth();
        placeOfBirth = placeOfBirth == null ? "" : ", " + placeOfBirth;
        try {
            Date date = DateUtils.toDate(birthday());
            int age = DateUtils.age(date);
            born = String.format(Locale.getDefault(), "\nBorn: %s (%d)%s", DateUtils.format_MMM_dd_yyyy(date), age, placeOfBirth);
        } catch (ParseException | NullPointerException e) {
            born = "\nBorn: n/a" + placeOfBirth;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(name);
        builder.append(born);
        builder.setSpan(new RelativeSizeSpan(0.70f), name.length(), builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static PersonDetails create(Integer id, String imdbId, String name, String biography, String placeOfBirth, String profilePicId,
                                       Integer gender,
                                       String birthday, String deathday, List<String> alsoKnownAs, Float popularity, Boolean adult, String homepage,
                                       List<String> images, List<CastOrCrewPersonDetails> movieCast, List<CastOrCrewPersonDetails> movieCrew,
                                       List<CastOrCrewPersonDetails> tvCast, List<CastOrCrewPersonDetails> tvCrew) {
        return builder()
                .id(id)
                .imdbId(imdbId)
                .name(name)
                .biography(biography)
                .placeOfBirth(placeOfBirth)
                .profilePicId(profilePicId)
                .gender(gender)
                .birthday(birthday)
                .deathday(deathday)
                .alsoKnownAs(alsoKnownAs)
                .popularity(popularity)
                .adult(adult)
                .homepage(homepage)
                .images(images)
                .movieCast(movieCast)
                .movieCrew(movieCrew)
                .tvCast(tvCast)
                .tvCrew(tvCrew)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PersonDetails.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder imdbId(String imdbId);

        public abstract Builder name(String name);

        public abstract Builder biography(String biography);

        public abstract Builder placeOfBirth(String placeOfBirth);

        public abstract Builder profilePicId(String profilePicId);

        public abstract Builder gender(Integer gender);

        public abstract Builder birthday(String birthday);

        public abstract Builder deathday(String deathday);

        public abstract Builder alsoKnownAs(List<String> alsoKnownAs);

        public abstract Builder popularity(Float popularity);

        public abstract Builder adult(Boolean adult);

        public abstract Builder homepage(String homepage);

        public abstract Builder images(List<String> images);

        public abstract Builder movieCast(List<CastOrCrewPersonDetails> movieCast);

        public abstract Builder movieCrew(List<CastOrCrewPersonDetails> movieCrew);

        public abstract Builder tvCast(List<CastOrCrewPersonDetails> tvCast);

        public abstract Builder tvCrew(List<CastOrCrewPersonDetails> tvCrew);

        public abstract PersonDetails build();
    }
}
