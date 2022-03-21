package com.nowonbun.household.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.nowonbun.household.auth.JwtProvider;
import com.nowonbun.household.bean.UserBean;
import com.nowonbun.household.common.AbstractController;
import com.nowonbun.household.common.Util;
import com.nowonbun.household.dao.AccountTypeDao;

@RestController
public class AuthController extends AbstractController {
  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  @Qualifier("AccountTypeDao")
  private AccountTypeDao accountTypeDao;

  @PostMapping(value = "auth/login.auth")
  public void login(@RequestBody Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) {
    jwtProvider.createRefreshToken(params.get("id"), req, res);
    var user = new UserBean();
    user.setId(params.get("id"));
    jwtProvider.createAccessToken(Util.convertSerializable(user), req, res);
    res.setStatus(200);
    // res.setStatus(403);
  }

  @PostMapping(value = "auth/check.auth")
  public long check(Model model, HttpServletRequest req, HttpServletResponse res) {
    var refresh = jwtProvider.getRefreshToken(req);
    if (refresh == null) {
      res.setStatus(403);
      return -1L;
    }
    res.setStatus(200);
    return refresh.getBody().getExpiration().getTime() - new Date().getTime();
  }

  @PostMapping(value = "auth/logout.auth")
  public void logout(Model model, HttpServletRequest req, HttpServletResponse res) {
    jwtProvider.clearToken(req, res);
  }

  @PostMapping(value = "auth/refesh.auth")
  public void refesh(Model model, HttpServletRequest req, HttpServletResponse res) {
    var refresh = jwtProvider.getRefreshToken(req);
    if (refresh == null) {
      res.setStatus(403);
      return;
    }
    var id = jwtProvider.getId(refresh);
    var user = new UserBean();
    user.setId(id);
    var tt = jwtProvider.createAccessToken(Util.convertSerializable(user), req, res);
    System.out.println(tt);
    res.setStatus(200);
  }

}
