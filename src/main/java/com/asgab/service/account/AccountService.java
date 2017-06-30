package com.asgab.service.account;

import com.asgab.core.pagination.Page;
import com.asgab.entity.DataSharing;
import com.asgab.entity.Group;
import com.asgab.entity.User;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.setting.DataSharingService;
import com.asgab.util.Digests;
import com.asgab.util.Encodes;
import com.asgab.util.SelectMapper;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理类.
 * 
 * @author calvin
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService {

  public static final String HASH_ALGORITHM = "SHA-1";
  public static final int HASH_INTERATIONS = 1024;

  @Resource
  private UserXMOMapper userXMOMapper;
  
  @Resource
  private DataSharingService dataSharingService;

  public Page<User> getAllUser(Page<User> page) {
    List<User> list = userXMOMapper.search(page.getSearchMap(), page.getRowBounds());
    int count = userXMOMapper.count(page.getSearchMap());
    page.setContent(list);
    page.setTotal(count);
    return page;
  }

  public User findUserByEmail(String email) {
    List<User> users = userXMOMapper.getUserByEmail(email);
    if (users != null && users.size() > 0) {

      if (users.size() == 1) {
        return users.get(0);
      } else {
        for (User user : users) {
          if (email.equals(user.getEmail())) {
            return user;
          }
        }
      }
    }
    return null;
  }

  /*
   * public User findUserByLoginName(String loginName) { Map<String, Object> map =
   * Maps.newHashMap(); map.put("loginName", loginName); List<User> users =
   * userXMOMapper.search(map); if (users != null && users.size() > 0) {
   * 
   * if (users.size() == 1) { return users.get(0); } else { for (User user : users) { if
   * (loginName.equals(user.getLoginName())) { return user; } } } } return null; }
   */



  public List<Group> findUserGroupByUserId(Long userId) {
    Map<String, Object> map = Maps.newHashMap();
    map.put("userId", userId);
    return userXMOMapper.findUserGroupByUserId(map);
  }

  public List<Long> canSeeChannel(Long userId){
    Map<String, Object> map = Maps.newHashMap();
    map.put("userId", userId);
    return userXMOMapper.canSeeChannel(map);
  }

  public User get(Long id) {
    return userXMOMapper.get(id);
  }

  public static void main(String[] args) {
    byte[] hashPassword = Digests.sha1("0123456789".getBytes());

    System.out.println(Encodes.encodeHex(hashPassword));
  }

  public List<User> getAllXMOUser() {
    HashMap<String, Object> searchMap = new HashMap<String, Object>();
    searchMap.put("sort", "name asc");
    return (List<User>) userXMOMapper.search(searchMap);
  }

  public List<Long> findCanSeeGp() {
    return (List<Long>) userXMOMapper.findCanSeeGp();
  }

  public List<User> findUsersByUserIds(List<Long> userIds) {
    if (userIds != null && userIds.size() > 0) {
      return userXMOMapper.findUsersByUserIds(userIds);
    }
    return new ArrayList<User>();
  }

  /**
   * 获取所有用户, 下拉框 包含所有状态的 报表查看销售代表用. 因为有些人离职了但是还是有数据的
   * 
   * @return
   */
  public List<SelectMapper> getAllStatusUserMappers() {
    List<User> users = userXMOMapper.getAllStatusUsers(null);
    List<SelectMapper> userMappers = new ArrayList<SelectMapper>();
    for (User user : users) {
      userMappers.add(new SelectMapper(String.valueOf(user.getId()), user.getName()));
    }
    return userMappers;
  }

  public Page<User> getUsersForSetting(Page<User> page) {
    List<User> list = userXMOMapper.getAllStatusUsers(page.getSearchMap());
    page.setContent(list);
    return page;
  }

  public List<User> getAllStatusUsers() {
    return userXMOMapper.getAllStatusUsers(null);
  }

  public DataSharing getAssistant(Long assistant_id){
    return dataSharingService.getAssistant(assistant_id);
  }
  
  public DataSharing getByUserId(Long user_id){
    return dataSharingService.getByUserId(user_id);
  }
  
  public int update_logged(Long id) {
    return userXMOMapper.update_logged(id);
  }
}
