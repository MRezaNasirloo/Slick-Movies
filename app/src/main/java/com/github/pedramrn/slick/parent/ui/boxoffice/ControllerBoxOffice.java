package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;
import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.pedramrn.slick.parent.ui.ToolbarHost;
import com.github.pedramrn.slick.parent.ui.android.ImageLoader;
import com.github.pedramrn.slick.parent.ui.boxoffice.model.MovieBoxOffice;
import com.github.pedramrn.slick.parent.ui.changehandler.ArcFadeMoveChangeHandler;
import com.github.pedramrn.slick.parent.ui.details.ControllerDetails;
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
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull final ViewGroup container) {
        componentMain().inject(this);
        ControllerBoxOffice_Slick.bind(this);

        final ControllerBoxOfficeBinding binding = inflate(inflater, container, false);
        if (getActivity() != null) {
            ((ToolbarHost) getActivity()).setToolbar(binding.toolbar);
        }

        disposable = new CompositeDisposable();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        ViewModelBoxOffice viewModel = new ViewModelBoxOffice(disposable, presenter, this);
        AdapterBoxOffice adapter = new AdapterBoxOffice(disposable, viewModel, imageLoader, getResources());
        binding.recyclerView.setAdapter(adapter);

        adapter.streamCommand().subscribe(new Consumer<Pair<MovieBoxOffice, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Pair<MovieBoxOffice, Integer> pair) throws Exception {
                RouterTransaction transaction = RouterTransaction.with(new ControllerDetails(pair.first, pair.second))
                        .pushChangeHandler(new ArcFadeMoveChangeHandler())
                        .popChangeHandler(new ArcFadeMoveChangeHandler());

                getRouter().pushController(transaction);
            }
        });

        final Observable<Integer> trigger = scrollEvents(binding.recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        return totalItemCount <= (lastVisibleItem + 1);
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
