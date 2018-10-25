package laiwei.mydagger2.repository;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import laiwei.mydagger2.bean.ServiceException;
import laiwei.mydagger2.bean.Transaction;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.network.NetworkInfo;
import laiwei.mydagger2.service.AccountService;

/**
 * Created by laiwei on 2018/3/30 0030.
 */
public class TransactionRepository {
    private final BlockExplorerClient blockExplorerClient;
    private final AccountService accountService;
    private final NetworkInfo defaultNetworkInfo;

    public TransactionRepository(BlockExplorerClient blockExplorerClient, AccountService accountService, NetworkInfo defaultNetworkInfo) {
        this.blockExplorerClient = blockExplorerClient;
        this.accountService = accountService;
        this.defaultNetworkInfo = defaultNetworkInfo;
    }

    public Observable<Transaction[]> fetchTransaction(Wallet wallet) {
        return Observable.create(e -> {
            Transaction[] transactions = blockExplorerClient.fetchTransactions(wallet.address).blockingFirst();
            e.onNext(transactions);
            e.onComplete();
        });
    }

    public Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password) {
        final Web3j web3j = Web3jFactory.build(new HttpService(defaultNetworkInfo.rpcServerUrl));

        return Single.fromCallable(() -> {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
                    .send();
            return ethGetTransactionCount.getTransactionCount();
        })
                .flatMap(nonce -> accountService.signTransaction(from, password, toAddress, subunitAmount, gasPrice, gasLimit, nonce.longValue(), data, defaultNetworkInfo.chainId))
                .flatMap(signedMessage -> Single.fromCallable( () -> {
                    EthSendTransaction raw = web3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new ServiceException(raw.getError().getMessage());
                    }
                    return raw.getTransactionHash();
                })).subscribeOn(Schedulers.io());
    }
}
