package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TicketMapper {
    List<Ticket> list(Integer currentUserId, String belongTo, Integer priority, Integer state, Integer type);

    @Insert("insert into tickets (title, description, assigneeId, ownerId, createdTime, updatedTime, dueTime, lastEditedBy, belongsTo) " +
            "values (#{title}, #{description}, #{assigneeId}, #{ownerId}, now(), now(), DATE_ADD(now(), INTERVAL #{dueTime} DAY), #{ownerId}, #{belongsTo})")
//    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
//    Integer addTicket(Ticket ticket);
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer addTicket(Ticket ticket);

    @Select("select * from tickets where id = #{ticketId}")
    Ticket getTicketById(Integer ticketId);

    @Update("update tickets set priority = #{priority}, lastEditedBy = #{lastEditedBy}, updatedTime = now() where id = #{id}")
    void modifyPriority(Ticket ticketId);

    @Update("update tickets set belongsTo = #{belongsTo}, lastEditedBy = #{lastEditedBy}, updatedTime = now() where id = #{id}")
    void addTicketToGroup(Ticket ticket);

    @Update("update tickets set state = #{state}, lastEditedBy = #{lastEditedBy}, updatedTime = now() where id = #{id}")
    void modifyState(Ticket ticket);

    @Update("update tickets set assigneeId = #{assigneeId}, lastEditedBy = #{lastEditedBy}, updatedTime = now() where id = #{id}")
    void modifyAssignee(Ticket ticket);

    @Update("update tickets set title = #{title}, description = #{description}, lastEditedBy = #{lastEditedBy}, updatedTime = now(), assigneeId = #{assigneeId}, belongsTo = #{belongsTo}, dueTime = DATE_ADD(now(), INTERVAL #{dueTime} DAY) where id = #{id}")
    void updateTicket(Ticket ticket);

    @Delete("delete from tickets where id = #{ticketId}")
    void deleteTicket(Integer ticketId);

    @Select("select * from tickets where id = #{ticketId}")
    Ticket findById(Integer ticketId);

    @Select("select * from tickets where assigneeId = #{assigneeId}")
    List<Ticket> getTicketByAssignee(Integer assigneeId);

    @Select("select * from tickets where belongsTo = #{ownerId}")
    List<Ticket> getTicketByGroup(Integer groupId);

    @Select("select * from tickets where ownerId = #{creatorId}")
    List<Ticket> getTicketByCreator(Integer creatorId);

    @Select("select * from tickets where state = #{state}")
    List<Ticket> getTicketByState(Integer state);

    @Select("select * from tickets where priority = #{priority}")
    List<Ticket> getTicketByPriority(Integer priority);

    @Select("select * from tickets where priority > #{priority}")
    List<Ticket> getTicketHigherThan(Integer priority);

    @Select("select * from tickets where type = #{type}")
    List<Ticket> getTicketByType(Integer type);

//    TODO: handle dueTime
    @Select("select * from tickets where dueTime = #{dueTime}")
    List<Ticket> getTicketByDueTime(String dueTime);

    @Select("select * from tickets where title = #{title}")
    List<Ticket> getTicketByTitle(String title);

    @Select("select attachment from tickets where id = #{ticketId}")
    List<String> getAttachment(Integer ticketId);

    @Update("update tickets set attachment = #{newAttachmentList} where id = #{ticketId}")
    void addAttachment(Integer ticketId, String newAttachmentList);

    @Update("update tickets set lastEditedBy = #{currentUserId}, updatedTime = now() where id = #{ticketId}")
    void updateEditRecord(Integer ticketId, Integer currentUserId);

    @Update("update tickets set ownerId = #{userId}, updatedTime = now() where id = #{ticketId}")
    void transferTicket(Integer ticketId, Integer userId);

    @Update("update tickets set belongsTo = #{groupId}, updatedTime = now() where id = #{ticketId}")
    void transferTicketToGroup(Integer ticketId, Integer groupId);

    @Select("select image from tickets where id = #{ticketId}")
    List<String> getImage(Integer ticketId);

    @Update("update tickets set image = #{newImage} where id = #{ticketId}")
    void addImage(Integer ticketId, String newImage);

    List<Ticket> findByOwnerId(Integer currentUserId);
}
