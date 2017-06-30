package com.asgab.repository;

import java.util.Map;

import com.asgab.entity.ClientExamination;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface ClientExaminationMapper {

  ClientExamination getNewestProcess(Map<String, Object> parameters);

}
