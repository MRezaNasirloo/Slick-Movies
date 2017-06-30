package com.github.pedramrn.slick.parent.di;

import android.content.Context;

import com.github.pedramrn.slick.parent.datasource.network.ApiOmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.datasource.network.models.MovieOmdb;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-22
 */

public class ApiMocker {

    @SuppressWarnings("ConstantConditions")
    private ApiMocker(Context context) {

    }

    public static ApiMocker apiMocker(Context context) {
        return null;
    }

    public ApiOmdb mockApiOmdb() {
        return null;
    }

    public ApiTrakt mockApiTrakt() {
        return null;
    }

    public Observable<MovieOmdb> movieOmdb() {
        return null;
    }

    public List<BoxOfficeItem> getBoxOfficeItems() {
        return null;
    }

    public MovieOmdb getMovieOmdb() {
        return null;
    }
}
