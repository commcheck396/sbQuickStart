package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.utils.*;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.UserService;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionCheckingUtil permissionCheckingUtil;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "username format error...") String username,
                           @Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "password format error...") String password,
                           String email) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.fail("user already exists...");
        }
        else{
            Result result = userService.addUser(username, password, email);
            if(result.getCode() == 0){
//                System.out.println(result.getCode());
                return Result.success("register success...");
            }
            else{
                return Result.fail(result.getMessage());
            }
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "username format error...") String username,
                        @Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "password format error...") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.fail("user not exists...");
        }
        else{
            String encryptedPassword = Encrypter.encrypt(password, "MD5");
            if (user.getPassword().equals(encryptedPassword)) {
                Map<String, Object> claims= new HashMap<>();
                claims.put("id", user.getId());
                claims.put("username", user.getUsername());
                String token = JWTUtil.JWTGeneration(claims);
                return Result.success(token);
            }
            else{
                return Result.fail("password error...");
            }
        }
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(@RequestHeader(name = "Authorization") String token){
        Map<String, Object> map = JWTUtil.JWTVerification(token);
        String username = (String) map.get("username");
//        System.out.println(username);
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        user.setUpdated_time(LocalDateTime.now());
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        System.out.println("updating user avatar...");
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> body){
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        String reTypePassword = body.get("reTypePassword");
        if (oldPassword.isEmpty() || newPassword.isEmpty() || reTypePassword.isEmpty()){
            return Result.fail("passwords cannot be empty...");
        }
        if(!newPassword.equals(reTypePassword)){
            return Result.fail("passwords are not the same...");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUsername(username);
        String encryptedOldPassword = Encrypter.encrypt(oldPassword, "MD5");
        if (!user.getPassword().equals(encryptedOldPassword)){
            return Result.fail("old password error...");
        }
        String encryptedNewPassword = Encrypter.encrypt(newPassword, "MD5");
        userService.updatePassword(encryptedNewPassword);
        return Result.success();
    }



//    @PostMapping("/joinGroupRequest")
//    public Result joinGroupRequest(@RequestParam String groupList){
////        TODO: notify group admin or admin to approve
//        List<Integer> list = SplitUtil.splitBySemicolonInt(groupList);
//        Map<String, Object> map = ThreadLocalUtil.get();
//        Integer currentUserId = (Integer) map.get("id");
//        System.out.println("user "+ currentUserId + "apply to join group" + list);
//        return Result.success();
//    }

    @PostMapping("/rootAdminRequest")
    public Result adminRequest(){
//        TODO: notify root admin to approve
        if (permissionCheckingUtil.isRootAdmin()){
            return Result.fail("you are already an admin...");
        }
        return Result.success();
    }

    @PostMapping("/groupAdminRequest")
    public Result groupAdminRequest(@RequestParam Integer groupId){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        if (permissionCheckingUtil.isGroupAdmin(groupId)){
            return Result.fail("you are already an admin of this group...");
        }
        userService.requestGroupAdmin(groupId, currentUserId);
        return Result.success();
    }

    @PostMapping("/joinGroupRequest")
    public Result joinGroupRequest(@RequestParam Integer groupId){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        if (permissionCheckingUtil.isInGroup(groupId)){
            return Result.fail("you are already in this group...");
        }
        userService.requestJoinGroup(groupId, currentUserId);
        return Result.success();
    }


    @PostMapping("/approveRootAdminRequest")
    public Result approveAdminRequest(@RequestParam Integer userId){
        if(!permissionCheckingUtil.isRootAdmin()){
            return Result.fail("you don't have permission to this operation...");
        }
        userService.upgradeToRootAdmin(userId);
        return Result.success();
    }

//    @PostMapping("/approveJoinGroup")
//    public Result approveJoinGroup(@RequestParam String groupList, @RequestParam Integer userId){
//        Map<String, Object> map = ThreadLocalUtil.get();
//        Integer currentUserId = (Integer) map.get("id");
//        User currentUser = userService.findById(currentUserId);
//        if (currentUser.getStatus() != 0){
//            return Result.fail("you don't have permission to this operation...");
//        }
//        List<Integer> list = SplitUtil.splitBySemicolonInt(groupList);
//        userService.addUserToGroup(list, userId);
//        return Result.success();
//    }



    @PostMapping("/upgradeToAdminDirectly")
    public Result upgradeToAdminDirectly(){
//        TODO: delete this method
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        userService.upgradeToRootAdmin(currentUserId);
        return Result.success();
    }

    @GetMapping("/groupsIJioned")
    public Result<List<Category>> groupsIJioned() {
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Category> list = userService.groupsIJioned(currentUserId);
        return Result.success(list);
    }

    @GetMapping("/groupsIin")
    public Result<List<Integer>> groupsIin() {
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Integer> list = userService.groupsIin(currentUserId);
        return Result.success(list);
    }

    @GetMapping("/groupsInfoIin")
    public Result<List<Category>> groupsInfoIin() {
        List<Integer> Ids = groupsIin().getData();
        List<Category> list = userService.groupsInfoIin(Ids);
        return Result.success(list);
    }

    @GetMapping("/groupsIAdmin")
    public Result<List<Category>> groupsIAdmin() {
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Category> list = userService.groupsIAdmin(currentUserId);
        return Result.success(list);
    }

    @GetMapping("/ticketsICreated")
    public Result ticketsICreated() {
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Ticket> list = userService.ticketsICreated(currentUserId);
        return Result.success(list);
    }

    @GetMapping("/getNameById")
    public Result<String> getNameById(@RequestParam Integer userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return Result.fail("user not exists...");
        }
        String username = user.getUsername();
        return Result.success(username);
    }

    @GetMapping("/isUserInGroup")
    public Result<Boolean> isUserInGroup(@RequestParam Integer groupId) {
        if (permissionCheckingUtil.isInGroup(groupId)) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @GetMapping("/canUserEditGroup")
    public Result<Boolean> canUserEditGroup(@RequestParam Integer groupId) {
        if (permissionCheckingUtil.isGroupAdmin(groupId) || permissionCheckingUtil.isRootAdmin() || permissionCheckingUtil.isGroupOwner(groupId)) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @GetMapping("/canUserDeleteGroup")
    public Result<Boolean> canUserDeleteGroup(@RequestParam Integer groupId) {
        if (permissionCheckingUtil.isRootAdmin() || permissionCheckingUtil.isGroupOwner(groupId)) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @GetMapping("/canUserViewGroup")
    public Result<Boolean> canUserViewGroup(@RequestParam Integer groupId) {
        if (permissionCheckingUtil.checkReadPermissionForCategory(groupId)) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @GetMapping("/allUserNames")
    public Result<Map<Integer, String>> allUserNames() {
        Map<Integer, String> userNames = userService.allUserNames();
        return Result.success(userNames);
    }

    @GetMapping("/getUserByName")
    public Result<User> getUserByName(@RequestParam String username) {
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @GetMapping("/getUserByEmail")
    public Result<User> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return Result.success(user);
    }

    @PostMapping("/updateUserEmail")
    public Result updateUserEmail(@RequestParam String email) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        userService.updateUserEmail(currentUserId, email);
        return Result.success();
    }

    @GetMapping("/canUserEditTicket")
    public Result<Boolean> canUserEditTicket(@RequestParam Integer ticketId) {
        if (permissionCheckingUtil.checkEditPermissionForTicket(ticketId)) {
            return Result.success(true);
        } else {
            return Result.success(false);
        }
    }

    @GetMapping("/generateUserCloneCode")
    public Result generateUserCloneCode() {
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        String cloneCode = userService.generateUserCloneCode(currentUserId);
        return Result.success(cloneCode);
    }

    @GetMapping("/getUserByCloneCode")
    public Result getUserByCloneCode(@RequestParam String cloneCode) {
        User user = userService.getUserByCloneCode(cloneCode);
        return Result.success(user);
    }

    @PostMapping("/cloneUser")
    public Result cloneUser(@RequestParam String cloneCode) {
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        User sourceUser = userService.getUserByCloneCode(cloneCode);
        if (sourceUser == null) {
            return Result.fail("clone code not exists...");
        }
        if (sourceUser.getId().equals(currentUserId)) {
            return Result.fail("cannot clone to yourself...");
        }
        userService.cloneUser(currentUserId, sourceUser.getId());
        return Result.success();
    }


}
