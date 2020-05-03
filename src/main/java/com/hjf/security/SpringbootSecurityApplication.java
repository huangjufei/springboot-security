package com.hjf.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * SpringbootSecurity 的helloWorld 程序
 * 如何玩?
 * 启动后访问controller层
 *
 * 主要思想:
 * SpringbootSecurity 分为认证和授权
 * 认证可以理解为就是账号密码检查
 * 授权可以理解为对资源访问的检查
 *
 * 对于认证,我们主要要学习的就是:
 * 1,定义密码规则 PasswordEncoder
 * 2,实现 UserDetailsService 重写得到用户信息方法(用户名,密码,权限,角色等)
 * 3,主要类有 DaoAuthenticationProvider 这里会调 2 得到用户信息,然后进行密码比对
 *
 * 对授权 主要就是 :
 * 1, 继承 WebSecurityConfigurerAdapter 重写  protected void configure(HttpSecurity http) 方法
 * 2, 主要就是在方法内部定义如果拦截url,那些url需要被检查,如何检查,是检查角色还是权限(推荐)
 * 3, 对登陆和退出的重定义(接口),对页面的重定义
 * 4, 当然我们还可以基于注解在Controller方法上拦截,就当前我觉得不是很好(用不到这么细的粒度)
 *
 * 5, 主要类有 AccessDecisionManager 的 decide 方法被3个类实现(都是通过投票确定,机制不一样)默认是AffirmativeBased
 * 所以这个方法我们会常常在这里打断点,排查授权无效等问题
 *
 *
 * 自定义登陆:
 * 1, .loginPage("/login-view")//自定义登陆controller层方法上,默认login;
 *    .loginProcessingUrl("/login")//这是地址和login.jsp页面,点击登陆按钮发出的请求地址一致
 * 2. 可能login.jsp报404,看哈pom的插件和2个依赖是否有
 * 3,需要关闭security的跨域拦截
 *
 * 自定义退出,和上面一样我代码里就没做了
 *
 * 连接数据,只需配置数据源,然后从数据得到用户信息赋值给UserDetails;数据库的表在sql目录下
 *
 * 会话信息只要用户授权成功后就可以从SecurityContextHolder.getContext().getAuthentication();得到
 *
 * 参考
 * https://www.cnblogs.com/zhengqing/p/11612654.html
 * 还有黑马文档
 *
 */
@SpringBootApplication
public class SpringbootSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityApplication.class, args);
    }

}