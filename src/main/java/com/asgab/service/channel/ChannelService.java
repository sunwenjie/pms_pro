package com.asgab.service.channel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Channel;
import com.asgab.entity.ChannelRebate;
import com.asgab.entity.User;
import com.asgab.repository.ChannelMapper;
import com.asgab.service.account.AccountService;
import com.asgab.util.SelectMapper;

// Spring Service Bean的标识.
@Component
@Transactional
public class ChannelService {

  @Resource
  private ChannelMapper channelMapper;
  
  @Resource
  private AccountService accountService;

  public Channel get(Long id) {
    return channelMapper.get(id);
  }

  public void delete(Long id) {
	channelMapper.deleteRebates(id);
	channelMapper.deleteUserChannelRelation(id);
    channelMapper.delete(id);
  }

  public List<Channel> search(Map<String, Object> parameters) {
    return channelMapper.search(parameters);
  }

  public List<SelectMapper> getChannelMapper() {
    List<SelectMapper> mappers = new ArrayList<SelectMapper>();
    List<Channel> channels = search(null);
    for (Channel channel : channels) {
      mappers.add(new SelectMapper(String.valueOf(channel.getId()), channel.getChannel_name()));
    }
    return mappers;
  }
  
  public Channel save(Channel channel, String userIds, String[] deliverDates, String[] rebates) throws Exception{
	channel.setUpdated_at(new Date());
	if (channel.getId() == null || channel.getId() <= 0){
	  channel.setCreated_at(new Date());
	  channelMapper.insertChannel(channel);
	}
	else{
	  channelMapper.updateChannel(channel);
	}
	
	channelMapper.deleteUserChannelRelation(channel.getId());
	if (StringUtils.isNotBlank(userIds)){
	  List<Map<String, Long>> relations = new ArrayList<>();
	  List<Long> users = new ArrayList<>();
	  for(String s : userIds.split(",")){
		if (StringUtils.isNotBlank(s) && StringUtils.isNumeric(s)){
		  Map<String, Long> map = new HashMap<String, Long>();
		  map.put("channelId", channel.getId());
		  map.put("userId", Long.parseLong(s));
		  relations.add(map);
		  users.add(Long.parseLong(s));
		}
	  }
	  channelMapper.addUserChannelRelation(channel.getId(), users);
	}
	
	channelMapper.deleteRebates(channel.getId());
	List<ChannelRebate> channelRebates = new ArrayList<>();
	if (deliverDates.length > 0 && deliverDates.length == rebates.length){
	  channelRebates = generateRebateByRequest(channel, deliverDates, rebates);
	  for (int i = 0; i < channelRebates.size(); i++){
		for (int j = 0; j < channelRebates.size(); j++){
		  if (i == j)continue;
		  Long si = channelRebates.get(i).getStart_date().getTime();
		  Long ei = channelRebates.get(i).getEnd_date().getTime();
		  Long sj = channelRebates.get(j).getStart_date().getTime();
		  Long ej = channelRebates.get(j).getEnd_date().getTime();
		  if ((si < sj && sj < ei) || (si < ej && ej < ei) || (si.equals(sj) && ei.equals(ej))){
			throw new RuntimeException("repeat time");
		  }
		}
	  }
	  channelMapper.addRebates(channelRebates);
	}
	return channel;
  }
  
  public List<ChannelRebate> generateRebateByRequest(Channel channel, String[] deliverDates, String[] rebates) throws Exception{
	List<ChannelRebate> channelRebates = new ArrayList<>();
	if (deliverDates.length > 0 && deliverDates.length == rebates.length){
		for(int i = 0; i < deliverDates.length; i++){
			String[] array = deliverDates[i].split(" - ");
			ChannelRebate rebate = new ChannelRebate();
			if (array.length != 2)
				continue;
			rebate.setChannel_id(channel.getId());
			rebate.setRebate(new BigDecimal(rebates[i]));
			rebate.setStart_date(new SimpleDateFormat("yyyy-MM-dd").parse(array[0]));
			rebate.setEnd_date(new SimpleDateFormat("yyyy-MM-dd").parse(array[1]));
			channelRebates.add(rebate);
		}
	}
	return channelRebates;
  }

  public List<User> getSales(Long channelId) {
	List<User> cooperateSales = new ArrayList<>();
	String userIds = channelMapper.getAllUserIdsByChannel(channelId);
	if (StringUtils.isNotBlank(userIds)) {
	  cooperateSales = accountService.findUsersByUserIds(string2LongList(userIds));
	}
	return cooperateSales;
  }
  
  public List<Long> string2LongList(String userIds){
	List<Long> users = new ArrayList<>();
	for(String s : userIds.split(",")){
	  users.add(Long.parseLong(s));
	}
	return users;
  }

  public Page<Channel> searchChannels(Page<Channel> page) {
	List<Channel> channels = channelMapper.searchChannels(page.getSearchMap(), page.getRowBounds());
	int count = channelMapper.countChannels(page.getSearchMap());
	page.setContent(channels);
	page.setTotal(count);
	return page;
  }
  
  public List<ChannelRebate> getRebatesByChannelId(Long channelId, Boolean isLeastOne){
	List<ChannelRebate> rebates = new ArrayList<>();
	if (channelId != null && channelId > 0)
	  rebates = channelMapper.getRebatesByChannelId(channelId);
	if (rebates.size() == 0 && isLeastOne){
	  rebates.add(new ChannelRebate());
	}
	return rebates;
  }

  public  List<Long> findCanSeeChannel(Map<String, Long> parameters){
    List<Long> userIds = channelMapper.findCanSeeChannel(parameters);
    return userIds;
  }

}
