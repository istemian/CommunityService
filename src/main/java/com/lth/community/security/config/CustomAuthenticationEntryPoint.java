package com.lth.community.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(401);
        Map<String, Object> errorJson = new LinkedHashMap<>();
        errorJson.put("timestamp", Instant.now().toEpochMilli());
        errorJson.put("status", HttpStatus.valueOf(401).toString());
        errorJson.put("message", "This Service needs login");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(errorJson));
        writer.flush();
        writer.close();
    }
}
