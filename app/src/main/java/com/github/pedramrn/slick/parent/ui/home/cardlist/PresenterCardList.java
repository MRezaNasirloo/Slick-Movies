package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.router.PagedRouter;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.home.MapperMovieMetadataToMovieBasic;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.CardProgressiveMovie;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.Error;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.Loaded;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.Movies;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.util.ScanToMap;
import com.mrezanasirloo.slick.uni.PartialViewState;
import com.mrezanasirloo.slick.uni.SlickPresenterUni;
import com.xwray.groupie.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A base Presenter for loading a list of movies progressively
 */
public class PresenterCardList extends SlickPresenterUni<ViewCardList, ViewStateCardList> {
    public static final String MOVIES_CARD = "MOVIES_CARD_";
    public static final String POPULAR = "POPULAR";
    public static final String TRENDING = "TRENDING";

    private final PagedRouter router;
    private final MapperMovieMetadataToMovieBasic mapperMetadata;
    private final String tag;

    private final MapProgressive mapProgressive = new MapProgressive();
    private final ScanToMap<Item> scanToMap = new ScanToMap<>();
    private ViewStateCardList state;

    public PresenterCardList(
            PagedRouter router,
            MapperMovieMetadataToMovieBasic mapperMetadata,
            String tag,
            @Named("main") Scheduler main,
            @Named("io") Scheduler io
    ) {
        super(main, io);
        this.router = router;
        this.mapperMetadata = mapperMetadata;
        this.tag = MOVIES_CARD + tag;
    }

    @Override
    protected void start(@NonNull ViewCardList view) {
        final int pageSize = view.pageSize();
        Observable<Integer> trigger = command(ViewCardList::trigger).map(o -> state.page()).startWith(1);
        final Observable<PartialViewState<ViewStateCardList>> movies = trigger.concatMap(page -> {
            System.out.println("PresenterCardList.movies page " + page);
            return router.page(page, pageSize).subscribeOn(io)
                    .map(mapperMetadata)
                    .cast(AutoBase.class)
                    .map(mapProgressive)
                    .cast(ItemView.class)
                    .map(itemView -> itemView.render(tag))
                    .compose(scanToMap)
                    .map((Function<Map<Integer, Item>, PartialViewState<ViewStateCardList>>) items -> {
                        System.out.println("PresenterCardList.Movies size: " + items.size());
                        return new Movies(new TreeMap<>(items));
                    })
                    .lift(new OnCompleteReturn<PartialViewState<ViewStateCardList>>() {
                        @Override
                        public PartialViewState<ViewStateCardList> apply(@NonNull Boolean hadError) throws Exception {
                            System.out.println("PresenterCardList.Loaded had Error: " + hadError);
                            return new Loaded();
                        }
                    })
                    .startWith(new CardProgressiveMovie(pageSize / 2, tag))
                    .onErrorReturn(throwable -> {
                                System.out.println("PresenterCardList.Error");
                                return new Error(throwable);
                            }
                    );
        });

        ViewStateCardList initialState = ViewStateCardList.builder()
                .page(1)
                .isLoading(true)
                .itemLoadedCount(0)
                .movies(Collections.<Integer, Item>emptyMap())
                .build();

        reduce(initialState, movies).subscribe(this);
    }

    @Override
    protected void render(@NonNull ViewStateCardList state, @NonNull ViewCardList view) {
        this.state = state;
        view.updateList(Arrays.asList(state.movies().values().toArray(new Item[state.movies().size()])));
        view.loading(state.isLoading());
    }
}
