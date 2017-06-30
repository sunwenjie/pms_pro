package com.asgab.repository;

import java.util.List;

import com.asgab.entity.ProductCategory;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ProductCategoryMapper {
  
  ProductCategory get(Long id);

  List<ProductCategory> getList();
  
  List<ProductCategory> getByName(String name);
  
}
