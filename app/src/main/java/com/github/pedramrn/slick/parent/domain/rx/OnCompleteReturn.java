package com.github.pedramrn.slick.parent.domain.rx;

import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * This operator provides you an opportunity to emmit one last item just before the stream shuts down.
 * <p>
 * <code>
 * A--B--C--D--E---> X
 * |
 * -------|---------
 * | OnCompleteReturn |
 * --------|--------
 * |
 * A--B--C--D--E---> C --> X
 * </code>
 *
 * @param <T>
 */
public abstract class OnCompleteReturn<T> implements ObservableOperator<T, T>, Function<Boolean, T> {
    public static final String TAG = OnCompleteReturn.class.getSimpleName();

    @Override
    public Observer<? super T> apply(@NonNull Observer<? super T> observer) throws Exception {
        return new Op(observer);
    }

    private final class Op implements Observer<T>, Disposable {
        final private Observer<? super T> actual;
        private Disposable d;
        private boolean error;

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
            error = true;
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            try {
                actual.onNext(apply(error));
            } catch (Exception e) {
                actual.onError(e);
            }
            actual.onComplete();
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
