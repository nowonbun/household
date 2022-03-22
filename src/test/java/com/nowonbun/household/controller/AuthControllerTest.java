package com.nowonbun.household.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;
import com.nowonbun.household.AbstractTest;
import com.nowonbun.household.auth.JwtProvider;
import com.nowonbun.household.bean.UserBean;
import com.nowonbun.household.dao.UserDao;
import com.nowonbun.household.model.User;
import com.nowonbun.household.service.StringUtil;
import io.jsonwebtoken.Jwts;

@WebMvcTest(value = AuthController.class, properties = { "spring.jwt.secret=secret1", "spring.jwt.access=access1" })
@ImportAutoConfiguration({ JwtProvider.class, StringUtil.class })
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@AutoConfigureMockMvc
public class AuthControllerTest extends AbstractTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private StringUtil stringUtil;

  @MockBean(name = "UserDao")
  private UserDao userDao;

  private String SECRET_KEY = "";
  private String ACCESS_KEY = "";

  @BeforeEach
  public void setting() {
    SECRET_KEY = super.getField(jwtProvider, "SECRET_KEY");
    ACCESS_KEY = super.getField(jwtProvider, "ACCESS_KEY");
  }

  // https://shinsunyoung.tistory.com/52
  // https://jojoldu.tistory.com/226
  // https://velog.io/@sproutt/MockBean%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%9C-%ED%86%B5%ED%95%A9Controller%ED%85%8C%EC%8A%A4%ED%8A%B8
  @Test
  public void loginTest() throws Exception {

    User user = new User();
    user.setEmail("nowonbun@naver.com");
    user.setLastname("aaa");
    user.setFirstname("bbb");

    when(userDao.signOn("nowonbun@naver.com", stringUtil.md5("112"))).thenReturn(user);

    var map = new HashMap<String, String>();
    map.put("id", "nowonbun@naver.com");
    map.put("pw", "112");
    String content = gson.toJson(map);
    var reqb = post("/auth/login.auth").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    var ret = mockMvc.perform(reqb).andExpect(status().isOk()).andDo(print()).andDo(log()).andReturn();
    var cookie = ret.getResponse().getCookie("X-AUTH-TOKEN-REFRESH");
    assertEquals(cookie.getPath(), "/");
    assertEquals(cookie.getSecure(), true);
    var token = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(cookie.getValue());
    assertEquals(token.getBody().getId(), "nowonbun@naver.com");

    token = Jwts.parser().setSigningKey(ACCESS_KEY).parseClaimsJws(ret.getResponse().getHeader("X-AUTH-TOKEN-ACCESS"));
    UserBean bean = stringUtil.deserialize(token.getBody().getId());
    assertEquals(bean.getId(), "nowonbun@naver.com");
    assertEquals(bean.getName(), "aaa bbb");
  }

  @Test
  public void checkTest() throws Exception {
    // filter!
    var reqb = post("/auth/check.auth").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    var ret = mockMvc.perform(reqb).andExpect(status().isForbidden()).andDo(print()).andDo(log()).andReturn();

    jwtProvider.createRefreshToken("nowonbun", ret.getRequest(), ret.getResponse());
    reqb = post("/auth/check.auth").cookie(ret.getResponse().getCookies()).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    ret = mockMvc.perform(reqb).andExpect(status().isOk()).andDo(print()).andDo(log()).andReturn();
  }

  @Test
  public void logoutTest() throws Exception {
    var cookie = new Cookie("X-AUTH-TOKEN-REFRESH", jwtProvider.createToken("nowonbun", 1000 * 60 * 60 * 24 * 14, SECRET_KEY));
    cookie.setMaxAge(100000);

    var reqb = post("/auth/logout.auth").cookie(cookie).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    var ret = mockMvc.perform(reqb).andExpect(status().isOk()).andDo(print()).andDo(log()).andReturn();

    cookie = ret.getResponse().getCookie("X-AUTH-TOKEN-REFRESH");
    assertEquals(cookie.getSecure(), true);
    assertEquals(cookie.getPath(), "/");
    assertEquals(cookie.getMaxAge(), 0);
  }

  @Test
  public void refreshTest() throws Exception {
    User user = new User();
    user.setEmail("nowonbun@naver.com");
    user.setLastname("aaa");
    user.setFirstname("bbb");

    when(userDao.findOne("nowonbun@naver.com")).thenReturn(user);

    var reqb = post("/auth/refesh.auth").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    mockMvc.perform(reqb).andExpect(status().isForbidden()).andDo(print()).andDo(log()).andReturn();

    var cookie = new Cookie("X-AUTH-TOKEN-REFRESH", jwtProvider.createToken("nowonbun@naver.com", 1000 * 60 * 60 * 24 * 14, SECRET_KEY));
    cookie.setMaxAge(100000);

    reqb = post("/auth/refesh.auth").cookie(cookie).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    var ret = mockMvc.perform(reqb).andExpect(status().isOk()).andDo(print()).andDo(log()).andReturn();

    var token = Jwts.parser().setSigningKey(ACCESS_KEY).parseClaimsJws(ret.getResponse().getHeader("X-AUTH-TOKEN-ACCESS"));
    UserBean bean = stringUtil.deserialize(token.getBody().getId());
    assertEquals(bean.getId(), "nowonbun@naver.com");
    assertEquals(bean.getName(), "aaa bbb");

    reqb = post("/auth/refesh.auth").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    mockMvc.perform(reqb).andExpect(status().isForbidden()).andDo(print()).andDo(log()).andReturn();
  }
}
