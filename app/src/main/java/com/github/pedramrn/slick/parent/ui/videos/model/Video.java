package com.github.pedramrn.slick.parent.ui.videos.model;

import android.os.Parcelable;

import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.ui.videos.PresenterVideos;
import com.github.pedramrn.slick.parent.ui.videos.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.videos.item.ItemVideoInDetailsPage;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 * Created on: 2017-06-20
 */

@AutoValue
public abstract class Video extends AutoBase implements Parcelable, ItemView {

    public abstract Integer tmdb();

    public abstract String type();

    public abstract String key();

    public abstract String name();

    public String thumbnail() {
        return String.format("https://i1.ytimg.com/vi/%s/hqdefault.jpg", key());
    }

    @Override
    public Item render(final String tag) {
        switch (tag) {
            case PresenterVideos.VIDEO_IN_DETAILS_PAGE: {
                return new ItemVideoInDetailsPage(uniqueId(), this);
            }
            case PresenterVideos.VIDEO_IN_VIDEOS_PAGE: {
                return new ItemVideo(uniqueId(), this);
            }
            default:
                throw new IllegalArgumentException("Cannot handle view type: " + tag);
        }
    }

    @Override
    public long itemId() {
        return uniqueId().longValue();
    }

    public abstract Builder toBuilder();

    public static Video create(Integer uniqueId, Integer tmdb, String type, String key, String name) {
        return builder()
                .uniqueId(uniqueId)
                .tmdb(tmdb)
                .type(type)
                .key(key)
                .name(name)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_Video.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder extends AutoBase.BuilderBase {

        public abstract Builder uniqueId(Integer id);

        public abstract Builder tmdb(Integer tmdb);

        public abstract Builder type(String type);

        public abstract Builder key(String key);

        public abstract Builder name(String name);

        public abstract Video build();
    }
}
