package com.commcheck.sbquickstart.service;


import com.commcheck.sbquickstart.pojo.PageBean;
import com.commcheck.sbquickstart.pojo.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {
    PageBean<Ticket> list(Integer pageNum, Integer pageSize, String belongTo, Integer priority, Integer state, Integer type);

    Integer addTicket(Ticket ticket);

    Ticket getTicketById(Integer ticketId);

    void modifyPriority(Ticket ticket, Integer priority);

    void addTicketToGroup(Ticket ticket, Integer groupId);

    void modifyState(Ticket ticket, Integer state);

    void modifyAssignee(Ticket ticket, Integer assigneeId);

    void updateTicket(Ticket ticket);

    void deleteTicket(Integer ticketId);

    void addWatcher(Integer ticketId, Integer userId);

    void addWatcher(Integer ticketId);


    void removeWatcher(Integer ticketId, Integer userId);

    void removeWatcher(Integer ticketId);


    Ticket findById(Integer ticketId);

    boolean isWatcher(Integer ticketId, Integer userId);

    boolean isWatcher(Integer ticketId);


    List<Integer> getWatchers(Integer ticketId);

    List<Ticket> getTicketByAssignee(Integer assigneeId);

    List<Ticket> getTicketByWatcher(Integer watcherId);

    List<Ticket> getTicketByGroup(Integer groupId);

    List<Ticket> getTicketByCreator(Integer creatorId);

    List<Ticket> getTicketByState(Integer state);

    List<Ticket> getTicketByPriority(Integer priority);

    List<Ticket> getTicketHigherThan(Integer priority);

    List<Ticket> getTicketByType(Integer type);

    List<Ticket> getTicketByDueTime(String dueTime);

    List<Ticket> getTicketByTitle(String title);

    void addLink(Integer ticketId1, Integer ticketId2);

    void removeLink(Integer ticketId1, Integer ticketId2);

    List<Ticket> getLinkedTickets(Integer ticketId);

    boolean isLinked(Integer ticketId1, Integer ticketId2);

    void addAttachment(Integer ticketId, List<String> attachmentList);

    void transferTicket(Integer ticketId, Integer userId);

    void transferTicketToGroup(Integer ticketId, Integer groupId);

    void updateEditRecord(Integer ticketId);

    void removeAttachment(Integer ticketId, List<String> attachmentList);

    List<String> getAttachments(Integer ticketId);

    void addImage(Integer ticketId, List<String> imageList);

    void removeImage(Integer ticketId, List<String> imageList);
}
