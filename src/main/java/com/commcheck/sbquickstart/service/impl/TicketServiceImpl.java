package com.commcheck.sbquickstart.service.impl;

import com.commcheck.sbquickstart.mapper.ApplicationMapper;
import com.commcheck.sbquickstart.mapper.TicketMapper;
import com.commcheck.sbquickstart.mapper.TicketTicketMapper;
import com.commcheck.sbquickstart.mapper.TicketWatcherMapper;
import com.commcheck.sbquickstart.pojo.PageBean;
import com.commcheck.sbquickstart.pojo.Ticket;
import com.commcheck.sbquickstart.service.TicketService;
import com.commcheck.sbquickstart.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private TicketWatcherMapper ticketWatcherMapper;
    @Autowired
    private TicketTicketMapper ticketTicketMapper;

    @Autowired
    private ApplicationMapper applicationMapper;
    @Override
    public PageBean<Ticket> list(Integer pageNum, Integer pageSize, Integer belongsTo, Integer priority, Integer state, Integer type, Integer assigneeId, Integer OwnerId, Integer watcherId) {
        PageBean<Ticket> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        List<Ticket> list = ticketMapper.list(currentUserId, belongsTo, priority, state, type, assigneeId, OwnerId, watcherId);
        Page<Ticket> page = (Page<Ticket>) list;
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }


    @Override
    public Integer addTicket(Ticket ticket) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticket.setOwnerId(currentUserId);
        ticket.setLastEditedBy(currentUserId);
        return ticketMapper.addTicket(ticket);
    }

    @Override
    public Ticket getTicketById(Integer ticketId) {
        return ticketMapper.getTicketById(ticketId);
    }

    @Override
    public void modifyPriority(Ticket ticket, Integer priority) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticket.setLastEditedBy(currentUserId);
        ticket.setPriority(priority);
        ticketMapper.modifyPriority(ticket);
    }

    @Override
    public void addTicketToGroup(Ticket ticket, Integer groupId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticket.setLastEditedBy(currentUserId);
        ticket.setBelongsTo(groupId);
        ticketMapper.addTicketToGroup(ticket);
    }

    @Override
    public void modifyState(Ticket ticket, Integer state) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticket.setLastEditedBy(currentUserId);
        ticket.setState(state);
        ticketMapper.modifyState(ticket);
    }

    @Override
    public void modifyAssignee(Ticket ticket, Integer assigneeId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticket.setLastEditedBy(currentUserId);
        ticket.setAssigneeId(assigneeId);
        ticketMapper.modifyAssignee(ticket);
    }

    @Override
    public void updateTicket(Ticket ticket) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticket.setOwnerId(currentUserId);
        ticket.setLastEditedBy(currentUserId);
        ticketMapper.updateTicket(ticket);
    }

    @Override
    public void deleteTicket(Integer ticketId) {
        ticketTicketMapper.removeAllLinks(ticketId);
        ticketWatcherMapper.removeAllWatchers(ticketId);
        applicationMapper.removeAllTicketReminder(ticketId);
        ticketMapper.deleteTicket(ticketId);
    }

    @Override
    public void addWatcher(Integer ticketId, Integer userId) {
        ticketWatcherMapper.addWatcher(ticketId, userId);
    }

    @Override
    public void addWatcher(Integer ticketId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticketWatcherMapper.addWatcher(ticketId, currentUserId);

    }

    @Override
    public void removeWatcher(Integer ticketId, Integer userId) {
        ticketWatcherMapper.removeWatcher(ticketId, userId);
    }

    @Override
    public void removeWatcher(Integer ticketId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticketWatcherMapper.removeWatcher(ticketId, currentUserId);

    }

    @Override
    public Ticket findById(Integer ticketId) {
        return ticketMapper.findById(ticketId);
    }

    @Override
    public boolean isWatcher(Integer ticketId, Integer userId) {
        return ticketWatcherMapper.isWatcher(ticketId, userId) > 0;
    }

    @Override
    public boolean isWatcher(Integer ticketId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        return ticketWatcherMapper.isWatcher(ticketId, currentUserId) > 0;
    }

    @Override
    public List<Integer> getWatchers(Integer ticketId) {
        return ticketWatcherMapper.getWatchers(ticketId);
    }

    @Override
    public List<Ticket> getTicketByAssignee(Integer assigneeId) {
        return ticketMapper.getTicketByAssignee(assigneeId);
    }

    @Override
    public List<Ticket> getTicketByWatcher(Integer watcherId) {
        return ticketWatcherMapper.getTicketByWatcher(watcherId);
    }

    @Override
    public List<Ticket> getTicketByGroup(Integer groupId) {
        return ticketMapper.getTicketByGroup(groupId);
    }

    @Override
    public List<Ticket> getTicketByCreator(Integer creatorId) {
        return ticketMapper.getTicketByCreator(creatorId);
    }

    @Override
    public List<Ticket> getTicketByState(Integer state) {
        return ticketMapper.getTicketByState(state);
    }

    @Override
    public List<Ticket> getTicketByPriority(Integer priority) {
        return ticketMapper.getTicketByPriority(priority);
    }

    @Override
    public List<Ticket> getTicketHigherThan(Integer priority) {
        return ticketMapper.getTicketHigherThan(priority);
    }

    @Override
    public List<Ticket> getTicketByType(Integer type) {
        return ticketMapper.getTicketByType(type);
    }

    @Override
    public List<Ticket> getTicketByDueTime(String dueTime) {
        return ticketMapper.getTicketByDueTime(dueTime);
    }

    @Override
    public List<Ticket> getTicketByTitle(String title) {
        return ticketMapper.getTicketByTitle(title);
    }

    @Override
    public void addLink(Integer ticketId1, Integer ticketId2) {
        ticketTicketMapper.addLink(ticketId1, ticketId2);
    }

    @Override
    public void removeLink(Integer ticketId1, Integer ticketId2) {
        ticketTicketMapper.removeLink(ticketId1, ticketId2);
    }

    @Override
    public List<Ticket> getLinkedTickets(Integer ticketId) {
        return ticketTicketMapper.getLinkedTickets(ticketId);
    }

    @Override
    public boolean isLinked(Integer ticketId1, Integer ticketId2) {
        return ticketTicketMapper.isLinked(ticketId1, ticketId2) != null;
    }

    @Override
    public void addAttachment(Integer ticketId, List<String> attachmentList) {
        List<String> originAttachmentList = ticketMapper.getAttachment(ticketId);
        Set<String> set = new HashSet<>(originAttachmentList);
        set.addAll(attachmentList);
        List<String> newAttachmentList = new ArrayList<>(set);
        String newAttachment = String.join(";", newAttachmentList);
        ticketMapper.addAttachment(ticketId, newAttachment);
        this.updateEditRecord(ticketId);
    }

    @Override
    public void transferTicket(Integer ticketId, Integer userId) {
        ticketMapper.transferTicket(ticketId, userId);
        this.updateEditRecord(ticketId);

    }

    @Override
    public void transferTicketToGroup(Integer ticketId, Integer groupId) {
        ticketMapper.transferTicketToGroup(ticketId, groupId);
        this.updateEditRecord(ticketId);
    }

    @Override
    public void updateEditRecord(Integer ticketId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        ticketMapper.updateEditRecord(ticketId, currentUserId);
    }

    @Override
    public void removeAttachment(Integer ticketId, List<String> attachmentList) {
        List<String> originAttachmentList = ticketMapper.getAttachment(ticketId);
        Set<String> set = new HashSet<>(originAttachmentList);
        set.removeAll(attachmentList);
        List<String> newAttachmentList = new ArrayList<>(set);
        String newAttachment = String.join(";", newAttachmentList);
        ticketMapper.addAttachment(ticketId, newAttachment);
    }

    @Override
    public List<String> getAttachments(Integer ticketId) {
        return ticketMapper.getAttachment(ticketId);
    }

    @Override
    public void addImage(Integer ticketId, List<String> imageList) {
        List<String> originImageList = ticketMapper.getImage(ticketId);
        Set<String> set = new HashSet<>(originImageList);
        set.addAll(imageList);
        List<String> newImageList = new ArrayList<>(set);
        String newImage = String.join(";", newImageList);
        ticketMapper.addImage(ticketId, newImage);
        this.updateEditRecord(ticketId);
    }

    @Override
    public void removeImage(Integer ticketId, List<String> imageList) {
        List<String> originImageList = ticketMapper.getImage(ticketId);
        Set<String> set = new HashSet<>(originImageList);
        set.removeAll(imageList);
        List<String> newImageList = new ArrayList<>(set);
        String newImage = String.join(";", newImageList);
        ticketMapper.addImage(ticketId, newImage);
        this.updateEditRecord(ticketId);
    }

    @Override
    public List<String> getImages(Integer ticketId) {
        return ticketMapper.getImage(ticketId);
    }

    @Override
    public Integer getStatusById(Integer ticketId) {
        return ticketMapper.getStatusById(ticketId);

    }

    @Override
    public List<Integer> getWatchersId(Integer ticketId) {
        return ticketWatcherMapper.getWatchersId(ticketId);
    }

    @Override
    public void removeAllLinks(Integer ticketId) {
        ticketTicketMapper.removeAllLinks(ticketId);
    }

    @Override
    public void removeAllWatchers(Integer ticketId) {
        ticketWatcherMapper.removeAllWatchers(ticketId);

    }

    @Override
    public List<Integer> getLinkedTicketIds(Integer ticketId) {
        return ticketTicketMapper.getLinkedTicketIds(ticketId);
    }

    @Override
    public List<Integer> getTicketByWatcherId(Integer watcherId) {
        return ticketWatcherMapper.getTicketIdByWatcherId(watcherId);
    }



}
