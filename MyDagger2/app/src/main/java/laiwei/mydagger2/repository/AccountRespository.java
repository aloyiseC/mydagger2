package laiwei.mydagger2.repository;

import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.service.AccountService;
import okhttp3.OkHttpClient;

/**
 * Created by laiwei on 2018/3/26 0026.
 */
public class AccountRespository {

    private final AccountService service;
    private final OkHttpClient httpClient;
    private final NetworkInfo networkInfo;

    public AccountRespository(AccountService service, OkHttpClient httpClient, NetworkInfo networkInfo) {
        this.service = service;
        this.httpClient = httpClient;
        this.networkInfo = networkInfo;
    }

    public Single<Wallet[]> fetchWallets(){
        return service.fetchAccounts();
    }

    public Single<Wallet> createWallet(String password) {
        return service
                .createAccount(password);
    }

    public Single<String> exportWallet(Wallet wallet, String password, String newPassword) {
        return service.exportAccount(wallet, password, newPassword);
    }

    public Single<Wallet> findWallet(String address) {
        return fetchWallets()
                .flatMap(accounts -> {
                    for (Wallet wallet : accounts) {
                        if (wallet.sameAddress(address)) {
                            return Single.just(wallet);
                        }
                    }
                    return null;
                });
    }

    public Completable deleteWallet(String address, String password) {
        return service.deleteAccount(address, password);
    }

    public Single<BigInteger> balanceInWei(Wallet wallet) {
        return Single.fromCallable(() -> Web3jFactory
                .build(new HttpService(networkInfo.rpcServerUrl, httpClient, false))
                .ethGetBalance(wallet.address, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io());
    }

    public Single<Wallet> importKeystoreToWallet(String store, String password, String newPassword) {
        return service.importKeystore(store, password, newPassword);
    }
}
