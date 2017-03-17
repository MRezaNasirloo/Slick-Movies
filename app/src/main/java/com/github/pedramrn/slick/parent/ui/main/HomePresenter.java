package com.github.pedramrn.slick.parent.ui.main;

import android.util.Log;

import com.github.pedramrn.slick.parent.datasource.network.models.BoxOfficeItem;
import com.github.pedramrn.slick.parent.library.middleware.Callback;
import com.github.pedramrn.slick.parent.library.middleware.RequestData;
import com.github.pedramrn.slick.parent.library.middleware.RequestStack;
import com.github.pedramrn.slick.parent.ui.App;
import com.github.slick.SlickPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.flowable.FlowablePublish;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-02-28
 */
public class HomePresenter extends SlickPresenter<HomeView> {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private final BoxOfficeInteractor interactor;
    private final VoteInteractor voteInteractor;
    RequestStack requestStack = RequestStack.getInstance();

    private BehaviorSubject<List<BoxOfficeItem>> items = BehaviorSubject.create();

    @Inject
    public HomePresenter(BoxOfficeInteractor interactor, VoteInteractor voteInteractor) {
        this.interactor = interactor;
        this.voteInteractor = voteInteractor;
    }

    public void getBoxOffice() {
        interactor.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<BoxOfficeItem>>() {
                    @Override
                    public void onNext(List<BoxOfficeItem> boxOfficeItems) {
                        items.onNext(boxOfficeItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        items.onError(e);

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete() called");
                    }
                });
    }

    public void voteDown(String s) {
        voteInteractor.voteDown(new RequestData()).subscribe(new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called " + s);
            }

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe() called");
//                RequestStack.processLastRequest();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete() called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called");
            }
        });
    }

    public void voteUp() {
        voteInteractor.voteUp(new RequestData(), new Callback<String>() {
            @Override
            public void onPass(String s) {
                Log.d(TAG, "onPass() called");
            }
        });
    }

    @Override
    public void onViewUp(HomeView view) {
        super.onViewUp(view);
        requestStack.processLastRequest();
        Log.d(TAG, "onViewUp() called");
    }

    @Override
    public void onViewDown() {
        super.onViewDown();
        Log.d(TAG, "onViewDown() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    public BehaviorSubject<List<BoxOfficeItem>> updateStream() {
        return items;
    }

    public void navigate() {

    }
}
