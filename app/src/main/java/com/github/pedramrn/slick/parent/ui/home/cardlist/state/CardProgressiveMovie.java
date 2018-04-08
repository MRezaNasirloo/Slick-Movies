package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.home.item.ItemLoading;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.xwray.groupie.Item;

import java.util.Map;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class CardProgressiveMovie extends PartialProgressive implements PartialViewState<ViewStateCardList> {

    public CardProgressiveMovie(int count, String tag) {
        super(count, tag, new ItemRendererProgressiveCard());
    }

    @Override
    public ViewStateCardList reduce(ViewStateCardList state) {
        Map<Integer, Item> movies = reduce(state.movies());
        movies.put(movies.size(), new ItemLoading(movies.size()));
        return state.toBuilder().movies(movies).isLoading(true).build();
    }

    private static class ItemRendererProgressiveCard implements ItemRenderer {

        @Override
        public Item render(long id, String tag) {
            return ItemCardProgressiveImpl.create(id).render(tag);
        }
    }
}
