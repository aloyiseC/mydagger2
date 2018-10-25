package laiwei.mydagger2.repository;

import io.reactivex.Single;
import laiwei.mydagger2.bean.Ticker;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.service.TickerService;

/**
 * Created by laiwei on 2018/3/30 0030.
 */
public class TickerRepository {
    private final TickerService service;
    private final NetworkInfo networkInfo;

    public TickerRepository(TickerService service, NetworkInfo networkInfo) {
        this.service = service;
        this.networkInfo = networkInfo;
    }

    public Single<Ticker> getTicker() {
        return Single.fromObservable(service
                .fetchTickerPrice(networkInfo.symbol));
    }

}
