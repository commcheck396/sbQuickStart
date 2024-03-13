package com.commcheck.sbquickstart.controller;

import com.commcheck.sbquickstart.pojo.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private final HttpServletResponse response;

    public TicketController(HttpServletResponse response) {
        this.response = response;
    }

    @GetMapping("/list")
    public Result<String> list() {
//        try {
//            Map<String, Object> map = JWTUtil.JWTVerification(token);
//            System.out.println("user profile: " + map);
//        } catch (Exception e) {
//            response.setStatus(401);
//            throw new RuntimeException(e);
//        }
        return Result.success("Ticket List");
    }
}
