package laiwei.mydagger2.rx;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOperator;
import io.reactivex.observers.DisposableCompletableObserver;

/**
 * Created by laiwei on 2018/3/29 0029.
 */
public class CompletableErrorProxyOperator implements CompletableOperator {

    private final Throwable throwable;

    CompletableErrorProxyOperator(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public CompletableObserver apply(CompletableObserver observer) throws Exception {
        return new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                if (!isDisposed()) {
                    observer.onError(throwable);
                }
            }

            @Override
            public void onError(Throwable ex) {
                if (!isDisposed()) {
                    observer.onError(ex);
                }
            }
        };
    }
}
