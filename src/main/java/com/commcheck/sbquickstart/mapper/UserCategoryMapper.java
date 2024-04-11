package com.commcheck.sbquickstart.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCategoryMapper {
    @Insert("insert into user_category (userId, categoryId) values (#{currentUserId}, #{id})")
    void addRelation(Integer currentUserId, Integer id);

    @Select("select count(*) from user_category where userId = #{userId} and categoryId = #{groupId}")
    Integer isUserInGroup(Integer userId, Integer groupId);

    @Delete("delete from user_category where userId = #{currentUserId} and categoryId = #{groupId}")
    void deleteRelation(Integer currentUserId, Integer groupId);

    @Select("select userId from user_category where categoryId = #{groupId}")
    List<Integer> listUsersId(Integer groupId);

    @Select("select categoryId from user_category where userId = #{currentUserId}")
    List<Integer> groupsIJioned(Integer currentUserId);

    @Delete("delete from user_category where categoryId = #{id}")
    void deleteAllGroupRelations(Integer id);

    @Select("select categoryId from user_category where userId = #{currentUserId}")
    List<Integer> groupsIJoined(Integer currentUserId);
}
