package com.github.pedramrn.slick.parent.ui.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.pedramrn.slick.parent.App;
import com.github.slick.Presenter;
import com.lapism.searchview.SearchView;
import com.xwray.groupie.GroupAdapter;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class SearchViewImpl extends SearchView implements ViewSearch, Observer<ViewStateSearch>, SearchView.OnQueryTextListener {

    @Inject
    Provider<PresenterSearch> provider;
    @Presenter
    PresenterSearch presenter;

    public SearchViewImpl(Context context) {
        super(context);
    }

    public SearchViewImpl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchViewImpl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchViewImpl(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        App.componentMain().inject(this);
        SearchViewImpl_Slick.bind(this);
        setOnQueryTextListener(this);
        GroupAdapter adapter = new GroupAdapter();
        setAdapter(adapter);
        presenter.updateStream().subscribe(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO: 2017-08-11 launch the search result controller
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // TODO: 2017-08-11 show suggestions
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ViewStateSearch viewStateSearch) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
