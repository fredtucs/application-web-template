package org.wifry.fooddelivery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.wifry.fooddelivery.exceptions.JsfRedirectStrategy;
import org.wifry.fooddelivery.security.AuthenticationSuccessCustomHandler;
import org.wifry.fooddelivery.services.security.PermissionService;
import org.wifry.fooddelivery.util.ObjectUtils;
import org.wifry.fooddelivery.model.Permission;
import org.wifry.fooddelivery.security.AccessDeniedHandler;
import org.wifry.fooddelivery.security.AuthenticationFailureHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wtuco on 16/03/2016.
 * <p>
 * Config Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessCustomHandler successCustomHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<Permission> permissions = permissionService.list();

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry interceptUrlRegistry = http.authorizeRequests();

        for (Permission permission : permissions) {
            if (permission.getPermissionName().contains("LIST") && !ObjectUtils.isEmpty(permission.getUrl()))
                interceptUrlRegistry.antMatchers(permission.getUrl()).hasAuthority(permission.getPermissionName());
        }

        http.authorizeRequests()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/pages/**").authenticated()
                .antMatchers("/rest/**").authenticated();

        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/loginIn")
                .passwordParameter("j_password")
                .usernameParameter("j_username")
                .successHandler(successCustomHandler)
                .failureHandler(failureHandler);

        http.logout()
                .logoutUrl("/j_logout")
                .logoutSuccessUrl("/login.html?state=logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

        http.addFilter(concurrentSessionFilter())
                .addFilter(sessionManagementFilter());

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

        http.sessionManagement()
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy());

        http.csrf().disable();

        http.headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new Md5PasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter() {
        return new ConcurrentSessionFilter(sessionRegistry(), "/login.html?state=logout");
    }

    @Bean
    public SessionManagementFilter sessionManagementFilter() {
        JsfRedirectStrategy jsfRedirectStrategy = new JsfRedirectStrategy();
        jsfRedirectStrategy.setInvalidSessionUrl("/errorpages/expired.html");
        SessionManagementFilter sessionManagementFilter = new SessionManagementFilter(httpSessionSecurityContextRepository());
        sessionManagementFilter.setInvalidSessionStrategy(jsfRedirectStrategy);
        return sessionManagementFilter;
    }

    @Bean
    public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {

        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        concurrentSessionControlStrategy.setMaximumSessions(1);
        concurrentSessionControlStrategy.setExceptionIfMaximumExceeded(false);

        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
        delegateStrategies.add(concurrentSessionControlStrategy);
        delegateStrategies.add(new SessionFixationProtectionStrategy());
        delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));

        return new CompositeSessionAuthenticationStrategy(delegateStrategies);
    }

}
