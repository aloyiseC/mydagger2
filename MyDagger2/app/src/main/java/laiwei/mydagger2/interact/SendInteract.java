package laiwei.mydagger2.interact;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.TransactionRepository;

/**
 * Created by laiwei on 2018/4/3 0003.
 */
public class SendInteract {
    private final TransactionRepository transactionRepository;
    private final PasswordStore passwordStore;

    public SendInteract(TransactionRepository transactionRepository, PasswordStore passwordStore) {
        this.transactionRepository = transactionRepository;
        this.passwordStore = passwordStore;
    }

    public Single<String> create(Wallet from, String to, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data) {
        return passwordStore.getPassword(from)
                .flatMap(password ->
                        transactionRepository.createTransaction(from, to, subunitAmount, gasPrice, gasLimit, data, password)
                                .observeOn(AndroidSchedulers.mainThread()));
    }
}
