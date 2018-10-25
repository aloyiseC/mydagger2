package laiwei.mydagger2.repository;

import io.reactivex.Single;
import laiwei.mydagger2.bean.User;
import laiwei.mydagger2.service.UserService;

/**
 * Created by laiwei on 2018/3/23 0023.
 */
public  class UserRepository {

    private UserService service;

    public Single<User> fetchUser(){
       return service.fetchUsers();
    }

    public UserRepository(UserService service) {
        this.service = service;
    }
}
