package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;

import javax.annotation.Resource;

@Resource
public class UserServiceImpl implements UserService {
    @Override
    public User findById(int id) {
        return null;
    }
}
