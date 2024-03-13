package com.commcheck.sbquickstart.utils;

import com.commcheck.sbquickstart.pojo.Category;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.UserService;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PermissionCheckingUtil {
    @Autowired
    private UserService userService;
    public boolean checkPermissionForCategory(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer currentUserId = (Integer) map.get("id");
        User currentUser = userService.findById(currentUserId);
        if (category.getOwnerId() != currentUserId && currentUser.getStatus() != 0){
            return false;
        }
        return true;
    }
}
