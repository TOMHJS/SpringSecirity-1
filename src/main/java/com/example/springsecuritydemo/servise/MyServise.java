package com.example.springsecuritydemo.servise;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface MyServise {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
