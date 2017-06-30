package com.asgab.service.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.ClientShare;
import com.asgab.repository.ClientShareMapper;

@Component
@Transactional
public class ClientShareService {

  @Autowired
  private ClientShareMapper clientShareMapper;

  public List<ClientShare> getList(Map<String, Object> parameters) {
    return clientShareMapper.search(parameters);
  }

  public void save(ClientShare clientShare) {
    clientShareMapper.save(clientShare);
  }

  public void delete(long id) {
    clientShareMapper.delete(id);
  }
}
