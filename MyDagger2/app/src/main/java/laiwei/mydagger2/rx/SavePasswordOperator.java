package laiwei.mydagger2.rx;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.observers.DisposableCompletableObserver;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;

/**
 * Created by laiwei on 2018/3/27 0027.
 */
public class SavePasswordOperator implements SingleTransformer<Wallet, Wallet> {

    private final PasswordStore passwordStore;
    private final String password;
    private final AccountRespository accountRespository;

    public SavePasswordOperator(
            PasswordStore passwordStore, AccountRespository accountRespository, String password) {
        this.passwordStore = passwordStore;
        this.password = password;
        this.accountRespository = accountRespository;
    }

    @Override
    public SingleSource<Wallet> apply(Single<Wallet> upstream) {
        Wallet wallet = upstream.blockingGet();
        return passwordStore
                .setPassword(wallet, password)
                .onErrorResumeNext(err -> accountRespository.deleteWallet(wallet.address, password)
                        .lift(observer -> new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                observer.onError(err);
                            }

                            @Override
                            public void onError(Throwable e) {
                                observer.onError(e);
                            }
                        }))
                .toSingle(() -> wallet);
    }
}
