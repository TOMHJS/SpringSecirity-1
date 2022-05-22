package com.example.springsecuritydemo.config;

import com.example.springsecuritydemo.handle.MyAccessDeniedHandler;
import com.example.springsecuritydemo.handle.MyAuthenticationFailureHandler;
import com.example.springsecuritydemo.handle.MyAuthenticationSuccessHandler;
import com.example.springsecuritydemo.servise.UserDetailsServiseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private UserDetailsServiseImpl userDetailsServise;

    @Autowired
    private DataSource dataSource;

    @Autowired
    PersistentTokenRepository persistentTokenRepository;

    @Override

    protected void configure(HttpSecurity http) throws Exception {
        //表单提交
        http.formLogin()
                //当发现是/login会认为是登录，必须和表单提交的地址一样，去执行UserDetailsServiseImpl的登录逻辑
                .loginProcessingUrl("/login")
                //自定义登录界面
                .loginPage("/login.html")
                //登录成功后的跳转页面，post请求
               .successForwardUrl("/toMain")
                //d登录成功处理器，不能和successForwardUrl共存
                //.successHandler(new MyAuthenticationSuccessHandler("http://www.baidu.com"))
                //登录失败后跳转页面，post请求
                .failureForwardUrl("/toError");
                //failureForwardUrl会冲突，不要
                //.failureHandler(new MyAuthenticationFailureHandler("/error.html"));

        //授权认证
        http.authorizeRequests()
                //error.html不需要被认证
                .antMatchers("/error.html").permitAll()
                //login.html不需要被认证
                .antMatchers("/login.html").permitAll()
                //所有的请求都需要被验证,必须登录之后被访问
                //.mvcMatchers("/demo").servletPath("/xxx").permitAll()
                //必须有权限才能登录
                //.antMatchers("/main1.html").hasAuthority("admin")
                .antMatchers("/main1.html").hasRole("abc")
                .anyRequest().authenticated();
                //.anyRequest().access("@myServiseImpl.hasPermission(request,authentication)");


        //关闭csrf防护
        http.csrf().disable();

        //异常处理
        http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

        http.rememberMe()
                //失效时间，单位秒
                .tokenValiditySeconds(60)
                //自定义登录逻辑
                .userDetailsService(userDetailsServise)
                //持久层对象
                .tokenRepository(persistentTokenRepository);

        //退出登录
        http.logout().logoutSuccessUrl("login.html");

    }


    @Bean
    public PasswordEncoder getPw(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public PersistentTokenRepository getPersistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository=new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //自动建表，第一次启动时候需要，第二次启动注释掉
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
