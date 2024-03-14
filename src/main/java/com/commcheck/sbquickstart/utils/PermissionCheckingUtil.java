package com.commcheck.sbquickstart.utils;

import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.CategoryService;
import com.commcheck.sbquickstart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PermissionCheckingUtil {
    @Autowired
    private UserService userService;

    @Autowired
    public CategoryService categoryService;


    public boolean isRootAdmin(){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        User user = userService.findById(currentUserId);
        return user.getStatus() <= 1;
    }

    public boolean isGroupAdmin(Integer groupId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        String groupAdmins = categoryService.findById(groupId).getGroupAdmin();
        return groupAdmins.contains(currentUserId.toString());
    }

    public boolean isInGroup(Integer groupId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        String groupMembers = categoryService.findById(groupId).getMember();
        return groupMembers.contains(currentUserId.toString());
    }

    public boolean isInGroup(Integer groupId, Integer currentUserId){
        String groupMembers = categoryService.findById(groupId).getMember();
        return groupMembers.contains(currentUserId.toString());
    }

    public boolean isGroupOwner(Integer groupId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        Integer groupOwnerId = categoryService.findById(groupId).getOwnerId();
        return groupOwnerId.equals(currentUserId);
    }

    public boolean isGroupOwner(Integer groupId, Integer currentUserId){
        Integer groupOwnerId = categoryService.findById(groupId).getOwnerId();
        return groupOwnerId.equals(currentUserId);
    }

    public boolean isUserExist(Integer userId){
        return userService.findById(userId) != null;
    }

    public boolean isCategoryExist(Integer categoryId){
        return categoryService.findById(categoryId) != null;
    }

    public boolean checkEditPermissionForCategory(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        User currentUser = userService.findById(currentUserId);
        return category.getOwnerId() == currentUserId ||
                currentUser.getStatus() <= 1 ||
                isGroupAdmin(category.getId());
    }

    public boolean checkEditPermissionForCategory(Integer categoryId) {
        Category category = categoryService.findById(categoryId);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        User currentUser = userService.findById(currentUserId);
        return category.getOwnerId() == currentUserId ||
                currentUser.getStatus() <= 1 ||
                isGroupAdmin(category.getId());
    }


}
