package com.github.pedramrn.slick.parent.ui.search;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.github.pedramrn.slick.parent.App;
import com.github.pedramrn.slick.parent.ui.error.ErrorHandler;
import com.github.slick.OnDestroyListener;
import com.github.slick.Presenter;
import com.lapism.searchview.SearchView;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-11
 */

public class SearchViewImpl extends SearchView implements ViewSearch, SearchView.OnQueryTextListener,
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
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        adapter.setOnItemClickListener(null);
        adapter = null;
        SearchViewImpl_Slick.onDetach(this);
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

    private static final String TAG = SearchViewImpl.class.getSimpleName();

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

    @Override
    public void update(List<Item> items) {
        adapter.clear();
        adapter.addAll(items);
    }

    @Override
    public void showLoading(boolean isLoading) {
        if (isLoading) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    @Override
    public void error(short code) {
        Context context = getContext().getApplicationContext();
        Toast.makeText(context, ErrorHandler.message(context, code), Toast.LENGTH_SHORT).show();
    }
}
