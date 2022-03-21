package com.nowonbun.household.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration(DatabaseConfig.class)
public class AccountTypeDaoTest {

  // https://xlffm3.github.io/spring%20data/spring-db-initialization/
  @Autowired
  public AccountTypeDao accountTypeDao;

  @Test
  public void testCount() {
    int length = accountTypeDao.findAll().size();
    assertEquals(2, length);
  }

  @Test
  public void testPYMN() {
    var PYMN = accountTypeDao.PYMN();
    assertEquals(PYMN.getCode(), "PYMN");
    assertEquals(PYMN.getName(), "支出");
  }

  @Test
  public void testINCM() {
    var INCM = accountTypeDao.INCM();
    assertEquals(INCM.getCode(), "INCM");
    assertEquals(INCM.getName(), "収入");
  }

}
