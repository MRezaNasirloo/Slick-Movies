package com.github.pedramrn.slick.parent.ui.details.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.github.pedramrn.slick.parent.ui.details.item.ItemBackdrop;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

import java.util.List;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-09
 */
@AutoValue
public abstract class Backdrop extends AutoBase implements Parcelable, ItemView {

    public abstract Integer uniqueId();

    public abstract List<String> allBackdrops();

    public abstract String movieTitle();

    @Nullable
    public abstract String backdropPath();

    public abstract Builder toBuilder();

    @Override
    public Item render(String tag) {
        return new ItemBackdrop(uniqueId(), this);
    }

    @Nullable
    public String backdropThumbnail() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/w300" + backdropPath();
    }

    @Nullable
    public String backdropOriginal() {
        if (backdropPath() == null) return null;
        return "http://image.tmdb.org/t/p/original" + backdropPath();
    }

    public static Backdrop create(
            long itemId,
            Integer uniqueId,
            List<String> allBackdrops,
            String movieTitle,
            String backdropPath
    ) {
        return builder()
                .itemId(itemId)
                .uniqueId(uniqueId)
                .allBackdrops(allBackdrops)
                .movieTitle(movieTitle)
                .backdropPath(backdropPath)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Backdrop.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder extends AutoBase.BuilderBase {
        public abstract Builder uniqueId(Integer id);

        public abstract Builder itemId(long itemId);

        public abstract Builder backdropPath(String backdropPath);

        public abstract Builder allBackdrops(List<String> allBackdrops);

        public abstract Builder movieTitle(String movieTitle);

        public abstract Backdrop build();
    }
}
