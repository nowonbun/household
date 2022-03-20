package com.nowonbun.household.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the password database table.
 * 
 */
@Entity
@NamedQuery(name="Password.findAll", query="SELECT p FROM Password p")
public class Password implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idx;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	private byte isdelete;

	private String password;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="email")
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

	public byte getIsdelete() {
		return this.isdelete;
	}

	public void setIsdelete(byte isdelete) {
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