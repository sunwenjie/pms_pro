package com.asgab.web.channel;

import com.asgab.entity.Channel;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.channel.ChannelService;
import com.asgab.service.log.LogService;
import com.asgab.util.CommonUtil;
import com.asgab.util.JsonMapper;
import com.asgab.util.SelectMapper;
import com.asgab.util.Servlets;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.pagination.Page;
import com.asgab.service.business.opportunity.BusinessOpportunityService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/channel")
public class ChannelController {
	private static final String PAGE_SIZE = "10";

	// Json工具类
	private final static JsonMapper jsonMapper = new JsonMapper();

	@Autowired
	private LogService logService;

	@Resource
	private ChannelService channelService;
	
	@Resource
	private BusinessOpportunityService businessOpportunityService;
	
	@Resource
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "id desc") String sort,
					   HttpServletRequest request, Model model) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNoneBlank(request.getParameterValues("name"))) {
			params.put("name", request.getParameter("name").trim());
		}
		if (StringUtils.isNoneBlank(request.getParameterValues("sales"))) {
			params.put("sales", request.getParameter("sales").trim());
		}
		String created_period = request.getParameter("created_period");
		if (StringUtils.isNotBlank(created_period) && created_period.contains(" - ") && created_period.split(" - ").length == 2) {
			params.put("created_period", created_period);
			params.put("createDateStart", created_period.split(" - ")[0] + " 00:00");
			params.put("createDateEnd", created_period.split(" - ")[1] + " 23:59");
		}
		model.addAttribute("search", Servlets.encodeParameterString(params));
		params.put("sort", sort);

		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser user = (ShiroUser) currentUser.getPrincipal();
		if (user != null && !currentUser.hasRole("admin") && !currentUser.hasRole("superAdmin")) {
            Map<String,Long> userIdMap = new HashMap<>();
			userIdMap.put("current_user_id",user.getId());
			List<Long> userIds = channelService.findCanSeeChannel(userIdMap);
			params.put("userIds", userIds);
		}
		Page<Channel> page = new Page<Channel>(pageNumber, pageSize, sort, params);
		Page<Channel> pages = channelService.searchChannels(page);
		model.addAttribute("pages", pages);
		return "channel/channelList";
	}
	
	@RequestMapping(value="/update/{id}", method = RequestMethod.GET)
	public String editChannel(@PathVariable long id, Model model){
		model.addAttribute("channel", channelService.get(id));
		model.addAttribute("sale_users", channelService.getSales(id));
		model.addAttribute("channelRebates", channelService.getRebatesByChannelId(id, true));
	    model.addAttribute("currencys", businessOpportunityService.getCurrencyMappers());
		return "channel/channelForm";
	}
	
	@RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
	public String deleteChannel(@PathVariable long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
		channelService.delete(id);
	    redirectAttributes.addFlashAttribute("alert_type", "alert-success");
	    redirectAttributes.addFlashAttribute("message_del", CommonUtil.getProperty(request, "message.delete.success"));
	    redirectAttributes.addFlashAttribute("success", true);
		return "redirect:/channel";
	}
	
	@RequestMapping(value="/view/{id}", method = RequestMethod.GET)
	public String viewChannel(@PathVariable long id, Model model){
		Channel channel = channelService.get(id);
		model.addAttribute("channel", channel);
		model.addAttribute("sale_users", channelService.getSales(id));
		model.addAttribute("channelRebates", channelService.getRebatesByChannelId(id, true));
		String currency = "";
		List<SelectMapper> mapps = businessOpportunityService.getCurrencyMappers();
		for (int i = 0; i < mapps.size(); i++){
		  if (mapps.get(i).getId().equals(String.valueOf(channel.getCurrency_id()))){
			currency = mapps.get(i).getValue();
		  }
		}
	    model.addAttribute("currency", currency);
		return "channel/channelView";
	}

	@RequestMapping(value="/create", method = RequestMethod.GET)
	public String createChannel(Model model){
		model.addAttribute("channel", new Channel());
		model.addAttribute("channelRebates", channelService.getRebatesByChannelId(null, true));
	    model.addAttribute("currencys", businessOpportunityService.getCurrencyMappers());
		return "channel/channelForm";
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String saveChannel(Channel channel, String sale_ids, String[] deliver_date, String[] nonuniform_rebate, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
		boolean isCreate = channel.getId() != null && channel.getId() > 0 ? false : true;
		try {
			channel = channelService.save(channel, sale_ids, deliver_date, nonuniform_rebate);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("channel", channel);
			try {
				model.addAttribute("channelRebates", channelService.generateRebateByRequest(channel, deliver_date, nonuniform_rebate));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			model.addAttribute("sale_users", accountService.findUsersByUserIds(channelService.string2LongList(sale_ids)));
			model.addAttribute("currencys", businessOpportunityService.getCurrencyMappers());
			model.addAttribute("success", "false");
			if ("repeat time".equals(e.getMessage())){
			  model.addAttribute("message_del", CommonUtil.getProperty(request, "channel.rebate.notice"));
			}
			return "channel/channelForm";
		}
		if (isCreate)
			redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.create.success"));
		else
			redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.update.success"));
	    redirectAttributes.addFlashAttribute("successId", String.format("AG%09d", channel.getId()));
		return "redirect:/channel";
	}
}
