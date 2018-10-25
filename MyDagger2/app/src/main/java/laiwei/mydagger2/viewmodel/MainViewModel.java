package laiwei.mydagger2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import laiwei.mydagger2.C;
import laiwei.mydagger2.bean.ErrorEnvelope;
import laiwei.mydagger2.bean.Person;
import laiwei.mydagger2.bean.User;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.interact.CreateWalletInteract;
import laiwei.mydagger2.interact.ExportWalletInteract;
import laiwei.mydagger2.interact.FetchWalletInteract;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
public class MainViewModel extends ViewModel {
    private final FetchWalletInteract fetchWalletInteract;
    private final CreateWalletInteract createWalletInteract;
    private final ExportWalletInteract exportWalletInteract;
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> createWalletError = new MutableLiveData<>();
    private MutableLiveData<Wallet> createdWallet = new MutableLiveData<>();
    private MutableLiveData<String> exportedWallet = new MutableLiveData<>();
    private final MutableLiveData<ErrorEnvelope> exportWalletError = new MutableLiveData<>();

    public MainViewModel(FetchWalletInteract fetchWalletInteract, CreateWalletInteract createWalletInteract, ExportWalletInteract exportWalletInteract) {
        this.fetchWalletInteract = fetchWalletInteract;
        this.createWalletInteract = createWalletInteract;
        this.exportWalletInteract = exportWalletInteract;
        fetchWalletInteract.fetch().subscribe(userMutableLiveData::postValue,this::onError);
        fetchAccounts();
    }

    private void onError(Throwable throwable) {
        userMutableLiveData.postValue(new User(new Person("error",0)));
    }

    private void onAccountError(Throwable throwable) {
        wallets.postValue(new Wallet[0]);
    }

    public LiveData<User> users() {
        return userMutableLiveData;
    }

    public LiveData<Wallet[]> wallets(){
        return wallets;
    }

    public void createWallet(){
        createWalletInteract.create().subscribe(account -> {fetchAccounts();
            createdWallet.postValue(account);},this::onCreateWalletError);
    }

    private void onCreateWalletError(Throwable throwable) {
        createWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }

    public void fetchAccounts(){
        fetchWalletInteract.fetchAccounts().subscribe(wallets::postValue,this::onAccountError);
    }

    public LiveData<Wallet> createadWallet(){
        return createdWallet;
    }

    public LiveData<String> exportedWallet(){
        return exportedWallet;
    }


    public void exportWallet(Wallet wallet,String password) {
          exportWalletInteract.export(wallet,password)
                  .subscribe(exportedWallet::postValue,this::onExportError);
    }

    private void onExportError(Throwable throwable) {
        exportWalletError.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null));
    }
}
