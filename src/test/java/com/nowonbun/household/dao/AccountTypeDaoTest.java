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

import com.nowonbun.household.AbstractTest;
import com.nowonbun.household.common.AbstractDao;

@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul"
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration(DatabaseConfig.class)
public class AccountTypeDaoTest extends AbstractTest {

  @Autowired
  public AccountTypeDao accountTypeDao;

  @BeforeEach
  public void setDebug() {
    var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
    super.setField(AbstractDao.class, accountTypeDao, "debug", em);
    em.getTransaction().begin();
  }

  @AfterEach
  public void rollback() {
    EntityManager em = super.getField(AbstractDao.class, accountTypeDao, "debug");
    em.getTransaction().rollback();
    em.close();
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
