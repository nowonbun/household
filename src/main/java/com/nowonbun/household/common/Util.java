package com.nowonbun.household.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class Util {
  public static boolean isStringNullOrWhitespace(String val) {
    if (val == null) {
      return true;
    }
    if (val.trim().length() < 1) {
      return true;
    }
    return false;
  }

  public static <T> String convertSerializable(T bean) {
    try (var baos = new ByteArrayOutputStream()) {
      try (var oos = new ObjectOutputStream(baos)) {
        oos.writeObject(bean);
        var data = baos.toByteArray();
        return Base64.getEncoder().encodeToString(data);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T convertData(String code) {
    var data = Base64.getDecoder().decode(code);
    try (var bais = new ByteArrayInputStream(data)) {
      try (var ois = new ObjectInputStream(bais)) {
        Object objectMember = ois.readObject();
        return (T) objectMember;
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }
}
