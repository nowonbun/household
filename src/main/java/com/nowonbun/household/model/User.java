package com.nowonbun.household.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String email;

  @Temporal(TemporalType.DATE)
  @Column(name = "create_date")
  private Date createDate;

  private String firstname;

  private String lastname;

  @OneToMany(mappedBy = "user")
  private List<Password> passwords;

  public User() {
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return this.lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public List<Password> getPasswords() {
    return this.passwords;
  }

  public void setPasswords(List<Password> passwords) {
    this.passwords = passwords;
  }

  public Password addPassword(Password password) {
    getPasswords().add(password);
    password.setUser(this);

    return password;
  }

  public Password removePassword(Password password) {
    getPasswords().remove(password);
    password.setUser(null);

    return password;
  }

}