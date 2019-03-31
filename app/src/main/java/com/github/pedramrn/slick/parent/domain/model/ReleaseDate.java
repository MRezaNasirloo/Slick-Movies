package com.github.pedramrn.slick.parent.domain.model;

import com.github.pedramrn.slick.parent.ui.details.item.ItemReleaseDate;
import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

@AutoValue
public abstract class ReleaseDate extends AutoBase implements ItemView {
    public abstract String date();

    public abstract ReleaseType type();

    public abstract Integer uniqueId();

    @Override
    public Item render(String tag) {
        return new ItemReleaseDate(uniqueId(), this);
    }

    @Override
    public long itemId() {
        return uniqueId();
    }

    public enum ReleaseType {
        PREMIERE,
        THEATRICAL_LIMITED,
        THEATRICAL,
        DIGITAL,
        PHYSICAL,
        TV
    }

    abstract public Builder toBuilder();

    public static ReleaseDate create(int uniqueId, String date, ReleaseType type) {
        return builder()
                .date(date)
                .uniqueId(uniqueId)
                .type(type)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ReleaseDate.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder extends AutoBase.BuilderBase {
        public abstract Builder date(String date);

        public abstract Builder type(ReleaseType type);

        public abstract Builder uniqueId(Integer id);

        public abstract ReleaseDate build();
    }
}
