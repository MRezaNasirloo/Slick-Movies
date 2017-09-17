package com.github.pedramrn.slick.parent.ui.home.cardlist.state;

import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.home.cardlist.ViewStateCardList;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardProgressiveImpl;
import com.github.pedramrn.slick.parent.ui.item.ItemRenderer;
import com.github.pedramrn.slick.parent.ui.item.PartialProgressive;
import com.xwray.groupie.Item;

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
        return state.toBuilder().movies(reduce(state.movies())).build();
    }

    private static class ItemRendererProgressiveCard implements ItemRenderer {

        @Override
        public Item render(long id, String tag) {
            return ItemCardProgressiveImpl.create(id).render(tag);
        }
    }
}
