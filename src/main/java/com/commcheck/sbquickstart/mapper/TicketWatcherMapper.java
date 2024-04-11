package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.Ticket;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketWatcherMapper {
    @Insert("insert into ticket_watcher (ticketId, userId) values (#{ticketId}, #{currentUserId})")
    void addWatcher(Integer ticketId, Integer currentUserId);

    @Delete("delete from ticket_watcher where ticketId = #{ticketId} and userId = #{currentUserId}")
    void removeWatcher(Integer ticketId, Integer currentUserId);

    @Select("select count(*) from ticket_watcher where ticketId = #{ticketId} and userId = #{userId}")
    int isWatcher(Integer ticketId, Integer userId);

    @Select("select userId from ticket_watcher where ticketId = #{ticketId}")
    List<Integer> getWatchers(Integer ticketId);

//    @Select("select ticketId from ticket_watcher where userId = #{watcherId}")
    @Select("select * from tickets where id in (select ticketId from ticket_watcher where userId = #{watcherId})")
    List<Ticket> getTicketByWatcher(Integer watcherId);

    @Select("select userId from ticket_watcher where ticketId = #{ticketId}")
    List<Integer> getWatchersId(Integer ticketId);
}
