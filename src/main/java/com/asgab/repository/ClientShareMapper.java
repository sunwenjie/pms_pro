package com.asgab.repository;

import java.util.List;
import java.util.Map;

import com.asgab.entity.ClientShare;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ClientShareMapper {

  List<ClientShare> search(Map<String, Object> parameters);

  void save(ClientShare clientShare);

  void delete(long id);

  void deleteByClientId(long client_id);

}
