package com.nowonbun.household.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.google.gson.Gson;
import com.nowonbun.household.AbstractTest;
import com.nowonbun.household.auth.JwtProvider;
import com.nowonbun.household.dao.DatabaseConfig;

@SpringBootTest(properties = { "spring.jwt.secret=secret1", "spring.jwt.access=access1", "spring.datasource.url=jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul" })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration({ JwtProvider.class, DatabaseConfig.class })
@AutoConfigureMockMvc
public class AuthControllerTest extends AbstractTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  // https://shinsunyoung.tistory.com/52
  @Test
  public void loginTest() throws Exception {

    var map = new HashMap<String, String>();
    map.put("id", "nowonbun");

    String content = gson.toJson(map);

    var req = post("/auth/login.auth").content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(req).andExpect(status().isOk())

        .andDo(print()).andDo(log());

  }
}
