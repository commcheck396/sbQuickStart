package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.service.CategoryService;
import com.commcheck.sbquickstart.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public Result add(@RequestBody @Validated(Category.Add.class) Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().length() == 0) {
            return Result.fail("Category Name cannot be empty");
        }
        if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
            return Result.fail("Category Name already exists");
        }
        categoryService.addCategory(category);
        return Result.success();
    }
    @GetMapping()
    public Result<List<Category>> list() {
        List<Category> categoryList = categoryService.listCategory();
        return Result.success(categoryList);
    }

    @GetMapping("/detail")
    public Result<Category> detail(@RequestParam("id") Integer id) {
        Category category = categoryService.findById(id);
        return Result.success(category);
    }

    @PutMapping()
    public Result update(@RequestBody @Validated Category category) {
        Integer categoryId = category.getId();
        if (categoryService.findById(categoryId) == null){
            return Result.fail("Category does not exist");
        }
        if (categoryService.findByCategoryName(category.getCategoryName()) != null){
            return Result.fail("Category Name already exists");
        }
        categoryService.updateCategory(category);
        return Result.success();
    }

    @DeleteMapping()
    public Result delete(@RequestParam("id") Integer id) {
        if (categoryService.findById(id) == null){
            return Result.fail("Category does not exist");
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        Category currentCategory = categoryService.findById(id);
        if (currentCategory.getOwnerId() != currentUserId){
            return Result.fail("You do not have permission to delete this category");
        }
        categoryService.deleteCategory(id);
        return Result.success();
    }

}
