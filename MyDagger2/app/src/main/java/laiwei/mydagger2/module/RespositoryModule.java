package laiwei.mydagger2.module;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.repository.BlockExplorerClient;
import laiwei.mydagger2.repository.TickerRepository;
import laiwei.mydagger2.repository.TransactionRepository;
import laiwei.mydagger2.repository.UserRepository;
import laiwei.mydagger2.service.AccountService;
import laiwei.mydagger2.service.TickerService;
import laiwei.mydagger2.service.UserService;
import okhttp3.OkHttpClient;

/**
 * Created by laiwei on 2018/3/26 0026.
 */
@Module
public class RespositoryModule {
    @Singleton
    @Provides
    public UserRepository getUserRepository(UserService service){
        return new UserRepository(service);
    }
    @Singleton
    @Provides
    public UserService getUserService(){
        return new UserService();
    }

    @Singleton
    @Provides
    AccountService provideAccountKeyStoreService(Context context) {
        File file = new File(context.getFilesDir(), "keystore/keystore");
        return new AccountService(file);
    }

    @Singleton
    @Provides
    AccountRespository getAccountRespository(AccountService service, OkHttpClient httpClient
    , NetworkInfo networkInfo){
        return new AccountRespository(service, httpClient, networkInfo);
    }

    @Singleton
    @Provides
    public TickerService getTickerService(OkHttpClient client, Gson gson){
        return new TickerService(client,gson);
    }

    @Singleton
    @Provides
    public TickerRepository getTickerRepository(TickerService service, NetworkInfo networkInfo){
        return new TickerRepository(service,networkInfo);
    }
    @Singleton
    @Provides
    public BlockExplorerClient getBlockExplorerClient(OkHttpClient httpClient, Gson gson, NetworkInfo networkInfo) {
        return new BlockExplorerClient(httpClient,gson,networkInfo);
    }
    @Singleton
    @Provides
    public TransactionRepository getTransactionRepository(BlockExplorerClient blockExplorerClient,AccountService accountService,NetworkInfo defaultNetworkInfo) {
        return new TransactionRepository(blockExplorerClient, accountService, defaultNetworkInfo);
    }
}
