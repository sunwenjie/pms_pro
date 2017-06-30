package com.asgab.service.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asgab.service.business.opportunity.BusinessOpportunityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
@ActiveProfiles("production")
public class BusinessOpportunityTest {
  @Autowired
  private BusinessOpportunityService businessOpportunityService;



  /**
   * delete orders from orders left join business_opportunity_orders on
   * business_opportunity_orders.order_id = orders.id where
   * business_opportunity_orders.business_opportunity_id = 100042 ;
   * 
   * delete from business_opportunity_orders where business_opportunity_id = 100042;
   * 
   * update business_opportunities set status=2 , progress=10 where id = 100042;
   */
  @Test
  public void testCreateOrder() {
    System.out.println(businessOpportunityService.createOrder(58L));
  }

}
