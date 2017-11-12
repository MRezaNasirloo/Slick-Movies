package com.github.pedramrn.slick.parent.domain.rx;

import com.github.pedramrn.slick.parent.util.UtilsRx;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * This operator passes the stream data as it received it,
 * after that it passes the modified version through the provided apply method.
 * <p>
 * <code>
 * A--B--C--D--E---> X
 * |
 * -------|---------
 * | passThroughMap |
 * --------|--------
 * |
 * A--a--B--b--C--c--D--d--E--e--> X
 * </code>
 *
 * @param <T>
 */
public abstract class PassThroughMap<T> implements ObservableOperator<T, T>, Function<T, Observable<T>> {
    @Override
    public Observer<? super T> apply(@NonNull Observer<? super T> observer) throws Exception {
        return new Op(observer);
    }

    private final class Op implements Observer<T>, Disposable {
        final private Observer<? super T> actual;
        private Disposable d;
        private List<Disposable> disposables;
        private AtomicInteger wip = new AtomicInteger(0);
        private boolean completed;
        private boolean done;

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
            try {
                System.out.println("Op.onNext " + hashCode());
                wip.incrementAndGet();
                actual.onNext(downStream);
                apply(downStream).subscribe(new Observer<T>() {
                    private Disposable dis;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.dis = d;
                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        System.out.println("Op.onNext.anonymous " + hashCode());
                        actual.onNext(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("Op.OnError.anonymous " + hashCode());
                        Op.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Op.onComplete.anonymous1 " + hashCode());
                        wip.decrementAndGet();
                        UtilsRx.dispose(this.dis);
                        if (done && !completed && wip.intValue() == 0) {
                            System.out.println("Op.onComplete.anonymous2 " + hashCode());
                            Op.this.onComplete();
                        }
                    }
                });
            } catch (Exception e) {
                System.out.println("Op.OnError.anonymous.catch " + hashCode());
                Op.this.onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("Op.onError1 " + hashCode());
            // actual.onError(t);
            synchronized (this) {//possible fix, swallows the errors after termination
                if (!completed) {
                    System.out.println("Op.onError2 " + hashCode());
                    completed = true;
                    actual.onError(t);
                }
            }
        }

        @Override
        public void onComplete() {
            System.out.println("Op.onComplete1 " + hashCode());
            synchronized (actual) {
                if (!completed && wip.intValue() == 0) {
                    System.out.println("Op.onComplete2 " + hashCode());
                    actual.onComplete();
                    UtilsRx.dispose(d);
                    completed = true;
                }
            }
            done = true;
        }

        @Override
        public void dispose() {
            d.dispose();
            System.out.println("Op.dispose " + hashCode());
            completed = true;
        }

        @Override
        public boolean isDisposed() {
            System.out.println("Op.isDisposed " + hashCode());
            return d.isDisposed();
        }
    }
}
