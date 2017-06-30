package com.asgab.service.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.CurrencyType;
import com.asgab.repository.CurrencyTypeMapper;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class CurrencyTypeService {

  @Autowired
  private CurrencyTypeMapper currencyTypeMapper;

  public List<CurrencyType> getList(Map<String, Object> searchMap) {
    return currencyTypeMapper.search(searchMap);
  }

  public List<SelectMapper> getOptions(Map<String, Object> searchMap) {

    List<SelectMapper> options = new ArrayList<SelectMapper>();
    List<CurrencyType> list = currencyTypeMapper.search(searchMap);

    for (CurrencyType ct : list) {
      options.add(new SelectMapper(String.valueOf(ct.getId()), ct.getName()));
    }

    return options;
  }

  public CurrencyType getCurrencyTypeById(Long id) {
    List<CurrencyType> list = getList(null);
    if (list != null && list.size() > 0) {
      for (CurrencyType currencyType : list) {
        if (id.equals(currencyType.getId())) {
          return currencyType;
        }
      }
    }
    return null;
  }
}
