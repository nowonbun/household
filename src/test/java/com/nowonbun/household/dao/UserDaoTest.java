package com.nowonbun.household.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;

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
import com.nowonbun.household.config.DatabaseConfig;
import com.nowonbun.household.model.Password;
import com.nowonbun.household.model.User;
import com.nowonbun.household.service.StringUtil;

@DataJpaTest(properties = { "spring.datasource.url=jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul" })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ImportAutoConfiguration({ DatabaseConfig.class, StringUtil.class })
public class UserDaoTest extends AbstractTest {
  @Autowired
  public UserDao userDao;

  @Autowired
  public StringUtil stringUtil;

  @BeforeEach
  public void setDebug() {
    var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
    super.setField(AbstractDao.class, userDao, "debug", em);
    em.getTransaction().begin();
  }

  @AfterEach
  public void rollback() {
    EntityManager em = super.getField(AbstractDao.class, userDao, "debug");
    em.getTransaction().rollback();
    em.close();
  }

  @Test
  public void signOnTest() {
    var user = new User();
    user.setEmail("nowonbun");
    user.setFirstname("name");
    user.setLastname("test");
    user.setCreateDate(new Date());
    user.setPasswords(new ArrayList<>());

    var pw = new Password();
    pw.setPassword(stringUtil.md5("111"));
    pw.setIsdelete(true);
    pw.setUser(user);
    user.getPasswords().add(pw);

    pw = new Password();
    pw.setPassword(stringUtil.md5("222"));
    pw.setIsdelete(false);
    pw.setUser(user);
    user.getPasswords().add(pw);

    userDao.create(user);

    var ret = userDao.signOn("nowonbun", stringUtil.md5("111"));
    assertEquals(ret, false);
    
    ret = userDao.signOn("nowonbun", stringUtil.md5("1111"));
    assertEquals(ret, false);
    
    ret = userDao.signOn("nowonbun", stringUtil.md5("222"));
    assertEquals(ret, true);
  }
}
