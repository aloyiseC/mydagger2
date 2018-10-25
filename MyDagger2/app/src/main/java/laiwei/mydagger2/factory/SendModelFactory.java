package laiwei.mydagger2.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import laiwei.mydagger2.interact.SendInteract;
import laiwei.mydagger2.viewmodel.SendViewModel;

/**
 * Created by laiwei on 2018/4/3 0003.
 */
public class SendModelFactory implements ViewModelProvider.Factory{

    private final SendInteract sendInteract;

    public SendModelFactory(SendInteract sendInteract) {
        this.sendInteract = sendInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SendViewModel(sendInteract);
    }
}
