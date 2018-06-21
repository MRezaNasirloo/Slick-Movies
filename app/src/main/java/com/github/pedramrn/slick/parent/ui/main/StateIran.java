package com.github.pedramrn.slick.parent.ui.main;

import com.github.pedramrn.slick.parent.datasource.network.models.IpLocation;
import com.google.auto.value.AutoValue;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 *         Created on: 2018-06-21
 */
@AutoValue
public abstract class StateIran {
    public abstract IpLocation ipLocation();

    public abstract Boolean seen();

    public static Builder builder() {
        return new AutoValue_StateIran.Builder();
    }

    public abstract Builder toBuilder();

    public static StateIran create(IpLocation ipLocation, Boolean seen) {
        return builder()
                .ipLocation(ipLocation)
                .seen(seen)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder ipLocation(IpLocation ipLocation);

        public abstract Builder seen(Boolean seen);

        public abstract StateIran build();
    }
}
