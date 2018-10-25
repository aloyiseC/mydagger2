package laiwei.mydagger2.rx;

import io.reactivex.CompletableOperator;
import io.reactivex.SingleTransformer;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;

/**
 * Created by laiwei on 2018/3/27 0027.
 */
public class Operators {

    public static SingleTransformer<Wallet, Wallet> savePassword(
            PasswordStore passwordStore, AccountRespository walletRepository, String password) {
        return new SavePasswordOperator(passwordStore, walletRepository, password);
    }

    public static CompletableOperator completableErrorProxy(Throwable throwable) {
        return new CompletableErrorProxyOperator(throwable);
    }
}
