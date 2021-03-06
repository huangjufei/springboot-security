package com.hjf.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {

    @GetMapping(value = "/r/r1")
    @ResponseBody
    public String r1() {
        return getUsername() + "r1,我是被拦截的,且我必须要有p1才权限才可以被访问";
    }

    @GetMapping(value = "/r/r2")
    @ResponseBody
    public String r2() {
        return getUsername() + "r2,我是被拦截的,且我必须要有p2才权限才可以被访问";
    }

    @GetMapping(value = "/r/r3")
    @ResponseBody
    @PreAuthorize("hasAuthority('p2')")//我添加了注解权限控制,现在登陆后,还要拥有p2权限才可以访问
    public String r3() {
        return getUsername() + "r3,我是被拦截的,只要是登陆成功就可以访问";
    }

    @PostMapping(value = "/login-success")
    @ResponseBody
    public String loginSuccess() {
        return getUsername() + " 登录成功,我是不能被直接访问的";
    }


    @PostMapping(value = "/login-fail")
    @ResponseBody
    public String loginFail() {
        //其实这里还是该返回登陆也面,只是提示账号密码不一致
        return "账号密码不一致,登陆失败";
    }


    @GetMapping(value = "/index")
    @ResponseBody
    public String index() {
        return getUsername() + " 我是可以被直接访问的";
    }


    //登陆页面点击登陆返回403可能就是跨域问题
    @GetMapping(value = "/login-view")
    public String loginView() {
        return "login";
    }


    /**
     * 管理会话的
     */
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

}
