package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemAnticipated;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.ui.home.model.Video;
import com.github.pedramrn.slick.parent.util.Indexed;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
@AutoValue
abstract class ViewStateHome {
    @Nullable
    public abstract List<ItemVideo> items();

    @Nullable
    public abstract List<String> foo();

    @Nullable
    public abstract List<Video> videos();

    @Nullable
    public abstract List<Movie> movies();

    @Nullable
    public abstract Throwable videosError();


    @UiThread
    public List<ItemAnticipated> anticipated() {
        List<ItemVideo> items = items();
        if (items == null) return Collections.emptyList();
        return Observable.fromIterable(items)
                .zipWith(Observable.range(0, items.size()), new BiFunction<ItemVideo, Integer, Indexed<ItemVideo>>() {
                    @Override
                    public Indexed<ItemVideo> apply(@NonNull ItemVideo cast, @NonNull Integer index) throws Exception {
                        return new Indexed<>(cast, index);
                    }
                }).map(new Function<Indexed<ItemVideo>, ItemAnticipated>() {
                    @Override
                    public ItemAnticipated apply(@NonNull Indexed<ItemVideo> indexed) throws Exception {
                        return indexed.value().render(indexed.index());
                    }
                }).toList(items.size()).blockingGet();
    }


    public abstract Builder toBuilder();

    public static ViewStateHome create(List<ItemVideo> items, List<String> foo, List<Video> videos, List<Movie> movies, Throwable videosError) {
        return builder()
                .items(items)
                .foo(foo)
                .videos(videos)
                .movies(movies)
                .videosError(videosError)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ViewStateHome.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder items(List<ItemVideo> items);

        public abstract Builder foo(List<String> foo);

        public abstract Builder videos(List<Video> videos);

        public abstract Builder movies(List<Movie> movies);

        public abstract Builder videosError(Throwable videosError);

        public abstract ViewStateHome build();
    }
}
