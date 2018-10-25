package laiwei.mydagger2.interact;

import io.reactivex.Single;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.rx.Operators;

/**
 * Created by laiwei on 2018/3/27 0027.
 */
public class CreateWalletInteract {
    private final AccountRespository accountRespository;
    private final PasswordStore passwordStore;

    public CreateWalletInteract(AccountRespository accountRespository, PasswordStore passwordStore) {
        this.accountRespository = accountRespository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet> create() {
        return passwordStore.generatePassword()
                .flatMap(masterPassword -> accountRespository
                        .createWallet(masterPassword)
                        .compose(Operators.savePassword(passwordStore, accountRespository, masterPassword))
                        .flatMap(wallet -> passwordVerification(wallet, masterPassword)));
    }

    private Single<Wallet> passwordVerification(Wallet wallet, String masterPassword) {
        return passwordStore
                .getPassword(wallet)
                .flatMap(password -> accountRespository
                        .exportWallet(wallet, password, password)
                        .flatMap(keyStore -> accountRespository.findWallet(wallet.address)))
                .onErrorResumeNext(throwable -> accountRespository
                        .deleteWallet(wallet.address, masterPassword)
                        .lift(Operators.completableErrorProxy(throwable))
                        .toSingle(() -> wallet));
    }
}
