package laiwei.mydagger2.module;

import dagger.Module;
import dagger.Provides;
import laiwei.mydagger2.factory.TransModelFactory;
import laiwei.mydagger2.interact.FetchTransactionsInteract;
import laiwei.mydagger2.interact.FetchWalletInteract;
import laiwei.mydagger2.interact.GetDefaultWalletBalance;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.repository.TickerRepository;
import laiwei.mydagger2.repository.TransactionRepository;
import laiwei.mydagger2.repository.UserRepository;

/**
 * Created by laiwei on 2018/3/29 0029.
 */
@Module
public class TransModule {
    @Provides
    public FetchWalletInteract getFetchUserInteract(UserRepository repository, AccountRespository accountRespository){
        return new FetchWalletInteract(repository, accountRespository);
    }
    @Provides
    public TransModelFactory getTransModelFactory(FetchWalletInteract fetchWalletInteract,NetworkInfo defaultNetworkInfo,GetDefaultWalletBalance getDefaultWalletBalance,
                                                  FetchTransactionsInteract fetchTransactionsInteract){
        return new TransModelFactory(fetchWalletInteract,defaultNetworkInfo, getDefaultWalletBalance, fetchTransactionsInteract);
    }
    @Provides
    public GetDefaultWalletBalance getGetDefaultWalletBalance(AccountRespository accountRespository, TickerRepository tickerRepository, NetworkInfo networkInfo){
        return new GetDefaultWalletBalance(accountRespository,tickerRepository,networkInfo);
    }
    @Provides
    public FetchTransactionsInteract getFetchTransactionsInteract(TransactionRepository transactionRepository) {
        return new FetchTransactionsInteract(transactionRepository);
    }
}
