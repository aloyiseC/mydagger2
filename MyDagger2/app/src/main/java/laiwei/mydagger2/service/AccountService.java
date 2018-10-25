package laiwei.mydagger2.service;

import android.util.Log;

import org.ethereum.geth.Accounts;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Transaction;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import laiwei.mydagger2.bean.ServiceException;
import laiwei.mydagger2.bean.Wallet;


/**
 * Created by laiwei on 2018/3/26 0026.
 */
public class AccountService {

    private final KeyStore keyStore;

    public AccountService(File keyStoreFile) {
        keyStore = new KeyStore(keyStoreFile.getAbsolutePath(), Geth.LightScryptN, Geth.LightScryptP);
    }

    public Single<Wallet[]> fetchAccounts() {
        return Single.fromCallable(() -> {
            Accounts accounts = keyStore.getAccounts();
            int len = (int) accounts.size();
            Wallet[] result = new Wallet[len];
            Log.i("step","len:"+len);
            for (int i = 0; i < len; i++) {
                org.ethereum.geth.Account gethAccount = accounts.get(i);
                result[i] = new Wallet(gethAccount.getAddress().getHex().toLowerCase());
            }
            return result;
        })
                .subscribeOn(Schedulers.io());
    }

    public Single<Wallet> createAccount(String password) {
        return Single.fromCallable(() -> new Wallet(
                keyStore.newAccount(password).getAddress().getHex().toLowerCase()))
                .subscribeOn(Schedulers.io());
    }

    public Single<String> exportAccount(Wallet wallet, String password, String newPassword) {
        return Single
                .fromCallable(() -> findAccount(wallet.address))
                .flatMap(account1 -> Single.fromCallable(()
                        -> new String(keyStore.exportKey(account1, password, newPassword))))
                .subscribeOn(Schedulers.io());
    }

    private org.ethereum.geth.Account findAccount(String address) throws ServiceException {
        Accounts accounts = keyStore.getAccounts();
        int len = (int) accounts.size();
        for (int i = 0; i < len; i++) {
            try {
                android.util.Log.d("ACCOUNT_FIND", "Address: " + accounts.get(i).getAddress().getHex());
                if (accounts.get(i).getAddress().getHex().equalsIgnoreCase(address)) {
                    return accounts.get(i);
                }
            } catch (Exception ex) {
                /* Quietly: interest only result, maybe next is ok. */
            }
        }
        throw new ServiceException("Wallet with address: " + address + " not found");
    }

    public Completable deleteAccount(String address, String password) {
        return Single.fromCallable(() -> findAccount(address))
                .flatMapCompletable(account -> Completable.fromAction(
                        () -> keyStore.deleteAccount(account, password)))
                .subscribeOn(Schedulers.io());
    }

    public Single<byte[]> signTransaction(Wallet signer, String signerPassword, String toAddress, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, long nonce, byte[] data, long chainId) {
        return Single.fromCallable(() -> {
            BigInt value = new BigInt(0);
            value.setString(amount.toString(), 10);

            BigInt gasPriceBI = new BigInt(0);
            gasPriceBI.setString(gasPrice.toString(), 10);

            BigInt gasLimitBI = new BigInt(0);
            gasLimitBI.setString(gasLimit.toString(), 10);

            Transaction tx = new Transaction(
                    nonce,
                    new Address(toAddress),
                    value,
                    gasLimitBI,
                    gasPriceBI,
                    data);

            BigInt chain = new BigInt(chainId); // Chain identifier of the main net
            org.ethereum.geth.Account gethAccount = findAccount(signer.address);
            keyStore.unlock(gethAccount, signerPassword);
            Transaction signed = keyStore.signTx(gethAccount, tx, chain);
            keyStore.lock(gethAccount.getAddress());

            return signed.encodeRLP();
        })
                .subscribeOn(Schedulers.io());
    }

    public Single<Wallet> importKeystore(String store, String password, String newPassword) {
        return Single.fromCallable(() -> {
            org.ethereum.geth.Account account = keyStore
                    .importKey(store.getBytes(Charset.forName("UTF-8")), password, newPassword);
            return new Wallet(account.getAddress().getHex().toLowerCase());
        })
                .subscribeOn(Schedulers.io());
    }
}
