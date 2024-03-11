package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Encrypter;
import com.commcheck.sbquickstart.pojo.JWTUtil;
import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.pojo.User;
import com.commcheck.sbquickstart.service.UserService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "username format error...") String username,
                           @Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "password format error...") String password) {
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

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "username format error...") String username,
                        @Pattern(regexp = "^[a-zA-Z0-9_]{5,16}$", message = "password format error...") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.fail("user not exists...");
        }
        else{
            String encryptedPassword = Encrypter.encrypt(password, "MD5");
            if (user.getPassword().equals(encryptedPassword)) {
                Map<String, Object> claims= new HashMap<>();
                claims.put("id", user.getId());
                claims.put("username", user.getUsername());
                String token = JWTUtil.JWTGeneration(claims);
                return Result.success(token);
            }
            else{
                return Result.fail("password error...");
            }
        }
    }

}
