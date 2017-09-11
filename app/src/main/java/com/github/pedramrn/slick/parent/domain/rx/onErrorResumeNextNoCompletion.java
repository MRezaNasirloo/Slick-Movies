package com.github.pedramrn.slick.parent.domain.rx;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-09-10
 */
public abstract class onErrorResumeNextNoCompletion<T> implements ObservableOperator<T, T>, Function<Throwable, T> {

    @Override
    public Observer<? super T> apply(Observer<? super T> observer) throws Exception {
        return new Op(observer);
    }

    private final class Op implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        Disposable s;

        Op(Observer<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            s = d;
            actual.onSubscribe(d);
        }

        @Override
        public void onNext(T downStream) {
            actual.onNext(downStream);
        }

        @Override
        public void onError(Throwable t) {
            try {
                actual.onNext(apply(t));
            } catch (Exception e) {
                actual.onError(e);
            }
        }

        @Override
        public void onComplete() {
//                actual.onComplete();
        }

        @Override
        public void dispose() {
            s.dispose();
        }

        @Override
        public boolean isDisposed() {
            return s.isDisposed();
        }
    }
}
