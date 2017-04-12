package com.github.pedramrn.slick.parent.ui.main;

import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.github.pedramrn.slick.parent.R;
import com.github.pedramrn.slick.parent.ui.App;
import com.github.slick.Presenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-13
 */

public class HomeController extends Controller implements HomeView, Observer<HomeViewState> {

    private static final String TAG = HomeController.class.getSimpleName();

    @Inject
    Provider<HomePresenter> provider;
    @Presenter
    HomePresenter presenter;
    private TextView textView;
    private Button button;
    CompositeDisposable disposable = new CompositeDisposable();


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        App.getMainComponent(getRouter()).inject(this);
        HomeController_Slick.bind(this);
        final View view = layoutInflater.inflate(R.layout.controller_home, viewGroup, false);
        textView = ((TextView) view.findViewById(R.id.text_view_temp));
        textView.setMovementMethod(new ScrollingMovementMethod());
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
        final Observable<Object> observable = RxView.clicks(view.findViewById(R.id.button_more)).debounce(500, TimeUnit.MILLISECONDS);
            @Override
            public void onClick(View v) {
//                presenter.getBoxOffice();
                presenter.getMore(observable);

            }
        });



        return view;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable.add(d);
        Log.d(TAG, "onSubscribe() called");
    }

    @Override
    public void onNext(HomeViewState viewState) {
        render(viewState);
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError() called");
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    @Override
    protected void onAttach(@NonNull View view) {
        Log.d(TAG, "onAttach() called");
        super.onAttach(view);
        presenter.updateStream().subscribe(this);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        Log.d(TAG, "onDetach() called");
        super.onDetach(view);
        disposable.dispose();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.disposeMainComponent();
    }

    @Override
    public void render(HomeViewState viewState) {
        textView.setText(String.format(Locale.ENGLISH, "size: %d, content: %s", viewState.movieItems().size(), viewState.movieItems().toString()));

    }
}
