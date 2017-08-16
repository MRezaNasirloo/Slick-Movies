package com.github.pedramrn.slick.parent.ui.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.ui.search.state.ViewStateSearch;
import com.github.slick.Presenter;
import com.lapism.searchview.SearchView;
import com.xwray.groupie.GroupAdapter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class SearchViewImpl extends SearchView implements ViewSearch, Observer<ViewStateSearch>, SearchView.OnQueryTextListener,
        SearchView.OnOpenCloseListener {

    @Inject
    Provider<PresenterSearch> provider;
    @Presenter
    PresenterSearch presenter;

    PublishSubject<String> queryNewText = PublishSubject.create();
    PublishSubject<Boolean> openClose = PublishSubject.create();
    private GroupAdapter adapter = new GroupAdapter();
    private Disposable disposable;

    public SearchViewImpl(Context context) {
        super(context);
        adapter.setHasStableIds(true);
        setAdapter(adapter);
    }

    public SearchViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter.setHasStableIds(true);
        setAdapter(adapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        App.componentMain().inject(this);
        SearchViewImpl_Slick.bind(this);
        SearchViewImpl_Slick.onAttach(this);
        setOnQueryTextListener(this);
        setOnOpenCloseListener(this);
        Observable<String> queryNewText = this.queryNewText.filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                return s.length() > 2;
            }
        }).debounce(500, TimeUnit.MILLISECONDS);
        presenter.query(queryNewText, openClose);
        presenter.updateStream().subscribe(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SearchViewImpl_Slick.onDetach(this);
        dispose(disposable);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit() called with: query = [" + query + "]");
        // TODO: 2017-08-11 launch the search result controller
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.queryNewText.onNext(newText);
        Log.d(TAG, "onQueryTextChange() called with: newText = [" + newText + "]");
        // TODO: 2017-08-11 show suggestions
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(ViewStateSearch state) {
        adapter.clear();
        adapter.addAll(state.movies());
        renderError(state.errorMovies());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete() called");
    }

    private static final String TAG = SearchViewImpl.class.getSimpleName();

    protected void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    protected void renderError(@Nullable Throwable throwable) {
        if (throwable != null) {
            throwable.printStackTrace();
            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onClose() {
        openClose.onNext(false);
        return true;
    }

    @Override
    public boolean onOpen() {
        return false;
    }
}
