package com.hjf.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 授权
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //密码在test目录下有测试方法
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    /**
     * 配置安全拦截机制(拦截规则),下面的配置很容易出错,少一个/就会错
     * 规则的顺序是重要的,更具体的规则应该先写.默认AffirmativeBased规则,一旦有一个匹配上且又放行了,后面的都忽略
     * 保护URL常用的方法有：
     * authenticated() 保护URL，需要用户登录
     * permitAll() 指定URL无需保护，一般应用与静态资源文件
     * hasRole(String role) 限制单个角色访问，角色将被增加 “ROLE_” .所以”ADMIN” 将和 “ROLE_ADMIN”进行比较.
     * hasAuthority(String authority) 限制单个权限访问
     * hasAnyRole(String… roles)允许多个角色访问.
     * hasAnyAuthority(String… authorities) 允许多个权限访问.
     * access(String attribute) 该方法使用 SpEL表达式, 所以可以创建复杂的限制.
     * hasIpAddress(String ipaddressExpression) 限制IP地址或子网
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//关闭跨域拦截,即让spring security不再限制CSRF
                .authorizeRequests()
                //如果权限拦截有问题可以在这个类下AffirmativeBased打断点
                .antMatchers("/r/r1").hasAuthority("p1")
                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/r/**").authenticated()//指定了除了r1、r2之外"/r/**"资源，通过身份认证就能够访问，这里使用SpEL（Spring Expression Language）表达式
                .anyRequest().permitAll()//剩余的尚未匹配的资源，不做保护
                .and()
                .formLogin()//允许表单登陆
                .loginPage("/login-view")//自定义登陆页面地址(controller层),默认login;如果屏蔽这行就是默认登陆页面
                .loginProcessingUrl("/login")//这是地址和login.jsp页面,其实也是交给默人的login 方法在处理;(会走到认证流程中去)点击登陆按钮发出的请求地址一致
                .successForwardUrl("/login-success");//这里可以配置首页地址,这里我就随便返回了字符串

    }



//注意不等于上面方法,配置用户信息服务,注意这里是注入注解,这个方法相当于UserDetails(基于内存得到UserDetails)
/*
  @Autowired
    public void config(AuthenticationManagerBuilder auth) throws Exception {
        // 在内存中配置用户，配置多个用户调用`and()`方法
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder()) // 指定加密方式
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN").authorities("p1","p2")
                .and()
                .withUser("test").password(passwordEncoder().encode("123456")).roles("USER").authorities("p2");
    }
*/



}
