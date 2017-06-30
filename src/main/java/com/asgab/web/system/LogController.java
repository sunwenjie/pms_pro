package com.asgab.web.system;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Log;
import com.asgab.service.log.LogService;
import com.asgab.util.CommonUtil;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/log")
public class LogController {

  // 每页显示记录
  private static final String PAGE_SIZE = "10";

  @Autowired
  private LogService logService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,

  @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(
      value = "sort", defaultValue = "id desc") String sort, ServletRequest request, Model model) {

    Map<String, Object> params = new HashMap<>();
    params.put("sort", sort);
    if (StringUtils.isNotBlank(request.getParameter("pKey"))) {
      params.put("pKey", request.getParameter("pKey"));
    }
    if (StringUtils.isNotBlank(request.getParameter("module"))) {
      params.put("module", request.getParameter("module"));
    } else {
      params.put("module", "menu.advertiser");
    }

    Page<Log> page1 = new Page<>(pageNumber, pageSize, sort, params);
    model.addAttribute("pages1", logService.getAllLog(page1));
    model.addAttribute("search", Servlets.encodeParameterString(params));

    model.addAttribute("operateTypes_zh", CommonUtil.LOG_OPERATE_TYPE_ZH);
    model.addAttribute("operateTypes_en", CommonUtil.LOG_OPERATE_TYPE_EN);
    return "system/logList";
  }

}
