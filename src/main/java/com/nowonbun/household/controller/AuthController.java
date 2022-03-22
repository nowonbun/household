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
import com.nowonbun.household.dao.UserDao;
import com.nowonbun.household.service.StringUtil;

@RestController
public class AuthController extends AbstractController {
  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  @Qualifier("UserDao")
  private UserDao userDao;

  @Autowired
  private StringUtil stringUtil;

  @PostMapping(value = "auth/login.auth")
  public void login(@RequestBody Map<String, String> params, Model model, HttpServletRequest req, HttpServletResponse res) {
    var user = userDao.signOn(params.get("id"), stringUtil.md5(params.get("pw")));
    if (user != null) {
      jwtProvider.createRefreshToken(params.get("id"), req, res);
      jwtProvider.createAccessToken(stringUtil.serialize(new UserBean(user)), req, res);
      res.setStatus(200);
      return;
    }
    jwtProvider.clearToken(req, res);
    res.setStatus(403);
    return;
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
    var user = userDao.findOne(id);
    if (user == null) {
      res.setStatus(403);
      return;
    }
    jwtProvider.createAccessToken(stringUtil.serialize(new UserBean(user)), req, res);
    res.setStatus(200);
  }

}
