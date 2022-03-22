package com.nowonbun.household.bean;

import com.nowonbun.household.common.AbstractBean;
import com.nowonbun.household.model.User;

public class UserBean extends AbstractBean {
  private static final long serialVersionUID = 5821939809084552490L;
  private String id;
  private String name;

  public UserBean() {

  }

  public UserBean(User user) {
    this.id = user.getEmail();
    this.name = user.getLastname() + " " + user.getFirstname();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
