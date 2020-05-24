package com.hjf.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//SpringMvc 的配置文件,可以这这里添加视图解析器,配置那些资源不走dispatchServlet
@Configuration
public class WebConfig implements WebMvcConfigurer {


    
    //可以配置视图解析,但springboot可以直接在配置文件中添加2行配置就可以
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //这句话的好处是让访问根路径时也可以调转到自定义登陆页面
        registry.addViewController("/").setViewName("redirect:/login-view");
    }
}
