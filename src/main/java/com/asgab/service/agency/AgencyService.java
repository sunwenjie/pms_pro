package com.asgab.service.agency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Agency;
import com.asgab.repository.AgencyMapper;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class AgencyService {

  @Autowired
  private AgencyMapper agencyMapper;

  public Page<Agency> search(Page<Agency> page) {
    List<Agency> agencys = agencyMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = agencyMapper.count(page.getSearchMap());
    page.setContent(agencys);
    page.setTotal(count);
    return page;
  }

  public Agency get(Long id) {
    return agencyMapper.get(id);
  }

  public void save(Agency agency) {
    agencyMapper.save(agency);
  }

  public void update(Agency agency) {
    agencyMapper.update(agency);
  }

  public void delete(Long id) {
    agencyMapper.delete(id);
  }

  public List<Agency> getList(Map<String, Object> searchMap) {
    return agencyMapper.search(searchMap);
  }

  public List<SelectMapper> getOptions(Map<String, Object> searchMap) {
    List<SelectMapper> options = new ArrayList<SelectMapper>();
    List<Agency> list = agencyMapper.search(searchMap);

    for (Agency a : list) {
      options.add(new SelectMapper(String.valueOf(a.getId()), a.getChannel_name()));
    }
    return options;
  }

}
