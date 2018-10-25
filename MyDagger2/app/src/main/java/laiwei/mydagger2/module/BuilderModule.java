package laiwei.mydagger2.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import laiwei.mydagger2.ui.ImportKeystoreActivity;
import laiwei.mydagger2.ui.MainActivity;
import laiwei.mydagger2.ui.SendActivity;
import laiwei.mydagger2.ui.TransactionActivity;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
@Module
public abstract class BuilderModule {
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity bindMainModule();
    @ContributesAndroidInjector(modules = TransModule.class)
    abstract TransactionActivity bindTransModule();
    @ContributesAndroidInjector(modules = SendModule.class)
    abstract SendActivity bindSendActivity();
    @ContributesAndroidInjector(modules = ImportKeystoreModule.class)
    abstract ImportKeystoreActivity bindImportKeystoreActivity();
}
