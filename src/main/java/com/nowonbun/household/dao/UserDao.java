package com.nowonbun.household.dao;

import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.model.User;

public class UserDao extends AbstractDao<User> {
  public UserDao() {
    super(User.class);
  }
}
