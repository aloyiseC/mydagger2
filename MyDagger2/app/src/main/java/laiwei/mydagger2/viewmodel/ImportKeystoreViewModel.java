package laiwei.mydagger2.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.interact.ImportKeystoreInteract;

/**
 * Created by laiwei on 2018/4/4 0004.
 */
public class ImportKeystoreViewModel extends BaseViewModel{
    private final ImportKeystoreInteract importKeystoreInteract;
    private MutableLiveData<Wallet> importedWallet = new MutableLiveData<>();
    public ImportKeystoreViewModel(ImportKeystoreInteract importKeystoreInteract) {
        this.importKeystoreInteract = importKeystoreInteract;
    }

    public LiveData<Wallet> importedWallet(){
        return importedWallet;
    }

    public void importKeystore(String kestore,String password){
        importKeystoreInteract.importKeystore(kestore,password).subscribe(wallet -> importedWallet.postValue(wallet));
    }
}
