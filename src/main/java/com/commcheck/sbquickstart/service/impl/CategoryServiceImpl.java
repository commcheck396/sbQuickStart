package com.commcheck.sbquickstart.service.impl;

import com.commcheck.sbquickstart.mapper.CategoryMapper;
import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.CategoryService;
import com.commcheck.sbquickstart.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public Category findByCategoryName(String categoryName) {
        return categoryMapper.findByCategoryName(categoryName);
    }

    @Override
    public void addCategory(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setOwnerId(currentUserId);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> listCategory() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        return categoryMapper.list(currentUserId);
    }

    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    public void updateCategory(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        category.setLastEditedBy(currentUserId);
        categoryMapper.update(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.deleteCategory(id);
    }
}
