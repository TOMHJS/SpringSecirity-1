package com.example.springsecuritydemo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.RequestWrapper;

@Controller
public class LoginController {

   /* @RequestMapping("login")
    public String login(){
        System.out.println("执行登录方法");
        return  "redirect:main.html";
    }
*/
    //页面跳转
    //@Secured("ROLE_abc")
    @PreAuthorize("hasRole('abc')")
    @RequestMapping("toMain")
    public String toMain(){
        return  "redirect:main.html";
    }

    //登录跳转界面
    @RequestMapping("toError")
    public String toError(){
        return  "redirect:error.html";
    }

    @RequestMapping("demo")
    public String demo(){
        return  "demo";
    }



}
