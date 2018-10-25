package laiwei.mydagger2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import laiwei.mydagger2.bean.Transaction;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.interact.FetchTransactionsInteract;
import laiwei.mydagger2.interact.FetchWalletInteract;
import laiwei.mydagger2.interact.GetDefaultWalletBalance;
import laiwei.mydagger2.network.NetworkInfo;

/**
 * Created by laiwei on 2018/3/29 0029.
 */
public class TransViewModel extends BaseViewModel{
    private static final long GET_BALANCE_INTERVAL = 8;
    private static final long FETCH_TRANSACTIONS_INTERVAL = 10;
    private final FetchWalletInteract fetchWalletInteract;
    private MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final NetworkInfo defaultNetworkInfo;
    private final MutableLiveData<Map<String, String>> defaultWalletBalance = new MutableLiveData<>();
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final FetchTransactionsInteract fetchTransactionsInteract;
    private final MutableLiveData<Transaction[]> transactions = new MutableLiveData<>();

    public TransViewModel(FetchWalletInteract fetchWalletInteract, NetworkInfo defaultNetworkInfo, GetDefaultWalletBalance getDefaultWalletBalance, FetchTransactionsInteract fetchTransactionsInteract) {
        this.fetchWalletInteract = fetchWalletInteract;
        this.defaultNetworkInfo = defaultNetworkInfo;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
    }

//    public LiveData<Wallet> defaultWallet(){
//        return defaultWallet;
//    }

    public void setDefaultWallet(Wallet wallet){
        defaultWallet.setValue(wallet);
        getDefaultBalance();
        fetchTransactions();
    }

//    public void fetchAccounts(){
//        fetchWalletInteract.fetchAccounts().subscribe(this::setDefaultWallet,this::onAccountError);
//    }

//    private void setDefaultWallet(Wallet[] wallets){
//        defaultWallet.postValue(wallets[0]);
//    }

    private void onAccountError(Throwable throwable) {

    }

    public LiveData<Map<String, String>> balance(){
        return defaultWalletBalance;
    }

    public NetworkInfo getDefaultNetworkInfo(){
        return defaultNetworkInfo;
    }

    public void getDefaultBalance() {
        disposable = Observable.interval(0, GET_BALANCE_INTERVAL, TimeUnit.SECONDS)
                .doOnNext(l -> getDefaultWalletBalance
                        .get(defaultWallet.getValue())
                        .subscribe(defaultWalletBalance::postValue, t -> {}))
                .subscribe();
    }

    public void fetchTransactions(){
        disposable = Observable.interval(0, FETCH_TRANSACTIONS_INTERVAL, TimeUnit.SECONDS)
                .doOnNext(l ->
                        fetchTransactionsInteract
                                .fetch(defaultWallet.getValue()/*new Wallet("0x60f7a1cbc59470b74b1df20b133700ec381f15d3")*/)
                                .subscribe(this::onTransactions, this::onError))
                .subscribe();
    }

    private void onTransactions(Transaction[] trans) {
        transactions.postValue(trans);
    }

    public LiveData<Transaction[]> transactions(){
        return transactions;
    }
}
