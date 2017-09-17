package com.github.pedramrn.slick.parent.ui.home.cardlist;

import com.github.pedramrn.slick.parent.datasource.network.ApiTmdb;
import com.github.pedramrn.slick.parent.datasource.network.ApiTrakt;
import com.github.pedramrn.slick.parent.domain.mapper.MapperCast;
import com.github.pedramrn.slick.parent.domain.mapper.MapperMovie;
import com.github.pedramrn.slick.parent.domain.mapper.MapperSimpleData;
import com.github.pedramrn.slick.parent.mock.ApiMockProvider;
import com.github.pedramrn.slick.parent.ui.home.MapperMovieMetadataToMovieBasic;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovie;
import com.github.pedramrn.slick.parent.ui.home.item.ItemCardMovieProgressive;
import com.github.pedramrn.slick.parent.ui.home.router.RouterTrendingImpl;
import com.google.common.truth.Correspondence;
import com.google.common.truth.MapSubject;
import com.google.common.truth.Truth;
import com.xwray.groupie.Item;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.verify;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-17
 */
public class PresenterCardListTest {

    private PublishSubject<Object> trigger = PublishSubject.create();
    private ViewCardList view;
    private PresenterCardList presenter;
    private TestScheduler testScheduler;
    private final Class<ItemCardMovieProgressive> progressive = ItemCardMovieProgressive.class;
    private final Class<ItemCardMovie> movie = ItemCardMovie.class;

    @Before
    public void setUp() throws Exception {
        ApiMockProvider apiMockProvider = new ApiMockProvider();
        ApiTmdb apiTmdbMock = apiMockProvider.apiTmdb();
        ApiTrakt apiTraktMock = apiMockProvider.apiTrakt();

        RouterTrendingImpl routerTrending = new RouterTrendingImpl(
                apiTraktMock,
                apiTmdbMock,
                new MapperMovie(new MapperCast(), new MapperSimpleData())
        );

        testScheduler = new TestScheduler();
        presenter = new PresenterCardList(
                routerTrending,
                new MapperMovieMetadataToMovieBasic(),
                "TEST",
                testScheduler,
                testScheduler
        );

        view = Mockito.mock(ViewCardList.class);
        Mockito.when(view.trigger()).thenReturn(trigger);
        Mockito.when(view.pageSize()).thenReturn(3);
    }

    @Test
    public void testProgressive() throws Exception {
        presenter.onViewUp(view);
        verify(view, new Times(1)).updateList(Collections.<Item>emptyList());
        trigger.onNext(1);
        presenter.updateStream().test()
                .awaitCount(2, new Runnable() {
                    @Override
                    public void run() {
                        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS);
                    }
                })
                .assertValueAt(0, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        return viewStateCardList.page() == 1;
                    }
                })
                .assertValueAt(1, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, progressive, 1, progressive, 2, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(2, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, progressive, 2, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(3, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, progressive, 2, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(4, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(5, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(6, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie).inOrder();
                        return true;
                    }
                })
                .assertValueAt(7, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie).inOrder();
                        return true;
                    }
                })
                .assertValueAt(8, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie).inOrder();
                        return true;
                    }
                })
                .awaitCount(16, new Runnable() {
                    @Override
                    public void run() {
                        trigger.onNext(1);
                        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS);
                    }
                })
                .assertValueAt(9, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, progressive, 4, progressive, 5, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(10, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, progressive, 5, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(11, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, progressive, 5, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(12, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, movie, 5, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(13, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, movie, 5, progressive).inOrder();
                        return true;
                    }
                })
                .assertValueAt(14, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, movie, 5, movie).inOrder();
                        return true;
                    }
                })
                .assertValueAt(15, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, movie, 5, movie).inOrder();
                        return true;
                    }
                })
                .assertValueAt(16, new Predicate<ViewStateCardList>() {
                    @Override
                    public boolean test(@NonNull ViewStateCardList viewStateCardList) throws Exception {
                        assertThis(viewStateCardList).containsExactly(0, movie, 1, movie, 2, movie, 3, movie, 4, movie, 5, movie).inOrder();
                        return true;
                    }
                })
        ;
    }

    private MapSubject.UsingCorrespondence<Item, Class<?>> assertThis(@NonNull ViewStateCardList viewStateCardList) {
        return Truth.assertThat(viewStateCardList.movies()).comparingValuesUsing(new ItemClassCorrespondence());
    }

    private static class ItemClassCorrespondence extends Correspondence<Item, Class<?>> {
        @Override
        public boolean compare(@Nullable Item actual, @Nullable Class<?> expected) {
            if (actual == null) {
                return expected == null;
            }
            return actual.getClass().equals(expected);
        }

        @Override
        public String toString() {
            return "Parse to";
        }
    }
}