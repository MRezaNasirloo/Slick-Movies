package com.github.pedramrn.slick.parent.datasource.database.model;

import com.github.pedramrn.slick.parent.domain.model.FavoriteDomain;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-11-10
 */

public class ItemFavoriteModel {

    public Long dateAdded;
    public String imdbId;
    public Integer tmdb;
    public String name;
    public String type;

    public ItemFavoriteModel() {
    }

    public ItemFavoriteModel(String imdbId, Integer tmdb, String name, String type, Long dateAdded) {
        this.imdbId = imdbId;
        this.tmdb = tmdb;
        this.name = name;
        this.type = type;
        this.dateAdded = dateAdded;
    }


    public ItemFavoriteModel(FavoriteDomain favorite) {
        this(favorite.imdbId(), favorite.tmdb(), favorite.name(), favorite.type(), System.currentTimeMillis());
    }
}
