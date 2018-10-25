package laiwei.mydagger2.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import laiwei.mydagger2.interact.ImportKeystoreInteract;
import laiwei.mydagger2.viewmodel.ImportKeystoreViewModel;

/**
 * Created by laiwei on 2018/4/4 0004.
 */
public class ImportModelFactory implements ViewModelProvider.Factory{

    private final ImportKeystoreInteract importKeystoreInteract;

    public ImportModelFactory(ImportKeystoreInteract importKeystoreInteract) {
        this.importKeystoreInteract = importKeystoreInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ImportKeystoreViewModel(importKeystoreInteract);
    }
}
