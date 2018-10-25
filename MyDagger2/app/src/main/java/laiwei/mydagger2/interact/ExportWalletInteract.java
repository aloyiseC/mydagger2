package laiwei.mydagger2.interact;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;

/**
 * Created by laiwei on 2018/3/29 0029.
 */
public class ExportWalletInteract {

    private final AccountRespository accountRespository;
    private final PasswordStore passwordStore;

    public ExportWalletInteract(AccountRespository accountRespository, PasswordStore passwordStore) {
        this.accountRespository = accountRespository;
        this.passwordStore = passwordStore;
    }

    public Single<String> export(Wallet wallet, String backupPassword) {
        return passwordStore
                .getPassword(wallet)
                .flatMap(password -> accountRespository
                        .exportWallet(wallet, password, backupPassword))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
