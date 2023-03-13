package com.lth.community.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("timestamp", LocalDate.now());
        errorJson.put("status", HttpStatus.valueOf(403).toString());
        errorJson.put("message", "No Permission");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(errorJson));
        writer.flush();
        writer.close();
    }
    
}
