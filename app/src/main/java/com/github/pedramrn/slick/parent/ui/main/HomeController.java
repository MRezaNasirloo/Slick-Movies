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
import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.ui.App;
import com.github.slick.Presenter;
import com.github.slick.Slick;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-13
 */

public class HomeController extends Controller implements HomeView, Observer<List<BoxOfficeItem>> {

    private static final String TAG = HomeController.class.getSimpleName();

    @Inject
    Provider<HomePresenter> provider;
    @Presenter
    HomePresenter presenter;
    private TextView textView;
    private Button button;
    private Disposable disposable;
    private int scrollX;
    private int scrollY;


    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        App.getMainComponent(getRouter()).inject(this);
        Slick.bind(this);
        final View view = layoutInflater.inflate(R.layout.controller_home, viewGroup, false);
        textView = ((TextView) view.findViewById(R.id.text_view_temp));
        textView.setMovementMethod(new ScrollingMovementMethod());
        button = ((Button) view.findViewById(R.id.button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.voteDown("1");
//                Snackbar.make(getView(), "dadada", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        view.findViewById(R.id.button_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.navigate();
            }
        });
        return view;
    }

    @Override
    public void showData(List<BoxOfficeItem> items) {
        textView.setText(items.toString());
        textView.scrollTo(scrollX, scrollY);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = d;
        Log.d(TAG, "onSubscribe() called");
    }

    @Override
    public void onNext(List<BoxOfficeItem> boxOfficeItems) {
        showData(boxOfficeItems);
        Log.d(TAG, "onNext() called");
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
        scrollX = textView.getScrollX();
        scrollY = textView.getScrollY();
    }


}
