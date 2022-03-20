package com.nowonbun.household.dao;

import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.model.Password;

public class PasswordDao extends AbstractDao<Password> {
  public PasswordDao() {
    super(Password.class);
  }
}
