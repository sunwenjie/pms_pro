package com.asgab.service.exchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asgab.entity.ExchangeRate;
import com.asgab.repository.ExchangeRateMapper;

// Spring Service Bean的标识.
@Component
@Transactional
public class ExchangeRateService {

  @Resource
  private ExchangeRateMapper exchangeRateMapper;

  /**
   * 获取所有货币转换为人民币的汇率
   * 
   * @return
   */
  public List<ExchangeRate> getCurrency2RMB() {
    Map<String, Object> exchangeRateMap = new HashMap<String, Object>();
    exchangeRateMap.put("currency", "RMB");
    List<ExchangeRate> exchangeRates = search(exchangeRateMap);
    // 港币
    exchangeRateMap.clear();
    exchangeRateMap.put("base_currency", "RMB");
    exchangeRateMap.put("currency", "HKD");
    exchangeRates.addAll(search(exchangeRateMap));
    return exchangeRates;
  }

  public List<ExchangeRate> getCurrencyRMB2Others() {
    Map<String, Object> exchangeRateMap = new HashMap<String, Object>();
    exchangeRateMap.put("base_currency", "RMB");
    return search(exchangeRateMap);
  }

  public JSONArray getCurrencyRMB2OthersJson() {
    JSONArray array = new JSONArray();
    JSONObject rmbObject = new JSONObject();
    rmbObject.put("id", "1");
    rmbObject.put("text", "RMB");
    array.add(rmbObject);
    List<ExchangeRate> rates = getCurrencyRMB2Others();
    if (rates != null && rates.size() > 0) {
      for (ExchangeRate rate : rates) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", rate.getRate());
        jsonObject.put("text", rate.getCurrency());
        array.add(jsonObject);
      }
    }
    return array;
  }

  public List<ExchangeRate> search(Map<String, Object> map) {
    return exchangeRateMapper.search(map);
  }

  public ExchangeRate getExchangeRate(Long id) {
    return exchangeRateMapper.get(id);
  }

}
