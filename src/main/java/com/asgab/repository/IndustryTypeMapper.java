package com.asgab.repository;

import java.util.List;
import java.util.Map;

import com.asgab.entity.IndustryType;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface IndustryTypeMapper {

  List<IndustryType> search(Map<String, Object> parameters);

}
