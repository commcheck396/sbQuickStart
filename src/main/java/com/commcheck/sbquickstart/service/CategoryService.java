package com.commcheck.sbquickstart.service;

import com.commcheck.sbquickstart.pojo.Category;

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
}
