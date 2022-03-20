package com.nowonbun.household.dao;

import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.nowonbun.household.common.FactoryDao;

@Configuration
public class DatabaseConfig {
  @Autowired
  private EntityManagerFactory emf;
  private static DatabaseConfig instance;

  public DatabaseConfig() {
    DatabaseConfig.instance = this;
  }

  public static DatabaseConfig getInstance() {
    return DatabaseConfig.instance;
  }

  public EntityManagerFactory getEntityManagerFactory() {
    return this.emf;
  }

  @Bean(name = "AccountDao")
  public AccountDao getAccountDao() {
    return FactoryDao.getDao(AccountDao.class);
  }

  @Bean(name = "AccountTypeDao")
  public AccountTypeDao getAccountTypeDao() {
    return FactoryDao.getDao(AccountTypeDao.class);
  }

  @Bean(name = "CategoryDao")
  public CategoryDao getCategoryDao() {
    return FactoryDao.getDao(CategoryDao.class);
  }

  @Bean(name = "PasswordDao")
  public PasswordDao getPasswordDao() {
    return FactoryDao.getDao(PasswordDao.class);
  }

  @Bean(name = "UserDao")
  public UserDao getUserDao() {
    return FactoryDao.getDao(UserDao.class);
  }
}
