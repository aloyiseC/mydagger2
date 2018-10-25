package laiwei.mydagger2.repository;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import laiwei.mydagger2.bean.Transaction;
import laiwei.mydagger2.network.NetworkInfo;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by laiwei on 2018/3/30 0030.
 */
public class BlockExplorerClient {
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final NetworkInfo networkInfo;
    private EtherScanApiClient etherScanApiClient;

    public BlockExplorerClient(OkHttpClient httpClient, Gson gson, NetworkInfo networkInfo) {
        this.httpClient = httpClient;
        this.gson = gson;
        this.networkInfo = networkInfo;
        buildApiClient(networkInfo.backendUrl);
    }

    private void buildApiClient(String baseUrl) {
        etherScanApiClient = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(EtherScanApiClient.class);
    }

    public Observable<Transaction[]> fetchTransactions(String address) {
        return etherScanApiClient
                .fetchTransactions(address)
                .lift(apiError(gson))
                .map(r -> r.docs)
                .subscribeOn(Schedulers.io());
    }

    private static @NonNull
    <T> ApiErrorOperator<T> apiError(Gson gson) {
        return new ApiErrorOperator<>(gson);
    }

    private interface EtherScanApiClient {
        @GET("/transactions?limit=50") // TODO: startBlock - it's pagination. Not work now
        Observable<Response<EtherScanResponse>> fetchTransactions(
                @Query("address") String address);
    }

    private final static class EtherScanResponse {
        Transaction[] docs;
    }

    private final static class ApiErrorOperator <T> implements ObservableOperator<T, Response<T>> {

        private final Gson gson;

        public ApiErrorOperator(Gson gson) {
            this.gson = gson;
        }

        @Override
        public Observer<? super Response<T>> apply(Observer<? super T> observer) throws Exception {
            return new DisposableObserver<Response<T>>() {
                @Override
                public void onNext(Response<T> response) {
                    observer.onNext(response.body());
                    observer.onComplete();
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }

                @Override
                public void onComplete() {
                    observer.onComplete();
                }
            };
        }
    }
}
