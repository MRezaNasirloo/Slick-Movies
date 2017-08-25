package com.github.pedramrn.slick.parent.ui.people.state;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.people.model.PersonDetails;
import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-16
 */
@AutoValue
public abstract class ViewStatePeople {

    @Nullable
    public abstract PersonDetails personDetails();

    @Nullable
    public abstract Throwable errorPersonDetails();

    public abstract Builder toBuilder();

    public static ViewStatePeople create(PersonDetails personDetails, Throwable errorPersonDetails) {
        return builder()
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

        public abstract ViewStatePeople build();
    }
}
