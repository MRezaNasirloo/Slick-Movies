package com.github.pedramrn.slick.parent.ui.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.ui.search.state.ViewStateSearch;
import com.github.pedramrn.slick.parent.util.UtilsRx;
import com.github.slick.OnDestroyListener;
import com.github.slick.Presenter;
import com.lapism.searchview.SearchView;
import com.xwray.groupie.GroupAdapter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class SearchViewImpl extends SearchView implements ViewSearch, Observer<ViewStateSearch>, SearchView.OnQueryTextListener,
        SearchView.OnOpenCloseListener, OnDestroyListener {

    @Inject
    Provider<PresenterSearch> provider;
    @Presenter
    PresenterSearch presenter;

    PublishSubject<String> queryNewText = PublishSubject.create();
    PublishSubject<Boolean> openClose = PublishSubject.create();

    private GroupAdapter adapter = new GroupAdapter();
    private Disposable disposable;
    private Observable<String> queryNewText1;

    public SearchViewImpl(Context context) {
        super(context);
        init();
    }

    public SearchViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        queryNewText1 = queryNewText.filter(s -> s.length() > 2).debounce(500, TimeUnit.MILLISECONDS);
        adapter.setHasStableIds(true);
        setAdapter(adapter);
        setOnQueryTextListener(this);
        setOnOpenCloseListener(this);
        mSearchEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        App.componentMain().inject(this);
        SearchViewImpl_Slick.bind(this);
        SearchViewImpl_Slick.onAttach(this);
        if (!isInEditMode()) {
            presenter.updateStream().subscribe(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        adapter.setOnItemClickListener(null);
        adapter = null;
        SearchViewImpl_Slick.onDetach(this);
        UtilsRx.dispose(disposable);
    }

    @Override
    public void onDestroy() {
        SearchViewImpl_Slick.onDestroy(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit() called with: start = [" + query + "]");
        // TODO: 2017-08-11 launch the search result controller
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange() called");
        this.queryNewText.onNext(newText);
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

    protected void renderError(@Nullable Throwable throwable) {
        // TODO: 2017-11-15 show error as a list item
        if (throwable != null) {
            if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
                Toast.makeText(getContext().getApplicationContext(), "Network Error, Are you Connected?", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext().getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                // TODO: 2017-11-13 log to fabric
            }
        }
    }

    @Override
    public boolean onClose() {
        Log.d(TAG, "onClose() called");
        openClose.onNext(false);
        return true;
    }

    @Override
    public boolean onOpen() {
        return false;
    }

    @Override
    public Observable<String> queryNexText() {
        return queryNewText1;
    }

    @Override
    public Observable<Boolean> searchOpenClose() {
        return openClose;
    }
}
