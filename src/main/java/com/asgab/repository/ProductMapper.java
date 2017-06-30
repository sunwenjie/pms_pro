package com.asgab.repository;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.Product;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ProductMapper {

  List<Product> search(Map<String, Object> parameters);

  List<Product> search(Map<String, Object> parameters, RowBounds rowBounds);

  Product get(Long id);

}
