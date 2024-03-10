package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public Result register(String username, String password) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return Result.fail("user already exists...");
        }
        else{
            Result result = userService.addUser(username, password);
            if(result.getCode() == 0){
                System.out.println(result.getCode());
                return Result.success("register success...");
            }
            else{
                return Result.fail(result.getMessage());
            }
        }
    }

}
