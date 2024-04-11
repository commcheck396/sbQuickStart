package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

//import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper {
    @Update("update users set username = #{username}, nickname = #{nickname}, email = #{email}, updated_time = now() where id = #{id}")
    void update(User user);

    @Select("select * from users where username = #{username}")
    User findByUsername(String username);
    @Insert("insert into users (username, password, created_time, updated_time)" +
            "values (#{username}, #{password}, now(), now())")
    void addUser(String username, String password);

    @Update("update users set avatar = #{avatarUrl}, updated_time = now() where id = #{id}")
    void updateAvatar(String avatarUrl, Integer id);

    @Update("update users set password = #{encryptedNewPassword}, updated_time = now() where id = #{id}")
    void updatePassword(String encryptedNewPassword, Integer id);

    @Select("select * from users where id = #{currentUserId}")
    User findById(Integer currentUserId);

    @Update("update users set belongsTo = #{userGroup} where id = #{userId}")
    void addUserToGroup(String userGroup, Integer userId);

    @Update("update users set status = 0 where id = #{userId}")
    void upgradeToRootAdmin(Integer userId);
    @Select("SELECT id, username FROM users")
    List<User> allUsers();

    @Select("select * from users where email = #{email}")
    User findByEmail(String email);
}
