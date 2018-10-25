package laiwei.mydagger2.module;

import dagger.Module;
import dagger.Provides;
import laiwei.mydagger2.factory.ImportModelFactory;
import laiwei.mydagger2.interact.ImportKeystoreInteract;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;

/**
 * Created by laiwei on 2018/4/4 0004.
 */
@Module
public class ImportKeystoreModule {
   @Provides
   public ImportModelFactory getImportModelFactory(ImportKeystoreInteract importKeystoreInteract){
       return new ImportModelFactory(importKeystoreInteract);
   }
    @Provides
    public  ImportKeystoreInteract getImportKeystoreInteract(AccountRespository accountRespository, PasswordStore passwordStore){
        return new ImportKeystoreInteract(accountRespository,passwordStore);
    }
}
