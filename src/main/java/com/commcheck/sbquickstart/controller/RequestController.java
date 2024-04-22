package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Message;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.service.TicketService;
import com.commcheck.sbquickstart.service.UserService;
import com.commcheck.sbquickstart.utils.PermissionCheckingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionCheckingUtil permissionCheckingUtil;

    @Autowired
    private TicketService ticketService;
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

    @PostMapping("/remindTicketAssignee")
    public Result remindTicketAssignee(@RequestParam Integer ticketId){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
//        String owner = userService.findById(ticketService.getTicketById(ticketId).getOwnerId()).getUsername();
        Integer ownerId = ticketService.getTicketById(ticketId).getOwnerId();
        Integer assingeeId = ticketService.getTicketById(ticketId).getAssigneeId();
        String title = ticketService.getTicketById(ticketId).getTitle();
        userService.remindTicketAssignee(ownerId, assingeeId, title, currentUserId, ticketId);
        return Result.success();
    }

    @PostMapping("/approveRequest")
    public Result approveRequest(@RequestParam Integer messageId){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        userService.approveRequest(messageId, currentUserId);
        return Result.success();
    }

    @PostMapping("/rejectRequest")
    public Result rejectRequest(@RequestParam Integer messageId, @RequestParam String reason){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        userService.rejectRequest(messageId, currentUserId, reason);
        return Result.success();
    }

    @GetMapping("/getAllRequestForCurrentUser")
    public Result getAllRequestForCurrentUser(){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Message> messages = userService.getAllRequestForCurrentUser(currentUserId);
        return Result.success(messages);
    }

    @GetMapping("/getAllRequestByCurrentUser")
    public Result getAllRequestByCurrentUser(){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Message> messages = userService.getAllRequestByCurrentUser(currentUserId);
        return Result.success(messages);
    }

    @PostMapping("/approveTicketRequest")
    public Result approveTicketRequest(@RequestParam Integer ticketId){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        userService.approveTicketRequest(ticketId, currentUserId);
        return Result.success();
    }

    @PostMapping("/rejectTicketRequest")
    public Result rejectTicketRequest(@RequestParam Integer ticketId, @RequestParam String msg){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        userService.rejectTicketRequest(ticketId, currentUserId, msg);
        return Result.success();
    }

    @GetMapping("/getAllRequestOfCurrentUser")
    public Result getAllRequestOfCurrentUser(){
        Integer currentUserId = permissionCheckingUtil.getCurrentUserId();
        List<Message> messages = userService.getAllRequestForCurrentUser(currentUserId);
        messages.addAll(userService.getAllRequestByCurrentUser(currentUserId));
        return Result.success(messages);
    }

}
