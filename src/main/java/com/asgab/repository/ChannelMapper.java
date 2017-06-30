package com.asgab.repository;

import org.apache.ibatis.annotations.Param;

import com.asgab.entity.Channel;
import com.asgab.entity.ChannelRebate;
import com.asgab.repository.mybatis.MyBatisRepository;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 */
@MyBatisRepository
public interface ChannelMapper {

  Channel get(Long id);
  
  void delete(Long id);

  List<Channel> search(Map<String, Object> parameters);
  
  void insertChannel(Channel channel);
  
  void updateChannel(Channel channel);
  
  String getAllUserIdsByChannel(Long id);
  
  void deleteUserChannelRelation(Long channelId);
  
  void addUserChannelRelation(@Param("channelId") Long channelId, @Param("users") List<Long> users);

  int countChannels(Map<String, Object> map);

  List<Channel> searchChannels(Map<String, Object> parameters, RowBounds rowBounds);

  List<Long> findCanSeeChannel(Map<String, Long> parameters);

  List<ChannelRebate> getRebatesByChannelId(Long channelId);
  
  void deleteRebates(Long channelId);
  
  void addRebates(List<ChannelRebate> list);

}
