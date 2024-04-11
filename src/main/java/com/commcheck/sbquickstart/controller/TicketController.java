package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.mapper.TicketTicketMapper;
import com.commcheck.sbquickstart.mapper.TicketWatcherMapper;
import com.commcheck.sbquickstart.pojo.PageBean;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.service.TicketService;
import com.commcheck.sbquickstart.utils.PermissionCheckingUtil;
import com.commcheck.sbquickstart.utils.SplitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private PermissionCheckingUtil permissionCheckingUtil;


    @GetMapping("/list")
    public Result<String> list() {
        return Result.success("Ticket List");
    }

//    @PostMapping("/add")
//    public Result<String> add(@) {
//
//    }

    @GetMapping
    public Result<PageBean<Ticket>> list(Integer pageNum,
                                         Integer pageSize,
                                         @RequestParam(required = false) String belongTo,
                                         @RequestParam(required = false) Integer priority,
                                         @RequestParam(required = false)Integer state,
                                         @RequestParam(required = false)Integer type){
        PageBean<Ticket> page = ticketService.list(pageNum, pageSize, belongTo, priority, state, type);

        return Result.success(page);
    }

    @PostMapping("/addTicket")
    public Result addTicket(@RequestBody Ticket ticket) {
        if (ticket.getTitle() == null || ticket.getTitle().isEmpty()){
            return Result.fail("title can not be empty");
        }
        else if(ticket.getDescription() == null || ticket.getDescription().isEmpty()){
            return Result.fail("description can not be empty");
        }
        else if(ticket.getBelongsTo() == null){
            return Result.fail("belongTo can not be empty");
        }
        else if(ticket.getDueTime() == null){
            return Result.fail("dueTime can not be empty");
        }
        else if(ticket.getAssigneeId() == null){
            return Result.fail("assigneeId can not be empty");
        }
        else if (!permissionCheckingUtil.isUserExist(ticket.getAssigneeId())){
            return Result.fail("assigneeId does not exist");
        }
        else if (!permissionCheckingUtil.isGroupExist(ticket.getBelongsTo())){
            return Result.fail("group does not exist");
        }
//        else if (ticket.getType() == null){
//            return Result.fail("type can not be empty");
//        }
//        else if (ticket.getState() == null){
//            return Result.fail("state can not be empty");
//        }
        else{
            Integer ticketId = ticketService.addTicket(ticket);
            if (ticketId == null){
                return Result.fail("add ticket failed");
            }
            if (ticket.getPriority() != null){
                ticketService.modifyPriority(ticket, ticket.getPriority());
            }
            return Result.success(ticket.getId());
        }
    }

    @PostMapping("/modifyPriority")
    public Result modifyPriority(@RequestParam Integer ticketId, @RequestParam Integer priority) {
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null){
            return Result.fail("ticket does not exist");
        }
        else if (priority < 0 || priority > 5){
            return Result.fail("priority should be between 0 and 5");
        }
        else{
            ticketService.modifyPriority(ticket, priority);
            return Result.success();
        }
    }

    @PostMapping("/addToGroup")
    public Result addToGroup(@RequestParam Integer ticketId, @RequestParam Integer groupId){
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (!permissionCheckingUtil.isInGroup(groupId)) {
            System.out.println("you don't have permission to add ticket to group " + groupId);
        }
        Ticket ticket = ticketService.getTicketById(ticketId);
        ticketService.addTicketToGroup(ticket, groupId);
        return Result.success();
    }

    @PostMapping("/modifyState")
    public Result modifyState(@RequestParam Integer ticketId, @RequestParam Integer state){
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null){
            return Result.fail("ticket does not exist");
        }
        else if (state < 0 || state > 5){
            return Result.fail("state should be between 0 and 5");
        }
        else{
            ticketService.modifyState(ticket, state);
            return Result.success();
        }
    }

    @PostMapping("/modifyAssignee")
    public Result modifyAssignee(@RequestParam Integer ticketId, @RequestParam Integer assigneeId){
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null){
            return Result.fail("ticket does not exist");
        }
        else if (!permissionCheckingUtil.isUserExist(assigneeId)){
            return Result.fail("assigneeId does not exist");
        }
        else{
            ticketService.modifyAssignee(ticket, assigneeId);
            return Result.success();
        }
    }

    @PostMapping("/updateTicket")
    public Result updateTicket(@RequestBody Ticket ticket) {
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticket.getId())) {
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (ticket.getTitle() == null || ticket.getTitle().isEmpty()) {
            return Result.fail("title can not be empty");
        } else if (ticket.getDescription() == null || ticket.getDescription().isEmpty()) {
            return Result.fail("description can not be empty");
        } else if (ticket.getBelongsTo() == null) {
            return Result.fail("belongTo can not be empty");
        } else if (ticket.getDueTime() == null) {
//            TODO: handle date format
            return Result.fail("dueTime can not be empty");
        } else if (ticket.getAssigneeId() == null) {
            return Result.fail("assigneeId can not be empty");
        } else if (!permissionCheckingUtil.isUserExist(ticket.getAssigneeId())) {
            return Result.fail("assigneeId does not exist");
        } else if (!permissionCheckingUtil.isGroupExist(ticket.getBelongsTo())) {
            return Result.fail("group does not exist");
        } else {
            ticketService.updateTicket(ticket);
            return Result.success();
        }
    }

    @PostMapping("/deleteTicket")
    public Result deleteTicket(@RequestParam Integer ticketId){
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        ticketService.deleteTicket(ticketId);
        return Result.success();
    }

    @PostMapping("/addWatcher")
    public Result addWatcher(@RequestParam Integer ticketId, @RequestParam String userIds){
        boolean flag = false;
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        List<Integer> userIdList = SplitUtil.splitBySemicolonInt(userIds);
        for (Integer userId : userIdList){
            if (!permissionCheckingUtil.isUserExist(userId)){
                System.out.println("user " + userId + " does not exist");
                continue;
            }
            else if(ticketService.isWatcher(ticketId, userId)){
                System.out.println("user " + userId + " is already a watcher");
                continue;
            }
            else if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId, userId)){
                System.out.println("you don't have permission to add watcher to this ticket");
                continue;
            }
            ticketService.addWatcher(ticketId, userId);
            flag = true;
        }
        if (flag){
            return Result.success();
        }
        else{
            return Result.fail("add watcher failed");
        }
    }

    @PostMapping("/addWatchers")
    public Result addWatchers(@RequestParam Integer ticketId, @RequestParam List<Integer> userIds){
        boolean flag = false;
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        for (Integer userId : userIds){
            if (!permissionCheckingUtil.isUserExist(userId)){
                System.out.println("user " + userId + " does not exist");
                continue;
            }
            else if(ticketService.isWatcher(ticketId, userId)){
                System.out.println("user " + userId + " is already a watcher");
                continue;
            }
            else if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId, userId)){
                System.out.println("you don't have permission to add watcher to this ticket");
                continue;
            }
            ticketService.addWatcher(ticketId, userId);
            flag = true;
        }
        if (flag){
            return Result.success();
        }
        else{
            return Result.fail("add watcher failed");
        }
    }

    @PostMapping("/removeWatcher")
    public Result removeWatcher(@RequestParam Integer ticketId, @RequestParam String userIds){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        boolean flag = false;
        if(!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        List<Integer> userIdList = SplitUtil.splitBySemicolonInt(userIds);
        for (Integer userId : userIdList){
            if (!permissionCheckingUtil.isUserExist(userId)){
                System.out.println("user " + userId + " does not exist");
                continue;
            }
            else if(!ticketService.isWatcher(ticketId, userId)){
                System.out.println("user " + userId + " is not a watcher");
                continue;
            }
            ticketService.removeWatcher(ticketId, userId);
            flag = true;
        }
        if (flag){
            return Result.success();
        }
        else{
            return Result.fail("remove watcher failed");
        }
    }

    @PostMapping("/watchTicket")
    public Result watchTicket(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to watch this ticket");
        }
        else if(ticketService.isWatcher(ticketId)){
            return Result.fail("you are already a watcher");
        }
        ticketService.addWatcher(ticketId);
        return Result.success();
    }

    @PostMapping("/unwatchTicket")
    public Result unwatchTicket(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to unwatch this ticket");
        }
        else if(!ticketService.isWatcher(ticketId)){
            return Result.fail("you are not a watcher");
        }
        ticketService.removeWatcher(ticketId);
        return Result.success();
    }

    @GetMapping("/getWatchers")
    public Result getWatchers(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to get watchers of this ticket");
        }
        List<Integer> watchers = ticketService.getWatchers(ticketId);
        return Result.success(watchers);
    }

    @GetMapping("/getWatchersId")
    public Result getWatchersId(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to get watchers of this ticket");
        }
        List<Integer> watchers = ticketService.getWatchersId(ticketId);
        return Result.success(watchers);
    }

    @GetMapping("/getTicketById")
    public Result getTicketById(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to get this ticket");
        }
        Ticket ticket = ticketService.getTicketById(ticketId);
        return Result.success(ticket);
    }

    @GetMapping("/getTicketByAssignee")
    public Result getTicketByAssignee(@RequestParam Integer assigneeId){
        if (!permissionCheckingUtil.isUserExist(assigneeId)){
            return Result.fail("assignee does not exist");
        }
        List<Ticket> tickets = ticketService.getTicketByAssignee(assigneeId);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByWatcher")
    public Result getTicketByWatcher(@RequestParam Integer watcherId){
        if (!permissionCheckingUtil.isUserExist(watcherId)){
            return Result.fail("watcher does not exist");
        }
        List<Ticket> tickets = ticketService.getTicketByWatcher(watcherId);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByGroup")
    public Result getTicketByGroup(@RequestParam Integer groupId){
        if (!permissionCheckingUtil.isGroupExist(groupId)){
            return Result.fail("group does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(groupId)){
            return Result.fail("you don't have permission to get tickets of this group");
        }
        List<Ticket> tickets = ticketService.getTicketByGroup(groupId);
        return Result.success(tickets);
    }

    @GetMapping("/getTicketByCreator")
    public Result getTicketByCreator(@RequestParam Integer creatorId){
        if (!permissionCheckingUtil.isUserExist(creatorId)){
            return Result.fail("creator does not exist");
        }
        List<Ticket> tickets = ticketService.getTicketByCreator(creatorId);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByState")
    public Result getTicketByState(@RequestParam Integer state){
        List<Ticket> tickets = ticketService.getTicketByState(state);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByPriority")
    public Result getTicketByPriority(@RequestParam Integer priority){
        List<Ticket> tickets = ticketService.getTicketByPriority(priority);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketHigherThan")
    public Result getTicketHigherThan(@RequestParam Integer priority){
        List<Ticket> tickets = ticketService.getTicketHigherThan(priority);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByType")
    public Result getTicketByType(@RequestParam Integer type){
        List<Ticket> tickets = ticketService.getTicketByType(type);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByDueTime")
    public Result getTicketByDueTime(@RequestParam String dueTime){
        List<Ticket> tickets = ticketService.getTicketByDueTime(dueTime);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @GetMapping("/getTicketByTitle")
    public Result getTicketByTitle(@RequestParam String title){
        List<Ticket> tickets = ticketService.getTicketByTitle(title);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @PostMapping("/addLink")
    public Result addLink(@RequestParam Integer ticketId1, @RequestParam Integer ticketId2){
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId1) || !permissionCheckingUtil.checkEditPermissionForTicket(ticketId2)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (!permissionCheckingUtil.isTicketExist(ticketId1) || !permissionCheckingUtil.isTicketExist(ticketId2)){
            return Result.fail("ticket does not exist");
        }
        if (ticketId1.equals(ticketId2)){
            return Result.fail("ticket can not link to itself");
        }
        Integer lowerId = ticketId1 < ticketId2 ? ticketId1 : ticketId2;
        Integer higherId = ticketId1 < ticketId2 ? ticketId2 : ticketId1;
        if (ticketService.isLinked(lowerId, higherId)){
            return Result.fail("ticket is already linked");
        }
        ticketService.addLink(lowerId, higherId);
        return Result.success();
    }

    @PostMapping("/addLinks")
    public Result addLinks(@RequestParam Integer ticketId, @RequestParam List<Integer> ticketIds){
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        for (Integer id : ticketIds){
            if (!permissionCheckingUtil.isTicketExist(id)){
                return Result.fail("ticket does not exist");
            }
            if (ticketId.equals(id)){
                return Result.fail("ticket can not link to itself");
            }
            Integer lowerId = ticketId < id ? ticketId : id;
            Integer higherId = ticketId < id ? id : ticketId;
            if (ticketService.isLinked(lowerId, higherId)){
                return Result.fail("ticket is already linked");
            }
            ticketService.addLink(lowerId, higherId);
        }
        return Result.success();
    }

    @PostMapping("/removeLink")
    public Result removeLink(@RequestParam Integer ticketId1, @RequestParam Integer ticketId2){
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId1) || !permissionCheckingUtil.checkEditPermissionForTicket(ticketId2)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (!permissionCheckingUtil.isTicketExist(ticketId1) || !permissionCheckingUtil.isTicketExist(ticketId2)){
            return Result.fail("ticket does not exist");
        }
        if (ticketId1.equals(ticketId2)){
            return Result.fail("ticket can not link to itself");
        }
        Integer lowerId = ticketId1 < ticketId2 ? ticketId1 : ticketId2;
        Integer higherId = ticketId1 < ticketId2 ? ticketId2 : ticketId1;
        if (!ticketService.isLinked(lowerId, higherId)){
            return Result.fail("ticket is not linked");
        }
        ticketService.removeLink(lowerId, higherId);
        return Result.success();
    }

    @GetMapping("/getLinkedTickets")
    public Result getLinkedTickets(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to get this ticket");
        }
        List<Ticket> tickets = ticketService.getLinkedTickets(ticketId);
        List<Ticket> viewableTickets = permissionCheckingUtil.filterViewableTickets(tickets);
        return Result.success(viewableTickets);
    }

    @PostMapping("/transferTicket")
    public Result transferTicket(@RequestParam Integer ticketId, @RequestParam Integer userId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (!permissionCheckingUtil.isUserExist(userId)){
            return Result.fail("user does not exist");
        }
        ticketService.transferTicket(ticketId, userId);
        return Result.success();
    }

    @PostMapping("/transferTicketToGroup")
    public Result transferTicketToGroup(@RequestParam Integer ticketId, @RequestParam Integer groupId) {
        if (!permissionCheckingUtil.isTicketExist(ticketId)) {
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)) {
            return Result.fail("you don't have permission to modify this ticket");
        }
        if (!permissionCheckingUtil.isGroupExist(groupId)) {
            return Result.fail("group does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForCategory(groupId)) {
            return Result.fail("you don't have permission to modify this group");
        }
        ticketService.transferTicketToGroup(ticketId, groupId);
        return Result.success();
    }

    @PostMapping("/addAttachment")
    public Result addAttachment(@RequestParam Integer ticketId, @RequestParam String attachments){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        List<String> attachmentList = SplitUtil.splitBySemicolon(attachments);
        ticketService.addAttachment(ticketId, attachmentList);
        return Result.success();
    }

    @PostMapping("/removeAttachment")
    public Result removeAttachment(@RequestParam Integer ticketId, @RequestParam String attachments){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        List<String> attachmentList = SplitUtil.splitBySemicolon(attachments);
        ticketService.removeAttachment(ticketId, attachmentList);
        return Result.success();
    }

    @GetMapping("/getAttachments")
    public Result getAttachments(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        List<String> attachments = ticketService.getAttachments(ticketId);
        return Result.success(attachments);
    }

    @PostMapping("/addImage")
    public Result addImage(@RequestParam Integer ticketId, @RequestParam String images){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        List<String> imageList = SplitUtil.splitBySemicolon(images);
        ticketService.addImage(ticketId, imageList);
        return Result.success();
    }

    @PostMapping("/removeImage")
    public Result removeImage(@RequestParam Integer ticketId, @RequestParam String images){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkEditPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to modify this ticket");
        }
        List<String> imageList = SplitUtil.splitBySemicolon(images);
        ticketService.removeImage(ticketId, imageList);
        return Result.success();
    }

    @GetMapping("/getImages")
    public Result getImages(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to get this ticket");
        }
        List<String> images = ticketService.getImages(ticketId);
        return Result.success(images);
    }

    @GetMapping("/getStatusById")
    public Result getStatusById(@RequestParam Integer ticketId){
        if (!permissionCheckingUtil.isTicketExist(ticketId)){
            return Result.fail("ticket does not exist");
        }
        if (!permissionCheckingUtil.checkReadPermissionForTicket(ticketId)){
            return Result.fail("you don't have permission to get this ticket");
        }
        Integer status = ticketService.getStatusById(ticketId);
        return Result.success(status);
    }


}
