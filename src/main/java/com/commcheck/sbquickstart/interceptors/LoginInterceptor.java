package com.commcheck.sbquickstart.interceptors;

import com.commcheck.sbquickstart.pojo.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Data
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> map = JWTUtil.JWTVerification(token);
            System.out.println("user profile: " + map);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
