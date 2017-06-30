package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.ExchangeRate;
import com.asgab.repository.mybatis.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 */
@MyBatisRepository
public interface ExchangeRateMapper {

  ExchangeRate get(Long id);

  // 获取当前生效的
  List<ExchangeRate> search(Map<String, Object> parameters);

  List<ExchangeRate> search(Map<String, Object> parameters, RowBounds rowBounds);

  int count(Map<String, Object> parameters);

}
