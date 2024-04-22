package com.commcheck.sbquickstart.utils;

import com.commcheck.sbquickstart.mapper.AdminCategoryMapper;
import com.commcheck.sbquickstart.mapper.TicketTicketMapper;
import com.commcheck.sbquickstart.mapper.UserCategoryMapper;
import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.CategoryService;
import com.commcheck.sbquickstart.service.TicketService;
import com.commcheck.sbquickstart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.print.PrinterGraphics;
import java.util.List;
import java.util.Map;

@Component
public class PermissionCheckingUtil {
    @Autowired
    private UserService userService;

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public TicketService ticketService;

    @Autowired
    public TicketTicketMapper ticketTicketMapper;

    @Autowired
    public UserCategoryMapper userCategoryMapper;

    @Autowired
    public AdminCategoryMapper adminCategoryMapper;



    public boolean isRootAdmin(){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        User user = userService.findById(currentUserId);
        return user.getStatus() <= 1;
    }

//    public boolean isGroupAdmin(Integer groupId){
//        Map<String, Object> map = ThreadLocalUtil.get();
//        Integer currentUserId = (Integer) map.get("id");
//        String groupAdmins = categoryService.findById(groupId).getGroupAdmin();
//        return groupAdmins.contains(currentUserId.toString());
//    }
    public boolean isGroupAdmin(Integer groupId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        return adminCategoryMapper.isAdminInGroup(currentUserId, groupId) >= 1;
    }

    public boolean isGroupAdmin(Integer groupId, Integer currentUserId){
        return adminCategoryMapper.isAdminInGroup(currentUserId, groupId) >= 1;
    }

    public boolean isInGroup(Integer groupId){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        return userCategoryMapper.isUserInGroup(currentUserId, groupId) >= 1;
    }

    public boolean isInGroup(Integer groupId, Integer currentUserId){
        return userCategoryMapper.isUserInGroup(currentUserId, groupId) >= 1;
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

    public boolean checkEditPermissionForTicket(Integer ticketId) {
        Integer ownerId = ticketService.findById(ticketId).getOwnerId();
        Integer currentUserId = getCurrentUserId();
        Integer assigneeId = ticketService.findById(ticketId).getAssigneeId();
        return ownerId.equals(currentUserId) || assigneeId.equals(currentUserId) || isRootAdmin();
    }
    public boolean checkReadPermissionForTicket(Integer ticketId) {
        return true;
    }

    public boolean checkReadPermissionForTicket(Integer ticketId, Integer userId) {
        return true;
    }

    public boolean isGroupExist(Integer groupId){
        return categoryService.findById(groupId) != null;
    }

    public Integer getCurrentUserId(){
        Map<String, Object> map = ThreadLocalUtil.get();
        return (Integer) map.get("id");
    }

    public boolean isTicketExist(Integer ticketId){
         return ticketService.findById(ticketId) != null;
    }



    public List<Ticket> filterViewableTickets(List<Ticket> tickets) {
        List<Ticket> viewableTickets = tickets;
        for (Ticket ticket : tickets) {
            if (!checkReadPermissionForTicket(ticket.getId())) {
                viewableTickets.remove(ticket);
            }
        }
        return viewableTickets;
    }

    public boolean checkReadPermissionForCategory(Integer groupId) {
        if (isInGroup(groupId)||isGroupAdmin(groupId)||isGroupOwner(groupId)||isRootAdmin()){
            return true;
        }
        return false;
    }
}
