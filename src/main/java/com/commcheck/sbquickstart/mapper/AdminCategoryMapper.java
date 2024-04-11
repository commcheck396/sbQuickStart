package com.commcheck.sbquickstart.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminCategoryMapper {
    @Insert("insert into admin_category (userId, categoryId) values (#{currentUserId}, #{id})")
    void addAdminRelation(Integer currentUserId, Integer id);

    @Select("select count(*) from admin_category where userId = #{currentUserId} and categoryId = #{groupId}")
    int isAdminInGroup(Integer currentUserId, Integer groupId);

    @Delete("delete from admin_category where userId = #{currentUserId} and categoryId = #{groupId}")
    void deleteAdminRelation(Integer currentUserId, Integer groupId);

    @Select("select userId from admin_category where categoryId = #{groupId}")
    List<Integer> listGroupAdminsId(Integer groupId);

    @Select("select categoryId from admin_category where userId = #{currentUserId}")
    List<Integer> groupsIAdmin(Integer currentUserId);

    @Delete("delete from admin_category where categoryId = #{id}")
    void deleteAllGroupRelations(Integer id);
}
