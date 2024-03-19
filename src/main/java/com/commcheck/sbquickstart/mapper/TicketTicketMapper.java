package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.Ticket;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TicketTicketMapper {
    @Insert("insert into ticket_ticket (ticketId, linkedTicketId) values (#{ticketId1}, #{ticketId2})")
    void addLink(Integer ticketId1, Integer ticketId2);

//    @Delete("delete from ticket_ticket where (ticketId = #{ticketId1} and linkedTicketId = #{ticketId2}) or (ticketId = #{ticketId2} and linkedTicketId = #{ticketId1})")
    @Delete("delete from ticket_ticket where (ticketId = #{ticketId1} and linkedTicketId = #{ticketId2})")
    void removeLink(Integer ticketId1, Integer ticketId2);

//    TODO: untested
//    @Select("select t.* from tickets t join ticket_ticket tt on (t.id = tt.linkedTicketId) where tt.ticketId = #{ticketId} or tt.linkedTicketId = #{ticketId}")
    @Select("select t.* from tickets t join ticket_ticket tt on (t.id = tt.linkedTicketId) where tt.ticketId = #{ticketId} union select t.* from tickets t join ticket_ticket tt on (t.id = tt.ticketId) where tt.linkedTicketId = #{ticketId}")
    List<Ticket> getLinkedTickets(Integer ticketId);

    @Select("select linkedTicketId from ticket_ticket where ticketId = #{ticketId1} and linkedTicketId = #{ticketId2}")
    Integer isLinked(Integer ticketId1, Integer ticketId2);
}
