package com.nowonbun.household.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.model.AccountType;

@SuppressWarnings("unchecked")
public class AccountTypeDao extends AbstractDao<AccountType> {
  private Map<String, AccountType> master = null;

  public AccountTypeDao() {
    super(AccountType.class);
    reload();
  }

  public void reload() {
    master = new HashMap<>();
    for (var entity : findAllbyActive()) {
      master.put(entity.getCode(), entity);
    }
  }

  public List<AccountType> findAll() {
    return transaction((em) -> {
      try {
        Query query = em.createQuery("SELECT a FROM AccountType a", AccountType.class);
        return (List<AccountType>) query.getResultList();
      } catch (NoResultException e) {
        return null;
      }
    });
  }

  public List<AccountType> findAllbyActive() {
    return transaction((em) -> {
      try {
        Query query = em.createQuery("SELECT a FROM AccountType a where a.isdelete = 0", AccountType.class);
        return (List<AccountType>) query.getResultList();
      } catch (NoResultException e) {
        return null;
      }
    });
  }

  public AccountType PYMN() {
    return master.get("PYMN");
  }

  public AccountType INCM() {
    return master.get("INCM");
  }
}
