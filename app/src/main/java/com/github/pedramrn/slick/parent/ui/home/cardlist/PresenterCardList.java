package com.github.pedramrn.slick.parent.ui.home.cardlist;

import android.support.annotation.NonNull;

import com.github.pedramrn.slick.parent.domain.router.PagedRouter;
import com.github.pedramrn.slick.parent.domain.rx.OnCompleteReturn;
import com.github.pedramrn.slick.parent.ui.PresenterBase;
import com.github.pedramrn.slick.parent.ui.details.PartialViewState;
import com.github.pedramrn.slick.parent.ui.details.model.AutoBase;
import com.github.pedramrn.slick.parent.ui.home.MapperMovieMetadataToMovieBasic;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.CardProgressiveMovie;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.Error;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.Loaded;
import com.github.pedramrn.slick.parent.ui.home.cardlist.state.Movies;
import com.github.pedramrn.slick.parent.ui.home.mapper.MapProgressive;
import com.github.pedramrn.slick.parent.ui.item.ItemView;
import com.github.pedramrn.slick.parent.util.ScanToMap;
import com.xwray.groupie.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * A base Presenter for loading a list of movies progressively
 */
public class PresenterCardList extends PresenterBase<ViewCardList, ViewStateCardList> {
    public static final String MOVIES_CARD = "MOVIES_CARD_";

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
    protected void start(ViewCardList view) {
        final int pageSize = view.pageSize();
        Observable<Integer> trigger = command(new CommandProvider<Object, ViewCardList>() {
            @Override
            public Observable<Object> provide(ViewCardList view) {
                return view.trigger();
            }
        }).map(new Function<Object, Integer>() {
            @Override
            public Integer apply(@NonNull Object o) throws Exception {
                return state.page();
            }
        });
        final Observable<PartialViewState<ViewStateCardList>> movies = trigger.concatMap(new Function<Integer,
                ObservableSource<PartialViewState<ViewStateCardList>>>() {
            @Override
            public ObservableSource<PartialViewState<ViewStateCardList>> apply(@NonNull final Integer page) throws Exception {
                System.out.println("PresenterCardList.movies page " + page);
                return router.page(page, pageSize).subscribeOn(io)
                        .map(mapperMetadata)
                        .cast(AutoBase.class)
                        .map(mapProgressive)
                        .cast(ItemView.class)
                        .map(new Function<ItemView, Item>() {
                            @Override
                            public Item apply(@NonNull ItemView itemView) throws Exception {
                                return itemView.render(tag);
                            }
                        })
                        .compose(scanToMap)
                        .map(new Function<Map<Integer, Item>, PartialViewState<ViewStateCardList>>() {
                            @Override
                            public PartialViewState<ViewStateCardList> apply(@NonNull Map<Integer, Item> items) throws Exception {
                                System.out.println("PresenterCardList.Movies size: " + items.size());
                                return new Movies(new TreeMap<>(items));
                            }
                        })
                        .lift(new OnCompleteReturn<PartialViewState<ViewStateCardList>>() {
                            @Override
                            public PartialViewState<ViewStateCardList> apply(@NonNull Boolean hadError) throws Exception {
                                System.out.println("PresenterCardList.Loaded had Error: " + hadError);
                                return new Loaded(!hadError);
                            }
                        })
                        .startWith(new CardProgressiveMovie(pageSize, tag))
                        .onErrorReturn(new Function<Throwable, PartialViewState<ViewStateCardList>>() {
                                           @Override
                                           public PartialViewState<ViewStateCardList> apply(@NonNull Throwable throwable) throws Exception {
                                               System.out.println("PresenterCardList.Error");
                                               return new Error(throwable);
                                           }
                                       }
                        );
            }
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
        System.out.println("PresenterCardList.render " + state.movies());
        this.state = state;
        view.updateList(Arrays.asList(state.movies().values().toArray(new Item[state.movies().size()])));
        view.loading(state.isLoading());
    }
}
