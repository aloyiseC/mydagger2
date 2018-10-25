package laiwei.mydagger2.interact;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.rx.Operators;

/**
 * Created by laiwei on 2018/4/4 0004.
 */
public class ImportKeystoreInteract {
    private final AccountRespository accountRespository;
    private final PasswordStore passwordStore;

    public ImportKeystoreInteract(AccountRespository accountRespository, PasswordStore passwordStore) {
        this.accountRespository = accountRespository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet> importKeystore(String keystore, String password) {
        return passwordStore
                .generatePassword()
                .flatMap(newPassword -> accountRespository
                        .importKeystoreToWallet(keystore, password, newPassword)
                        .compose(Operators.savePassword(passwordStore, accountRespository, newPassword)))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
