package com.asgab.repository.xmo;

import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.asgab.repository.mybatis.MyBatisRepositoryXMO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepositoryXMO
public interface UserXMOMapper {

  User get(Long id);
  
  List<User> getUserByEmail(String email);

  List<User> search(Map<String, Object> parameters);

  List<User> search(Map<String, Object> parameters, RowBounds rowBounds);

  void save(User user);

  void update(User user);

  void delete(Long id);

  int count(Map<String, Object> map);

  List<Group> findUserGroupByUserId(Map<String, Object> parameters);

  List<User> findUsersByGroupName(Map<String, Object> parameters);

  List<Group> findGroupByGroupName(String groupName);

  List<Long> findCanSeeGp();

  List<Long> canSeeChannel(Map<String,Object> parameters);

  List<User> findUsersByUserIds(List<Long> userIds);

  List<User> getAllStatusUsers(Map<String, Object> parameters, RowBounds rowBounds);

  List<User> getAllStatusUsers(Map<String, Object> parameters);
  
  int update_logged(Long id);
}
