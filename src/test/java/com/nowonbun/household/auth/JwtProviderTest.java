package com.nowonbun.household.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.nowonbun.household.AbstractTest;
import com.nowonbun.household.config.SecurityConfig;
import com.nowonbun.household.service.StringUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@WebMvcTest(value = JwtProvider.class, properties = { "spring.jwt.secret=secret1", "spring.jwt.access=access1" })
@ImportAutoConfiguration({ SecurityConfig.class, StringUtil.class })
public class JwtProviderTest extends AbstractTest {

  @Autowired
  private JwtProvider jwtProvider;

  private String SECRET_KEY = "";
  private String ACCESS_KEY = "";

  private MockHttpServletRequest req;
  private MockHttpServletResponse res;

  @BeforeEach
  public void setting() {
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    SECRET_KEY = super.getField(jwtProvider, "SECRET_KEY");
    ACCESS_KEY = super.getField(jwtProvider, "ACCESS_KEY");
  }

  @Test
  public void createRefreshTokenTest() {
    String token = jwtProvider.createRefreshToken("nowonbun", req, res);
    var cookie = res.getCookie("X-AUTH-TOKEN-REFRESH");
    assertEquals(cookie.getSecure(), true);
    assertEquals(cookie.getPath(), "/");
    assertEquals(cookie.getMaxAge(), (1000 * 60 * 60 * 24) * 14 / 1000);
    assertEquals(cookie.getValue(), token);

    var jwt = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    assertEquals(jwt.getBody().getId(), "nowonbun");
  }

  @Test
  public void createAccessTokenTest() {
    String token = jwtProvider.createAccessToken("nowonbun", req, res);
    String header = res.getHeader("X-AUTH-TOKEN-ACCESS");

    assertEquals(token, header);
    var jwt = Jwts.parser().setSigningKey(ACCESS_KEY).parseClaimsJws(token);
    assertEquals(jwt.getBody().getId(), "nowonbun");
  }

  @Test
  public void clearTokenTest() {
    var cookie = new Cookie("X-AUTH-TOKEN-REFRESH", "");
    cookie.setMaxAge(100000);
    req.setCookies(cookie);

    jwtProvider.clearToken(req, res);
    cookie = res.getCookie("X-AUTH-TOKEN-REFRESH");
    assertEquals(cookie.getSecure(), true);
    assertEquals(cookie.getPath(), "/");
    assertEquals(cookie.getMaxAge(), 0);
  }

  @Test
  public void createTokenTest() {
    var token = jwtProvider.createToken("nowonbun", 10000, SECRET_KEY, req);
    var jwt = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    assertEquals(jwt.getBody().getId(), "nowonbun");
  }

  @Test
  public void parseTokenTest() {
    var claims = Jwts.claims().setId("nowonbun");
    var data = new Date();
    var token = Jwts.builder().setClaims(claims).setIssuedAt(data).setExpiration(new Date(data.getTime() + (1000 * 10))).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    var jwt = jwtProvider.parseToken(token, SECRET_KEY);
    assertEquals(jwt.getBody().getId(), "nowonbun");
  }

  @Test
  public void getIdTest() {
    var claims = Jwts.claims().setId("nowonbun");
    var data = new Date();
    var token = Jwts.builder().setClaims(claims).setIssuedAt(data).setExpiration(new Date(data.getTime() + (1000 * 10))).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    var jwt = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    assertEquals(jwtProvider.getId(jwt), "nowonbun");
  }

  @Test
  public void getRefreshTokenTest() {
    var claims = Jwts.claims().setId("nowonbun");
    var data = new Date();
    var token = Jwts.builder().setClaims(claims).setIssuedAt(data).setExpiration(new Date(data.getTime() + (1000 * 10))).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

    var cookie = new Cookie("X-AUTH-TOKEN-REFRESH", token);
    cookie.setMaxAge(100000);
    req.setCookies(cookie);

    var jwt = jwtProvider.getRefreshToken(req);
    assertEquals(jwt.getBody().getId(), "nowonbun");
  }

  @Test
  public void getAccessTokenTest() {
    var claims = Jwts.claims().setId("nowonbun");
    var data = new Date();
    var token = Jwts.builder().setClaims(claims).setIssuedAt(data).setExpiration(new Date(data.getTime() + (1000 * 10))).signWith(SignatureAlgorithm.HS256, ACCESS_KEY).compact();
    req.addHeader("X-AUTH-TOKEN-ACCESS", token);

    var token2 = jwtProvider.getAccessToken(req);
    assertEquals(token2.getBody().getId(), "nowonbun");
  }

  @Test
  public void getCookieTest() {
    var cookie = new Cookie("X-AUTH-TOKEN-REFRESH", "test");
    cookie.setMaxAge(100000);
    req.setCookies(cookie);
    Class<?>[] params = new Class<?>[] { HttpServletRequest.class, String.class };
    Object[] args = new Object[] { req, "X-AUTH-TOKEN-REFRESH" };

    var val = executeMethod(jwtProvider, "getCookie", params, args);
    assertEquals(val, "test");
  }

  @Test
  public void createCookieTest() {
    Class<?>[] params = new Class<?>[] { String.class, String.class, int.class };
    Object[] args = new Object[] { "value", "key", 1000 };
    Cookie val = executeMethod(jwtProvider, "createCookie", params, args);
    assertEquals(val.getName(), "key");
    assertEquals(val.getValue(), "value");
  }
}
