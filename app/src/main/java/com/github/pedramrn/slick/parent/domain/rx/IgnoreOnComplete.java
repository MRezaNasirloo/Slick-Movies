package com.github.pedramrn.slick.parent.domain.rx;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * This operator Ignores the termination of an onComplete call.
 * <p>
 * <code>
 * A--B--C--D--E---> X
 * |
 * -------|---------
 * | IgnoreOnComplete |
 * --------|--------
 * |
 * A--B--C--D--E--->
 * </code>
 *
 * @param <T>
 */
public abstract class IgnoreOnComplete<T> implements ObservableOperator<T, T> {
    public static final String TAG = IgnoreOnComplete.class.getSimpleName();

    @Override
    public Observer<? super T> apply(@NonNull Observer<? super T> observer) throws Exception {
        return new Op(observer);
    }

    private final class Op implements Observer<T>, Disposable {
        final private Observer<? super T> actual;
        private Disposable d;

        Op(Observer<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            this.d = d;
            actual.onSubscribe(d);
        }

        @Override
        public void onNext(T downStream) {
            actual.onNext(downStream);
        }

        @Override
        public void onError(Throwable t) {
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            // no-op
            // actual.onComplete();
        }

        @Override
        public void dispose() {
            d.dispose();
        }

        @Override
        public boolean isDisposed() {
            return d.isDisposed();
        }
    }
}
