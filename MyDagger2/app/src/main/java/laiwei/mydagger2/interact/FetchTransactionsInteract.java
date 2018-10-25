package laiwei.mydagger2.interact;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import laiwei.mydagger2.bean.Transaction;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.repository.TransactionRepository;

/**
 * Created by laiwei on 2018/3/30 0030.
 */
public class FetchTransactionsInteract {
    private final TransactionRepository transactionRepository;

    public FetchTransactionsInteract(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Observable<Transaction[]> fetch(Wallet wallet) {
        return transactionRepository
                .fetchTransaction(wallet)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
