package com.nowonbun.household.auth;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class WebFilter extends GenericFilterBean {
  private JwtProvider jwtProvider;
  private UsernamePasswordAuthenticationToken auth;

  public WebFilter(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;

    auth = new UsernamePasswordAuthenticationToken("", null, new ArrayList<>());
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    var req = (HttpServletRequest) request;
    var res = (HttpServletResponse) response;

    String url = req.getRequestURI();
    if (url.indexOf("/", 1) == -1 || url.matches("/static/(.*)")) {
      chain.doFilter(request, response);
      return;
    }
    if (!url.matches("/auth/login.auth") && !url.matches("/auth/check.auth")) {
      Jws<Claims> token = null;
      if (url.matches("/auth/(.*)")) {
        token = jwtProvider.getRefreshToken(req);
      } else {
        token = jwtProvider.getAccessToken(req);
      }
      if (token != null) {
        SecurityContextHolder.getContext().setAuthentication(auth);
      } else {
        res.setStatus(403);
      }
    }
    chain.doFilter(request, response);
  }
}