package com.asgab.service.opportunity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Opportunity;
import com.asgab.entity.ProgressBar;
import com.asgab.repository.OpportunityMapper;

@Component
@Transactional
public class OpportunityService {

  @Autowired
  private OpportunityMapper opportunityMapper;

  public Page<Opportunity> search(Page<Opportunity> page) {
    List<Opportunity> opportunitys = opportunityMapper.search(page.getSearchMap(), page.getRowBounds());
    for (int i = 0; i < opportunitys.size(); i++) {
      opportunitys.get(i).setProgressBar(new ProgressBar(opportunitys.get(i).getProgress()));
    }
    int count = opportunityMapper.count(page.getSearchMap());
    page.setContent(opportunitys);
    page.setTotal(count);
    return page;
  }

  public Opportunity get(Long id) {
    return opportunityMapper.get(id);
  }

  public void save(Opportunity opportunity) {
    opportunityMapper.save(opportunity);
  }

  public void update(Opportunity opportunity) {
    opportunityMapper.update(opportunity);
  }

  public void delete(Long id) {
    opportunityMapper.delete(id);
  }
}
