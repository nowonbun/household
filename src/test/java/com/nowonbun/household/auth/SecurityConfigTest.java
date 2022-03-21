package com.nowonbun.household.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.nowonbun.household.AbstractTest;

@WebMvcTest(value = SecurityConfig.class, properties = { "spring.jwt.secret=secret1", "spring.jwt.access=access1" })
@ImportAutoConfiguration(JwtProvider.class)
public class SecurityConfigTest extends AbstractTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void test1() throws Exception {
    mockMvc.perform(get("/index.html")).andExpect(status().isOk());
  }

  @Test
  public void test2() throws Exception {
    mockMvc.perform(get("/favicon.ico")).andExpect(status().isOk());
  }

  @Test
  public void test3() throws Exception {
    mockMvc.perform(get("/manifest.json")).andExpect(status().isOk());
  }

  @Test
  public void test4() throws Exception {
    // mvc.perform(get("/static/css/test.css")).andExpect(status().isOk());
    mockMvc.perform(get("/static/css/test.css")).andExpect(status().isNotFound());
  }

  @Test
  public void test5() throws Exception {
    // mvc.perform(get("/static/js/test.js")).andExpect(status().isOk());
    mockMvc.perform(get("/static/js/test.js")).andExpect(status().isNotFound());
  }

  @Test
  public void test6() throws Exception {
    mockMvc.perform(get("/test/index.html")).andExpect(status().isForbidden());
  }
}
