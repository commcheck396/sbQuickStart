package com.commcheck.sbquickstart.service;

import com.commcheck.sbquickstart.pojo.*;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findByUsername(String username);
    
    Result addUser(String username, String password, String email);

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

    void resetPassword(String encryptedPassword, Integer id);

    void updateUserEmail(Integer currentUserId, String email);

    void requestGroupAdmin(Integer groupId, Integer currentUserId);

    void requestJoinGroup(Integer groupId, Integer currentUserId);

    void remindTicketAssignee(Integer ownerId, Integer assingeeId, String title, Integer currentUserId, Integer ticketId);

    List<Message> getAllRequestForCurrentUser(Integer currentUserId);

    void approveRequest(Integer messageId, Integer currentUserId);

    void rejectRequest(Integer messageId, Integer currentUserId, String msg);

    List<Message> getAllRequestByCurrentUser(Integer currentUserId);

    void updateRequestLastEdit(Integer messageId, Integer currentUserId);

    void approveTicketRequest(Integer ticketId, Integer currentUserId);

    void rejectTicketRequest(Integer ticketId, Integer currentUserId, String msg);

    String generateUserCloneCode(Integer currentUserId);

    User getUserByCloneCode(String cloneCode);

    void cloneUser(Integer currentUserId, Integer id);


    void closeRequest(Integer ticketId, Integer currentUserId);
}
