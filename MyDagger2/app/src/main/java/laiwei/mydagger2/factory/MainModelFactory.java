package laiwei.mydagger2.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import laiwei.mydagger2.interact.CreateWalletInteract;
import laiwei.mydagger2.interact.ExportWalletInteract;
import laiwei.mydagger2.interact.FetchWalletInteract;
import laiwei.mydagger2.viewmodel.MainViewModel;

/**
 * Created by laiwei on 2018/3/26 0026.
 */
public class MainModelFactory implements ViewModelProvider.Factory{

    private final FetchWalletInteract fetchWalletInteract;
    private final CreateWalletInteract createWalletInteract;
    private final ExportWalletInteract exportWalletInteract;

    public MainModelFactory(FetchWalletInteract fetchWalletInteract, CreateWalletInteract createWalletInteract, ExportWalletInteract exportWalletInteract) {
        this.fetchWalletInteract = fetchWalletInteract;
        this.createWalletInteract = createWalletInteract;
        this.exportWalletInteract = exportWalletInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(fetchWalletInteract, createWalletInteract, exportWalletInteract);
    }
}
