package com.nowonbun.household.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;
import com.nowonbun.household.AbstractTest;
import com.nowonbun.household.auth.JwtProvider;
import com.nowonbun.household.bean.UserBean;
import com.nowonbun.household.dao.AccountTypeDao;
import com.nowonbun.household.dao.UserDao;
import com.nowonbun.household.service.StringUtil;
import io.jsonwebtoken.Jwts;

@WebMvcTest(value = AuthController.class, properties = { "spring.jwt.secret=secret1", "spring.jwt.access=access1" })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration({ JwtProvider.class, StringUtil.class })
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

    when(userDao.signOn("nowonbun", stringUtil.md5("112"))).thenReturn(true);
    
    var map = new HashMap<String, String>();
    map.put("id", "nowonbun");
    map.put("pw", "112");
    String content = gson.toJson(map);
    var req = post("/auth/login.auth").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    var res = mockMvc.perform(req).andExpect(status().isOk()).andDo(print()).andDo(log()).andReturn();
    var cookie = res.getResponse().getCookie("X-AUTH-TOKEN-REFRESH");
    assertEquals(cookie.getPath(), "/");
    assertEquals(cookie.getSecure(), true);
    var token = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(cookie.getValue());
    assertEquals(token.getBody().getId(), "nowonbun");

    token = Jwts.parser().setSigningKey(ACCESS_KEY).parseClaimsJws(res.getResponse().getHeader("X-AUTH-TOKEN-ACCESS"));
    UserBean bean = stringUtil.deserialize(token.getBody().getId());
    assertEquals(bean.getId(), "nowonbun");
  }
}
