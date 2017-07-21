package com.github.pedramrn.slick.parent.ui.home.state;

import android.support.annotation.UiThread;

import com.github.pedramrn.slick.parent.ui.home.item.ItemView;
import com.xwray.groupie.Item;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-06-21
 */
public class ViewStateHomeRenderer {

    private final ViewStateHome viewStateHome;
    private List<? extends Item> popular;

    public ViewStateHomeRenderer(ViewStateHome viewStateHome) {
        this.viewStateHome = viewStateHome;
    }

    /*@UiThread
    public List<Item> anticipated() {
        List<ItemVideo> items = viewStateHome.anticipated();
        if (items == null || items.size() == 0) return Collections.emptyList();
        return Observable.fromIterable(items)
                .zipWith(Observable.range(0, items.size()), new BiFunction<ItemVideo, Integer, Indexed<ItemVideo>>() {
                    @Override
                    public Indexed<ItemVideo> apply(@NonNull ItemVideo casts, @NonNull Integer index) throws Exception {
                        return new Indexed<>(casts, index);
                    }
                }).map(new Function<Indexed<ItemVideo>, Item>() {
                    @Override
                    public Item apply(@NonNull Indexed<ItemVideo> indexed) throws Exception {
                        return indexed.value().render(indexed.index());
                    }
                }).toList(items.size()).blockingGet();
    }*/

    /*public List<? extends Item> trending() {
        List<ItemView> trending = viewStateHome.trending();
        return map(trending);
    }*/

    /*public List<? extends Item> popular() {
        List<ItemView> popular = viewStateHome.popular();
        this.popular = map(popular);
        return this.popular;
    }*/

    @UiThread
    private List<? extends Item> map(Collection<ItemView> items) {
        if (items == null || items.size() == 0) {
            return Collections.emptyList();
        }
        return Observable.fromIterable(items).map(new Function<ItemView, Item>() {
            @Override
            public Item apply(@NonNull ItemView itemView) throws Exception {
                return itemView.render(null);
            }
        }).toList(items.size()).blockingGet();
    }

}