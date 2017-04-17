package com.github.pedramrn.slick.parent.ui.boxoffice;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.databinding.ControllerBoxOfficeBinding;
import com.github.pedramrn.slick.parent.domain.model.MovieItem;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

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

    ViewModelBoxOffice viewModel;

    private CompositeDisposable disposable = new CompositeDisposable();

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        App.getMainComponent(getRouter()).inject(this);
        ControllerBoxOffice_Slick.bind(this);
        if (viewModel == null){
            viewModel = new ViewModelBoxOffice(presenter, this);
        }
        final ControllerBoxOfficeBinding binding = ControllerBoxOfficeBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerViewBoxOffice adapter = new RecyclerViewBoxOffice(viewModel);
        adapter.setHasStableIds(true);
        binding.recyclerView.setAdapter(adapter);

        final Observable<Integer> trigger = RxRecyclerView.scrollEvents(binding.recyclerView)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@io.reactivex.annotations.NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                        return !recyclerViewScrollEvent.view().canScrollVertically(RecyclerView.FOCUS_DOWN);
                    }
                }).scan(0, new BiFunction<Integer, RecyclerViewScrollEvent, Integer>() {
                    @Override
                    public Integer apply(@io.reactivex.annotations.NonNull Integer integer,
                                         @io.reactivex.annotations.NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                        return integer + 1;
                    }
                });


        viewModel.listTrigger(trigger, 2);

        return binding.getRoot();
    }

    @Override
    public void render(ViewStateBoxOffice viewState) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
