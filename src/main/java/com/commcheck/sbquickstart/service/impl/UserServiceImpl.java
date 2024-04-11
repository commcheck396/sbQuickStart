package com.commcheck.sbquickstart.service.impl;

import com.commcheck.sbquickstart.mapper.*;
import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.utils.Encrypter;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.UserService;
import com.commcheck.sbquickstart.utils.PermissionCheckingUtil;
import com.commcheck.sbquickstart.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private UserCategoryMapper userCategoryMapper;
    @Autowired
    private AdminCategoryMapper adminCategoryMapper;
//    @Autowired
//    private PermissionCheckingUtil permissionCheckingUtil;
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

    @Override
    public void update(User user) {
//        user.setId(permissionCheckingUtil.getCurrentUserId());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        userMapper.updateAvatar(avatarUrl, (Integer)map.get("id"));
    }

    @Override
    public void updatePassword(String encryptedNewPassword) {
        Map<String, Object> map = ThreadLocalUtil.get();
        userMapper.updatePassword(encryptedNewPassword, (Integer)map.get("id"));
    }

    @Override
    public User findById(Integer currentUserId) {
        return userMapper.findById(currentUserId);
    }

    @Override
    public void addUserToGroup(List<Integer> list, Integer userId) {
        User user = this.findById(userId);
        String userGroup = user.getBelongsTo();
        for (Integer groupId : list) {
            if (userGroup == null){
                userGroup = Integer.toString(groupId);
                continue;
            }
            userGroup = userGroup + ";" + groupId;
        }
        userMapper.addUserToGroup(userGroup, userId);
    }

    @Override
    public void upgradeToRootAdmin(Integer userId) {
        userMapper.upgradeToRootAdmin(userId);
    }

    @Override
    public List<Category> groupsIJioned(Integer currentUserId) {
        List<Integer> groupIds = userCategoryMapper.groupsIJioned(currentUserId);
        List<Category> categories = new ArrayList<>();
        for (Integer groupId : groupIds) {
            categories.add(categoryMapper.findById(groupId));
        }
        return categories;
    }

    @Override
    public List<Category> groupsIAdmin(Integer currentUserId) {
        List<Integer> groupIds = adminCategoryMapper.groupsIAdmin(currentUserId);
        List<Category> categories = new ArrayList<>();
        for (Integer groupId : groupIds) {
            categories.add(categoryMapper.findById(groupId));
        }
        return categories;
    }

    @Override
    public List<Ticket> ticketsICreated(Integer currentUserId) {
        List<Ticket> tickets = ticketMapper.findByOwnerId(currentUserId);
        return tickets;
    }


    @Override
    public Map<Integer, String> allUserNames() {
        List<User> users = this.allUsers();
        Map<Integer, String> map = new HashMap<>();
        for (User user : users) {
            map.put(user.getId(), user.getUsername());
        }
        return map;
    }

    @Override
    public List<User> allUsers() {
        return userMapper.allUsers();
    }

    @Override
    public List<Integer> groupsIin(Integer currentUserId) {
        return userCategoryMapper.groupsIJoined(currentUserId);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public List<Category> groupsInfoIin(List<Integer> ids) {
        List<Category> categories = new ArrayList<>();
        for (Integer id : ids) {
            categories.add(categoryMapper.findById(id));
        }
        return categories;
    }
}
