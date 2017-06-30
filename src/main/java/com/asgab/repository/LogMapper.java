package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.Log;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface LogMapper {

  Log get(Long id);

  List<Log> search(Map<String, Object> parameters);

  List<Log> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(Log log);

  void update(Log log);

  void delete(Long id);

  int count(Map<String, Object> map);
}
