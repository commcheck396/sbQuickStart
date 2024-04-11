package com.commcheck.sbquickstart.service;

import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.User;

import java.util.List;

public interface CategoryService {
    Category findByCategoryName(String categoryName);

    void addCategory(Category category);

    List<Category> listCategory();

    Category findById(Integer id);

    void updateCategory(Category category);

    void deleteCategory(Integer id);

    void addUserToGroup(Integer groupId, Integer userId);

    void upgradeUserToGroupAdmin(Integer groupId, Integer userId);

    void exitGroup(Integer groupId);

    void transferGroupOwnership(Integer groupId, Integer newOwnerId);

    void removeUserFromGroup(Integer groupId, Integer userId);

    void removeGroupAdmin(Integer groupId, Integer userId);

    boolean isUserInGroup(Integer groupId, Integer userId);

    void updateEditRecord (Integer categoryId);

    List<Integer> listUsersId(Integer groupId);

    List<User> listUsers(Integer groupId);

    List<Integer> listGroupAdminsId(Integer groupId);

    List<User> listGroupAdmins(Integer groupId);

    List<User> listGroupOwners(Integer groupId);

    User getGroupOwner(Integer groupId);

    List<Category> listAllCategory();
}
