package com.nowonbun.household.common;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class FactoryDao {
  private static FactoryDao instance = null;
  private final Map<Class<?>, AbstractDao<?>> flyweight;

  private FactoryDao() {
    flyweight = new HashMap<Class<?>, AbstractDao<?>>();
  }

  @SuppressWarnings("unchecked")
  public static <T> T getDao(Class<T> clz) {
    try {
      if (instance == null) {
        instance = new FactoryDao();
      }
      if (!instance.flyweight.containsKey(clz)) {
        Constructor<T> constructor = clz.getDeclaredConstructor();
        constructor.setAccessible(true);
        instance.flyweight.put(clz, (AbstractDao<?>) constructor.newInstance());
      }
      return (T) instance.flyweight.get(clz);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
