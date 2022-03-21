package com.nowonbun.household.dao;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.nowonbun.household.common.AbstractDao;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration(DatabaseConfig.class)
public class AccountDaoTest {
  @Autowired
  public AccountDao accountDao;

  @BeforeEach
  public void setDebug() {
    try {
      var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
      var field = AbstractDao.class.getDeclaredField("debug");
      field.setAccessible(true);
      field.set(accountDao, em);
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
      var em = (EntityManager) field.get(accountDao);
      em.getTransaction().rollback();
      em.close();
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
