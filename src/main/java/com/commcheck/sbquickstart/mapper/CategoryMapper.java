package com.commcheck.sbquickstart.mapper;

import com.commcheck.sbquickstart.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("SELECT * FROM category WHERE categoryName = #{categoryName}")
    Category findByCategoryName(String categoryName);

    @Insert("insert into category (categoryName, categoryDetail, ownerId, createdTime, updatedTime, lastEditedBy)" +
            "values (#{categoryName}, #{categoryDetail}, #{ownerId}, now(), now(), #{ownerId})")
    void add(Category category);

    @Select("select * from category where ownerId = #{currentUserId}")
    List<Category> list(Integer currentUserId);

    @Select("select * from category where id = #{id}")
    Category findById(Integer id);

    @Update("update category set categoryName = #{categoryName}, categoryDetail = #{categoryDetail}, updatedTime = now(), lastEditedBy = #{lastEditedBy} where id = #{id}")
    void update(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(Integer id);
}
