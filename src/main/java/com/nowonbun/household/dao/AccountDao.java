package com.nowonbun.household.dao;

import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.model.Account;

public class AccountDao extends AbstractDao<Account> {
  public AccountDao() {
    super(Account.class);
  }
}
