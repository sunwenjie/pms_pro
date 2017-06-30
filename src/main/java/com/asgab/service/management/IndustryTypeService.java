package com.asgab.service.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.IndustryType;
import com.asgab.repository.IndustryTypeMapper;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class IndustryTypeService {

  @Autowired
  private IndustryTypeMapper industryTypeMapper;

  public List<IndustryType> getList(Map<String, Object> searchMap) {
    return industryTypeMapper.search(searchMap);
  }

  public List<SelectMapper> getOptions(Map<String, Object> searchMap) {
    List<SelectMapper> options = new ArrayList<SelectMapper>();
    List<IndustryType> list = industryTypeMapper.search(searchMap);
    for (IndustryType it : list) {
      options.add(new SelectMapper(it.getId().toString(), "zh".equalsIgnoreCase(String
          .valueOf(searchMap.get("lang"))) ? it.getName() : it.getName_en()));
    }
    return options;
  }
}
