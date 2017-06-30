package com.asgab.service.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.ClientExamination;
import com.asgab.repository.ClientExaminationMapper;

@Component
@Transactional
public class ClientExaminationService {

  @Autowired
  private ClientExaminationMapper clientExaminationMapper;


  public ClientExamination getNewestProcess(Map<String, Object> parameters) {
    return clientExaminationMapper.getNewestProcess(parameters);
  }
}
