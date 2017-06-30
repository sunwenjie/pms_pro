package com.asgab.web.agency;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asgab.core.pagination.Page;
import com.asgab.entity.Agency;
import com.asgab.service.agency.AgencyService;
import com.asgab.util.CommonUtil;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/agency")
public class AgencyController {
  // TODO
  private static final String PAGE_SIZE = "10";

  @Autowired
  private AgencyService agencyService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(
          value = "sort", defaultValue = "id desc") String sort, ServletRequest request, Model model) {
    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(request.getParameter("name"))) {
      params.put("name", request.getParameter("name"));
    }
    if (StringUtils.isNotBlank(request.getParameter("brand"))) {
      params.put("brand", request.getParameter("brand"));
    }
    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<Agency> page = new Page<Agency>(pageNumber, pageSize, sort, params);
    Page<Agency> pages = agencyService.search(page);
    model.addAttribute("pages", pages);
    return "client/clientList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model, HttpServletRequest request) {
    model.addAttribute("agency", new Agency());
    model.addAttribute("action", "create");
    return "client/clientForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(Agency agency, HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    agencyService.save(agency);
    redirectAttributes.addFlashAttribute("message",
        CommonUtil.getProperty(request, "message.create.success"));
    return "redirect:/client";
  }

  @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
  public String view(@PathVariable("id") Long id, Model model) {
    model.addAttribute("agency", agencyService.get(id));
    return "client/clientView";
  }

  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model) {
    model.addAttribute("agency", agencyService.get(id));
    model.addAttribute("action", "update");
    return "client/clientForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("agency") Agency agency, HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    agencyService.update(agency);
    redirectAttributes.addFlashAttribute("message",
        CommonUtil.getProperty(request, "message.update.success"));
    return "redirect:/client";
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, Model model, HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    agencyService.delete(id);
    redirectAttributes.addFlashAttribute("message",
        CommonUtil.getProperty(request, "message.delete.success"));
    return "redirect:/client";
  }

  @ModelAttribute
  public void getAgency(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("client", agencyService.get(id));
    }
  }

  @RequestMapping(value = "/agencys", method = RequestMethod.GET)
  @ResponseBody
  public String get_agencys(HttpServletResponse response) throws IOException {
    List<Agency> list = agencyService.getList(null);
    JSONArray jsonArray = new JSONArray();
    for (Agency o : list) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("id", o.getId());
      jsonObject.put("text", o.getChannel_name());
      jsonArray.add(jsonObject);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();
    return jsonArray.toString();
  }
}
