package com.asgab.repository;

import java.util.List;
import java.util.Map;

import com.asgab.entity.CurrencyType;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface CurrencyTypeMapper {

  List<CurrencyType> search(Map<String, Object> parameters);

}
