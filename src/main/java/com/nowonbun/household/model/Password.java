package com.nowonbun.household.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery(name = "Password.findAll", query = "SELECT p FROM Password p")
public class Password implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int idx;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date")
  private Date createDate;

  private boolean isdelete;

  private String password;

  @ManyToOne
  @JoinColumn(name = "email")
  private User user;

  public Password() {
  }

  public int getIdx() {
    return this.idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public boolean Isdelete() {
    return this.isdelete;
  }

  public void setIsdelete(boolean isdelete) {
    this.isdelete = isdelete;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

}