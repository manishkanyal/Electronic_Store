package com.lcwd.Electronic.Store.Eletronic.Store.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtEntryAuthenticationPoint implements AuthenticationEntryPoint {

//This method throws an error when the unauthenticated user tries to access the website
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);   //This is equivalent to 404 unauthorised request
        PrintWriter writer=response.getWriter();
        writer.println("Access Denied!!!"+authException.getMessage());
    }
}
