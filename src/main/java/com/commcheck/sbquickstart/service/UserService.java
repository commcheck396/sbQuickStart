package com.commcheck.sbquickstart.service;

import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserService {

    User findByUsername(String username);


    Result addUser(String username, String password);
}
