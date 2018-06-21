package com.github.pedramrn.slick.parent.domain.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class PersonSearchDomain {

    public abstract Integer id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String profilePath();

    public abstract Float popularity();

    public abstract Boolean adult();

    public static Builder builder() {
        return new AutoValue_PersonSearchDomain.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(Integer id);

        public abstract Builder name(String name);

        public abstract Builder profilePath(String profilePath);

        public abstract Builder popularity(Float popularity);

        public abstract Builder adult(Boolean adult);

        public abstract PersonSearchDomain build();
    }
    // public abstract List<KnownFor> knownFor();


}
