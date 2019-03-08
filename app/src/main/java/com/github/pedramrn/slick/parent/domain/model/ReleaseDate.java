package com.github.pedramrn.slick.parent.domain.model;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class ReleaseDate {
    public abstract String date();

    public abstract ReleaseType type();

    public enum ReleaseType {
        PREMIERE,
        THEATRICAL_LIMITED,
        THEATRICAL,
        DIGITAL,
        PHYSICAL,
        TV
    }

    public static ReleaseDate create(String date, ReleaseType type) {
        return builder()
                .date(date)
                .type(type)
                .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_ReleaseDate.Builder();
    }
    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder date(String date);

        public abstract Builder type(ReleaseType type);

        public abstract ReleaseDate build();
    }
}
