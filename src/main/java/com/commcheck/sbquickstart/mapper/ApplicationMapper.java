package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ApplicationMapper {
    @Insert("insert into application (sender, receiver, type, status, message, createdTime, updatedTime, lastEditedBy) values (#{currentUserId}, #{groupId}, #{type}, #{status}, #{message}, now(), now(), #{currentUserId})")
    void addApplication(Integer currentUserId, Integer groupId, int type, int status, String message);

    @Insert("insert into application (sender, receiver, type, target, status, message, createdTime, updatedTime, lastEditedBy) values (#{currentUserId}, #{assigneeId}, #{type}, #{ticketId}, #{status}, #{message}, now(), now(), #{currentUserId})")
    void addTicketReminder(Integer currentUserId, Integer assigneeId, Integer ticketId, int type, int status, String message);

//    @Select("select * from application where receiver in (#{inGroupIds}) and type = 1")
//    List<Message> getJoinGroupRequests(List<Integer> inGroupIds);
//
//    @Select("select * from application where receiver in (#{inGroupIds}) and type = 2")
//    List<Message> getAdminGroupRequests(List<Integer> inGroupIds);

    @Select("select * from application where receiver = #{currentUserId} and type = 3")
    List<Message> getTicketReminders(Integer currentUserId);

    @Select("select * from application where receiver in (select categoryId from user_category where userId = #{currentUserId}) and type = 1")
    List<Message> getJoinGroupRequests(Integer currentUserId);

    @Select("select * from application where receiver in (select categoryId from user_category where userId = #{currentUserId}) and type = 2")
    List<Message> getAdminGroupRequests(Integer currentUserId);

    @Select("select * from application where id = #{messageId}")
    Message getApplicationById(Integer messageId);

    @Update("update application set status = #{i} where id = #{messageId}")
    void updateApplicationStatus(Integer messageId, int i);

    @Update("update application set message = #{s} where id = #{messageId}")
    void updateApplicationMessage(Integer messageId, String s);

    @Select("select * from application where sender = #{currentUserId}")
    List<Message> getAllRequestByCurrentUser(Integer currentUserId);

    @Update("update application set lastEditedBy = #{currentUserId}, updatedTime = now() where id = #{messageId}")
    void updateLastEdit(Integer messageId, Integer currentUserId);

    @Select("select * from application where target = #{ticketId} and receiver = #{currentUserId} and type = 3")
    Message getTicketReminderByTicketIdAndReceiver(Integer ticketId, Integer currentUserId);

    @Select("select * from application where sender = #{currentUserId} and receiver = #{groupId} and type = #{type}")
    Message findApplicationBySenderAndReceiverAndType(Integer currentUserId, Integer groupId, int type);

    @Select("select * from application where target = #{ticketId} and receiver = #{assingeeId} and type = 3")
    Message findTicketReminderByTicketIdAndReceiver(Integer ticketId, Integer assingeeId);
}
