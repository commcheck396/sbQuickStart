package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

//import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper {
    @Select("select * from users where username = #{username}")
    User findByUsername(String username);
    @Insert("insert into users (username, password, created_time, updated_time)" +
            "values (#{username}, #{password}, now(), now())")
    void addUser(String username, String password);
}
