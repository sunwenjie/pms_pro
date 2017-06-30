package com.asgab.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.ProductCategory;
import com.asgab.repository.ProductCategoryMapper;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class ProductCategoryService {

  @Autowired
  private ProductCategoryMapper productCategoryMapper;

  public ProductCategory get(Long id) {
    return productCategoryMapper.get(id);
  }

  public List<ProductCategory> getList() {
    return productCategoryMapper.getList();
  }

  public List<SelectMapper> getListMapper() {
    List<ProductCategory> list = getList();
    List<SelectMapper> mappers = new ArrayList<SelectMapper>();
    for (ProductCategory p : list) {
      mappers.add(new SelectMapper(String.valueOf(p.getId()), p.getValue()));
    }
    return mappers;
  }

  public List<ProductCategory> getByName(String name) {
    return productCategoryMapper.getByName(name);
  }

}
