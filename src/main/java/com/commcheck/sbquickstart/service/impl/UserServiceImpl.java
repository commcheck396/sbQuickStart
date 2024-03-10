package com.commcheck.sbquickstart.service.impl;

import com.commcheck.sbquickstart.mapper.UserMapper;
import com.commcheck.sbquickstart.pojo.Encrypter;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
//    @Select("select * from users where username = #{username}")
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public Result addUser(String username, String password) {
        //use MD5 to encrypt password
        String encryptedPassword = Encrypter.encrypt(password, "MD5");
        try {
            userMapper.addUser(username, encryptedPassword);
        } catch (Exception e) {
            System.out.println("add user to Mysql failed");
            return Result.fail("add user to Mysql failed");
        }
        System.out.println("add user success");
        return Result.success("add user to Mysql success");
    }
}
