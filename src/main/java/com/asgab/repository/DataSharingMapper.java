package com.asgab.repository;

import java.util.List;

import com.asgab.entity.DataSharing;
import com.asgab.entity.User;
import com.asgab.repository.mybatis.MyBatisRepository;

@MyBatisRepository
public interface DataSharingMapper {

  void save(DataSharing dataSharing);

  void update(DataSharing dataSharing);
  
  void delete(Long id);

  DataSharing get(Long id);

  List<DataSharing> getByUserId(Long user_id);

  List<User> queryChildrenDataSharing(Long parent_id);

  List<User> getUsersByParentId(Long parent_id);

  List<User> getDirectShareUsers(Long parent_id);

  List<DataSharing> getListByParentId(Long parent_id);

  String getViewDataSharing(Long userId);

  List<DataSharing> getDataSharingByUserId(Long parent_id);

  List<DataSharing> getTeamByUserIds(List<Long> user_ids);

  List<DataSharing> getAllSaleTeams();
  
  List<DataSharing> getAllAssistants();
  
  DataSharing getAssistant(Long assistant_id);

}
