package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.home.item.ItemView;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Cast extends AutoBase implements Parcelable, ItemView {

    public abstract Integer id();

    public abstract Integer castId();

    public abstract String creditId();

    public abstract String name();

    @Nullable
    protected abstract String profilePath();

    public abstract String character();

    public abstract Integer gender();

    public abstract Integer order();

    public abstract Builder toBuilder();


    @Override
    public Item render(String tag) {
        return new ItemCast(uniqueId(), this);
    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    @Nullable
    public String profileIcon() {
        if (profilePath() == null) return null;
        return "http://image.tmdb.org/t/p/w92" + profilePath();
    }

    @Nullable
    public String profileOriginal() {
        if (profilePath() == null) return null;
        return "http://image.tmdb.org/t/p/original" + profilePath();
    }

    public static Cast create(Integer id, Integer uniqueId, Integer castId, String creditId, String name, String profilePath, String character,
                              Integer gender,
                              Integer order) {
        return builder()
                .id(id)
                .uniqueId(uniqueId)
                .castId(castId)
                .creditId(creditId)
                .name(name)
                .profilePath(profilePath)
                .character(character)
                .gender(gender)
                .order(order)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Cast.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder extends AutoBase.BuilderBase {

        public abstract Builder uniqueId(Integer id);

        public abstract Builder id(Integer id);

        public abstract Builder castId(Integer castId);

        public abstract Builder creditId(String creditId);

        public abstract Builder name(String name);

        public abstract Builder profilePath(String profilePath);

        public abstract Builder character(String character);

        public abstract Builder gender(Integer gender);

        public abstract Builder order(Integer order);

        public abstract Cast build();
    }
}
