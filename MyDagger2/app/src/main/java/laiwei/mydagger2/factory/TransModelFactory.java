package laiwei.mydagger2.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import laiwei.mydagger2.interact.FetchTransactionsInteract;
import laiwei.mydagger2.interact.FetchWalletInteract;
import laiwei.mydagger2.interact.GetDefaultWalletBalance;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.viewmodel.TransViewModel;

/**
 * Created by laiwei on 2018/3/29 0029.
 */
public class TransModelFactory implements ViewModelProvider.Factory{

    private final FetchWalletInteract fetchWalletInteract;
    private final NetworkInfo defaultNetworkInfo;
    private final GetDefaultWalletBalance getDefaultWalletBalance;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    public TransModelFactory(FetchWalletInteract fetchWalletInteract, NetworkInfo networkInfo, GetDefaultWalletBalance getDefaultWalletBalance, FetchTransactionsInteract fetchTransactionsInteract) {
        this.fetchWalletInteract = fetchWalletInteract;
        defaultNetworkInfo = networkInfo;
        this.getDefaultWalletBalance = getDefaultWalletBalance;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TransViewModel(fetchWalletInteract, defaultNetworkInfo, getDefaultWalletBalance, fetchTransactionsInteract);
    }
}
