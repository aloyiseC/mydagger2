package laiwei.mydagger2.interact;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import laiwei.mydagger2.bean.User;
import laiwei.mydagger2.bean.Wallet;
import laiwei.mydagger2.repository.AccountRespository;
import laiwei.mydagger2.repository.UserRepository;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
public class FetchWalletInteract {
    private final UserRepository userRepository;
    private final AccountRespository accountRespository;

    public FetchWalletInteract(UserRepository userRepository, AccountRespository accountRespository) {
        this.userRepository = userRepository;
        this.accountRespository = accountRespository;
    }

    public Single<User> fetch(){
        return userRepository.fetchUser().subscribeOn(AndroidSchedulers.mainThread());
    }

    public Single<Wallet[]> fetchAccounts(){
        return accountRespository.fetchWallets();
    }
}
