package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.mapper.UserCategoryMapper;
import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.CategoryService;
import com.commcheck.sbquickstart.utils.PermissionCheckingUtil;
import com.commcheck.sbquickstart.utils.SplitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PermissionCheckingUtil permissionCheckingUtil;

    //    TODO: add illigal cat id check
    @PostMapping()
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            return Result.fail("Category Name cannot be empty");
        }
        if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
            return Result.fail("Category Name already exists");
        }
        categoryService.addCategory(category);
        return Result.success();
    }

    @GetMapping()
    public Result<List<Category>> list() {
        List<Category> categoryList = categoryService.listCategory();
        return Result.success(categoryList);
    }

    @GetMapping("/detail")
    public Result<Category> detail(@RequestParam("id") Integer id) {
        Category category = categoryService.findById(id);
        if (category == null) {
            return Result.fail("Category does not exist");
        }
        return Result.success(category);
    }

    @PutMapping()
    public Result update(@RequestBody @Validated Category category) {
//        TODO: can be edited by the admin/owner/group member
        Integer categoryId = category.getId();
        Category currentCategory = categoryService.findById(categoryId);
        if (currentCategory == null) {
            return Result.fail("Category does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForCategory(currentCategory)) {
            return Result.fail("You do not have permission to edit this category");
        }
        if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
            return Result.fail("Category Name already exists");
        }
        categoryService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping()
    public Result delete(@RequestParam("id") Integer id) {
//        TODO: can be edited by the admin/owner/group member
        Category currentCategory = categoryService.findById(id);
        if (currentCategory == null) {
            return Result.fail("Category does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForCategory(currentCategory)) {
            return Result.fail("You do not have permission to edit this category");
        }
        categoryService.deleteCategory(id);
        return Result.success();
//        TODO: also delete group info in the user_category table
    }

    @PostMapping("/approveJoinGroup")
    public Result approveJoinGroup(@RequestParam String groupList, @RequestParam Integer userId) {
        if (!permissionCheckingUtil.isUserExist(userId)) {
            return Result.fail("User does not exist");
        }
        boolean flag = false;
        List<Integer> list = SplitUtil.splitBySemicolonInt(groupList);
        for (Integer groupId : list) {
            if (!permissionCheckingUtil.checkEditPermissionForCategory(groupId)) {
                System.out.println("You do not have permission to edit " + groupId + " group, skip...");
                continue;
            }
            if (permissionCheckingUtil.isInGroup(groupId, userId)) {
                System.out.println("User " + userId + " is already in " + groupId + " group, skip...");
                continue;
            }
            categoryService.addUserToGroup(groupId, userId);
            flag = true;
        }
        if (flag) {
            return Result.success();
        } else {
            return Result.fail("user is already in all the groups or you do not have permission to edit any of the groups");
        }
    }

    @PostMapping("/approveUpgradeToGroupAdmin")
    public Result approveUpgradeToGroupAdmin(@RequestParam String groupList, @RequestParam Integer userId) {
        if (!permissionCheckingUtil.isUserExist(userId)) {
            return Result.fail("User does not exist");
        }
        boolean flag = false;
        List<Integer> list = SplitUtil.splitBySemicolonInt(groupList);
        for (Integer groupId : list) {
            if (!permissionCheckingUtil.checkEditPermissionForCategory(groupId)) {
//                TODO: skip this group and continue with the next one
                System.out.println("You do not have permission to edit " + groupId + " group, skip...");
                continue;
            }
            if (permissionCheckingUtil.isGroupAdmin(groupId, userId)) {
                System.out.println("User " + userId + " is already a group admin in " + groupId + " group, skip...");
                continue;
            }
            categoryService.upgradeUserToGroupAdmin(groupId, userId);
            flag = true;
        }
        if (flag) {
            return Result.success();
        } else {
            return Result.fail("user is already a group admin in all the groups or you do not have permission to edit any of the groups");
        }
    }

    @PostMapping("/exitGroup")
    public Result exitGroup(@RequestParam String groupList) {
        boolean flag = false;
        List<Integer> list = SplitUtil.splitBySemicolonInt(groupList);
        for (Integer groupId : list) {
            if (!permissionCheckingUtil.isInGroup(groupId)) {
                System.out.println("You are not in " + groupId + " group, skip...");
                continue;
            } else if (permissionCheckingUtil.isGroupOwner(groupId)) {
                System.out.println("You are the owner of " + groupId + " group, skip...");
                continue;
            }
            categoryService.exitGroup(groupId);
            flag = true;
        }
        if (flag) {
            return Result.success();
        } else {
            return Result.fail("Can't exit any of the groups, check if you are in the group or not, or if you are the owner of the group or not");
        }
    }

    @PostMapping("/transferGroupOwnership")
    public Result transferGroupOwnership(@RequestParam Integer groupId, @RequestParam Integer newOwnerId) {
        if (!permissionCheckingUtil.isUserExist(newOwnerId)) {
            return Result.fail("User does not exist");
        }
        if (!(permissionCheckingUtil.isGroupOwner(groupId) || permissionCheckingUtil.isRootAdmin())) {
            return Result.fail("You do not have permission to edit this group");
        }
        categoryService.transferGroupOwnership(groupId, newOwnerId);
        return Result.success();
    }

    @PostMapping("/removeUserFromGroup")
    public Result removeUserFromGroup(@RequestParam Integer groupId, @RequestParam String userIds) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to edit this group");
        }
        List<Integer> userIdList = SplitUtil.splitBySemicolonInt(userIds);
        for (Integer userId : userIdList) {
            if (!permissionCheckingUtil.isUserExist(userId)) {
                System.out.println("User " + userId + " does not exist, skip...");
                continue;
            } else if (permissionCheckingUtil.isGroupOwner(userId)) {
                System.out.println("User " + userId + " is the owner of the group, skip...");
                continue;
            } else if (permissionCheckingUtil.isGroupAdmin(userId) && !permissionCheckingUtil.isRootAdmin()) {
                System.out.println("You don't have authority to remove group admin, skip...");
                continue;
            }
            categoryService.removeUserFromGroup(groupId, userId);
        }
        return Result.success();
    }

    @PostMapping("/removeGroupAdmin")
    public Result removeGroupAdmin(@RequestParam Integer groupId, @RequestParam Integer userId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.isRootAdmin() && !permissionCheckingUtil.isGroupOwner(groupId)) {
            return Result.fail("You do not have permission to remove group admin from this group");
        }
        if (!permissionCheckingUtil.isUserExist(userId)) {
            return Result.fail("User does not exist");
        }
        if (!permissionCheckingUtil.isGroupAdmin(userId)) {
            return Result.fail("User is not a group admin");
        }
        categoryService.removeGroupAdmin(groupId, userId);
        return Result.success();
    }

    @GetMapping("/listUsersId")
    public Result listUsersId(@RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to read this group");
        }
        List<Integer> userIdList = categoryService.listUsersId(groupId);
        return Result.success(userIdList);
    }

    @GetMapping("/listUsers")
    public Result listUsers(@RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to read this group");
        }
        List<User> userList = categoryService.listUsers(groupId);
        return Result.success(userList);
    }

    @GetMapping("/listGroupAdminsId")
    public Result listGroupAdminsId(@RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to read this group");
        }
        List<Integer> userIdList = categoryService.listGroupAdminsId(groupId);
        return Result.success(userIdList);
    }

    @GetMapping("/listGroupAdmins")
    public Result listGroupAdmins(@RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to read this group");
        }
        List<User> userList = categoryService.listGroupAdmins(groupId);
        return Result.success(userList);
    }

    @GetMapping("/listGroupOwners")
    public Result listGroupOwners(@RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to read this group");
        }
        List<User> userList = categoryService.listGroupOwners(groupId);
        return Result.success(userList);
    }

    @GetMapping("groupOwner")
    public Result groupOwner(@RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isCategoryExist(groupId)) {
            return Result.fail("Group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.fail("You do not have permission to read this group");
        }
        User user = categoryService.getGroupOwner(groupId);
        return Result.success(user);
    }



}
