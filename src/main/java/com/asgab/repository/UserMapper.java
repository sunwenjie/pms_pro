package com.asgab.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.asgab.entity.User;
import com.asgab.repository.mybatis.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepository
public interface UserMapper {

  User get(Long id);

  List<User> search(Map<String, Object> parameters);

  List<User> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(User user);

  void update(User user);

  void delete(Long id);

  int count(Map<String, Object> map);

}
