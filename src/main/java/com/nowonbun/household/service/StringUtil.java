package com.nowonbun.household.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("unchecked")
public class StringUtil {
  public boolean isStringNullOrWhitespace(String val) {
    if (val == null) {
      return true;
    }
    if (val.trim().length() < 1) {
      return true;
    }
    return false;
  }

  public <T> String serialize(T bean) {
    try (var baos = new ByteArrayOutputStream()) {
      try (var oos = new ObjectOutputStream(baos)) {
        oos.writeObject(bean);
        var data = baos.toByteArray();
        return Base64.getEncoder().encodeToString(data);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserialize(String code) {
    var data = Base64.getDecoder().decode(code);
    try (var bais = new ByteArrayInputStream(data)) {
      try (var ois = new ObjectInputStream(bais)) {
        Object objectMember = ois.readObject();
        return (T) objectMember;
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public String md5(String pwd) {
    try {
      var md = MessageDigest.getInstance("MD5");
      md.update(pwd.getBytes());
      var byteData = md.digest();
      var sb = new StringBuffer();
      for (int i = 0; i < byteData.length; i++) {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }
}
