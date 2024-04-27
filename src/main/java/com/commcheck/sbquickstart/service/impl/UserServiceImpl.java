package com.commcheck.sbquickstart.service.impl;

import com.commcheck.sbquickstart.mapper.*;
import com.commcheck.sbquickstart.pojo.*;
import com.commcheck.sbquickstart.utils.Encrypter;
import com.commcheck.sbquickstart.service.UserService;
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
    @Autowired
    private ApplicationMapper applicationMapper;
//    @Autowired
//    private PermissionCheckingUtil permissionCheckingUtil;
//    @Select("select * from users where username = #{username}")
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public Result addUser(String username, String password, String email) {
        //use MD5 to encrypt password
        String encryptedPassword = Encrypter.encrypt(password, "MD5");
        try {
            userMapper.addUser(username, encryptedPassword, email);
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
        Integer currentUserId = (Integer)map.get("id");
        userMapper.updatePassword(encryptedNewPassword, currentUserId);
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

    @Override
    public void resetPassword(String encryptedPassword, Integer id) {
        userMapper.updatePassword(encryptedPassword, id);
    }

    @Override
    public void updateUserEmail(Integer currentUserId, String email) {
        userMapper.updateUserEmail(currentUserId, email);
    }

    @Override
    public void requestGroupAdmin(Integer groupId, Integer currentUserId) {
        String userName = userMapper.findById(currentUserId).getUsername();
        String groupName = categoryMapper.findById(groupId).getCategoryName();
        if(applicationMapper.findApplicationBySenderAndReceiverAndType(currentUserId, groupId, 2) != null){
            return;
        }
        applicationMapper.addApplication(currentUserId, groupId, 2, 0, "用户" + userName + "申请加入组" + groupName);
    }

    @Override
    public void requestJoinGroup(Integer groupId, Integer currentUserId) {
        String userName = userMapper.findById(currentUserId).getUsername();
        String groupName = categoryMapper.findById(groupId).getCategoryName();
        if(applicationMapper.findApplicationBySenderAndReceiverAndType(currentUserId, groupId, 1) != null){
            return;
        }
        applicationMapper.addApplication(currentUserId, groupId, 1, 0, "用户" + userName + "申请加入组" + groupName);
    }

    @Override
    public void remindTicketAssignee(Integer ownerId, Integer assingeeId, String title, Integer currentUserId, Integer ticketId) {
        String ownerName = userMapper.findById(ownerId).getUsername();
        String assigneeName = userMapper.findById(assingeeId).getUsername();
        if (applicationMapper.findTicketReminderByTicketIdAndReceiver(ticketId, assingeeId) != null){
            return;
        }
        applicationMapper.addTicketReminder(currentUserId, assingeeId, ticketId, 3, 0, "用户" + ownerName + "提醒您处理" + title + "的工单，工单已经分配给" + assigneeName);
    }

    @Override
    public List<Message> getAllRequestForCurrentUser(Integer currentUserId) {
        List<Integer> inGroupIds = userCategoryMapper.groupsIJoined(currentUserId);
        List<Message> messages = new ArrayList<>();
//        List<Message> joinGroupRequests = applicationMapper.getJoinGroupRequests(inGroupIds);
//        List<Message> adminGroupRequests = applicationMapper.getAdminGroupRequests(inGroupIds);
        List<Message> joinGroupRequests = applicationMapper.getJoinGroupRequests(currentUserId);
        List<Message> adminGroupRequests = applicationMapper.getAdminGroupRequests(currentUserId);
        List<Message> ticketReminders = applicationMapper.getTicketReminders(currentUserId);
        messages.addAll(joinGroupRequests);
        messages.addAll(adminGroupRequests);
        messages.addAll(ticketReminders);
        return messages;
    }

    @Override
    public void approveRequest(Integer messageId, Integer currentUserId) {
        Message message = applicationMapper.getApplicationById(messageId);
        Integer type = message.getType();
        Integer sender = message.getSender();
        Integer receiver = message.getReceiver();
        String userName = userMapper.findById(currentUserId).getUsername();
        applicationMapper.updateApplicationStatus(messageId, 1);
        if (type == 1) {
//            userCategoryMapper.addUserToGroup(receiver, sender);
            applicationMapper.updateApplicationMessage(messageId, "管理員" + userName + "同意了您加入组的请求");
            userCategoryMapper.addRelation(sender, receiver);
        } else if (type == 2) {
//            adminCategoryMapper.addAdminToGroup(receiver, sender);
            applicationMapper.updateApplicationMessage(messageId, "管理員" + userName + "同意了您成为组管理员的请求");
            adminCategoryMapper.addAdminRelation(sender, receiver);
        } else if (type == 3) {
            applicationMapper.updateApplicationMessage(messageId, "用戶" + userName + "处理了您的工单提醒。");
        }
        updateRequestLastEdit(messageId, currentUserId);
    }

    @Override
    public List<Message> getAllRequestByCurrentUser(Integer currentUserId) {
        List<Message> messages = applicationMapper.getAllRequestByCurrentUser(currentUserId);
        return messages;
    }

    @Override
    public void updateRequestLastEdit(Integer messageId, Integer currentUserId) {
        applicationMapper.updateLastEdit(messageId, currentUserId);
    }

    @Override
    public void approveTicketRequest(Integer ticketId, Integer currentUserId) {
        Message message = applicationMapper.getTicketReminderByTicketIdAndReceiver(ticketId, currentUserId);
        Integer messageId = message.getId();
        approveRequest(messageId, currentUserId);
    }


    @Override
    public void rejectTicketRequest(Integer ticketId, Integer currentUserId, String msg) {
        Message message = applicationMapper.getTicketReminderByTicketIdAndReceiver(ticketId, currentUserId);
        Integer messageId = message.getId();
        rejectRequest(messageId, currentUserId, msg);
    }

    @Override
    public void rejectRequest(Integer messageId, Integer currentUserId, String Msg) {
        Message message = applicationMapper.getApplicationById(messageId);
        Integer type = message.getType();
        Integer sender = message.getSender();
        Integer receiver = message.getReceiver();
        String senderName = userMapper.findById(sender).getUsername();
        String receiverName = userMapper.findById(receiver).getUsername();
        String userName = userMapper.findById(currentUserId).getUsername();
        applicationMapper.updateApplicationStatus(messageId, 2);
        if (Msg == null || Msg.equals("")) {
            if (type == 1) {
                applicationMapper.updateApplicationMessage(messageId, "管理員" + userName + "拒绝了您加入组的请求");
            } else if (type == 2) {
                applicationMapper.updateApplicationMessage(messageId, "管理員" + userName + "拒绝了您成为组管理员的请求。");
            } else if (type == 3) {
                applicationMapper.updateApplicationMessage(messageId, "用戶" + userName + "拒绝了您的工单审批。");
            }
        }
        else{
            if (type == 1) {
                applicationMapper.updateApplicationMessage(messageId, "管理員" + userName + "拒绝了您加入组的请求。" + "原因：" + Msg);
            } else if (type == 2) {
                applicationMapper.updateApplicationMessage(messageId, "管理員" + userName + "拒绝了您成为组管理员的请求" + "原因：" + Msg);
            } else if (type == 3) {
                applicationMapper.updateApplicationMessage(messageId, "用戶" + userName + "拒绝了您的工单提醒" + "原因：" + Msg);
            }
        }
        updateRequestLastEdit(messageId, currentUserId);
    }



}
