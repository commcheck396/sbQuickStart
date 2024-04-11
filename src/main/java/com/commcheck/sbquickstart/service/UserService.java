package com.commcheck.sbquickstart.service;

import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findByUsername(String username);
    
    Result addUser(String username, String password);

    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePassword(String encryptedNewPassword);

    User findById(Integer currentUserId);

    void addUserToGroup(List<Integer> list, Integer userId);

    void upgradeToRootAdmin(Integer userId);

    List<Category> groupsIJioned(Integer currentUserId);

    List<Category> groupsIAdmin(Integer currentUserId);

    List<Ticket> ticketsICreated(Integer currentUserId);

    Map<Integer, String> allUserNames();

    List<User> allUsers();

    List<Integer> groupsIin(Integer currentUserId);

    User findByEmail(String email);

    List<Category> groupsInfoIin(List<Integer> ids);
}
