package com.nowonbun.household.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.nowonbun.household.common.AbstractDao;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration(DatabaseConfig.class)
public class AccountTypeDaoTest {

  @Autowired
  public AccountTypeDao accountTypeDao;

  @BeforeEach
  public void setDebug() {
    try {
      var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
      var field = AbstractDao.class.getDeclaredField("debug");
      field.setAccessible(true);
      field.set(accountTypeDao, em);
      em.getTransaction().begin();
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @AfterEach
  public void rollback() {
    try {
      var field = AbstractDao.class.getDeclaredField("debug");
      field.setAccessible(true);
      var em = (EntityManager) field.get(accountTypeDao);
      em.getTransaction().rollback();
      em.close();
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

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