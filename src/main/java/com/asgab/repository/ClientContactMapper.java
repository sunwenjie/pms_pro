package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.ClientContact;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ClientContactMapper {

  ClientContact get(Long id);

  List<ClientContact> search(Map<String, Object> parameters);

  List<ClientContact> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(ClientContact clientContact);

  void update(ClientContact clientContact);;

  void delete(Long id);

  int count(Map<String, Object> map);

  List<ClientContact> getClientContactsByClientIdList(List<Long> clientIds);

  void deleteByClientId(Long client_id);

}
