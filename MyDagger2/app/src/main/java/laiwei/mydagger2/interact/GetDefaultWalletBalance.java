package laiwei.mydagger2.interact;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import laiwei.mydagger2.C;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.repository.TickerRepository;
import laiwei.mydagger2.util.BalanceUtils;

/**
 * Created by laiwei on 2018/3/30 0030.
 */
public class GetDefaultWalletBalance {
    private final AccountRespository accountRespository;
    private final TickerRepository tickerRepository;
    private final NetworkInfo networkInfo;

    public GetDefaultWalletBalance(AccountRespository accountRespository, TickerRepository tickerRepository, NetworkInfo networkInfo) {
        this.accountRespository = accountRespository;
        this.tickerRepository = tickerRepository;
        this.networkInfo = networkInfo;
    }

    public Single<Map<String, String>> get(Wallet wallet) {
        return accountRespository.balanceInWei(wallet)
                .flatMap(ethBallance -> {
                    Map<String, String> balances = new HashMap<>();
                    balances.put(networkInfo.symbol, BalanceUtils.weiToEth(ethBallance, 5));
                    return Single.just(balances);
                })
                .flatMap(balances -> tickerRepository
                        .getTicker()
                        .observeOn(Schedulers.io())
                        .flatMap(ticker -> {
                            String ethBallance = balances.get(networkInfo.symbol);
                            balances.put(C.USD_SYMBOL, BalanceUtils.ethToUsd(ticker.price, ethBallance));
                            return Single.just(balances);
                        })
                        .onErrorResumeNext(throwable -> Single.just(balances)))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
