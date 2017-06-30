package com.asgab.service.setting;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.entity.DataSharing;
import com.asgab.entity.DataSharingDTO;
import com.asgab.entity.User;
import com.asgab.repository.DataSharingMapper;
import com.asgab.repository.xmo.UserXMOMapper;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.util.SelectMapper;

@Component
@Transactional
public class DataSharingService {

  @Autowired
  private DataSharingMapper dataSharingMapper;

  @Autowired
  private UserXMOMapper userXMOMapper;

  // 如果 parent_id assistant 都为空就不保存
  public void save(DataSharing dataSharing) {
    if (dataSharing.getParent_id() != null || dataSharing.getAssistant_id() != null || dataSharing.getCurrency_id() != null) {
      if (StringUtils.isBlank(dataSharing.getTeam_name())) {
        dataSharing.setTeam_name(null);
      }
      dataSharingMapper.save(dataSharing);
      updateShiroUser(dataSharing);
    }
  }

  public void update(DataSharing dataSharing) {
    // 如果 user_id parent_id assistant 都为空就不保存
    if (dataSharing.getParent_id() != null || dataSharing.getAssistant_id() != null || dataSharing.getCurrency_id() != null) {
      if (StringUtils.isBlank(dataSharing.getTeam_name())) {
        dataSharing.setTeam_name(null);
      }
      dataSharingMapper.update(dataSharing);
      updateShiroUser(dataSharing);
    } else {
      dataSharingMapper.delete(dataSharing.getId());
    }
  }

  public void updateShiroUser(DataSharing dataSharing) {
    ((ShiroUser) SecurityUtils.getSubject().getPrincipal()).currency_id = dataSharing.getCurrency_id();
  }

  public DataSharing get(Long id) {
    return dataSharingMapper.get(id);
  }

  public DataSharing getByUserId(Long user_id) {
    List<DataSharing> list = dataSharingMapper.getByUserId(user_id);
    if (list == null || list.size() == 0)
      return null;
    return list.get(0);
  }

  public List<User> queryChildrenDataSharing(Long parent_id) {
    return dataSharingMapper.queryChildrenDataSharing(parent_id);
  }

  public List<User> getUsersByParentId(Long parent_id) {
    return dataSharingMapper.getUsersByParentId(parent_id);
  }

  /**
   * 递归获取
   * 
   * @param parent_id
   * @return
   */
  public List<SelectMapper> getUserMppersByUserId(Long parent_id) {
    List<SelectMapper> userMappers = new ArrayList<SelectMapper>();
    List<User> users = dataSharingMapper.queryChildrenDataSharing(parent_id);
    for (User user : users) {
      userMappers.add(new SelectMapper(String.valueOf(user.getId()), user.getName()));
    }
    return userMappers;
  }

  /**
   * 递归获取 不不包含自己. 只向下一级
   * 
   * @param user_id
   * @return
   */
  public List<SelectMapper> getTeamMpperByUserId(Long user_id) {
    List<SelectMapper> teamMappers = new ArrayList<SelectMapper>();
    List<DataSharing> dataSharings = dataSharingMapper.getListByParentId(user_id);
    for (DataSharing data : dataSharings) {
      if (StringUtils.isNotBlank(data.getTeam_name())) {
        teamMappers.add(new SelectMapper(String.valueOf(data.getUser_id()), data.getTeam_name()));
      }
    }
    return teamMappers;
  }

  public List<SelectMapper> getAllSaleTeamMappers() {
    List<DataSharing> dataSharings = dataSharingMapper.getAllSaleTeams();
    List<SelectMapper> teamMappers = new ArrayList<SelectMapper>();
    for (DataSharing data : dataSharings) {
      teamMappers.add(new SelectMapper(String.valueOf(data.getUser_id()), data.getTeam_name()));
    }
    return teamMappers;
  }

  public List<User> getDirectShareUsers(Long parent_id) {
    return dataSharingMapper.getDirectShareUsers(parent_id);
  }

  public String getViewDataSharing(Long userId) {
    return dataSharingMapper.getViewDataSharing(userId);
  }

  public List<DataSharingDTO> getDataSharingDTOs() {
    return getDataSharingDTOs(0l);
  }

  public List<DataSharingDTO> getDataSharingDTOs(Long id) {
    List<DataSharingDTO> dtos = new ArrayList<DataSharingDTO>();

    List<DataSharing> roots = new ArrayList<DataSharing>();
    User user = userXMOMapper.get(id);
    if (user != null) {
      DataSharing newRoot = new DataSharing();
      newRoot.setUser_id(id);
      newRoot.setParent_id(0l);
      newRoot.setShowName(user.getName() + (StringUtils.isNotBlank(user.getEmail()) ? ("(" + user.getEmail() + ")") : ""));
      roots.add(newRoot);
    }

    for (DataSharing parentNode : roots) {
      if (parentNode != null) {
        dtos.add(fetchDataSharingDTO(parentNode));
      }
    }

    return dtos;
  }

  private DataSharingDTO fetchDataSharingDTO(DataSharing parentNode) {
    DataSharingDTO dto = new DataSharingDTO();
    dto.setParentNode(parentNode);

    List<DataSharing> chidrenDSs = dataSharingMapper.getListByParentId(parentNode.getUser_id());

    for (DataSharing chidren : chidrenDSs) {
      dto.getChidrenNodes().add(fetchDataSharingDTO(chidren));
    }

    return dto;
  }

  public List<DataSharing> getTeamByUserIds(List<Long> user_ids) {
    if (user_ids != null && user_ids.size() > 0) {
      return dataSharingMapper.getTeamByUserIds(user_ids);
    }
    return new ArrayList<DataSharing>();
  }

  public List<DataSharing> getDataSharingByUserId(Long parent_id) {
    return dataSharingMapper.getDataSharingByUserId(parent_id);
  }

  public List<DataSharing> getAllAssistants() {
    return dataSharingMapper.getAllAssistants();
  }

  public DataSharing getAssistant(Long assistant_id) {
    return dataSharingMapper.getAssistant(assistant_id);
  }

}
