package com.nowonbun.household.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtProvider jwtProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable()
        .csrf().disable()
        
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()

        .authorizeRequests()
        .antMatchers("/*", "/static/css/*", "/static/js/*", "/auth/login.auth").permitAll()
        .anyRequest().authenticated()
        .and()

        .addFilterAfter(new WebFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
  }
}
