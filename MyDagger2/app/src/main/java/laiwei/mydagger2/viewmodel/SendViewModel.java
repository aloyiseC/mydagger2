package laiwei.mydagger2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.math.BigInteger;

import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.interact.SendInteract;

/**
 * Created by laiwei on 2018/4/3 0003.
 */
public class SendViewModel extends BaseViewModel{
    private final SendInteract sendInteract;
    private final MutableLiveData<String> newTransaction = new MutableLiveData<>();

    public SendViewModel(SendInteract sendInteract) {
        this.sendInteract = sendInteract;
    }

    public void createTransaction(String from, String to, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit) {
        progress.postValue(true);
        disposable = sendInteract
                .create(new Wallet(from), to, amount, gasPrice, gasLimit, null)
                .subscribe(this::onCreateTransaction, this::onError);
    }

    private void onCreateTransaction(String transaction) {
        progress.postValue(false);
        newTransaction.postValue(transaction);
    }

    public LiveData<String> sendTransaction() { return newTransaction; }
}
