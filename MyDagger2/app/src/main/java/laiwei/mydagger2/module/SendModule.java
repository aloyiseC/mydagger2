package laiwei.mydagger2.module;

import dagger.Module;
import dagger.Provides;
import laiwei.mydagger2.factory.SendModelFactory;
import laiwei.mydagger2.interact.SendInteract;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.TransactionRepository;

/**
 * Created by laiwei on 2018/4/3 0003.
 */
@Module
public class SendModule{
    @Provides
    SendInteract getSendInteract(TransactionRepository transactionRepository, PasswordStore passwordStore){
        return new SendInteract(transactionRepository, passwordStore);
    }

    @Provides
    SendModelFactory getSendModeFactory(SendInteract sendInteract){
        return new SendModelFactory(sendInteract);
    }
}
