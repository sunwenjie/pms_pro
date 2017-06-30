package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.Opportunity;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface OpportunityMapper {

  Opportunity get(Long id);

  List<Opportunity> search(Map<String, Object> parameters);

  List<Opportunity> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(Opportunity opportunity);

  void update(Opportunity opportunity);

  void delete(Long id);

  int count(Map<String, Object> map);

}
