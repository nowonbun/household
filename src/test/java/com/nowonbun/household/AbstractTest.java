package com.nowonbun.household;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public abstract class AbstractTest {
  protected <T> T getField(Object obj, String fieldName) {
    return getField(obj.getClass(), obj, fieldName);
  }

  protected <T> T getField(Class<?> clz, Object obj, String fieldName) {
    try {
      var field = clz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(obj);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  protected <T> void setField(Object obj, String fieldName, T value) {
    setField(obj.getClass(), obj, fieldName, value);
  }

  protected <T> void setField(Class<?> clz, Object obj, String fieldName, T value) {
    try {
      var field = clz.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(obj, value);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  protected <T> T executeMethod(Object obj, String methodName, Class<?>[] params, Object[] args) {
    try {
      var method = obj.getClass().getDeclaredMethod(methodName, params);
      method.setAccessible(true);
      return (T) method.invoke(obj, args);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  protected <T> T executeMethod(Object obj, String methodName, Object... args) {
    Class<?>[] params = new Class<?>[args.length];
    for (int i = 0; i < args.length; i++) {
      params[i] = args[i].getClass();
    }
    return executeMethod(obj, methodName, params, args);
  }
}
