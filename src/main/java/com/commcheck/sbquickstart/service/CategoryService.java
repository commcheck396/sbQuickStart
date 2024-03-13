package com.commcheck.sbquickstart.service;

import com.commcheck.sbquickstart.pojo.Category;

import java.util.List;

public interface CategoryService {
    Category findByCategoryName(String categoryName);

    void addCategory(Category category);

    List<Category> listCategory();

    Category findById(Integer id);

    void updateCategory(Category category);

    void deleteCategory(Integer id);
}
