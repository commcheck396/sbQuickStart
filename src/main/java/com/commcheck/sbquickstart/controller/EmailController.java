package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Result;
import com.commcheck.sbquickstart.service.UserService;
import com.commcheck.sbquickstart.utils.Encrypter;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PrivilegedExceptionAction;
import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private UserService userService;
    protected Map<String, String> emailCodeMap = new java.util.concurrent.ConcurrentHashMap<>();
    @GetMapping("/sendCode")
    public Result sendCode(@RequestParam String addr){
        Email email = new SimpleEmail();
        email.setHostName("smtp.qq.com");
        email.setSmtpPort(465);
        email.setCharset("UTF-8");
        try {
            email.addTo(addr);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        try {
            email.setFrom("comm_check@qq.com", "CommCheck");
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        email.setAuthentication("comm_check@qq.com", "rubjwnpabsxqiieh");
        email.setSSLOnConnect(true);
        String randomCode = String.valueOf((int)((Math.random()*9+1)*100000));
        try {
            email.setSubject("请查收您的验证码");
            email.setMsg("您的验证码是：" + randomCode);
            email.send();
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
        emailCodeMap.put(addr, randomCode);
        return Result.success();
    }

    @GetMapping("/verifyCode")
    public Result verifyCode(@RequestParam String addr, @RequestParam String code){
        if(emailCodeMap.containsKey(addr) && emailCodeMap.get(addr).equals(code)){
            emailCodeMap.remove(addr);
            return Result.success();
        }
        return Result.fail("验证码错误");
    }

    @GetMapping("/getUserByName")
    public Result getUserByName(@RequestParam String name){
        return Result.success(userService.findByUsername(name));
    }

    @PatchMapping("/resetPassword")
    public Result resetPassword(@RequestParam String password, @RequestParam Integer id){
        String encryptedPassword = Encrypter.encrypt(password, "MD5");
        userService.resetPassword(encryptedPassword, id);
        return Result.success();
    }


}
