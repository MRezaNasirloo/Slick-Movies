package com.github.pedramrn.slick.parent.ui.people.model;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;

import com.github.pedramrn.slick.parent.util.DateUtils;
import com.google.auto.value.AutoValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public CharSequence nameBorn() {
        String name = name() + "\n";
        SimpleDateFormat dateFormatNum = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        SimpleDateFormat dateFormatName = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String born;
        String placeOfBirth = placeOfBirth();
        placeOfBirth = placeOfBirth == null ? "" : ", " + placeOfBirth;
        try {
            Date date = dateFormatNum.parse(birthday());
            int age = DateUtils.age(date);
            born = String.format(Locale.getDefault(), "\nBorn: %s (%d)%s", dateFormatName.format(date), age, placeOfBirth);
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
                                       List<String> images) {
        return new AutoValue_PersonDetails(id, imdbId, name, biography, placeOfBirth, profilePicId, gender, birthday, deathday, alsoKnownAs,
                popularity, adult, homepage, images);
    }

}
