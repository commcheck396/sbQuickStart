package com.commcheck.sbquickstart.service.impl;

import com.commcheck.sbquickstart.mapper.CategoryMapper;
import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.CategoryService;
import com.commcheck.sbquickstart.utils.PermissionCheckingUtil;
import com.commcheck.sbquickstart.utils.SplitUtil;
import com.commcheck.sbquickstart.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public Category findByCategoryName(String categoryName) {
        return categoryMapper.findByCategoryName(categoryName);
    }

    @Override
    public void addCategory(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setOwnerId(currentUserId);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> listCategory() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        return categoryMapper.list(currentUserId);
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    public void updateCategory(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setLastEditedBy(currentUserId);
        categoryMapper.update(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.deleteCategory(id);
    }

    @Override
    public void addUserToGroup(Integer groupId, Integer userId) {
        Category category = categoryMapper.findById(groupId);
        String memberIds = category.getMember();
        if (memberIds == null || memberIds.equals("")) {
            memberIds = userId.toString();
        } else {
            if (memberIds.contains(userId.toString())) {
                System.out.println("User already in the group.");
                return;
            }
            memberIds = memberIds + ";" + userId;
        }
        category.setMember(memberIds);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setLastEditedBy(currentUserId);
        categoryMapper.addUserToGroup(category);
    }

    @Override
    public void upgradeUserToGroupAdmin(Integer groupId, Integer userId) {
        Category category = categoryMapper.findById(groupId);
        String adminIds = category.getGroupAdmin();
        if (adminIds == null || adminIds.equals("")) {
            adminIds = userId.toString();
        } else {
            if (adminIds.contains(userId.toString())) {
                System.out.println("User already as group admin.");
                return;
            }
            adminIds = adminIds + ";" + userId;
        }
        category.setGroupAdmin(adminIds);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setLastEditedBy(currentUserId);
        categoryMapper.upgradeUserToGroupAdmin(category);
    }

    @Override
    public void exitGroup(Integer groupId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        Category category = categoryMapper.findById(groupId);
        List<String> memberIds = new ArrayList<>(SplitUtil.splitBySemicolon(category.getMember()));
        List<String> groupAdminIds = new ArrayList<>(SplitUtil.splitBySemicolon(category.getGroupAdmin()));
        boolean flag = false;
        if (memberIds.contains(Integer.toString(currentUserId))) {
            memberIds.remove(Integer.toString(currentUserId));
            String newMemberIds = String.join(";", memberIds);
            category.setMember(newMemberIds);
            flag = true;
        }
        else {
            System.out.println("User not in the group.");
        }
        if (groupAdminIds.contains(Integer.toString(currentUserId))) {
            groupAdminIds.remove(Integer.toString(currentUserId));
            String newGroupAdminIds = String.join(";", groupAdminIds);
            category.setGroupAdmin(newGroupAdminIds);
            flag = true;
        }
        else{
            System.out.println("User not as group admin.");
        }
        if (flag){
            category.setLastEditedBy(currentUserId);
            categoryMapper.exitGroup(category);
        }
        else{
            System.out.println("User not in the group or as group admin.");
        }
    }

    @Override
    public void transferGroupOwnership(Integer groupId, Integer newOwnerId) {
        Category category = categoryMapper.findById(groupId);
        Integer oldOwnerId = category.getOwnerId();
        category.setOwnerId(newOwnerId);
        List<String> groupAdminIds = new ArrayList<>(SplitUtil.splitBySemicolon(category.getGroupAdmin()));
        groupAdminIds.remove(Integer.toString(oldOwnerId));
        groupAdminIds.add(Integer.toString(newOwnerId));
        String newGroupAdminIds = String.join(";", groupAdminIds);
        category.setGroupAdmin(newGroupAdminIds);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setLastEditedBy(currentUserId);
        categoryMapper.transferGroupOwnership(category);
    }

    @Override
    public void removeUserFromGroup(Integer groupId, Integer currentUserId) {
        Category category = categoryMapper.findById(groupId);
        List<String> memberIds = new ArrayList<>(SplitUtil.splitBySemicolon(category.getMember()));
        List<String> groupAdminIds = new ArrayList<>(SplitUtil.splitBySemicolon(category.getGroupAdmin()));
        boolean flag = false;
        if (memberIds.contains(Integer.toString(currentUserId))) {
            memberIds.remove(Integer.toString(currentUserId));
            String newMemberIds = String.join(";", memberIds);
            category.setMember(newMemberIds);
            flag = true;
        }
        else {
            System.out.println("User not in the group.");
        }
        if (groupAdminIds.contains(Integer.toString(currentUserId))) {
            groupAdminIds.remove(Integer.toString(currentUserId));
            String newGroupAdminIds = String.join(";", groupAdminIds);
            category.setGroupAdmin(newGroupAdminIds);
            flag = true;
        }
        else{
            System.out.println("User not as group admin.");
        }
        if (flag){
            category.setLastEditedBy(currentUserId);
            categoryMapper.exitGroup(category);
        }
        else{
            System.out.println("User not in the group or as group admin.");
        }
    }

    @Override
    public void removeGroupAdmin(Integer groupId, Integer userId) {
        Category category = categoryMapper.findById(groupId);
        List<String> groupAdminIds = new ArrayList<>(SplitUtil.splitBySemicolon(category.getGroupAdmin()));
        if (groupAdminIds.contains(Integer.toString(userId))) {
            groupAdminIds.remove(Integer.toString(userId));
            String newGroupAdminIds = String.join(";", groupAdminIds);
            category.setGroupAdmin(newGroupAdminIds);
        }
        else{
            System.out.println("User not as group admin.");
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setLastEditedBy(currentUserId);
        categoryMapper.exitGroup(category);
    }


}
