package com.github.pedramrn.slick.parent.ui.home;

import android.support.annotation.UiThread;

import com.github.pedramrn.slick.parent.ui.home.item.ItemCard;
import com.github.pedramrn.slick.parent.ui.home.item.ItemVideo;
import com.github.pedramrn.slick.parent.util.Indexed;
import com.xwray.groupie.Item;

import java.util.Collection;
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
                    public Indexed<ItemVideo> apply(@NonNull ItemVideo cast, @NonNull Integer index) throws Exception {
                        return new Indexed<>(cast, index);
                    }
                }).map(new Function<Indexed<ItemVideo>, Item>() {
                    @Override
                    public Item apply(@NonNull Indexed<ItemVideo> indexed) throws Exception {
                        return indexed.value().render(indexed.index());
                    }
                }).toList(items.size()).blockingGet();
    }*/

    /*public List<? extends Item> trending() {
        List<ItemCard> trending = viewStateHome.trending();
        return map(trending);
    }*/

    /*public List<? extends Item> popular() {
        List<ItemCard> popular = viewStateHome.popular();
        this.popular = map(popular);
        return this.popular;
    }*/

    @UiThread
    private List<? extends Item> map(Collection<ItemCard> items) {
        if (items == null || items.size() == 0) {
            return Collections.emptyList();
        }
        return Observable.fromIterable(items).map(new Function<ItemCard, Item>() {
            @Override
            public Item apply(@NonNull ItemCard itemCard) throws Exception {
                return itemCard.render(null);
            }
        }).toList(items.size()).blockingGet();
    }

}