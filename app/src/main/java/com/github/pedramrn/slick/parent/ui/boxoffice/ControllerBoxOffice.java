package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler;
import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.details.ControllerBase;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
import com.github.pedramrn.slick.parent.ui.details.model.Movie;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.util.Log.d;
import static com.github.pedramrn.slick.parent.App.componentMain;
import static com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding.inflate;
import static com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView.scrollEvents;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-04-13
 */

public class ControllerBoxOffice extends ControllerBase implements ViewBoxOffice {
    private static final String TAG = ControllerBoxOffice.class.getSimpleName();

    @Inject
    Provider<PresenterBoxOffice> provider;
    @Presenter
    PresenterBoxOffice presenter;

    private CompositeDisposable disposable;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull final ViewGroup container) {
        componentMain().inject(this);
        ControllerBoxOffice_Slick.bind(this);
        final ControllerBoxOfficeBinding binding = inflate(inflater, container, false);

        binding.toolbar.setTitle("Box Office");
        setToolbar(binding.toolbar);
        disposable = new CompositeDisposable();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        final ViewModelBoxOffice viewModel = new ViewModelBoxOffice(disposable, presenter, this);
        AdapterBoxOffice adapter = new AdapterBoxOffice(disposable, viewModel, getResources());
        binding.recyclerView.setAdapter(adapter);

        adapter.streamCommand().subscribe(new Consumer<Pair<Movie, String>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Pair<Movie, String> pair) throws Exception {
                RouterTransaction transaction = RouterTransaction.with(new ControllerDetails(pair.first, pair.second))
                        .pushChangeHandler(new HorizontalChangeHandler(true))
                        .popChangeHandler(new HorizontalChangeHandler(true));

                getRouter().pushController(transaction);
            }
        });

        final Observable<Integer> trigger = scrollEvents(binding.recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent event) throws Exception {
                        return !viewModel.isLoading();
                    }
                })
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        return totalItemCount == (lastVisibleItem + 1);
                    }
                })

                .scan(0, new BiFunction<Integer, RecyclerViewScrollEvent, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull RecyclerViewScrollEvent event) throws Exception {
                        return integer + 1;
                    }
                })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        viewModel.setLoading();
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        d(TAG, "doOnComplete() called");
                    }
                });


        viewModel.pagination(trigger, 1);

        return binding.getRoot();
    }

    private boolean isLoadingNext() {
        return false;
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
    public void showSnackBar(String s) {
        if (getView() != null) {
            Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
