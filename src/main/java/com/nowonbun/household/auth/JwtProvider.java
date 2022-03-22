package com.nowonbun.household.auth;

import java.util.Base64;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nowonbun.household.service.StringUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtProvider {
  @Value("${spring.jwt.secret}")
  private String SECRET_KEY = "";
  @Value("${spring.jwt.access}")
  private String ACCESS_KEY = "";
  @Autowired
  private StringUtil stringUtil;

  private final int TICK_24HOUR = 1000 * 60 * 60 * 24;
  private final int TICK_10MIN = 1000 * 60 * 10;
  private final String X_AUTH_TOKEN_REFRESH = "X-AUTH-TOKEN-REFRESH";
  private final String X_AUTH_TOKEN_ACCESS = "X-AUTH-TOKEN-ACCESS";

  public JwtProvider() {
    SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    ACCESS_KEY = Base64.getEncoder().encodeToString(ACCESS_KEY.getBytes());
  }

  public String createRefreshToken(String id, HttpServletRequest req, HttpServletResponse res) {
    var token = createToken(id, TICK_24HOUR * 14, SECRET_KEY);
    var cookie = createCookie(token, X_AUTH_TOKEN_REFRESH, TICK_24HOUR * 14 / 1000);
    res.addCookie(cookie);
    return token;
  }

  public String createAccessToken(String id, HttpServletRequest req, HttpServletResponse res) {
    var token = createToken(id, TICK_10MIN, ACCESS_KEY);
    res.setHeader(X_AUTH_TOKEN_ACCESS, token);
    return token;
  }

  public void clearToken(HttpServletRequest req, HttpServletResponse res) {
    if (req.getCookies() == null) {
      return;
    }
    res.addCookie(this.createCookie(null, X_AUTH_TOKEN_REFRESH, 0));
  }

  public String createToken(String id, long milisecond, String signature) {
    var claims = Jwts.claims().setId(id);
    var now = new Date();
    return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(new Date(now.getTime() + milisecond)).signWith(SignatureAlgorithm.HS256, signature).compact();
  }

  public Jws<Claims> parseToken(String jwt, String signature) {
    try {
      return Jwts.parser().setSigningKey(signature).parseClaimsJws(jwt);
    } catch (SignatureException | ExpiredJwtException | IllegalArgumentException e) {
      return null;
    }
  }

  public String getId(Jws<Claims> token) {
    return token.getBody().getId();
  }

  public Jws<Claims> getRefreshToken(HttpServletRequest req) {
    var cookie = this.getCookie(req, X_AUTH_TOKEN_REFRESH);
    if (cookie != null) {
      var token = parseToken(cookie, SECRET_KEY);
      if (token != null) {
        return token;
      }
    }
    return null;
  }

  public Jws<Claims> getAccessToken(HttpServletRequest req) {
    var code = req.getHeader(X_AUTH_TOKEN_ACCESS);
    if (!stringUtil.isStringNullOrWhitespace(code)) {
      var token = parseToken(code, ACCESS_KEY);
      if (token != null) {
        return token;
      }
    }
    return null;
  }

  private String getCookie(HttpServletRequest req, String key) {
    if (req.getCookies() == null) {
      return null;
    }
    for (var c : req.getCookies()) {
      if (key.equals(c.getName())) {
        return c.getValue();
      }
    }
    return null;
  }

  private Cookie createCookie(String token, String key, int expire) {
    Cookie cookie = new Cookie(key, token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(expire);
    return cookie;
  }
}