package com.github.pedramrn.slick.parent.ui.people.state;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.people.item.ItemBio;
import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-16
 */
@AutoValue
public abstract class ViewStatePeople {

    public abstract List<Item> tvShowsCrew();

    public abstract List<Item> tvShowsCast();

    public abstract List<Item> moviesCrew();

    public abstract List<Item> moviesCast();

    @Nullable
    public abstract ItemBio itemBio();

    @Nullable
    public abstract PersonDetails personDetails();

    @Nullable
    public abstract Throwable errorPersonDetails();

    public abstract Builder toBuilder();

    public static ViewStatePeople create(List<Item> tvShowsCrew, List<Item> tvShowsCast, List<Item> moviesCrew, List<Item> moviesCast,
                                         ItemBio itemBio,
                                         PersonDetails personDetails, Throwable errorPersonDetails) {
        return builder()
                .tvShowsCrew(tvShowsCrew)
                .tvShowsCast(tvShowsCast)
                .moviesCrew(moviesCrew)
                .moviesCast(moviesCast)
                .itemBio(itemBio)
                .personDetails(personDetails)
                .errorPersonDetails(errorPersonDetails)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStatePeople.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder personDetails(PersonDetails personDetails);

        public abstract Builder errorPersonDetails(Throwable errorPersonDetails);

        public abstract Builder tvShowsCrew(List<Item> tvShowsCrew);

        public abstract Builder tvShowsCast(List<Item> tvShowsCast);

        public abstract Builder moviesCrew(List<Item> moviesCrew);

        public abstract Builder moviesCast(List<Item> moviesCast);

        public abstract Builder itemBio(ItemBio itemBio);

        public abstract ViewStatePeople build();
    }
}
