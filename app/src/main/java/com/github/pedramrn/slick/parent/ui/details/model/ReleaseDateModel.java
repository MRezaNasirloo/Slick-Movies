package com.github.pedramrn.slick.parent.ui.details.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.Map;

public class ReleaseDateModel {
    public String imdbId;
    public Integer tmdb;
    public String name;
    @PropertyName("release_dates")
    public Map<String, String> release_dates;
    @Exclude
    public boolean isAdd;

    public ReleaseDateModel() {
    }

    public ReleaseDateModel(
            String imdbId,
            Integer tmdb,
            String name,
            Map<String, String> releaseDates,
            boolean isAdd
    ) {
        this.imdbId = imdbId;
        this.tmdb = tmdb;
        this.name = name;
        this.release_dates = releaseDates;
        this.isAdd = isAdd;
    }

    @Exclude
    public boolean getIsAdd() {
        return isAdd;
    }

    @Exclude
    public boolean isAdd() {
        return isAdd;
    }

    @Exclude
    public void setAdd(boolean add) {
        isAdd = add;
    }
}
