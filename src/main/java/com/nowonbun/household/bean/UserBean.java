package com.nowonbun.household.bean;

import com.nowonbun.household.common.AbstractBean;

public class UserBean extends AbstractBean{
  private static final long serialVersionUID = 5821939809084552490L;
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
