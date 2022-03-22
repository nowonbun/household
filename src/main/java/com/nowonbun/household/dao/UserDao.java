package com.nowonbun.household.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.model.User;

public class UserDao extends AbstractDao<User> {
  public UserDao() {
    super(User.class);
  }

  public User signOn(String id, String pw) {
    return transaction((em) -> {
      try {
        Query query = em.createQuery("SELECT u FROM User u inner join u.passwords p where u.email=:email and p.password=:pw and p.isdelete=false", User.class);
        query.setParameter("email", id);
        query.setParameter("pw", pw);
        return (User) query.getSingleResult();
      } catch (NoResultException | IndexOutOfBoundsException e) {
        return null;
      }
    });
  }
}
