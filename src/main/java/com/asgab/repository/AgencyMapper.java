package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.Agency;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface AgencyMapper {

  Agency get(Long id);

  List<Agency> search(Map<String, Object> parameters);

  List<Agency> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(Agency agency);

  void update(Agency agency);

  void delete(Long id);

  int count(Map<String, Object> map);

}
