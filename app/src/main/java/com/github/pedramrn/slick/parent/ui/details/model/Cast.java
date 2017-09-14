package com.github.pedramrn.slick.parent.ui.details.model;

import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.item.ItemCast;
import com.github.pedramrn.slick.parent.ui.item.ItemViewListParcelable;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Cast extends AutoBase implements ItemViewListParcelable {

    public abstract Integer id();

    public abstract Integer castId();

    public abstract String creditId();

    public abstract String name();

    @Nullable
    public abstract String profilePicId();

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
        if (profilePicId() == null) return null;
        return "http://image.tmdb.org/t/p/w92" + profilePicId();
    }

    @Nullable
    public String profileOriginal() {
        if (profilePicId() == null) return null;
        return "http://image.tmdb.org/t/p/original" + profilePicId();
    }

    public static Cast create(Integer id, Integer castId, String creditId, String name, String profilePicId, String character, Integer gender,
                              Integer order) {
        return builder()
                .id(id)
                .uniqueId(id)
                .castId(castId)
                .creditId(creditId)
                .name(name)
                .profilePicId(profilePicId)
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

        public abstract Builder character(String character);

        public abstract Builder gender(Integer gender);

        public abstract Builder order(Integer order);

        public abstract Builder profilePicId(String profilePicId);

        public abstract Cast build();
    }
}
