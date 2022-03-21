package com.nowonbun.household.dao;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nowonbun.household.AbstractTest;
import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.config.DatabaseConfig;

@DataJpaTest(properties = { 
    "spring.datasource.url=jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul" 
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration(DatabaseConfig.class)
public class AccountDaoTest extends AbstractTest {
  @Autowired
  public AccountDao accountDao;

  @BeforeEach
  public void setDebug() {
    var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
    super.setField(AbstractDao.class, accountDao, "debug", em);
    em.getTransaction().begin();
  }

  @AfterEach
  public void rollback() {
    EntityManager em = super.getField(AbstractDao.class, accountDao, "debug");
    em.getTransaction().rollback();
    em.close();
  }
}
