package laiwei.mydagger2.service;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import laiwei.mydagger2.bean.Person;
import laiwei.mydagger2.bean.User;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
public class UserService {
    public Single<User> fetchUsers(){
         return Single.fromCallable(() -> new User(new Person("iniesta",34))).subscribeOn(Schedulers.io());
    }
}
