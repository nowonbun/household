package com.nowonbun.household.dao;

import com.nowonbun.household.common.AbstractDao;
import com.nowonbun.household.model.Category;

public class CategoryDao extends AbstractDao<Category> {
  public CategoryDao() {
    super(Category.class);
  }
}
