package com.asgab.service.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Log;
import com.asgab.repository.LogMapper;

@Component
@Transactional
public class LogService {

  @Autowired
  private LogMapper logMapper;

  public Log get(Long id) {
    return logMapper.get(id);
  }

  public void addLog(Log log) {
    logMapper.save(log);
  }

  public List<Log> getAllLog() {
    return (List<Log>) logMapper.search(null);
  }

  public Page<Log> getAllLog(Page<Log> page) {
    List<Log> list = logMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = logMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

}
