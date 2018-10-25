package laiwei.mydagger2.module;

import dagger.Module;
import dagger.Provides;
import laiwei.mydagger2.bean.Person;
import laiwei.mydagger2.bean.User;
import laiwei.mydagger2.factory.MainModelFactory;
import laiwei.mydagger2.interact.CreateWalletInteract;
import laiwei.mydagger2.interact.ExportWalletInteract;
import laiwei.mydagger2.interact.FetchWalletInteract;
import laiwei.mydagger2.pwd.PasswordStore;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.repository.UserRepository;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
@Module
public class MainModule {
    @Provides
    public User getUser(Person person){
        return new User(person);
    }

    @Provides
    public Person getPerson(){
        return new Person("messi",30);
    }

    @Provides
    public MainModelFactory getMaiModelFactory(FetchWalletInteract fetchWalletInteract, CreateWalletInteract createWalletInteract
    , ExportWalletInteract exportWalletInteract){
        return new MainModelFactory(fetchWalletInteract, createWalletInteract, exportWalletInteract);
    }

    @Provides
    public FetchWalletInteract getFetchUserInteract(UserRepository repository, AccountRespository accountRespository){
        return new FetchWalletInteract(repository, accountRespository);
    }

    @Provides
    public CreateWalletInteract getCreateWalletInteract(AccountRespository accountRespository, PasswordStore passwordStore){
        return new CreateWalletInteract(accountRespository,passwordStore);
    }

    @Provides
    public ExportWalletInteract getExportWalletInteract(AccountRespository accountRespository, PasswordStore passwordStore){
        return new ExportWalletInteract(accountRespository,passwordStore);
    }
}
