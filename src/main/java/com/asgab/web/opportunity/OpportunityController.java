package com.asgab.web.opportunity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asgab.core.pagination.Page;
import com.asgab.entity.Opportunity;
import com.asgab.service.opportunity.OpportunityService;
import com.asgab.util.CommonUtil;
import com.asgab.util.Servlets;

@Controller
@RequestMapping(value = "/opportunity")
public class OpportunityController {
  private static final String PAGE_SIZE = "10";

  @Autowired
  private OpportunityService opportunityService;

  @RequestMapping(method = RequestMethod.GET)
  public String list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = PAGE_SIZE) int pageSize, @RequestParam(value = "sort", defaultValue = "id desc") String sort,
      ServletRequest request, Model model) {
    Map<String, Object> params = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(request.getParameter("task"))) {
      params.put("task", request.getParameter("task"));
    }

    model.addAttribute("search", Servlets.encodeParameterString(params));
    params.put("sort", sort);
    Page<Opportunity> page = new Page<Opportunity>(pageNumber, pageSize, sort, params);
    Page<Opportunity> pages = opportunityService.search(page);
    model.addAttribute("pages", pages);
    return "opportunity/opportunityList";
  }

  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String toCreate(Model model, HttpServletRequest request) {
    model.addAttribute("opportunity", new Opportunity());
    model.addAttribute("action", "create");
    return "opportunity/opportunityForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(Opportunity opportunity, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    opportunityService.save(opportunity);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.create.success"));
    return "redirect:/opportunity";
  }

  @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
  public String view(@PathVariable("id") Long id, Model model) {
    model.addAttribute("opportunity", opportunityService.get(id));
    return "opportunity/opportunityView";
  }
  
  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String toUpdate(@PathVariable("id") Long id, Model model) {
    model.addAttribute("opportunity", opportunityService.get(id));
    model.addAttribute("action", "update");
    return "opportunity/opportunityForm";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("opportunity") Opportunity opportunity, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    opportunityService.update(opportunity);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.update.success"));
    return "redirect:/opportunity";
  }

  @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
  public String delete(@PathVariable("id") Long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    opportunityService.delete(id);
    redirectAttributes.addFlashAttribute("message", CommonUtil.getProperty(request, "message.delete.success"));
    return "redirect:/opportunity";
  }

  @ModelAttribute
  public void getCustMaster(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("opportunity", opportunityService.get(id));
    }
  }

}
