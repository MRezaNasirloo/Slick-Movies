package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.android.ImageLoaderPicassoImpl;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.util.Log.d;
import static android.view.View.FOCUS_DOWN;
import static com.github.pedramrn.slick.parent.App.componentMain;
import static com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding.inflate;
import static com.github.pedramrn.slick.parent.ui.boxoffice.ControllerBoxOffice_Slick.bind;
import static com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView.scrollEvents;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class ControllerBoxOffice extends Controller implements ViewBoxOffice {
    private static final String TAG = ControllerBoxOffice.class.getSimpleName();

    @Inject
    Provider<PresenterBoxOffice> provider;
    @Presenter
    PresenterBoxOffice presenter;

    @Inject
    ImageLoader imageLoader;


    private CompositeDisposable disposable;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        componentMain().inject(this);
        bind(this);

        final ControllerBoxOfficeBinding binding = inflate(inflater, container, false);

        disposable = new CompositeDisposable();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), VERTICAL, false));
        ViewModelBoxOffice viewModel = new ViewModelBoxOffice(disposable, presenter, this);
        AdapterBoxOffice adapter = new AdapterBoxOffice(disposable, viewModel, imageLoader);
        binding.recyclerView.setAdapter(adapter);

        final Observable<Integer> trigger = scrollEvents(binding.recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                        return !recyclerViewScrollEvent.view().canScrollVertically(FOCUS_DOWN);
                    }
                }).scan(0, new BiFunction<Integer, RecyclerViewScrollEvent, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull Integer integer,
                                         @io.reactivex.annotations.NonNull RecyclerViewScrollEvent event) throws Exception {
                        return integer + 1;
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        d(TAG, "doOnComplete() called");
                    }
                });


        viewModel.pagination(trigger, 2);

        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        disposable.dispose();
    }

    @Override
    public void render(ViewStateBoxOffice viewState) {
        Log.d(TAG, "render() called with: viewState = [" + viewState + "]");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
