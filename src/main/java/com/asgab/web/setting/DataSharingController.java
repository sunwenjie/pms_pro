package com.asgab.web.setting;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
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

import com.asgab.entity.DataSharing;
import com.asgab.entity.User;
import com.asgab.service.account.AccountService;
import com.asgab.service.account.ShiroDbRealm.ShiroUser;
import com.asgab.service.business.opportunity.BusinessOpportunityService;
import com.asgab.service.setting.DataSharingService;
import com.asgab.util.CommonUtil;

@Controller
@RequestMapping(value = "/setting")
public class DataSharingController {

  @Autowired
  private DataSharingService dataSharingService;

  @Autowired
  private AccountService accountService;
  
  @Autowired
  private BusinessOpportunityService businessOpportunityService;

  @RequestMapping(method = RequestMethod.GET)
  public String toSetting(Model model, HttpServletRequest request) {

    ShiroUser shiroUser = getCurrUser();
    if (shiroUser != null) {
      DataSharing currUserDataSharing = dataSharingService.getByUserId(shiroUser.id);

      if (currUserDataSharing == null) {
        model.addAttribute("action", "create");
        model.addAttribute("dataSharing", new DataSharing());
      } else {
        model.addAttribute("action", "update");
        model.addAttribute("dataSharing", currUserDataSharing);
        model.addAttribute("parentUser", accountService.get(currUserDataSharing.getParent_id()));
        if (currUserDataSharing.getAssistant_id() != null) {
          model.addAttribute("assistantUser", accountService.get(currUserDataSharing.getAssistant_id()));
        }
      }
      List<User> subUsers = dataSharingService.getDirectShareUsers(shiroUser.id);
      model.addAttribute("subUsers", subUsers);
      model.addAttribute("disabledUserIds", dataSharingService.getViewDataSharing(shiroUser.id));
    } else {
      model.addAttribute("action", "create");
      model.addAttribute("dataSharing", new DataSharing());
    }
    model.addAttribute("currencys", businessOpportunityService.getCurrencyMappers());

    return "setting/settingForm";
  }

  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(DataSharing dataSharing, HttpServletRequest request, RedirectAttributes ra) {
    dataSharing.setCreated_at(new Date());
    ShiroUser user = getCurrUser();
    if (user != null) {
      dataSharing.setUser_id(user.id);
      dataSharing.setCreated_by(user.id);
    }
    dataSharingService.save(dataSharing);
    ra.addFlashAttribute("message", CommonUtil.getProperty(request, "message.save.success"));
    return "redirect:/setting";
  }

  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@ModelAttribute("dataSharing") DataSharing dataSharing, HttpServletRequest request, RedirectAttributes ra) {
    dataSharingService.update(dataSharing);
    ra.addFlashAttribute("message", CommonUtil.getProperty(request, "message.save.success"));
    return "redirect:/setting";
  }

  @RequestMapping(value = "tree", method = RequestMethod.GET)
  public String tree(HttpServletRequest request) {
    request.setAttribute("users", accountService.getAllStatusUsers());
    return "dataSharing/tree";
  }



  @RequestMapping(value = "global", method = RequestMethod.GET)
  public String global(Model model, HttpServletRequest request) {
    model.addAttribute("users", accountService.getAllStatusUsers());
    return "setting/settingGlobal";
  }


  @RequestMapping(value = "getDatashare/{userId}", method = RequestMethod.GET)
  @ResponseBody
  public DataSharing getDatashare(@PathVariable("userId") String userId, HttpServletRequest request) {
    if (StringUtils.isBlank(userId) || "null".equals(userId)) {
      return new DataSharing();
    }
    DataSharing dataSharing = dataSharingService.getByUserId(Long.valueOf(userId));
    return dataSharing == null ? new DataSharing() : dataSharing;
  }

  @RequiresRoles("superAdmin")
  @RequestMapping(value = "save", method = RequestMethod.POST)
  public String save(@ModelAttribute("dataSharing") DataSharing dataSharing, HttpServletRequest request, RedirectAttributes ra) {
    DataSharing ds = dataSharingService.getByUserId(dataSharing.getUser_id());
    if (ds == null) {
      dataSharingService.save(dataSharing);
    } else {
      dataSharing.setId(ds.getId());
      dataSharingService.update(dataSharing);
    }
    ra.addFlashAttribute("message", CommonUtil.getProperty(request, "message.save.success"));
    return "redirect:/setting/global";
  }


  @ModelAttribute
  public void getClient(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
    if (id != -1) {
      model.addAttribute("dataSharing", dataSharingService.get(id));
    }
  }

  private ShiroUser getCurrUser() {
    Subject currentUser = SecurityUtils.getSubject();
    if (null == currentUser)
      return null;
    return (ShiroUser) currentUser.getPrincipal();
  }
}
