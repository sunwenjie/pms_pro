package com.asgab.service.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.ClientContact;
import com.asgab.repository.ClientContactMapper;

@Component
@Transactional
public class ClientContactService {

  @Autowired
  private ClientContactMapper clientContactMapper;

  public List<ClientContact> getList(Map<String, Object> searchMap) {
    return clientContactMapper.search(searchMap);
  }

  public void save(ClientContact clientContact) {
    clientContactMapper.save(clientContact);
  }

  public void delete(long id) {
    clientContactMapper.delete(id);
  }

  public List<ClientContact> getClientContactsByClientIdList(List<Long> clientIds) {
    return clientContactMapper.getClientContactsByClientIdList(clientIds);
  }
}
