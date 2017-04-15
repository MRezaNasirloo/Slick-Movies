package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class ControllerBoxOffice extends Controller implements ViewBoxOffice, Observer<ViewStateBoxOffice> {

    @Inject
    Provider<PresenterBoxOffice> provider;
    @Presenter
    PresenterBoxOffice presenter;

    private static final String TAG = ControllerBoxOffice.class.getSimpleName();
    private RecyclerViewBoxOffice adapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.getMainComponent(getRouter()).inject(this);
        ControllerBoxOffice_Slick.bind(this);
        final ControllerBoxOfficeBinding binding = ControllerBoxOfficeBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RecyclerViewBoxOffice();
        binding.recyclerView.setAdapter(adapter);

        final Observable<Integer> trigger = RxRecyclerView.scrollEvents(binding.recyclerView).filter(new Predicate<RecyclerViewScrollEvent>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                final boolean reachedBottom = !recyclerViewScrollEvent.view().canScrollVertically(RecyclerView.FOCUS_DOWN);
                if (reachedBottom) {
                    Log.e(TAG, "reachedBottom: " + reachedBottom);
                }
                return reachedBottom;
            }
        }).scan(0, new BiFunction<Integer, RecyclerViewScrollEvent, Integer>() {
            @Override
            public Integer apply(@io.reactivex.annotations.NonNull Integer integer,
                                 @io.reactivex.annotations.NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                return integer + 1;
            }
        });

        presenter.getBoxOffice(trigger, 2);

        presenter.updateStream().subscribe(this);

        return binding.getRoot();
    }

    @Override
    public void render(ViewStateBoxOffice viewState) {
        adapter.setMovieItems(viewState.movieItems());
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
    }

    @Override
    public void onNext(ViewStateBoxOffice viewStateBoxOffice) {
        render(viewStateBoxOffice);
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
        Toast.makeText(getApplicationContext(), "onComplete", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
