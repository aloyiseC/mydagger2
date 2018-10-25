package laiwei.mydagger2.module;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import laiwei.mydagger2.App;
import laiwei.mydagger2.C;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.pwd.PasswordStore;
import okhttp3.OkHttpClient;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
@Module
public class ToolsModule {
    @Singleton
    @Provides
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                //.addInterceptor(new LogInterceptor())
                .build();
    }

    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    PasswordStore getPasswordStore(Context context){
        return new PasswordStore(context);
    }

    @Singleton
    @Provides
    NetworkInfo getNetworkInfo(){
        return new NetworkInfo(C.ROPSTEN_NETWORK_NAME, C.ETH_SYMBOL,
                "https://ropsten.infura.io/llyrtzQ3YhkdESt2Fzrk",
                "https://ropsten.trustwalletapp.com/",
                "https://ropsten.etherscan.io",3, false);
    }


    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }
}
