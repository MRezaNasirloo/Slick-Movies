package com.github.pedramrn.slick.parent.ui.home.model;

import android.os.Parcelable;

import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipated;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.google.auto.value.AutoValue;
import com.xwray.groupie.Item;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-20
 */

@AutoValue
public abstract class Video implements Parcelable, ItemVideo {

    public abstract Integer tmdb();

    public abstract String type();

    public abstract String key();

    public abstract String name();

    public String thumbnail(){
        return String.format("https://i1.ytimg.com/vi/%s/hqdefault.jpg", key());
    }

    @Override
    public Item render(int id) {
        return new ItemAnticipated(id, this);
    }

    public static Video create(Integer tmdb, String type, String key, String name) {
        return new AutoValue_Video(tmdb, type, key, name);
    }

}
