package com.asgab.service.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.asgab.util.CommonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
@ActiveProfiles("production")
public class CommonUtilTest {
  @Test
  public void testCreateOrder() {
    System.out.println(CommonUtil.isProduct());
  }

}
