package com.nowonbun.household.common;

import javax.persistence.EntityManager;

import com.nowonbun.household.config.DatabaseConfig;

public abstract class AbstractDao<T> {
  private Class<T> clazz;
  private EntityManager debug = null;

  public interface EntityManagerRunable {
    void run(EntityManager em);
  }

  public interface EntityManagerCallable<V> {
    V run(EntityManager em);
  }

  protected AbstractDao(Class<T> clazz) {
    this.clazz = clazz;
  }

  protected final Class<T> getClazz() {
    return clazz;
  }

  public T findOne(Object id) {
    return transaction((em) -> {
      return em.find(clazz, id);
    });
  }

  public T create(T entity) {
    return create(entity, false);
  }

  public T create(T entity, boolean readonly) {
    return transaction((em) -> {
      em.persist(entity);
      return entity;
    }, readonly);
  }

  public T update(T entity) {
    return update(entity, false);
  }

  public T update(T entity, boolean readonly) {
    return transaction((em) -> {
      em.detach(entity);
      return em.merge(entity);
    }, readonly);
  }

  public void delete(T entity) {
    delete(entity, false);
  }

  public void delete(T entity, boolean readonly) {
    transaction((em) -> {
      em.detach(entity);
      em.remove(em.merge(entity));
    }, readonly);
  }

  public <V> V transaction(EntityManagerCallable<V> callable) {
    return transaction(callable, false);
  }

  public <V> V transaction(EntityManagerCallable<V> callable, boolean readonly) {
    if (this.debug != null) {
      return callable.run(this.debug);
    }
    var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    try {
      V ret = callable.run(em);
      if (readonly) {
        transaction.rollback();
      } else {
        transaction.commit();
      }
      return ret;
    } catch (Throwable e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw new RuntimeException(e);
    } finally {
      em.close();
    }
  }

  public void transaction(EntityManagerRunable runnable) {
    transaction(runnable, false);
  }

  public void transaction(EntityManagerRunable runnable, boolean readonly) {
    if (this.debug != null) {
      runnable.run(this.debug);
      return;
    }
    var em = DatabaseConfig.getInstance().getEntityManagerFactory().createEntityManager();
    var transaction = em.getTransaction();
    transaction.begin();
    try {
      runnable.run(em);
      if (readonly) {
        transaction.rollback();
      } else {
        transaction.commit();
      }
    } catch (Throwable e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw new RuntimeException(e);
    } finally {
      em.close();
    }
  }
}